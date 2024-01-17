package com.yc.appleet.stack;

public class ArrayStack {

    //数组
    private int[] arr;
    //元素个数
    private int size = 0;
    //栈大小
    private int n;

    public ArrayStack(int n){
        arr = new int[n];
        this.n = n;
        size = 0;
    }

    public boolean push(int data){
        if (size>=n){
            //空间不够
            return false;
        }
        arr[n] = data;
        ++n;
        return false;
    }

    public int pop(){
        if (n == 0){
            return -100;
        }
        // 返回下标为count-1的数组元素，并且栈中元素个数count减一
        int tmp = arr[n-1];
        --n;
        return tmp;
    }

    public int size(){
        return size;
    }

}
