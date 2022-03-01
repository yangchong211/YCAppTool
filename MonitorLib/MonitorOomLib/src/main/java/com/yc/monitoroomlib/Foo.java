package com.yc.monitoroomlib;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Foo {

    private int x = 10;
    static boolean boolValue;//这里不在栈区

    public static void main(String[] args){
        boolValue = true;
        if(boolValue)
            System.out.println("Hello Java!");
        if(boolValue == true)
            System.out.println("Hello JVM!");

        Integer i = 10;
        int i1 = Integer.parseInt("1");
        Integer integer1 = new Integer(123);
        Integer integer = Integer.valueOf(123);
    }


}
