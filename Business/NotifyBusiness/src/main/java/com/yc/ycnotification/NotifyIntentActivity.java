package com.yc.ycnotification;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.yc.ycnotification.R;


/**
 * Created by PC on 2017/12/7.
 * 作者：PC
 */

public class NotifyIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_test);
        TextView tv = (TextView) findViewById(R.id.tv);
        int what = getIntent().getIntExtra("what", 0);
        switch (what){
            case 1:
                tv.setText("2.在Activity中发送通知1，最简单的通知");
                break;
            case 2:
                tv.setText("3.在Activity中发送通知2，添加action");
                break;
            case 3:
                tv.setText("4.在Activity中发送通知3，设置通知栏左右滑动不删除");
                break;
            case 4:
                tv.setText("点击通知栏的根容器时要执行的意图");
                break;
            case 5:
                tv.setText("在Activity中发送通知");
                break;
            case 6:
                tv.setText("在广播中发送通知");
                break;
            case 7:
                tv.setText("在广播中发送通知");
                break;
            case 8:

                break;
            case 9:
                tv.setText("9.在Activity中发通知，设置通知不能被状态栏的清除按钮给清除掉,也不能被手动清除,但能通过 cancel() 方法清除");
                break;
            case 10:
                tv.setText("10.在Activity中发通知，设置用户单击通知后自动消失");
                break;
            case 11:
                tv.setText("上一首");
                break;
            case 12:
                tv.setText("下一首");
                break;
            case 13:
                tv.setText("播放暂停");
                break;
            case 14:
                tv.setText("点击通知栏的根容器时要执行的意图");
                break;
            default:
                break;
        }
    }
}
