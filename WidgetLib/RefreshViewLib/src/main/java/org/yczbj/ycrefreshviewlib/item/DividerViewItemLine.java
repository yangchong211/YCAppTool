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

package org.yczbj.ycrefreshviewlib.item;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.View;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/2
 *     desc  : list条目的分割线
 *     revise: 可以设置线条颜色和宽度，并且可以设置距离左右的间距
 * </pre>
 */
public class DividerViewItemLine extends RecyclerView.ItemDecoration{

    private ColorDrawable mColorDrawable;
    /**
     * 分割线的高度，单位是像素px
     */
    private int mHeight;
    /**
     * 距离左边的padding值
     */
    private int mPaddingLeft;
    /**
     * 距离右边的padding值
     */
    private int mPaddingRight;
    /**
     * 设置是否绘制最后一条item的分割线
     */
    private boolean mDrawLastItem = true;
    /**
     * 设置是否绘制header和footer的分割线
     */
    private boolean mDrawHeaderFooter = false;

    public DividerViewItemLine(int color, int height) {
        this.mColorDrawable = new ColorDrawable(color);
        this.mHeight = height;
    }

    public DividerViewItemLine(int color, int height, int paddingLeft, int paddingRight) {
        this.mColorDrawable = new ColorDrawable(color);
        this.mHeight = height;
        this.mPaddingLeft = paddingLeft;
        this.mPaddingRight = paddingRight;
    }

    public void setDrawLastItem(boolean mDrawLastItem) {
        this.mDrawLastItem = mDrawLastItem;
    }

    public void setDrawHeaderFooter(boolean mDrawHeaderFooter) {
        this.mDrawHeaderFooter = mDrawHeaderFooter;
    }

    /**
     * 调用的是getItemOffsets会被多次调用，在layoutManager每次测量可摆放的view的时候回调用一次，
     * 在当前状态下需要摆放多少个view这个方法就会回调多少次。
     * @param outRect                   核心参数，这个rect相当于item摆放的时候设置的margin，
     *                                  rect的left相当于item的marginLeft，
     *                                  rect的right相当于item的marginRight
     * @param view                      当前绘制的view，可以用来获取它在adapter中的位置
     * @param parent                    recyclerView
     * @param state                     状态，用的很少
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int orientation = 0;
        int headerCount = 0,footerCount = 0;
        if (parent.getAdapter()==null){
            return;
        }
        //获取header和footer的数量
        if (parent.getAdapter() instanceof RecyclerArrayAdapter){
            headerCount = ((RecyclerArrayAdapter) parent.getAdapter()).getHeaderCount();
            footerCount = ((RecyclerArrayAdapter) parent.getAdapter()).getFooterCount();
        }

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager){
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        }else if (layoutManager instanceof GridLayoutManager){
            orientation = ((GridLayoutManager) layoutManager).getOrientation();
        }else if (layoutManager instanceof LinearLayoutManager){
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
        }
        int itemCount = parent.getAdapter().getItemCount();
        int count = itemCount-footerCount;

        //下面代码才是重点，更多内容可以看我的GitHub博客汇总：https://github.com/yangchong211/YCBlogs
        if (mDrawHeaderFooter){
            if (position >= headerCount && position<count){
                if (orientation == OrientationHelper.VERTICAL){
                    //当是竖直方向的时候，距离底部marginBottom是分割线的高度
                    outRect.bottom = mHeight;
                }else {
                    //noinspection SuspiciousNameCombination
                    outRect.right = mHeight;
                }
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent,
                           @NonNull RecyclerView.State state) {
        if (parent.getAdapter() == null){
            return;
        }
        int orientation = 0;
        int headerCount = 0, footerCount = 0 , dataCount;
        if (parent.getAdapter() instanceof RecyclerArrayAdapter){
            headerCount = ((RecyclerArrayAdapter) parent.getAdapter()).getHeaderCount();
            footerCount = ((RecyclerArrayAdapter) parent.getAdapter()).getFooterCount();
            dataCount = ((RecyclerArrayAdapter) parent.getAdapter()).getCount();
        }else {
            dataCount = parent.getAdapter().getItemCount();
        }
        int dataStartPosition = headerCount;
        int dataEndPosition = headerCount+dataCount;

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager){
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        }else if (layoutManager instanceof GridLayoutManager){
            orientation = ((GridLayoutManager) layoutManager).getOrientation();
        }else if (layoutManager instanceof LinearLayoutManager){
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
        }
        int start,end;
        if (orientation == OrientationHelper.VERTICAL){
            start = parent.getPaddingLeft() + mPaddingLeft;
            end = parent.getWidth() - parent.getPaddingRight() - mPaddingRight;
        }else {
            start = parent.getPaddingTop() + mPaddingLeft;
            end = parent.getHeight() - parent.getPaddingBottom() - mPaddingRight;
        }

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            //数据项除了最后一项 数据项最后一项
            //header&footer且可绘制
            boolean a = position >= dataStartPosition;
            boolean b = position<dataEndPosition-1;
            boolean d = position == dataEndPosition-1 && mDrawLastItem;
            boolean f = !(position>=dataStartPosition&&position<dataEndPosition)&&mDrawHeaderFooter;
            if (a){
                if (b ||d ||f){
                    if (orientation == OrientationHelper.VERTICAL){
                        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                                child.getLayoutParams();
                        int top = child.getBottom() + params.bottomMargin;
                        int bottom = top + mHeight;
                        mColorDrawable.setBounds(start,top,end,bottom);
                        mColorDrawable.draw(c);
                    }else {
                        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                                child.getLayoutParams();
                        int left = child.getRight() + params.rightMargin;
                        int right = left + mHeight;
                        mColorDrawable.setBounds(left,start,right,end);
                        mColorDrawable.draw(c);
                    }
                }
            }
        }
    }


}