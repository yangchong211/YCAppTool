package com.yc.window.config;

import android.content.pm.ActivityInfo;
import android.os.IBinder;
import android.view.View;

import com.yc.window.FloatWindow;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/08/18
 *     desc  : 设置window参数配置
 *     revise:
 * </pre>
 */
public final class ParamsConfig {

    /**
     * 窗口方向，默认是窗口的特定方向值-1
     */
    private int screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    /**
     * 是否外层可触摸
     */
    private boolean touchable;
    /**
     * token
     */
    private IBinder token = null;
    /**
     * 设置窗口透明度
     */
    private float alpha = -1f;
    /**
     * 设置垂直间距
     */
    private float verticalMargin = -1f;
    /**
     * 设置水平间距
     */
    private float horizontalMargin = -1f;
    /**
     * 设置状态栏可见性
     */
    private int systemUiVisibility = -1;
    /**
     * 设置窗口标题
     */
    private CharSequence title;
    /**
     * 设置屏幕的亮度
     */
    private float screenBrightness = -1;
    /**
     * 设置垂直权重
     */
    private float verticalWeight = -1;
    /**
     * 设置窗口在哪个显示屏上显示
     */
    private int preferredDisplayModeId = -1;
    /**
     * 设置按键的亮度
     */
    private float buttonBrightness = -1f;

    public int getScreenOrientation() {
        return screenOrientation;
    }

    public boolean isTouchable() {
        return touchable;
    }

    public IBinder getToken() {
        return token;
    }

    public float getAlpha() {
        return alpha;
    }

    public float getVerticalMargin() {
        return verticalMargin;
    }

    public float getHorizontalMargin() {
        return horizontalMargin;
    }

    public int getSystemUiVisibility() {
        return systemUiVisibility;
    }

    public CharSequence getTitle() {
        return title;
    }

    public float getScreenBrightness() {
        return screenBrightness;
    }

    public float getVerticalWeight() {
        return verticalWeight;
    }

    public int getPreferredDisplayModeId() {
        return preferredDisplayModeId;
    }

    public float getButtonBrightness() {
        return buttonBrightness;
    }

    public static class Builder {

        private final ParamsConfig config;

        private Builder() {
            config = new ParamsConfig();
        }

        /**
         * 设置窗口方向
         *
         * 自适应：{@link ActivityInfo#SCREEN_ORIENTATION_UNSPECIFIED}
         * 横屏：{@link ActivityInfo#SCREEN_ORIENTATION_LANDSCAPE}
         * 竖屏：{@link ActivityInfo#SCREEN_ORIENTATION_PORTRAIT}
         */
        public Builder setScreenOrientation(int screenOrientation) {
            config.screenOrientation = screenOrientation;
            return this;
        }

        /**
         * 是否外层可触摸
         */
        public Builder setOutsideTouchable(boolean touchable) {
            config.touchable = touchable;
            return this;
        }

        /**
         * 设置窗口 Token
         */
        public Builder setWindowToken(IBinder token) {
            config.token = token;
            return this;
        }

        /**
         * 设置窗口透明度
         */
        public Builder setWindowAlpha(float alpha) {
            config.alpha = alpha;
            return this;
        }

        /**
         * 设置垂直间距
         */
        public Builder setVerticalMargin(float verticalMargin) {
            config.verticalMargin = verticalMargin;
            return this;
        }

        /**
         * 设置水平间距
         */
        public Builder setHorizontalMargin(float horizontalMargin) {
            config.horizontalMargin = horizontalMargin;
            return this;
        }

        /**
         * 设置状态栏的可见性
         *
         * @see View#STATUS_BAR_VISIBLE
         * @see View#STATUS_BAR_HIDDEN
         */
        public Builder setSystemUiVisibility(int systemUiVisibility) {
            config.systemUiVisibility = systemUiVisibility;
            return this;
        }

        /**
         * 设置窗口标题
         */
        public Builder setWindowTitle(CharSequence title) {
            config.title = title;
            return this;
        }

        /**
         * 设置屏幕的亮度
         */
        public Builder setScreenBrightness(float screenBrightness) {
            config.screenBrightness = screenBrightness;
            return this;
        }

        /**
         * 设置垂直权重
         */
        public Builder setVerticalWeight(float verticalWeight) {
            config.verticalWeight = verticalWeight;
            return this;
        }

        /**
         * 设置窗口在哪个显示屏上显示
         */
        public Builder setPreferredDisplayModeId(int preferredDisplayModeId) {
            config.preferredDisplayModeId = preferredDisplayModeId;
            return this;
        }

        /**
         * 设置按键的亮度
         */
        public Builder setButtonBrightness(float buttonBrightness) {
            config.buttonBrightness = buttonBrightness;
            return this;
        }

        public ParamsConfig build() {
            return config;
        }
    }


}
