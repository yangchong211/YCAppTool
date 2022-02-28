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
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import androidx.annotation.LayoutRes;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/7/6
 *     desc  : 状态管理器
 *     revise: 具体demo参考：https://github.com/yangchong211/YCStateLayout
 * </pre>
 */
public class StateLayoutManager {

    final Context context;
    final ViewStub netWorkErrorVs;
    final int netWorkErrorRetryViewId;
    final ViewStub emptyDataVs;
    final int emptyDataRetryViewId;
    final ViewStub errorVs;
    final int errorRetryViewId;
    final int loadingLayoutResId;
    final int contentLayoutResId;
    final int retryViewId;
    final int emptyDataIconImageId;
    final int emptyDataTextTipId;
    final int errorIconImageId;
    final int errorTextTipId;
    final AbsViewStubLayout errorLayout;
    final AbsViewStubLayout emptyDataLayout;

    private final StateFrameLayout rootFrameLayout;
    final OnShowHideViewListener onShowHideViewListener;
    final OnRetryListener onRetryListener;
    final OnNetworkListener onNetworkListener;

    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    public static Builder newBuilder(Context context , boolean wrapContent) {
        return new Builder(context,wrapContent);
    }

    /**
     * 复杂构造对象，使用builder模式
     * @param builder                       builder
     * @param wrapContent                   是否包裹内容
     */
    private StateLayoutManager(Builder builder , boolean wrapContent) {
        this.context = builder.context;
        this.loadingLayoutResId = builder.loadingLayoutResId;
        this.netWorkErrorVs = builder.netWorkErrorVs;
        this.netWorkErrorRetryViewId = builder.netWorkErrorRetryViewId;
        this.emptyDataVs = builder.emptyDataVs;
        this.emptyDataRetryViewId = builder.emptyDataRetryViewId;
        this.errorVs = builder.errorVs;
        this.errorRetryViewId = builder.errorRetryViewId;
        this.contentLayoutResId = builder.contentLayoutResId;
        this.onShowHideViewListener = builder.onShowHideViewListener;
        this.retryViewId = builder.retryViewId;
        this.onRetryListener = builder.onRetryListener;
        this.onNetworkListener = builder.onNetworkListener;
        this.emptyDataIconImageId = builder.emptyDataIconImageId;
        this.emptyDataTextTipId = builder.emptyDataTextTipId;
        this.errorIconImageId = builder.errorIconImageId;
        this.errorTextTipId = builder.errorTextTipId;
        this.errorLayout = builder.errorLayout;
        this.emptyDataLayout = builder.emptyDataLayout;

        //创建帧布局
        rootFrameLayout = new StateFrameLayout(this.context);
        ViewGroup.LayoutParams layoutParams ;
        //是否包裹内容
        if (wrapContent){
            layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }else {
            layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        rootFrameLayout.setLayoutParams(layoutParams);
        //设置为白色
        rootFrameLayout.setBackgroundColor(Color.WHITE);

        //设置状态管理器
        rootFrameLayout.setStatusLayoutManager(this);
    }

    /**
     * 判断是否在showLoading中
     * @return
     */
    public boolean isShowLoading(){
        return rootFrameLayout.isLoading();
    }

    /**
     * 显示loading
     */
    public void showLoading() {
        if (!isShowLoading()){
            rootFrameLayout.showLoading();
        }
    }

    /**
     * 隐藏loading
     */
    public void goneLoading() {
        rootFrameLayout.goneLoading();
    }

    /**
     * 显示内容
     */
    public void showContent() {
        rootFrameLayout.showContent();
    }

    /**
     * 显示空数据
     */
    public void showEmptyData(int iconImage, String textTip) {
        rootFrameLayout.showEmptyData(iconImage, textTip);
    }

    /**
     * 显示空数据
     */
    public void showEmptyData() {
        showEmptyData(0, "");
    }

    /**
     * 显示空数据
     */
    public void showLayoutEmptyData(Object... objects) {
        rootFrameLayout.showLayoutEmptyData(objects);
    }

    /**
     * 显示网络异常
     */
    public void showNetWorkError() {
        rootFrameLayout.showNetWorkError();
    }

    /**
     * 显示异常
     */
    public void showError(int iconImage, String textTip) {
        rootFrameLayout.showError(iconImage, textTip);
    }

    /**
     * 显示异常
     */
    public void showError() {
        showError(0, "");
    }

    /**
     * 显示异常
     * @param objects               objects
     */
    public void showLayoutError(Object... objects) {
        rootFrameLayout.showLayoutError(objects);
    }

    /**
     * 得到root 布局
     */
    public View getRootLayout() {
        return rootFrameLayout;
    }

    public static final class Builder {

        private Context context;
        /**
         * StateFrameLayout布局创建时，是否包裹内容，默认是MATCH_PARENT
         */
        private boolean wrapContent = false;
        private int loadingLayoutResId;
        private int contentLayoutResId;
        private ViewStub netWorkErrorVs;
        private int netWorkErrorRetryViewId;
        private ViewStub emptyDataVs;
        private int emptyDataRetryViewId;
        private ViewStub errorVs;
        private int errorRetryViewId;
        private int retryViewId;
        private int emptyDataIconImageId;
        private int emptyDataTextTipId;
        private int errorIconImageId;
        private int errorTextTipId;
        private AbsViewStubLayout errorLayout;
        private AbsViewStubLayout emptyDataLayout;
        private OnShowHideViewListener onShowHideViewListener;
        private OnRetryListener onRetryListener;
        private OnNetworkListener onNetworkListener;

        Builder(Context context) {
            this.context = context;
        }


        Builder(Context context , boolean wrapContent) {
            this.context = context;
            this.wrapContent = wrapContent;
        }

        /**
         * 自定义加载布局
         */
        public Builder loadingView(@LayoutRes int loadingLayoutResId) {
            this.loadingLayoutResId = loadingLayoutResId;
            return this;
        }

        /**
         * 自定义网络错误布局
         */
        public Builder netWorkErrorView(@LayoutRes int newWorkErrorId) {
            netWorkErrorVs = new ViewStub(context);
            netWorkErrorVs.setLayoutResource(newWorkErrorId);
            return this;
        }

        /**
         * 自定义加载空数据布局
         */
        public Builder emptyDataView(@LayoutRes int noDataViewId) {
            emptyDataVs = new ViewStub(context);
            emptyDataVs.setLayoutResource(noDataViewId);
            return this;
        }

        /**
         * 自定义加载错误布局
         */
        public Builder errorView(@LayoutRes int errorViewId) {
            errorVs = new ViewStub(context);
            errorVs.setLayoutResource(errorViewId);
            return this;
        }

        /**
         * 自定义加载内容正常布局
         */
        public Builder contentView(@LayoutRes int contentLayoutResId) {
            this.contentLayoutResId = contentLayoutResId;
            return this;
        }

        /**
         * 自定义异常布局
         * @param errorLayout                   error
         * @return
         */
        public Builder errorLayout(AbsViewStubLayout errorLayout) {
            this.errorLayout = errorLayout;
            this.errorVs = errorLayout.getLayoutVs();
            return this;
        }

        /**
         * 自定义空数据布局
         * @param emptyDataLayout              emptyDataLayout
         * @return
         */
        public Builder emptyDataLayout(AbsViewStubLayout emptyDataLayout) {
            this.emptyDataLayout = emptyDataLayout;
            this.emptyDataVs = emptyDataLayout.getLayoutVs();
            return this;
        }

        /**
         * 自定义网络异常布局
         * @param netWorkErrorRetryViewId       layoutId
         * @return
         */
        public Builder netWorkErrorRetryViewId(int netWorkErrorRetryViewId) {
            this.netWorkErrorRetryViewId = netWorkErrorRetryViewId;
            return this;
        }

        /**
         * 自定义空数据异常布局
         * @param emptyDataRetryViewId          layoutId
         * @return
         */
        public Builder emptyDataRetryViewId(int emptyDataRetryViewId) {
            this.emptyDataRetryViewId = emptyDataRetryViewId;
            return this;
        }

        /**
         * 自定义重新刷新布局
         * @param errorRetryViewId              layoutId
         * @return
         */
        public Builder errorRetryViewId(int errorRetryViewId) {
            this.errorRetryViewId = errorRetryViewId;
            return this;
        }


        public Builder retryViewId(int retryViewId) {
            this.retryViewId = retryViewId;
            return this;
        }

        public Builder emptyDataIconImageId(int emptyDataIconImageId) {
            this.emptyDataIconImageId = emptyDataIconImageId;
            return this;
        }

        public Builder emptyDataTextTipId(int emptyDataTextTipId) {
            this.emptyDataTextTipId = emptyDataTextTipId;
            return this;
        }

        public Builder errorIconImageId(int errorIconImageId) {
            this.errorIconImageId = errorIconImageId;
            return this;
        }

        public Builder errorTextTipId(int errorTextTipId) {
            this.errorTextTipId = errorTextTipId;
            return this;
        }

        /**
         * 为状态View显示隐藏监听事件
         * @param listener                  listener
         * @return
         */
        public Builder onShowHideViewListener(OnShowHideViewListener listener) {
            this.onShowHideViewListener = listener;
            return this;
        }

        /**
         * 为重试加载按钮的监听事件
         * @param onRetryListener           listener
         * @return
         */
        public Builder onRetryListener(OnRetryListener onRetryListener) {
            this.onRetryListener = onRetryListener;
            return this;
        }

        /**
         * 为重试加载按钮的监听事件
         * @param onNetworkListener           listener
         * @return
         */
        public Builder onNetworkListener(OnNetworkListener onNetworkListener) {
            this.onNetworkListener = onNetworkListener;
            return this;
        }

        /**
         * 创建对象
         * @return
         */
        public StateLayoutManager build() {
            return new StateLayoutManager(this, wrapContent);
        }
    }

}
