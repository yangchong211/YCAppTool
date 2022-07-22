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
package com.yc.reddot;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TabWidget;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.tabs.TabLayout;

/**
 * @author: 杨充
 * @email : yangchong211@163.com
 * @blog : https://github.com/yangchong211/YCWidgetLib
 * @time : 2017/05/13
 * @desc : 自定义红点视图，用在清科公司投资界，新芽；以及大搜车多个项目中
 * @revise :
 */
public class YCRedDotView extends AppCompatTextView {

    /**
     * 是否隐藏红点上的数字
     */
    private boolean mHideOnNull = true;
    private static final String ZERO = "0";

    public YCRedDotView(Context context) {
        this(context, null);
    }

    public YCRedDotView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public YCRedDotView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 重新父类setText方法
     * @param text                  text
     * @param type                  type
     */
    @Override
    public void setText(CharSequence text, BufferType type) {
        if (isHideOnNull()) {
            if (text == null || text.toString().equalsIgnoreCase(ZERO)){
                setVisibility(View.GONE);
            }else {
                setVisibility(View.VISIBLE);
            }
        } else {
            setVisibility(View.VISIBLE);
        }
        super.setText(text, type);
    }

    /**
     * 初始化view
     * 1.设置布局属性
     */
    private void initView() {
        setLayoutParams();
        setTextView();
        setDefaultValues();
    }

    private void setLayoutParams() {
        if(!(getLayoutParams() instanceof LayoutParams)){
            LayoutParams lParams = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.END | Gravity.TOP);
            setLayoutParams(lParams);
        }
    }

    private void setTextView() {
        setTextColor(Color.WHITE);
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        setPadding(dip2Px(5), dip2Px(1), dip2Px(5), dip2Px(1));
        setBackground(9, Color.parseColor("#f14850"));
        setGravity(Gravity.CENTER);
    }

    private void setDefaultValues() {
        setHideNull(true);
        setBadgeCount(0);
    }

    /**
     * 设置背景颜色
     * @param dipRadius                 半径
     * @param badgeColor                颜色
     */
    private void setBackground(int dipRadius, int badgeColor) {
        int radius = dip2Px(dipRadius);
        float[] radiusArray = new float[]{radius, radius, radius, radius,
                radius, radius, radius, radius};
        RoundRectShape roundRect = new RoundRectShape(radiusArray, null, null);
        ShapeDrawable bgDrawable = new ShapeDrawable(roundRect);
        bgDrawable.getPaint().setColor(badgeColor);
        setBackground(bgDrawable);
    }

    public void setHideNull(boolean hideOnNull) {
        mHideOnNull = hideOnNull;
        setText(getText());
    }

    public boolean isHideOnNull() {
        return mHideOnNull;
    }

    /**
     * 设置红点的数字
     * @param count                 数字
     */
    public void setBadgeCount(int count) {
        //设置参数，超过99显示99+
        setBadgeCount(count > 99 ? "99+" : String.valueOf(count));
    }

    private void setBadgeCount(String count) {
        setText(count);
    }

    /**
     * 设置小红点，不设置数字
     */
    public void setBadgeView(int dipRadius) {
        setText("");
        setWidth(dip2Px(dipRadius));
        setHeight(dip2Px(dipRadius));
        setBackground(9, Color.parseColor("#f14850"));
    }

    /**
     * 设置支持TabWidget控件
     * @param target                TabWidget
     * @param tabIndex              索引
     */
    public void setTargetView(TabWidget target, int tabIndex) {
        View tabView = target.getChildTabViewAt(tabIndex);
        setTargetView(tabView);
    }

    /**
     * 设置支持tabLayout控件
     * @param target                TabLayout
     * @param tabIndex              索引
     */
    public void setTargetView(TabLayout target, int tabIndex) {
        TabLayout.Tab tabAt = target.getTabAt(tabIndex);
        View customView = null;
        if (tabAt != null) {
            customView = tabAt.getCustomView();
        }
        setTargetView(customView);
    }

    /**
     * 设置红点依附的view
     * @param view                  view
     */
    public void setTargetView(View view){
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
        if (view == null) {
            return;
        }
        if(view.getParent() instanceof FrameLayout){
            ((FrameLayout) view.getParent()).addView(this);
        }else if(view.getParent() instanceof ViewGroup){
            ViewGroup parentContainer = (ViewGroup) view.getParent();
            int groupIndex = parentContainer.indexOfChild(view);
            parentContainer.removeView(view);

            FrameLayout badgeContainer = new FrameLayout(getContext());
            ViewGroup.LayoutParams parentLayoutParams = view.getLayoutParams();
            badgeContainer.setLayoutParams(parentLayoutParams);
            view.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            parentContainer.addView(badgeContainer, groupIndex, parentLayoutParams);
            badgeContainer.addView(view);
            badgeContainer.addView(this);
        }else {
            Log.e(getClass().getSimpleName(), "ParentView is must needed");
        }
    }

    /**
     * 设置红点位置
     * @param gravity               位置
     */
    public void setRedHotViewGravity(int gravity){
        /*ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof FrameLayout.LayoutParams){
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
            params.gravity = gravity;
            setLayoutParams(params);
        }else if(layoutParams instanceof LinearLayout.LayoutParams){
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
            params.gravity = gravity;
            setLayoutParams(params);
        }*/
        LayoutParams params = (LayoutParams) getLayoutParams();
        params.gravity = gravity;
        setLayoutParams(params);
    }

    /**
     * 设置红点属性
     * @param dipMargin             margin值
     */
    public void setBadgeMargin(int dipMargin) {
        setBadgeMargin(dipMargin, dipMargin, dipMargin, dipMargin);
    }

    /**
     * 设置红点的margin属性
     * @param leftDipMargin         左边margin
     * @param topDipMargin          上边margin
     * @param rightDipMargin        右边margin
     * @param bottomDipMargin       下边margin
     */
    public void setBadgeMargin(int leftDipMargin, int topDipMargin,
                               int rightDipMargin, int bottomDipMargin) {
        LayoutParams params = (LayoutParams) getLayoutParams();
        params.leftMargin = dip2Px(leftDipMargin);
        params.topMargin = dip2Px(topDipMargin);
        params.rightMargin = dip2Px(rightDipMargin);
        params.bottomMargin = dip2Px(bottomDipMargin);
        setLayoutParams(params);
    }


    /**
     * 获取小红点的数量
     * @return                      数量
     */
    public int getBadgeCount() {
        if (getText() == null) {
            return 0;
        }
        String text = getText().toString();
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int dip2Px(float dip) {
        return (int) (dip * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

}
