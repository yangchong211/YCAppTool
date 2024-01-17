package com.yc.cardmanager;

/**
 * 卡类型
 */
public enum CardType {

    /**
     * M1卡
     */
    M1(1),
    /**
     * CPU卡
     */
    CPU(2),
    /**
     * PSAM卡
     */
    PSAM(3),
    /**
     * 社保卡
     */
    SB(4),
    /**
     * 银行卡
     */
    BANK(5),
    /**
     * 其他卡
     */
    OTHER_CARD(-1),
    /**
     * 未知
     */
    INVALID(-2),
    /**
     * None
     */
    NONE(-3);
    private int type;

    private CardType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public CardType setType(int type) {
        this.type = type;
        return this;
    }
}
