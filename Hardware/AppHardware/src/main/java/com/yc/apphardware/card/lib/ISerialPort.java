package com.yc.apphardware.card.lib;

public interface ISerialPort {
    /**
     * 打开串口
     *
     * @param port 串口路径，根据你的设备不同，路径也不同
     * @param flag 波特率
     */
    void open(String port, int flag);

    /**
     * 往串口中写入数据
     * @param data          数据
     */
    void writeByte(byte[] data);

    /**
     * 关闭串口
     */
    void close();
}
