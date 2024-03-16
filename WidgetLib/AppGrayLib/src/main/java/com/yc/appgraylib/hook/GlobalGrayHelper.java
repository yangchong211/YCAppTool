package com.yc.appgraylib.hook;

import android.annotation.SuppressLint;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/5/11
 *     desc   : 全局灰色帮助类
 *     revise :
 * </pre>
 */
public class GlobalGrayHelper {

    /**
     * @param enable 是否开启全局灰色调
     */
    public static void enable(boolean enable) {
        if (!enable) {
            return;
        }
        //灰色调Paint
        final Paint mPaint = new Paint();
        ColorMatrix mColorMatrix = new ColorMatrix();
        mColorMatrix.setSaturation(0);
        mPaint.setColorFilter(new ColorMatrixColorFilter(mColorMatrix));
        try {
            //反射获取windowManagerGlobal
            @SuppressLint("PrivateApi")
            Class<?> windowManagerGlobal = Class.forName("android.view.WindowManagerGlobal");
            @SuppressLint("DiscouragedPrivateApi")
            java.lang.reflect.Method getInstanceMethod = windowManagerGlobal.getDeclaredMethod("getInstance");
            getInstanceMethod.setAccessible(true);
            Object windowManagerGlobalInstance = getInstanceMethod.invoke(windowManagerGlobal);

            //反射获取mViews
            Field mViewsField = windowManagerGlobal.getDeclaredField("mViews");
            mViewsField.setAccessible(true);
            Object mViewsObject = mViewsField.get(windowManagerGlobalInstance);

            //创建具有数据感知能力的ObservableArrayList
            ObservableArrayList<View> observerArrayList = new ObservableArrayList<>();
            observerArrayList.addOnListChangedListener(new ObservableArrayList.OnListChangeListener() {
                @Override
                public void onChange(ArrayList list, int index, int count) {

                }

                @Override
                public void onAdd(ArrayList list, int start, int count) {
                    View view = (View) list.get(start);
                    if (view != null) {
                        view.setLayerType(View.LAYER_TYPE_HARDWARE, mPaint);
                    }
                }

                @Override
                public void onRemove(ArrayList list, int start, int count) {

                }
            });
            //将原有的数据添加到新创建的list
            observerArrayList.addAll((ArrayList<View>) mViewsObject);
            //替换掉原有的mViews
            mViewsField.set(windowManagerGlobalInstance, observerArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
