package com.yc.widgetbusiness.event;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.toastutils.ToastUtils;
import com.yc.widgetbusiness.R;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_view);

        findViewById(R.id.ll_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("我的容器1");
            }
        });
        findViewById(R.id.rl_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("我的容器2");
            }
        });
        findViewById(R.id.tv_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("我的文本");
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("touch_event_test", "Activity dispatchTouchEvent return super");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("touch_event_test", "Activity onTouchEvent return super");
        return super.onTouchEvent(event);
    }

}
