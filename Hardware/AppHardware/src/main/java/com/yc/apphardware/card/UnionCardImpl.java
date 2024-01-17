package com.yc.apphardware.card;

import com.yc.apphardware.card.lib.CardReadManager;
import com.yc.cardmanager.AbsUnionCard;
import com.yc.cardmanager.CardHelper;
import com.yc.cardmanager.CardType;
import com.yc.cpucard.AbstractCpuCard;
import com.yc.mifarecard.AbstractM1Card;
import com.yc.toolutils.AppLogUtils;

public class UnionCardImpl extends AbsUnionCard {

    private static final String TAG = "Card UnionImpl : ";
    private CardType cardType1;

    public UnionCardImpl(AbstractM1Card m1Card, AbstractCpuCard cpuCard) {
        super(m1Card, cpuCard);
    }

    @Override
    public CardType getCardType() {
        CardReadManager.CardType cardType = CardReadManager.getInstance().SearchCardType();
        cardType1 = CardType.NONE;
        if (cardType == CardReadManager.CardType.M1_CARD) {
            cardType1 = CardType.M1;
            AppLogUtils.d(TAG + "M1卡类型");
        } else if (cardType == CardReadManager.CardType.CPU_CARD) {
            cardType1 = CardType.CPU;
            AppLogUtils.d(TAG + "CPU卡类型");
        }
        return cardType1;
    }

    @Override
    public String search() {
        String cardNo = "";
        if (cardType1 == CardType.CPU) {
            cardNo = CardHelper.getInstance().getCpuCard().search();
            AppLogUtils.d(TAG + "CPU 卡号" + cardNo);
        } else if (cardType1 == CardType.M1) {
            cardNo = CardHelper.getInstance().getM1Card().search();
            AppLogUtils.d(TAG+ "M1 卡号"  + cardNo);
        }
        return null;
    }


    @Override
    public String verify() {
        return super.verify();
    }
}
