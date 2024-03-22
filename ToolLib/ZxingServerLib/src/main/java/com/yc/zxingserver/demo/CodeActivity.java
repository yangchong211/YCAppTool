package com.yc.zxingserver.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.yc.zxingserver.R;
import com.yc.zxingserver.tools.BarCodeCreate;
import com.yc.zxingserver.tools.ZxingCodeCreate;


public class CodeActivity extends AppCompatActivity {

    private TextView tvTitle;
    private ImageView ivCode;
    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTv4;
    public static final String KEY_IS_QR_CODE = "isCode";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_activity);
        ivCode = findViewById(R.id.ivCode);
        tvTitle = findViewById(R.id.tvTitle);
        ImageView mIvLeft = findViewById(R.id.ivLeft);
        mTv1 = findViewById(R.id.tv_1);
        mTv2 = findViewById(R.id.tv_2);
        mTv3 = findViewById(R.id.tv_3);
        mTv4 = findViewById(R.id.tv_4);



        boolean isQRCode = getIntent().getBooleanExtra(KEY_IS_QR_CODE,false);
        if(isQRCode){
            tvTitle.setText("二维码");
            createQRCode("杨充");
        }else{
            tvTitle.setText("条形码");
            createBarCode("1234567890");
        }

        mIvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createQRCode("https://github.com/yangchong211/YCBlogs");
            }
        });
        mTv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBarCode("https://github.com/yangchong211/YCBlogs");
            }
        });
        mTv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                    }
                }).start();
            }
        });
    }

    /**
     * 生成二维码
     * @param content
     */
    private void createQRCode(String content){
        new Thread(() -> {
            //生成二维码相关放在子线程里面
            Bitmap logo = BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
            Bitmap bitmap =  ZxingCodeCreate.createQRCode(content,600,logo);
            runOnUiThread(()->{
                //显示二维码
                ivCode.setImageBitmap(bitmap);
            });
        }).start();

    }

    /**
     * 生成条形码
     * @param content
     */
    private void createBarCode(String content){
        new Thread(() -> {
            //生成条形码相关放在子线程里面
            Bitmap bitmap = BarCodeCreate.createBarCode(content, BarcodeFormat.CODE_128,
                    800,200,null,true);
            runOnUiThread(()->{
                //显示条形码
                ivCode.setImageBitmap(bitmap);
            });
        }).start();
    }


}