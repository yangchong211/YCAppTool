package com.yc.appleet.queue;

import android.util.Log;

/**
 *    @author yangchong
 *    GitHub : https://github.com/yangchong211/YCAppTool
 *    time   : 2017/11/30
 *    desc   : 用数组写的顺序队列
 */
public class ArrayQueue {

    private String[] items;
    private int n = 0;
    private int head = 0;
    private int tail = 0;

    public ArrayQueue(int capacity){
        items = new String[capacity];
        n = capacity;
    }

    /**
     * 入栈，每次添加都是添加到数组的末尾
     * @param data
     * @return
     */
    public boolean enqueue(String data){
        if (tail == n) {
            return false;
        }
        items[tail] = data;
        ++tail;
        return false;
    }

    /**
     * 出栈，每次处数组的第一个
     * @return
     */
    public String dequeue(){
        // 如果head == tail 表示队列为空
        if (head == tail){
            return null;
        }
        String item = items[head];
        ++head;
        return item;
    }

    public void print(){
        for (int i=0 ; i<items.length ; i++){
            if (items[i]!=null){
                Log.d("print---",items[i]);
            }
        }
    }

}
