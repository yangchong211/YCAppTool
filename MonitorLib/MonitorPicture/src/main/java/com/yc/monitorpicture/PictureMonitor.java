package com.yc.monitorpicture;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import de.robv.android.xposed.DexposedBridge;
import de.robv.android.xposed.XC_MethodHook;

public final class PictureMonitor {

    public static final String TAG = "PictureMonitor: ";

    public static void pictureMonitor(){
        DexposedBridge.findAndHookMethod(ImageView.class, "setImageBitmap", Bitmap.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if (param.thisObject instanceof ImageView) {
                    final ImageView imageView = (ImageView) param.thisObject;
                    if (imageView.getDrawable() instanceof BitmapDrawable) {
                        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                        final Bitmap bitmap = drawable.getBitmap();
                        bitmapListener(imageView,bitmap);
                    }
                }
            }
        });
    }

    private static void bitmapListener(ImageView imageView, Bitmap bitmap) {
        if (bitmap != null) {
            //bitmap 宽高
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            //视图宽高
            int viewWidth = imageView.getWidth();
            int viewHeight = imageView.getHeight();
            if (viewHeight > 0 && viewWidth > 0) {
                //view 有宽高
                //当图片宽高都大于视图宽高的2倍时就报出警告
                if (bitmapWidth >= viewWidth << 2 && bitmapHeight >= viewHeight << 2) {
                    warn(bitmapWidth, bitmapHeight, viewWidth, viewHeight);
                }
            } else {
                imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        int bitmapWidth = bitmap.getWidth();
                        int bitmapHeight = bitmap.getHeight();
                        int viewWidth = imageView.getWidth();
                        int viewHeight = imageView.getHeight();
                        if (bitmapWidth >= viewWidth << 2 && bitmapHeight >= viewHeight << 2) {
                            warn(bitmapWidth, bitmapHeight, viewWidth, viewHeight);
                        }
                        imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });
            }
        }
    }

    private static void warn(int bitmapWidth, int bitmapHeight, int viewWidth, int viewHeight) {
        StringBuffer msg = new StringBuffer();
        msg.append("图片大小不合理：")
                .append("bitmapWidth=").append(bitmapWidth)
                .append(",bitmapHeight=").append(bitmapHeight)
                .append(",viewWidth=").append(viewWidth)
                .append(",viewHeight=").append(viewHeight);
        //不合理
        Log.e(TAG, Log.getStackTraceString(new Throwable(msg.toString())));
    }

}
