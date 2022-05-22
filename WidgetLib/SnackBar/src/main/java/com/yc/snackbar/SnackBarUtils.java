package com.yc.snackbar;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.IntRange;
import androidx.annotation.RestrictTo;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/09/18
 *     desc  : SnackbarUtils工具类
 *     revise:
 *     GitHub: https://github.com/yangchong211/YCDialog
 * </pre>
 */
public final class SnackBarUtils {

    /**
     * 不要直接new，否则抛个异常再说
     */
    private SnackBarUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DurationType {
        int LENGTH_INDEFINITE = Snackbar.LENGTH_INDEFINITE;
        int LENGTH_SHORT = Snackbar.LENGTH_SHORT;
        int LENGTH_LONG = Snackbar.LENGTH_LONG;
    }


    @RestrictTo(LIBRARY_GROUP)
    @IntDef({DurationType.LENGTH_INDEFINITE, DurationType.LENGTH_SHORT, DurationType.LENGTH_LONG})
    @IntRange(from = 1)
    @Retention(RetentionPolicy.SOURCE)
    private @interface Duration {
    }


    public static void showSnackBar(Activity activity, String text) {
        if(activity==null || text==null || text.length()==0){
            return;
        }
        SnackBarUtils.builder()
                .setBackgroundColor(activity.getResources().getColor(R.color.color_000000))
                .setTextSize(14)
                .setTextColor(activity.getResources().getColor(R.color.white))
                .setTextTypefaceStyle(Typeface.BOLD)
                .setText(text)
                .setMaxLines(1)
                .setActivity(activity)
                .setDuration(DurationType.LENGTH_SHORT)
                .build()
                .show();
    }


    public static void showSnackBar(Activity activity, String text, String action, View.OnClickListener listener) {
        if(activity==null || text==null || text.length()==0 || action==null || action.length()==0){
            return;
        }
        SnackBarUtils.builder()
                .setBackgroundColor(activity.getResources().getColor(R.color.color_000000))
                .setTextSize(14)
                .setTextColor(activity.getResources().getColor(R.color.white))
                .setTextTypefaceStyle(Typeface.BOLD)
                .setText(text)
                .setMaxLines(1)
                .setActionText(action)
                .setActionTextColor(activity.getResources().getColor(R.color.color_f25057))
                .setActionTextSize(14)
                .setActionTextTypefaceStyle(Typeface.BOLD)
                .setActionClickListener(listener)
                .setActivity(activity)
                .setDuration(DurationType.LENGTH_INDEFINITE)
                .build()
                .show();
    }

    public static void showSnackBar(Activity activity, String text, String action, @DrawableRes int resId, View.OnClickListener listener) {
        if(activity==null || text==null || text.length()==0 || action==null || action.length()==0 || resId!=0){
            return;
        }
        SnackBarUtils.builder()
                .setBackgroundColor(activity.getResources().getColor(R.color.color_000000))
                .setTextSize(14)
                .setTextColor(activity.getResources().getColor(R.color.white))
                .setTextTypefaceStyle(Typeface.BOLD)
                .setText(text)
                .setMaxLines(1)
                .setActionText(action)
                .setActionTextColor(activity.getResources().getColor(R.color.color_f25057))
                .setActionTextSize(14)
                .setActionTextTypefaceStyle(Typeface.BOLD)
                .setActionClickListener(listener)
                .setActivity(activity)
                .setDuration(DurationType.LENGTH_INDEFINITE)
                .setIcon(resId)
                .build()
                .show();
    }


    private final Builder builder;

    private SnackBarUtils(Builder builder) {
        this.builder = builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    private Snackbar make() {
        Snackbar snackbar = Snackbar.make(builder.view, builder.text, builder.duration);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        Button actionText = layout.findViewById(R.id.snackbar_action);
        TextView text = layout.findViewById(R.id.snackbar_text);


        //设置action内容和action点击事件
        if (builder.actionClickListener != null || builder.actionText != null) {
            if (builder.actionClickListener == null) {
                builder.actionClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                };
            }
            snackbar.setAction(builder.actionText, builder.actionClickListener);
            if (builder.actionTextColors != null) {
                snackbar.setActionTextColor(builder.actionTextColors);
            } else if (builder.actionTextColor != null) {
                snackbar.setActionTextColor(builder.actionTextColor);
            }
        }

        //设置背景
        if (builder.backgroundColor != null) {
            layout.setBackgroundColor(builder.backgroundColor);
        }

        //设置action内容文字大小
        if (builder.actionTextSize != null) {
            if (builder.actionTextSizeUnit != null) {
                actionText.setTextSize(builder.actionTextSizeUnit, builder.actionTextSize);
            } else {
                actionText.setTextSize(builder.actionTextSize);
            }
        }

        //设置action文字类型
        Typeface actionTextTypeface = actionText.getTypeface();
        if (builder.actionTextTypeface != null) {
            actionTextTypeface = builder.actionTextTypeface;
        }
        if (builder.actionTextTypefaceStyle != null) {
            actionText.setTypeface(actionTextTypeface, builder.actionTextTypefaceStyle);
        } else {
            actionText.setTypeface(actionTextTypeface);
        }


        //设置内容字体大小
        if (builder.textSize != null) {
            if (builder.textSizeUnit != null) {
                text.setTextSize(builder.textSizeUnit, builder.textSize);
            } else {
                text.setTextSize(builder.textSize);
            }
        }

        //设置内容字体样式
        Typeface textTypeface = text.getTypeface();
        if (builder.textTypeface != null) {
            textTypeface = builder.textTypeface;
        }
        if (builder.textTypefaceStyle != null) {
            text.setTypeface(textTypeface, builder.textTypefaceStyle);
        } else {
            text.setTypeface(textTypeface);
        }


        //设置颜色，最大行数和位置属性
        if (builder.textColors != null) {
            text.setTextColor(builder.textColors);
        } else if (builder.textColor != null) {
            text.setTextColor(builder.textColor);
        }
        text.setMaxLines(builder.maxLines);
        text.setGravity(builder.centerText ? Gravity.CENTER : Gravity.CENTER_VERTICAL);
        if (builder.centerText && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        //设置icon
        if (builder.icon != null) {
            Drawable transparentDrawable = null;
            if (builder.centerText && TextUtils.isEmpty(builder.actionText)) {
                Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                Bitmap bmp = Bitmap.createBitmap(builder.icon.getIntrinsicWidth(), builder.icon.getIntrinsicHeight(), conf);
                transparentDrawable = new BitmapDrawable(builder.view.getContext().getResources(), bmp);
            }
            text.setCompoundDrawablesWithIntrinsicBounds(builder.icon, null, transparentDrawable, null);
            text.setCompoundDrawablePadding(10);
        }
        return snackbar;
    }


    public static class Builder {

        private View view = null;
        private int duration = Snackbar.LENGTH_SHORT;
        private CharSequence text = "";
        private int textResId = 0;
        private Integer textColor = null;
        private ColorStateList textColors = null;
        private Integer textSizeUnit = null;
        private Float textSize = null;
        private Integer textTypefaceStyle = null;
        private Typeface textTypeface = null;
        private Integer actionTextSizeUnit = null;
        private Float actionTextSize = null;
        private CharSequence actionText = "";
        private int actionTextResId = 0;
        private Integer actionTextTypefaceStyle = null;
        private Typeface actionTextTypeface = null;
        private View.OnClickListener actionClickListener = null;
        private Integer actionTextColor = null;
        private ColorStateList actionTextColors = null;
        private int maxLines = Integer.MAX_VALUE;
        private boolean centerText = false;
        private Drawable icon = null;
        private int iconResId = 0;
        private Integer backgroundColor = null;

        private Builder() {
        }

        public Builder setActivity(Activity activity) {
            return setView(((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0));
        }

        public Builder setView(View view) {
            this.view = view;
            return this;
        }

        public Builder setText(@StringRes int resId) {
            this.textResId = resId;
            return this;
        }

        public Builder setText(CharSequence text) {
            this.textResId = 0;
            this.text = text;
            return this;
        }

        public Builder setTextColor(@ColorInt int color) {
            this.textColor = color;
            return this;
        }

        public Builder setTextColor(ColorStateList colorStateList) {
            this.textColor = null;
            this.textColors = colorStateList;
            return this;
        }

        public Builder setTextSize(float textSize) {
            this.textSizeUnit = null;
            this.textSize = textSize;
            return this;
        }

        public Builder setTextSize(int unit, float textSize) {
            this.textSizeUnit = unit;
            this.textSize = textSize;
            return this;
        }

        public Builder setTextTypeface(Typeface typeface) {
            this.textTypeface = typeface;
            return this;
        }

        public Builder setTextTypefaceStyle(int style) {
            this.textTypefaceStyle = style;
            return this;
        }

        public Builder centerText() {
            this.centerText = true;
            return this;
        }

        public Builder setActionTextColor(ColorStateList colorStateList) {
            this.actionTextColor = null;
            this.actionTextColors = colorStateList;
            return this;
        }

        public Builder setActionTextColor(@ColorInt int color) {
            this.actionTextColor = color;
            return this;
        }

        public Builder setActionText(@StringRes int resId) {
            this.actionTextResId = resId;
            return this;
        }

        public Builder setActionText(CharSequence text) {
            this.textResId = 0;
            this.actionText = text;
            return this;
        }

        public Builder setActionTextSize(float textSize) {
            this.actionTextSizeUnit = null;
            this.actionTextSize = textSize;
            return this;
        }

        public Builder setActionTextSize(int unit, float textSize) {
            this.actionTextSizeUnit = unit;
            this.actionTextSize = textSize;
            return this;
        }

        public Builder setActionTextTypeface(Typeface typeface) {
            this.actionTextTypeface = typeface;
            return this;
        }


        public Builder setActionTextTypefaceStyle(int style) {
            this.actionTextTypefaceStyle = style;
            return this;
        }

        public Builder setActionClickListener(View.OnClickListener listener) {
            this.actionClickListener = listener;
            return this;
        }

        public Builder setMaxLines(int maxLines) {
            this.maxLines = maxLines;
            return this;
        }

        public Builder setDuration(@Duration int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setIcon(@DrawableRes int resId) {
            this.iconResId = resId;
            return this;
        }

        public Builder setIcon(Drawable drawable) {
            this.icon = drawable;
            return this;
        }

        public Builder setBackgroundColor(@ColorInt int color) {
            this.backgroundColor = color;
            return this;
        }

        public Snackbar build() {
            return make();
        }

        private Snackbar make() {
            if (view == null) {
                throw new IllegalStateException("must set an Activity or a View before making a snack");
            }
            if (textResId != 0) {
                text = view.getResources().getText(textResId);
            }
            if (actionTextResId != 0) {
                actionText = view.getResources().getText(actionTextResId);
            }
            if (iconResId != 0) {
                icon = ContextCompat.getDrawable(view.getContext(), iconResId);
            }
            return new SnackBarUtils(this).make();
        }
    }


}
