package com.yc.alive.service;

import android.accessibilityservice.AccessibilityService;
import androidx.annotation.RestrictTo;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ListView;

import com.yc.alive.nova.ka.R;
import com.yc.alive.manager.AssistantManager;
import com.yc.alive.manager.AliveDeviceManager;
import com.yc.alive.model.AliveDeviceModel;
import com.yc.alive.model.AliveOptionModel;
import com.yc.alive.util.AccessibilityUtils;
import com.yc.alive.util.AliveAppUtils;
import com.yc.alive.util.AliveExecutorUtils;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;
import static androidx.annotation.RestrictTo.Scope.LIBRARY;
import static com.yc.alive.constant.AliveRomConst.SupportModel.OPPO_V3_0;
import static com.yc.alive.constant.AliveRomConst.SupportModel.OPPO_V3_2;
import static com.yc.alive.constant.AliveRomConst.SupportModel.OPPO_V5_0;
import static com.yc.alive.constant.AliveRomConst.SupportModel.OPPP_V3_0_0;
import static com.yc.alive.constant.AliveSettingConst.CLASS_BATTERY_APP_OPPO_V3_0_0;
import static com.yc.alive.constant.AliveSettingConst.CLASS_BATTERY_LIST_OPPO_V3_0_0;
import static com.yc.alive.constant.AliveSettingConst.CLASS_BATTERY_OPPO_V3_0_0;
import static com.yc.alive.constant.AliveSettingConst.CLASS_FLOAT_WINDOW_OPPO_V3_0;
import static com.yc.alive.constant.AliveSettingConst.CLASS_FLOAT_WINDOW_OPPO_V3_0_0;
import static com.yc.alive.constant.AliveSettingConst.CLASS_NOTIFICATION_OPPO_V3_0_0;
import static com.yc.alive.constant.AliveSettingConst.CLASS_NOTIFICATION_OPPO_V5_0;
import static com.yc.alive.constant.AliveSettingConst.CLASS_PERMISSION_OPPO;
import static com.yc.alive.constant.AliveSettingConst.CLASS_SELF_START_OPPO_2;
import static com.yc.alive.constant.AliveSettingConst.CLASS_SELF_START_OPPO_V3_0;
import static com.yc.alive.constant.AliveSettingConst.CLASS_SELF_START_OPPO_V3_0_0;
import static com.yc.alive.constant.AliveSettingConst.CLASS_WIFI_OPPO_2;
import static com.yc.alive.constant.AliveSettingConst.CLASS_WIFI_OPPO_V3_0_0;
import static com.yc.alive.constant.AliveSettingConst.ID_BATTERY_LIST_ID_OPPO;
import static com.yc.alive.constant.AliveSettingConst.ID_FLOAT_WINDOW_LIST_ID_OPPO;
import static com.yc.alive.constant.AliveSettingConst.ID_SELF_START_LIST_ID_OPPO;
import static com.yc.alive.constant.AliveSettingConst.ID_WIFI_LIST_ID_OPPO;
import static com.yc.alive.util.AccessibilityUtils.findListItemNode;
import static com.yc.alive.util.AccessibilityUtils.findNodeById;
import static com.yc.alive.util.AccessibilityUtils.findNodeByText;
import static com.yc.alive.util.AccessibilityUtils.getCheckableNode;
import static com.yc.alive.util.AccessibilityUtils.getClickableNode;
import static com.yc.alive.util.AccessibilityUtils.getNode;

/**
 * Oppo 事件处理
 */
@RestrictTo(LIBRARY)
public class OppoAccessibility extends BaseAccessibility {

    @Override
    protected void processFloatWindow(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                      AliveOptionModel option, OnProcessCallback callback) {

        AliveDeviceModel device = AliveDeviceManager.getInstance().getDevice();

        if (device == null) {
            callback.process(false);
            return;
        }

        switch (device.romVersionName) {
            case OPPP_V3_0_0:
                processFloatWindowV3_0_0(service, rootNode, className, option, callback);
                break;
            case OPPO_V3_0:
            case OPPO_V3_2:
            case OPPO_V5_0:
                processFloatWindowV3_0(service, rootNode, className, option, callback);
                break;
            default:
                callback.process(false);
                break;
        }
    }

    @Override
    protected void processNotification(AccessibilityService service, AccessibilityNodeInfo rootNode,
                                       String className, AliveOptionModel option, OnProcessCallback callback) {

        AliveDeviceModel device = AliveDeviceManager.getInstance().getDevice();

        if (device == null) {
            callback.process(false);
            return;
        }

        switch (device.romVersionName) {
            case OPPP_V3_0_0:
            case OPPO_V3_0:
            case OPPO_V3_2:
            case OPPO_V5_0:
                processNotificationV3_0_0(service, rootNode, className, option, callback);
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

        switch (device.romVersionName) {
            case OPPP_V3_0_0:
                processWifiV3_0_0(service, rootNode, className, option, callback);
                break;
            case OPPO_V3_0:
            case OPPO_V3_2:
            case OPPO_V5_0:
                processWifiV3_0(service, rootNode, className, option, callback);
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

        switch (device.romVersionName) {
            case OPPP_V3_0_0:
                processSelfStartV3_0_0(service, rootNode, className, option, callback);
                break;
            case OPPO_V3_0:
                processSelfStartV3_0(service, rootNode, className, option, callback);
                break;
            case OPPO_V3_2:
            case OPPO_V5_0:
                processSelfStartV3_2(service, rootNode, className, option, callback);
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

        switch (device.romVersionName) {
            case OPPP_V3_0_0:
            case OPPO_V3_0:
            case OPPO_V3_2:
            case OPPO_V5_0:
                processBatteryV3_0_0(service, rootNode, className, option, callback);
                break;
            default:
                callback.process(false);
                break;
        }
    }

    private void processFloatWindowV3_0(final AccessibilityService service, AccessibilityNodeInfo rootNode,
                                        String className, final AliveOptionModel option, final OnProcessCallback callback) {

        if (CLASS_FLOAT_WINDOW_OPPO_V3_0.equals(className)) {
            // 悬浮窗
            AccessibilityNodeInfo lv = getNode(rootNode, ListView.class);
            findListItemNode(lv, AssistantManager.getInstance().getAppName(), new AccessibilityUtils.Runnable() {
                @Override
                public void onFindBack(AccessibilityNodeInfo node) {
                    node = getCheckableNode(node);
                    if (node == null) {
                        option.failWithMessage("node null");
                    } else {
                        if (node.isChecked()) {
                            // 已选中，已有权限
                        } else {
                            // 无权限，设置权限
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
                }
            });
            return;
        }

        callback.process(false);
    }

    private void processFloatWindowV3_0_0(final AccessibilityService service, AccessibilityNodeInfo rootNode,
                                          String className, final AliveOptionModel option, final OnProcessCallback callback) {

        if (CLASS_FLOAT_WINDOW_OPPO_V3_0_0.equals(className)) {
            // 悬浮窗
            AccessibilityNodeInfo lv = findNodeById(rootNode, ID_FLOAT_WINDOW_LIST_ID_OPPO);
            findListItemNode(lv, AssistantManager.getInstance().getAppName(), new AccessibilityUtils.Runnable() {
                @Override
                public void onFindBack(AccessibilityNodeInfo node) {
                    node = getCheckableNode(node);
                    if (node == null) {
                        option.failWithMessage("node null");
                    } else {
                        if (node.isChecked()) {
                            // 已选中，已有权限
                        } else {
                            // 无权限，设置权限
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
                }
            });
            return;
        }

        callback.process(false);
    }

    private void processNotificationV3_0_0(AccessibilityService service, AccessibilityNodeInfo rootNode,
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
        } else if (CLASS_NOTIFICATION_OPPO_V3_0_0.equals(className) || CLASS_NOTIFICATION_OPPO_V5_0.equals(className)) {
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
        }
        callback.process(false);
    }

    private void processWifiV3_0(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                 AliveOptionModel option, OnProcessCallback callback) {

        if (CLASS_WIFI_OPPO_2.equals(className)) {
            // WIFI 高级设置 休眠设置 不可修改
            // 关闭页面，流程结束
            service.performGlobalAction(GLOBAL_ACTION_BACK);
            callback.process(true);
            return;
        }

        callback.process(false);
    }

    private void processWifiV3_0_0(final AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                   final AliveOptionModel option, OnProcessCallback callback) {

        if (CLASS_WIFI_OPPO_V3_0_0.equals(className)) {
            // wifi 1. 先找高级设置，进入高级还是这个页面，2. 找休眠
            if (option.isFirstPageEntered()) {
                // 首次进入
                option.enterFirstPage();
                AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.wifiConfigRes));
                if (node == null) {
                    node = findNodeById(rootNode, ID_WIFI_LIST_ID_OPPO);
                    findListItemNode(node, service.getString(option.wifiConfigRes), new AccessibilityUtils.Runnable() {
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
                        }
                    });
                } else {
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
                }
            } else if (option.isSecondPageEntered()) {
                option.enterSecondPage();
                AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.wifiKeepLiveRes));
                node = getCheckableNode(node);
                if (node == null) {
                    option.failWithMessage("node null");
                } else {
                    if (node.isChecked()) {
                        // 已选中，无需设置
                    } else {
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
            } else {
                // 关闭页面，流程结束
                service.performGlobalAction(GLOBAL_ACTION_BACK);
            }
            callback.process(true);
            return;
        }

        callback.process(false);
    }

    private void processSelfStartV3_0(final AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                      final AliveOptionModel option, final OnProcessCallback callback) {

        if (CLASS_SELF_START_OPPO_V3_0.equals(className)) {
            // 自启动
            AccessibilityNodeInfo gv = getNode(rootNode, ListView.class);
            findListItemNode(gv, AssistantManager.getInstance().getAppName(), new AccessibilityUtils.Runnable() {
                @Override
                public void onFindBack(AccessibilityNodeInfo node) {
                    node = getCheckableNode(node);
                    if (node == null) {
                        option.failWithMessage("node null");
                    } else {
                        if (node.isChecked()) {
                            // 已选中，无需设置
                        } else {
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
                }
            });
            return;
        }
        callback.process(false);
    }

    private void processSelfStartV3_0_0(final AccessibilityService service, AccessibilityNodeInfo rootNode,
                                        String className, final AliveOptionModel option, final OnProcessCallback callback) {

        if (CLASS_SELF_START_OPPO_V3_0_0.equals(className)) {
            // 自启动
            AccessibilityNodeInfo gv = findNodeById(rootNode, ID_SELF_START_LIST_ID_OPPO);
            findListItemNode(gv, AssistantManager.getInstance().getAppName(), new AccessibilityUtils.Runnable() {
                @Override
                public void onFindBack(AccessibilityNodeInfo node) {
                    node = getCheckableNode(node);
                    if (node == null) {
                        option.failWithMessage("node null");
                    } else {
                        if (node.isChecked()) {
                            // 已选中，无需设置
                        } else {
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
                }
            });
            return;
        }
        callback.process(false);
    }

    private void processSelfStartV3_2(final AccessibilityService service, AccessibilityNodeInfo rootNode,
                                      String className, final AliveOptionModel option, final OnProcessCallback callback) {

        if (CLASS_PERMISSION_OPPO.equals(className)) {
            // 权限隐私页面
            if (option.isFirstPageEntered()) {
                option.enterFirstPage();
                AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(R.string.ka_self_start_manager));
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
                service.performGlobalAction(GLOBAL_ACTION_BACK);
            }
            callback.process(true);
            return;
        } else if (CLASS_SELF_START_OPPO_2.equals(className)) {
            // 自启动管理
            if (option.isSecondPageEntered()) {
                option.enterSecondPage();
                AccessibilityNodeInfo lv = getNode(rootNode, ListView.class);
                findListItemNode(lv, AssistantManager.getInstance().getAppName(), new AccessibilityUtils.Runnable() {
                    @Override
                    public void onFindBack(AccessibilityNodeInfo node) {
                        node = getCheckableNode(node);
                        if (node == null) {
                            option.failWithMessage("node null");
                        } else {
                            if (node.isChecked()) {
                                // 已选中，无需设置
                            } else {
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
                    }
                });
            } else {
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
            }
            return;
        }

        callback.process(false);
    }

    private void processBatteryV3_0_0(final AccessibilityService service, final AccessibilityNodeInfo rootNode, String className,
                                      final AliveOptionModel option, final OnProcessCallback callback) {

        // 电池 - 其他 - 后台冻结 & 自动优化
        if (CLASS_BATTERY_OPPO_V3_0_0.equals(className)) {
            // 电池
            if (option.isFirstPageEntered()) {
                option.enterFirstPage();
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
                service.performGlobalAction(GLOBAL_ACTION_BACK);
            }
            callback.process(true);
            return;
        } else if (CLASS_BATTERY_LIST_OPPO_V3_0_0.equals(className)) {
            // 其他
            if (option.isSecondPageEntered()) {
                option.enterSecondPage();
                AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                    private AccessibilityNodeInfo lv;
                    @Override
                    public void runWorker() {
                        lv = findNodeById(rootNode, ID_BATTERY_LIST_ID_OPPO);
                        if (lv == null) {
                            // 这个页面加载慢，暂停一会
                            AliveAppUtils.sleep(1000);
                            lv = findNodeById(rootNode, ID_BATTERY_LIST_ID_OPPO);
                        }
                    }

                    @Override
                    public void runUI() {
                        findListItemNode(lv, AssistantManager.getInstance().getAppName(),
                            new AccessibilityUtils.Runnable() {
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
            }
            return;
        } else if (CLASS_BATTERY_APP_OPPO_V3_0_0.equals(className)) {
            if (option.isThirdPageEntered()) {
                option.enterThirdPage();
                AccessibilityNodeInfo node =
                    findNodeByText(rootNode, service.getString(R.string.ka_battery_background_freeze));
                node = getCheckableNode(node);
                if (node == null) {
                    option.failWithMessage("node null");
                } else {
                    if (node.isChecked()) {
                        // 冻结状态，设置为不冻结
                        AccessibilityNodeInfo clickableNode = getClickableNode(node);
                        if (clickableNode == null) {
                            option.failWithMessage("clickableNode null");
                        } else {
                            boolean performAction = clickableNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            if (!performAction) {
                                option.failWithMessage("performAction fail");
                            }
                        }
                    } else {
                        // 已经是不清理状态
                    }
                }
                node = findNodeByText(rootNode, service.getString(R.string.ka_battery_auto_optimize));
                if (node == null) {
                    node = findNodeByText(rootNode, service.getString(R.string.ka_battery_auto_optimize_2));
                }
                node = getCheckableNode(node);
                if (node == null) {
                    option.failWithMessage("node null");
                } else {
                    if (node.isChecked()) {
                        // 冻结状态，设置为不冻结
                        AccessibilityNodeInfo clickableNode = getClickableNode(node);
                        if (clickableNode == null) {
                            option.failWithMessage("clickableNode null");
                        } else {
                            boolean performAction = clickableNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            if (!performAction) {
                                option.failWithMessage("performAction fail");
                            }
                        }
                    } else {
                        // 已经是不清理状态
                    }
                }
                node = findNodeByText(rootNode, service.getString(R.string.ka_battery_sleep));
                node = getCheckableNode(node);
                if (node == null) {
                    // ignore 有的手机没有
                } else {
                    if (node.isChecked()) {
                        // 设置为不睡眠
                        AccessibilityNodeInfo clickableNode = getClickableNode(node);
                        if (clickableNode == null) {
                            option.failWithMessage("clickableNode null");
                        } else {
                            boolean performAction = clickableNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            if (!performAction) {
                                option.failWithMessage("performAction fail");
                            }
                        }
                    } else {
                        // 已经是不清理状态
                    }
                }
            }
            service.performGlobalAction(GLOBAL_ACTION_BACK);
            callback.process(true);
            return;
        }
        callback.process(false);
    }
}
