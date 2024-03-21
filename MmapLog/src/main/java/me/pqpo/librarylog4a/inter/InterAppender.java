package me.pqpo.librarylog4a.inter;


public interface InterAppender {

    void append(int logLevel, String tag, String msg);

    void flush();

    void release();

}
