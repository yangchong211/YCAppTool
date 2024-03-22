package com.yc.cameraopen;

import android.media.Image;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


public class CameraOpenUtils {

    private static final String TAG = "YTPalm";

    /**
     * 将图片转化为字节数组
     *
     * @param image 图片对象
     * @return      字节数组
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static byte[] nv21ImageToByte(Image image) {
        Image.Plane[] planes = image.getPlanes();
        int width = image.getWidth();
        int height = image.getHeight();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uvBuffer = planes[1].getBuffer();
        int ySize = yBuffer.remaining();
        int uvSize = uvBuffer.remaining();
        byte[] data = new byte[width * height * 3 / 2];
        yBuffer.get(data, 0, ySize);
        uvBuffer.get(data, ySize, uvSize);
        return data;
    }

    public static void writeBinToFile(String filepath, byte[] data) {
        File file = new File(filepath);  // 要写入的文件路径
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);  // 将数据写入文件
            fos.flush();  // 刷新缓冲区
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();  // 关闭文件输出流
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void d(String msg) {
        Log.d("OpenCamera |: ", msg);
    }
}
