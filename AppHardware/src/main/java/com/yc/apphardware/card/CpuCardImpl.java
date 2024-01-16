package com.yc.apphardware.card;

import android.text.TextUtils;

import com.yc.apphardware.card.lib.CardReadManager;
import com.yc.bytetoolutils.BytesHexStrUtils;
import com.yc.cpucard.AbstractCpuCard;
import com.yc.toolutils.AppLogUtils;

import java.util.Arrays;

public class CpuCardImpl extends AbstractCpuCard {


    @Override
    public String search() {
        //开始寻卡
        CardReadManager.CardType cardType = CardReadManager.getInstance().SearchCardType();
        String carNumber = cardType.getCardNo();
        if (!TextUtils.isEmpty(carNumber) && carNumber.length() == 8) {
            carNumber = WeCardCardHelper.flipCardNum(carNumber);
        }
        return carNumber;
    }

    @Override
    public String[] sendAPDU(byte[] cosCmd) {
        byte[] byteData = CardReadManager.getInstance().CardCpuSendCosCmd(cosCmd);
        String[] result = new String[2];
        if (byteData != null && byteData.length >= 2) {
            //状态码
            byte[] sw1sw2 = new byte[2];
            System.arraycopy(byteData, byteData.length - sw1sw2.length, sw1sw2, 0, sw1sw2.length);
            //真实数据
            byte[] data = new byte[byteData.length - sw1sw2.length];
            System.arraycopy(byteData, 0, data, 0, data.length);
            result[0] = BytesHexStrUtils.bytesToHex(sw1sw2);
            result[1] = BytesHexStrUtils.bytesToHex(data);
        } else {
            result[0] = "0000";
        }
        //[0000, null]
        AppLogUtils.d("Card CPU 开始寻卡 " + Arrays.toString(result));
        return result;
    }

    @Override
    public byte[] reset() {
        byte[] bytes = CardReadManager.getInstance().CardCpuReset();
        AppLogUtils.d("Card CPU 复位 " + BytesHexStrUtils.bytesToHex(bytes));
        return bytes;
    }
}
