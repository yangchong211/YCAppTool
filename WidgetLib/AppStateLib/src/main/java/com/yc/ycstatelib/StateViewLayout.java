package com.yc.ycstatelib;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/7/6
 *     desc  : 自定义状态管理，用于list条目中
 *     revise: 借鉴大神齐翔开源库
 *             参考链接：https://github.com/luckybilly/Gloading
 * </pre>
 */
public class StateViewLayout {

    public static final int STATUS_LOADING = 1;
    public static final int STATUS_LOAD_SUCCESS = 2;
    public static final int STATUS_LOAD_FAILED = 3;
    public static final int STATUS_EMPTY_DATA = 4;
    
    private static volatile StateViewLayout mDefault;
    private Adapter mAdapter;
    private static boolean DEBUG = false;

    /**
     * 提供显示当前加载状态的视图
     */
    public interface Adapter {
        /**
         * get view for current status
         * @param holder Holder
         * @param convertView The old view to reuse, if possible.
         * @param status current status
         * @return status view to show. Maybe convertView for reuse.
         * @see Holder
         */
        View getView(Holder holder, View convertView, int status);
    }

    /**
     * set debug mode or not
     * @param debug true:debug mode, false:not debug mode
     */
    public static void debug(boolean debug) {
        DEBUG = debug;
    }

    /**
     * 构造方法私有话，避免外部通过new进行初始化
     */
    private StateViewLayout() { }

    /**
     * Create a new StateViewLayout different from the default one
     * @param adapter another adapter different from the default one
     * @return StateViewLayout
     */
    public static StateViewLayout from(Adapter adapter) {
        checkAdapter(adapter);
        StateViewLayout stateViewLayout = new StateViewLayout();
        stateViewLayout.mAdapter = adapter;
        return stateViewLayout;
    }

    /**
     * get default StateViewLayout object for global usage in whole app
     * @return default StateViewLayout object
     */
    public static StateViewLayout getDefault() {
        if (mDefault == null) {
            synchronized (StateViewLayout.class) {
                if (mDefault == null) {
                    mDefault = new StateViewLayout();
                }
            }
        }
        return mDefault;
    }

    /**
     * init the default loading status view creator ({@link Adapter})
     * @param adapter adapter to create all status views
     */
    public static void initDefault(Adapter adapter) {
        checkAdapter(adapter);
        getDefault().mAdapter = adapter;
    }

    /**
     * StateViewLayout(loading status view) wrap the whole activity
     * wrapper is android.R.id.content
     * @param activity current activity object
     * @return holder of StateViewLayout
     */
    public Holder wrap(Activity activity) {
        ViewGroup wrapper = activity.findViewById(android.R.id.content);
        return new Holder(mAdapter, activity, wrapper);
    }

    /**
     * StateViewLayout(loading status view) wrap the specific view.
     * @param view view to be wrapped
     * @return Holder
     */
    public Holder wrap(View view) {
        FrameLayout wrapper = new FrameLayout(view.getContext());
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp != null) {
            wrapper.setLayoutParams(lp);
        }
        if (view.getParent() != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            int index = parent.indexOfChild(view);
            parent.removeView(view);
            parent.addView(wrapper, index);
        }
        LayoutParams newLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        wrapper.addView(view, newLp);
        return new Holder(mAdapter, view.getContext(), wrapper);
    }

    /**
     * 检查是否设置了自定义adapter，必须先初始化
     * @param adapter                       adapter
     */
    private static void checkAdapter(Adapter adapter){
        if (adapter == null){
            throw new NullPointerException("please set initDefault at first");
        }
    }

    /**
     * StateViewLayout holder<br>
     * create by {@link StateViewLayout#wrap(Activity)} or {@link StateViewLayout#wrap(View)}<br>
     * the core API for showing all status view
     */
    public static class Holder {

        private Adapter mAdapter;
        private Context mContext;
        private Runnable mRetryTask;
        private View mCurStatusView;
        private ViewGroup mWrapper;
        private int curState;
        private SparseArray<View> mStatusViews = new SparseArray<>(4);
        private Object mData;

        private Holder(Adapter adapter, Context context, ViewGroup wrapper) {
            this.mAdapter = adapter;
            this.mContext = context;
            this.mWrapper = wrapper;
        }

        /**
         * set retry task when user click the retry button in load failed page
         * @param task when user click in load failed UI, run this task
         * @return this
         */
        public Holder withRetry(Runnable task) {
            mRetryTask = task;
            return this;
        }

        /**
         * set extension data
         * @param data extension data
         * @return this
         */
        public Holder withData(Object data) {
            this.mData = data;
            return this;
        }

        /** show UI for status: {@link #STATUS_LOADING} */
        public void showLoading() {
            showLoadingStatus(STATUS_LOADING);
        }
        /** show UI for status: {@link #STATUS_LOAD_SUCCESS} */
        public void showLoadSuccess() {
            showLoadingStatus(STATUS_LOAD_SUCCESS);
        }
        /** show UI for status: {@link #STATUS_LOAD_FAILED} */
        public void showLoadFailed() {
            showLoadingStatus(STATUS_LOAD_FAILED);
        }
        /** show UI for status: {@link #STATUS_EMPTY_DATA} */
        public void showEmpty() {
            showLoadingStatus(STATUS_EMPTY_DATA);
        }

        /**
         * Show specific status UI
         * @param status status
         * @see #showLoading()
         * @see #showLoadFailed()
         * @see #showLoadSuccess()
         * @see #showEmpty()
         */
        public void showLoadingStatus(int status) {
            if (curState == status || !validate()) {
                return;
            }
            curState = status;
            //first try to reuse status view
            View convertView = mStatusViews.get(status);
            if (convertView == null) {
                //secondly try to reuse current status view
                convertView = mCurStatusView;
            }
            try {
                //call customer adapter to get UI for specific status. convertView can be reused
                View view = mAdapter.getView(this, convertView, status);
                if (view == null) {
                    printLog(mAdapter.getClass().getName() + ".getView returns null");
                    return;
                }
                if (view != mCurStatusView || mWrapper.indexOfChild(view) < 0) {
                    if (mCurStatusView != null) {
                        mWrapper.removeView(mCurStatusView);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.setElevation(Float.MAX_VALUE);
                    }
                    mWrapper.addView(view);
                    ViewGroup.LayoutParams lp = view.getLayoutParams();
                    if (lp != null) {
                        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    }
                } else if (mWrapper.indexOfChild(view) != mWrapper.getChildCount() - 1) {
                    // 确保加载状态视图在前面
                    view.bringToFront();
                }
                mCurStatusView = view;
                mStatusViews.put(status, view);
            } catch(Exception e) {
                if (DEBUG) {
                    e.printStackTrace();
                }
            }
        }

        private boolean validate() {
            if (mAdapter == null) {
                printLog("StateViewLayout.Adapter is not specified.");
            }
            if (mContext == null) {
                printLog("Context is null.");
            }
            if (mWrapper == null) {
                printLog("The mWrapper of loading status view is null.");
            }
            return mAdapter != null && mContext != null && mWrapper != null;
        }

        public Context getContext() {
            return mContext;
        }

        /**
         * get wrapper
         * @return container of gloading
         */
        public ViewGroup getWrapper() {
            return mWrapper;
        }

        /**
         * get retry task
         * @return retry task
         */
        public Runnable getRetryTask() {
            return mRetryTask;
        }

        /**
         *
         * get extension data
         * @param <T> return type
         * @return data
         */
        public <T> T getData() {
            try {
                return (T) mData;
            } catch(Exception e) {
                if (DEBUG) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private static void printLog(String msg) {
        if (DEBUG) {
            Log.e("StateViewLayout", msg);
        }
    }
}
