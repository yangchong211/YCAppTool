package com.yc.cardmanager;

import com.yc.cardmanager.cpu.AbstractCpuCard;
import com.yc.cardmanager.m1.AbstractM1Card;

public abstract class AbsUnionCard implements IUnionCard {

    private static final String TAG = "Card Union : ";
    protected CardType cardType;

    public AbsUnionCard(AbstractM1Card m1Card, AbstractCpuCard cpuCard) {
        this.cardType = CardType.NONE;
        CardHelper.getInstance().setM1Card(m1Card);
        CardHelper.getInstance().setCpuCard(cpuCard);
    }

    @Override
    public String verify(CardType type, String cardNo) {
        return null;
    }
}
