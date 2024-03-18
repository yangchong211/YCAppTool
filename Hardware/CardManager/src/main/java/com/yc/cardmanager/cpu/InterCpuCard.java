package com.yc.cardmanager.cpu;

public interface InterCpuCard {

    /**
     * 寻找卡号
     *
     * @return 获取卡号
     */
    String search();

    /**
     * 发送指令
     *
     * @param cosCmd 指令
     * @return 返回数据
     */
    String[] sendAPDU(byte[] cosCmd);


    /**
     * 归位重置
     *
     * @return 数据
     */
    byte[] reset();

}

