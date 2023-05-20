package com.yc.percent;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.core.view.MarginLayoutParamsCompat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class PercentLayoutHelper {
    private static final String TAG = "PercentLayout";
    private final ViewGroup mHost;
    private static int mWidthScreen;
    private static int mHeightScreen;

    public PercentLayoutHelper(ViewGroup host) {
        mHost = host;
        getScreenSize();
    }

    private void getScreenSize() {
        WindowManager wm = (WindowManager) mHost.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mWidthScreen = outMetrics.widthPixels;
        mHeightScreen = outMetrics.heightPixels;
    }


    /**
     * Helper method to be called from  override
     * that reads layout_width and layout_height attribute values without throwing an exception if
     * they aren't present.
     */
    public static void fetchWidthAndHeight(ViewGroup.LayoutParams params, TypedArray array,
                                           int widthAttr, int heightAttr) {
        params.width = array.getLayoutDimension(widthAttr, 0);
        params.height = array.getLayoutDimension(heightAttr, 0);
    }

    /**
     * 迭代子元素并将其宽度和高度更改为根据百分比值计算的值。
     * Iterates over children and changes their width and height to one calculated from percentage
     * values.
     *
     * @param widthMeasureSpec  Width MeasureSpec of the parent ViewGroup.
     * @param heightMeasureSpec Height MeasureSpec of the parent ViewGroup.
     */
    public void adjustChildren(int widthMeasureSpec, int heightMeasureSpec) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "adjustChildren: " + mHost + " widthMeasureSpec: "
                    + View.MeasureSpec.toString(widthMeasureSpec) + " heightMeasureSpec: "
                    + View.MeasureSpec.toString(heightMeasureSpec));
        }
        //获取父容器的尺寸
        int widthHint = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightHint = View.MeasureSpec.getSize(heightMeasureSpec);

        if (Log.isLoggable(TAG, Log.DEBUG)){
            Log.d(TAG, "widthHint = " + widthHint + " , heightHint = " + heightHint);
        }

        for (int j=0 ; j<mHost.getChildCount() ;  j++){
            View view = mHost.getChildAt(j);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams instanceof PercentLayoutParams){
                PercentLayoutInfo percentLayoutInfo = ((PercentLayoutParams) layoutParams).getPercentLayoutInfo();
                if (percentLayoutInfo != null){
                    //解析xml数据，
                }
            }
        }

        //获取孩子控件，然后遍历
        for (int i = 0 ; i < mHost.getChildCount(); i++) {
            View view = mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "should adjust " + view + " " + params);
            }
            //判断是否是自定义百分比布局属性
            if (params instanceof PercentLayoutParams) {
                //获取自定义属性
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, "using " + info);
                }
                if (info != null) {
                    //计算百分比属性
                    //设置文本的属性
                    supportTextSize(widthHint, heightHint, view, info);
                    //设置内间距
                    supportPadding(widthHint, heightHint, view, info);
                    //设置最大最小宽高的属性
                    supportMinOrMaxDimesion(widthHint, heightHint, view, info);
                    //设置宽和高属性
                    if (params instanceof ViewGroup.MarginLayoutParams) {
                        info.fillMarginLayoutParams((ViewGroup.MarginLayoutParams) params, widthHint, heightHint);
                    } else {
                        info.fillLayoutParams(params, widthHint, heightHint);
                    }
                }
            }
        }
    }

    private void supportPadding(int widthHint, int heightHint, View view, PercentLayoutInfo info) {
        int left = view.getPaddingLeft(), right = view.getPaddingRight(),
                top = view.getPaddingTop(), bottom = view.getPaddingBottom();
        PercentVal percentVal = info.paddingLeftPercent;
        if (percentVal != null) {
            int base = getBaseByModeAndVal(widthHint, heightHint, percentVal.basemode);
            left = (int) (base * percentVal.percent);
        }
        percentVal = info.paddingRightPercent;
        if (percentVal != null) {
            int base = getBaseByModeAndVal(widthHint, heightHint, percentVal.basemode);
            right = (int) (base * percentVal.percent);
        }

        percentVal = info.paddingTopPercent;
        if (percentVal != null) {
            int base = getBaseByModeAndVal(widthHint, heightHint, percentVal.basemode);
            top = (int) (base * percentVal.percent);
        }

        percentVal = info.paddingBottomPercent;
        if (percentVal != null) {
            int base = getBaseByModeAndVal(widthHint, heightHint, percentVal.basemode);
            bottom = (int) (base * percentVal.percent);
        }
        view.setPadding(left, top, right, bottom);
    }

    private void supportMinOrMaxDimesion(int widthHint, int heightHint, View view, PercentLayoutInfo info) {
        try {
            Class clazz = view.getClass();
            invokeMethod("setMaxWidth", widthHint, heightHint, view, clazz, info.maxWidthPercent);
            invokeMethod("setMaxHeight", widthHint, heightHint, view, clazz, info.maxHeightPercent);
            invokeMethod("setMinWidth", widthHint, heightHint, view, clazz, info.minWidthPercent);
            invokeMethod("setMinHeight", widthHint, heightHint, view, clazz, info.minHeightPercent);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void invokeMethod(String methodName, int widthHint, int heightHint, View view,
                              Class clazz, PercentVal percentVal)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (Log.isLoggable(TAG, Log.DEBUG))
            Log.d(TAG, methodName + " ==> " + percentVal);
        if (percentVal != null) {
            Method setMaxWidthMethod = clazz.getMethod(methodName, int.class);
            setMaxWidthMethod.setAccessible(true);
            int base = getBaseByModeAndVal(widthHint, heightHint, percentVal.basemode);
            setMaxWidthMethod.invoke(view, (int) (base * percentVal.percent));
        }
    }

    private void supportTextSize(int widthHint, int heightHint, View view, PercentLayoutInfo info) {
        //textsize percent support
        PercentVal textSizePercent = info.textSizePercent;
        if (textSizePercent == null) {
            return;
        }
        int base = getBaseByModeAndVal(widthHint, heightHint, textSizePercent.basemode);
        float textSize = (int) (base * textSizePercent.percent);
        //Button 和 EditText 是TextView的子类
        if (view instanceof TextView) {
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
    }

    private static int getBaseByModeAndVal(int widthHint, int heightHint, PercentBaseMode basemode) {
        switch (basemode) {
                //容器高度
            case BASE_HEIGHT:
                return heightHint;
                //容器宽度
            case BASE_WIDTH:
                return widthHint;
                //屏幕宽度
            case BASE_SCREEN_WIDTH:
                return mWidthScreen;
                //屏幕高度
            case BASE_SCREEN_HEIGHT:
                return mHeightScreen;
        }
        return 0;
    }


    /**
     * 遍历子节点并恢复为百分比值更改的原始维度。调用此方法只有在之前调用过的情况下才有意义
     * Iterates over children and restores their original dimensions that were changed for
     * percentage values. Calling this method only makes sense if you previously called
     * {@link PercentLayoutHelper#adjustChildren(int, int)}.
     */

    public void restoreOriginalParams() {
        for (int i = 0, N = mHost.getChildCount(); i < N; i++) {
            View view = mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "should restore " + view + " " + params);
            }
            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Log.d(TAG, "using " + info);
                }
                if (info != null) {
                    if (params instanceof ViewGroup.MarginLayoutParams) {
                        info.restoreMarginLayoutParams((ViewGroup.MarginLayoutParams) params);
                    } else {
                        info.restoreLayoutParams(params);
                    }
                }
            }
        }
    }

    /**
     * 遍历子节点，并检查它们中是否有任何一个希望获得比通过百分比维度接收到的更多的空间。
     * Iterates over children and checks if any of them would like to get more space than it
     * received through the percentage dimension.
     * <p/>
     * If you are building a layout that supports percentage dimensions you are encouraged to take
     * advantage of this method. The developer should be able to specify that a child should be
     * remeasured by adding normal dimension attribute with {@code wrap_content} value. For example
     * he might specify child's attributes as {@code app:layout_widthPercent="60%p"} and
     * {@code android:layout_width="wrap_content"}. In this case if the child receives too little
     * space, it will be remeasured with width set to {@code WRAP_CONTENT}.
     *
     * @return True if the measure phase needs to be rerun because one of the children would like
     * to receive more space.
     */
    public boolean handleMeasuredStateTooSmall() {
        boolean needsSecondMeasure = false;
        for (int i = 0, N = mHost.getChildCount(); i < N; i++) {
            View view = mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "should handle measured state too small " + view + " " + params);
            }
            if (params instanceof PercentLayoutParams) {
                PercentLayoutInfo info = ((PercentLayoutParams) params).getPercentLayoutInfo();
                if (info != null) {
                    if (PercentHelper.shouldHandleMeasuredWidthTooSmall(view, info)) {
                        needsSecondMeasure = true;
                        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                    if (PercentHelper.shouldHandleMeasuredHeightTooSmall(view, info)) {
                        needsSecondMeasure = true;
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    }
                }
            }
        }
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "should trigger second measure pass: " + needsSecondMeasure);
        }
        return needsSecondMeasure;
    }


    /**
     * Container for information about percentage dimensions and margins. It acts as an extension
     * for {@code LayoutParams}.
     */
    public static class PercentLayoutInfo {
        
        public PercentVal widthPercent;
        public PercentVal heightPercent;

        public PercentVal leftMarginPercent;
        public PercentVal topMarginPercent;
        public PercentVal rightMarginPercent;
        public PercentVal bottomMarginPercent;
        public PercentVal startMarginPercent;
        public PercentVal endMarginPercent;

        public PercentVal textSizePercent;

        //1.0.4 those attr for some views' setMax/min Height/Width method
        public PercentVal maxWidthPercent;
        public PercentVal maxHeightPercent;
        public PercentVal minWidthPercent;
        public PercentVal minHeightPercent;

        //1.0.6 add padding supprot
        public PercentVal paddingLeftPercent;
        public PercentVal paddingRightPercent;
        public PercentVal paddingTopPercent;
        public PercentVal paddingBottomPercent;
        final ViewGroup.MarginLayoutParams mPreservedParams;

        public PercentLayoutInfo() {
            mPreservedParams = new ViewGroup.MarginLayoutParams(0, 0);
        }

        /**
         * Fills {@code ViewGroup.LayoutParams} dimensions based on percentage values.
         */
        public void fillLayoutParams(ViewGroup.LayoutParams params, int widthHint,
                                     int heightHint) {
            // Preserve the original layout params, so we can restore them after the measure step.
            // 保留原始的布局参数，以便我们可以在测量步骤后恢复它们。
            mPreservedParams.width = params.width;
            mPreservedParams.height = params.height;

            if (widthPercent != null) {
                int base = getBaseByModeAndVal(widthHint, heightHint, widthPercent.basemode);
                params.width = (int) (base * widthPercent.percent);
            }
            if (heightPercent != null) {
                int base = getBaseByModeAndVal(widthHint, heightHint, heightPercent.basemode);
                params.height = (int) (base * heightPercent.percent);
            }

            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "after fillLayoutParams: (" + params.width + ", " + params.height + ")");
            }
        }

        /**
         * Fills {@code ViewGroup.MarginLayoutParams} dimensions and margins based on percentage
         * values.
         */
        public void fillMarginLayoutParams(ViewGroup.MarginLayoutParams params, int widthHint,
                                           int heightHint) {
            fillLayoutParams(params, widthHint, heightHint);

            // Preserver the original margins, so we can restore them after the measure step.
            mPreservedParams.leftMargin = params.leftMargin;
            mPreservedParams.topMargin = params.topMargin;
            mPreservedParams.rightMargin = params.rightMargin;
            mPreservedParams.bottomMargin = params.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(mPreservedParams,
                    MarginLayoutParamsCompat.getMarginStart(params));
            MarginLayoutParamsCompat.setMarginEnd(mPreservedParams,
                    MarginLayoutParamsCompat.getMarginEnd(params));

            if (leftMarginPercent != null) {
                int base = getBaseByModeAndVal(widthHint, heightHint, leftMarginPercent.basemode);
                params.leftMargin = (int) (base * leftMarginPercent.percent);
            }
            if (topMarginPercent != null) {
                int base = getBaseByModeAndVal(widthHint, heightHint, topMarginPercent.basemode);
                params.topMargin = (int) (base * topMarginPercent.percent);
            }
            if (rightMarginPercent != null) {
                int base = getBaseByModeAndVal(widthHint, heightHint, rightMarginPercent.basemode);
                params.rightMargin = (int) (base * rightMarginPercent.percent);
            }
            if (bottomMarginPercent != null) {
                int base = getBaseByModeAndVal(widthHint, heightHint, bottomMarginPercent.basemode);
                params.bottomMargin = (int) (base * bottomMarginPercent.percent);
            }

            if (startMarginPercent != null) {
                int base = getBaseByModeAndVal(widthHint, heightHint, startMarginPercent.basemode);
                MarginLayoutParamsCompat.setMarginStart(params,
                        (int) (base * startMarginPercent.percent));
            }
            if (endMarginPercent != null) {
                int base = getBaseByModeAndVal(widthHint, heightHint, endMarginPercent.basemode);
                MarginLayoutParamsCompat.setMarginEnd(params,
                        (int) (base * endMarginPercent.percent));
            }
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "after fillMarginLayoutParams: (" + params.width + ", " + params.height
                        + ")");
            }
        }

        @Override
        public String toString() {
            return "PercentLayoutInfo{" +
                    "widthPercent=" + widthPercent +
                    ", heightPercent=" + heightPercent +
                    ", leftMarginPercent=" + leftMarginPercent +
                    ", topMarginPercent=" + topMarginPercent +
                    ", rightMarginPercent=" + rightMarginPercent +
                    ", bottomMarginPercent=" + bottomMarginPercent +
                    ", startMarginPercent=" + startMarginPercent +
                    ", endMarginPercent=" + endMarginPercent +
                    ", textSizePercent=" + textSizePercent +
                    ", maxWidthPercent=" + maxWidthPercent +
                    ", maxHeightPercent=" + maxHeightPercent +
                    ", minWidthPercent=" + minWidthPercent +
                    ", minHeightPercent=" + minHeightPercent +
                    ", paddingLeftPercent=" + paddingLeftPercent +
                    ", paddingRightPercent=" + paddingRightPercent +
                    ", paddingTopPercent=" + paddingTopPercent +
                    ", paddingBottomPercent=" + paddingBottomPercent +
                    ", mPreservedParams=" + mPreservedParams +
                    '}';
        }

        /**
         * Restores original dimensions and margins after they were changed for percentage based
         * values. Calling this method only makes sense if you previously called
         * {@link PercentLayoutInfo#fillMarginLayoutParams}.
         */
        public void restoreMarginLayoutParams(ViewGroup.MarginLayoutParams params) {
            restoreLayoutParams(params);
            params.leftMargin = mPreservedParams.leftMargin;
            params.topMargin = mPreservedParams.topMargin;
            params.rightMargin = mPreservedParams.rightMargin;
            params.bottomMargin = mPreservedParams.bottomMargin;
            MarginLayoutParamsCompat.setMarginStart(params,
                    MarginLayoutParamsCompat.getMarginStart(mPreservedParams));
            MarginLayoutParamsCompat.setMarginEnd(params,
                    MarginLayoutParamsCompat.getMarginEnd(mPreservedParams));
        }

        /**
         * Restores original dimensions after they were changed for percentage based values. Calling
         * this method only makes sense if you previously called
         * {@link PercentLayoutInfo#fillLayoutParams}.
         */
        public void restoreLayoutParams(ViewGroup.LayoutParams params) {
            params.width = mPreservedParams.width;
            params.height = mPreservedParams.height;
        }
    }

    /**
     * If a layout wants to support percentage based dimensions and use this helper class, its
     * {@code LayoutParams} subclass must implement this interface.
     * <p/>
     * Your {@code LayoutParams} subclass should contain an instance of {@code PercentLayoutInfo}
     * and the implementation of this interface should be a simple accessor.
     */
    public interface PercentLayoutParams {
        PercentLayoutInfo getPercentLayoutInfo();
    }
}
