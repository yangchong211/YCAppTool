package com.yc.appleet.code;

import android.view.View;
import android.widget.TextView;

import com.yc.appleet.R;
import com.yc.library.base.mvp.BaseActivity;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/30
 *     desc  : 算法导论
 *     revise:
 * </pre>
 */
public class CodeTestActivity extends BaseActivity implements View.OnClickListener {

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
        return R.layout.activity_leet_code;
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
        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        if (v == tv1) {
            cla(100);
        } else if (v == tv2) {
            cal(100);
        } else if (v == tv3) {
            cal2(100);
        } else if (v == tv4) {
            cal3(100);
        } else if (v == tv5){
            cal4(100);
        } else if (v == tv6){
            print(100);
        }
    }

    /**
     * 算法的执行效率，粗略的讲，就是算法的执行时间
     * 尽管每行代码对应cpu执行的个数、执行的时间都不一样，但是我们这里只是粗略的估计，所以我们可以假设每行代码执行的时间都一样，都为unit_time。在这个假设的基础上，我们进行分析！
     * 第一行，第二行执行一次，2 * unit_time
     * 第三行，第四行执行n次，2 * n * unit_time
     *
     * 代码时间复杂度：2*unit_time + 2n*unit_time
     *
     * T(n) = (2+2n)*unit_time
     *
     * 当 n 很大时，公式中的低阶、常量、系数三部分并不左右增长趋势，所以都可以忽略
     */
    private int cla(int n){
        int sum = 0;
        for (int i=1 ;i<=n; i++){
            sum = sum + 1;
        }
        return sum;
    }

    /**
     * 第二，三，四行执行一次，3 * unit_time
     * 第五，六行执行n次，2n * unit_time
     * 第七，八行执行n^2次，2n^2 * unit_time
     *
     * 代码时间复杂度：3*unit_time + 2n*unit_time + 2n^2*unit_time
     *
     * T(n) = (2n^2+2n+3)*unit_time
     *
     * 当 n 很大时，公式中的低阶、常量、系数三部分并不左右增长趋势，所以都可以忽略
     */
    private int cal(int n){
        int sum = 0;
        int i = 1;
        int j = 1;
        for( ; i<= n ;i++){
            j = 1;
            for( ; j <= n ; j++){
                sum = sum + i*j;
            }
        }
        return sum;
    }

    /**
     * 只关注循环执行次数最多的一段代码
     * 时间复杂度：O(n)
     */
    private int cal2(int n) {
        int sum = 0;
        int i = 1;
        for (; i <= n; ++i) {
            sum = sum + i;
        }
        return sum;
    }

    /**
     * 加法法则计算时间复杂度
     * 时间复杂度：O(2n) + O(n^2)
     */
    private int cal3(int n) {
        int sum_1 = 0;
        int p = 1;
        //O(n)
        for (; p < 100; ++p) {
            sum_1 = sum_1 + p;
        }
        int sum_2 = 0;
        int q = 1;
        //O(n)
        for (; q < n; ++q) {
            sum_2 = sum_2 + q;
        }
        int sum_3 = 0;
        int i = 1;
        int j = 1;
        //O(n^2)
        for (; i <= n; ++i) {
            j = 1;
            for (; j <= n; ++j) {
                sum_3 = sum_3 + i * j;
            }
        }
        return sum_1 + sum_2 + sum_3;
    }

    /**
     * 乘法法则计算时间复杂度
     * 时间复杂度：O(n*n)
     */
    private int cal4(int n) {
        int ret = 0;
        int i = 1;
        for (; i < n; ++i) {
            ret = ret + f(i);
        }
        return ret;
    }

    private int f(int n) {
        int sum = 0;
        int i = 1;
        for (; i < n; ++i) {
            sum = sum + i;
        }
        return sum;
    }

    /**
     * 空间复杂度，相比而言，就是对容易中装载对象所占空间分析
     */
    private void print(int n) {
        int i = 0;
        int[] a = new int[n];
        for (; i <n; ++i) {
            a[i] = i * i;
        }
        for (i = n-1; i >= 0; --i) {
            System.out.println(a[i]);
        }
    }
}
