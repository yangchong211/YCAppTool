package com.yc.leetbusiness.array;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yc.leetbusiness.R;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.logging.LoggerService;
import com.yc.logging.logger.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

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
    private final Logger logger = LoggerService.getInstance().getLogger("ArrayTestActivity");

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
        logger.debug("索引0的值" + i);
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
        removeData(nums1);
        removeData(nums2);
    }

    private int removeData(int[] num) {
        if (num == null || num.length == 0) {
            return 0;
        }
        int currentV = num[0];
        int count = 0;
        for (int i = 1; i < num.length; i++) {
            if (num[i] != currentV) {
                count++;
                currentV = num[i];
                num[count] = currentV;
            }
        }
        logger.debug("数据数组数量" + count + "  " + Arrays.toString(num));
        return count + 1;
    }

    /**
     * 啤酒每罐2.3元，饮料每罐1.9元。小明买了若干啤酒和饮料，一共花了82.3元。
     * 我们还知道他买的啤酒比饮料的数量少，请你计算他买了几罐啤酒。
     */
    private void beerAndDrink() {
        int x = (int) (82.3 / 2.3);
        int y = (int) (82.3 / 1.9);
        logger.debug("打印值 "+x + " " + y);
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                // 钱刚好花光了，并且啤酒比饮料少
                if ((i * 2.3 + j * 1.9) == 82.3 && i<j) {
                    logger.debug("打印值"+"----啤酒买了" + i);
                    logger.debug("打印值"+"----饮料买了" + j);
                }
            }
        }
    }

    /**
     * 数组中只出现一次的数字
     */
    private void singleNumber(){
        MyThread myThread = new MyThread();
        Thread t1 = new Thread(myThread , "张三") ;
        t1.start();
        String name = t1.getName();
        logger.debug("打印值"+"----"+name);



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

    private class MyThread implements Runnable{
        @Override
        public void run() {
            logger.debug("打印值"+"----"+this.toString());
        }
    }

}
