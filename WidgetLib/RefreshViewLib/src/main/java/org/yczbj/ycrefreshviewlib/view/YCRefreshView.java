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
package org.yczbj.ycrefreshviewlib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.ColorRes;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import org.yczbj.ycrefreshviewlib.R;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.observer.ViewDataObserver;
import org.yczbj.ycrefreshviewlib.utils.RefreshLogUtils;

import java.util.ArrayList;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://blog.csdn.net/m0_37700275/article/details/80863685
 *     time  : 2017/4/22
 *     desc  : 自定义控件
 *     revise: 支持多种状态切换；支持上拉加载更多，下拉刷新；支持添加头部或底部view
 * </pre>
 */
public class YCRefreshView extends FrameLayout {


    protected RecyclerView mRecyclerView;
    protected ViewGroup mProgressView;
    protected ViewGroup mEmptyView;
    protected ViewGroup mErrorView;
    private int mProgressId;
    private int mEmptyId;
    private int mErrorId;
    protected boolean mClipToPadding;
    protected int mPadding;
    protected int mPaddingTop;
    protected int mPaddingBottom;
    protected int mPaddingLeft;
    protected int mPaddingRight;
    protected int mScrollbarStyle;
    protected int mScrollbar;

    protected RecyclerView.OnScrollListener mInternalOnScrollListener;
    protected RecyclerView.OnScrollListener mExternalOnScrollListener;
    protected ArrayList<RecyclerView.OnScrollListener> mExternalOnScrollListenerList = new ArrayList<>();
    protected SwipeRefreshLayout mPtrLayout;
    protected SwipeRefreshLayout.OnRefreshListener mRefreshListener;

    public YCRefreshView(Context context) {
        super(context);
        initView();
    }

    public YCRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView();
    }

    public YCRefreshView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs);
        initView();
    }

    /**
     * 事件的分发
     * @param ev                事件
     * @return                  是否分发事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //交给父类去处理
        return mPtrLayout.dispatchTouchEvent(ev);
    }

    /**
     * 事件的触摸
     * @param event             event
     * @return                  是否自己处理触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //暂不处理
        return super.onTouchEvent(event);
    }

    /**
     * 拦截事件
     * @param ev                event
     * @return                  是否拦截事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //事件拦截，暂不处理
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * 当view销毁时会调用该方法
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRecyclerView!=null){
            mRecyclerView.removeOnScrollListener(mInternalOnScrollListener);
        }
    }

    /**
     * 完成绘制会调用该方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //int childCount = getChildCount();
        RecyclerView recyclerView = getRecyclerView();
        if (recyclerView!=null){
            initScrollListener();
            //添加滚动监听事件
            recyclerView.addOnScrollListener(mInternalOnScrollListener);
        }
    }


    /**
     * 初始化资源
     * @param attrs         attrs
     */
    protected void initAttrs(AttributeSet attrs) {
        //加载attr属性
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.YCRefreshView);
        try {
            mClipToPadding = a.getBoolean(R.styleable.YCRefreshView_recyclerClipToPadding, false);
            mPadding = (int) a.getDimension(R.styleable.YCRefreshView_recyclerPadding, -1.0f);
            mPaddingTop = (int) a.getDimension(R.styleable.YCRefreshView_recyclerPaddingTop, 0.0f);
            mPaddingBottom = (int) a.getDimension(R.styleable.YCRefreshView_recyclerPaddingBottom, 0.0f);
            mPaddingLeft = (int) a.getDimension(R.styleable.YCRefreshView_recyclerPaddingLeft, 0.0f);
            mPaddingRight = (int) a.getDimension(R.styleable.YCRefreshView_recyclerPaddingRight, 0.0f);
            mScrollbarStyle = a.getInteger(R.styleable.YCRefreshView_scrollbarStyle, -1);
            mScrollbar = a.getInteger(R.styleable.YCRefreshView_scrollbars, -1);
            mEmptyId = a.getResourceId(R.styleable.YCRefreshView_layout_empty, 0);
            mProgressId = a.getResourceId(R.styleable.YCRefreshView_layout_progress, 0);
            mErrorId = a.getResourceId(R.styleable.YCRefreshView_layout_error, 0);
        } finally {
            a.recycle();
        }
    }

    /**
     * 2017年3月29日
     * 欢迎访问GitHub：https://github.com/yangchong211
     * 初始化
     */
    private void initView() {
        //使用isInEditMode解决可视化编辑器无法识别自定义控件的问题
        if (isInEditMode()) {
            return;
        }
        //生成主View
        View v = LayoutInflater.from(getContext()).inflate(R.layout.refresh_recyclerview, this);
        mPtrLayout = v.findViewById(R.id.ptr_layout);
        mPtrLayout.setEnabled(false);

        //加载进度view
        mProgressView = v.findViewById(R.id.progress);
        if (mProgressId!=0){
            LayoutInflater.from(getContext()).inflate(mProgressId,mProgressView);
        }

        //数据为空时view
        mEmptyView = v.findViewById(R.id.empty);
        if (mEmptyId!=0){
            LayoutInflater.from(getContext()).inflate(mEmptyId,mEmptyView);
        }

        //数据加载错误view
        mErrorView = v.findViewById(R.id.error);
        if (mErrorId!=0){
            LayoutInflater.from(getContext()).inflate(mErrorId,mErrorView);
        }

        //初始化
        initRecyclerView(v);
    }


    protected void initRecyclerView(View view) {
        mRecyclerView = view.findViewById(android.R.id.list);
        setItemAnimator(null);
        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setClipToPadding(mClipToPadding);
            //设置recyclerView的padding值
            if (mPadding != -1) {
                mRecyclerView.setPadding(mPadding, mPadding, mPadding, mPadding);
            } else {
                mRecyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
            }
            if (mScrollbarStyle != -1) {
                mRecyclerView.setScrollBarStyle(mScrollbarStyle);
            }
            //这个是滑动区分滑动方向
            switch (mScrollbar){
                case 0:
                    setVerticalScrollBarEnabled(false);
                    break;
                case 1:
                    setHorizontalScrollBarEnabled(false);
                    break;
                case 2:
                    setVerticalScrollBarEnabled(false);
                    setHorizontalScrollBarEnabled(false);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 初始化滚动监听事件
     */
    private void initScrollListener() {
        mInternalOnScrollListener = new RecyclerView.OnScrollListener() {
            /**
             *
             * @param recyclerView                      recyclerView
             * @param dx                                x
             * @param dy                                y
             */
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mExternalOnScrollListener != null){
                    mExternalOnScrollListener.onScrolled(recyclerView, dx, dy);
                }
                for (RecyclerView.OnScrollListener listener : mExternalOnScrollListenerList) {
                    listener.onScrolled(recyclerView, dx, dy);
                }
            }

            /**
             *
             * @param recyclerView                      recyclerView
             * @param newState                          newState
             */
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mExternalOnScrollListener != null){
                    mExternalOnScrollListener.onScrollStateChanged(recyclerView, newState);
                }
                for (RecyclerView.OnScrollListener listener : mExternalOnScrollListenerList) {
                    listener.onScrollStateChanged(recyclerView, newState);
                }
            }
        };
    }

    /*--------------------------------------相关方法--------------------------------------------**/

    /**
     * 设置RecyclerView的LayoutManager
     * @param manager           LayoutManager
     */
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        if (manager!=null){
            mRecyclerView.setLayoutManager(manager);
        } else {
            throw new NullPointerException("un find no manager , please set manager must be null");
        }
    }

    /**
     * 获取SwipeRefreshLayout对象
     * @return              SwipeRefreshLayout对象
     */
    public SwipeRefreshLayout getSwipeToRefresh() {
        return mPtrLayout;
    }

    /**
     * 获取RecyclerView对象
     * @return                              RecyclerView对象
     */
    public RecyclerView getRecyclerView() {
        //获取对象
        return mRecyclerView;
    }

    /**
     * 设置预加载itemView数目
     * @param size                          size
     */
    public void setCacheSize(@IntRange  int size){
        if (getRecyclerView()!=null){
            getRecyclerView().setItemViewCacheSize(size);
        }
    }

    /**
     * 设置上下左右的边距
     */
    public void setRecyclerPadding(int left,int top,int right,int bottom){
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
        mRecyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
    }

    /**
     * 是否将剪辑设置为填充
     * @param isClip            isClip
     */
    @Override
    public void setClipToPadding(boolean isClip){
        if (getRecyclerView()!=null){
            getRecyclerView().setClipToPadding(isClip);
        }
    }

    /**
     * 设置滚动到索引为position的位置
     * @param position          位置
     */
    public void scrollToPosition(int position){
        if(getRecyclerView()!=null){
            getRecyclerView().scrollToPosition(position);
        }
    }


    @Override
    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        mRecyclerView.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
    }

    @Override
    public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
        mRecyclerView.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
    }

    /**
     * 设置加载数据为空的状态
     * @param emptyView         自定义布局
     */
    public void setEmptyView(View emptyView){
        mEmptyView.removeAllViews();
        mEmptyView.addView(emptyView);
    }

    /**
     * 设置加载数据为空的状态
     * @param emptyView         自定义布局
     */
    public void setEmptyView(int emptyView){
        mEmptyView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(emptyView, mEmptyView);
    }


    /**
     * 设置加载数据中
     * @param progressView      自定义布局
     */
    public void setProgressView(View progressView){
        mProgressView.removeAllViews();
        mProgressView.addView(progressView);
    }


    /**
     * 设置加载数据中
     * @param progressView      自定义布局
     */
    public void setProgressView(int progressView){
        mProgressView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(progressView, mProgressView);
    }


    /**
     * 设置加载错误的状态
     * @param errorView         自定义布局
     */
    public void setErrorView(View errorView){
        mErrorView.removeAllViews();
        mErrorView.addView(errorView);
    }


    /**
     * 设置加载错误的状态
     * @param errorView         自定义布局
     */
    public void setErrorView(int errorView){
        mErrorView.removeAllViews();
        LayoutInflater.from(getContext()).inflate(errorView, mErrorView);
    }


    /**
     * 设置适配器，关闭所有副view。展示recyclerView
     * 适配器有更新，自动关闭所有副view。根据条数判断是否展示EmptyView
     * @param adapter               Adapter适配器
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new ViewDataObserver(this));
        showRecycler();
    }


    /**
     * 设置适配器，关闭所有副view。展示进度条View
     * 适配器有更新，自动关闭所有副view。根据条数判断是否展示EmptyView
     * @param adapter               Adapter适配器
     */
    public void setAdapterWithProgress(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
        adapter.registerAdapterDataObserver(new ViewDataObserver(this));
        //只有Adapter为空时才显示ProgressView
        if (adapter instanceof RecyclerArrayAdapter){
            if (((RecyclerArrayAdapter) adapter).getCount() == 0){
                showProgress();
            }else {
                showRecycler();
            }
        }else {
            if (adapter.getItemCount() == 0){
                showProgress();
            }else {
                showRecycler();
            }
        }
    }


    /**
     * 从recycler清除adapter
     */
    public void clear() {
        mRecyclerView.setAdapter(null);
    }


    /**
     * 展示有数据时的布局，隐藏其他布局
     */
    private void hideAll(){
        mEmptyView.setVisibility(View.GONE);
        mProgressView.setVisibility(View.GONE);
        mErrorView.setVisibility(GONE);
        mPtrLayout.setRefreshing(false);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }


    /**
     * 设置加载错误状态
     */
    public void showError() {
        RefreshLogUtils.e("showError");
        if (mErrorView.getChildCount()>0){
            hideAll();
            mErrorView.setVisibility(View.VISIBLE);
        }else {
            showRecycler();
        }
    }

    /**
     * 设置加载数据为空状态
     */
    public void showEmpty() {
        RefreshLogUtils.e("showEmpty");
        if (mEmptyView.getChildCount()>0){
            hideAll();
            mEmptyView.setVisibility(View.VISIBLE);
        }else {
            showRecycler();
        }
    }


    /**
     * 设置加载数据中状态
     */
    public void showProgress() {
        RefreshLogUtils.e("showProgress");
        if (mProgressView.getChildCount()>0){
            hideAll();
            mProgressView.setVisibility(View.VISIBLE);
        }else {
            showRecycler();
        }
    }


    /**
     * 设置加载数据完毕状态
     */
    public void showRecycler() {
        RefreshLogUtils.e("showRecycler");
        hideAll();
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    /**
     * 设置下拉刷新监听
     * @param listener              OnRefreshListener监听
     */
    public void setRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        mPtrLayout.setEnabled(true);
        mPtrLayout.setOnRefreshListener(listener);
        this.mRefreshListener = listener;
    }

    /**
     * 设置是否刷新
     * @param isRefreshing          是否刷新
     */
    public void setRefreshing(final boolean isRefreshing){
        mPtrLayout.post(new Runnable() {
            @Override
            public void run() {
                mPtrLayout.setRefreshing(isRefreshing);
            }
        });
    }

    /**
     * 设置是否刷新
     * @param isRefreshing          是否刷新
     * @param isCallbackListener    是否回调监听
     */
    public void setRefreshing(final boolean isRefreshing, final boolean isCallbackListener){
        mPtrLayout.post(new Runnable() {
            @Override
            public void run() {
                mPtrLayout.setRefreshing(isRefreshing);
                if (isRefreshing && isCallbackListener && mRefreshListener!=null){
                    mRefreshListener.onRefresh();
                }
            }
        });
    }

    /**
     * 设置刷新颜色
     * @param colRes                int类型颜色值
     */
    public void setRefreshingColorResources(@ColorRes int... colRes) {
        mPtrLayout.setColorSchemeResources(colRes);
    }

    /**
     * 设置刷新颜色
     * @param col                   int类型颜色值
     */
    public void setRefreshingColor(int... col) {
        mPtrLayout.setColorSchemeColors(col);
    }

    /**
     * 设置滚动监听
     * @param listener              OnScrollListener监听器
     */
    @Deprecated
    public void setOnScrollListener(RecyclerView.OnScrollListener listener) {
        mExternalOnScrollListener = listener;
    }


    /**
     * 添加滚动监听器
     * @param listener              OnScrollListener监听器
     */
    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        mExternalOnScrollListenerList.add(listener);
    }


    /**
     * 移除滚动监听器
     * @param listener              OnScrollListener监听器
     */
    public void removeOnScrollListener(RecyclerView.OnScrollListener listener) {
        mExternalOnScrollListenerList.remove(listener);
    }


    /**
     * 移除所有的滚动监听
     */
    public void removeAllOnScrollListeners() {
        mExternalOnScrollListenerList.clear();
    }

    /**
     * 添加条目触摸监听器
     * @param listener              OnItemTouchListener监听器
     */
    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecyclerView.addOnItemTouchListener(listener);
    }

    /**
     * 移除条目触摸监听器
     */
    public void removeOnItemTouchListener(RecyclerView.OnItemTouchListener listener) {
        mRecyclerView.removeOnItemTouchListener(listener);
    }

    /**
     * 获取RecyclerView的adapter适配器
     * @return                      RecyclerView.Adapter对象
     */
    public RecyclerView.Adapter getAdapter() {
        return mRecyclerView.getAdapter();
    }


    @Override
    @SuppressLint("ClickableViewAccessibility")
    public void setOnTouchListener(OnTouchListener listener) {
        mRecyclerView.setOnTouchListener(listener);
    }

    /**
     * 设置条目动画效果
     * @param animator              ItemAnimator
     */
    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }


    /**
     * 添加分割线
     * @param itemDecoration        分割线
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    /**
     * 在索引index出添加分割线
     * @param itemDecoration        分割线
     * @param index                 索引
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        mRecyclerView.addItemDecoration(itemDecoration, index);
    }


    /**
     * 移除分割线
     * @param itemDecoration        分割线
     */
    public void removeItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.removeItemDecoration(itemDecoration);
    }

    /**
     * 获取error视图view
     * @return                      view
     */
    public View getErrorView() {
        if (mErrorView.getChildCount()>0){
            return mErrorView.getChildAt(0);
        }
        return null;
    }


    /**
     * 获取Progress视图view
     * @return                      view
     */
    public View getProgressView() {
        if (mProgressView.getChildCount()>0){
            return mProgressView.getChildAt(0);
        }
        return null;
    }

    /**
     * 获取Empty视图view
     * @return                      view
     */
    public View getEmptyView() {
        if (mEmptyView.getChildCount()>0) {
            return mEmptyView.getChildAt(0);
        }
        return null;
    }

}
