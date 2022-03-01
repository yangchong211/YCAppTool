package com.yc.leetbusiness.array;

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
//        ArrayList<String> listStr = new ArrayList<>();
//        for(int i = 0 ; i < 3 ;i++){
//            listStr.add(i+"");
//        }
//        //clone一次
//        ArrayList<String> listStrCopy = (ArrayList<String>) listStr.clone();
//        //修改clone后对象的值
//        listStrCopy.remove(2);
//        listStrCopy.add(100+"");
//        for (int i = 0; i < listStr.size(); i++) {
//            logger.debug("打印值 原始数据："+ listStr.get(i).toString());
//            logger.debug("打印值 clone数据："+ listStrCopy.get(i).toString());
//        }

        // 原始对象
        Student stud = new Student("杨充", "潇湘剑雨");
        System.out.println("原始对象: " + stud.getName() + " - " + stud.getSubj().getName());

        // 拷贝对象
        Student clonedStud = (Student) stud.clone();
        System.out.println("拷贝对象: " + clonedStud.getName() + " - " + clonedStud.getSubj().getName());

        // 原始对象和拷贝对象是否一样：
        System.out.println("原始对象和拷贝对象是否一样: " + (stud == clonedStud));
        // 原始对象和拷贝对象的name属性是否一样
        System.out.println("原始对象和拷贝对象的name属性是否一样: " + (stud.getName() == clonedStud.getName()));
        // 原始对象和拷贝对象的subj属性是否一样
        System.out.println("原始对象和拷贝对象的subj属性是否一样: " + (stud.getSubj() == clonedStud.getSubj()));

        //修改原始对象
        stud.setName("小杨逗比");
        stud.getSubj().setName("潇湘剑雨大侠");
        System.out.println("更新后的原始对象: " + stud.getName() + " - " + stud.getSubj().getName());
        System.out.println("更新原始对象后的克隆对象: " + clonedStud.getName() + " - " + clonedStud.getSubj().getName());

        //修改拷贝对象
//        clonedStud.setName("小杨逗比");
//        clonedStud.getSubj().setName("潇湘剑雨大侠");
//        System.out.println("更新后的原始对象: " + stud.getName() + " - " + stud.getSubj().getName());
//        System.out.println("更新原始对象后的克隆对象: " + clonedStud.getName() + " - " + clonedStud.getSubj().getName());
    }


    public class Subject implements Cloneable{

        private String name;
        public Subject(String s) {
            name = s;
        }

        public String getName() {
            return name;
        }

        public void setName(String s) {
            name = s;
        }

        /**
         *  重写clone()方法
         * @return
         */
        public Object clone() {
            //浅拷贝
            try {
                // 直接调用父类的clone()方法
                return super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }

    public class Student implements Cloneable {

        // 对象引用
        private Subject subj;
        private String name;

        public Student(String s, String sub) {
            name = s;
            subj = new Subject(sub);
        }

        public Subject getSubj() {
            return subj;
        }

        public String getName() {
            return name;
        }

        public void setName(String s) {
            name = s;
        }

        /**
         *  重写clone()方法
         * @return
         */
        public Object clone() {
            //浅拷贝
            try {
                // 直接调用父类的clone()方法
                return super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }

}
