package com.yc.leetbusiness.array;

import android.view.View;
import android.widget.TextView;

import com.yc.leetbusiness.R;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.logging.LoggerService;
import com.yc.logging.logger.Logger;

import java.util.Arrays;

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

}
