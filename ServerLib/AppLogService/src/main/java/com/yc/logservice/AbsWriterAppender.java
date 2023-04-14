package com.yc.logservice;


import android.util.Log;

import com.yc.toolutils.AppLogUtils;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

/**
 * 职责描述: 支持Append方式写日志
 */
public class AbsWriterAppender implements IWriteLogger {

    public static final String TAG = AbsWriterAppender.class.getSimpleName();

    protected boolean closed = false;

    protected boolean immediateFlush = true;

    //编码方式
    protected String encoding;

    protected CatchFilterWriter mInnerWriter;

    public AbsWriterAppender() {
    }


    public void activateOptions() {
    }

    //flush操作
    @Override
    public void flush() {
        if (mInnerWriter != null) {
            try {
                mInnerWriter.flush();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    //close操作
    @Override
    public synchronized void close() {
        writeFooter();
        closeWriter();
    }


    @Override
    public void log(int leve, String msg) {
        append(msg);
    }

    @Override
    public void append(String msg) {
        doAppend(msg);
    }

    @Override
    public void append(List<String> list) {
        doAppend(list);
    }

    // ------------ 自定义的方法 ---------- //

    /**
     * 创建Writer
     *
     * @param os 输出流
     * @return
     */
    protected OutputStreamWriter createWriter(OutputStream os) {
        OutputStreamWriter retval = null;

        String enc = getEncoding();
        if (enc != null) {
            try {
                retval = new OutputStreamWriter(os, enc);
            } catch (IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                AppLogUtils.e(TAG, e.getMessage());
            }
        }
        if (retval == null) {
            retval = new OutputStreamWriter(os);
        }
        return retval;
    }

    /**
     * 设置Writer
     *
     * @param writer
     */
    public synchronized void setWriter(Writer writer) {
        reset();
        this.mInnerWriter = new CatchFilterWriter(writer);
        this.closed = false;
        writeHeader();
    }

    /**
     * 关闭Writer
     */
    protected void closeWriter() {
        if (mInnerWriter != null) {
            try {
                mInnerWriter.close();
            } catch (IOException e) {
                if (e instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                AppLogUtils.e(TAG, e.getMessage());
            }
        }
        this.mInnerWriter = null;
    }

    /**
     * 单条追加操作
     *
     * @param msg
     */
    protected void doAppend(String msg) {
        if (msg == null) {
            return;
        }
        this.mInnerWriter.write(msg + "\n");

        if (shouldFlush()) {
            this.mInnerWriter.flush();
        }
    }

    /**
     * 批量追加操作
     *
     * @param list
     */
    protected void doAppend(List<String> list) {
        if (list != null) {
            for (String object : list) {
                if (object == null) {
                    continue;
                }
                this.mInnerWriter.write(object + "\n");
            }
        }
        if (shouldFlush()) {
            this.mInnerWriter.flush();
        }
    }


    /**
     * 重置操作,打印Footer之后，关闭输出流
     */
    protected void reset() {
        close();
    }

    protected void writeFooter() {
        String footer = "\n====================FOOTER==========================\n";
        if (footer != null && this.mInnerWriter != null) {
            this.mInnerWriter.write(footer);
        }
    }

    protected void writeHeader() {
        String header = "\n====================START==========================\n";
        if (header != null && this.mInnerWriter != null) {
            this.mInnerWriter.write(header);
        }
    }

    protected boolean shouldFlush() {
        return immediateFlush;
    }


    //------------------- getters and setters -----------------//

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String value) {
        encoding = value;
    }

    public void setImmediateFlush(boolean value) {
        AppLogUtils.e(TAG, "setImmediateFlush:" + value);
        immediateFlush = value;
    }

    public boolean getImmediateFlush() {
        return immediateFlush;
    }

}

