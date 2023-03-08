package com.yc.notifymessage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 通知栏manager类
 *     revise:
 * </pre>
 */
public class NotificationManager {

    /**
     * 展示消息
     */
    public static final int MSG_SHOW = 1;
    /**
     * 隐藏消息
     */
    public static final int MSG_HIDE = 2;
    /**
     * key
     */
    public static final String BUNDLE_NOTIFICATION = "notification";
    public static final String BUNDLE_TYPE = "type";
    /**
     * 单例对象
     */
    private static volatile NotificationManager sInstance;
    /**
     * node节点链表
     */
    private final LinkedList<NotificationNode> mNodeLinkedList = new LinkedList<>();
    /**
     * 采用弱引用管理handler
     */
    private final WeakMyHandler mHandler = new WeakMyHandler(this);


    private NotificationManager() {

    }

    public static NotificationManager getInstance() {
        if (sInstance == null) {
            synchronized (NotificationManager.class) {
                if (sInstance == null) {
                    sInstance = new NotificationManager();
                }
            }
        }
        return sInstance;
    }

    void notify(final CustomNotification notification) {
        sendMessageShow(notification);
    }

    /**
     * 隐藏某类型消息
     *
     * @param type 消息类型
     */
    public void cancel(int type) {
        sendMessageHide(type);
    }

    protected void hideNotification() {
        if (mNodeLinkedList.isEmpty()) {
            return;
        }
        hideNotification(mNodeLinkedList.getFirst());
    }

    /**
     * 隐藏某通知
     *
     * @param notificationNode 需要被隐藏的通知
     */
    private void hideNotification(NotificationNode notificationNode) {
        if (mNodeLinkedList.isEmpty() || notificationNode == null) {
            return;
        }
        boolean isHead = notificationNode == mNodeLinkedList.getFirst();
        removeNotificationNode(notificationNode);
        notificationNode.changeIsShowing(false);
        if (isHead) {
            notificationNode.handleHide(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!mNodeLinkedList.isEmpty()) {
                        showNotification(mNodeLinkedList.getFirst().mNotification);
                    }
                }
            });
        }
    }

    /**
     * 依据 type 隐藏某通知
     *
     * @param type 通知类型
     */
    protected void hideNotification(int type) {
        NotificationNode notificationNode = findNodeByType(type);
        if (notificationNode != null) {
            hideNotification(notificationNode);
        }
    }

    /**
     * 通过 type 在当前显示队列里查找 NotificationNode
     *
     * @param type 通知类型
     * @return 该类型的通知节点
     */
    @Nullable
    private NotificationNode findNodeByType(int type) {
        for (NotificationNode node : mNodeLinkedList) {
            if (node.mNotification != null && type == node.mNotification.mType) {
                return node;
            }
        }
        return null;
    }

    /**
     * 开始自动消失的计时消息
     *
     * @param type    某通知的类型
     * @param timeout 自动消失的时间
     */
    protected void startTimeout(int type, int timeout) {
        if (mHandler != null && timeout != 0) {
            mHandler.removeMessages(MSG_HIDE + type);
            mHandler.sendEmptyMessageDelayed(MSG_HIDE + type, timeout);
        }
    }

    /**
     * 执行某通知的展示逻辑
     *
     * @param notification 某通知
     */
    protected void showNotification(@NonNull final CustomNotification notification) {
        try {
            if (mNodeLinkedList.isEmpty()) {
                //链表如果是空的，则插入数据，并获取第一个展示
                insertNotificationLocked(notification);
                mNodeLinkedList.getFirst().handleShow();
            } else {
                if (notification.mType == mNodeLinkedList.getFirst().mNotification.mType) {
                    insertNotificationLocked(notification);
                    NotificationNode first = mNodeLinkedList.getFirst();
                    if (!first.isShowing()) {
                        // 如果当前 notification 还没有展示，则展示
                        first.handleShow();
                    }
                } else {
                    //判断改通知的优先级是否比当前队头的通知高
                    boolean showImmediately = isHigherPriority(notification);
                    if (showImmediately) {
                        final NotificationNode oldFirst = mNodeLinkedList.getFirst();
                        insertNotificationLocked(notification);
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                oldFirst.handleHide(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        if (!mNodeLinkedList.isEmpty()) {
                                            mNodeLinkedList.getFirst().handleShow();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        insertNotificationLocked(notification);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 判断改通知的优先级是否比当前队头的通知高
     *
     * @param notification 需要判别的通知
     */
    private boolean isHigherPriority(CustomNotification notification) {
        return mNodeLinkedList.isEmpty() || mNodeLinkedList.getFirst().mNotification == null
                || mNodeLinkedList.getFirst().getPriority() < notification.getPriority();
    }

    /**
     * 将新的通知加入到通知队列
     *
     * @param notification 待入队的通知
     */
    private void insertNotificationLocked(@NonNull CustomNotification notification) {
        NotificationNode node = new NotificationNode(notification,this);
        if (mNodeLinkedList.isEmpty()) {
            // 如果当前链表为空，则直接赋值给 headNode
            mNodeLinkedList.offerFirst(node);
        } else if (mNodeLinkedList.contains(node)) {
            // 如果该 type 的通知已经在队列内，则直接更改通知信息并更新 UI
            NotificationNode exist = mNodeLinkedList.get(mNodeLinkedList.indexOf(node));
            exist.getNotification().setData(notification.getData(), true);
        } else if (isHigherPriority(notification)) {
            // 如果比队列头部的优先级高，则插入到链表头部
            mNodeLinkedList.offerFirst(node);
        } else {
            // 找到合适的位置，插入到队列中
            NotificationNode found = null;
            Iterator<NotificationNode> iterator = mNodeLinkedList.iterator();
            NotificationNode tmp = iterator.next();
            while (tmp != null) {
                if (node.compareTo(tmp) == NotificationNode.GREATER) {
                    found = tmp;
                    break;
                }
                if (iterator.hasNext()) {
                    tmp = iterator.next();
                } else {
                    break;
                }
            }
            if (found == null) {
                mNodeLinkedList.offerLast(node);
            } else {
                mNodeLinkedList.add(mNodeLinkedList.indexOf(found), node);
            }
        }
    }

    /**
     * 将某 NotificationNode 从链表中去除
     *
     * @param removeNode 需要移除的 node
     */
    private void removeNotificationNode(NotificationNode removeNode) {
        if (removeNode == null) {
            return;
        }
        mNodeLinkedList.remove(removeNode);
    }

    /**
     * 发送显示通知的消息
     *
     * @param notification 需要显示的通知
     */
    private void sendMessageShow(CustomNotification notification) {
        if (mHandler == null) {
            return;
        }
        Message msg = mHandler.obtainMessage(MSG_SHOW);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_NOTIFICATION, notification);
        msg.setData(bundle);
        msg.sendToTarget();
    }

    /**
     * 发送隐藏通知的消息
     *
     * @param type 需要隐藏的通知类型
     */
    private void sendMessageHide(int type) {
        if (mHandler == null) {
            return;
        }
        // 这里 message type 加上了通知的 type，是为了能够在 handleHide 时得到需要隐藏的 type，
        // 实现不同的通知执行各自的隐藏时间
        Message msg = mHandler.obtainMessage(MSG_HIDE + type);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_TYPE, type);
        msg.setData(bundle);
        msg.sendToTarget();
    }


}
