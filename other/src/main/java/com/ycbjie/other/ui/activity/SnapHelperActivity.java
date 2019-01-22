package com.ycbjie.other.ui.activity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yc.cn.ycbannerlib.snap.ScrollLinearHelper;
import com.yc.cn.ycbannerlib.snap.ScrollPageHelper;
import com.yc.cn.ycbannerlib.snap.ScrollSnapHelper;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.other.R;
import com.ycbjie.other.ui.adapter.MeBannerAdapter;

import java.util.ArrayList;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/2
 *     desc  : 妹子轮播图
 *     revise:
 *              关于SnapHelper封装库：https://github.com/yangchong211/YCBanner
 *              关于SnapHelper源码分析：https://www.jianshu.com/p/9b8e0696802d
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_OTHER_SNAPHELPER_ACTIVITY)
public class SnapHelperActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private RecyclerView mRecyclerView3;
    private RecyclerView mRecyclerView4;

    @Override
    public int getContentView() {
        return R.layout.activity_snap_helper_view;
    }

    @Override
    public void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView2 = findViewById(R.id.recyclerView2);
        mRecyclerView3 = findViewById(R.id.recyclerView3);
        mRecyclerView4 = findViewById(R.id.recyclerView4);
        initRecyclerView();
        initRecyclerView2();
        initRecyclerView3();
        initRecyclerView4();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        ScrollLinearHelper snapHelper = new ScrollLinearHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        MeBannerAdapter adapter = new MeBannerAdapter(this,true);
        mRecyclerView.setAdapter(adapter);
        adapter.setData(getData());
    }

    private void initRecyclerView2() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView2.setLayoutManager(manager);
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView2);
        MeBannerAdapter adapter = new MeBannerAdapter(this,true);
        mRecyclerView2.setAdapter(adapter);
        adapter.setData(getData());
    }


    private void initRecyclerView3() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView3.setLayoutManager(manager);
        ScrollPageHelper snapHelper = new ScrollPageHelper(Gravity.START,false);
        snapHelper.attachToRecyclerView(mRecyclerView3);
        MeBannerAdapter adapter = new MeBannerAdapter(this,true);
        mRecyclerView3.setAdapter(adapter);
        adapter.setData(getData());
    }


    private void initRecyclerView4() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView4.setLayoutManager(manager);
        ScrollSnapHelper snapHelper = new ScrollSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView4);
        MeBannerAdapter adapter = new MeBannerAdapter(this,true);
        mRecyclerView4.setAdapter(adapter);
        adapter.setData(getData());
    }




    private ArrayList<Integer> getData(){
        ArrayList<Integer> data = new ArrayList<>();
        TypedArray bannerImage = getResources().obtainTypedArray(R.array.image_girls);
        for (int i = 0; i < 12 ; i++) {
            int image = bannerImage.getResourceId(i, R.drawable.girl2);
            data.add(image);
        }
        bannerImage.recycle();

        return data;
    }



}
