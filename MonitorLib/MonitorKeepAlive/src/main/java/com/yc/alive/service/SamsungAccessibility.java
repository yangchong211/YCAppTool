package com.yc.alive.service;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ListView;

import androidx.annotation.RestrictTo;

import com.yc.alive.manager.AssistantManager;
import com.yc.alive.model.AliveOptionModel;
import com.yc.alive.util.AccessibilityUtils;
import com.yc.alive.util.AliveAppUtils;
import com.yc.alive.util.AliveExecutorUtils;

import java.util.HashSet;
import java.util.Set;

import static android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_BACK;
import static androidx.annotation.RestrictTo.Scope.LIBRARY;
import static com.yc.alive.constant.AliveSettingConst.CLASS_BATTERY_SAMSUNG;
import static com.yc.alive.constant.AliveSettingConst.CLASS_SLEEP_LIST_SAMSUNG;
import static com.yc.alive.util.AccessibilityUtils.findNodeByText;
import static com.yc.alive.util.AccessibilityUtils.findNodeInScrollableView;
import static com.yc.alive.util.AccessibilityUtils.getCheckableNode;
import static com.yc.alive.util.AccessibilityUtils.getClickableNode;
import static com.yc.alive.util.AccessibilityUtils.getNode;

/**
 * 事件处理
 */
@RestrictTo(LIBRARY)
public class SamsungAccessibility extends BaseAccessibility {

    @Override
    protected void processNotification(AccessibilityService service, AccessibilityNodeInfo rootNode,
                                       String className, AliveOptionModel option, OnProcessCallback callback) {

        processNotification24(service, rootNode, className, option, callback);
    }

    private static void processNotification24(final AccessibilityService service, AccessibilityNodeInfo rootNode,
                                              String className, final AliveOptionModel option, final OnProcessCallback callback) {

        if (CLASS_NAME_APPLICATION_INFO.equals(className)) {
            // 应用信息 - 通知
            if (option.isFirstPageEntered()) {
                // 首次进入
                option.enterFirstPage();
                AccessibilityNodeInfo lv = getNode(rootNode, ListView.class);
                findNodeInScrollableView(lv, service.getString(option.notificationRes), new AccessibilityUtils.Runnable() {
                    @Override
                    public void onFindBack(AccessibilityNodeInfo node) {
                        node = getClickableNode(node);
                        if (node == null) {
                            option.failWithMessage("CLASS_NAME_APPLICATION_INFO node null");
                            service.performGlobalAction(GLOBAL_ACTION_BACK);
                            callback.process(true);
                        } else {
                            final boolean performAction = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                                @Override
                                public void runWorker() {
                                    AliveAppUtils.sleep(500);
                                }

                                @Override
                                public void runUI() {
                                    if (!performAction) {
                                        option.failWithMessage("CLASS_NAME_APPLICATION_INFO performAction fail");
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
            } else {
                // 关闭页面，流程结束
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
            }
            return;
        } else if (CLASS_NAME_SUB_SETTING.equals(className)) {
            // 应用通知: 允许通知
            AccessibilityNodeInfo node = findNodeByText(rootNode, service.getString(option.notificationAllowRes));
            node = getCheckableNode(node);
            if (node == null) {
                option.failWithMessage("CLASS_NAME_SUB_SETTING node null");
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
            } else {
                if (node.isChecked()) {
                    // 已打开
                    service.performGlobalAction(GLOBAL_ACTION_BACK);
                    callback.process(true);
                } else {
                    // 去打开
                    AccessibilityNodeInfo clickableNode = getClickableNode(node);
                    if (clickableNode == null) {
                        option.failWithMessage("CLASS_NAME_SUB_SETTING clickableNode null");
                        service.performGlobalAction(GLOBAL_ACTION_BACK);
                        callback.process(true);
                    } else {
                        final boolean performAction = clickableNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                            @Override
                            public void runWorker() {
                                AliveAppUtils.sleep(500);
                            }

                            @Override
                            public void runUI() {
                                if (!performAction) {
                                    option.failWithMessage("CLASS_NAME_SUB_SETTING performAction fail");
                                }
                                service.performGlobalAction(GLOBAL_ACTION_BACK);
                                callback.process(true);
                            }
                        });
                    }
                }
            }
            return;
        }
        callback.process(false);
    }

    @Override
    protected void processBattery(final AccessibilityService service, final AccessibilityNodeInfo rootNode, String className,
                                  final AliveOptionModel option, final OnProcessCallback callback) {

        if (CLASS_BATTERY_SAMSUNG.equals(className)) {
            if (option.isFirstPageEntered()) {
                // 首次进入
                option.enterFirstPage();
                Set<CharSequence> hashSet = new HashSet<>();
                hashSet.add("android.widget.SemExpandableListView");
                AccessibilityNodeInfo node = getNode(rootNode, hashSet);
                findNodeInScrollableView(node, "未监视的应用程序", new AccessibilityUtils.Runnable() {
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
                // 关闭页面，流程结束
                service.performGlobalAction(GLOBAL_ACTION_BACK);
                callback.process(true);
            }
            return;
        } else if (CLASS_SLEEP_LIST_SAMSUNG.equals(className)) {
            final AccessibilityNodeInfo lv = getNode(rootNode, ListView.class);
            findNodeInScrollableView(lv, AssistantManager.getInstance().getAppName(), new AccessibilityUtils.Runnable() {
                @Override
                public void onFindBack(AccessibilityNodeInfo node) {
                    if (node != null) {
                        // 已经在列表里，不需要设置了
                        // 关闭页面，流程结束
                        service.performGlobalAction(GLOBAL_ACTION_BACK);
                        callback.process(true);
                    } else {
                        // 去添加
                        findNodeInScrollableView(lv, "添加应用程序", new AccessibilityUtils.Runnable() {
                            @Override
                            public void onFindBack(final AccessibilityNodeInfo findNode) {
                                final AccessibilityNodeInfo node = getClickableNode(findNode);
                                if (node == null) {
                                    option.failWithMessage("node null");
                                } else {
                                    boolean performAction = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    if (!performAction) {
                                        option.failWithMessage("performAction fail");
                                    }
                                }

                                // 等待页面
                                AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                                    @Override
                                    public void runWorker() {
                                        AliveAppUtils.sleep(1000);
                                        AccessibilityNodeInfo lv = getNode(rootNode, ListView.class);
                                    }

                                    @Override
                                    public void runUI() {
                                        findNodeInScrollableView(lv, AssistantManager.getInstance().getAppName(), new AccessibilityUtils.Runnable() {
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

                                                node = findNodeByText(rootNode, "完成");
                                                node = getClickableNode(node);
                                                if (node == null) {
                                                    option.failWithMessage("node null");
                                                } else {
                                                    boolean performAction = node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                                    if (!performAction) {
                                                        option.failWithMessage("performAction fail");
                                                    }
                                                }

                                                AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                                                    @Override
                                                    public void runWorker() {
                                                        // 等待页面
                                                        AliveAppUtils.sleep(500);
                                                    }

                                                    @Override
                                                    public void runUI() {
                                                        // 关闭页面，流程结束
                                                        service.performGlobalAction(GLOBAL_ACTION_BACK);

                                                        callback.process(true);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                }
            });
            return;
        }

        callback.process(false);
    }
}
