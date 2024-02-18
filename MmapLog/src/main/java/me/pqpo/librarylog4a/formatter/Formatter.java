package me.pqpo.librarylog4a.formatter;

public interface Formatter {
    String format(int logLevel, String tag, String msg);
}
