package com.yc.logging.executor;

import androidx.annotation.RestrictTo;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.yc.logging.policy.AbstractRollingPolicy;
import com.yc.logging.config.LoggerConfig;
import com.yc.logging.config.LoggerContext;
import com.yc.logging.LoggerFactory;
import com.yc.logging.policy.SizeAndTimeBasedRollingPolicy;
import com.yc.logging.constant.Level;
import com.yc.logging.constant.Type;
import com.yc.logging.log.AbstractLog;
import com.yc.logging.util.Debug;
import com.yc.logging.util.LoggerUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Key;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class LogbackExecutor {

    private static final int MAX_QUEUE_SIZE = 1024;

    private static final Map<String, LogbackExecutor> sCachedExecutors =
            Collections.synchronizedMap(new HashMap<String, LogbackExecutor>());

    private final AbstractRollingPolicy mPolicy;

    private final BlockingQueue<AbstractLog> mLogQueue;

    private final Worker mWorker;

    private final Object mMutex = new Object();

    private File mCurrentlyActiveFile;
    private OutputStream mOutputStream;
    private final AtomicBoolean mLogConsumerStarted = new AtomicBoolean(false);
    private String IvKey = DXDecryptor.decode("+8at/cgV10ipDyx8W4Chrw==");

    public static LogbackExecutor getInstance(String buffer) {
        LogbackExecutor executor = sCachedExecutors.get(buffer);
        if (executor == null) {
            synchronized (sCachedExecutors) {
                //noinspection ConstantConditions
                if (executor == null) {
                    executor = new LogbackExecutor(buffer);
                    sCachedExecutors.put(buffer, executor);
                }
            }
        }
        return executor;
    }

    private LogbackExecutor(String buffer) {
        mWorker = new Worker("logger-logback-" + buffer);
        this.mLogQueue = new ArrayBlockingQueue<>(MAX_QUEUE_SIZE);
        this.mPolicy =
                new SizeAndTimeBasedRollingPolicy(Type.LOGBACK, buffer);
    }

    public void enqueue(final AbstractLog log) {
        if (log == null) {
            return;
        }
        if (mLogConsumerStarted.compareAndSet(false, true)) {
            start();
        }
        if (LoggerUtils.isMainThread()) {
            this.mLogQueue.offer(log);
        } else {
            try {
                this.mLogQueue.put(log);
            } catch (final InterruptedException ignore) {
            }
        }
    }

    private void start() {
        mPolicy.setDateInCurrentPeriod(System.currentTimeMillis());
        mCurrentlyActiveFile = new File(mPolicy.getActiveFile());

        try {
            openFile(mCurrentlyActiveFile);
        } catch (final IOException e) {
            Debug.logOrThrow("start work thread openFile IOException ", e);
        }

        startLogConsumerWorker();
    }

    private void startLogConsumerWorker() {
        mWorker.setDaemon(true);
        mWorker.start();
    }

    class Worker extends Thread {
        Worker(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (mLogConsumerStarted.get()) {
                try {
                    AbstractLog log = mLogQueue.take();
                    if (log == null) {
                        continue;
                    }
                    LoggerConfig config = LoggerFactory.getConfig();

                    Boolean isLogcatLogEnabled = config.isLogcatLogEnabled();

                    Boolean isFileLogEnabled = config.isFileLogEnabled();

                    boolean appDebuggable = LoggerContext.getDefault().isAppDebuggable();

                    boolean fileEnabled;
                    if (isFileLogEnabled == null) {
                        fileEnabled = isLogcatLogEnabled == null /*&& !appDebuggable*/;
                    } else {
                        fileEnabled = isFileLogEnabled;
                    }

                    boolean logcatEnabled;
                    if (isLogcatLogEnabled == null) {
                        logcatEnabled = isFileLogEnabled == null && appDebuggable;
                    } else {
                        logcatEnabled = isLogcatLogEnabled;
                    }

                    int fileLogLevel = config.getFileLogLevel().level;
                    int logcatLogLevel = config.getLogcatLogLevel().level;
                    int level = log.logLevel.level;

                    if (fileEnabled && level >= fileLogLevel) {
                        String logString = log.getContent();
                        if (!TextUtils.isEmpty(logString)) {
                            logToFile(logString, config.isEncryptEnabled());
                        }
                    }
                    if (logcatEnabled && level >= logcatLogLevel) {
                        String msg = log.getMsg();
                        if (!TextUtils.isEmpty(msg)) {
                            logToLogcat(log.getLogLevel(), log.getTag(), msg);
                        }
                    }
                } catch (Exception e) {
                    Debug.logOrThrow("Consume log failed log  ", e);
                }
            }
        }
    }


    private void logToFile(final String logMsg, boolean encrypt) {
        if (mPolicy.isTriggeringEvent(mCurrentlyActiveFile)) {
            rollover();
        }
        try {
            writeLogToFile(logMsg, encrypt);
        } catch (IOException e) {
            Debug.logOrThrow("writeLogToFile failed ", e);
        }
    }

    private void rollover() {
        synchronized (this.mMutex) {
            // Note: This method needs to be synchronized because it needs
            // exclusive access while it closes and then re-opens the target file.
            //
            // make sure to close the hereto active log file! Renaming under windows
            // does not work for open files.
            this.closeOutputStream();
            this.mPolicy.rollover();

            mCurrentlyActiveFile = new File(mPolicy.getActiveFile());

            try {
                // update the currentlyActiveFile
                // http://jira.qos.ch/browse/LBCORE-90

                // This will also close the file. This is OK since multiple
                // close operations are safe.
                this.openFile(mCurrentlyActiveFile);
            } catch (final IOException e) {
                Debug.e("rollover openFile IOException e = " + e);
            }
        }
    }

    private void openFile(final File activeFile) throws IOException {
        synchronized (this.mMutex) {
            if (!activeFile.getParentFile().exists()) {
                activeFile.getParentFile().mkdirs();
            }
            mOutputStream = new ResilientFileOutputStream(activeFile, true);
        }
    }


    private void writeLogToFile(final String txt, boolean encrypt) throws IOException {
        if (mOutputStream == null) {
            return;
        }

        if (TextUtils.isEmpty(txt)) {
            return;
        }

        if (TextUtils.isEmpty(IvKey)) {
            IvKey = DXDecryptor.decode("+8at/cgV10ipDyx8W4Chrw==");
        }

        final AESEncryptor encryptor = new AESEncryptor(IvKey);
        byte[] bytes = convertToBytes(txt + "\n");
        if (bytes == null || bytes.length == 0) {
            return;
        }
        if (encrypt) bytes = encryptor.encrypt(bytes);
        if (bytes == null || bytes.length == 0) {
            return;
        }

        if (encrypt) writeInt(bytes.length);

        this.mOutputStream.write(bytes);
        this.mOutputStream.flush();
    }


    /**
     * 写入int
     *
     * @param v
     * @throws IOException
     */
    private final void writeInt(int v) throws IOException {
        mOutputStream.write((v >>> 24) & 0xFF);
        mOutputStream.write((v >>> 16) & 0xFF);
        mOutputStream.write((v >>> 8) & 0xFF);
        mOutputStream.write((v >>> 0) & 0xFF);
    }

    /**
     * string转化成byte数组
     *
     * @param s
     * @return
     */
    private byte[] convertToBytes(final String s) {
        byte[] bytes;
        try {
            bytes = s.getBytes();
        } catch (OutOfMemoryError oom) {
            bytes = null;
        }
        return bytes;
    }


    private void closeOutputStream() {
        if (this.mOutputStream != null) {
            try {
                this.mOutputStream.close();
                this.mOutputStream = null;
            } catch (IOException ignore) {

            }
        }
    }

    /**
     * 进行AES加密
     *
     * @author wangqian
     */
    static final class AESEncryptor {

        private final IvParameterSpec ivSpec;

        private final SecretKeySpec keySpec;

        public AESEncryptor(final String key) {
            final byte[] keyBytes = key.getBytes();
            final byte[] buf = new byte[16];
            System.arraycopy(keyBytes, 0, buf, 0, Math.min(16, keyBytes.length));
            this.keySpec = new SecretKeySpec(buf, "AES");
            this.ivSpec = new IvParameterSpec(keyBytes);
        }

        public byte[] encrypt(final byte[] data) {
            try {
                final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, this.keySpec, this.ivSpec);
                return cipher.doFinal(data);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return data;
        }
    }


    static class DXDecryptor {
        static String algo = "ARCFOUR";
        static String kp = "DKrW9F9rh1oAHKf6";

        public static String decode(String s) {
            String str;
            String key = "Jvjd0+0C6wPyUUkARsSLEQ==";
            try {
                Cipher rc4 = Cipher.getInstance(algo);
                Key kpk = new SecretKeySpec(kp.getBytes(), algo);
                rc4.init(Cipher.DECRYPT_MODE, kpk);
                byte[] bck = Base64.decode(key, Base64.DEFAULT);
                byte[] bdk = rc4.doFinal(bck);
                Key dk = new SecretKeySpec(bdk, algo);
                rc4.init(Cipher.DECRYPT_MODE, dk);
                byte[] bcs = Base64.decode(s, Base64.DEFAULT);
                byte[] byteDecryptedString = rc4.doFinal(bcs);
                str = new String(byteDecryptedString);
            } catch (Exception e) {
                str = "";
            }
            return str;
        }

    }

    private void logToLogcat(Level level, String tag, String msg) {
        switch (level) {
            case TRACE:
                Log.v(tag, msg);
                break;
            case DEBUG:
                Log.d(tag, msg);
                break;
            case INFO:
                Log.i(tag, msg);
                break;
            case WARN:
                Log.w(tag, msg);
                break;
            case ERROR:
                Log.e(tag, msg);
                break;
            default:
        }
    }
}
