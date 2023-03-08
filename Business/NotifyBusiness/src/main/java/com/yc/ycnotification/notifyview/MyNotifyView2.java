package com.yc.ycnotification.notifyview;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.yc.ycnotification.R;
import com.yc.notifymessage.CustomNotification;
import com.yc.notifymessage.NotificationView;
import com.yc.toastutils.ToastUtils;

public class MyNotifyView2 extends NotificationView<Void> {

    public MyNotifyView2(@NonNull Activity activity) {
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
        if (id == R.id.btn_click) {
            ToastUtils.showRoundRectToast("点击吐司");
            return true;
        }
        return false;
    }

    @Override
    public void bindNotification(CustomNotification<Void> notification) {
        super.bindNotification(notification);
    }
}
