package com.yc.other.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yc.serialtasklib.AppendMode;
import com.yc.serialtasklib.SerialTaskQueue;
import com.yc.serialtasklib.AbsTask;
import com.yc.other.R;

import java.util.Timer;
import java.util.TimerTask;

public class SerialTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private SerialTaskQueue serialTaskQueue = new SerialTaskQueue();
    private Timer timer = new Timer();
    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTvMessage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_task);
        mTv1 = findViewById(R.id.tv_1);
        mTv2 = findViewById(R.id.tv_2);
        mTv3 = findViewById(R.id.tv_3);
        mTvMessage = findViewById(R.id.tv_message);

        mTv1.setOnClickListener(this);
        mTv2.setOnClickListener(this);
        mTv3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTv1){
            addTask();
        } else if (v == mTv2){
            startPolling();
        } else if (v == mTv3){
            stopPolling();
        }
    }

    int count = 0;
    private void startPolling() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                count++;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvMessage.setText("count : " + count);
                    }
                });
                addTask();
            }
        },0,1000);
    }

    private void stopPolling() {
        timer.cancel();
    }

    private void addTask() {
        MyTask task = new MyTask();
        serialTaskQueue.append(task, AppendMode.ReplaceStrict);
    }

    class MyTask extends AbsTask {

        @Override
        public void onCancel() {
            Log.i("MyTask: " ," onCancel ");
        }

        @Override
        public void onWorkThread() {
            Log.i("MyTask: " ," onWorkThread ");
            long sleepTime = (long) (Math.random() * 3000 + 1000);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onMainThread() {
            Log.i("MyTask: " ," onMainThread ");
        }
    }

}
