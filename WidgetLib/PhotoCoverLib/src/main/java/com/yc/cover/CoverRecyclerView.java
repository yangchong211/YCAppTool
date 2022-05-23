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
package com.yc.cover;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/9/11
 *     desc  : 自定义RecyclerView
 *     revise:
 * </pre>
 */
public class CoverRecyclerView extends RecyclerView {

    /**
     * 按下的X轴坐标
     */
    private float mDownX;
    /**
     * 布局器构建者
     */
    private CoverLayoutManger.Builder mManagerBuilder;

    public CoverRecyclerView(Context context) {
        super(context);
        init();
    }

    public CoverRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CoverRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        createManageBuilder();
        this.setLayoutManager(mManagerBuilder.build());
        //开启重新排序
        this.setChildrenDrawingOrderEnabled(true);
        this.setOverScrollMode(OVER_SCROLL_NEVER);
        this.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //滚动停止时
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:

                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dx != 0) {//去掉奇怪的内存疯涨问题
                    if (recyclerView.getLayoutManager() instanceof CoverLayoutManger) {
                        CoverLayoutManger manger = (CoverLayoutManger) recyclerView.getLayoutManager();
                        manger.fixOffsetWhenFinishScroll();
                    }
                }
            }
        });
    }

    /**
     * 创建布局构建器
     */
    private void createManageBuilder() {
        if (mManagerBuilder == null) {
            mManagerBuilder = new CoverLayoutManger.Builder();
        }
    }

    /**
     * 设置是否为普通平面滚动
     * @param isFlat true:平面滚动；false:叠加缩放滚动
     */
    public void setFlatFlow(boolean isFlat) {
        createManageBuilder();
        mManagerBuilder.setFlat(isFlat);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置Item灰度渐变
     * @param greyItem true:Item灰度渐变；false:Item灰度不变
     */
    public void setGreyItem(boolean greyItem) {
        createManageBuilder();
        mManagerBuilder.setGreyItem(greyItem);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置Item灰度渐变
     * @param alphaItem true:Item半透渐变；false:Item透明度不变
     */
    public void setAlphaItem(boolean alphaItem) {
        createManageBuilder();
        mManagerBuilder.setAlphaItem(alphaItem);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置Item的间隔比例
     * @param intervalRatio Item间隔比例。
     *                      即：item的宽 x intervalRatio
     */
    public void setIntervalRatio(float intervalRatio) {
        createManageBuilder();
        mManagerBuilder.setIntervalRatio(intervalRatio);
        setLayoutManager(mManagerBuilder.build());
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (!(layout instanceof CoverLayoutManger)) {
            throw new IllegalArgumentException("The layout manager must be CoverLayoutManger");
        }
        super.setLayoutManager(layout);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int center = getCoverFlowLayout().getCenterPosition() - getCoverFlowLayout().getFirstVisiblePosition();
        //计算正在显示的所有Item的中间位置
        if (center < 0) {
            center = 0;
        } else if (center > childCount) {
            center = childCount;
        }
        int order;
        if (i == center) {
            order = childCount - 1;
        } else if (i > center) {
            order = center + childCount - 1 - i;
        } else {
            order = i;
        }
        return order;
    }

    /**
     * 获取LayoutManger，并强制转换为CoverFlowLayoutManger
     */
    public CoverLayoutManger getCoverFlowLayout() {
        return ((CoverLayoutManger)getLayoutManager());
    }

    /**
     * 获取被选中的Item位置
     */
    public int getSelectedPos() {
        CoverLayoutManger coverFlowLayout = getCoverFlowLayout();
        if (coverFlowLayout!=null){
            int selectedPos = getCoverFlowLayout().getSelectedPos();
            return selectedPos;
        }
        return 0;
    }

    /**
     * 设置选中监听
     * @param l 监听接口
     */
    public void setOnItemSelectedListener(CoverLayoutManger.OnSelected l) {
        if (getCoverFlowLayout()!=null){
            getCoverFlowLayout().setOnSelectedListener(l);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                //设置父类不拦截滑动事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int centerPosition = getCoverFlowLayout().getCenterPosition();
                int itemCount = getCoverFlowLayout().getItemCount();
                if ((ev.getX() > mDownX &&  centerPosition== 0) ||
                        (ev.getX() < mDownX && centerPosition == itemCount -1)) {
                    //如果是滑动到了最前和最后，开放父类滑动事件拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    //滑动到中间，设置父类不拦截滑动事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
