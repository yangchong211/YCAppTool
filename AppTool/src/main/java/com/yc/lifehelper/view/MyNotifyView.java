package com.yc.lifehelper.view;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

import com.yc.library.base.config.Constant;
import com.yc.library.web.WebViewActivity;
import com.yc.lifehelper.R;
import com.yc.notifymessage.CustomNotification;
import com.yc.notifymessage.NotificationView;

public class MyNotifyView extends NotificationView<Void> {

    public MyNotifyView(@NonNull Activity activity) {
        super(activity);
    }

    @Override
    public int provideLayoutResourceId() {
        return R.layout.notify_custom_view;
    }

    @Override
    public int[] provideClickableViewArray() {
        return new int[]{R.id.btn_click};
    }

    @Override
    protected boolean onClick(View view, int id) {
        switch (id) {
            case R.id.btn_click:
                WebViewActivity.lunch(getActivity(), Constant.GITHUB,"我的GitHub");
                return true;
            default:
                return false;
        }
    }

    @Override
    public void bindNotification(CustomNotification<Void> notification) {
        super.bindNotification(notification);
    }

}
