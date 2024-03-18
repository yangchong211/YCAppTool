package com.yc.cardmanager;

import com.yc.cardmanager.cpu.AbstractCpuCard;
import com.yc.cardmanager.m1.AbstractM1Card;

public final class CardHelper {

    private static volatile CardHelper singleton = null;
    private AbstractM1Card m1Card;
    private AbstractCpuCard cpuCard;
    private AbsUnionCard unionCard;

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

    private CardHelper() {

    }

    public AbstractM1Card getM1Card() {
        return m1Card;
    }

    public void setM1Card(AbstractM1Card m1Card) {
        this.m1Card = m1Card;
    }

    public AbstractCpuCard getCpuCard() {
        return cpuCard;
    }

    public void setCpuCard(AbstractCpuCard cpuCard) {
        this.cpuCard = cpuCard;
    }

    public AbsUnionCard getUnionCard() {
        return unionCard;
    }

    public void setUnionCard(AbsUnionCard unionCard) {
        this.unionCard = unionCard;
    }
}
