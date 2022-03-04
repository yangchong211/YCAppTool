package com.yc.alive.service;

import android.accessibilityservice.AccessibilityService;
import androidx.annotation.RestrictTo;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yc.alive.constant.AliveSettingConst;
import com.yc.alive.model.AliveOptionModel;
import com.yc.alive.util.KAAccessibilityUtils;
import com.yc.alive.util.KAAppUtils;
import com.yc.alive.util.KAExecutorUtils;
import com.yc.alive.util.AliveLogUtils;
import com.yc.alive.util.KAStringUtils;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;
import static androidx.annotation.RestrictTo.Scope.LIBRARY;
import static com.yc.alive.constant.AliveSettingType.TYPE_BATTERY;
import static com.yc.alive.constant.AliveSettingType.TYPE_FLOAT_WINDOW;
import static com.yc.alive.constant.AliveSettingType.TYPE_NOTIFICATION;
import static com.yc.alive.constant.AliveSettingType.TYPE_SELF_START;
import static com.yc.alive.constant.AliveSettingType.TYPE_WIFI_NEVER_SLEEP;
import static com.yc.alive.util.KAAccessibilityUtils.findNodeByText;
import static com.yc.alive.util.KAAccessibilityUtils.findNodeByTextContains;
import static com.yc.alive.util.KAAccessibilityUtils.getCheckableNode;
import static com.yc.alive.util.KAAccessibilityUtils.getClickableNode;

/**
 * Accessibility 事件处理基类
 */
@RestrictTo(LIBRARY)
public class BaseAccessibility {

    private static final String TAG = "KABaseAccessibility";

    // 悬浮窗
    protected static final String CLASS_NAME_FLOAT_WINDOW = AliveSettingConst.CLASS_FLOAT_WINDOW;
    // 应用信息
    protected static final String CLASS_NAME_APPLICATION_INFO = AliveSettingConst.CLASS_APPLICATION_INFO;
    // WIFI 设置
    protected static final String CLASS_NAME_WIFI_SETTING = AliveSettingConst.CLASS_WIFI_SETTING;
    // WIFI 设置 - 高级界面 - 有休眠设置
    protected static final String CLASS_NAME_WIFI_ADVANCED_SETTING = AliveSettingConst.CLASS_WIFI_ADVANCED_SETTING;
    // 设置二级页面
    protected static final String CLASS_NAME_SUB_SETTING = AliveSettingConst.CLASS_SUB_SETTING;
    // 弹窗
    protected static final String CLASS_NAME_DIALOG = AliveSettingConst.CLASS_DIALOG;

    public void process(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                        AliveOptionModel option, KAProcessCallback callback) {

        if (service == null || rootNode == null || KAStringUtils.isEmpty(className) || option == null) {
            AliveLogUtils.d(TAG,
                "service " + service + ", rootNode " + rootNode + ", className " + className + ", option " + option);

            if (option != null) {
                AliveLogUtils.d(TAG, "check null");
            }
            callback.process(false);
            return;
        }

        switch (option.type) {
            case TYPE_FLOAT_WINDOW:
                processFloatWindow(service, rootNode, className, option, callback);
                break;
            case TYPE_NOTIFICATION:
                processNotification(service, rootNode, className, option, callback);
                break;
            case TYPE_WIFI_NEVER_SLEEP:
                processWifi(service, rootNode, className, option, callback);
                break;
            case TYPE_SELF_START:
                processSelfStart(service, rootNode, className, option, callback);
                break;
            case TYPE_BATTERY:
                processBattery(service, rootNode, className, option, callback);
                break;
            default:
                callback.process(false);
                break;
        }
    }

    protected void processFloatWindow(final AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                      AliveOptionModel option, final KAProcessCallback callback) {

        if (CLASS_NAME_FLOAT_WINDOW.equals(className)) {
            // 悬浮窗设置页面
            AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.floatWindowRes));
            if (node == null) {
                node = findNodeByText(rootNode, "允许显示在其他应用的上层");
            }
            node = getCheckableNode(node);
            if (node == null) {
                option.failWithMessage("node null");
            } else {
                if (node.isChecked()) {
                    // 已有
                } else {
                    // 需要开启
                    AccessibilityNodeInfo clickableNode = getClickableNode(node);
                    if (clickableNode == null) {
                        option.failWithMessage("clickableNode null");
                    } else {
                        boolean performAction = clickableNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        if (!performAction) {
                            option.failWithMessage("performAction fail");
                        }
                    }
                }
            }

            if (option.isFailed()) {
                callback.process(false);
                return;
            }

            KAExecutorUtils.getInstance().runWorker(new KAExecutorUtils.KARunnable() {
                @Override
                public void runWorker() {
                    KAAppUtils.sleep(500);
                }

                @Override
                public void runUI() {
                    service.performGlobalAction(GLOBAL_ACTION_BACK);
                    callback.process(true);
                }
            });

            return;
        }

        callback.process(false);
    }

    protected void processNotification(AccessibilityService service, AccessibilityNodeInfo rootNode,
                                       String className, AliveOptionModel option, KAProcessCallback callback) {
    }

    protected void processWifi(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                               AliveOptionModel option, KAProcessCallback callback) {

        if (CLASS_NAME_WIFI_ADVANCED_SETTING.equals(className)) {
            // wifi 高级设置页面
            if (option.isFirstPageEntered()) {
                option.enterFirstPage();
                AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.wifiKeepLiveRes));
                node = getClickableNode(node);
                if (node == null) {
                    option.failWithMessage("node null");
                } else {
                    boolean performAction = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    if (!performAction) {
                        option.failWithMessage("performAction fail");
                    }
                }
                if (option.isFailed()) {
                    // 失败了，关闭页面，流程结束
                    service.performGlobalAction(GLOBAL_ACTION_BACK);
                }
            } else {
                // 关闭页面，流程结束
                service.performGlobalAction(GLOBAL_ACTION_BACK);
            }
            callback.process(true);
            return;
        } else if (CLASS_NAME_DIALOG.equals(className)) {
            // 弹窗
            AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.wifiKeepLiveAlwaysRes));
            if (node == null) {
                node = findNodeByTextContains(rootNode, service.getString(option.wifiKeepLiveAlwaysRes));
            }
            node = KAAccessibilityUtils.getCheckableNode(node);
            if (node == null) {
                option.failWithMessage("node null");
            } else {
                boolean performAction = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                if (!performAction) {
                    option.failWithMessage("performAction fail");
                }
            }
            callback.process(true);
            return;
        }

        callback.process(false);
    }

    protected void processSelfStart(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                    AliveOptionModel option, KAProcessCallback callback) {
    }

    protected void processBattery(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                  AliveOptionModel option, KAProcessCallback callback) {
    }
}
