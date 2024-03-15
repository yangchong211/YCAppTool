package com.yc.common.lru;

import android.view.View;

import com.yc.store.lru.lib.SystemLruCache;
import com.yc.common.R;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.roundcorner.view.RoundTextView;

import java.util.Map;
import java.util.Set;

public class LruActivity extends BaseActivity implements View.OnClickListener {

    private RoundTextView tvFile1;
    private RoundTextView tvFile2;
    private RoundTextView tvFile3;
    private RoundTextView tvFile4;
    private RoundTextView tvFile5;
    private RoundTextView tvFile6;
    private RoundTextView tvFile7;
    private SystemLruCache<String , String> lruCache = new SystemLruCache<>(100);

    @Override
    public int getContentView() {
        return R.layout.activity_base_test_view;
    }

    @Override
    public void initView() {
        tvFile1 = findViewById(R.id.tv_file_1);
        tvFile2 = findViewById(R.id.tv_file_2);
        tvFile3 = findViewById(R.id.tv_file_3);
        tvFile4 = findViewById(R.id.tv_file_4);
        tvFile5 = findViewById(R.id.tv_file_5);
        tvFile6 = findViewById(R.id.tv_file_6);
        tvFile7 = findViewById(R.id.tv_file_7);
    }

    @Override
    public void initListener() {
        tvFile1.setOnClickListener(this);
        tvFile2.setOnClickListener(this);
        tvFile3.setOnClickListener(this);
        tvFile4.setOnClickListener(this);
        tvFile5.setOnClickListener(this);
        tvFile6.setOnClickListener(this);
        tvFile7.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //获取数据
        lruCache.get("lru");
        //判断是否包含数据
        lruCache.containsKey("lru");
        //移除数据
        lruCache.remove("lru");
        //最大的长度
        int i = lruCache.maxSize();
        //拷贝一份数据
        Map<String, String> snapshot = lruCache.snapshot();
        //清除数据
        lruCache.clear();
        //获取健值对
        Set<String> keySet = lruCache.keySet();
    }

    @Override
    public void onClick(View v) {
        if (v == tvFile1) {
        } else if (v == tvFile2) {
        } else if (v == tvFile3) {
        } else if (v == tvFile4) {
        } else if (v == tvFile5) {

        } else if (v == tvFile6){
        } else if (v == tvFile7){

        }
    }


}
