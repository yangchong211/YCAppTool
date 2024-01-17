package com.yc.lifehelper;

import java.util.ArrayList;

public class OOMTestMaker {

    public static ArrayList<byte[]> list = new ArrayList();

    public OOMTestMaker() {
    }

    public static void createOOM() {
        while(true) {
            list.add(new byte[2097152]);
        }
    }

    public static void encreaseMem() {
        list.add(new byte[15728640]);
    }

}
