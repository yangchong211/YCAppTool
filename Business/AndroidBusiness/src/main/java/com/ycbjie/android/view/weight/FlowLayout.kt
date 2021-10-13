package com.ycbjie.android.view.weight
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/7/18
 *     desc  : 流式布局
 *     revise:
 * </pre>
 */
open class FlowLayout : ViewGroup {

    //kotlin的class默认是final, 如果想要能被继承，那么需要使用open 修饰，它的设计原则来自于Effective Java
    //这种默认的设计原则，会导致第三方/未完成的sdk的大多数会是不可继承的，
    //从提供者角度来说：它必须保证这个类是足够完整的，从使用者角度来说：这个类必须要提供我需要的所有东西，
    //否则，我必须重写。当然提供者可以为这个类加上open来表示这个类是可以继承的，但是这是另外一种设计思维。

    // 保存每行多少个多少行
    private var viewLines = mutableListOf<MutableList<View>>()
    // 保存高度
    private var totalHeight = mutableListOf<Int>()

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    // 要去测量子View，首先要知道父容器的测量模式
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 布局最后的宽高
        var width = 0
        var height = 0

        // 获取父容器的测量模式
        val widthMode: Int = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode: Int = MeasureSpec.getMode(heightMeasureSpec)

        // 获取父容器的大小
        val widthSize: Int = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize: Int = MeasureSpec.getSize(heightMeasureSpec)

        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            width = widthSize
            height = heightSize
        } else {
            var lineWidth = 0
            var lineHeight = 0
            var views = mutableListOf<View>()

            for (h in 0 until childCount) {
                val child = getChildAt(h)
                // 测量子View
                measureChild(child, widthMeasureSpec, heightMeasureSpec)

                var childWidth = child.measuredWidth
                var childHeight = child.measuredHeight
                val margin = child.layoutParams as MarginLayoutParams

                childWidth += margin.leftMargin + margin.rightMargin
                childHeight += margin.topMargin + margin.bottomMargin

                // 换行
                if (lineWidth + childWidth > widthSize) {

                    viewLines.add(views)
                    totalHeight.add(lineHeight)

                    // 先计算高度
                    height += lineHeight
                    // 然后计算宽度取最大值
                    width = Math.max(width, lineWidth)
                    lineWidth = childWidth
                    lineHeight = childHeight

                    views = ArrayList()
                    views.add(child)

                } else {
                    // 宽度++
                    lineWidth += childWidth
                    // 计算每一行的最高的view
                    lineHeight = Math.max(lineHeight, childHeight)

                    views.add(child)
                }
                if (h == childCount - 1) {
                    width = Math.max(width, lineWidth)
                    height += lineHeight

                    viewLines.add(views)
                    totalHeight.add(lineHeight)
                }
            }
        }

        setMeasuredDimension(width, height)
    }

    // layout 摆放布局
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left: Int
        var top: Int
        var right: Int
        var bottom: Int

        var curTop = 0
        var curLeft = 0

        for (i in 0 until viewLines.size) {
            val views = viewLines[i]
            for (j in 0 until views.size) {
                val view = views[j]
                val margin = view.layoutParams as MarginLayoutParams
                left = curLeft + margin.leftMargin
                top = curTop + margin.topMargin
                right = left + view.measuredWidth
                bottom = top + view.measuredHeight
                view.layout(left, top, right, bottom)
                curLeft += view.measuredWidth + margin.leftMargin + margin.rightMargin
            }
            curLeft = 0
            curTop += totalHeight[i]
        }

        viewLines.clear()
        totalHeight.clear()
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    /**
     * 代码添加items
     */
    fun addItems(views: MutableList<View>) {
        removeAllViews()
        for (i in 0 until views.size) {
            addView(views[i])
        }
        invalidate()
    }


}