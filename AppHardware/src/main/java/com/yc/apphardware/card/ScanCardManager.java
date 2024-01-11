package com.yc.apphardware.card;


import android.text.TextUtils;
import android.util.Log;

import com.android.scanCard.utils.CardReadManager;
import com.android.scanCard.utils.MyConverterTool;
import com.android.scanCard.utils.ResultMasage;
import com.tencent.wx.pos.jni.WxPosLib;
import com.tencent.wx.pos.jni.bean.ByteArrayResult;
import com.tencent.wx.pos.jni.bean.NfcResult;
import com.yc.cardmanager.CardHelper;
import com.yc.mifarecard.InterM1Card;
import com.yc.toolutils.AppLogUtils;

import java.util.ArrayList;

/**
 * @author: Administrator
 * @date: 2023/12/6
 */

public class ScanCardManager {
    private static final String M1Secret = "99a1ccc6bb40a8339e7c65445b6c7b09b6214065d3e023439b052d32f12c7d8c";
    private static final String oCode = "1010169056";

    //读卡线程
    ScanCardThread mScanCardThread;
    //读卡回调接口
    private OnScanCardListen mOnScanCardListen;
    //读卡线程标志
    private static boolean mIsScanCard = false;
    //读卡模块打开标志
    boolean mDevIsOpen;

    public ScanCardManager(OnScanCardListen onScanCardListen) {
        mDevIsOpen = CardReadManager.getInstance().CardOpen();
        mOnScanCardListen = onScanCardListen;
        AppLogUtils.d("Card mDevIsOpen " + mDevIsOpen);
    }

    public void startScanCard() {
        if (mScanCardThread == null) {
            mScanCardThread = new ScanCardThread();
            mScanCardThread.start();
        }
    }

    public void stopScanCard() {
        if (mScanCardThread != null) {
            mScanCardThread.isInterrupted();
            mScanCardThread = null;
        }
    }

    public void setScanCard(boolean scan) {
        mIsScanCard = scan;
    }

    public void closeScanCard() {
        stopScanCard();
        mIsScanCard = false;
        mDevIsOpen = false;
        CardReadManager.getInstance().destory();
    }

    private String processRspByteArray(byte[] rspByteArray) {
        if (rspByteArray == null) {
            AppLogUtils.d("APDU通讯失败");
            mOnScanCardListen.onScanCard("APDU通讯失败", null);
            return null;
        }
        String rspData = MyConverterTool.bytesToHex(rspByteArray);
        if (rspData.length() <= 4) {
            AppLogUtils.d("APDU错误：" + rspData);
            String errorData = rspData + "|" + ResultMasage.CommandProcessSW1SW2(rspData);
            if (mOnScanCardListen != null) {
                mOnScanCardListen.onScanCard(errorData, null);
            }
            return null;
        }
        return rspData.substring(0, rspData.length() - 4);
    }

    private String joinM1Data(String cardNum, String flipCardNum, ArrayList<String> arrayList) {
        StringBuilder sb = new StringBuilder();
        String key_6 = arrayList.get(0);
        byte[] password6 = MyConverterTool.HexToByteArr(key_6);
        if (!TextUtils.isEmpty(key_6) && key_6.length() == 12) {
            int block61 = 24;
            byte[] data61 = CardHelper.getInstance().getM1Card().readBlock(cardNum, CardReadManager.KEY_MODE_A, block61, password6);
            String data61String = "";
            if (data61 != null) {
                data61String = MyConverterTool.bytesToHex(data61).replace("0", "");
            }
            if (data61 != null && !TextUtils.isEmpty(data61String)) {
                byte[] data62 = CardHelper.getInstance().getM1Card().readBlock(cardNum, CardReadManager.KEY_MODE_A, block61 + 1, password6);
                byte[] data63 = CardHelper.getInstance().getM1Card().readBlock(cardNum, CardReadManager.KEY_MODE_A, block61 + 2, password6);
                //byte[] data63 = CardReadManager.getInstance().CardM1BlockRead((byte) 26);
                if (data62 != null && data63 != null) {
                    String s2 = ToolUtils.bytesToHex(data62);
                    String s3 = ToolUtils.bytesToHex(data63);
                    sb.append(data61String).append(s2).append(s3);
                    AppLogUtils.d("Card M1 扇区6密钥认证成功：" + sb);
                } else {
                    AppLogUtils.d("Card M1 扇区6密钥认证失败2：请检查秘钥-" + key_6);
                    if (mOnScanCardListen != null) {
                        mOnScanCardListen.onScanCard("扇区6读取失败", null);
                    }
                    return null;
                }
            } else {
                AppLogUtils.d("Card M1 扇区6密钥认证失败1：请检查秘钥-" + key_6);
                if (mOnScanCardListen != null) {
                    mOnScanCardListen.onScanCard("扇区6读取失败", null);
                }
                return null;
            }
        }

        String key_7 = arrayList.get(1);
        if (!TextUtils.isEmpty(key_7) && key_7.length() == 12) {
            int block71 = 28;
            byte[] data71 = readBlock(cardNum, block71, key_7);
            String data71String = "";
            if (data71 != null) {
                data71String = MyConverterTool.bytesToHex(data71).replace("0", "");
            }
            if (data71 != null && !TextUtils.isEmpty(data71String)) {
                byte[] data72 = readBlock(cardNum, block71 + 1, key_7);
                byte[] data73 = readBlock(cardNum, block71 + 2, key_7);
                if (data72 != null && data73 != null) {
                    String s2 = ToolUtils.bytesToHex(data72);
                    String s3 = ToolUtils.bytesToHex(data73);
                    sb.append(data71String).append(s2).append(s3);
                    AppLogUtils.d("Card M1 扇区7密钥认证成功：" + data71String + s2 + s3);
                } else {
                    AppLogUtils.d("Card M1 扇区7密钥认证失败2：请检查秘钥-" + key_7);
                    if (mOnScanCardListen != null) {
                        mOnScanCardListen.onScanCard("扇区7读取失败", null);
                    }
                    return null;
                }
            } else {
                AppLogUtils.d("Card M1 扇区7密钥认证失败1：请检查秘钥-" + key_7);
                if (mOnScanCardListen != null) {
                    mOnScanCardListen.onScanCard("扇区7读取失败", null);
                }
                return null;
            }
        }

        String key_8 = arrayList.get(2);
        if (!TextUtils.isEmpty(key_8) && key_8.length() == 12) {
            int block81 = 32;
            byte[] data81 = readBlock(cardNum, block81, key_8);
            String data81String = "";
            if (data81 != null) {
                data81String = ToolUtils.bytesToHex(data81);
            }
            if (data81 != null && !TextUtils.isEmpty(data81String)) {
                byte[] data82 = readBlock(cardNum, block81 + 1, key_8);
                if (data82 != null) {
                    String s2 = ToolUtils.bytesToHex(data82);
                    sb.append(data81String).append(s2);
                    AppLogUtils.d("Card M1 扇区8密钥认证成功：" + data81String + s2);
                } else {
                    AppLogUtils.d("Card M1 扇区8密钥认证失败2：请检查秘钥-" + key_8);
                    if (mOnScanCardListen != null) {
                        mOnScanCardListen.onScanCard("扇区8读取失败", null);
                    }
                    return null;
                }
            } else {
                AppLogUtils.d("Card M1 扇区8密钥认证失败1：请检查秘钥-" + key_8);
                if (mOnScanCardListen != null) {
                    mOnScanCardListen.onScanCard("扇区8读取失败", null);
                }
                return null;
            }
        }
        return sb.toString();
    }

    private byte[] readBlock(String cardNum, int block, String key) {
        if (TextUtils.isEmpty(cardNum)) {
            return null;
        }
        //fix 一定要转化为小写，否则会出现验证密钥不成功
        String cardNumNo = cardNum.toLowerCase();
        byte[] password = MyConverterTool.HexToByteArr(key);
        byte[] cardNo = MyConverterTool.HexToByteArr(cardNumNo);
        //先验证秘钥
        boolean result = CardReadManager.getInstance().CardM1Authority(CardReadManager.KEY_MODE_A, password, cardNo, (byte) block);
        Log.d("Card M1", "扇区 " + block + " , 密钥 " + key + " , 卡号 " + cardNumNo + " , 验证结果" + result);
        if (result) {
            return CardReadManager.getInstance().CardM1BlockRead((byte) block);
        }
        return null;
    }


    private void decodeM1Card(String cardNum) {
        //先将旋转卡号
        String flipCardNum = WeCardCardHelper.flipCardNum(cardNum);
        String cardNumber = flipCardNum.toLowerCase();
        AppLogUtils.d("Card M1 flipCardNum : " + flipCardNum + " , " + cardNumber + " , 天安 M1卡秘钥 " + M1Secret);
        //微卡密钥换算
        ByteArrayResult byteArrayResult;
        byteArrayResult = WxPosLib.getInstance().getCardKey(flipCardNum, M1Secret,
                "", "", "", "", "");
        AppLogUtils.d("Card M1 code = " + byteArrayResult.getCode() + " , ");
        if (byteArrayResult.getCode() == 1) {
            byte[] cardKeyArray = byteArrayResult.getByteArray();
            String taKeys = MyConverterTool.bytesToHex(cardKeyArray);
            AppLogUtils.d("Card M1 cardKeyArray = " + MyConverterTool.ByteArrToHex(cardKeyArray) + " , taKeys " + taKeys);
            ArrayList<String> keyArray = CardHelper.getInstance().getM1Card().getCardSecretList(taKeys);
            if (keyArray != null) {
                String cardData = joinM1Data(cardNum, flipCardNum, keyArray);
                AppLogUtils.d("Card M1 cardData = " + cardData);
                if (TextUtils.isEmpty(cardData)) {
                    return;
                }
                NfcResult nfcResult = WxPosLib.getInstance().getCardMessage(cardData, flipCardNum, "", "", "", "", "");
                if (nfcResult == null) {
                    if (mOnScanCardListen != null) {
                        mOnScanCardListen.onScanCard("读卡失败", null);
                    }
                    return;
                }
                AppLogUtils.d("nfcResult = " + nfcResult);
                if (mOnScanCardListen != null) {
                    mOnScanCardListen.onScanCard(nfcResult.getUserName(), nfcResult.getStudentId());
                }
            } else {
                if (mOnScanCardListen != null) {
                    mOnScanCardListen.onScanCard("卡密截取失败", null);
                }
            }
        } else {
            if (mOnScanCardListen != null) {
                mOnScanCardListen.onScanCard(byteArrayResult.getMsg(), null);
            }
        }
    }

    private void decodeCpuCard(String cardNum) {
        //先将旋转卡号
        String flipCardNum = WeCardCardHelper.flipCardNum(cardNum);
        //复位
        byte[] resetDataArray = CardReadManager.getInstance().CardCpuReset();
        String resetData = processRspByteArray(resetDataArray);
        if (resetData == null) {
            return;
        }
        //选择文件
        byte[] selectApplyDataArray = CardReadManager.getInstance().CardCpuSendCosCmd(MyConverterTool.HexToByteArr(WeCardCardHelper.getSelectFileOrder()));
        String selectApplyData = processRspByteArray(selectApplyDataArray);
        if (selectApplyData == null) {
            return;
        }

        //获取随机数
        byte[] randomDataArray = CardReadManager.getInstance().CardCpuSendCosCmd(MyConverterTool.HexToByteArr(WeCardCardHelper.getRandomOrder()));
        String randomData = processRspByteArray(randomDataArray);
        if (randomData == null) {
            return;
        }

        //微卡密钥换算
        ByteArrayResult byteArrayResult;
        byteArrayResult = WxPosLib.getInstance().getCardKey(flipCardNum, M1Secret,
                "", "", "", "", "");
        if (byteArrayResult.getCode() == 1) {
            byte[] cardKeyArray = byteArrayResult.getByteArray();
            //获取读取CPU卡16文件的指令
            String cardOrder = WeCardCardHelper.generateReadCardOrder(MyConverterTool.bytesToHex(cardKeyArray), MyConverterTool.HexToByteArr(randomData));
            if (!TextUtils.isEmpty(cardOrder)) {
                byte[] rspCardOrderArray = CardReadManager.getInstance().CardCpuSendCosCmd(MyConverterTool.HexToByteArr(cardOrder));
                String rspCardOrder = processRspByteArray(rspCardOrderArray);
                if (rspCardOrder == null) {
                    return;
                }
                NfcResult nfcResult = WxPosLib.getInstance().getCardMessage(rspCardOrder, flipCardNum, "", "", "", "", "");
                if (nfcResult == null) {
                    if (mOnScanCardListen != null) {
                        mOnScanCardListen.onScanCard("读卡失败", null);
                    }
                    return;
                }
                if (mOnScanCardListen != null) {
                    mOnScanCardListen.onScanCard(nfcResult.getUserName(), nfcResult.getStudentId());
                }
            } else {
                if (mOnScanCardListen != null) {
                    mOnScanCardListen.onScanCard("读取CPU卡16文件指令失败", null);
                }
            }
        } else {
            if (mOnScanCardListen != null) {
                mOnScanCardListen.onScanCard(byteArrayResult.getMsg(), null);
            }
        }
    }

    private class ScanCardThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!interrupted()) {
                if (!mDevIsOpen) {
                    AppLogUtils.d("Card 未找到读卡设备");
                    return;
                }
                if (!mIsScanCard) {
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    CardReadManager.CardType cardType = CardReadManager.getInstance().SearchCardType();
                    //AppLogUtils.d("Card 开始寻卡 " + cardType.getCardType() + " , " + cardType.getCardNo());
                    if (cardType.getCardType() == CardReadManager.CardType.M1_CARD.getCardType()) {
                        ToolUtils.playBeep();
                        String cardNum = cardType.getCardNo();
//                        decodeM1Card(cardNum);

//                        InterM1Card m1Card = CardHelper.getInstance().getM1Card();
//                        String cardNum = m1Card.searchNo();
                        decodeM1Card2(cardNum);

                    } else if (cardType.getCardType() == CardReadManager.CardType.CPU_CARD.getCardType()) {
                        ToolUtils.playBeep();
                        String cardNum = cardType.getCardNo();
                        decodeCpuCard(cardNum);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void decodeM1Card2(String cardNum) {
        //先将旋转卡号
        String flipCardNum = WeCardCardHelper.flipCardNum(cardNum);
        String cardNumber = flipCardNum.toLowerCase();
        AppLogUtils.d("Card M1 flipCardNum : " + flipCardNum + " , " + cardNumber + " , 天安 M1卡秘钥 " + M1Secret);
        //微卡密钥换算
        byte[] cardKeyArray = CardHelper.getInstance().getM1Card().getCardSecret(flipCardNum);
        if (cardKeyArray != null) {
            String taKeys = MyConverterTool.bytesToHex(cardKeyArray);
            AppLogUtils.d("Card M1 cardKeyArray = " + MyConverterTool.ByteArrToHex(cardKeyArray) + " , taKeys " + taKeys);
            ArrayList<String> keyArray = CardHelper.getInstance().getM1Card().getCardSecretList(taKeys);
            if (keyArray != null) {
                String cardData = joinM1Data(cardNum, flipCardNum, keyArray);
                AppLogUtils.d("Card M1 cardData = " + cardData);
                if (TextUtils.isEmpty(cardData)) {
                    return;
                }
                NfcResult nfcResult = WxPosLib.getInstance().getCardMessage(cardData, flipCardNum, "", "", "", "", "");
                if (nfcResult == null) {
                    if (mOnScanCardListen != null) {
                        mOnScanCardListen.onScanCard("读卡失败", null);
                    }
                    return;
                }
                AppLogUtils.d("nfcResult = " + nfcResult);
                if (mOnScanCardListen != null) {
                    mOnScanCardListen.onScanCard(nfcResult.getUserName(), nfcResult.getStudentId());
                }
            } else {
                if (mOnScanCardListen != null) {
                    mOnScanCardListen.onScanCard("卡密截取失败", null);
                }
            }
        } else {
            if (mOnScanCardListen != null) {
                mOnScanCardListen.onScanCard("异常", null);
            }
        }
    }
}
