package com.yc.alive.service;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import androidx.annotation.RestrictTo;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ListView;
import android.widget.ScrollView;

import com.yc.alive.nova.ka.R;
import com.yc.alive.manager.AssistantManager;
import com.yc.alive.manager.AliveDeviceManager;
import com.yc.alive.model.AliveDeviceModel;
import com.yc.alive.model.AliveOptionModel;
import com.yc.alive.util.KAAccessibilityUtils;
import com.yc.alive.util.KAAppUtils;
import com.yc.alive.util.KAExecutorUtils;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;
import static androidx.annotation.RestrictTo.Scope.LIBRARY;
import static com.yc.alive.constant.AliveRomConst.SupportModel.MIUI_V9;
import static com.yc.alive.constant.AliveRomConst.SupportModel.MIUI_V9_MIX_2;
import static com.yc.alive.constant.AliveRomConst.SupportModel.MIUI_V9_MI_5;
import static com.yc.alive.constant.AliveRomConst.SupportModel.MIUI_V9_MI_6;
import static com.yc.alive.constant.AliveSettingConst.CLASS_APPLICATION_INFO;
import static com.yc.alive.constant.AliveSettingConst.CLASS_APP_LIST_MIUI;
import static com.yc.alive.constant.AliveSettingConst.CLASS_DIALOG_MIUI;
import static com.yc.alive.constant.AliveSettingConst.CLASS_NAME_SETTING_POWER_MIUI_V9;
import static com.yc.alive.constant.AliveSettingConst.CLASS_NAME_SETTING_POWER_SUB_MIUI_V9;
import static com.yc.alive.constant.AliveSettingConst.CLASS_PERMISSION_MIUI;
import static com.yc.alive.constant.AliveSettingConst.CLASS_SELF_START_DETAIL_MIUI_V9;
import static com.yc.alive.constant.AliveSettingConst.CLASS_SELF_START_MIUI_V9;
import static com.yc.alive.constant.AliveSettingConst.CLASS_WIFI_ADVANCED_SETTING_V3;
import static com.yc.alive.util.KAAccessibilityUtils.findListItemNode;
import static com.yc.alive.util.KAAccessibilityUtils.findNodeByText;
import static com.yc.alive.util.KAAccessibilityUtils.findNodeByTextContains;
import static com.yc.alive.util.KAAccessibilityUtils.findNodeInScrollableView;
import static com.yc.alive.util.KAAccessibilityUtils.getCheckableNode;
import static com.yc.alive.util.KAAccessibilityUtils.getClickableNode;
import static com.yc.alive.util.KAAccessibilityUtils.getNode;

/**
 * MIUI 事件处理
 * <p>
 *  2018/5/21.
 */
@RestrictTo(LIBRARY)
public class KAMiuiAccessibility extends BaseAccessibility {

    // 应用信息
    protected static final String CLASS_NAME_APPLICATION_INFO = "com.miui.appmanager.ApplicationsDetailsActivity";
    // 通知设置
    protected static final String CLASS_NAME_NOTIFICATION_SETTING =
        "com.android.settings.Settings$NotificationFilterActivity";

    /**
     * 手动：
     *
     * 设置 - 更多应用 - 找到应用 - 权限管理 - 显示悬浮窗 - 开启
     */
    @Override
    protected void processFloatWindow(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                      AliveOptionModel option, KAProcessCallback callback) {

        AliveDeviceModel device = AliveDeviceManager.getInstance().getDevice();
        if (device == null) {
            callback.process(false);
            return;
        }

        switch (device.romVersionName) {
            case MIUI_V9:
            case MIUI_V9_MI_6:
            case MIUI_V9_MI_5:
            case MIUI_V9_MIX_2:
                processFloatWindowV9(service, rootNode, className, option, callback);
                break;
            default:
                callback.process(false);
                break;
        }
    }

    private void processFloatWindowV9(final AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                      final AliveOptionModel option, final KAProcessCallback callback) {

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
        } else if (CLASS_PERMISSION_MIUI.equals(className)) {
            if (option.isFirstPageEntered()) {
                option.enterFirstPage();
                AccessibilityNodeInfo lv = getNode(rootNode, ListView.class);
                findListItemNode(lv, service.getString(R.string.ka_float_window_show), new KAAccessibilityUtils.Runnable() {
                    @Override
                    public void onFindBack(AccessibilityNodeInfo node) {
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
                        callback.process(true);
                    }
                });
            } else {
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
            }
            return;
        } else if (CLASS_DIALOG_MIUI.equals(className)) {
            AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(R.string.ka_common_allow));
            node = getClickableNode(node);
            if (node == null) {
                option.failWithMessage("node null");
            } else {
                boolean performAction = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                if (!performAction) {
                    option.failWithMessage("performAction fail");
                }
            }
            service.performGlobalAction(GLOBAL_ACTION_BACK);
            callback.process(true);
            return;
        }

        callback.process(false);
    }

    @Override
    protected void processNotification(AccessibilityService service, AccessibilityNodeInfo rootNode,
                                       String className, AliveOptionModel option, KAProcessCallback callback) {

        AliveDeviceModel device = AliveDeviceManager.getInstance().getDevice();
        if (device == null) {
            callback.process(false);
            return;
        }

        switch (device.romVersionName) {
            case MIUI_V9:
            case MIUI_V9_MI_6:
            case MIUI_V9_MI_5:
                processNotificationV9(service, rootNode, className, option, callback);
                break;
            case MIUI_V9_MIX_2:
                processNotificationV9_MIX_2(service, rootNode, className, option, callback);
                break;
            default:
                callback.process(false);
                break;
        }
    }

    @Override
    protected void processWifi(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                               AliveOptionModel option, KAProcessCallback callback) {

        AliveDeviceModel device = AliveDeviceManager.getInstance().getDevice();
        if (device == null) {
            callback.process(false);
            return;
        }

        switch (device.romVersionName) {
            case MIUI_V9:
            case MIUI_V9_MI_6:
            case MIUI_V9_MI_5:
            case MIUI_V9_MIX_2:
                processWifiV9(service, rootNode, className, option, callback);
                break;
            default:
                callback.process(false);
                break;
        }
    }

    /**
     * 手动：
     *
     * 设置 - 更多应用 - 找到应用 - 自启动
     */
    @Override
    protected void processSelfStart(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                    AliveOptionModel option, KAProcessCallback callback) {

        AliveDeviceModel device = AliveDeviceManager.getInstance().getDevice();
        if (device == null) {
            callback.process(false);
            return;
        }

        switch (device.romVersionName) {
            case MIUI_V9:
            case MIUI_V9_MI_6:
            case MIUI_V9_MI_5:
            case MIUI_V9_MIX_2:
                processSelfStartV9(service, rootNode, className, option, callback);
                break;
            default:
                callback.process(false);
                break;
        }
    }

    @Override
    protected void processBattery(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                  AliveOptionModel option, KAProcessCallback callback) {

        AliveDeviceModel device = AliveDeviceManager.getInstance().getDevice();
        if (device == null) {
            callback.process(false);
            return;
        }

        switch (device.romVersionName) {
            case MIUI_V9:
            case MIUI_V9_MI_6:
            case MIUI_V9_MI_5:
                processBatteryV9(service, rootNode, className, option, callback);
                break;
            case MIUI_V9_MIX_2:
                processBatteryV9_MIX_2(service, rootNode, className, option, callback);
                break;
            default:
                callback.process(false);
                break;
        }
    }

    /**
     * 手动：
     *
     * 设置 - 更多应用 - 找到应用 - 通知管理 - 允许通知
     */
    private static void processNotificationV9(AccessibilityService service, AccessibilityNodeInfo rootNode,
                                              String className, AliveOptionModel option, KAProcessCallback callback) {

        if (CLASS_NAME_APPLICATION_INFO.equals(className) || CLASS_APPLICATION_INFO.equals(className)) {
            // 应用信息 - 通知
            if (option.isFirstPageEntered()) {
                // 首次进入
                option.enterFirstPage();
                AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.notificationRes));
                if (node == null) {
                    node = findNodeByTextContains(rootNode, service.getString(R.string.ka_notification));
                }
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
        } else if (CLASS_NAME_NOTIFICATION_SETTING.equals(className)) {
            // 应用通知: 全部阻止 - 保证开关关闭
            AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.notificationAllowRes));
            node = getCheckableNode(node);
            if (node == null) {
                option.failWithMessage("node null");
            } else {
                if (node.isChecked()) {
                    // 允许通知，不需要设置
                } else {
                    // 不允许通知，需要设置
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
            service.performGlobalAction(GLOBAL_ACTION_BACK);
            callback.process(true);
            return;
        }

        callback.process(false);
    }

    private static void processNotificationV9_MIX_2(AccessibilityService service, AccessibilityNodeInfo rootNode,
                                                    String className, final AliveOptionModel option, final KAProcessCallback callback) {

        if (CLASS_APP_LIST_MIUI.equals(className)) {
            // 应用列表
            if (option.isFirstPageEntered()) {
                option.enterFirstPage();
                AccessibilityNodeInfo lvNode = getNode(rootNode, ListView.class);
                findListItemNode(lvNode, AssistantManager.getInstance().getAppName(), new KAAccessibilityUtils.Runnable() {
                    @Override
                    public void onFindBack(AccessibilityNodeInfo node) {
                        node = getClickableNode(node);
                        if (node == null) {
                            option.failWithMessage("node null");
                        } else {
                            boolean performAction = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            if (!performAction) {
                                option.failWithMessage("performAction fail");
                            }
                        }
                        callback.process(true);
                    }
                });
            } else {
                // 关闭页面，流程结束
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
            }
            return;
        } else if (CLASS_NAME_APPLICATION_INFO.equals(className) || CLASS_APPLICATION_INFO.equals(className)) {
            // 应用信息
            if (option.isSecondPageEntered()) {
                // 首次进入
                option.enterSecondPage();
                AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.notificationRes));
                if (node == null) {
                    node = findNodeByTextContains(rootNode, service.getString(R.string.ka_notification));
                }
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
        } else if (CLASS_NAME_NOTIFICATION_SETTING.equals(className)) {
            // 应用通知: 全部阻止 - 保证开关关闭
            AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.notificationAllowRes));
            node = getCheckableNode(node);
            if (node == null) {
                option.failWithMessage("node null");
            } else {
                if (node.isChecked()) {
                    // 允许通知，不需要设置
                } else {
                    // 不允许通知，需要设置
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
            service.performGlobalAction(GLOBAL_ACTION_BACK);
            callback.process(true);
            return;
        }

        callback.process(false);
    }

    private void processWifiV9(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                               AliveOptionModel option, KAProcessCallback callback) {

        if (CLASS_NAME_WIFI_ADVANCED_SETTING.equals(className) || CLASS_WIFI_ADVANCED_SETTING_V3.equals(className)) {
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
            node = KAAccessibilityUtils.getCheckableNode(node);
            if (node == null) {
                option.failWithMessage("node null");
            } else {
                boolean performAction = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                if (!performAction) {
                    option.failWithMessage("performAction fail");
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // 7.0 以上 Dialog 小时，不会有下一个页面显示的通知，这里需要手动回退
                service.performGlobalAction(GLOBAL_ACTION_BACK);
            }
            callback.process(true);
            return;
        }

        callback.process(false);
    }

    /**
     * 手动：
     *
     * 设置 - 更多应用 - 找到应用 - 自启动 - 允许系统 & 允许其他
     */
    private static void processSelfStartV9(final AccessibilityService service, final AccessibilityNodeInfo rootNode,
                                           String className, final AliveOptionModel option, final KAProcessCallback callback) {

        if (CLASS_SELF_START_MIUI_V9.equals(className)) {
            // 自启动管理
            if (option.isFirstPageEntered()) {
                // 首次进入
                option.enterFirstPage();
                KAExecutorUtils.getInstance().runWorker(new KAExecutorUtils.KARunnable() {
                    private AccessibilityNodeInfo lvNode;
                    @Override
                    public void runWorker() {
                        lvNode = getNode(rootNode, ListView.class);
                        if (lvNode == null) {
                            KAAppUtils.sleep(1000);
                            lvNode = getNode(rootNode, ListView.class);
                        }
                    }

                    @Override
                    public void runUI() {
                        findListItemNode(lvNode, AssistantManager.getInstance().getAppName(),
                            new KAAccessibilityUtils.Runnable() {
                                @Override
                                public void onFindBack(AccessibilityNodeInfo node) {
                                    if (node == null) {
                                        option.failWithMessage("node null");
                                    } else {
                                        AccessibilityNodeInfo clickableNode = getClickableNode(node);
                                        if (clickableNode != null) {
                                            clickableNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                        }
                                    }
                                    if (option.isFailed()) {
                                        // 失败了，关闭页面，流程结束
                                        service.performGlobalAction(GLOBAL_ACTION_BACK);
                                    }
                                    callback.process(true);
                                }
                            });
                    }
                });
            } else {
                // 关闭页面，流程结束
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
            }
            return;
        } else if (CLASS_SELF_START_DETAIL_MIUI_V9.equals(className)) {
            // 自启动详情
            AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.selfStartSystemRes));
            node = getCheckableNode(node);
            if (node == null) {
                option.failWithMessage("node null");
            } else {
                if (node.isChecked()) {
                    // 允许，不需要设置
                } else {
                    // 不允许，需要设置
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

            AccessibilityNodeInfo other = findNodeByText(rootNode, service.getString(option.selfStartOtherRes));
            other = getCheckableNode(other);
            if (other != null) {
                if (!other.isChecked()) {
                    // 不允许，需要设置
                    AccessibilityNodeInfo clickableNode = getClickableNode(other);
                    if (clickableNode != null) {
                        clickableNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                }
            }
            service.performGlobalAction(GLOBAL_ACTION_BACK);
            callback.process(true);
            return;
        }
        callback.process(false);
    }

    /**
     * 手动：
     *
     * 设置 - 更多应用 - 找到应用 - 省电策略 - 无限制
     */
    private static void processBatteryV9(final AccessibilityService service, AccessibilityNodeInfo rootNode,
                                         String className, final AliveOptionModel option, final KAProcessCallback callback) {

        if (CLASS_NAME_APPLICATION_INFO.equals(className) || CLASS_APPLICATION_INFO.equals(className)) {
            // 应用信息 - 通知
            if (option.isFirstPageEntered()) {
                // 首次进入
                option.enterFirstPage();
                AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.batteryEnterRes));
                if (node == null) {
                    AccessibilityNodeInfo srcollNode = getNode(rootNode, ScrollView.class);
                    findNodeInScrollableView(srcollNode, service.getString(option.batteryEnterRes), new KAAccessibilityUtils.Runnable() {
                        @Override
                        public void onFindBack(AccessibilityNodeInfo node) {
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
                            callback.process(true);
                        }
                    });
                }
            } else {
                // 关闭页面，流程结束
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
            }
            return;
        }

        if (CLASS_NAME_SETTING_POWER_MIUI_V9.equals(className)) {
            // 神隐模式
            if (option.isFirstPageEntered()) {
                // 首次进入
                option.enterFirstPage();
                AccessibilityNodeInfo lvNode = getNode(rootNode, ListView.class);
                findListItemNode(lvNode, AssistantManager.getInstance().getAppName(), new KAAccessibilityUtils.Runnable() {
                    @Override
                    public void onFindBack(AccessibilityNodeInfo node) {
                        if (node == null) {
                            option.failWithMessage("node null");
                        } else {
                            AccessibilityNodeInfo clickableNode = getClickableNode(node);
                            if (clickableNode != null) {
                                clickableNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        }
                        if (option.isFailed()) {
                            // 失败了，关闭页面，流程结束
                            service.performGlobalAction(GLOBAL_ACTION_BACK);
                        }
                        callback.process(true);
                    }
                });
            } else {
                // 关闭页面，流程结束
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
            }
            return;
        }

        if (CLASS_NAME_SETTING_POWER_SUB_MIUI_V9.equals(className)) {
            AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.batteryNoLimitRes));
            node = getClickableNode(node);
            if (node == null) {
                option.failWithMessage("node null");
            } else {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            // 关闭页面，流程结束
            service.performGlobalAction(GLOBAL_ACTION_BACK);
            callback.process(true);
            return;
        }
        callback.process(false);
        return;
    }

    private static void processBatteryV9_MIX_2(final AccessibilityService service, AccessibilityNodeInfo rootNode,
                                               String className, final AliveOptionModel option, final KAProcessCallback callback) {

        if (CLASS_APP_LIST_MIUI.equals(className)) {
            // 应用列表
            if (option.isFirstPageEntered()) {
                option.enterFirstPage();
                AccessibilityNodeInfo lvNode = getNode(rootNode, ListView.class);
                findListItemNode(lvNode, AssistantManager.getInstance().getAppName(), new KAAccessibilityUtils.Runnable() {
                    @Override
                    public void onFindBack(AccessibilityNodeInfo node) {
                        node = getClickableNode(node);
                        if (node == null) {
                            option.failWithMessage("node null");
                        } else {
                            boolean performAction = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            if (!performAction) {
                                option.failWithMessage("performAction fail");
                            }
                        }
                        callback.process(true);
                    }
                });
            } else {
                // 关闭页面，流程结束
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
            }
            return;
        } else if (CLASS_NAME_APPLICATION_INFO.equals(className) || CLASS_APPLICATION_INFO.equals(className)) {
            // 应用信息
            if (option.isSecondPageEntered()) {
                // 首次进入
                option.enterSecondPage();
                AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.batteryEnterRes));
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
        }

        if (CLASS_NAME_SETTING_POWER_MIUI_V9.equals(className)) {
            // 神隐模式
            if (option.isSecondPageEntered()) {
                // 首次进入
                option.enterSecondPage();
                AccessibilityNodeInfo lvNode = getNode(rootNode, ListView.class);
                findListItemNode(lvNode, AssistantManager.getInstance().getAppName(), new KAAccessibilityUtils.Runnable() {
                    @Override
                    public void onFindBack(AccessibilityNodeInfo node) {
                        if (node == null) {
                            option.failWithMessage("node null");
                        } else {
                            AccessibilityNodeInfo clickableNode = getClickableNode(node);
                            if (clickableNode != null) {
                                clickableNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        }
                        if (option.isFailed()) {
                            // 失败了，关闭页面，流程结束
                            service.performGlobalAction(GLOBAL_ACTION_BACK);
                        }
                        callback.process(true);
                    }
                });
            } else {
                // 关闭页面，流程结束
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
            }
            return;
        }

        if (CLASS_NAME_SETTING_POWER_SUB_MIUI_V9.equals(className)) {
            AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.batteryNoLimitRes));
            node = getClickableNode(node);
            if (node == null) {
                option.failWithMessage("node null");
            } else {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            // 关闭页面，流程结束
            service.performGlobalAction(GLOBAL_ACTION_BACK);
            callback.process(true);
            return;
        }
        callback.process(false);
    }
}
