package com.yc.apphardware.card;

import android.text.TextUtils;

import com.yc.apphardware.card.lib.CardReadManager;
import com.yc.bytetoolutils.BytesHexStrUtils;
import com.yc.cpucard.AbstractCpuCard;
import com.yc.toolutils.AppLogUtils;

import java.util.Arrays;

public class CpuCardImpl extends AbstractCpuCard {

    private static final String TAG = "Card CPU : ";

    @Override
    public String search() {
        //开始寻卡
//        CardReadManager.CardType cardType = CardReadManager.getInstance().SearchCardType();
//        String carNumber = cardType.getCardNo();
        String carNumber = CardReadManager.getInstance().CardSearch();
        if (!TextUtils.isEmpty(carNumber) && carNumber.length() == 8) {
            carNumber = WeCardCardHelper.flipCardNum(carNumber);
        }
        AppLogUtils.d(TAG + carNumber);
        return carNumber;
    }

    @Override
    public String[] sendAPDU(byte[] cosCmd) {
        byte[] byteData = CardReadManager.getInstance().CardCpuSendCosCmd(cosCmd);
        String[] result = new String[2];
        if (byteData != null && byteData.length >= 2) {
            //举个例子：2f4bd2749000
            //result[0]是9000，result[1]是2f4bd274
            //状态码
            byte[] sw1sw2 = new byte[2];
            System.arraycopy(byteData, byteData.length - sw1sw2.length, sw1sw2, 0, sw1sw2.length);
            //真实数据
            byte[] data = new byte[byteData.length - sw1sw2.length];
            System.arraycopy(byteData, 0, data, 0, data.length);
            String s0 = BytesHexStrUtils.bytesToHex(sw1sw2);
            String s1 = BytesHexStrUtils.bytesToHex(data);
            result[0] = s0;
            result[1] = s1;
        } else {
            result[0] = "0000";
        }
        //[0000, null]
        AppLogUtils.d(TAG+"发送指令数据 " + Arrays.toString(result));
        return result;
    }

    @Override
    public byte[] reset() {
        byte[] bytes = CardReadManager.getInstance().CardCpuReset();
        AppLogUtils.d(TAG+"复位 " + BytesHexStrUtils.bytesToHex(bytes));
        return bytes;
    }
}
