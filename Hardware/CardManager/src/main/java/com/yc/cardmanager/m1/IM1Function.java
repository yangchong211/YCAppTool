package com.yc.cardmanager.m1;

import java.util.ArrayList;

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

    /**
     * 将密钥拆分成几个不同扇区的秘密，拆分成list集合
     *
     * @param secret 密钥
     * @return 集合
     */
    ArrayList<String> getCardSecretList(String secret);


    /**
     * 校验卡号并读取数据
     * @param cardNo            卡号
     * @return                  卡数据
     */
    String verify(String cardNo);
}
