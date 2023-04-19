package com.yc.appleet.node;

import com.yc.toolutils.AppLogUtils;

/**
 * @author yangchong
 * 单链表
 */
public class SingleNode {

    /**
     * 单链表node
     */
    public static class Node {
        //每个节点数据
        private final Object data;
        //每个节点指向下一个节点数据
        private Node next;

        public Node(Object data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    ", next=" + next +
                    '}';
        }
    }

    class Node1{
        private int data;
        private Node1 node1;

        public Node1(int data){
            this.data = data;
        }
    }

    private static class NodeList {

        private int size;//链表节点的个数
        private Node head;//头节点

        public NodeList() {
            size = 0;
            head = null;
        }

        public Object add(Object obj) {
            Node newHead = new Node(obj);
            if (size == 0) {
                //第一个元素
                head = newHead;
            } else {
                newHead.next = head;
                head = newHead;
            }
            size++;
            return obj;
        }

        public Object removeHeader() {
            Object obj = head.data;
            head = head.next;
            size--;
            return obj;
        }

        public Node insert(Object obj){
            Node node = new Node(obj);
            if (size == 0){
                head = node;
            } else {
                node.next = head;
                head = node;
            }
            size++;
            return node;
        }

        public Node find(Object obj){
            Node current = head;
            int tempSize = size;
            while (tempSize > 0){
                if (current.data == obj){
                    return current;
                } else {
                    current = current.next;
                }
                tempSize--;
            }
            return null;
        }

        public void printAll(){
            if (size > 0){
                Node node = head;
                int tempSize = size;
                if (size == 1){
                    System.out.println("["+node.data+"]");
                    return;
                }
                while (tempSize>0){
                    //不断遍历下一个
                    if (node == head){
                        AppLogUtils.d("["+node.data+"->");
                    } else if (node.next == null){
                        AppLogUtils.d(node.data+"]");
                    } else {
                        AppLogUtils.d(node.data+"->");
                    }
                    node = node.next;
                    tempSize--;
                }
            } else {
                //如果链表一个节点都没有，直接打印[]
                AppLogUtils.d("[]");
            }
        }
    }

    public void test() {
        NodeList nodeList = new NodeList();
        nodeList.add(5);
        nodeList.add(4);
        nodeList.add(3);
        nodeList.add(2);
        nodeList.add(1);
        nodeList.add(9);
        nodeList.printAll();
    }

    public void testFind() {
        NodeList nodeList = new NodeList();
        nodeList.add(5);
        nodeList.add(4);
        nodeList.add(3);
        nodeList.add(2);
        nodeList.add(1);
        nodeList.add(9);
        Node node = nodeList.find(3);
        AppLogUtils.d("testFind : " + node.toString());
    }
}
