package com.yc.baseclasslib.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.yc.baseclasslib.BuildConfig;

import java.util.ArrayList;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time   : 2019/5/11
 *     desc   : fragment是否可见
 *     revise : 由于fragment生命周期很复杂
 * </pre>
 */
public class BaseVisibilityFragment extends Fragment implements OnFragmentVisibilityListener, View.OnAttachStateChangeListener {

    /**
     * 关于transaction事物
     * add() 向Activity中添加一个Fragment
     * remove() 从Activity中移除一个Fragment，如果被移除的Fragment没有添加到回退栈，这个Fragment实例将会被销毁
     * replace() 使用另一个Fragment替换当前的，实际上就是remove()然后add()的合体
     * hide() 隐藏当前的Fragment，仅仅是设为不可见，并不会销毁，它只会触发onHiddenChange()方法
     * show() 显示之前隐藏的Fragment，它只会触发onHiddenChange()方法
     *
     * 注意：在add/replace/hide/show以后都要commit其效果才会在屏幕上显示出来
     */

    /**
     * ParentActivity是否可见
     */
    private boolean parentActivityVisible = false;

    /**
     * 是否可见（Activity处于前台、Tab被选中、Fragment被添加、Fragment没有隐藏、Fragment.View已经Attach）
     */
    private boolean visible = false;
    private static final String TAG = "BaseVisibilityFragment";
    private BaseVisibilityFragment localParentFragment;
    private final ArrayList<OnFragmentVisibilityListener> listeners = new ArrayList<>();

    /**
     * 添加fragment是否可见监听listener
     *
     * @param listener listener
     */
    public void addOnVisibilityChangedListener(OnFragmentVisibilityListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    /**
     * 移除fragment是否可见监听listener
     *
     * @param listener listener
     */
    public void removeOnVisibilityChangedListener(OnFragmentVisibilityListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    /**
     * 这个方法会最先调用
     *
     * @param context 上下文
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        info("onAttach");
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof BaseVisibilityFragment) {
            this.localParentFragment = (BaseVisibilityFragment) parentFragment;
            localParentFragment.addOnVisibilityChangedListener(this);
        }
        checkVisibility(true);
    }

    /**
     * 和onAttach方法对应，销毁时调用，在onDestroy之前
     */
    @Override
    public void onDetach() {
        super.onDetach();
        info("onDetach");
        if (localParentFragment != null) {
            localParentFragment.removeOnVisibilityChangedListener(this);
        }
        checkVisibility(false);
    }

    /**
     * 销毁逻辑
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        localParentFragment = null;
    }

    /**
     * 页面由创建(onCreate)到可见，即会调用该方法
     */
    @Override
    public void onResume() {
        super.onResume();
        info("onResume");
        onActivityVisibilityChanged(true);
    }

    /**
     * 页面即将不可见，即会调用该方法
     */
    @Override
    public void onPause() {
        super.onPause();
        info("onPause");
        onActivityVisibilityChanged(false);
    }

    /**
     * ParentActivity可见性改变
     */
    protected void onActivityVisibilityChanged(boolean visible) {
        parentActivityVisible = visible;
        checkVisibility(visible);
    }

    /**
     * fragment创建时调用，注意：此时还不是可见状态
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info("onCreate");
    }

    /**
     * view布局已经经过inflater解析出来后调用
     *
     * @param view               view
     * @param savedInstanceState bundle
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 处理直接 replace 的 case
        view.addOnAttachStateChangeListener(this);
    }

    /**
     * 调用 fragment add hide 的时候回调用这个方法
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        checkVisibility(hidden);
    }

    /**
     * Tab切换时会回调此方法。对于没有Tab的页面，[Fragment.getUserVisibleHint]默认为true。
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        info("setUserVisibleHint = $isVisibleToUser");
        checkVisibility(isVisibleToUser);
    }


    /**
     * 检查可见性是否变化
     *
     * @param expected 可见性期望的值。只有当前值和expected不同，才需要做判断
     */
    private void checkVisibility(boolean expected) {
        if (expected == visible) {
            return;
        }
        boolean parentVisible;
        if (localParentFragment == null) {
            parentVisible = parentActivityVisible;
        } else {
            parentVisible = localParentFragment.isFragmentVisible();
        }
        //调用fragment父类方法判断是否可见
        boolean superVisible = super.isVisible();
        //获取当前fragment是否可见
        boolean hintVisible = getUserVisibleHint();
        boolean visible = parentVisible && superVisible && hintVisible;
        info(String.format(
                "==> checkVisibility = %s  ( parent = %s, super = %s, hint = %s )",
                visible, parentVisible, superVisible, hintVisible));
        if (visible != this.visible) {
            //跟上一次不同，才刷新调用
            this.visible = visible;
            onVisibilityChanged(this.visible);
        }
    }

    /**
     * 可见性改变
     */
    protected void onVisibilityChanged(boolean visible) {
        info("==> onVisibilityChanged = $visible");
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onFragmentVisibilityChanged(visible);
        }
    }

    /**
     * 是否可见（Activity处于前台、Tab被选中、Fragment被添加、Fragment没有隐藏、Fragment.View已经Attach）
     */
    private boolean isFragmentVisible() {
        return visible;
    }

    private void info(String s) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, s);
        }
    }

    /**
     * ParentFragment可见性改变
     */
    @Override
    public void onFragmentVisibilityChanged(boolean visibility) {
        info("onFragmentVisibilityChanged : " + visibility);
        checkVisibility(visibility);
    }

    @Override
    public void onViewAttachedToWindow(View v) {
        info("onViewAttachedToWindow");
        checkVisibility(true);
    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        info("onViewDetachedFromWindow");
        v.removeOnAttachStateChangeListener(this);
        checkVisibility(false);
    }
}
