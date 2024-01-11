package com.yc.cardmanager;

import com.yc.mifarecard.AbstractM1Card;

public final class CardHelper {

    private static volatile CardHelper singleton = null;
    private AbstractM1Card m1Card;

    /**
     * 获取单例
     *
     * @return 单例
     */
    public static CardHelper getInstance() {
        if (singleton == null) {
            synchronized (CardHelper.class) {
                if (singleton == null) {
                    singleton = new CardHelper();
                }
            }
        }
        return singleton;
    }

    public AbstractM1Card getM1Card() {
        return m1Card;
    }

    public void setM1Card(AbstractM1Card m1Card) {
        this.m1Card = m1Card;
    }
}
