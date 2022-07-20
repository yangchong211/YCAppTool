package com.yc.alive.service;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.RestrictTo;

import com.yc.alive.constant.AliveSettingConst;
import com.yc.alive.manager.AliveDeviceManager;
import com.yc.alive.manager.AssistantManager;
import com.yc.alive.model.AliveDeviceModel;
import com.yc.alive.model.AliveOptionModel;
import com.yc.alive.util.AccessibilityUtils;
import com.yc.alive.util.AliveAppUtils;
import com.yc.alive.util.AliveExecutorUtils;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;
import static androidx.annotation.RestrictTo.Scope.LIBRARY;
import static com.yc.alive.constant.AliveRomConst.SupportModel.EMUI_10;
import static com.yc.alive.constant.AliveRomConst.SupportModel.EMUI_11;
import static com.yc.alive.constant.AliveRomConst.SupportModel.EMUI_13;
import static com.yc.alive.constant.AliveRomConst.SupportModel.EMUI_14;
import static com.yc.alive.constant.AliveRomConst.SupportModel.EMUI_15;
import static com.yc.alive.constant.AliveSettingConst.CLASS_APP_NOTIFICATION_EMUI_V11;
import static com.yc.alive.constant.AliveSettingConst.CLASS_BATTERY_EMUI_V11;
import static com.yc.alive.constant.AliveSettingConst.CLASS_DIALOG_V2;
import static com.yc.alive.constant.AliveSettingConst.CLASS_SELF_START_EMUI_V11;
import static com.yc.alive.constant.AliveSettingConst.CLASS_SELF_START_EMUI_V14;
import static com.yc.alive.constant.AliveSettingConst.CLASS_WIFI_ADVANCED_SETTING;
import static com.yc.alive.util.AccessibilityUtils.findListItemNode;
import static com.yc.alive.util.AccessibilityUtils.findNodeByText;
import static com.yc.alive.util.AccessibilityUtils.getCheckableNode;
import static com.yc.alive.util.AccessibilityUtils.getClickableNode;
import static com.yc.alive.util.AccessibilityUtils.getNode;
import static com.yc.alive.util.AccessibilityUtils.refresh;

/**
 * EMUI 事件处理
 */
@RestrictTo(LIBRARY)
public class EmuiAccessibility extends BaseAccessibility {

    @Override
    protected void processNotification(AccessibilityService service, AccessibilityNodeInfo rootNode,
                                       String className, AliveOptionModel option, OnProcessCallback callback) {

        AliveDeviceModel device = AliveDeviceManager.getInstance().getDevice();
        if (device == null) {
            callback.process(false);
            return;
        }

        switch (device.romVersionCode) {
            case EMUI_10:
            case EMUI_11:
            case EMUI_13:
            case EMUI_14:
            case EMUI_15:
                processNotification11(service, rootNode, className, option, callback);
                break;
            default:
                callback.process(false);
                break;
        }
    }

    @Override
    protected void processWifi(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                               AliveOptionModel option, OnProcessCallback callback) {

        AliveDeviceModel device = AliveDeviceManager.getInstance().getDevice();
        if (device == null) {
            callback.process(false);
            return;
        }

        switch (device.romVersionCode) {
            case EMUI_10:
            case EMUI_11:
            case EMUI_13:
            case EMUI_14:
            case EMUI_15:
                processWifi11(service, rootNode, className, option, callback);
                break;
            default:
                callback.process(false);
                break;
        }
    }

    @Override
    protected void processSelfStart(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                    AliveOptionModel option, OnProcessCallback callback) {

        AliveDeviceModel device = AliveDeviceManager.getInstance().getDevice();
        if (device == null) {
            callback.process(false);
            return;
        }

        switch (device.romVersionCode) {
            case EMUI_10:
            case EMUI_11:
            case EMUI_13:
                processSelfStart11(service, rootNode, className, option, callback);
                break;
            case EMUI_14:
            case EMUI_15:
                processSelfStart14(service, rootNode, className, option, callback);
                break;
            default:
                callback.process(false);
                break;
        }
    }

    @Override
    protected void processBattery(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                  AliveOptionModel option, OnProcessCallback callback) {

        AliveDeviceModel device = AliveDeviceManager.getInstance().getDevice();
        if (device == null) {
            callback.process(false);
            return;
        }

        switch (device.romVersionCode) {
            case EMUI_10:
                processBattery10(service, rootNode, className, option, callback);
                break;
            case EMUI_11:
            case EMUI_13:
                processBattery11(service, rootNode, className, option, callback);
                break;
            case EMUI_14:
            case EMUI_15:
                processSelfStart14(service, rootNode, className, option, callback);
                break;
            default:
                callback.process(false);
                break;
        }
    }

    /**
     * 应用信息 - 通知 - 允许通知
     * 
     * 手动：
     * 
     * 1. 设置 - 通知和状态栏 - 通知管理 - 找到应用 - 允许通知
     * 
     * 2. 设置 - 应用管理 - 找到应用 - 通知 - 允许通知
     */
    private void processNotification11(AccessibilityService service, AccessibilityNodeInfo rootNode,
                                       String className, AliveOptionModel option, OnProcessCallback callback) {

        if (CLASS_NAME_APPLICATION_INFO.equals(className)) {
            // 应用信息 - 通知
            if (option.isFirstPageEntered()) {
                // 首次进入
                option.enterFirstPage();
                AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.notificationRes));
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
        } else if (CLASS_APP_NOTIFICATION_EMUI_V11.equals(className)) {
            // 应用通知具体设置页面
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
            // 关闭页面，流程结束
            service.performGlobalAction(GLOBAL_ACTION_BACK);
            callback.process(true);
            return;
        } else if (CLASS_DIALOG_V2.equals(className)) {
            // 手机管家第一次使用弹窗
            AccessibilityNodeInfo node = findNodeByText(rootNode, "同意");
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
            return;
        }
        callback.process(false);
    }

    /**
     * WLAN - 配置 - 在休眠状态下保持 WLAN 连接 - 弹窗 - 始终
     * <p>
     * 手动：设置 - WLAN - 配置 - 在休眠状态下保持 WLAN 连接 - 始终
     */
    private static void processWifi11(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                      AliveOptionModel option, OnProcessCallback callback) {

        if (CLASS_NAME_WIFI_SETTING.equals(className)) {
            // WIFI 设置
            if (option.isFirstPageEntered()) {
                // 首次进入
                option.enterFirstPage();
                AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.wifiConfigRes));
                node = getClickableNode(node);
                if (node == null) {
                    option.failWithMessage("node null");
                } else {
                    boolean performAction = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    if (!performAction) {
                        option.failWithMessage("performAction fail");
                    }
                }
            } else {
                // 关闭页面，流程结束
                service.performGlobalAction(GLOBAL_ACTION_BACK);
            }
            callback.process(true);
            return;
        } else if (CLASS_NAME_SUB_SETTING.equals(className) || CLASS_WIFI_ADVANCED_SETTING.equals(className)) {
            // 配置 WIFI
            if (option.isSecondPageEntered()) {
                option.enterSecondPage();
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
                // 关闭页面
                service.performGlobalAction(GLOBAL_ACTION_BACK);
            }
            callback.process(true);
            return;
        } else if (CLASS_NAME_DIALOG.equals(className)) {
            // 弹窗
            AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.wifiKeepLiveAlwaysRes));
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

    private static void processSelfStart14(final AccessibilityService service, final AccessibilityNodeInfo rootNode,
                                           String className, final AliveOptionModel option, final OnProcessCallback callback) {

        if (CLASS_SELF_START_EMUI_V14.equals(className)) {
            // 自启动管理页面
            if (option.isFirstPageEntered()) {
                // 首次进入
                option.enterFirstPage();
                AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                    private AccessibilityNodeInfo lv;
                    @Override
                    public void runWorker() {
                        lv = getNode(rootNode, ListView.class);
                        if (lv == null) {
                            AliveAppUtils.sleep(1000);
                            lv = getNode(rootNode, ListView.class);
                        }
                    }

                    @Override
                    public void runUI() {
                        findListItemNode(lv, AssistantManager.getInstance().getAppName(),
                            new AccessibilityUtils.Runnable() {
                                @Override
                                public void onFindBack(AccessibilityNodeInfo node) {
                                    node = getCheckableNode(node);
                                    if (node == null) {
                                        option.failWithMessage("node null");
                                        service.performGlobalAction(GLOBAL_ACTION_BACK);
                                        callback.process(true);
                                    } else {
                                        refresh(node);
                                        final AccessibilityNodeInfo finalNode = node;
                                        AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                                            @Override
                                            public void runWorker() {
                                                AliveAppUtils.sleep(500);
                                            }

                                            @Override
                                            public void runUI() {
                                                if (finalNode.isChecked()) {
                                                    // 目前是自动管理，需要手动管理
                                                    AccessibilityNodeInfo clickableNode = getClickableNode(finalNode);
                                                    if (clickableNode == null) {
                                                        option.failWithMessage("clickableNode null");
                                                    } else {
                                                        boolean performAction =
                                                            clickableNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                                        if (!performAction) {
                                                            option.failWithMessage("performAction fail");
                                                        }
                                                    }
                                                } else {
                                                    // 已经设置了，直接返回
                                                    service.performGlobalAction(GLOBAL_ACTION_BACK);
                                                }
                                                if (option.isFailed()) {
                                                    // 失败了，关闭页面，流程结束
                                                    service.performGlobalAction(GLOBAL_ACTION_BACK);
                                                }
                                                callback.process(true);
                                            }
                                        });
                                    }
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
        } else if (CLASS_NAME_DIALOG.equals(className)) {
            AccessibilityNodeInfo node = findNodeByText(rootNode, "允许自启动");
            node = getCheckableNode(node);
            if (node == null) {
                option.failWithMessage("node null");
            } else {
                if (node.isChecked()) {
                    // 已经设置了
                } else {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
            node = findNodeByText(rootNode, "允许关联启动");
            node = getCheckableNode(node);
            if (node == null) {
                option.failWithMessage("node null");
            } else {
                if (node.isChecked()) {
                    // 已经设置了
                } else {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
            node = findNodeByText(rootNode, "允许后台活动");
            node = getCheckableNode(node);
            if (node == null) {
                option.failWithMessage("node null");
            } else {
                if (node.isChecked()) {
                    // 已经设置了
                } else {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }

            // 关闭页面，流程结束
            service.performGlobalAction(GLOBAL_ACTION_BACK);
            callback.process(true);
            return;
        }
        callback.process(false);
    }

    /**
     * 手动：设置 - 应用管理 - 找到应用 - 权限 - 设置单项权限 - 信任此应用 & 应用自动启动
     */
    private static void processSelfStart11(final AccessibilityService service, final AccessibilityNodeInfo rootNode,
                                           String className, final AliveOptionModel option, final OnProcessCallback callback) {

        if (CLASS_SELF_START_EMUI_V11.equals(className)) {
            // 自启动管理页面
            if (option.isFirstPageEntered()) {
                // 首次进入
                option.enterFirstPage();
                AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                    private AccessibilityNodeInfo gv;
                    @Override
                    public void runWorker() {
                        gv = getNode(rootNode, GridView.class, ListView.class);
                        if (gv == null) {
                            AliveAppUtils.sleep(1000);
                            gv = getNode(rootNode, GridView.class, ListView.class);
                        }
                    }

                    @Override
                    public void runUI() {
                        findListItemNode(gv, AssistantManager.getInstance().getAppName(),
                            new AccessibilityUtils.Runnable() {
                                @Override
                                public void onFindBack(AccessibilityNodeInfo node) {
                                    node = getCheckableNode(node);
                                    if (node == null) {
                                        option.failWithMessage("node null");
                                        service.performGlobalAction(GLOBAL_ACTION_BACK);
                                        callback.process(true);
                                    } else {
                                        refresh(node);
                                        final AccessibilityNodeInfo finalNode = node;
                                        AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                                            @Override
                                            public void runWorker() {
                                                AliveAppUtils.sleep(500);
                                            }

                                            @Override
                                            public void runUI() {
                                                if (finalNode.isChecked()) {
                                                    // 已经设置了，直接返回
                                                    service.performGlobalAction(GLOBAL_ACTION_BACK);
                                                } else {
                                                    AccessibilityNodeInfo clickableNode = getClickableNode(finalNode);
                                                    if (clickableNode == null) {
                                                        option.failWithMessage("clickableNode null");
                                                    } else {
                                                        boolean performAction =
                                                            clickableNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                                        if (!performAction) {
                                                            option.failWithMessage("performAction fail");
                                                        }
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
        } else if (CLASS_NAME_DIALOG.equals(className)) {
            AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.selfStartRes));
            if (node == null) {
                option.failWithMessage("node null");
            } else {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            callback.process(true);
            return;
        }
        callback.process(false);
    }

    /**
     * 手动：设置 - 电池 - 锁屏清理应用 - 找到应用 - 设置不清理
     */
    private void processBattery11(final AccessibilityService service, final AccessibilityNodeInfo rootNode, final String className,
                                  final AliveOptionModel option, final OnProcessCallback callback) {

        AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
            private AccessibilityNodeInfo gv;
            @Override
            public void runWorker() {
                if (CLASS_BATTERY_EMUI_V11.equals(className)) {
                    // 锁屏清理应用
                    gv = getNode(rootNode, GridView.class);
                    if (gv == null) {
                        AliveAppUtils.sleep(1000);
                        gv = getNode(rootNode, GridView.class);
                    }
                    if (gv == null) {
                        gv = AccessibilityUtils.findNodeById(rootNode, AliveSettingConst.ID_BATTERY_LIST_ID_EMUI_V11);
                    }
                }
            }

            @Override
            public void runUI() {
                if (CLASS_BATTERY_EMUI_V11.equals(className)) {
                    // 锁屏清理应用
                    findListItemNode(gv, AssistantManager.getInstance().getAppName(), new AccessibilityUtils.Runnable() {
                        @Override
                        public void onFindBack(AccessibilityNodeInfo node) {
                            node = getCheckableNode(node);
                            if (node == null) {
                                option.failWithMessage("node null");
                                service.performGlobalAction(GLOBAL_ACTION_BACK);
                                callback.process(true);
                            } else {
                                refresh(node);
                                final AccessibilityNodeInfo finalNode = node;
                                AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                                    @Override
                                    public void runWorker() {
                                        AliveAppUtils.sleep(500);
                                    }

                                    @Override
                                    public void runUI() {
                                        if (finalNode.isChecked()) {
                                            // 清理状态，设置为不清理
                                            AccessibilityNodeInfo clickableNode = getClickableNode(finalNode);
                                            if (clickableNode == null) {
                                                option.failWithMessage("clickableNode null");
                                            } else {
                                                boolean performAction =
                                                    clickableNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                                if (!performAction) {
                                                    option.failWithMessage("performAction fail");
                                                }
                                            }
                                        } else {
                                            // 已经是不清理状态
                                        }
                                        service.performGlobalAction(GLOBAL_ACTION_BACK);
                                        callback.process(true);
                                    }
                                });
                            }
                        }
                    });
                    
                    return;
                }
                callback.process(false);
            }
        });
    }

    private void processBattery10(final AccessibilityService service, final AccessibilityNodeInfo rootNode, String className,
                                  final AliveOptionModel option, final OnProcessCallback callback) {

        if (CLASS_BATTERY_EMUI_V11.equals(className)) {
            AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                private AccessibilityNodeInfo lv;
                @Override
                public void runWorker() {
                    // 锁屏清理应用
                    lv = getNode(rootNode, ListView.class);
                    if (lv == null) {
                        AliveAppUtils.sleep(1000);
                        lv = getNode(rootNode, GridView.class);
                    }
                    if (lv == null) {
                        lv = AccessibilityUtils.findNodeById(rootNode, AliveSettingConst.ID_BATTERY_LIST_ID_EMUI_V11);
                    }
                }

                @Override
                public void runUI() {
                    findListItemNode(lv, AssistantManager.getInstance().getAppName(), new AccessibilityUtils.Runnable() {
                        @Override
                        public void onFindBack(AccessibilityNodeInfo node) {
                            node = getCheckableNode(node);
                            if (node == null) {
                                option.failWithMessage("node null");
                                service.performGlobalAction(GLOBAL_ACTION_BACK);
                                callback.process(true);
                            } else {
                                refresh(node);
                                final AccessibilityNodeInfo finalNode = node;
                                AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                                    @Override
                                    public void runWorker() {
                                        AliveAppUtils.sleep(500);
                                    }

                                    @Override
                                    public void runUI() {
                                        if (finalNode.isChecked()) {
                                            // 保护，不需要设置
                                        } else {
                                            // 未保护，需要设置
                                            AccessibilityNodeInfo clickableNode = getClickableNode(finalNode);
                                            if (clickableNode == null) {
                                                option.failWithMessage("clickableNode null");
                                            } else {
                                                boolean performAction =
                                                    clickableNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
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
                        }
                    });
                }
            });
            return;
        }
        callback.process(false);
    }
}
