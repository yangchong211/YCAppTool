package com.yc.appleet.node;

/**
 * @author yangchong
 * 双向链表
 */
public class DoubleNode {

    private static class Node{
        //每个节点数据
        private final Object data;
        //下一个节点
        private Node next;

        public Node(Object data) {
            this.data = data;
        }
    }

}
