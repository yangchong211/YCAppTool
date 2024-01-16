package com.yc.apphardware.card;


import android.text.TextUtils;
import android.util.Log;

import com.tencent.wx.pos.jni.WxPosLib;
import com.tencent.wx.pos.jni.bean.ByteArrayResult;
import com.tencent.wx.pos.jni.bean.NfcResult;
import com.yc.appcontextlib.AppToolUtils;
import com.yc.apphardware.R;
import com.yc.apphardware.card.lib.CardReadManager;
import com.yc.apphardware.card.lib.MyConverterTool;
import com.yc.apphardware.card.lib.ResultMasage;
import com.yc.bytetoolutils.ByteUtils;
import com.yc.bytetoolutils.BytesHexStrUtils;
import com.yc.cardmanager.CardHelper;
import com.yc.cpucard.CpuResultMessage;
import com.yc.soundpool.SoundPoolPlayer;
import com.yc.toolutils.AppLogUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author: Administrator
 * @date: 2023/12/6
 */

public class ScanCardManager {
    private static final String M1Secret = "99a1ccc6bb40a8339e7c65445b6c7b09b6214065d3e023439b052d32f12c7d8c";
    private static final String CPUSecret = "9aa4c8bbcd40a14597701b435469080df300e0fa53a0f50370e18ac97ea2f38e";
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
        String rspData = BytesHexStrUtils.bytesToHex(rspByteArray);
        if (rspData.length() <= 4) {
            String errorData = rspData + "|" + CpuResultMessage.commandProcessSW1SW2(rspData);
            AppLogUtils.d("APDU错误：" + rspData + "，errorData " + errorData);
            if (mOnScanCardListen != null) {
                mOnScanCardListen.onScanCard(errorData, null);
            }
            return null;
        }
        String substring = rspData.substring(0, rspData.length() - 4);
        AppLogUtils.d("APDU解析OK，processRspByteArray ：" + rspData + "，data " + substring);
        return substring;
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
            String taKeys = BytesHexStrUtils.bytesToHex(cardKeyArray);
            AppLogUtils.d("Card M1 cardKeyArray = " + MyConverterTool.ByteArrToHex(cardKeyArray) + " , taKeys " + taKeys);
            ArrayList<String> keyArray = CardHelper.getInstance().getM1Card().getCardSecretList(taKeys);
            if (keyArray != null) {
                String cardData = joinM1Data1(cardNum, keyArray);
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


    private String joinM1Data1(String flipCardNum, ArrayList<String> arrayList) {
        StringBuilder sb = new StringBuilder();
        String key_6 = arrayList.get(0);
        if (!TextUtils.isEmpty(key_6) && key_6.length() == 12) {
            byte[] password = MyConverterTool.HexToByteArr(key_6);
            byte[] cardNo = MyConverterTool.HexToByteArr(flipCardNum);
            boolean sector6Auth = CardReadManager.getInstance().CardM1Authority(CardReadManager.KEY_MODE_A, password, cardNo, (byte) 24);
            Log.d("Card M1", "扇区6 " + CardReadManager.KEY_MODE_A + " , " + key_6 + " , " + flipCardNum + " , " + sector6Auth);
            if (sector6Auth) {
                byte[] sector6_1 = CardReadManager.getInstance().CardM1BlockRead((byte) 24);
                byte[] sector6_2 = CardReadManager.getInstance().CardM1BlockRead((byte) 25);
                byte[] sector6_3 = CardReadManager.getInstance().CardM1BlockRead((byte) 26);
                String s61 = ByteUtils.bytesToHex(sector6_1);
                String s62 = ByteUtils.bytesToHex(sector6_2);
                String s63 = ByteUtils.bytesToHex(sector6_3);
                AppLogUtils.d("Card M1 扇区6密钥认证成功：" + s61 + s62 + s63);
                sb.append(s61).append(s62).append(s63);
            } else {
                AppLogUtils.d("M1 扇区6密钥认证失败");
                if (mOnScanCardListen != null) {
                    mOnScanCardListen.onScanCard("扇区读取失败", null);
                }
                return null;
            }
        }

        String key_7 = arrayList.get(1);
        if (!TextUtils.isEmpty(key_7) && key_7.length() == 12) {
            boolean sector7Auth = CardReadManager.getInstance().CardM1Authority(CardReadManager.KEY_MODE_A, MyConverterTool.HexToByteArr(key_7), MyConverterTool.HexToByteArr(flipCardNum), (byte) 28);
            Log.d("Card M1", "扇区7 " + CardReadManager.KEY_MODE_A + " , " + key_7 + " , " + flipCardNum + " , " + sector7Auth);

            if (sector7Auth) {
                byte[] sector7_1 = CardReadManager.getInstance().CardM1BlockRead((byte) 28);
                byte[] sector7_2 = CardReadManager.getInstance().CardM1BlockRead((byte) 29);
                byte[] sector7_3 = CardReadManager.getInstance().CardM1BlockRead((byte) 30);
                String s71 = ByteUtils.bytesToHex(sector7_1);
                String s72 = ByteUtils.bytesToHex(sector7_2);
                String s73 = ByteUtils.bytesToHex(sector7_3);
                AppLogUtils.d("Card M1 扇区7密钥认证成功：" + s71 + s72 + s73);
                sb.append(s71).append(s72).append(s73);
            } else {
                AppLogUtils.d("M1 扇区7密钥认证失败");
                if (mOnScanCardListen != null) {
                    mOnScanCardListen.onScanCard("扇区读取失败", null);
                }
                return null;
            }
        }

        String key_8 = arrayList.get(2);
        if (!TextUtils.isEmpty(key_8) && key_8.length() == 12) {
            boolean sector8Auth = CardReadManager.getInstance().CardM1Authority(CardReadManager.KEY_MODE_A, MyConverterTool.HexToByteArr(key_8), MyConverterTool.HexToByteArr(flipCardNum), (byte) 32);
            Log.d("Card M1", "扇区8 " + CardReadManager.KEY_MODE_A + " , " + key_8 + " , " + flipCardNum + " , " + sector8Auth);
            if (sector8Auth) {
                byte[] sector8_1 = CardReadManager.getInstance().CardM1BlockRead((byte) 32);
                byte[] sector8_2 = CardReadManager.getInstance().CardM1BlockRead((byte) 33);
                String s81 = ByteUtils.bytesToHex(sector8_1);
                String s82 = ByteUtils.bytesToHex(sector8_2);
                AppLogUtils.d("Card M1 扇区8密钥认证成功：" + s81 + s82);
                sb.append(ByteUtils.bytesToHex(sector8_1)).append(ByteUtils.bytesToHex(sector8_2));
            } else {
                AppLogUtils.d("M1 扇区8密钥认证失败");
                if (mOnScanCardListen != null) {
                    mOnScanCardListen.onScanCard("扇区读取失败", null);
                }
                return null;
            }
        }
        return sb.toString();
    }

    private void decodeCpuCard(String cardNum) {
        //先将旋转卡号
        String flipCardNum = WeCardCardHelper.flipCardNum(cardNum);
        AppLogUtils.d("Card CPU flipCardNum : " + flipCardNum + " , " + cardNum + " , 天安 CPU卡秘钥 " + CPUSecret);
        //复位
        byte[] resetDataArray = CardReadManager.getInstance().CardCpuReset();
        String resetData = processRspByteArray(resetDataArray);
        AppLogUtils.d("Card CPU 复位 : " + resetData);
        if (resetData == null) {
            //如果复位的数据不为空，则说明复位成功
            return;
        }
        //选择文件
        String selectFileOrder = WeCardCardHelper.getSelectFileOrder();
        AppLogUtils.d("Card CPU 选择文件 : " + selectFileOrder);
        byte[] bytes = MyConverterTool.HexToByteArr(selectFileOrder);
        //apdu操作
        byte[] selectApplyDataArray = CardReadManager.getInstance().CardCpuSendCosCmd(bytes);
        String selectApplyData = processRspByteArray(selectApplyDataArray);
        AppLogUtils.d("Card CPU selectApplyData : " + selectApplyData);
        if (selectApplyData == null) {
            return;
        }

        //获取随机数
        byte[] randomDataArray = CardReadManager.getInstance().CardCpuSendCosCmd(MyConverterTool.HexToByteArr(WeCardCardHelper.getRandomOrder()));
        String randomData = processRspByteArray(randomDataArray);
        AppLogUtils.d("Card CPU 获取随机数 : " + randomData);
        if (randomData == null) {
            return;
        }

        //微卡密钥换算
        ByteArrayResult byteArrayResult;
        byteArrayResult = WxPosLib.getInstance().getCardKey(flipCardNum, CPUSecret,
                "", "", "", "", "");
        if (byteArrayResult.getCode() == 1) {
            byte[] cardKeyArray = byteArrayResult.getByteArray();
            String s = BytesHexStrUtils.bytesToHex(cardKeyArray);
            //获取读取CPU卡16文件的指令
            String cardOrder = WeCardCardHelper.generateReadCardOrder(s, MyConverterTool.HexToByteArr(randomData));
            AppLogUtils.d("Card CPU 密钥 " + s + " , 获取读取CPU卡16文件的指令 :" + cardOrder);
            if (!TextUtils.isEmpty(cardOrder)) {
                byte[] rspCardOrderArray = CardReadManager.getInstance().CardCpuSendCosCmd(MyConverterTool.HexToByteArr(cardOrder));
                String rspCardOrder = processRspByteArray(rspCardOrderArray);
                AppLogUtils.d("Card CPU 解析指令 " + rspCardOrder);
                if (rspCardOrder == null) {
                    return;
                }
                NfcResult nfcResult = WxPosLib.getInstance().getCardMessage(rspCardOrder, flipCardNum, "", "", "", "", "");
                AppLogUtils.d("Card CPU nfcResult = " + nfcResult);
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
                        SoundPoolPlayer.create(AppToolUtils.getApp(), R.raw.beep).play();
                        String cardNum = cardType.getCardNo();
//                        decodeM1Card(cardNum);

                        decodeM1Card2(cardNum);

                    } else if (cardType.getCardType() == CardReadManager.CardType.CPU_CARD.getCardType()) {
                        SoundPoolPlayer.create(AppToolUtils.getApp(), R.raw.beep).play();
                        String cardNum = cardType.getCardNo();
//                        decodeCpuCard(cardNum);
                        decodeCpuCard2(cardNum);
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
            //f23e9889d032736be51a5d866bceef43
            String taKeys = BytesHexStrUtils.bytesToHex(cardKeyArray);
            //String taKeys = MyConverterTool.bytesToHex(cardKeyArray);
            AppLogUtils.d("Card M1 cardKeyArray = " + BytesHexStrUtils.bytesToHex2(cardKeyArray) + " , taKeys " + taKeys);
            ArrayList<String> keyArray = CardHelper.getInstance().getM1Card().getCardSecretList(taKeys);
            if (keyArray != null) {
                String cardData = joinM1Data(cardNum, flipCardNum, keyArray);
                AppLogUtils.d("Card M1 cardData = " + cardData);
                if (TextUtils.isEmpty(cardData)) {
                    return;
                }
                //将卡数据转化为数据bean，相当于解析数据。这一步解析数据
                NfcResult nfcResult = WxPosLib.getInstance().getCardMessage(cardData, flipCardNum, "", "", "", "", "");
                AppLogUtils.d("Card M1 nfcResult = " + nfcResult);
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


    private void decodeCpuCard2(String cardNum) {
        //先将旋转卡号
        String flipCardNum = WeCardCardHelper.flipCardNum(cardNum);
        AppLogUtils.d("Card CPU flipCardNum : " + flipCardNum + " , " + cardNum + " , 天安 CPU卡秘钥 " + CPUSecret);
        //复位
        byte[] resetDataArray = CardHelper.getInstance().getCpuCard().reset();
        String resetData = processRspByteArray(resetDataArray);
        AppLogUtils.d("Card CPU 复位 : " + resetData);
        if (resetData == null) {
            return;
        }
        //选择文件
        String selectFileOrder = WeCardCardHelper.getSelectFileOrder();
        //Card CPU 选择文件 : 00a40000023f01
        AppLogUtils.d("Card CPU 选择文件 : " + selectFileOrder);
        byte[] bytes = MyConverterTool.HexToByteArr(selectFileOrder);
        //Card CPU selectApplyData : 6f33840854582e5041593031a5279f0801029f0c200000000000000000000000000000000000000000000000000000000000000000
        byte[] selectApplyDataArray = CardReadManager.getInstance().CardCpuSendCosCmd(bytes);
        String selectApplyData = processRspByteArray(selectApplyDataArray);
        AppLogUtils.d("Card CPU selectApplyData : " + selectApplyData);
        if (selectApplyData == null) {
            return;
        }

        //获取随机数
        String randomOrder = WeCardCardHelper.getRandomOrder();
        byte[] randomOrderBytes = MyConverterTool.HexToByteArr(randomOrder);
        byte[] randomDataArray = CardReadManager.getInstance().CardCpuSendCosCmd(randomOrderBytes);
        String randomData = processRspByteArray(randomDataArray);
        AppLogUtils.d("Card CPU 获取随机数 : " + randomData);
        if (randomData == null) {
            return;
        }

        //微卡密钥换算
        ByteArrayResult byteArrayResult;
        byteArrayResult = WxPosLib.getInstance().getCardKey(flipCardNum, CPUSecret, "", "", "", "", "");
        if (byteArrayResult.getCode() == 1) {
            byte[] cardKeyArray = byteArrayResult.getByteArray();
            //拿到密钥
            String s = BytesHexStrUtils.bytesToHex(cardKeyArray);
            //获取读取CPU卡16文件的指令
            String cardOrder = WeCardCardHelper.generateReadCardOrder(s, MyConverterTool.HexToByteArr(randomData));
            AppLogUtils.d("Card CPU 密钥 " + s + " , 获取读取CPU卡16文件的指令 :" + cardOrder);
            if (!TextUtils.isEmpty(cardOrder)) {
                byte[] rspCardOrderArray = CardReadManager.getInstance().CardCpuSendCosCmd(MyConverterTool.HexToByteArr(cardOrder));
                String rspCardOrder = processRspByteArray(rspCardOrderArray);
                AppLogUtils.d("Card CPU 解析指令 " + rspCardOrder);
                if (rspCardOrder == null) {
                    return;
                }
                NfcResult nfcResult = WxPosLib.getInstance().getCardMessage(rspCardOrder, flipCardNum, "", "", "", "", "");
                AppLogUtils.d("Card CPU nfcResult = " + nfcResult);
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


    private String joinM1Data(String cardNum, String flipCardNum, ArrayList<String> arrayList) {
        StringBuilder sb = new StringBuilder();
        String key_6 = arrayList.get(0);
        byte[] password6 = BytesHexStrUtils.hexToBytes1(key_6);
        if (!TextUtils.isEmpty(key_6) && key_6.length() == 12) {
            int block61 = 24;
            byte[] data61 = CardHelper.getInstance().getM1Card().readBlock(cardNum, CardReadManager.KEY_MODE_A, block61, password6);
            String data61String = "";
            if (data61 != null) {
                data61String = BytesHexStrUtils.bytesToHex(data61).replace("0", "");
            }
            if (data61 != null && !TextUtils.isEmpty(data61String)) {
                byte[] data62 = CardHelper.getInstance().getM1Card().readBlock(cardNum, CardReadManager.KEY_MODE_A, block61 + 1, password6);
                byte[] data63 = CardHelper.getInstance().getM1Card().readBlock(cardNum, CardReadManager.KEY_MODE_A, block61 + 2, password6);
                //byte[] data63 = CardReadManager.getInstance().CardM1BlockRead((byte) 26);
                if (data62 != null && data63 != null) {
                    String s2 = ByteUtils.bytesToHex(data62);
                    String s3 = ByteUtils.bytesToHex(data63);
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
                data71String = BytesHexStrUtils.bytesToHex(data71).replace("0", "");
            }
            if (data71 != null && !TextUtils.isEmpty(data71String)) {
                byte[] data72 = readBlock(cardNum, block71 + 1, key_7);
                byte[] data73 = readBlock(cardNum, block71 + 2, key_7);
                if (data72 != null && data73 != null) {
                    String s2 = ByteUtils.bytesToHex(data72);
                    String s3 = ByteUtils.bytesToHex(data73);
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
                data81String = ByteUtils.bytesToHex(data81);
            }
            if (data81 != null && !TextUtils.isEmpty(data81String)) {
                byte[] data82 = readBlock(cardNum, block81 + 1, key_8);
                if (data82 != null) {
                    String s2 = ByteUtils.bytesToHex(data82);
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

}
