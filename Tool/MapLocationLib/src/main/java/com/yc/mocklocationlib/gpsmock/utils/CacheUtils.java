package com.yc.mocklocationlib.gpsmock.utils;


import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CacheUtils {

    private static final String TAG = "CacheUtils";

    private CacheUtils() {
    }

    public static boolean saveObject(Context context, String key, Serializable ser) {
        File file = new File(context.getCacheDir() + "/" + key);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException var5) {
                LogMockUtils.e("CacheUtils", var5.toString());
            }
        }

        return saveObject(ser, file);
    }

    public static Serializable readObject(Context context, String key) {
        File file = new File(context.getCacheDir() + "/" + key);
        return readObject(file);
    }

    public static boolean saveObject(Serializable ser, File file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        boolean var5;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            boolean var4 = true;
            return var4;
        } catch (IOException var19) {
            LogMockUtils.e("CacheUtils", var19.toString());
            var5 = false;
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException var18) {
                    LogMockUtils.e("CacheUtils", var18.toString());
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException var17) {
                    LogMockUtils.e("CacheUtils", var17.toString());
                }
            }

        }

        return var5;
    }

    public static Serializable readObject(File file) {
        if (file != null && file.exists() && !file.isDirectory()) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;

            Object var4;
            try {
                fis = new FileInputStream(file);
                ois = new ObjectInputStream(fis);
                Serializable var3 = (Serializable)ois.readObject();
                return var3;
            } catch (IOException var21) {
                if (var21 instanceof InvalidClassException) {
                    file.delete();
                }

                LogMockUtils.e("CacheUtils", var21.toString());
                var4 = null;
            } catch (ClassNotFoundException var22) {
                LogMockUtils.e("CacheUtils", var22.toString());
                var4 = null;
                return (Serializable)var4;
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException var20) {
                        LogMockUtils.e("CacheUtils", var20.toString());
                    }
                }

                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException var19) {
                        LogMockUtils.e("CacheUtils", var19.toString());
                    }
                }

            }

            return (Serializable)var4;
        } else {
            return null;
        }
    }
}

