package com.yc.alive.util;

import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

/**
 * 无障碍辅助功能工具类
 */
@RestrictTo(LIBRARY)
public class AccessibilityUtils {

    private static final String SMART_SWITCH = "smartisanos.widget.SwitchEx";

    public static List<AccessibilityNodeInfo> getParentNodeList(AccessibilityNodeInfo node, int level,
        Class...clazzArr) {

        AccessibilityNodeInfo parentNode = getParentNode(node, level);
        return getNodeList(parentNode, clazzArr);
    }

    private static AccessibilityNodeInfo getParentNode(AccessibilityNodeInfo node, int level) {
        if (node == null) {
            return null;
        }
        AccessibilityNodeInfo parent = node;
        for (int i = 0; i < level; i++) {
            AccessibilityNodeInfo newParent = parent.getParent();
            if (newParent == null) {
                break;
            }
            parent = newParent;
        }
        return parent;
    }

    public static List<AccessibilityNodeInfo> getNodeList(AccessibilityNodeInfo rootNode, Class...clazzArr) {
        List<AccessibilityNodeInfo> list = new ArrayList<>();
        if (rootNode == null || clazzArr == null) {
            return list;
        }
        Set<CharSequence> hashSet = new HashSet<>();
        for (Class clazz : clazzArr) {
            if (clazz != null) {
                hashSet.add(clazz.getName());
            }
        }
        return getNodeList(rootNode, hashSet);
    }

    public static List<AccessibilityNodeInfo> getNodeList(AccessibilityNodeInfo rootNode,
        CharSequence...charSequenceArr) {
        return getNodeList(rootNode, new HashSet<>(Arrays.asList(charSequenceArr)));
    }

    private static List<AccessibilityNodeInfo> getNodeList(AccessibilityNodeInfo rootNode, Set<CharSequence> set) {
        List<AccessibilityNodeInfo> list = new ArrayList<>();
        if (rootNode == null) {
            return list;
        }
        if (set == null || set.contains(rootNode.getClassName())) {
            list.add(rootNode);
        }
        for (int i = 0, len = rootNode.getChildCount(); i < len; i++) {
            AccessibilityNodeInfo child = rootNode.getChild(i);
            if (child != null) {
                list.addAll(getNodeList(child, set));
            }
        }
        return list;
    }

    public static List<AccessibilityNodeInfo> getNodeList(AccessibilityNodeInfo rootNode) {
        if (rootNode == null) {
            return Collections.emptyList();
        }
        List<AccessibilityNodeInfo> list = new ArrayList<>();
        Queue<AccessibilityNodeInfo> linkedList = new LinkedList<>();
        linkedList.add(rootNode);
        while (!linkedList.isEmpty()) {
            AccessibilityNodeInfo item = linkedList.poll();
            if (!list.contains(item)) {
                list.add(item);
                for (int i = 0, len = item.getChildCount(); i < len; i++) {
                    AccessibilityNodeInfo child = item.getChild(i);
                    if (child != null) {
                        linkedList.offer(child);
                    }
                }
            }
        }
        return list;
    }

    @Nullable
    public static AccessibilityNodeInfo getNode(AccessibilityNodeInfo rootNode, Class...clazzArr) {
        if (rootNode == null || clazzArr == null) {
            return null;
        }
        Set<CharSequence> hashSet = new HashSet<>();
        for (Class clazz : clazzArr) {
            if (clazz != null) {
                hashSet.add(clazz.getName());
            }
        }
        return getNode(rootNode, hashSet);
    }

    @Nullable
    public static AccessibilityNodeInfo getNode(AccessibilityNodeInfo rootNode, Set<CharSequence> set) {
        if (rootNode == null || set == null || set.isEmpty()) {
            return null;
        }
        List<AccessibilityNodeInfo> list = new ArrayList<>();
        Queue<AccessibilityNodeInfo> queue = new ArrayBlockingQueue<>(16);
        queue.add(rootNode);
        while (!queue.isEmpty()) {
            AccessibilityNodeInfo item = queue.poll();
            if (item != null) {
                if (set.contains(item.getClassName())) {
                    return item;
                }
                if (!list.contains(item)) {
                    list.add(item);
                    for (int i = 0, len = item.getChildCount(); i < len; i++) {
                        AccessibilityNodeInfo child = item.getChild(i);
                        if (child != null) {
                            queue.offer(child);
                        }
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    public static AccessibilityNodeInfo findNodeByText(AccessibilityNodeInfo root, String text) {
        if (root == null || AliveStringUtils.isEmpty(text)) {
            return null;
        }
        List<AccessibilityNodeInfo> list = root.findAccessibilityNodeInfosByText(text);
        if (list == null || list.isEmpty()) {
            return null;
        }
        for (AccessibilityNodeInfo node : list) {
            if (node != null) {
                CharSequence nodeText = node.getText();
                if (nodeText != null && nodeText.toString().equalsIgnoreCase(text)) {
                    return node;
                }
            }
        }
        return null;
    }

    @Nullable
    public static AccessibilityNodeInfo findNodeByTextContains(AccessibilityNodeInfo root, String text) {
        if (root == null || AliveStringUtils.isEmpty(text)) {
            return null;
        }
        List<AccessibilityNodeInfo> list = root.findAccessibilityNodeInfosByText(text);
        if (list == null || list.isEmpty()) {
            return null;
        }
        for (AccessibilityNodeInfo node : list) {
            if (node != null) {
                CharSequence nodeText = node.getText();
                if (nodeText != null && nodeText.toString().contains(text)) {
                    return node;
                }
            }
        }
        return null;
    }

    @Nullable
    public static AccessibilityNodeInfo findNodeById(AccessibilityNodeInfo root, String id) {
        if (root == null || AliveStringUtils.isEmpty(id)) {
            return null;
        }
        List<AccessibilityNodeInfo> list = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            list = root.findAccessibilityNodeInfosByViewId(id);
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public static void findListItemNode(final AccessibilityNodeInfo listNode, final String text, final Runnable runnable) {
        if (listNode == null || AliveStringUtils.isEmpty(text)) {
            runnable.onFindBack(null);
            return;
        }

        AccessibilityNodeInfo node = findNodeByText(listNode, text);
        if (node == null && listNode.isScrollable()) {
            listNode.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                @Override
                public void runWorker() {
                    AliveAppUtils.sleep(500);
                }

                @Override
                public void runUI() {
                    findListItemNode(listNode, text, runnable);
                }
            });
        } else {
            runnable.onFindBack(node);
        }
    }

    public static void findNodeInScrollableView(final AccessibilityNodeInfo listNode, final String text, final Runnable runnable) {
        if (listNode == null || AliveStringUtils.isEmpty(text)) {
            runnable.onFindBack(null);
            return;
        }

        AccessibilityNodeInfo node = findNodeByText(listNode, text);
        if (node == null && listNode.isScrollable()) {
            listNode.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                @Override
                public void runWorker() {
                    AliveAppUtils.sleep(500);
                }

                @Override
                public void runUI() {
                    findNodeInScrollableView(listNode, text, runnable);
                }
            });
        } else {
            runnable.onFindBack(node);
        }
    }

    public interface Runnable {
        void onFindBack(AccessibilityNodeInfo node);
    }

    @Nullable
    public static AccessibilityNodeInfo getClickableNode(AccessibilityNodeInfo node) {
        if (node == null) {
            return null;
        }
        if (node.isClickable()) {
            return node;
        }
        AccessibilityNodeInfo parent = node.getParent();
        return getClickableNode(parent);
    }

    @Nullable
    public static AccessibilityNodeInfo getCheckableNode(AccessibilityNodeInfo node) {
        if (node == null) {
            return null;
        }
        if (node.isCheckable()) {
            return node;
        }
        AccessibilityNodeInfo parent = node.getParent();
        if (parent == null) {
            return null;
        }
        return getNode(parent, CheckBox.class, Switch.class, CheckedTextView.class);
    }

    @Nullable
    public static AccessibilityNodeInfo getSmartCheckableNode(AccessibilityNodeInfo node) {
        if (node == null) {
            return null;
        }
        if (node.isCheckable()) {
            return node;
        }
        AccessibilityNodeInfo parent = node.getParent();
        if (parent == null) {
            return null;
        }
        Set<CharSequence> hashSet = new HashSet<>();
        hashSet.add(SMART_SWITCH);
        return getNode(parent, hashSet);
    }

    @Nullable
    public static void getSmartCheckableNodeList(final AccessibilityNodeInfo node, final ListRunnable runnable) {
        if (node == null) {
            runnable.onFindBack(null);
            return;
        }
        final AccessibilityNodeInfo parent = node.getParent();
        if (parent == null) {
            runnable.onFindBack(null);
            return;
        }
        final Set<CharSequence> hashSet = new HashSet<>();
        hashSet.add(SMART_SWITCH);
        final List<AccessibilityNodeInfo> allNode = getNodeList(parent, hashSet);
        if (node.isScrollable()) {
            node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            AliveExecutorUtils.getInstance().runWorker(new AliveExecutorUtils.KARunnable() {
                @Override
                public void runWorker() {
                    AliveAppUtils.sleep(500);
                }

                @Override
                public void runUI() {
                    List<AccessibilityNodeInfo> nodeList = getNodeList(parent, hashSet);
                    if (nodeList != null) {
                        for (AccessibilityNodeInfo node : nodeList) {
                            if (allNode.contains(node)) {
                                continue;
                            }
                            allNode.add(node);
                        }
                    }
                    runnable.onFindBack(allNode);
                }
            });
        } else {
            runnable.onFindBack(allNode);
        }
    }

    public interface ListRunnable {
        void onFindBack(List<AccessibilityNodeInfo> list);
    }

    public static void refresh(AccessibilityNodeInfo node) {
        if (node == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            node.refresh();
        }
    }
}
