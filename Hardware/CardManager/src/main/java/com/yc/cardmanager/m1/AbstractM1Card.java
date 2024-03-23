package com.yc.cardmanager.m1;

import android.text.TextUtils;

import com.yc.toolutils.system.AppLogUtils;
import com.yc.toolutils.bytes.ByteUtils;
import com.yc.toolutils.bytes.BytesHexStrUtils;

import java.util.ArrayList;

public abstract class AbstractM1Card implements InterM1Card, IM1Function {

    public static final byte KEY_MODE_A = 0x60;
    public static final byte KEY_MODE_B = 0x61;

    @Override
    public String verify(String cardNo) {
        String lowCardNo = cardNo.toLowerCase();
        //获取密钥
        byte[] cardSecret = getCardSecret(lowCardNo);
        if (cardSecret != null) {
            String keys = BytesHexStrUtils.bytesToHex(cardSecret);
            AppLogUtils.d("Card M1 cardKeyArray = " + BytesHexStrUtils.bytesToHex2(cardSecret) + " , taKeys " + keys);
            //获取每个扇区的密钥
            ArrayList<String> keyArray = getCardSecretList(keys);
            if (keyArray != null) {
                String cardData = joinM1Data(lowCardNo, keyArray);
                AppLogUtils.d("Card M1 cardData = " + cardData);
                return cardData;
            }
        }
        return null;
    }

    /**
     * 子类可以自己实现扇区的读写
     *
     * @param cardNum   卡号
     * @param arrayList 扇区密钥集合
     * @return 扇区数据
     */
    protected String joinM1Data(String cardNum, ArrayList<String> arrayList) {
        StringBuilder sb = new StringBuilder();
        String key_6 = arrayList.get(0);
        byte[] password6 = BytesHexStrUtils.hexToBytes1(key_6);
        if (!TextUtils.isEmpty(key_6) && key_6.length() == 12) {
            int block61 = 24;
            byte[] data61 = readBlock(cardNum, KEY_MODE_A, block61, password6);
            String data61String = "";
            if (data61 != null) {
                data61String = BytesHexStrUtils.bytesToHex(data61).replace("0", "");
            }
            if (data61 != null && !TextUtils.isEmpty(data61String)) {
                byte[] data62 = readBlock(cardNum, KEY_MODE_A, block61 + 1, password6);
                byte[] data63 = readBlock(cardNum, KEY_MODE_A, block61 + 2, password6);
                if (data62 != null && data63 != null) {
                    String s2 = ByteUtils.bytesToHex(data62);
                    String s3 = ByteUtils.bytesToHex(data63);
                    sb.append(data61String).append(s2).append(s3);
                    AppLogUtils.d("Card M1 扇区6密钥认证成功：" + sb);
                } else {
                    AppLogUtils.d("Card M1 扇区6密钥认证失败2：请检查秘钥-" + key_6);
                    return null;
                }
            } else {
                AppLogUtils.d("Card M1 扇区6密钥认证失败1：请检查秘钥-" + key_6);
                return null;
            }
        }

        String key_7 = arrayList.get(1);
        if (!TextUtils.isEmpty(key_7) && key_7.length() == 12) {
            byte[] password7 = BytesHexStrUtils.hexToBytes1(key_7);
            int block71 = 28;
            byte[] data71 = readBlock(cardNum, KEY_MODE_A, block71, password7);
            String data71String = "";
            if (data71 != null) {
                data71String = BytesHexStrUtils.bytesToHex(data71).replace("0", "");
            }
            if (data71 != null && !TextUtils.isEmpty(data71String)) {
                byte[] data72 = readBlock(cardNum, KEY_MODE_A, block71 + 1, password7);
                byte[] data73 = readBlock(cardNum, KEY_MODE_A, block71 + 2, password7);
                if (data72 != null && data73 != null) {
                    String s2 = ByteUtils.bytesToHex(data72);
                    String s3 = ByteUtils.bytesToHex(data73);
                    sb.append(data71String).append(s2).append(s3);
                    AppLogUtils.d("Card M1 扇区7密钥认证成功：" + data71String + s2 + s3);
                } else {
                    AppLogUtils.d("Card M1 扇区7密钥认证失败2：请检查秘钥-" + key_7);
                    return null;
                }
            } else {
                AppLogUtils.d("Card M1 扇区7密钥认证失败1：请检查秘钥-" + key_7);
                return null;
            }
        }

        String key_8 = arrayList.get(2);
        if (!TextUtils.isEmpty(key_8) && key_8.length() == 12) {
            byte[] password8 = BytesHexStrUtils.hexToBytes1(key_7);
            int block81 = 32;
            byte[] data81 = readBlock(cardNum, KEY_MODE_A, block81, password8);
            String data81String = "";
            if (data81 != null) {
                data81String = ByteUtils.bytesToHex(data81);
            }
            if (data81 != null && !TextUtils.isEmpty(data81String)) {
                byte[] data82 = readBlock(cardNum, KEY_MODE_A, block81 + 1, password8);
                if (data82 != null) {
                    String s2 = ByteUtils.bytesToHex(data82);
                    sb.append(data81String).append(s2);
                    AppLogUtils.d("Card M1 扇区8密钥认证成功：" + data81String + s2);
                } else {
                    AppLogUtils.d("Card M1 扇区8密钥认证失败2：请检查秘钥-" + key_8);
                    return null;
                }
            } else {
                AppLogUtils.d("Card M1 扇区8密钥认证失败1：请检查秘钥-" + key_8);
                return null;
            }
        }
        return sb.toString();
    }
}
