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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import org.yczbj.ycrefreshviewlib.utils.RefreshLogUtils;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/5/2
 *     desc  : list条目的分割线
 *     revise: 可以设置线条颜色和宽度
 * </pre>
 */
public class RecycleViewItemLine extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private Drawable mDivider;
    /**
     * 分割线高度，默认为1px
     */
    private int mDividerHeight = 1;
    /**
     * 列表的方向：LinearLayoutManager.VERTICAL或LinearLayoutManager.HORIZONTAL
     */
    private int mOrientation;
    private static int[] ATTRS = new int[]{android.R.attr.listDivider};

    /**
     * 默认分割线：高度为2px，颜色为灰色
     * @param context     上下文
     * @param orientation 列表方向
     */
    public RecycleViewItemLine(Context context, int orientation) {
        if (orientation != LinearLayoutManager.VERTICAL &&
                orientation != LinearLayoutManager.HORIZONTAL) {
            throw new IllegalArgumentException("请输入正确的参数！");
        }
        mOrientation = orientation;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    /**
     * 自定义分割线
     * @param context     上下文
     * @param orientation 列表方向
     * @param drawableId  分割线图片,或者shape图形
     */
    public RecycleViewItemLine(Context context, int orientation, int drawableId) {
        this(context, orientation);
        mDivider = ContextCompat.getDrawable(context, drawableId);
        if (mDivider != null) {
            mDividerHeight = mDivider.getIntrinsicHeight();
        }
    }

    /**
     * 自定义分割线
     * @param context       上下文
     * @param orientation   列表方向
     * @param dividerHeight 分割线高度
     * @param dividerColor  分割线颜色
     */
    public RecycleViewItemLine(Context context, int orientation,
                               int dividerHeight, int dividerColor) {
        this(context, orientation);
        mDividerHeight = dividerHeight;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置画笔paint的颜色
        mPaint.setColor(dividerColor);
        //设置样式
        mPaint.setStyle(Paint.Style.FILL);
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
        super.getItemOffsets(outRect, view, parent, state);
        //给bottom留出一个高度为mDividerHeight的空白
        //这样做的目的是什么呢？就是为下面onDraw方法绘制高度为mDividerHeight的分割线做准备用的
        //set方法作用：将矩形的坐标设置为指定的值
        outRect.set(0, 0, 0, mDividerHeight);
        RefreshLogUtils.d("RecycleViewItemLine-------"+"getItemOffsets");
    }

    /**
     * 绘制分割线
     * ItemDecoration的onDraw方法是在RecyclerView的onDraw方法中调用的
     * 注意这时候传入的canvas是RecyclerView的canvas，要时刻注意这点，它是和RecyclerView的边界是一致的。
     * 这个时候绘制的内容相当于背景，会被item覆盖。
     * @param c                         canvas用来绘制的画板
     * @param parent                    recyclerView
     * @param state                     状态，用的很少
     */
    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent,
                       @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
        RefreshLogUtils.d("RecycleViewItemLine-------"+"onDraw");
    }

    /**
     * 绘制分割线
     * ItemDecoration的onDrawOver方法是在RecyclerView的draw方法中调用的
     * 同样传入的是RecyclerView的canvas，这时候onLayout已经调用，所以此时绘制的内容会覆盖item。
     * @param c                         canvas用来绘制的画板
     * @param parent                    recyclerView
     * @param state                     状态，用的很少
     */
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent,
                           @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        RefreshLogUtils.d("RecycleViewItemLine-------"+"onDrawOver");
    }

    /**
     * 绘制横向 item 分割线
     */
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        RefreshLogUtils.d("小杨逗比左右的间距分别是" + left + "----"+right);
        //获取的当前显示的view的数量，并不会获取不显示的view的数量。
        //假如recyclerView里共有30条数据，而当前屏幕内显示的只有5条，这paren.getChildCount的值是5，不是30。
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            //获取索引i处的控件view
            final View child = parent.getChildAt(i);
            //拿到layoutParams属性
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)
                    child.getLayoutParams();
            final int top = child.getBottom() + layoutParams.bottomMargin;
            final int bottom = top + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            //使用画笔paint进行绘制
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

    /**
     * 绘制纵向 item 分割线
     */
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)
                    child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + mDividerHeight;
            if (mDivider != null) {
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }

}
