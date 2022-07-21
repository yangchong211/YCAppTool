package com.yc.appleet.array;


import com.yc.toolutils.AppLogUtils;

/// 题目，使用类实现简单数组
public class MyArray {

    private static final String TAG = "MyArray: ";

    //定义一个数组
    private final int[] intArray;
    //定义数组的实际有效长度
    private int elems;
    //定义数组的最大长度
    private final int length;

    //默认构造一个长度为50的数组
    public MyArray() {
        elems = 0;
        length = 50;
        intArray = new int[length];
    }

    //构造函数，初始化一个长度为length 的数组
    public MyArray(int length) {
        elems = 0;
        this.length = length;
        intArray = new int[length];
    }

    //获取数组的有效长度
    public int getSize() {
        return elems;
    }

    /**
     * 遍历显示元素
     */
    public void display() {
        for (int i = 0; i < elems; i++) {
            AppLogUtils.d(TAG+intArray[i] + " ");
        }
        AppLogUtils.d(TAG+"display");
    }

    /**
     * 添加元素
     *
     * @param value 假设操作人是不会添加重复元素的，如果有重复元素对于后面的操作都会有影响。
     * @return 添加成功返回true,添加的元素超过范围了返回false
     */
    public boolean add(int value) {
        if (elems == length) {
            return false;
        } else {
            intArray[elems] = value;
            elems++;
        }
        return true;
    }

    public boolean add(int index , int value){
        if (elems == index){
            //数据满了
            return false;
        }
        int currentV = intArray[index-1];
        for (int i=0 ; i<intArray.length ; i++){
            if (index>=i){

            }
        }
        return true;
    }

    /**
     * 根据下标获取元素
     *
     * @param i 索引
     * @return 查找下标值在数组下标有效范围内，返回下标所表示的元素 查找下标超出数组下标有效值，提示访问下标越界
     */
    public int get(int i) {
        if (i < 0 || i > elems) {
            throw new IndexOutOfBoundsException("访问下标越界");
        }
        int data = intArray[i];
        AppLogUtils.d(TAG+data);
        return data;
    }

    /**
     * 查找元素
     * 数组中查找元素，时间复杂度是：O(1)，O(n)
     * @param searchValue
     * @return 查找的元素如果存在则返回下标值，如果不存在，返回 -1
     */
    public int find(int searchValue) {
        int i;
        for (i = 0; i < elems; i++) {
            if (intArray[i] == searchValue) {
                break;
            }
        }
        if (i == elems) {
            return -1;
        }
        return i;
    }

    /**
     * 删除元素
     *
     * @param value
     * @return 如果要删除的值不存在，直接返回 false;否则返回true，删除成功
     */
    public boolean delete(int value) {
        int k = find(value);
        if (k == -1) {
            AppLogUtils.d(TAG+"删除失败，找不到");
            return false;
        } else {
            if (k == elems - 1) {
                elems--;
            } else {
                for (int i = k; i < elems - 1; i++) {
                    intArray[i] = intArray[i + 1];
                }
                elems--;
            }
            AppLogUtils.d(TAG+"删除成功"+value);
            return true;
        }
    }

    /**
     * 修改数据
     *
     * @param oldValue 原值
     * @param newValue 新值
     * @return 修改成功返回true，修改失败返回false
     */
    public boolean modify(int oldValue, int newValue) {
        int i = find(oldValue);
        if (i == -1) {
            throw new IndexOutOfBoundsException("需要修改的数据不存在");
        } else {
            intArray[i] = newValue;
            AppLogUtils.d(TAG+"修改成功"+intArray[i]);
            return true;
        }
    }

}
