package com.yc.monitoroomlib;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    private void test(){
        LinkedHashMap<String, Integer> lmap = new LinkedHashMap<String, Integer>();
        lmap.put("语文", 1);
        lmap.put("数学", 2);
        lmap.put("英语", 3);
        lmap.put("历史", 4);
        lmap.put("政治", 5);
        lmap.put("地理", 6);
        lmap.put("生物", 7);
        lmap.put("化学", 8);

        //有序，怎么实现有序
        Integer i = lmap.get("语文");
    }

}
