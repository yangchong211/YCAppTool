package com.yc.easyble.data;


public enum BleScanState {

    /**
     * 空闲
     */
    STATE_IDLE(-1),
    /**
     * 正在扫描中
     */
    STATE_SCANNING(0X01);

    private final int code;

    BleScanState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
