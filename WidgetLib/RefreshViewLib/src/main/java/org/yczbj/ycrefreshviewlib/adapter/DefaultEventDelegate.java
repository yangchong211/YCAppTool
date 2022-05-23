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

package org.yczbj.ycrefreshviewlib.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.yczbj.ycrefreshviewlib.utils.RefreshLogUtils;
import org.yczbj.ycrefreshviewlib.inter.InterEventDelegate;
import org.yczbj.ycrefreshviewlib.inter.InterItemView;
import org.yczbj.ycrefreshviewlib.inter.OnErrorListener;
import org.yczbj.ycrefreshviewlib.inter.OnMoreListener;
import org.yczbj.ycrefreshviewlib.inter.OnNoMoreListener;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/1/29
 *     desc  : 默认设置数据和加载监听的类
 *     revise:
 * </pre>
 */
public class DefaultEventDelegate implements InterEventDelegate {

    private RecyclerArrayAdapter adapter;
    private EventFooter footer ;

    private OnMoreListener onMoreListener;
    private OnNoMoreListener onNoMoreListener;
    private OnErrorListener onErrorListener;

    private boolean hasData = false;
    private boolean isLoadingMore = false;
    private boolean hasMore = false;
    private boolean hasNoMore = false;
    private boolean hasError = false;
    private int status = STATUS_INITIAL;
    private static final int STATUS_INITIAL = 291;
    private static final int STATUS_MORE = 260;
    private static final int STATUS_NO_MORE = 408;
    private static final int STATUS_ERROR = 732;

    DefaultEventDelegate(RecyclerArrayAdapter adapter) {
        this.adapter = adapter;
        footer = new EventFooter();
        adapter.addFooter(footer);
    }

    private void onMoreViewShowed() {
        RefreshLogUtils.d("onMoreViewShowed");
        if (!isLoadingMore&& onMoreListener !=null){
            isLoadingMore = true;
            onMoreListener.onMoreShow();
        }
    }

    private void onMoreViewClicked() {
        if (onMoreListener !=null){
            onMoreListener.onMoreClick();
        }
    }

    private void onErrorViewShowed() {
        if (onErrorListener!=null){
            onErrorListener.onErrorShow();
        }
    }

    private void onErrorViewClicked() {
        if (onErrorListener!=null){
            onErrorListener.onErrorClick();
        }
    }

    private void onNoMoreViewShowed() {
        if (onNoMoreListener!=null){
            onNoMoreListener.onNoMoreShow();
        }
    }

    private void onNoMoreViewClicked() {
        if (onNoMoreListener!=null){
            onNoMoreListener.onNoMoreClick();
        }
    }

    //-------------------5个状态触发事件-------------------

    @Override
    public void addData(int length) {
        RefreshLogUtils.d("addData" + length);
        if (hasMore){
            if (length == 0){
                //当添加0个时，认为已结束加载到底
                if (status==STATUS_INITIAL || status == STATUS_MORE){
                    footer.showNoMore();
                    status = STATUS_NO_MORE;
                }
            }else {
                //当Error或初始时。添加数据，如果有More则还原。
                footer.showMore();
                status = STATUS_MORE;
                hasData = true;
            }
        }else{
            if (hasNoMore){
                footer.showNoMore();
                status = STATUS_NO_MORE;
            }
        }
        isLoadingMore = false;
    }

    @Override
    public void clear() {
        RefreshLogUtils.d("clear");
        hasData = false;
        status = STATUS_INITIAL;
        footer.hide();
        isLoadingMore = false;
    }

    @Override
    public void stopLoadMore() {
        RefreshLogUtils.d("stopLoadMore");
        footer.showNoMore();
        status = STATUS_NO_MORE;
        isLoadingMore = false;
    }

    @Override
    public void pauseLoadMore() {
        RefreshLogUtils.d("pauseLoadMore");
        footer.showError();
        status = STATUS_ERROR;
        isLoadingMore = false;
    }

    @Override
    public void resumeLoadMore() {
        isLoadingMore = false;
        footer.showMore();
        status = STATUS_MORE;
        onMoreViewShowed();
    }

    //-------------------3种View设置-------------------

    @Override
    public void setMore(View view, OnMoreListener listener) {
        this.footer.setMoreView(view);
        this.onMoreListener = listener;
        hasMore = true;
        // 为了处理setMore之前就添加了数据的情况
        if (adapter.getCount()>0){
            addData(adapter.getCount());
        }
        RefreshLogUtils.d("setMore");
    }

    @Override
    public void setNoMore(View view, OnNoMoreListener listener) {
        this.footer.setNoMoreView(view);
        this.onNoMoreListener = listener;
        hasNoMore = true;
        RefreshLogUtils.d("setNoMore");
    }

    @Override
    public void setErrorMore(View view, OnErrorListener listener) {
        this.footer.setErrorView(view);
        this.onErrorListener = listener;
        hasError = true;
        RefreshLogUtils.d("setErrorMore");
    }

    @Override
    public void setMore(int res, OnMoreListener listener) {
        this.footer.setMoreViewRes(res);
        this.onMoreListener = listener;
        hasMore = true;
        // 为了处理setMore之前就添加了数据的情况
        if (adapter.getCount()>0){
            addData(adapter.getCount());
        }
        RefreshLogUtils.d("setMore");
    }

    @Override
    public void setNoMore(int res, OnNoMoreListener listener) {
        this.footer.setNoMoreViewRes(res);
        this.onNoMoreListener = listener;
        hasNoMore = true;
        RefreshLogUtils.d("setNoMore");
    }

    @Override
    public void setErrorMore(int res, OnErrorListener listener) {
        this.footer.setErrorViewRes(res);
        this.onErrorListener = listener;
        hasError = true;
        RefreshLogUtils.d("setErrorMore");
    }


    /**
     * 这个是设置上拉加载footer
     */
    public class EventFooter implements InterItemView {

        private View moreView = null;
        private View noMoreView = null;
        private View errorView = null;
        private int moreViewRes = 0;
        private int noMoreViewRes = 0;
        private int errorViewRes = 0;
        private int flag = HIDE;
        static final int HIDE = 520;
        private static final int SHOW_MORE = 521;
        private static final int SHOW_ERROR = 522;
        static final int SHOW_NO_MORE = 523;
        /**
         * 是否展示error的view
         */
        private boolean skipError = false;
        /**
         * 是否展示noMore的view
         */
        private boolean skipNoMore = false;

        private EventFooter(){}

        @Override
        public View onCreateView(ViewGroup parent) {
            RefreshLogUtils.d("onCreateView");
            return refreshStatus(parent);
        }

        @Override
        public void onBindView(View headerView) {
            RefreshLogUtils.d("onBindView");
            headerView.post(new Runnable() {
                @Override
                public void run() {
                    running(flag);
                }
            });
        }

        private void running(int flag) {
            switch (flag){
                case SHOW_MORE:
                    onMoreViewShowed();
                    break;
                case SHOW_NO_MORE:
                    if (!skipNoMore) {
                        onNoMoreViewShowed();
                    }
                    skipNoMore = false;
                    break;
                case SHOW_ERROR:
                    if (!skipError) {
                        onErrorViewShowed();
                    }
                    skipError = false;
                    break;
                default:
                    break;
            }
        }

        View refreshStatus(ViewGroup parent){
            View view = null;
            switch (flag){
                case SHOW_MORE:
                    if (moreView!=null) {
                        view = moreView;
                    } else if (moreViewRes!=0) {
                        view = LayoutInflater.from(parent.getContext())
                                .inflate(moreViewRes, parent, false);
                    }
                    if (view!=null) {
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onMoreViewClicked();
                            }
                        });
                    }
                    break;
                case SHOW_ERROR:
                    if (errorView!=null) {
                        view = errorView;
                    } else if (errorViewRes!=0) {
                        view = LayoutInflater.from(parent.getContext())
                                .inflate(errorViewRes, parent, false);
                    }
                    if (view!=null) {
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onErrorViewClicked();
                            }
                        });
                    }
                    break;
                case SHOW_NO_MORE:
                    if (noMoreView!=null) {
                        view = noMoreView;
                    } else if (noMoreViewRes!=0) {
                        view = LayoutInflater.from(parent.getContext())
                                .inflate(noMoreViewRes, parent, false);
                    }
                    if (view!=null) {
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onNoMoreViewClicked();
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
            if (view == null) {
                view = new FrameLayout(parent.getContext());
            }
            return view;
        }

        void showError(){
            RefreshLogUtils.d("footer showError");
            skipError = true;
            flag = SHOW_ERROR;
            //noinspection deprecation
            if (adapter.getItemCount()>0){
                adapter.notifyItemChanged(adapter.getItemCount()-1);
            }
        }
        void showMore(){
            RefreshLogUtils.d("footer showMore");
            flag = SHOW_MORE;
            //noinspection deprecation
            if (adapter.getItemCount()>0){
                adapter.notifyItemChanged(adapter.getItemCount()-1);
            }
        }
        void showNoMore(){
            RefreshLogUtils.d("footer showNoMore");
            skipNoMore = true;
            flag = SHOW_NO_MORE;
            //noinspection deprecation
            if (adapter.getItemCount()>0){
                adapter.notifyItemChanged(adapter.getItemCount()-1);
            }
        }

        void hide(){
            RefreshLogUtils.d("footer hide");
            flag = HIDE;
            //noinspection deprecation
            if (adapter.getItemCount()>0){
                adapter.notifyItemChanged(adapter.getItemCount()-1);
            }
        }

        void setMoreView(View moreView) {
            this.moreView = moreView;
            this.moreViewRes = 0;
        }

        void setNoMoreView(View noMoreView) {
            this.noMoreView = noMoreView;
            this.noMoreViewRes = 0;
        }

        void setErrorView(View errorView) {
            this.errorView = errorView;
            this.errorViewRes = 0;
        }

        void setMoreViewRes(int moreViewRes) {
            this.moreView = null;
            this.moreViewRes = moreViewRes;
        }

        void setNoMoreViewRes(int noMoreViewRes) {
            this.noMoreView = null;
            this.noMoreViewRes = noMoreViewRes;
        }

        void setErrorViewRes(int errorViewRes) {
            this.errorView = null;
            this.errorViewRes = errorViewRes;
        }

        @Override
        public int hashCode() {
            return flag;
        }
    }


}
