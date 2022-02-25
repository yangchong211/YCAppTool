/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.yc.ycstatelib;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/7/6
 *     desc  : 自定义帧布局
 *     revise:
 * </pre>
 */
public class StateFrameLayout extends FrameLayout {

    /**
     *  loading 加载id
     */
    public static final int LAYOUT_LOADING_ID = 1;

    /**
     *  内容id
     */
    public static final int LAYOUT_CONTENT_ID = 2;

    /**
     *  异常id
     */
    public static final int LAYOUT_ERROR_ID = 3;

    /**
     *  网络异常id
     */
    public static final int LAYOUT_NETWORK_ERROR_ID = 4;

    /**
     *  空数据id
     */
    public static final int LAYOUT_EMPTY_DATA_ID = 5;

    /**
     *  存放布局集合
     */
    private SparseArray<View> layoutSparseArray = new SparseArray<>();

    //private HashMap<Integer,View> map = new HashMap<>();

    /**
     *  布局管理器
     */
    private StateLayoutManager mStatusLayoutManager;


    public StateFrameLayout(Context context) {
        super(context);
    }

    public StateFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StateFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置状态管理者manager
     * 该manager主要的操作是设置不同状态view以及设置属性
     * 而该帧布局主要操作是显示和隐藏视图，用帧布局可以较少一层视图层级
     * 这样操作是利用了面向对象中封装类尽量保持类的单一职责，一个类尽量只做一件事情
     * @param statusLayoutManager               manager
     */
    public void setStatusLayoutManager(StateLayoutManager statusLayoutManager) {
        mStatusLayoutManager = statusLayoutManager;
        //添加所有的布局到帧布局
        addAllLayoutToRootLayout();
    }

    /**
     * 添加所有不同状态布局到帧布局中
     */
    private void addAllLayoutToRootLayout() {
        //将内容视图添加到布局中
        if (mStatusLayoutManager.contentLayoutResId != 0) {
            addLayoutResId(mStatusLayoutManager.contentLayoutResId, StateFrameLayout.LAYOUT_CONTENT_ID);
        }
        //将加载loading视图添加到布局中
        if (mStatusLayoutManager.loadingLayoutResId != 0) {
            addLayoutResId(mStatusLayoutManager.loadingLayoutResId, StateFrameLayout.LAYOUT_LOADING_ID);
        }
        //将空数据视图添加到布局中，注意这里是添加ViewStub，使用的时候才inflate
        if (mStatusLayoutManager.emptyDataVs != null) {
            addView(mStatusLayoutManager.emptyDataVs);
        }
        //将加载异常视图添加到布局中，注意这里是添加ViewStub，使用的时候才inflate
        if (mStatusLayoutManager.errorVs != null) {
            addView(mStatusLayoutManager.errorVs);
        }
        //将网络异常视图添加到布局中，注意这里是添加ViewStub，使用的时候才inflate
        if (mStatusLayoutManager.netWorkErrorVs != null) {
            addView(mStatusLayoutManager.netWorkErrorVs);
        }
    }

    private void addLayoutResId(@LayoutRes int layoutResId, int id) {
        View resView = LayoutInflater.from(mStatusLayoutManager.context).inflate(layoutResId, null);
        if (id == StateFrameLayout.LAYOUT_LOADING_ID){
            //如果是loading，则设置不可点击
            resView.setOnClickListener(null);
        }
        layoutSparseArray.put(id, resView);
        addView(resView);
    }

    /**
     * 关闭showLoading
     * @return
     */
    public boolean goneLoading(){
        if (layoutSparseArray.get(LAYOUT_LOADING_ID) != null) {
            View view = layoutSparseArray.get(LAYOUT_LOADING_ID);
            if (view.getVisibility() == View.VISIBLE){
                view.setVisibility(View.GONE);
                return true;
            }else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 判断是否正在loading中
     * @return                      true 表示loading正在加载中
     */
    public boolean isLoading(){
        View view = layoutSparseArray.get(LAYOUT_LOADING_ID);
        if (view != null) {
            if (view.getVisibility() == View.VISIBLE){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     *  显示loading
     */
    public void showLoading() {
        if (layoutSparseArray.get(LAYOUT_LOADING_ID) != null) {
            showHideViewById(LAYOUT_LOADING_ID);
        }
    }

    /**
     *  显示内容
     */
    public void showContent() {
        if (layoutSparseArray.get(LAYOUT_CONTENT_ID) != null) {
            showHideViewById(LAYOUT_CONTENT_ID);
        }
    }

    /**
     *  显示空数据
     */
    public void showEmptyData(int iconImage, String textTip) {
        if (inflateLayout(LAYOUT_EMPTY_DATA_ID)) {
            showHideViewById(LAYOUT_EMPTY_DATA_ID);
            emptyDataViewAddData(iconImage, textTip);
        }
    }

    /**
     *  显示网络异常
     */
    public void showNetWorkError() {
        if (inflateLayout(LAYOUT_NETWORK_ERROR_ID)) {
            showHideViewById(LAYOUT_NETWORK_ERROR_ID);
        }
    }

    /**
     *  显示异常
     */
    public void showError(int iconImage, String textTip) {
        if (inflateLayout(LAYOUT_ERROR_ID)) {
            showHideViewById(LAYOUT_ERROR_ID);
            errorViewAddData(iconImage, textTip);
        }
    }

    /**
     * 空数据并且设置页面简单数据
     * @param iconImage                 空页面图片
     * @param textTip                   文字
     */
    private void emptyDataViewAddData(int iconImage, String textTip) {
        if (iconImage == 0 && TextUtils.isEmpty(textTip)) {
            return;
        }
        View emptyDataView = layoutSparseArray.get(LAYOUT_EMPTY_DATA_ID);
        View iconImageView = emptyDataView.findViewById(mStatusLayoutManager.emptyDataIconImageId);
        View textView = emptyDataView.findViewById(mStatusLayoutManager.emptyDataTextTipId);
        if (iconImageView != null && iconImageView instanceof ImageView) {
            ((ImageView) iconImageView).setImageResource(iconImage);
        }
        if (textView != null && textView instanceof TextView) {
            ((TextView) textView).setText(textTip);
        }
    }

    /**
     * 展示空页面
     * @param objects                   object
     */
    public void showLayoutEmptyData(Object... objects) {
        if (inflateLayout(LAYOUT_EMPTY_DATA_ID)) {
            showHideViewById(LAYOUT_EMPTY_DATA_ID);

            AbsViewStubLayout emptyDataLayout = mStatusLayoutManager.emptyDataLayout;
            if (emptyDataLayout != null) {
                emptyDataLayout.setData(objects);
            }
        }
    }

    /**
     * 展示加载异常页面
     * @param iconImage                 image图片
     * @param textTip                   文案
     */
    private void errorViewAddData(int iconImage, String textTip) {
        if (iconImage == 0 && TextUtils.isEmpty(textTip)) {
            return;
        }
        View errorView = layoutSparseArray.get(LAYOUT_ERROR_ID);
        View iconImageView = errorView.findViewById(mStatusLayoutManager.errorIconImageId);
        View textView = errorView.findViewById(mStatusLayoutManager.errorTextTipId);
        if (iconImageView != null && iconImageView instanceof ImageView) {
            ((ImageView) iconImageView).setImageResource(iconImage);
        }
        if (textView != null && textView instanceof TextView) {
            ((TextView) textView).setText(textTip);
        }
    }

    /**
     * 展示错误
     * @param objects
     */
    public void showLayoutError(Object... objects) {
        if (inflateLayout(LAYOUT_ERROR_ID)) {
            showHideViewById(LAYOUT_ERROR_ID);

            AbsViewStubLayout errorLayout = mStatusLayoutManager.errorLayout;
            if (errorLayout != null) {
                errorLayout.setData(objects);
            }
        }
    }

    /**
     * 根据ID显示隐藏布局
     * @param id                    id值
     */
    private void showHideViewById(int id) {
        //这个需要遍历集合中数据，然后切换显示和隐藏
        for (int i = 0; i < layoutSparseArray.size(); i++) {
            int key = layoutSparseArray.keyAt(i);
            View valueView = layoutSparseArray.valueAt(i);
            //显示该view
            if(key == id) {
                //显示该视图
                valueView.setVisibility(View.VISIBLE);
                if(mStatusLayoutManager.onShowHideViewListener != null) {
                    mStatusLayoutManager.onShowHideViewListener.onShowView(valueView, key);
                }
            } else {
                //隐藏该视图
                if(valueView.getVisibility() != View.GONE) {
                    valueView.setVisibility(View.GONE);
                    if(mStatusLayoutManager.onShowHideViewListener != null) {
                        mStatusLayoutManager.onShowHideViewListener.onHideView(valueView, key);
                    }
                }
            }
        }
    }


    /**
     * 主要是显示ViewStub布局，比如网络异常，加载异常以及空数据等页面
     * 注意该方法中只有当切换到这些页面的时候，才会将ViewStub视图给inflate出来，之后才会走视图绘制的三大流程
     * 方法里面通过id判断来执行不同的代码，首先判断ViewStub是否为空，如果为空就代表没有添加这个View就返回false，
     * 不为空就加载View并且添加到集合当中，然后调用showHideViewById方法显示隐藏View，
     * retryLoad方法是给重试按钮添加事件
     * @param id                        布局id
     * @return                          是否inflate出视图
     */
    private boolean inflateLayout(int id) {
        boolean isShow = true;
        if (layoutSparseArray.get(id) != null) {
            return isShow;
        }
        switch (id) {
                //网络异常
            case LAYOUT_NETWORK_ERROR_ID:
                if (mStatusLayoutManager.netWorkErrorVs != null) {
                    //只有当展示的时候，才将ViewStub视图给inflate出来
                    View view = mStatusLayoutManager.netWorkErrorVs.inflate();
                    view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mStatusLayoutManager.onNetworkListener.onNetwork();
                        }
                    });
                    layoutSparseArray.put(id, view);
                    isShow = true;
                } else {
                    isShow = false;
                }
                break;
                //加载异常
            case LAYOUT_ERROR_ID:
                if (mStatusLayoutManager.errorVs != null) {
                    //只有当展示的时候，才将ViewStub视图给inflate出来
                    View view = mStatusLayoutManager.errorVs.inflate();
                    if (mStatusLayoutManager.errorLayout != null) {
                        mStatusLayoutManager.errorLayout.setView(view);
                    }
                    view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mStatusLayoutManager.onRetryListener.onRetry();
                        }
                    });
                    layoutSparseArray.put(id, view);
                    isShow = true;
                } else {
                    isShow = false;
                }
                break;
                //空数据
            case LAYOUT_EMPTY_DATA_ID:
                if (mStatusLayoutManager.emptyDataVs != null) {
                    //只有当展示的时候，才将ViewStub视图给inflate出来
                    View view = mStatusLayoutManager.emptyDataVs.inflate();
                    if (mStatusLayoutManager.emptyDataLayout != null) {
                        mStatusLayoutManager.emptyDataLayout.setView(view);
                    }
                    view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mStatusLayoutManager.onRetryListener.onRetry();
                        }
                    });
                    layoutSparseArray.put(id, view);
                    isShow = true;
                } else {
                    isShow = false;
                }
                break;
            default:
                break;
        }
        return isShow;
    }

}

