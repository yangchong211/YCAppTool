package me.pqpo.librarylog4a.logger;

/**
 * Created by pqpo on 2018/2/27.
 */
public interface Logger {

    void println(int priority, String tag, String msg);

    void flush();

    void release();
}
