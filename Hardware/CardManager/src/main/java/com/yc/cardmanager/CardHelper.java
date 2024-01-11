package com.yc.cardmanager;

import com.yc.mifarecard.InterM1Card;

public final class CardHelper {

    private static volatile CardHelper singleton = null;
    private InterM1Card m1Card;

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

    public InterM1Card getM1Card() {
        return m1Card;
    }

    public void setM1Card(InterM1Card m1Card) {
        this.m1Card = m1Card;
    }
}
