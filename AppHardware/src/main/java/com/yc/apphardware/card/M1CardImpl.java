package com.yc.apphardware.card;

import android.text.TextUtils;

import com.android.scanCard.utils.CardReadManager;
import com.yc.mifarecard.AbstractM1Card;
import com.yc.toolutils.AppLogUtils;

public class M1CardImpl extends AbstractM1Card {

    String carNumberOrigin = "";


    @Override
    public String searchNo() {
        CardReadManager.CardType cardType = CardReadManager.getInstance().SearchCardType();
        carNumberOrigin = cardType.getCardNo();
        String newCarNumber = carNumberOrigin;
        String cardNumber = "";
        if (!TextUtils.isEmpty(newCarNumber) && newCarNumber.length() == 8) {
            cardNumber = WeCardCardHelper.flipCardNum(newCarNumber);
        }
        AppLogUtils.d("Card 开始寻卡 " + cardType.getCardType() + " , " + cardType.getCardNo());
        return cardNumber;
    }

    @Override
    public byte[] readBlock(int mode, int block, byte[] password) {
        return new byte[0];
    }

    @Override
    public int writeBlock(int mode, int block, byte[] password, byte[] blockData) {
        return 0;
    }

    @Override
    public byte[] reset() {
        return new byte[0];
    }
}
