package me.pqpo.librarylog4a;

/**
 * Created by pqpo on 2017/11/16.
 */

public class Level {

    public static final int VERBOSE = 2;

    public static final int DEBUG = 3;

    public static final int INFO = 4;

    public static final int WARN = 5;

    public static final int ERROR = 6;

    public static String getLevelName(int logLevel) {
        String levelName;
        switch (logLevel) {
            case VERBOSE:
                levelName = "VERBOSE";
                break;
            case DEBUG:
                levelName = "DEBUG";
                break;
            case INFO:
                levelName = "INFO";
                break;
            case WARN:
                levelName = "WARN";
                break;
            case ERROR:
                levelName = "ERROR";
                break;
            default:
                if (logLevel < VERBOSE) {
                    levelName = "VERBOSE-" + (VERBOSE - logLevel);
                } else {
                    levelName = "ERROR+" + (logLevel - ERROR);
                }
                break;
        }
        return levelName;
    }

    public static String getShortLevelName(int logLevel) {
        String levelName;
        switch (logLevel) {
            case VERBOSE:
                levelName = "V";
                break;
            case DEBUG:
                levelName = "D";
                break;
            case INFO:
                levelName = "I";
                break;
            case WARN:
                levelName = "W";
                break;
            case ERROR:
                levelName = "E";
                break;
            default:
                if (logLevel < VERBOSE) {
                    levelName = "V-" + (VERBOSE - logLevel);
                } else {
                    levelName = "E+" + (logLevel - ERROR);
                }
                break;
        }
        return levelName;
    }

}
