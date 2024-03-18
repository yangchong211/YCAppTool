package com.yc.cardmanager.m1;

/**
 * M1卡通用接口
 */
public interface InterM1Card {

    /**
     * 寻找卡号
     *
     * @return 获取卡号
     */
    String search();

    /**
     * 读取卡数据
     *
     * @param mode     类型
     * @param block    块
     * @param password 密钥
     * @return 卡数据
     */
    byte[] readBlock(String cardNo , int mode, int block, byte[] password);

    /**
     * 写入卡数据
     *
     * @param mode      类型
     * @param block     块
     * @param password  密钥
     * @param blockData 数据
     * @return 写入状态
     */
    int writeBlock(String cardNo , int mode, int block, byte[] password, byte[] blockData);

    /**
     * 归位重置
     *
     * @return 数据
     */
    byte[] reset();

}
