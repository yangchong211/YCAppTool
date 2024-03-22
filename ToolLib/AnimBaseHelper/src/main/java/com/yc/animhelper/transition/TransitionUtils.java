package com.yc.animhelper.transition;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.WindowManager;

import com.yc.animhelper.R;


/**
 * <pre>
 *     @author : yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/8/11
 *     desc   : 工具类
 *     revise :
 * </pre>
 */
public final class TransitionUtils {

    /**
     * 获取悬浮窗源View的数据
     *
     * @param sourceView        目标view
     * @return                  TransitionParam对象
     */
    public static TransitionParam getSourceViewParam(View sourceView , WindowManager.LayoutParams layoutParams) {
        if (sourceView == null || layoutParams == null) {
            throw new NullPointerException("source view or layoutParams is null");
        } else {
            //获取目标view的宽和高
            int width = sourceView.getMeasuredWidth();
            int height = sourceView.getMeasuredHeight();
            if (width > 0 && height > 0) {
                //宽高都有效，然后设置转场动画的属性
                TransitionParam animBean = new TransitionParam();
                animBean.width = width;
                animBean.height = height;

                int width1 = layoutParams.width;
                int height1 = layoutParams.height;
                int x = layoutParams.x;
                int y = layoutParams.y;

                animBean.left = x;
                animBean.right = x + width1;
                animBean.top = y;
                animBean.bottom = y + height1;
                return animBean;
            }
        }
        return null;
    }


    /**
     * 获取源View的数据
     *
     * @param sourceView        目标view
     * @return                  TransitionParam对象
     */
    public static TransitionParam getSourceViewParam(View sourceView) {
        if (sourceView == null) {
            throw new NullPointerException("source view is null");
        } else {
            //获取目标view的宽和高
            int width = sourceView.getMeasuredWidth();
            int height = sourceView.getMeasuredHeight();
            if (width > 0 && height > 0) {
                //宽高都有效，然后设置转场动画的属性
                TransitionParam animBean = new TransitionParam();
                animBean.width = width;
                animBean.height = height;

                //这个作用是：
                //以屏幕左上角为参考系，判断view是否有一部分仍然在屏幕中（没有被父View遮挡），则会返回true。
                //反之，如果它全部被父View遮挡住或者本身就是不可见的，则会返回false。
                //简单来说就是目标view在父view的映射，然后从屏幕左上角开始计算，保存到 rect 中，注意是父view，而不是屏幕
                Rect visibleRect = new Rect();
                sourceView.getGlobalVisibleRect(visibleRect);
                //getLocalVisibleRect(rect) 也是很好理解的，只不过这个方法是以 view 的左上角为参考系。
                //只要这个View的左上角在屏幕中，它的LocalVisibleRect的左上角坐标就一定是(0,0)，
                //如果View的右下角在屏幕中，它的LocalVisibleRect右下角坐标就一定是(view.getWidth(), view.getHeight())。
                //sourceView.getLocalVisibleRect(visibleRect);

                animBean.left = visibleRect.left;
                animBean.right = visibleRect.right;
                animBean.top = visibleRect.top;
                animBean.bottom = visibleRect.bottom;
                return animBean;
            }
        }
        return null;
    }


    public static void finishTransition(Activity activity) {
        if (activity != null) {
            activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

}
