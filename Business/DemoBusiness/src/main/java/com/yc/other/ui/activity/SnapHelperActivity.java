package com.yc.other.ui.activity;

import android.content.res.TypedArray;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;


import com.yc.library.base.mvp.BaseActivity;
import com.yc.other.R;
import com.yc.other.ui.adapter.MeBannerAdapter;
import com.yc.snapbannerlib.ScrollLinearHelper;
import com.yc.snapbannerlib.ScrollPageHelper;
import com.yc.snapbannerlib.ScrollSnapHelper;

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
            int image = bannerImage.getResourceId(i, R.drawable.bg_autumn_tree_min);
            data.add(image);
        }
        bannerImage.recycle();

        return data;
    }



}
