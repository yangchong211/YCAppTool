package com.yc.alive.service;

import androidx.annotation.RestrictTo;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yc.alive.manager.AssistantManager;
import com.yc.alive.manager.AliveOptionManager;
import com.yc.alive.model.AliveOptionModel;
import com.yc.alive.util.AliveAppUtils;
import com.yc.alive.util.AliveExecutorUtils;
import com.yc.alive.util.AliveLogUtils;
import com.yc.alive.util.AliveStringUtils;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_PERMISSION_VIVO;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_2;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_BATTERY_OPPO;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_EMUI;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_MIUI;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_NOTIFICATION_OPPO;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_OPPO;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_POWER_MIUI;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_SAMSUNG;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_SMART;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_VIVO;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_VIVO_BATTERY;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_VIVO_BATTERY_2;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_WIFI;
import static com.yc.alive.constant.AliveSettingConst.PACKAGE_SETTING_WIFI_OPPO;

/**
 * 辅助功能服务
 */
@RestrictTo(LIBRARY)
public class AccessibilityService extends android.accessibilityservice.AccessibilityService implements OnProcessCallback {

    private static final String TAG = "KAAccessibilityService";

    private String mPackageName;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mPackageName = getApplication().getPackageName();
        AliveLogUtils.d(TAG, "onServiceConnected");
    }

    private boolean isPackageAccessEnabled(String packageName) {
        if (AliveStringUtils.isEmpty(packageName)) {
            return false;
        }
        if ("com.android.packageinstaller".equals(packageName)) {
            return true;
        }
        if (PACKAGE_SETTING.equals(packageName)) {
            return true;
        }
        if (PACKAGE_SETTING_2.equals(packageName)) {
            return true;
        }
        if (PACKAGE_SETTING_WIFI.equals(packageName)) {
            return true;
        }
        if (PACKAGE_SETTING_OPPO.equals(packageName)) {
            return true;
        }
        if (PACKAGE_SETTING_NOTIFICATION_OPPO.equals(packageName)) {
            return true;
        }
        if (PACKAGE_SETTING_WIFI_OPPO.equals(packageName)) {
            return true;
        }
        if (PACKAGE_SETTING_BATTERY_OPPO.equals(packageName)) {
            return true;
        }
        if (PACKAGE_PERMISSION_VIVO.equals(packageName)) {
            return true;
        }
        if (PACKAGE_SETTING_VIVO.equals(packageName)) {
            return true;
        }
        if (PACKAGE_SETTING_VIVO_BATTERY.equals(packageName)) {
            return true;
        }
        if (PACKAGE_SETTING_VIVO_BATTERY_2.equals(packageName)) {
            return true;
        }
        if (PACKAGE_SETTING_EMUI.equals(packageName)) {
            return true;
        }
        if (PACKAGE_SETTING_MIUI.equals(packageName)) {
            return true;
        }
        if (PACKAGE_SETTING_POWER_MIUI.equals(packageName)) {
            return true;
        }
        if (PACKAGE_SETTING_SMART.equals(packageName)) {
            return true;
        }
        if (PACKAGE_SETTING_SAMSUNG.equals(packageName)) {
            return true;
        }
        return false;
    }

    private boolean isAppAccess(String packageName) {
        if (AliveStringUtils.isEmpty(packageName)) {
            return false;
        }
        if (packageName.equals(mPackageName)) {
            return true;
        }
        return false;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event == null) {
            AliveLogUtils.d(TAG, "event == null");
            return;
        }

        AliveOptionModel option = null;

        try {
            // getSource 会抛出 IllegalStateException，Cannot perform this action on a sealed instance.
            AccessibilityNodeInfo source = event.getSource();
            if (source == null) {
                AliveLogUtils.d(TAG, "source == null");
                return;
            }
            int eventType = event.getEventType();
            if (eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                // KALogUtils.d(TAG, "!type " + event.getEventType() + ", name " + eventTypeToString(event.getEventType()));
                return;
            }

            option = AliveOptionManager.getInstance().getCurrentOption();
            if (option == null || !option.isFromApp()) {
                return;
            }

            CharSequence packageName = event.getPackageName();
            if (packageName == null) {
                return;
            }
            if (isAppAccess(packageName.toString())) {
                AssistantManager.getInstance().notifyBack();
                return;
            }
            if (!isPackageAccessEnabled(packageName.toString())) {
                return;
            }

            AliveLogUtils.d(TAG, "------- onAccessibilityEvent " + event.toString());

            final AccessibilityNodeInfo rootNode = getRootInActiveWindow();
            final CharSequence className = event.getClassName();
            if (className == null) {
                return;
            }

            final BaseAccessibility accessibility = AliveOptionManager.getInstance().getAccessibility();
            if (accessibility == null) {
                return;
            }

            final AliveOptionModel finalOption = option;
            AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                @Override
                public void runWorker() {
                    AliveAppUtils.sleep(100);
                }

                @Override
                public void runUI() {
                    accessibility.process(AccessibilityService.this, rootNode, className.toString(), finalOption,
                        AccessibilityService.this);
                }
            });
        } catch (Exception e) {
            if (option != null) {
                option.failWithMessage(e.getMessage());
            }
            AliveLogUtils.d(TAG, "exception " + e.getMessage());
        }
    }

    @Override
    public void onInterrupt() {
        AliveLogUtils.d(TAG, "onInterrupt");
    }

    @Override
    public void process(boolean isProcessed) {
        if (!isProcessed) {
            performGlobalAction(GLOBAL_ACTION_BACK);
        }
    }
}
