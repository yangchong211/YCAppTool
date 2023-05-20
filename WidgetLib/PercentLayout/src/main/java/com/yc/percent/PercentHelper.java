package com.yc.percent;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PercentHelper {

    private static final String TAG = "PercentLayout";
    private static final String REGEX_PERCENT = "^(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)%([s]?[wh]?)$";

    /**
     * 这一步是解析资源自定义属性
     * Constructs a PercentLayoutInfo from attributes associated with a View. Call this method from
     * {@code LayoutParams(Context c, AttributeSet attrs)} constructor.
     */
    public static PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo(Context context,
                                                                             AttributeSet attrs) {
        PercentLayoutHelper.PercentLayoutInfo info = null;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PercentLayout_Layout);
        info = setWidthAndHeightVal(array, info);
        info = setMarginRelatedVal(array, info);
        info = setTextSizeSupportVal(array, info);
        info = setMinMaxWidthHeightRelatedVal(array, info);
        info = setPaddingRelatedVal(array, info);
        array.recycle();
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "constructed: " + info);
        }
        return info;
    }

    private static PercentLayoutHelper.PercentLayoutInfo setWidthAndHeightVal(TypedArray array, PercentLayoutHelper.PercentLayoutInfo info) {
        PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_widthPercent, true);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent width: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.widthPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_heightPercent, false);

        if (percentVal != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent height: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.heightPercent = percentVal;
        }

        return info;
    }


    private static PercentLayoutHelper.PercentLayoutInfo setMarginRelatedVal(TypedArray array, PercentLayoutHelper.PercentLayoutInfo info) {
        //默认margin参考宽度
        PercentVal percentVal =
                getPercentVal(array,
                        R.styleable.PercentLayout_Layout_layout_marginPercent,
                        true);

        if (percentVal != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent margin: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.leftMarginPercent = percentVal;
            info.topMarginPercent = percentVal;
            info.rightMarginPercent = percentVal;
            info.bottomMarginPercent = percentVal;
        }

        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_marginLeftPercent, true);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent left margin: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.leftMarginPercent = percentVal;
        }

        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_marginTopPercent, false);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent top margin: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.topMarginPercent = percentVal;
        }

        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_marginRightPercent, true);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent right margin: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.rightMarginPercent = percentVal;
        }

        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_marginBottomPercent, false);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent bottom margin: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.bottomMarginPercent = percentVal;
        }
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_marginStartPercent, true);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent start margin: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.startMarginPercent = percentVal;
        }

        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_marginEndPercent, true);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent end margin: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.endMarginPercent = percentVal;
        }

        return info;
    }


    private static PercentLayoutHelper.PercentLayoutInfo setTextSizeSupportVal(TypedArray array, PercentLayoutHelper.PercentLayoutInfo info) {
        //textSizePercent 默认以高度作为基准
        PercentVal percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_textSizePercent, false);
        if (percentVal != null) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "percent text size: " + percentVal.percent);
            }
            info = checkForInfoExists(info);
            info.textSizePercent = percentVal;
        }

        return info;
    }


    private static PercentLayoutHelper.PercentLayoutInfo setMinMaxWidthHeightRelatedVal(TypedArray array, PercentLayoutHelper.PercentLayoutInfo info) {
        //maxWidth
        PercentVal percentVal = getPercentVal(array,
                R.styleable.PercentLayout_Layout_layout_maxWidthPercent, true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.maxWidthPercent = percentVal;
        }
        //maxHeight
        percentVal = getPercentVal(array,
                R.styleable.PercentLayout_Layout_layout_maxHeightPercent, false);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.maxHeightPercent = percentVal;
        }
        //minWidth
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_minWidthPercent,
                true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.minWidthPercent = percentVal;
        }
        //minHeight
        percentVal = getPercentVal(array, R.styleable.PercentLayout_Layout_layout_minHeightPercent,
                false);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.minHeightPercent = percentVal;
        }

        return info;
    }


    /**
     * 设置paddingPercent相关属性
     *
     * @param array
     * @param info
     */
    private static PercentLayoutHelper.PercentLayoutInfo setPaddingRelatedVal(TypedArray array, PercentLayoutHelper.PercentLayoutInfo info) {
        //默认padding以宽度为标准
        PercentVal percentVal = getPercentVal(array,
                R.styleable.PercentLayout_Layout_layout_paddingPercent,
                true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.paddingLeftPercent = percentVal;
            info.paddingRightPercent = percentVal;
            info.paddingBottomPercent = percentVal;
            info.paddingTopPercent = percentVal;
        }


        percentVal = getPercentVal(array,
                R.styleable.PercentLayout_Layout_layout_paddingLeftPercent,
                true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.paddingLeftPercent = percentVal;
        }

        percentVal = getPercentVal(array,
                R.styleable.PercentLayout_Layout_layout_paddingRightPercent,
                true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.paddingRightPercent = percentVal;
        }

        percentVal = getPercentVal(array,
                R.styleable.PercentLayout_Layout_layout_paddingTopPercent,
                true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.paddingTopPercent = percentVal;
        }

        percentVal = getPercentVal(array,
                R.styleable.PercentLayout_Layout_layout_paddingBottomPercent,
                true);
        if (percentVal != null) {
            info = checkForInfoExists(info);
            info.paddingBottomPercent = percentVal;
        }

        return info;
    }

    private static PercentVal getPercentVal(TypedArray array, int index, boolean baseWidth) {
        String sizeStr = array.getString(index);
        PercentVal percentVal = PercentHelper.getPercentVal(sizeStr, baseWidth);
        return percentVal;
    }

    @NonNull
    private static PercentLayoutHelper.PercentLayoutInfo checkForInfoExists(PercentLayoutHelper.PercentLayoutInfo info) {
        info = info != null ? info : new PercentLayoutHelper.PercentLayoutInfo();
        return info;
    }

    /**
     * widthStr to PercentVal
     * <br/>
     * eg: 35%w => new PercentVal(35, true)
     *
     * @param percentStr
     * @param isOnWidth
     * @return
     */
    private static PercentVal getPercentVal(String percentStr, boolean isOnWidth) {
        //valid param
        if (percentStr == null) {
            return null;
        }
        Pattern p = Pattern.compile(REGEX_PERCENT);
        Matcher matcher = p.matcher(percentStr);
        if (!matcher.matches()) {
            throw new RuntimeException("the value of layout_xxxPercent invalid! ==>" + percentStr);
        }
        int len = percentStr.length();
        //extract the float value
        String floatVal = matcher.group(1);
        String lastAlpha = percentStr.substring(len - 1);

        float percent = Float.parseFloat(floatVal) / 100f;

        PercentVal percentVal = new PercentVal();
        percentVal.percent = percent;
        if (percentStr.endsWith(PercentBaseMode.SW)) {
            percentVal.basemode = PercentBaseMode.BASE_SCREEN_WIDTH;
        } else if (percentStr.endsWith(PercentBaseMode.SH)) {
            percentVal.basemode = PercentBaseMode.BASE_SCREEN_HEIGHT;
        } else if (percentStr.endsWith(PercentBaseMode.PERCENT)) {
            if (isOnWidth) {
                percentVal.basemode = PercentBaseMode.BASE_WIDTH;
            } else {
                percentVal.basemode = PercentBaseMode.BASE_HEIGHT;
            }
        } else if (percentStr.endsWith(PercentBaseMode.W)) {
            percentVal.basemode = PercentBaseMode.BASE_WIDTH;
        } else if (percentStr.endsWith(PercentBaseMode.H)) {
            percentVal.basemode = PercentBaseMode.BASE_HEIGHT;
        } else {
            throw new IllegalArgumentException("the " + percentStr + " must be endWith [%|w|h|sw|sh]");
        }

        return percentVal;
    }

    static boolean shouldHandleMeasuredWidthTooSmall(View view, PercentLayoutHelper.PercentLayoutInfo info) {
        int state = ViewCompat.getMeasuredWidthAndState(view) & ViewCompat.MEASURED_STATE_MASK;
        if (info == null || info.widthPercent == null) {
            return false;
        }
        return state == ViewCompat.MEASURED_STATE_TOO_SMALL && info.widthPercent.percent >= 0 &&
                info.mPreservedParams.width == ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    static boolean shouldHandleMeasuredHeightTooSmall(View view, PercentLayoutHelper.PercentLayoutInfo info) {
        int state = ViewCompat.getMeasuredHeightAndState(view) & ViewCompat.MEASURED_STATE_MASK;
        if (info == null || info.heightPercent == null) {
            return false;
        }
        return state == ViewCompat.MEASURED_STATE_TOO_SMALL && info.heightPercent.percent >= 0 &&
                info.mPreservedParams.height == ViewGroup.LayoutParams.WRAP_CONTENT;
    }


}
