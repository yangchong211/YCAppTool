package com.pedaily.yc.ycdialoglib.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.View;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/9
 *     desc  : 自定义布局弹窗DialogFragment，从底部弹出
 *     revise: 1月10日
 * </pre>
 */
@SuppressLint("ValidFragment")
public class BottomDialogFragment extends BaseDialogFragment {

    private static final String KEY_LAYOUT_RES = "bottom_layout_res";
    private static final String KEY_HEIGHT = "bottom_height";
    private static final String KEY_DIM = "bottom_dim";
    private static final String KEY_CANCEL_OUTSIDE = "bottom_cancel_outside";

    private FragmentManager mFragmentManager;
    private boolean mIsCancelOutside = false;
    private String mTag = super.getFragmentTag();
    private float mDimAmount = super.getDimAmount();
    private int mHeight = super.getHeight();

    @LayoutRes
    private int mLayoutRes;
    private ViewListener mViewListener;

    @SuppressLint("ValidFragment")
    public BottomDialogFragment(Local local){
        setLocal(local);
    }

    public static BottomDialogFragment create(FragmentManager manager) {
        BottomDialogFragment dialog = new BottomDialogFragment(Local.LEFT);
        dialog.setFragmentManager(manager);
        return dialog;
    }

    /**
     * 异常状态重启的话取出状态
     * @param savedInstanceState                    bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //setLocal(Local.LEFT);
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mLayoutRes = savedInstanceState.getInt(KEY_LAYOUT_RES);
            mHeight = savedInstanceState.getInt(KEY_HEIGHT);
            mDimAmount = savedInstanceState.getFloat(KEY_DIM);
            mIsCancelOutside = savedInstanceState.getBoolean(KEY_CANCEL_OUTSIDE);
        }
    }

    /**
     * 异常状态下保存重要信息
     * @param outState                              bundle
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KEY_LAYOUT_RES, mLayoutRes);
        outState.putInt(KEY_HEIGHT, mHeight);
        outState.putFloat(KEY_DIM, mDimAmount);
        outState.putBoolean(KEY_CANCEL_OUTSIDE, mIsCancelOutside);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void bindView(View v) {
        if (mViewListener != null) {
            mViewListener.bindView(v);
        }
    }

    /**
     * 是否支持点击弹窗外部取消
     * @return                          默认可以取消
     */
    @Override
    protected boolean isCancel() {
        return mIsCancelOutside;
    }

    @Override
    public int getLayoutRes() {
        return mLayoutRes;
    }

    /**
     * 设置manager
     * @param manager                   manager
     * @return
     */
    public BottomDialogFragment setFragmentManager(FragmentManager manager) {
        mFragmentManager = manager;
        return this;
    }

    /**
     * 绑定视图
     * @param listener                  listener
     * @return
     */
    public BottomDialogFragment setViewListener(ViewListener listener) {
        mViewListener = listener;
        return this;
    }

    /**
     * 设置弹窗布局
     * @param layoutRes                 layout
     * @return
     */
    public BottomDialogFragment setLayoutRes(@LayoutRes int layoutRes) {
        mLayoutRes = layoutRes;
        return this;
    }

    /**
     * 设置点击弹窗外部是否可以关闭弹窗
     * @param cancel                    是否取消
     * @return
     */
    public BottomDialogFragment setCancelOutside(boolean cancel) {
        mIsCancelOutside = cancel;
        return this;
    }

    /**
     * 设置tag
     * @param tag                       tag
     * @return
     */
    public BottomDialogFragment setTag(String tag) {
        mTag = tag;
        return this;
    }

    /**
     * 设置弹窗背景色透明度
     * @param dim                       0到1
     * @return
     */
    public BottomDialogFragment setDimAmount(float dim) {
        mDimAmount = dim;
        return this;
    }

    /**
     * 这个高度可以自己设置
     * @param heightPx                  高度
     * @return
     */
    public BottomDialogFragment setHeight(int heightPx) {
        mHeight = heightPx;
        return this;
    }

    @Override
    public float getDimAmount() {
        return mDimAmount;
    }

    @Override
    public int getHeight() {
        return mHeight;
    }

    @Override
    public String getFragmentTag() {
        return mTag;
    }


    public BaseDialogFragment show() {
        show(mFragmentManager);
        return this;
    }

    public void dismissDialogFragment(){
        dismissDialog();
    }

    public interface ViewListener {
        /**
         * 绑定
         * @param v     view
         */
        void bindView(View v);
    }

}
