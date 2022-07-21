package com.yc.appleet;

import java.util.ArrayList;

public class TestCode {

    private int[] array ;
    private int elems;
    private int length;

    public TestCode(){
        elems = 0;
        length = 50;
        array = new int[50];
    }

    public int getSize(){
        return array.length;
    }

    public int getMaxSize(){
        return length;
    }

    public boolean add(int value){
        if (elems == length){
            //加不进来了
            return false;
        } else {
            array[elems] = value;
            elems++;
            return true;
        }
    }

    public int find(int searchValue){
        int i=0;
        for (; i<array.length;i++){
            if (array[i] == searchValue){
                break;
            }
        }
        if (i == elems) {
            return -1;
        }
        return i;
    }

    public boolean remove(int value){
        int i = find(value);
        if (i == -1){
            return false;
        } else {
            if (i == elems-1){
                elems--;
            } else {
                //数组挨着偏移
                elems--;
            }
        }
        //删除元素
        return false;
    }
}
