package com.yc.logclient.constant;

public enum LogType {
    /**
     * 普通日志
     */
    app(10),
    /**
     * crash日志
     */
    crash(20),
    /**
     * 统计日志
     */
    statistics(30),
    /**
     * ANR 日志
     */
    anr(40);
    /**
     * 类型
     */
    private int type;

    public int getMaxFolderSize() {
        int maxFolderSize = LogConstant.MAX_FOLDER_SIZE_BIG;
        switch (this) {
            case app:
                maxFolderSize = LogConstant.MAX_FOLDER_SIZE_BIG;
                break;
            case crash:
            case statistics:
            case anr:
                maxFolderSize = LogConstant.MAX_FOLDER_SIZE_SMALL;
                break;
            default:
                break;
        }
        return maxFolderSize;
    }

    public int getKeepFreeStore() {
        int keepFreeStore = LogConstant.KEEP_FREE_STORE_BIG;
        switch (this) {
            case app:
                keepFreeStore = LogConstant.KEEP_FREE_STORE_BIG;
                break;
            case crash:
            case statistics:
            case anr:
                keepFreeStore = LogConstant.KEEP_FREE_STORE_SMALL;
                break;
            default:
                break;

        }
        return keepFreeStore;
    }

    public int getKeepDayNumber() {
        int keepDayNumber = LogConstant.KEEP_DAY_NUMBER_SMALL;
        switch (this) {
            case app:
                keepDayNumber = LogConstant.KEEP_DAY_NUMBER_SMALL;
                break;
            case crash:
            case statistics:
            case anr:
                keepDayNumber = LogConstant.KEEP_DAY_NUMBER_BIG;
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
