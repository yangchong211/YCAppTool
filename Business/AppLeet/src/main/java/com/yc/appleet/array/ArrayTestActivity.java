package com.yc.appleet.array;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yc.appleet.R;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.toolutils.AppLogUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ArrayTestActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private TextView tv8;
    private TextView tv9;
    private TextView tv10;
    private TextView tv11;
    private TextView tv12;

    @Override
    public int getContentView() {
        return R.layout.activity_leet_array;
    }

    @Override
    public void initView() {
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        tv4 = findViewById(R.id.tv_4);
        tv5 = findViewById(R.id.tv_5);
        tv6 = findViewById(R.id.tv_6);
        tv7 = findViewById(R.id.tv_7);
        tv8 = findViewById(R.id.tv_8);
        tv9 = findViewById(R.id.tv_9);
        tv10 = findViewById(R.id.tv_10);
        tv11 = findViewById(R.id.tv_11);
        tv12 = findViewById(R.id.tv_12);
    }

    @Override
    public void initListener() {
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v == tv1) {
            testArray1();
        } else if (v == tv2) {
            delete();
        } else if (v == tv3) {
            beerAndDrink();
        } else if (v == tv4) {
            singleNumber();
        } else if (v == tv5){
            int[] nums = {1,1,2};
            RemoveDuplicates removeDuplicates = new RemoveDuplicates();
            int i = removeDuplicates.removeDuplicates(nums);
            Log.d("","");
        }
    }

    //用类实现数组
    private void testArray1() {
        //创建自定义封装数组结构，数组大小为4
        MyArray array = new MyArray(4);
        //添加4个元素分别是1,2,3,4
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(4);
        //显示数组元素
        array.display();
        //根据下标为0的元素
        int i = array.get(0);
        AppLogUtils.d("索引0的值" + i);
        //删除4的元素
        array.delete(4);
        //将元素3修改为33
        array.modify(3, 33);
        array.display();
    }

    /**
     * 删除数组中重复内容
     */
    private void delete() {
        int[] nums1 = {0, 0, 1, 1, 1, 2, 2, 3, 3, 4};
        int[] nums2 = {1, 1, 2};
        RemoveDuplicates.removeDuplicates(nums1);
        RemoveDuplicates.removeDuplicates(nums2);
    }


    /**
     * 啤酒每罐2.3元，饮料每罐1.9元。小明买了若干啤酒和饮料，一共花了82.3元。
     * 我们还知道他买的啤酒比饮料的数量少，请你计算他买了几罐啤酒。
     */
    private void beerAndDrink() {
        int x = (int) (82.3 / 2.3);
        int y = (int) (82.3 / 1.9);
        AppLogUtils.d("打印值 "+x + " " + y);
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                // 钱刚好花光了，并且啤酒比饮料少
                if ((i * 2.3 + j * 1.9) == 82.3 && i<j) {
                    AppLogUtils.d("打印值"+"----啤酒买了" + i);
                    AppLogUtils.d("打印值"+"----饮料买了" + j);
                }
            }
        }
    }

    /**
     * 数组中只出现一次的数字
     */
    private void singleNumber(){

    }


    /**
     * 设计一个算法来计算你所能获取的最大利润。你可以尽可能地完成更多的交易（多次买卖一支股票）。
     *     输入: [7,1,5,3,6,4]
     *     输出: 7
     *     解释: 在第 2 天（股票价格 = 1）的时候买入，在第 3 天（股票价格 = 5）的时候卖出, 这笔交易所能获得
     *     利润 = 5-1 = 4 。
     *     随后，在第 4 天（股票价格 = 3）的时候买入，在第 5 天（股票价格 = 6）的时候卖出, 这笔交易所能获得
     *     利润 = 6-3 = 3 。
     */
    int maxProfit(int[] prices) {
        if (prices==null || prices.length==0){
            return 0;
        }
        int count = 0;
        int temp;
        for(int i = 1; i < prices.length; i++){
            temp = prices[i] - prices[i-1];
            if(temp > 0){
                count+=temp;
            }
        }
        return count;
    }

    /**
     * 输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有的奇数位于数组的前半部分，所有的偶数位于位于数组的后半部分，并保证奇数和奇数，偶数和偶数之间的相对位置不变。
     */
    public class Solution {
        public void reOrderArray(int [] array) {
            //如果数组长度等于0或者等于1，什么都不做直接返回
            if(array.length==0||array.length==1) {
                return;
            }
            //oddCount：保存奇数个数
            //oddBegin：奇数从数组头部开始添加
            int oddCount=0,oddBegin=0;
            //新建一个数组
            int[] newArray = new int[array.length];
            //计算出（数组中的奇数个数）开始添加元素
            for(int i=0;i<array.length;i++){
                if((array[i]&1)==1) {
                    oddCount++;
                }
            }
            for(int i=0;i<array.length;i++){
                //如果数为基数新数组从头开始添加元素
                //如果为偶数就从oddCount（数组中的奇数个数）开始添加元素
                if((array[i]&1)==1) {
                    newArray[oddBegin++]=array[i];
                } else {
                    newArray[oddCount++]=array[i];
                }
            }
            for(int i=0;i<array.length;i++){
                array[i]=newArray[i];
            }
        }
    }

    //给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
    public void test1(int[] arr){
        if (arr!=null && arr.length>0){
            HashMap<Integer, Integer> hashMap = new HashMap<>();
            for (int i=0 ; i<arr.length ; i++){
                int num = arr[i];
                if (hashMap.containsKey(arr[i])){
                    hashMap.put(num, 1);
                } else {
                    hashMap.put(num , hashMap.get(num) + 1);
                }


                Set<Integer> set = new HashSet<>();
                if (!set.remove(num)){
                    set.add(num);
                }
            }
            //遍历

            //排序
            int temp;
            for (int i=0 ; i<arr.length ; i++){
                for (int j=0 ; j<arr.length ; j++){
                    //相邻的两个数进行对比，小的往前靠，大的往后放
                    if (arr[j] > arr[j+1]){
                        //数据对调
                        temp = arr[j];
                        arr[j] = arr[j+1];
                        arr[j+1] = temp;
                    }
                }
            }
            //数据排好之后，还会进行对比，其实这步

        }
    }

    private void test(int[] arr){
        int temp;
        boolean flag;//是否交换的标志
        for (int i=0 ; i<arr.length ; i++){
            flag = false;
            for (int j=0 ; j<arr.length ; j++){
                //相邻的两个数进行对比，小的往前靠，大的往后放
                if (arr[j] > arr[j+1]){
                    //数据对调
                    temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                    flag = true;
                }
            }
            if (!flag){
                break;
            }
        }

    }}
