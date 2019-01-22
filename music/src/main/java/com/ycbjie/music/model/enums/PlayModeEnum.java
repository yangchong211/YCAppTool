package com.ycbjie.music.model.enums;

/**
 * 播放模式
 */
public enum PlayModeEnum {

    /**
     * 顺序播放，默认的播放模式
     */
    LOOP(0),
    /**
     * 随机播放
     */
    SHUFFLE(1),
    /**
     * 单曲循环
     */
    SINGLE(2);

    private int value;

    PlayModeEnum(int value) {
        this.value = value;
    }

    public static PlayModeEnum valueOf(int value) {
        switch (value) {
            case 1:
                return SHUFFLE;
            case 2:
                return SINGLE;
            case 0:
            default:
                return LOOP;
        }
    }

    public int value() {
        return value;
    }
}
