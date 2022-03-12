package com.yc.alive.service;

import android.accessibilityservice.AccessibilityService;
import androidx.annotation.RestrictTo;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ListView;
import android.widget.ScrollView;

import com.yc.alive.manager.AssistantManager;
import com.yc.alive.model.AliveOptionModel;
import com.yc.alive.util.AccessibilityUtils;
import com.yc.alive.util.AliveAppUtils;
import com.yc.alive.util.AliveExecutorUtils;

import java.util.List;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;
import static androidx.annotation.RestrictTo.Scope.LIBRARY;
import static com.yc.alive.constant.AliveSettingConst.CLASS_APPLICATION_INFO_V2;
import static com.yc.alive.constant.AliveSettingConst.CLASS_PERMISSION_DETAIL_SMART;
import static com.yc.alive.constant.AliveSettingConst.CLASS_PERMISSION_SMART;
import static com.yc.alive.constant.AliveSettingConst.CLASS_WIFI_SETTING_V2;
import static com.yc.alive.util.AccessibilityUtils.findListItemNode;
import static com.yc.alive.util.AccessibilityUtils.findNodeByText;
import static com.yc.alive.util.AccessibilityUtils.getClickableNode;
import static com.yc.alive.util.AccessibilityUtils.getNode;
import static com.yc.alive.util.AccessibilityUtils.getSmartCheckableNode;
import static com.yc.alive.util.AccessibilityUtils.getSmartCheckableNodeList;

/**
 * Smart 事件处理
 */
@RestrictTo(LIBRARY)
public class SmartAccessibility extends BaseAccessibility {

    @Override
    protected void processFloatWindow(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                      AliveOptionModel option, OnProcessCallback callback) {

        processSelfStart2_5(service, rootNode, className, option, callback);
    }

    @Override
    protected void processNotification(AccessibilityService service, AccessibilityNodeInfo rootNode,
                                       String className, AliveOptionModel option, OnProcessCallback callback) {

        processNotification2_5(service, rootNode, className, option, callback);
    }

    @Override
    protected void processWifi(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                               AliveOptionModel option, OnProcessCallback callback) {

        processWifi2_5(service, rootNode, className, option, callback);
    }

    @Override
    protected void processSelfStart(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                    AliveOptionModel option, OnProcessCallback callback) {

        processSelfStart2_5(service, rootNode, className, option, callback);
    }

    @Override
    protected void processBattery(AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                  AliveOptionModel option, OnProcessCallback callback) {

        processSelfStart2_5(service, rootNode, className, option, callback);
    }

    private void processNotification2_5(final AccessibilityService service, AccessibilityNodeInfo rootNode,
                                        String className, AliveOptionModel option, final OnProcessCallback callback) {

        if (CLASS_APPLICATION_INFO_V2.equals(className)) {
            AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.notificationAllowRes));
            node = getSmartCheckableNode(node);
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

            AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                @Override
                public void runWorker() {
                    AliveAppUtils.sleep(500);
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

    private void processWifi2_5(final AccessibilityService service, AccessibilityNodeInfo rootNode, String className,
                                final AliveOptionModel option, final OnProcessCallback callback) {

        if (CLASS_WIFI_SETTING_V2.equals(className)) {
            AccessibilityNodeInfo lvNode = getNode(rootNode, ListView.class);
            findListItemNode(lvNode, service.getString(option.wifiKeepLiveRes), new AccessibilityUtils.Runnable() {
                @Override
                public void onFindBack(AccessibilityNodeInfo node) {
                    node = getSmartCheckableNode(node);
                    if (node == null) {
                        option.failWithMessage("node null");
                        service.performGlobalAction(GLOBAL_ACTION_BACK);
                        callback.process(true);
                    } else {
                        if (node.isChecked()) {
                            // 允许，不需要设置
                            service.performGlobalAction(GLOBAL_ACTION_BACK);
                            callback.process(true);
                        } else {
                            // 不允许，需要设置
                            boolean performAction = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            if (!performAction) {
                                option.failWithMessage("performAction fail");
                            }
                            // 需要手动暂停下，不然太快退出页面，不生效
                            AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                                @Override
                                public void runWorker() {
                                    AliveAppUtils.sleep(500);
                                }

                                @Override
                                public void runUI() {
                                    service.performGlobalAction(GLOBAL_ACTION_BACK);
                                    callback.process(true);
                                }
                            });
                        }
                    }
                }
            });
            return;
        }

        callback.process(false);
    }

    private void processSelfStart2_5(final AccessibilityService service, final AccessibilityNodeInfo rootNode, String className,
                                     final AliveOptionModel option, final OnProcessCallback callback) {

        if (CLASS_PERMISSION_SMART.equals(className)) {
            // 权限管理页面
            if (option.isFirstPageEntered()) {
                // 首次进入
                option.enterFirstPage();
                AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                    private AccessibilityNodeInfo lvNode;
                    @Override
                    public void runWorker() {
                        lvNode = getNode(rootNode, ListView.class);
                        if (lvNode == null) {
                            AliveAppUtils.sleep(1000);
                            lvNode = getNode(rootNode, ListView.class);
                        }
                    }

                    @Override
                    public void runUI() {
                        findListItemNode(lvNode, AssistantManager.getInstance().getAppName(),
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
                // 关闭页面，流程结束
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
            }
            return;
        } else if (CLASS_PERMISSION_DETAIL_SMART.equals(className)) {
            AccessibilityNodeInfo sv = getNode(rootNode, ScrollView.class);
            getSmartCheckableNodeList(sv, new AccessibilityUtils.ListRunnable() {
                @Override
                public void onFindBack(List<AccessibilityNodeInfo> list) {
                    if (list == null || list.isEmpty()) {
                        option.failWithMessage("node list null");
                    } else {
                        for (AccessibilityNodeInfo item : list) {
                            if (item.isChecked()) {
                                // 允许，不需要设置
                            } else {
                                // 不允许，需要设置
                                boolean performAction = item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                if (!performAction) {
                                    option.failWithMessage("performAction fail");
                                }
                            }
                        }
                    }

                    AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                        @Override
                        public void runWorker() {
                            AliveAppUtils.sleep(500);
                        }

                        @Override
                        public void runUI() {
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
