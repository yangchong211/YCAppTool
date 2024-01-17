package com.yc.appgrpc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GreeterActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.toString();
    private TextView text_info;
    // 服务端和客户端
    private GRPCClient grpcClient;

    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, GreeterActivity.class);
            target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeter);
        // 初始化控件
        initView();
        // 注册广播接收器
        register();
        // 初始化服务端和客户端
        grpcClient = new GRPCClient(GreeterActivity.this);
        GRPCServer grpcServer = new GRPCServer(GreeterActivity.this);
    }
    // 初始化控件
    private void initView() {
        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(v -> grpcClient.simpleHello());
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> grpcClient.clientStream());
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(v -> grpcClient.serverStream());
        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(v -> grpcClient.bothFlowStream());
        text_info = findViewById(R.id.text_info);
        text_info.setMovementMethod(new ScrollingMovementMethod());
    }

    // 注册广播更新ui
    private void register() {
        // 注册一个广播用于更新ui
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "广播收到消息" + intent.getStringExtra("info"));
                text_info.append(intent.getStringExtra("info") + "\n");
            }
        };
        IntentFilter filter = new IntentFilter("main.info");
        registerReceiver(broadcastReceiver, filter);
    }

}
