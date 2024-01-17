package com.yc.jnidemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.yc.calljni.CallNativeLib;
import com.yc.safetyjni.SafetyJniLib;
import com.yc.signalhooker.ILogger;
import com.yc.signalhooker.ISignalListener;
import com.yc.signalhooker.SigQuitHooker;
import com.yc.testjnilib.NativeLib;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv2;
    private TextView tv4;
    private TextView tv5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //initSignal();
    }

    private void initSignal() {
        SigQuitHooker.initSignalHooker();
        SigQuitHooker.setSignalListener(new ISignalListener() {
            @Override
            public void onReceiveAnrSignal() {
                Log.d("SigQuitHooker: " , "onReceiveAnrSignal: do" );
            }
        });
        SigQuitHooker.setLogger(new ILogger() {
            @Override
            public void onPrintLog(String message) {
                Log.d("SigQuitHooker: " , "message: " + message);
            }
        });
    }

    private void init() {
        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        tv2 = findViewById(R.id.tv_2);
        tv4 = findViewById(R.id.tv_4);
        tv5 = findViewById(R.id.tv_5);
        tv5.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_1:
                String fromJNI = NativeLib.getInstance().stringFromJNI();
                String md5 = NativeLib.getInstance().getMd5("yc");
                NativeLib.getInstance().initLib("db");
                String stringFromJNI = SafetyJniLib.getInstance().stringFromJNI();
                String nameFromJNI = NativeLib.getInstance().getNameFromJNI();
                tv2.setText("java调用c/c++：" + nameFromJNI);
                break;
            case R.id.tv_3:
                CallNativeLib.getInstance().callJavaField("com/yc/calljni/HelloCallBack","name");
                CallNativeLib.getInstance().callJavaMethod("com/yc/calljni/HelloCallBack","updateName");
                tv4.setText("这个就看日志打印");
                break;
            case R.id.tv_5:
                startActivity(new Intent(this, CrashActivity.class));
                break;
            default:
                break;
        }
    }
}
