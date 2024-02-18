package me.pqpo.librarylog4a.formatter;

/**
 * Created by pqpo on 2017/11/24.
 */
public interface Formatter {
    String format(int logLevel, String tag, String msg);
}
