package me.pqpo.librarylog4a.logger;

public interface Logger {

    void println(int priority, String tag, String msg);

    void flush();

    void release();
}
