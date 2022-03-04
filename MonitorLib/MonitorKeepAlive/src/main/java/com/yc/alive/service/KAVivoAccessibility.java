package com.yc.alive.service;

import android.accessibilityservice.AccessibilityService;
import androidx.annotation.RestrictTo;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.GridView;
import android.widget.ListView;

import com.yc.alive.nova.ka.R;
import com.yc.alive.manager.AssistantManager;
import com.yc.alive.manager.AliveDeviceManager;
import com.yc.alive.model.AliveDeviceModel;
import com.yc.alive.model.AliveOptionModel;
import com.yc.alive.util.KAAccessibilityUtils;
import com.yc.alive.util.KAAppUtils;
import com.yc.alive.util.KAExecutorUtils;

import java.util.List;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;
import static androidx.annotation.RestrictTo.Scope.LIBRARY;
import static com.yc.alive.constant.AliveRomConst.SupportModel.VIVO_2_5;
import static com.yc.alive.constant.AliveRomConst.SupportModel.VIVO_3_0;
import static com.yc.alive.constant.AliveRomConst.SupportModel.VIVO_3_1;
import static com.yc.alive.constant.AliveRomConst.SupportModel.VIVO_4_0;
import static com.yc.alive.constant.AliveSettingConst.CLASS_APPLICATION_INFO;
import static com.yc.alive.constant.AliveSettingConst.CLASS_BATTERY_VIVO_V2_5;
import static com.yc.alive.constant.AliveSettingConst.CLASS_BATTERY_VIVO_V3_0;
import static com.yc.alive.constant.AliveSettingConst.CLASS_PERMISSION_APP_MANAGER_VIVO;
import static com.yc.alive.constant.AliveSettingConst.CLASS_PERMISSION_APP_VIVO;
import static com.yc.alive.constant.AliveSettingConst.CLASS_PERMISSION_VIVO;
import static com.yc.alive.constant.AliveSettingConst.CLASS_SELF_START_VIVO_V2_5;
import static com.yc.alive.constant.AliveSettingConst.CLASS_WIFI_ADVANCED_SETTING_V2;
import static com.yc.alive.util.KAAccessibilityUtils.findListItemNode;
import static com.yc.alive.util.KAAccessibilityUtils.findNodeById;
import static com.yc.alive.util.KAAccessibilityUtils.findNodeByText;
import static com.yc.alive.util.KAAccessibilityUtils.findNodeByTextContains;
import static com.yc.alive.util.KAAccessibilityUtils.getCheckableNode;
import static com.yc.alive.util.KAAccessibilityUtils.getClickableNode;
import static com.yc.alive.util.KAAccessibilityUtils.getNode;

/**
 * VIVO 事件处理
 */
@RestrictTo(LIBRARY)
public class KAVivoAccessibility extends BaseAccessibility {

    @Override
    protected void processFloatWindow(AccessibilityService service, AccessibilityNodeInfo rootNode, String className, AliveOptionModel option, KAProcessCallback callback) {
        AliveDeviceModel device = AliveDeviceManager.getInstance().getDevice();
        if (device == null) {
            callback.process(false);
            return;
        }

        switch (device.romVersionCode) {
            case VIVO_2_5:
            case VIVO_3_0:
            case VIVO_3_1:
                super.processFloatWindow(service, rootNode, className, option, callback);
                break;
            case VIVO_4_0:
                option.failWithMessage("vivo node");
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
                AssistantManager.getInstance().notifyBack();
                break;
            default:
                callback.process(false);
                break;
        }
    }

    @Override
    protected void processNotification(AccessibilityService service, AccessibilityNodeInfo rootNode,
                                       String className, AliveOptionModel option, KAProcessCallback callback) {

        AliveDeviceModel device = AliveDeviceManager.getInstance().getDevice();
        if (device == null) {
            callback.process(false);
            return;
        }

        switch (device.romVersionCode) {
            case VIVO_3_0:
                processNotificationV3_0(service, rootNode, className, option, callback);
                break;
            case VIVO_2_5:
            case VIVO_3_1:
                processNotificationV2_5(service, rootNode, className, option, callback);
                break;
            case VIVO_4_0:
                option.failWithMessage("vivo node");
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
                AssistantManager.getInstance().notifyBack();
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

        switch (device.romVersionCode) {
            case VIVO_3_0:
                processWifiV3_0(service, rootNode, className, option, callback);
                break;
            case VIVO_2_5:
            case VIVO_3_1:
                processWifiV2_5(service, rootNode, className, option, callback);
                break;
            case VIVO_4_0:
                option.failWithMessage("vivo node");
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
                AssistantManager.getInstance().notifyBack();
                break;
            default:
                callback.process(false);
                break;
        }
    }

    @Override
    protected void processSelfStart(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                    AliveOptionModel option, KAProcessCallback callback) {

        AliveDeviceModel device = AliveDeviceManager.getInstance().getDevice();
        if (device == null) {
            callback.process(false);
            return;
        }

        switch (device.romVersionCode) {
            case VIVO_2_5:
            case VIVO_3_0:
            case VIVO_3_1:
                processSelfStartV2_5(service, rootNode, className, option, callback);
                break;
            case VIVO_4_0:
                option.failWithMessage("vivo node");
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
                AssistantManager.getInstance().notifyBack();
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

        switch (device.romVersionCode) {
            case VIVO_3_0:
                processBatteryV3_0(service, rootNode, className, option, callback);
                break;
            case VIVO_2_5:
            case VIVO_3_1:
                processBatteryV2_5(service, rootNode, className, option, callback);
                break;
            case VIVO_4_0:
                option.failWithMessage("vivo node");
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
                AssistantManager.getInstance().notifyBack();
                break;
            default:
                callback.process(false);
                break;
        }
    }

    private void processFloatWindow4_0(final AccessibilityService service, final AccessibilityNodeInfo rootNode, String className,
                                       final AliveOptionModel option, final KAProcessCallback callback) {

        if (CLASS_PERMISSION_VIVO.equals(className)) {
            // 全局权限 tab - 找 app
            if (option.isFirstPageEntered()) {
                option.enterFirstPage();
                KAExecutorUtils.getInstance().runWorker(new KAExecutorUtils.KARunnable() {

                    private AccessibilityNodeInfo lv;

                    @Override
                    public void runWorker() {
                        lv = getNode(rootNode, ListView.class);
                        if (lv == null) {
                            KAAppUtils.sleep(1000);
                            lv = getNode(rootNode, ListView.class);
                        }
                    }

                    @Override
                    public void runUI() {
                        findListItemNode(lv, AssistantManager.getInstance().getAppName(), new KAAccessibilityUtils.Runnable() {
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
                });
            } else {
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
                AssistantManager.getInstance().notifyBack();
            }
            return;
        } else if (CLASS_PERMISSION_APP_VIVO.equals(className)) {
            // 应用权限页面
            if (option.isSecondPageEntered()) {
                option.enterSecondPage();
                AccessibilityNodeInfo node = findNodeByText(rootNode, "单项权限设置");
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
            } else {
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
            }
            return;
        } else if (CLASS_PERMISSION_APP_MANAGER_VIVO.equals(className)) {
            // 具体权限设置页面 开关不可点击
            option.failWithMessage("vivo node not click");
            service.performGlobalAction(GLOBAL_ACTION_BACK);
            callback.process(true);
            return;
        }
        callback.process(false);
    }

    private void processNotificationV2_5(AccessibilityService service, AccessibilityNodeInfo rootNode,
                                         String className, AliveOptionModel option, KAProcessCallback callback) {

        if (CLASS_NAME_APPLICATION_INFO.equals(className)) {
            // 应用信息 - 显示通知
            AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.notificationAllowRes));
            if (node == null) {
                option.failWithMessage("node null");
            } else {
                if (node.isChecked()) {
                    // 已经显示，不用设置了
                } else {
                    boolean performAction = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    if (!performAction) {
                        option.failWithMessage("performAction fail");
                    }
                }
            }

            // 关闭页面，流程结束
            service.performGlobalAction(GLOBAL_ACTION_BACK);

            callback.process(true);
            return;
        }

        callback.process(false);
    }

    private void processNotificationV3_0(AccessibilityService service, AccessibilityNodeInfo rootNode,
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
        } else if ("com.android.systemui.vivo.common.notification.settings.NotificationOpsDetailsActivity".equals(className)) {
            // 应用通知
            AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.notificationAllowRes));
            node = findNodeById(rootNode, "android:id/checkbox");
            if (node == null) {
                option.failWithMessage("node null");
            } else {
                if (node.isChecked()) {
                    // 允许通知，不需要设置
                } else {
                    // 不允许通知，需要设置
                    boolean performAction = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    if (!performAction) {
                        option.failWithMessage("performAction fail");
                    }
                }
            }
            service.performGlobalAction(GLOBAL_ACTION_BACK);
            callback.process(true);
            return;
        }

        callback.process(false);
    }

    private void processWifiV2_5(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                 AliveOptionModel option, KAProcessCallback callback) {

        if (CLASS_WIFI_ADVANCED_SETTING_V2.equals(className)) {
            // WIFI 高级设置 休眠设置 不可修改
            // 关闭页面，流程结束
            service.performGlobalAction(GLOBAL_ACTION_BACK);
            callback.process(true);
            return;
        }

        callback.process(false);
    }

    private void processWifiV3_0(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                 AliveOptionModel option, KAProcessCallback callback) {

        if (CLASS_WIFI_ADVANCED_SETTING_V2.equals(className)) {
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
            node = getCheckableNode(node);
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

    private void processSelfStartV2_5(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                      AliveOptionModel option, KAProcessCallback callback) {

        if (CLASS_SELF_START_VIVO_V2_5.equals(className)) {
            // 自启动管理页面 没有对外暴露，所以打开失败，执行不到这里。
            callback.process(true);
            return;
        }

        callback.process(false);
    }

    private void processBatteryV2_5(final AccessibilityService service, final AccessibilityNodeInfo rootNode, String className,
                                    final AliveOptionModel option, final KAProcessCallback callback) {

        if (CLASS_BATTERY_VIVO_V2_5.equals(className)) {
            KAExecutorUtils.getInstance().runWorker(new KAExecutorUtils.KARunnable() {
                private AccessibilityNodeInfo gv;
                @Override
                public void runWorker() {
                    // 允许高耗电
                    gv = getNode(rootNode, ListView.class, GridView.class);
                    if (gv == null) {
                        KAAppUtils.sleep(1000);
                        gv = getNode(rootNode, ListView.class);
                    }
                }

                @Override
                public void runUI() {
                    findListItemNode(gv, AssistantManager.getInstance().getAppName(), new KAAccessibilityUtils.Runnable() {
                        @Override
                        public void onFindBack(AccessibilityNodeInfo node) {
                            if (node == null) {
                                option.failWithMessage("node null");
                                service.performGlobalAction(GLOBAL_ACTION_BACK);
                                callback.process(false);
                                return;
                            }
                            AccessibilityNodeInfo parent = node.getParent();
                            if (parent == null) {
                                option.failWithMessage("parent null");
                                service.performGlobalAction(GLOBAL_ACTION_BACK);
                                callback.process(false);
                                return;

                            }
                            AccessibilityNodeInfo lvNode = parent.getParent();
                            if (lvNode == null) {
                                option.failWithMessage("lvNode null");
                                service.performGlobalAction(GLOBAL_ACTION_BACK);
                                callback.process(false);
                                return;
                            }
                            int index = -1;
                            for (int i = 0; i < lvNode.getChildCount(); i++) {
                                AccessibilityNodeInfo child = lvNode.getChild(i);
                                if (parent.equals(child)) {
                                    index = i;
                                    break;
                                }
                            }
                            if (index == -1) {
                                option.failWithMessage("index == -1");
                                service.performGlobalAction(GLOBAL_ACTION_BACK);
                                callback.process(false);
                                return;
                            }
                            List<AccessibilityNodeInfo> checkNodeList = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                checkNodeList = lvNode.findAccessibilityNodeInfosByViewId("com.vivo.abe:id/forbid_btn");
                            }
                            if (checkNodeList == null || checkNodeList.isEmpty()) {
                                option.failWithMessage("checkNodeList == null");
                                service.performGlobalAction(GLOBAL_ACTION_BACK);
                                callback.process(false);
                                return;
                            }
                            if (index > checkNodeList.size() - 1) {
                                option.failWithMessage("index > checkNodeList.size() - 1");
                                service.performGlobalAction(GLOBAL_ACTION_BACK);
                                callback.process(false);
                                return;
                            }
                            node = checkNodeList.get(index);
                            if (node == null) {
                                option.failWithMessage("node null");
                            } else {
                                if (node.isChecked()) {
                                    // 允许状态，不用处理
                                } else {
                                    // 去设置允许 NOTE: 这个节点不支持点击，肯定会失败
                                    boolean performAction = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    if (!performAction) {
                                        option.failWithMessage("performAction fail");
                                    }
                                }
                            }
                            service.performGlobalAction(GLOBAL_ACTION_BACK);

                            callback.process(true);
                        }
                    });
                }
            });

            return;
        }

        callback.process(false);
    }

    private void processBatteryV3_0(final AccessibilityService service, final AccessibilityNodeInfo rootNode, String className,
                                    final AliveOptionModel option, final KAProcessCallback callback) {

        if (CLASS_BATTERY_VIVO_V3_0.equals(className)) {
            KAExecutorUtils.getInstance().runWorker(new KAExecutorUtils.KARunnable() {
                private AccessibilityNodeInfo gv;
                @Override
                public void runWorker() {
                    // 允许高耗电
                    gv = getNode(rootNode, ListView.class, GridView.class);
                    if (gv == null) {
                        KAAppUtils.sleep(1000);
                        gv = getNode(rootNode, ListView.class);
                    }
                }

                @Override
                public void runUI() {
                    findListItemNode(gv, AssistantManager.getInstance().getAppName(), new KAAccessibilityUtils.Runnable() {
                        @Override
                        public void onFindBack(AccessibilityNodeInfo node) {
                            if (node == null) {
                                option.failWithMessage("node null");
                                service.performGlobalAction(GLOBAL_ACTION_BACK);
                                callback.process(false);
                                return;
                            }
                            AccessibilityNodeInfo parent = node.getParent();
                            if (parent == null) {
                                option.failWithMessage("parent null");
                                service.performGlobalAction(GLOBAL_ACTION_BACK);
                                callback.process(false);
                                return;
                            }
                            AccessibilityNodeInfo lvNode = parent.getParent();
                            if (lvNode == null) {
                                option.failWithMessage("lvNode null");
                                service.performGlobalAction(GLOBAL_ACTION_BACK);
                                callback.process(false);
                                return;
                            }
                            int index = -1;
                            for (int i = 0; i < lvNode.getChildCount(); i++) {
                                AccessibilityNodeInfo child = lvNode.getChild(i);
                                if (parent.equals(child)) {
                                    index = i;
                                    break;
                                }
                            }
                            if (index == -1) {
                                option.failWithMessage("index == -1");
                                service.performGlobalAction(GLOBAL_ACTION_BACK);
                                callback.process(false);
                                return;
                            }
                            List<AccessibilityNodeInfo> checkNodeList = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                checkNodeList = lvNode.findAccessibilityNodeInfosByViewId("com.vivo.abe:id/forbid_btn");
                            }
                            if (checkNodeList == null || checkNodeList.isEmpty()) {
                                option.failWithMessage("checkNodeList == null");
                                service.performGlobalAction(GLOBAL_ACTION_BACK);
                                callback.process(false);
                                return;
                            }
                            if (index > checkNodeList.size() - 1) {
                                option.failWithMessage("index > checkNodeList.size() - 1");
                                service.performGlobalAction(GLOBAL_ACTION_BACK);
                                callback.process(false);
                                return;
                            }
                            node = checkNodeList.get(index);
                            if (node == null) {
                                option.failWithMessage("node null");
                            } else {
                                if (node.isChecked()) {
                                    // 允许状态，不用处理
                                } else {
                                    // 去设置允许 NOTE: 这个节点不支持点击，肯定会失败
                                    boolean performAction = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    if (!performAction) {
                                        option.failWithMessage("performAction fail");
                                    }
                                }
                            }
                            service.performGlobalAction(GLOBAL_ACTION_BACK);

                            callback.process(true);
                        }
                    });
                }
            });

            return;
        }

        callback.process(false);
    }
}
