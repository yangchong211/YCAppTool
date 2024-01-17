package com.ycbjie.ycwebview;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.toastutils.ToastUtils;


public class SplashActivity extends AppCompatActivity {

    private EditText mEt;
    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTv4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mEt = findViewById(R.id.et);
        mTv1 = findViewById(R.id.tv_1);
        mTv2 = findViewById(R.id.tv_2);
        mTv3 = findViewById(R.id.tv_3);
        mTv4 = findViewById(R.id.tv_4);

//        Intent intent = new Intent(SplashActivity.this,WebViewActivity.class);
//        intent.putExtra("url","http://tongbuxueht.zhugexuetang.com/douzhanggui");
//        intent.putExtra("hide",true);
//        startActivity(intent);
//        finish();

        mTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable text = mEt.getText();
                if (text==null || text.toString()==null || text.toString().trim()==null || text.toString().length()==0){
                    ToastUtils.showRoundRectToast("输入地址不能为空");
                    return;
                }
                String url = text.toString().trim();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    ToastUtils.showRoundRectToast("输入地址需要是http或者https开头");
                    return;
                }
                Intent intent = new Intent(SplashActivity.this,WebViewActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("hide",false);
                startActivity(intent);
            }
        });
        mTv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }
        });
        mTv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable text = mEt.getText();
                if (text==null || text.toString()==null || text.toString().trim()==null || text.toString().length()==0){
                    ToastUtils.showRoundRectToast("输入地址不能为空");
                    return;
                }
                String url = text.toString().trim();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    ToastUtils.showRoundRectToast("输入地址需要是http或者https开头");
                    return;
                }
                Intent intent = new Intent(SplashActivity.this,WvNativeActivity.class);
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });
        mTv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable text = mEt.getText();
                if (text==null || text.toString()==null || text.toString().trim()==null || text.toString().length()==0){
                    ToastUtils.showRoundRectToast("输入地址不能为空");
                    return;
                }
                String url = text.toString().trim();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    ToastUtils.showRoundRectToast("输入地址需要是http或者https开头");
                    return;
                }
                Intent intent = new Intent(SplashActivity.this,WebViewActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("hide",true);
                startActivity(intent);
            }
        });
    }


}
