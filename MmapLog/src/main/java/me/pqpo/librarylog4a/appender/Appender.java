package me.pqpo.librarylog4a.appender;


public interface Appender {

    void append(int logLevel, String tag, String msg);
    void flush();
    void release();

}
