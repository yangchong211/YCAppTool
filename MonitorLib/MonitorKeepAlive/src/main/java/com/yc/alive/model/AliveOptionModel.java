package com.yc.alive.model;

import androidx.annotation.RestrictTo;
import androidx.annotation.StringRes;

import com.yc.alive.constant.AliveSettingType;
import com.yc.alive.constant.AliveSettingType.TYPE;
import com.yc.alive.service.AccessibilityService;
import com.yc.alive.util.AliveLogUtils;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

/**
 * 操作模型：用于记录 {@link AccessibilityService} 处理事件
 */
@RestrictTo(LIBRARY)
public class AliveOptionModel implements Cloneable {

    private static final String TAG = "KAOptionModel";

    /**
     * 类型：通知，自启动
     */
    @TYPE
    public int type;

    /**
     * 跳转的 intent
     */
    public AliveIntentModel intent;

    public boolean isNotSupport;

    @StringRes
    public int floatWindowRes;

    @StringRes
    public int notificationRes;
    @StringRes
    public int notificationAllowRes;

    @StringRes
    public int wifiConfigRes;
    @StringRes
    public int wifiKeepLiveRes;
    @StringRes
    public int wifiKeepLiveAlwaysRes;

    @StringRes
    public int selfStartRes;
    @StringRes
    public int selfStartSystemRes;
    @StringRes
    public int selfStartOtherRes;

    @StringRes
    public int batteryNoLimitRes;
    @StringRes
    public int batteryEnterRes;

    private boolean isFirstPageEntered = true;
    private boolean isSecondPageEntered = true;
    private boolean isThirdPageEntered = true;

    private boolean isFromApp = true;

    private boolean isFailed = false;
    private String failMessage;

    public void enterFirstPage() {
        isFirstPageEntered = false;
    }

    public void enterSecondPage() {
        isSecondPageEntered = false;
    }

    public void enterThirdPage() {
        isThirdPageEntered = false;
    }

    public boolean isFirstPageEntered() {
        return isFirstPageEntered;
    }

    public boolean isSecondPageEntered() {
        return isSecondPageEntered;
    }

    public boolean isThirdPageEntered() {
        return isThirdPageEntered;
    }

    public void failWithMessage(String message) {
        this.isFailed = true;
        this.failMessage = message;
        AliveLogUtils.d(TAG, "failWithMessage: " + message);
    }

    public boolean isFailed() {
        return isFailed;
    }

    /**
     * app 打开的
     */
    public boolean isFromApp() {
        return isFromApp;
    }

    public AliveOptionModel() {
        this.isFromApp = true;
        this.intent = new AliveIntentModel();
    }

    public AliveOptionModel(@TYPE int type) {
        this();
        this.type = type;
    }

    public void clear() {
        this.type = AliveSettingType.DEFAULT;
        this.isFromApp = false;
        this.isFirstPageEntered = true;
        this.isSecondPageEntered = true;
        this.isFailed = false;
    }

    @Override
    public AliveOptionModel clone() {
        AliveOptionModel model = null;
        try {
            model = (AliveOptionModel) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AliveOptionModel model = (AliveOptionModel) o;

        return type == model.type;
    }

    @Override
    public int hashCode() {
        return type;
    }
}
