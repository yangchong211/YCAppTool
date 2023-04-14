package com.yc.logservice;


import com.yc.eventuploadlib.ExceptionReporter;
import com.yc.toolutils.AppLogUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;

/**
 * 职责描述: 日志以Append的方式 输出到文件。
 * 相对于父类WriterAppender额外做的事情为:
 * 1、指定了日志文件路径，创建日志文件.
 * 2、生成FileWriter 指向日志文件.
 */

public class FileAppenderImpl extends AbsWriterAppender {

    protected boolean fileAppend = true;

    /**
     * The name of the log file.
     */
    protected String fileName = null;

    /**
     * Do we do bufferedIO?
     */
    protected boolean bufferedIO = false;

    /**
     * Determines the size of IO buffer be. Default is 8K.
     */
    protected int bufferSize = 1 * 1024;


    public FileAppenderImpl() {
    }

    public FileAppenderImpl(String filename, boolean append, boolean bufferedIO,
                            int bufferSize) throws IOException {
        this.fileName = filename;
        this.fileAppend = append;
        this.bufferedIO = bufferedIO;
        this.bufferSize = bufferSize;
    }


    public FileAppenderImpl(String filename, boolean append)
            throws IOException {
        this(filename, append, true, 1 * 1024);
    }


    public FileAppenderImpl(String filename) throws IOException {
        this(filename, true);
    }

    // ------ 相对于父类 扩展的方法 ------- //

    /**
     * 激活日志文件:
     * 创建日志文件,并生成FileWriter对象
     */
    @Override
    public void activateOptions() {
        if (fileName != null) {
            try {
                setFile(fileName, fileAppend, bufferedIO, bufferSize);
            } catch (IOException e) {
                AppLogUtils.e(TAG, "activateOptions:" + e.getMessage());
                ExceptionReporter.report("activateOptions : " , e);
            }
        } else {
            AppLogUtils.e(TAG, "activateOptions fileName was null");
        }
    }


    /**
     * 初始化Writer
     * 支持BufferWriter和OutputStreamWriter两种.
     * BufferWriter的化,会缓存bufferSize数据后,再flush到文件.
     *
     * @param fileName
     * @param append
     * @param bufferedIO 是否支持
     * @param bufferSize
     * @throws IOException
     */
    public synchronized void setFile(String fileName, boolean append, boolean bufferedIO, int bufferSize)
            throws IOException {
        com.yc.toolutils.AppLogUtils.d(TAG, "setFile called: " + fileName + ", " + append);
        //路径：/storage/emulated/0/Android/data/com.zuoyebang.iotunion/files/log/app/tmlog.tm
        // It does not make sense to have immediate flush and bufferedIO.
        if (bufferedIO) {
            setImmediateFlush(false);
        }

        reset();

        FileOutputStream ostream = null;
        try {
            //
            //   attempt to create file
            //
            ostream = new FileOutputStream(fileName, append);
        } catch (FileNotFoundException ex) {
            //
            //   if parent directory does not exist then
            //      attempt to create it and try to create file
            //      see bug 9150
            //
            String parentName = new File(fileName).getParent();
            if (parentName != null) {
                File parentDir = new File(parentName);
                if (!parentDir.exists() && parentDir.mkdirs()) {
                    ostream = new FileOutputStream(fileName, append);
                } else {
                    //这里抛出异常需要注明原因
                    ExceptionReporter.report("appender set file exc : " , ex);
                    throw new FileNotFoundException("appender set file exc : " + ex.getMessage());
                }
            } else {
                ExceptionReporter.report("parentName is null : " , ex);
                throw new NullPointerException("parentName is null");
            }
        }
        Writer fw = createWriter(ostream);
        if (bufferedIO) {
            fw = new BufferedWriter(fw, bufferSize);
        }
        setWriter(fw);
//        this.fileName = fileName;
//        this.fileAppend = append;
//        this.bufferedIO = bufferedIO;
//        this.bufferSize = bufferSize;

        AppLogUtils.d(TAG, "setFile ended");
    }

    // ------- getter and setter ------- //

    public void setFile(String file) {
        String val = file.trim();
        fileName = val;
    }


    public boolean getAppend() {
        return fileAppend;
    }

    public String getFile() {
        return fileName;
    }


    public boolean getBufferedIO() {
        return this.bufferedIO;
    }

    /**
     * Get the size of the IO buffer.
     */
    public int getBufferSize() {
        return this.bufferSize;
    }

    public void setAppend(boolean flag) {
        fileAppend = flag;
    }

    public void setBufferedIO(boolean bufferedIO) {
        this.bufferedIO = bufferedIO;
        if (bufferedIO) {
            setImmediateFlush(false);
        } else {
            setImmediateFlush(true);
        }
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

}


