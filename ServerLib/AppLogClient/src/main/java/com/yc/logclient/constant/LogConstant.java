package com.yc.logclient.constant;

public final class LogConstant {

    public static final int Log_Type_App = LogType.app.value();//普通日志
    public static final int Log_Type_Crash = LogType.crash.value();//crash日志
    public static final int Log_Type_Statistics = LogType.statistics.value();//统计日志
    public static final int Log_Type_Anr = LogType.anr.value();//ANR 日志

    public static final int Log_Level_statistics = Log_Type_Statistics;
    public static final int Log_Level_crash = Log_Type_Crash;
    public static final int Log_Level_anr = Log_Type_Anr;
    public static final int Log_Level_error = 4;
    public static final int Log_Level_warn = 3;
    public static final int Log_Level_info = 2;
    public static final int Log_Level_debug = 1;
    public static final int Log_Level_verbose = 0;


    public static final int MAX_FOLDER_SIZE_BIG = 50;
    public static final int MAX_FOLDER_SIZE_SMALL = 10;

    public static final int KEEP_FREE_STORE_BIG = 80;
    public static final int KEEP_FREE_STORE_SMALL = 40;

    public static final int KEEP_DAY_NUMBER_SMALL = 7; //7
    public static final int KEEP_DAY_NUMBER_BIG = 30;//30

    public enum LogType {
        app(10),//普通日志
        crash(20),//crash日志
        statistics(30),//统计日志
        anr(40);//ANR 日志
        private final int type;

        public int getMaxFolderSize() {
            int maxFolderSize = MAX_FOLDER_SIZE_BIG;
            switch (this) {
                case app:
                    maxFolderSize = MAX_FOLDER_SIZE_BIG;
                    break;
                case crash:
                case statistics:
                case anr:
                    maxFolderSize = MAX_FOLDER_SIZE_SMALL;
                    break;
                default:
                    break;
            }
            return maxFolderSize;
        }

        public int getKeepFreeStore() {
            int keepFreeStore = KEEP_FREE_STORE_BIG;
            switch (this) {
                case app:
                    keepFreeStore = KEEP_FREE_STORE_BIG;
                    break;
                case crash:
                case statistics:
                case anr:
                    keepFreeStore = KEEP_FREE_STORE_SMALL;
                    break;
                default:
                     break;

            }
            return keepFreeStore;
        }

        public int getKeepDayNumber() {
            int keepDayNumber = KEEP_DAY_NUMBER_SMALL;
            switch (this) {
                case app:
                    keepDayNumber = KEEP_DAY_NUMBER_SMALL;
                    break;
                case crash:
                case statistics:
                case anr:
                    keepDayNumber = KEEP_DAY_NUMBER_BIG;
                    break;
                default:
                    break;

            }
            return keepDayNumber;
        }

        LogType(int type) {
            this.type = type;
        }

        public static LogType convert2LogType(int type) {
            if (type == app.value()) {
                return app;
            } else if (type == crash.value()) {
                return crash;
            } else if (type == anr.value()) {
                return anr;
            } else if (type == statistics.value()) {
                return statistics;
            }
            return app;
        }

        public int value() {
            return this.type;
        }
    }
}
