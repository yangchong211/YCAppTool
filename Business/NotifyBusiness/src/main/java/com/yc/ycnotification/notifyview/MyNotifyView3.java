package com.yc.ycnotification.notifyview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yc.notifymessage.CustomNotification;
import com.yc.notifymessage.NotificationView;
import com.yc.toastutils.ToastUtils;
import com.yc.ycnotification.NotificationActivity;
import com.yc.ycnotification.R;

public class MyNotifyView3 extends NotificationView<NotificationActivity.Teacher> {

    public MyNotifyView3(@NonNull Activity activity) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public void bindNotification(CustomNotification<NotificationActivity.Teacher> notification) {
        super.bindNotification(notification);
        NotificationActivity.Teacher data = notification.getData();
        TextView title = findViewById(R.id.tv_custom_title);
        if (data!=null){
            title.setText(data.name + data.age);
        }
    }
}
