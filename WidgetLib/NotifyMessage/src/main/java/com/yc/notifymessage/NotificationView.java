package com.yc.notifymessage;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 通知栏 提供通知样式的 View 的类
 *     revise:
 * </pre>
 */
public abstract class NotificationView<T> {

    /**
     * 实际显示的通知的 View
     */
    private View mView;
    private final Activity mActivity;
    /**
     * 通知的配置类
     */
    private CustomNotification<T> mNotification;

    public NotificationView(@NonNull Activity activity) {
        mActivity = activity;
        initView();
    }

    private void initView() {
        int layoutRes = provideLayoutResourceId();
        if (layoutRes == 0) {
            throw new IllegalArgumentException("layout res is illegal!");
        }
        mView = LayoutInflater.from(mActivity).inflate(layoutRes, null);
        setView(mView);
        setClickableViewListener(mView);
    }

    /**
     * view相关操作
     * @param view                  view
     */
    private void setView(View view) {

    }

    public View getView() {
        return mView;
    }

    /**
     * 子类需复写来提供 View 的 layout id
     * @return
     */
    @LayoutRes
    public abstract int provideLayoutResourceId();

    /**
     * 子类复写，来指定那些 View 可以响应 OnClick 事件。
     * @return
     */
    public abstract int[] provideClickableViewArray();

    @CallSuper
    public void bindNotification(CustomNotification<T> notification) {
        this.mNotification = notification;
    }

    public Activity getActivity() {
        return mActivity;
    }

    protected <V extends View> V findViewById(@IdRes int id) {
        if (mView == null) {
            throw new NullPointerException("View is not created!");
        }
        return mView.findViewById(id);
    }

    private void setClickableViewListener(View view) {
        if (view == null) {
            return;
        }
        int[] clickableViewArray = provideClickableViewArray();
        for (int id : clickableViewArray) {
            if (id != 0) {
                setClickListener(view.findViewById(id));
            }
        }
    }

    private void setClickListener(View view) {
        if (view == null) {
            return;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NotificationView.this.onClick(v, v.getId())) {
                    NotificationView.this.hide();
                }
            }
        });
    }

    private void hide() {
        NotificationManager.getInstance().cancel(mNotification.mType);
    }

    /**
     * view 中的点击事件
     * @param view  任意 view 控件
     * @param id    view 的 id
     * @return      点击后是否要收起 Notification view，true：主动收起，false：不主动收起，按照配置自主进行
     */
    protected boolean onClick(View view, int id) {
        return false;
    }
}
