package com.yc.mifarecard;

/**
 * M1卡功能接口
 */
public interface IM1Function {

    /**
     * 根据卡号获取密钥
     *
     * @return 返回密钥
     */
    byte[] getCardSecret(String cardNo);

}
