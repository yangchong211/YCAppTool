package com.yc.apphardware.card;

import android.text.TextUtils;
import android.util.Log;

import com.tencent.wx.pos.jni.WxPosLib;
import com.tencent.wx.pos.jni.bean.ByteArrayResult;
import com.yc.apphardware.card.lib.CardReadManager;
import com.yc.apphardware.card.lib.MyConverterTool;
import com.yc.bytetoolutils.BytesHexStrUtils;
import com.yc.mifarecard.AbstractM1Card;
import com.yc.toolutils.AppLogUtils;

import java.util.ArrayList;

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
    public byte[] readBlock(String cardNum , int mode, int block, byte[] password) {
        //fix 一定要转化为小写，否则会出现验证密钥不成功
        String cardNumNo = cardNum.toLowerCase();
        byte[] cardNo = BytesHexStrUtils.hexToBytes1(cardNumNo);
        //先验证秘钥
        boolean result = CardReadManager.getInstance().CardM1Authority(CardReadManager.KEY_MODE_A, password, cardNo, (byte) block);
        Log.d("Card M1", "读数据扇区 " + block + " , 密钥 " + MyConverterTool.ByteArrToHex(password) + " , 卡号 " + cardNumNo + " , 验证结果" + result);
        if (result) {
            return CardReadManager.getInstance().CardM1BlockRead((byte) block);
        }
        return null;
    }

    @Override
    public int writeBlock(String cardNum , int mode, int block, byte[] password, byte[] blockData) {
        String cardNumNo = cardNum.toLowerCase();
        byte[] cardNo = BytesHexStrUtils.hexToBytes1(cardNumNo);
        //先验证秘钥
        boolean result = CardReadManager.getInstance().CardM1Authority(CardReadManager.KEY_MODE_A, password, cardNo, (byte) block);
        Log.d("Card M1", "写数据扇区 " + block + " , 密钥 " + MyConverterTool.bytesToHex(password) + " , 卡号 " + cardNumNo + " , 验证结果" + result);
        if (result) {
            boolean writeResult = CardReadManager.getInstance().CardM1BlockWrite((byte) block, blockData);
            return writeResult ? 1 : 0;
        }
        return 0;
    }

    @Override
    public byte[] reset() {
        String carNumber = CardReadManager.getInstance().CardSearch();
        return !TextUtils.isEmpty(carNumber) ? new byte[1] : null;
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

    @Override
    public ArrayList<String> getCardSecretList(String taKeys) {
        if (!TextUtils.isEmpty(taKeys) && taKeys.length() == 32) {
            String key_6 = taKeys.substring(0, 12);
            String key_7 = taKeys.substring(12, 24);
            String key_8 = taKeys.substring(0, 2) + taKeys.substring(12, 14) + taKeys.substring(24, 32);
            String key_9 = taKeys.substring(2, 4) + taKeys.substring(6, 8) + taKeys.substring(8, 10)
                    + taKeys.substring(12, 14) + taKeys.substring(16, 18) + taKeys.substring(20, 22);
            ArrayList<String> keysTass = new ArrayList<>();
            keysTass.add(key_6);
            keysTass.add(key_7);
            keysTass.add(key_8);
            keysTass.add(key_9);
            return keysTass;
        }
        return null;
    }

}
