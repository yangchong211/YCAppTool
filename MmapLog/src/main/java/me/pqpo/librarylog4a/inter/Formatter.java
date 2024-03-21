package me.pqpo.librarylog4a.inter;

public interface Formatter {
    String format(int logLevel, String tag, String msg);
}
