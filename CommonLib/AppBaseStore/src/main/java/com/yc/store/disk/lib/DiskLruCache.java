package com.yc.store.disk.lib;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/8/6
 *     desc  : 磁盘缓存类
 *     revise:
 * </pre>
 */
public final class DiskLruCache implements Closeable {


    /**
     * libcore.io.DiskLruCache
     * 1
     * 1
     * 1
     *
     * DIRTY 27c7e00adbacc71dc793e5e7bf02f861
     * CLEAN 27c7e00adbacc71dc793e5e7bf02f861 1208
     * READ 27c7e00adbacc71dc793e5e7bf02f861
     * DIRTY b80f9eec4b616dc6682c7fa8bas2061f
     * CLEAN b80f9eec4b616dc6682c7fa8bas2061f 1208
     * READ b80f9eec4b616dc6682c7fa8bas2061f
     * DIRTY be3fgac81c12a08e89088555d85dfd2b
     * CLEAN be3fgac81c12a08e89088555d85dfd2b 99
     * READ be3fgac81c12a08e89088555d85dfd2b
     * DIRTY 536990f4dbddfghcfbb8f350a941wsxd
     * REMOVE 536990f4dbddfghcfbb8f350a941wsxd
     *
     * 第1行：libcore.io.DiskLruCache 是固定字符串，表明使用的是 DiskLruCache 技术；
     * 第2行：DiskLruCache 的版本号，源码中为常量 1；
     * 第3行：APP 的版本号，即我们在 open() 方法里传入的版本号；
     * 第4行：valueCount，这个值也是在 open() 方法中传入的，指每个 key 对应几个文件，通常情况下都为 1；
     * 第5行：空行
     *
     *
     */

    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String MAGIC = "libcore.io.DiskLruCache";
    static final String VERSION_1 = "1";
    static final long ANY_SEQUENCE_NUMBER = -1;
    /**
     * 该状态表示一个缓存Entry已经被成功发布了并且可以读取，该行后面会有每个Value的大小。
     */
    private static final String CLEAN = "CLEAN";
    /**
     * 该状态表示一个Entry正在被创建或正在被更新，任意一个成功的DIRTY操作后面都会有一个CLEAN或REMOVE操作。
     * 如果一个DIRTY操作后面没有CLEAN或者REMOVE操作，那就表示这是一个临时文件，应该将其删除。
     */
    private static final String DIRTY = "DIRTY";
    /**
     * 表示被删除的缓存Entry。
     */
    private static final String REMOVE = "REMOVE";
    /**
     * 在LRU缓存中被读取了。
     */
    private static final String READ = "READ";

    private final File directory;
    private final File journalFile;
    private final File journalFileTmp;
    private final File journalFileBackup;
    private final int appVersion;
    private long maxSize;
    private final int valueCount;
    private long size = 0;
    private Writer journalWriter;
    private final LinkedHashMap<String, DiskLruEntry> lruEntries =
            new LinkedHashMap<String, DiskLruEntry>(0, 0.75f, true);
    /**
     * 记录用户操作的次数，每执行一次写入、读取或移除缓存的操作，这个变量值都会加 1
     * 当变量值达到 2000 时就会触发重构 journal 的事件，这时会自动把 journal 中多余的、不必要的记录全部清除掉，
     * 保证 journal 文件的大小始终保持在一个合理的范围内。
     */
    private int redundantOpCount;

    /**
     * 为了区分旧快照和当前快照，每次提交编辑时都给每个条目一个序号。如果快照的序列号不等于其条目的序列号，则快照失效。
     */
    private long nextSequenceNumber = 0;

    /**
     * 该缓存使用单个后台线程来清除条目
     */
    final ThreadPoolExecutor executorService = new ThreadPoolExecutor(
            0, 1, 60L,
                    TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
                    new DiskThreadFactory());

    /**
     * 创建清除callable
     */
    private final Callable<Void> cleanupCallable = new Callable<Void>() {
        @Override
        public Void call() throws Exception {
            synchronized (DiskLruCache.this) {
                if (journalWriter == null) {
                    return null; // Closed.
                }
                trimToSize();
                if (journalRebuildRequired()) {
                    //创建一个新的日志，删除多余的信息
                    rebuildJournal();
                    redundantOpCount = 0;
                }
            }
            return null;
        }
    };

    private DiskLruCache(File directory, int appVersion, int valueCount, long maxSize) {
        this.directory = directory;
        this.appVersion = appVersion;
        this.journalFile = new File(directory, JOURNAL_FILE);
        this.journalFileTmp = new File(directory, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(directory, JOURNAL_FILE_BACKUP);
        this.valueCount = valueCount;
        this.maxSize = maxSize;
    }

    /**
     * Opens the cache in {@code directory}, creating a cache if none exists
     * there.
     * 第一个参数表示磁盘缓存在文件系统中的存储路径。
     * 第二个参数表示应用的版本号，一般设为 1 即可。当版本号发生改变时 DiskLruCache 会清空之前所有的缓存文件，
     *      而这个特性在实际开发中作用并不大，很多情况下即使应用的版本号发生了改变缓存文件却仍然是有效的，因此这个参数设为 1 比较好。
     * 第三个参数表示同一个 key 可以对应多少个缓存文件，一般设为 1 即可。
     * 第四个参数表示缓存的总大小，比如 50MB，当缓存大小超出这个设定值后，DiskLruCache 会清除一些缓存从而保证总大小不大于这个设定值。
     * @param directory  a writable directory
     * @param valueCount the number of values per cache entry. Must be positive.
     * @param maxSize    the maximum number of bytes this cache should use to store
     * @throws IOException if reading or writing the cache directory fails
     */
    public static DiskLruCache open(File directory, int appVersion, int valueCount, long maxSize)
            throws IOException {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (valueCount <= 0) {
            throw new IllegalArgumentException("valueCount <= 0");
        }

        // 如果存在备份日志文件，则使用它
        File backupFile = new File(directory, JOURNAL_FILE_BACKUP);
        if (backupFile.exists()) {
            File journalFile = new File(directory, JOURNAL_FILE);
            // 如果存在正式的日志文件，则将备份日志文件删除
            if (journalFile.exists()) {
                backupFile.delete();
            } else {
                renameTo(backupFile, journalFile, false);
            }
        }

        // 首先尝试读取日志文件
        DiskLruCache cache = new DiskLruCache(directory, appVersion, valueCount, maxSize);
        if (cache.journalFile.exists()) {
            try {
                //日志文件的读取过程
                cache.readJournal();
                //用于统计缓存文件的总体大小，并删除脏文件
                cache.processJournal();
                return cache;
            } catch (IOException journalIsCorrupt) {
                System.out
                        .println("DiskLruCache "
                                + directory
                                + " is corrupt: "
                                + journalIsCorrupt.getMessage()
                                + ", removing");
                cache.delete();
            }
        }

        // 此时日志文件不存在或读取出错，新建一个DiskLruCache实例
        directory.mkdirs();
        cache = new DiskLruCache(directory, appVersion, valueCount, maxSize);
        //创建一个新的日志，删除多余的信息
        cache.rebuildJournal();
        return cache;
    }

    /**
     * 日志文件的读取过程
     * readJournal()方法其实就是通过readJournalLine(reader.readLine())方法读取日志文件中的每一行，
     * 最终会读取到lruEntries中，lruEntries是DiskLruCache在内存中的表现形式。
     * @throws IOException                      异常
     */
    private void readJournal() throws IOException {
        StrictLineReader reader = new StrictLineReader(new FileInputStream(journalFile), DiskHelperUtils.US_ASCII);
        try {
            //读取journal文件的前五行
            String magic = reader.readLine();
            String version = reader.readLine();
            String appVersionString = reader.readLine();
            String valueCountString = reader.readLine();
            String blank = reader.readLine();
            if (!MAGIC.equals(magic)
                    || !VERSION_1.equals(version)
                    || !Integer.toString(appVersion).equals(appVersionString)
                    || !Integer.toString(valueCount).equals(valueCountString)
                    || !"".equals(blank)) {
                throw new IOException("unexpected journal header: [" + magic + ", " + version + ", "
                        + valueCountString + ", " + blank + "]");
            }

            int lineCount = 0;
            while (true) {
                try {
                    //该方法用于读取每一行日志
                    readJournalLine(reader.readLine());
                    lineCount++;
                } catch (EOFException endOfJournal) {
                    break;
                }
            }
            redundantOpCount = lineCount - lruEntries.size();

            // If we ended on a truncated line, rebuild the journal before appending to it.
            if (reader.hasUnterminatedLine()) {
                //创建一个新的日志，删除多余的信息
                rebuildJournal();
            } else {
                journalWriter = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(journalFile, true), DiskHelperUtils.US_ASCII));
            }
        } finally {
            DiskHelperUtils.closeQuietly(reader);
        }
    }

    /**
     * 该方法用于读取每一行日志。日志文件的每一行都是DIRTY、CLEAN、READ或REMOVE四种行为之一，那么该方法就需要对这4中情况分别处理。
     * 大概思路：
     * 1.首先取出该行记录的key，然后根据该记录是否为REMOVE进行不同的操作，如果是REMOVE，则将该key的缓存从lruEntries中移除。
     * 2.如果不是REMOVE，说明该key存在一个对应的缓存实体Entry，则先新建一个Entry并添加到lruEntries中。
     * 3.之后再判断日志的类型，如果日志是CLEAN，代表该文件已经保存完毕了，将currentEditor设置为null；
     * 4.如果日志是DIRTY，代表文件没有保存完毕，为其currentEditor新建一个Editor。
     *
     * 为什么要这么做呢？
     * 保存一个文件时会先写入DIRTY日志，保存成功后再写入CLEAN日志，一般来说这两条日志会成对出现。
     * 这里的currentEditor相当于一个标志位，如果为空，表示文件完整，如果不为空，表示该文件是临时文件。
     * @param line                              每行内容
     * @throws IOException                      io流异常
     */
    private void readJournalLine(String line) throws IOException {
        int firstSpace = line.indexOf(' ');
        if (firstSpace == -1) {
            throw new IOException("unexpected journal line: " + line);
        }

        int keyBegin = firstSpace + 1;
        //journal 日志每一行中的各个部分都是用 ' ' 空格来分割的，所以先用 空格来截取一下
        int secondSpace = line.indexOf(' ', keyBegin);
        final String key;
        if (secondSpace == -1) {
            //拿到 key
            key = line.substring(keyBegin);
            // 如果是REMOVE，则将该key代表的缓存从lruEntries中移除
            if (firstSpace == REMOVE.length() && line.startsWith(REMOVE)) {
                //然后判断 firstSpace 是 REMOVE 就会调用 lruEntries.remove(key)
                lruEntries.remove(key);
                return;
            }
        } else {
            //拿到 key
            key = line.substring(keyBegin, secondSpace);
        }

        //取出当前key对应的缓存Entry
        DiskLruEntry entry = lruEntries.get(key);
        if (entry == null) {
            //若不是 REMOVE ，如果该 key 没有加入到 lruEntries ，则创建并且加入
            entry = new DiskLruEntry(key,valueCount,directory);
            lruEntries.put(key, entry);
        }
        // 如果是CLEAN、DIRTY或READ
        if (secondSpace != -1 && firstSpace == CLEAN.length() && line.startsWith(CLEAN)) {
            //继续判断 firstSpace ，若是 CLEAN ，则初始化 entry ，设置 readable=true , currentEditor 为 null ，初始化长度等。
            String[] parts = line.substring(secondSpace + 1).split(" ");
            entry.readable = true;
            entry.currentEditor = null;
            entry.setLengths(parts);
        } else if (secondSpace == -1 && firstSpace == DIRTY.length() && line.startsWith(DIRTY)) {
            //若是 DIRTY ，则设置 currentEditor 对象。
            entry.currentEditor = new Editor(entry);
        } else if (secondSpace == -1 && firstSpace == READ.length() && line.startsWith(READ)) {
            // This work was already done by calling lruEntries.get().
            //若是 READ，无操作。
        } else {
            throw new IOException("unexpected journal line: " + line);
        }
    }

    /**
     * 计算初始大小并收集垃圾，作为打开缓存。假设脏条目是不一致的，将被删除。
     *
     */
    private void processJournal() throws IOException {
        deleteIfExists(journalFileTmp);
        for (Iterator<DiskLruEntry> i = lruEntries.values().iterator(); i.hasNext(); ) {
            DiskLruEntry entry = i.next();
            if (entry.currentEditor == null) {
                for (int t = 0; t < valueCount; t++) {
                    size += entry.lengths[t];
                }
            } else {
                entry.currentEditor = null;
                for (int t = 0; t < valueCount; t++) {
                    deleteIfExists(entry.getCleanFile(t));
                    deleteIfExists(entry.getDirtyFile(t));
                }
                i.remove();
            }
        }
    }

    /**
     * 创建一个新的日志，删除多余的信息。如果当前日志存在，则替换当前日志。
     */
    private synchronized void rebuildJournal() throws IOException {
        if (journalWriter != null) {
            journalWriter.close();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(journalFileTmp);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, DiskHelperUtils.US_ASCII);
        //高效字符流
        Writer writer = new BufferedWriter(outputStreamWriter);
        try {
            //这一块是写入头部的5行
            //libcore.io.DiskLruCache 是固定字符串，表明使用的是 DiskLruCache 技术；
            writer.write(MAGIC);
            writer.write("\n");
            //DiskLruCache 的版本号，源码中为常量 1；
            writer.write(VERSION_1);
            writer.write("\n");
            //APP 的版本号，即我们在 open() 方法里传入的版本号；
            writer.write(Integer.toString(appVersion));
            writer.write("\n");
            //valueCount，这个值也是在 open() 方法中传入的，指每个 key 对应几个文件，通常情况下都为 1；
            writer.write(Integer.toString(valueCount));
            writer.write("\n");
            //空行
            writer.write("\n");

            for (DiskLruEntry entry : lruEntries.values()) {
                if (entry.currentEditor != null) {
                    //看到DIRTY这个字样都不代表着什么好事情，意味着这是一条脏数据。
                    //没错，每当我们调用一次DiskLruCache的edit()方法时，都会向journal文件中写入一条DIRTY记录，
                    //表示我们正准备写入一条缓存数据，但不知结果如何。
                    writer.write(DIRTY + ' ' + entry.key + '\n');
                } else {
                    writer.write(CLEAN + ' ' + entry.key + entry.getLengths() + '\n');
                }
            }
        } finally {
            writer.close();
        }

        if (journalFile.exists()) {
            renameTo(journalFile, journalFileBackup, true);
        }
        renameTo(journalFileTmp, journalFile, false);
        journalFileBackup.delete();

        journalWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(journalFile, true), DiskHelperUtils.US_ASCII));
    }

    private static void deleteIfExists(File file) throws IOException {
        if (file.exists() && !file.delete()) {
            throw new IOException();
        }
    }

    private static void renameTo(File from, File to, boolean deleteDestination) throws IOException {
        if (deleteDestination) {
            deleteIfExists(to);
        }
        if (!from.renameTo(to)) {
            throw new IOException();
        }
    }

    /**
     * 读取缓存
     * 返回名为{@code key}的条目的快照，如果不存在则返回null，这是当前不可读的。如果一个值被返回，它将被移动到LRU队列的头。
     */
    public synchronized Value get(String key) throws IOException {
        checkNotClosed();
        //取出当前key对应的缓存Entry
        DiskLruEntry entry = lruEntries.get(key);
        if (entry == null) {
            return null;
        }

        if (!entry.readable) {
            return null;
        }

        for (File file : entry.cleanFiles) {
            // A file must have been deleted manually!
            if (!file.exists()) {
                // 除非用户手动删了文件, 否则不会执行到这里...
                return null;
            }
        }

        redundantOpCount++;
        journalWriter.append(READ);
        journalWriter.append(' ');
        journalWriter.append(key);
        journalWriter.append('\n');
        if (journalRebuildRequired()) {
            executorService.submit(cleanupCallable);
        }

        return new Value(key, entry.sequenceNumber, entry.cleanFiles, entry.lengths);
    }

    /**
     * 返回名为{@code key}的条目的编辑器，如果正在进行另一个编辑，则返回null。
     * 这个key将会成为缓存文件的文件名
     */
    public Editor edit(String key) throws IOException {
        return edit(key, ANY_SEQUENCE_NUMBER);
    }

    /**
     * 如何写缓存？
     * 写缓存的时候需要先通过edit(String key)方法新建一个Editor，然后将数据写入Editor的输出流中，最后成功则调用
     * Editor.commit()，失败则调用Editor.abort()。
     *
     * 该方法大概思路
     * 1.取出当前key对应的缓存Entry，如果Entry不存在则新建并添加到lruEntries中，
     * 2.如果存在且entry.currentEditor不为空，表示Entry正在进行缓存编辑。
     * 3.随后新建一个Editor，并在日志文件中输出一行DIRTY日志表示开始编辑缓存文件。
     * @param key                                           key
     * @param expectedSequenceNumber                        number
     * @return
     * @throws IOException                                  异常
     */
    private synchronized Editor edit(String key, long expectedSequenceNumber) throws IOException {
        checkNotClosed();
        //取出当前key对应的缓存Entry
        DiskLruEntry entry = lruEntries.get(key);
        if (expectedSequenceNumber != ANY_SEQUENCE_NUMBER && (entry == null
                || entry.sequenceNumber != expectedSequenceNumber)) {
            return null; // Value is stale.
        }
        if (entry == null) {
            //如果Entry不存在则新建并添加到lruEntries中
            entry = new DiskLruEntry(key,valueCount,directory);
            lruEntries.put(key, entry);
        } else if (entry.currentEditor != null) {
            //如果存在且entry.currentEditor不为空，表示Entry正在进行缓存编辑
            return null; // Another edit is in progress.
        }

        //随后新建一个Editor，并在日志文件中输出一行DIRTY日志表示开始编辑缓存文件。
        Editor editor = new Editor(entry);
        entry.currentEditor = editor;

        // Flush the journal before creating files to prevent file leaks.
        // 为了防止文件泄露，在创建文件前，将日志立即写入journal中
        journalWriter.append(DIRTY);
        journalWriter.append(' ');
        journalWriter.append(key);
        journalWriter.append('\n');
        journalWriter.flush();
        return editor;
    }

    /**
     * 返回该缓存存储其数据的目录。
     */
    public File getDirectory() {
        return directory;
    }

    /**
     * 返回此缓存用于存储其数据的最大字节数。
     */
    public synchronized long getMaxSize() {
        return maxSize;
    }

    /**
     * 更改缓存可以存储的最大字节数，并在必要时对作业进行排队，以精简现有的存储。
     */
    public synchronized void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
        executorService.submit(cleanupCallable);
    }

    /**
     * 返回当前用于在此缓存中存储值的字节数。如果后台删除挂起，这个值可能大于最大大小。
     * 这个方法会返回当前缓存路径下所有缓存数据的总字节数，以byte为单位，
     * 如果应用程序中需要在界面上显示当前缓存数据的总大小，就可以通过调用这个方法计算出来。
     */
    public synchronized long size() {
        return size;
    }

    /**
     * 该方法首先根据文件写入是否成功来重命名或者删除tmp文件，随后向journal写入日志，最后判断是否需要清理磁盘空间。
     * @param editor                                        editor对象
     * @param success                                       是否成功
     * @throws IOException
     */
    private synchronized void completeEdit(Editor editor, boolean success) throws IOException {
        DiskLruEntry entry = editor.entry;
        if (entry.currentEditor != editor) {
            throw new IllegalStateException();
        }

        // 如果当前编辑是第一次创建Entry，那么每个索引上都应该有值
        // valueCount表示一个Entry中的value数量
        if (success && !entry.readable) {
            for (int i = 0; i < valueCount; i++) {
                if (!editor.written[i]) {
                    editor.abort();
                    throw new IllegalStateException("Newly created entry didn't create value for index " + i);
                }
                if (!entry.getDirtyFile(i).exists()) {
                    editor.abort();
                    return;
                }
            }
        }
        // 遍历Entry上的每个文件
        // 如果编辑成功就将临时文件改名, 如果失败则删除临时文件
        for (int i = 0; i < valueCount; i++) {
            File dirty = entry.getDirtyFile(i);
            if (success) {
                if (dirty.exists()) {
                    File clean = entry.getCleanFile(i);
                    dirty.renameTo(clean);
                    long oldLength = entry.lengths[i];
                    long newLength = clean.length();
                    entry.lengths[i] = newLength;
                    size = size - oldLength + newLength;
                }
            } else {
                deleteIfExists(dirty);
            }
        }

        redundantOpCount++;
        entry.currentEditor = null;
        if (entry.readable | success) {
            entry.readable = true;
            journalWriter.append(CLEAN);
            journalWriter.append(' ');
            journalWriter.append(entry.key);
            journalWriter.append(entry.getLengths());
            journalWriter.append('\n');

            if (success) {
                // 给Entry的sequenceNumber赋值, 用于标记snapshot是否过期
                // 如果Entry和snapshot的sequenceNumber不同, 则表示数据已经过期了
                entry.sequenceNumber = nextSequenceNumber++;
            }
        } else {
            lruEntries.remove(entry.key);
            journalWriter.append(REMOVE);
            journalWriter.append(' ');
            journalWriter.append(entry.key);
            journalWriter.append('\n');
        }
        journalWriter.flush();
        // 判断是否需要清理磁盘空间
        if (size > maxSize || journalRebuildRequired()) {
            executorService.submit(cleanupCallable);
        }
    }

    /**
     * 只有当日志大小减半并消除至少2000个ops时，我们才重新生成日志。
     * 在写入数据，获取数据或者移除数据时都会做校验
     */
    private boolean journalRebuildRequired() {
        final int redundantOpCompactThreshold = 2000;
        return redundantOpCount >= redundantOpCompactThreshold //
                && redundantOpCount >= lruEntries.size();
    }

    /**
     * 删除{@code key}的条目，如果它存在并且可以被删除。正在编辑的条目不能删除。
     * @return true if an entry was removed.
     */
    public synchronized boolean remove(String key) throws IOException {
        checkNotClosed();
        //取出当前key对应的缓存Entry
        DiskLruEntry entry = lruEntries.get(key);
        if (entry == null || entry.currentEditor != null) {
            return false;
        }

        for (int i = 0; i < valueCount; i++) {
            File file = entry.getCleanFile(i);
            if (file.exists() && !file.delete()) {
                throw new IOException("failed to delete " + file);
            }
            size -= entry.lengths[i];
            entry.lengths[i] = 0;
        }

        redundantOpCount++;
        journalWriter.append(REMOVE);
        journalWriter.append(' ');
        journalWriter.append(key);
        journalWriter.append('\n');

        lruEntries.remove(key);

        if (journalRebuildRequired()) {
            executorService.submit(cleanupCallable);
        }

        return true;
    }

    /**
     * Returns true if this cache has been closed.
     */
    public synchronized boolean isClosed() {
        return journalWriter == null;
    }

    private void checkNotClosed() {
        if (journalWriter == null) {
            throw new IllegalStateException("cache is closed");
        }
    }

    /**
     * 强制对文件系统进行缓冲操作。
     * 这个方法用于将内存中的操作记录同步到日志文件（也就是journal文件）当中。
     * 这个方法非常重要，因为DiskLruCache能够正常工作的前提就是要依赖于journal文件中的内容。
     * 并不是每次写读缓存都要调用一次flush()方法，频繁地调用并不会带来任何好处，只会额外增加同步journal文件的时间。
     * 比较标准的做法就是在Activity的onPause()方法中去调用一次flush()方法就可以了。
     */
    public synchronized void flush() throws IOException {
        checkNotClosed();
        trimToSize();
        journalWriter.flush();
    }

    /**
     * 这个方法用于将DiskLruCache关闭掉，是和open()方法对应的一个方法。
     * 关闭掉了之后就不能再调用DiskLruCache中任何操作缓存数据的方法
     * 通常只应该在Activity的onDestroy()方法中去调用close()方法。
     */
    @Override
    public synchronized void close() throws IOException {
        if (journalWriter == null) {
            return; // Already closed.
        }
        for (DiskLruEntry entry : new ArrayList<DiskLruEntry>(lruEntries.values())) {
            if (entry.currentEditor != null) {
                entry.currentEditor.abort();
            }
        }
        trimToSize();
        journalWriter.close();
        journalWriter = null;
    }

    private void trimToSize() throws IOException {
        while (size > maxSize) {
            Map.Entry<String, DiskLruEntry> toEvict = lruEntries.entrySet().iterator().next();
            remove(toEvict.getKey());
        }
    }

    /**
     * 获取大小
     * @return
     */
    public long getSize(){
        return lruEntries.keySet().size();
    }

    /**
     * 关闭缓存并删除其存储的所有值。这将删除缓存目录中的所有文件，包括不是由缓存创建的文件。
     */
    public void delete() throws IOException {
        close();
        DiskHelperUtils.deleteContents(directory);
    }

    private static String inputStreamToString(InputStream in) throws IOException {
        //写数据，一次读取一个字节
        return DiskHelperUtils.readFully(new InputStreamReader(in, DiskHelperUtils.UTF_8));
    }

    /**
     * A snapshot of the values for an entry.
     */
    public final class Value {
        private final String key;
        private final long sequenceNumber;
        private final long[] lengths;
        private final File[] files;

        private Value(String key, long sequenceNumber, File[] files, long[] lengths) {
            this.key = key;
            this.sequenceNumber = sequenceNumber;
            this.files = files;
            this.lengths = lengths;
        }

        /**
         * Returns an editor for this snapshot's entry, or null if either the
         * entry has changed since this snapshot was created or if another edit
         * is in progress.
         */
        public Editor edit() throws IOException {
            return DiskLruCache.this.edit(key, sequenceNumber);
        }

        public File getFile(int index) {
            return files[index];
        }

        /**
         * Returns the string value for {@code index}.
         */
        public String getString(int index) throws IOException {
            InputStream is = new FileInputStream(files[index]);
            return inputStreamToString(is);
        }

        /**
         * Returns the byte length of the value for {@code index}.
         */
        public long getLength(int index) {
            return lengths[index];
        }
    }

    /**
     * 编辑条目的值。
     */
    public final class Editor {
        private final DiskLruEntry entry;
        private final boolean[] written;
        private boolean committed;

        private Editor(DiskLruEntry entry) {
            this.entry = entry;
            this.written = (entry.readable) ? null : new boolean[valueCount];
        }

        /**
         * 返回一个未缓冲的输入流来读取最后提交的值，如果没有提交值，则返回null。
         */
        private InputStream newInputStream(int index) throws IOException {
            synchronized (DiskLruCache.this) {
                if (entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!entry.readable) {
                    return null;
                }
                try {
                    return new FileInputStream(entry.getCleanFile(index));
                } catch (FileNotFoundException e) {
                    return null;
                }
            }
        }

        /**
         * 以字符串的形式返回最后一个提交的值，如果没有提交值，则返回null。
         */
        public String getString(int index) throws IOException {
            //创建io流对象
            InputStream in = newInputStream(index);
            //如果流对象不为空，则写入数据
            return in != null ? inputStreamToString(in) : null;
        }

        public File getFile(int index) throws IOException {
            synchronized (DiskLruCache.this) {
                if (entry.currentEditor != this) {
                    throw new IllegalStateException();
                }
                if (!entry.readable) {
                    written[index] = true;
                }
                File dirtyFile = entry.getDirtyFile(index);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                return dirtyFile;
            }
        }

        /**
         * Sets the value at {@code index} to {@code value}.
         */
        public void set(int index, String value) throws IOException {
            Writer writer = null;
            try {
                File file = getFile(index);
                //创建输出流对象
                OutputStream os = new FileOutputStream(file);
                writer = new OutputStreamWriter(os, DiskHelperUtils.UTF_8);
                //写数据
                writer.write(value);
            } finally {
                //关闭流对象
                DiskHelperUtils.closeQuietly(writer);
            }
        }

        /**
         * 成功后调用Editor.commit()
         * 提交此编辑，使其对读者可见。这释放了编辑锁，因此可以在同一键上启动另一个编辑。
         */
        public void commit() throws IOException {
            // The object using this Editor must catch and handle any errors
            // during the write. If there is an error and they call commit
            // anyway, we will assume whatever they managed to write was valid.
            // Normally they should call abort.
            completeEdit(this, true);
            committed = true;
        }

        /**
         * 失败后调用Editor.abort()方法
         * 中止这个编辑。这释放了编辑锁，因此可以在同一键上启动另一个编辑。
         */
        public void abort() throws IOException {
            completeEdit(this, false);
        }

        public void abortUnlessCommitted() {
            if (!committed) {
                try {
                    abort();
                } catch (IOException ignored) {
                }
            }
        }
    }

}
