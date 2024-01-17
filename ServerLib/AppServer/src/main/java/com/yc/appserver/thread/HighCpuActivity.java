package com.yc.appserver.thread;

import java.util.ArrayList;
import java.util.List;

public class HighCpuActivity {

    public static void testHighCpu(){
        List<HignCpu> cpus = new ArrayList<>();
        Thread highCpuThread = new Thread(()->{
            int i = 0;
            while (true){
                HignCpu cpu = new HignCpu("Java日知录",i);
                cpus.add(cpu);
                System.out.println("high cpu size:" + cpus.size());
                i ++;
            }
        });
        highCpuThread.setName("HignCpu");
        highCpuThread.start();
    }

    public static class HignCpu {
        private String name;
        private int age;

        public HignCpu(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
