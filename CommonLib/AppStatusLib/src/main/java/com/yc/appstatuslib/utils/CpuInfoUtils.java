package com.yc.appstatuslib.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;

public final class CpuInfoUtils {

    public CpuInfoUtils() {

    }

    public static int cpuCount() {
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File fileName) {
                String path = fileName.getName();
                if (!path.startsWith("cpu")) {
                    return false;
                } else {
                    for(int i = 3; i < path.length(); ++i) {
                        if (path.charAt(i) < '0' || path.charAt(i) > '9') {
                            return false;
                        }
                    }

                    return true;
                }
            }
        };

        int cores;
        try {
            cores = (new File("/sys/devices/system/cpu/")).listFiles(fileFilter).length;
        } catch (Exception var3) {
            var3.printStackTrace();
            cores = 0;
        }

        return cores;
    }

    public static String cpuTemperature() {
        String temperature = "0";
        File file = new File("/sys/class/thermal/thermal_zone9/subsystem/thermal_zone9/temp");
        if (!file.exists()) {
            return "0";
        } else {
            try {
                FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);
                temperature = bufferedReader.readLine();
                bufferedReader.close();
            } catch (Throwable var4) {
                var4.printStackTrace();
            }
            return temperature;
        }
    }
}

