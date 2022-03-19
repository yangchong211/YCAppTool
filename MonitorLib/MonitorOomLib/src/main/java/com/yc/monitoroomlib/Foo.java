package com.yc.monitoroomlib;


import java.util.HashMap;

public class Foo {

    private static volatile int count;

    public static void main(String[] args){
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 100; i++){
                        count++;
                    }
                }
            });
            thread.start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("count is : " + count);

        HashMap<String, String> map = new HashMap<>();
        map.put(null,"");
    }



}
