package com.yc.cpucard;

public interface InterCpuCard {

    String search();

    String[] sendAPDU(byte[] cosCmd);


    byte[] reset();

}

