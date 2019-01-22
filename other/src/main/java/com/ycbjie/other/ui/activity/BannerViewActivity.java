package com.ycbjie.other.ui.activity;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.ns.yc.ycutilslib.blurView.blur.CustomBlur;
import com.yc.cn.ycbannerlib.gallery.GalleryLayoutManager;
import com.yc.cn.ycbannerlib.gallery.GalleryLinearSnapHelper;
import com.yc.cn.ycbannerlib.gallery.GalleryRecyclerView;
import com.yc.cn.ycbannerlib.gallery.GalleryScaleTransformer;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.other.R;
import com.ycbjie.other.ui.adapter.MeBannerAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/2
 *     desc  : 妹子轮播图
 *     revise: https://github.com/yangchong211/YCBanner
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_OTHER_BANNER_LIST_ACTIVITY)
public class BannerViewActivity extends BaseActivity {

    private GalleryRecyclerView mRecyclerView;
    private GalleryRecyclerView recyclerView2;
    private RecyclerView recyclerView3;
    private FrameLayout fl_container;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerView.release();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_banner_list_view;
    }

    @Override
    public void initView() {
        StateAppBar.translucentStatusBar(this,true);
        mRecyclerView = findViewById(R.id.recyclerView);
        fl_container = findViewById(R.id.fl_container);
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView3 = findViewById(R.id.recyclerView3);
        initRecyclerView();
        initRecyclerView2();
        initRecyclerView3();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }


    private void initRecyclerView() {
        MeBannerAdapter adapter = new MeBannerAdapter(this,false);
        adapter.setData(getData());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setFlingSpeed(15000);
        mRecyclerView.setOnItemSelectedListener(new GalleryRecyclerView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(RecyclerView recyclerView, View item, int position) {
                LogUtils.e("onItemSelected-----", position + "");
                //设置高斯模糊背景
                setBlurImage(true);
            }
        });
        GalleryLayoutManager manager = new GalleryLayoutManager(this, LinearLayoutManager.HORIZONTAL);
        //attach，绑定recyclerView，并且设置默认选中索引的位置
        manager.attach(100);
        //设置缩放比例因子，在0到1.0之间即可
        manager.setItemTransformer(new GalleryScaleTransformer( 0.2f,30));
        mRecyclerView.setLayoutManager(manager);
        GalleryLinearSnapHelper mSnapHelper = new GalleryLinearSnapHelper(mRecyclerView);
        mSnapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.onStart();
    }

    private void initRecyclerView2() {
        MeBannerAdapter adapter = new MeBannerAdapter(this,false);
        adapter.setData(getData());
        recyclerView2.setAdapter(adapter);
        recyclerView2.setFlingSpeed(8000);
        GalleryLayoutManager manager = new GalleryLayoutManager(this, LinearLayoutManager.HORIZONTAL);
        //attach，绑定recyclerView，并且设置默认选中索引的位置
        manager.attach(100);
        //设置缩放比例因子，在0到1.0之间即可
        manager.setItemTransformer(new GalleryScaleTransformer( 0.0f,30));
        recyclerView2.setLayoutManager(manager);
        GalleryLinearSnapHelper mSnapHelper = new GalleryLinearSnapHelper(recyclerView2);
        mSnapHelper.attachToRecyclerView(recyclerView2);
        recyclerView2.onStart();
    }


    private void initRecyclerView3() {
        GalleryLayoutManager manager = new GalleryLayoutManager(this, LinearLayoutManager.HORIZONTAL);
        manager.attach(recyclerView3,100);
        manager.setItemTransformer(new GalleryScaleTransformer( 0.0f,30));
        recyclerView3.setLayoutManager(manager);
        MeBannerAdapter adapter = new MeBannerAdapter(this,false);
        adapter.setData(getData());
        recyclerView3.setAdapter(adapter);
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


    /**
     * 获取虚化背景的位置
     */
    private int mLastDraPosition = -1;
    private Map<String, Drawable> mTSDraCacheMap = new HashMap<>();
    private static final String KEY_PRE_DRAW = "key_pre_draw";
    /**
     * 设置背景高斯模糊
     */
    public void setBlurImage(boolean forceUpdate) {
        final MeBannerAdapter adapter = (MeBannerAdapter) mRecyclerView.getAdapter();
        final int mCurViewPosition = mRecyclerView.getCurrentItem();

        boolean isSamePosAndNotUpdate = (mCurViewPosition == mLastDraPosition) && !forceUpdate;

        if (adapter == null || mRecyclerView == null || isSamePosAndNotUpdate) {
            return;
        }
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                // 获取当前位置的图片资源ID
                int resourceId = adapter.getData().get(mCurViewPosition%adapter.getData().size());
                // 将该资源图片转为Bitmap
                Bitmap resBmp = BitmapFactory.decodeResource(getResources(), resourceId);
                // 将该Bitmap高斯模糊后返回到resBlurBmp
                Bitmap resBlurBmp = CustomBlur.apply(mRecyclerView.getContext(), resBmp, 10);
                // 再将resBlurBmp转为Drawable
                Drawable resBlurDrawable = new BitmapDrawable(resBlurBmp);
                // 获取前一页的Drawable
                Drawable preBlurDrawable = mTSDraCacheMap.get(KEY_PRE_DRAW) == null ? resBlurDrawable : mTSDraCacheMap.get(KEY_PRE_DRAW);

                /* 以下为淡入淡出效果 */
                Drawable[] drawableArr = {preBlurDrawable, resBlurDrawable};
                TransitionDrawable transitionDrawable = new TransitionDrawable(drawableArr);
                fl_container.setBackgroundDrawable(transitionDrawable);
                transitionDrawable.startTransition(500);

                // 存入到cache中
                mTSDraCacheMap.put(KEY_PRE_DRAW, resBlurDrawable);
                // 记录上一次高斯模糊的位置
                mLastDraPosition = mCurViewPosition;
            }
        });
    }



}
