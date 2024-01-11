package com.yc.apphardware.card;

import android.text.TextUtils;

import com.android.scanCard.utils.CardReadManager;
import com.tencent.wx.pos.jni.WxPosLib;
import com.tencent.wx.pos.jni.bean.ByteArrayResult;
import com.yc.mifarecard.AbstractM1Card;
import com.yc.mifarecard.IM1Function;
import com.yc.toolutils.AppLogUtils;

public class M1CardImpl extends AbstractM1Card {

    String cardNumber = "";
    private static final String M1Secret = "99a1ccc6bb40a8339e7c65445b6c7b09b6214065d3e023439b052d32f12c7d8c";

    @Override
    public String searchNo() {
        CardReadManager.CardType cardType = CardReadManager.getInstance().SearchCardType();
        String carNumberOrigin = cardType.getCardNo();
        String newCarNumber = carNumberOrigin;
        if (!TextUtils.isEmpty(newCarNumber) && newCarNumber.length() == 8) {
            cardNumber = WeCardCardHelper.flipCardNum(newCarNumber);
        }
        AppLogUtils.d("Card M1 开始寻卡 " + cardType.getCardType() + " , " + cardType.getCardNo());
        return carNumberOrigin;
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

    @Override
    public byte[] getCardSecret(String cardNo) {
        //微卡密钥换算
        ByteArrayResult byteArrayResult;
        byteArrayResult = WxPosLib.getInstance().getCardKey(cardNo, M1Secret, "", "", "", "","");
        if (byteArrayResult.getCode() == 1) {
            return byteArrayResult.getByteArray();
        }
        return null;
    }

}
