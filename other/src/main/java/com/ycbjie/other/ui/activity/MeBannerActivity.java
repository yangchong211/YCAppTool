package com.ycbjie.other.ui.activity;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.ns.yc.ycutilslib.blurView.blur.CustomBlur;
import com.yc.cn.ycbannerlib.gallery.GalleryRecyclerView;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.other.R;
import com.ycbjie.other.ui.adapter.MeBannerAdapter;

import java.lang.ref.SoftReference;
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
@Route(path = ARouterConstant.ACTIVITY_OTHER_BANNER_ACTIVITY)
public class MeBannerActivity extends BaseActivity {

    private GalleryRecyclerView mRecyclerView;
    private LinearLayout mLlContainer;
    /**
     * 获取虚化背景的位置
     */
    private int mLastDraPosition = -1;
    private Map<String, Drawable> mTSDraCacheMap = new HashMap<>();
    private static final String KEY_PRE_DRAW = "key_pre_draw";
    private SoftReference<TransitionDrawable> bitmapSoftReference;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRecyclerView!=null){
            mRecyclerView.release();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mRecyclerView!=null){
            mRecyclerView.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.addOnScrollListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRecyclerView.removeOnScrollListener(listener);
        if (mRecyclerView!=null){
            mRecyclerView.onStop();
        }
    }


    @Override
    public int getContentView() {
        return R.layout.activity_banner_view;
    }

    @Override
    public void initView() {
        StateAppBar.translucentStatusBar(this,true);
        mRecyclerView = findViewById(R.id.recyclerView);
        mLlContainer = findViewById(R.id.ll_container);
        initRecyclerView();
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
        mRecyclerView.setDelayTime(3000)
                .setFlingSpeed(10000)
                .setDataAdapter(adapter)
                .setSelectedPosition(100)
                .setCallbackInFling(false)
                .setOnItemSelectedListener((recyclerView, item, position) -> {
                    //设置高斯模糊背景
                    setBlurImage(true);
                })
                .setSize(adapter.getData().size())
                .setUp();
    }


    /**
     * 报错提示：You cannot start a load for a destroyed activity
     * 原因的出处是因为我在滑动那里做的图片滑动时停止加载，停止时加载图片导致的
     * “You cannot start a load for a destroyed activity”,说白了就是activity在你按back键时候已经销毁了，
     * 而那个滚动事件的Glide图片处理事件还在执行。
     *
     * 方案1：尝试在每个Glide使用时候，this改成getApplicationContext()，依然crash！
     */
    private RecyclerView.OnScrollListener listener =  new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                Glide.with(getApplicationContext()).resumeRequests();
            }else {
                Glide.with(getApplicationContext()).pauseRequests();
            }
        }
    };


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
     * 设置背景高斯模糊
     */
    public void setBlurImage(boolean forceUpdate) {
        final MeBannerAdapter adapter = (MeBannerAdapter) mRecyclerView.getAdapter();
        final int mCurViewPosition = mRecyclerView.getCurrentItem();

        boolean isSamePosAndNotUpdate = (mCurViewPosition == mLastDraPosition) && !forceUpdate;

        if (adapter == null || mRecyclerView == null || isSamePosAndNotUpdate) {
            return;
        }

        mRecyclerView.post(() -> {
            // 获取当前位置的图片资源ID
            int resourceId = adapter.getData().get(mCurViewPosition%adapter.getData().size());
            // 将该资源图片转为Bitmap
            Bitmap resBmp = BitmapFactory.decodeResource(getResources(), resourceId);
            // 将该Bitmap高斯模糊后返回到resBlurBmp
            Bitmap resBlurBmp = CustomBlur.apply(mRecyclerView.getContext(), resBmp, 10);
            // 再将resBlurBmp转为Drawable
            Drawable resBlurDrawable = new BitmapDrawable(resBlurBmp);
            // 获取前一页的Drawable
            Drawable preBlurDrawable = mTSDraCacheMap.get(KEY_PRE_DRAW) == null ?
                    resBlurDrawable : mTSDraCacheMap.get(KEY_PRE_DRAW);

            /* 以下为淡入淡出效果 */
            Drawable[] drawableArr = {preBlurDrawable, resBlurDrawable};
            TransitionDrawable transitionDrawable = new TransitionDrawable(drawableArr);
            //正常是用来处理图片这种占用内存大的情况
            bitmapSoftReference = new SoftReference<>(transitionDrawable);
            if(bitmapSoftReference.get() != null) {
                mLlContainer.setBackgroundDrawable(bitmapSoftReference.get());
            }
            transitionDrawable.startTransition(500);
            // 存入到cache中
            mTSDraCacheMap.put(KEY_PRE_DRAW, resBlurDrawable);
            // 记录上一次高斯模糊的位置
            mLastDraPosition = mCurViewPosition;
        });
    }



}
