package com.ycbjie.ycwebview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yc.webviewlib.base.X5WebChromeClient;
import com.yc.webviewlib.base.X5WebViewClient;
import com.yc.webviewlib.inter.DefaultVideoListener;
import com.yc.webviewlib.inter.DefaultWebListener;
import com.yc.webviewlib.utils.X5WebUtils;
import com.yc.webviewlib.view.X5WebView;
import com.yc.webviewlib.widget.WebProgress;
import android.text.TextUtils;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_1_2).setOnClickListener(this);
        findViewById(R.id.tv_1_3).setOnClickListener(this);
        findViewById(R.id.tv_2_1).setOnClickListener(this);
        findViewById(R.id.tv_2_2).setOnClickListener(this);
        findViewById(R.id.tv_2_3).setOnClickListener(this);
        findViewById(R.id.tv_2_4).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_5).setOnClickListener(this);
        findViewById(R.id.tv_5_2).setOnClickListener(this);
        findViewById(R.id.tv_6_1).setOnClickListener(this);
        findViewById(R.id.tv_6_2).setOnClickListener(this);
        findViewById(R.id.tv_7_1).setOnClickListener(this);
        findViewById(R.id.tv_7_2).setOnClickListener(this);
        findViewById(R.id.tv_7_3).setOnClickListener(this);
        findViewById(R.id.tv_8).setOnClickListener(this);
        findViewById(R.id.tv_9).setOnClickListener(this);
        findViewById(R.id.tv_9_2).setOnClickListener(this);
        findViewById(R.id.tv_10).setOnClickListener(this);
        findViewById(R.id.tv_11).setOnClickListener(this);
        findViewById(R.id.tv_12).setOnClickListener(this);
        findViewById(R.id.tv_13).setOnClickListener(this);
        findViewById(R.id.tv_14).setOnClickListener(this);
        findViewById(R.id.tv_14_2).setOnClickListener(this);
        findViewById(R.id.tv_14_3).setOnClickListener(this);
        findViewById(R.id.tv_14_4).setOnClickListener(this);
        findViewById(R.id.tv_14_5).setOnClickListener(this);
        findViewById(R.id.tv_15).setOnClickListener(this);
        findViewById(R.id.tv_16).setOnClickListener(this);

        checkReadPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, 100);
        checkReadPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 101);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_1:
                startActivity(new Intent(this, FirstActivity1.class));
                break;
            case R.id.tv_1_2:
                startActivity(new Intent(this,FirstActivity2.class));
                break;
            case R.id.tv_1_3:
                startActivity(new Intent(this,FirstActivity3.class));
                break;
            case R.id.tv_2_1:
                startActivity(new Intent(this,SecondActivity.class));
                break;
            case R.id.tv_2_2:
                startActivity(new Intent(this,WebViewActivity.class));
                break;
            case R.id.tv_2_3:
                startActivity(new Intent(this, CacheWebViewActivity1.class));
                break;
            case R.id.tv_2_4:
                startActivity(new Intent(this, CacheWebViewActivity2.class));
                break;
            case R.id.tv_3:
                startActivity(new Intent(this,ThreeActivity.class));
                break;
            case R.id.tv_5:
                startActivity(new Intent(this,FiveActivity.class));
                break;
            case R.id.tv_5_2:
                startActivity(new Intent(this,FiveActivity2.class));
                break;
            case R.id.tv_6_1:
                startActivity(new Intent(this,SixActivity.class));
                break;
            case R.id.tv_6_2:
                startActivity(new Intent(this,SixActivity2.class));
                break;
            case R.id.tv_7_1:
                startActivity(new Intent(this,NativeActivity.class));
                break;
            case R.id.tv_7_2:
                startActivity(new Intent(this,NativeActivity2.class));
                break;
            case R.id.tv_7_3:
                startActivity(new Intent(this,NativeActivity3.class));
                break;
            case R.id.tv_8:
                startActivity(new Intent(this,EightActivity.class));
                break;
            case R.id.tv_9:
                startActivity(new Intent(this,FileDisplayActivity.class));
                break;
            case R.id.tv_9_2:
                startActivity(new Intent(this,DeepLinkActivity.class));
                break;
            case R.id.tv_10:
                startActivity(new Intent(this, TenActivity.class));
                break;
            case R.id.tv_11:
                openLink(this,"https://juejin.im/user/5939433efe88c2006afa0c6e/posts");
                break;
            case R.id.tv_12:
                startActivity(new Intent(this, DownActivity.class));
                break;
            case R.id.tv_13:
                startActivity(new Intent(this, ScrollViewActivity.class));
                break;
            case R.id.tv_14:
                startActivity(new Intent(this, VertcalWebActivity.class));
                break;
            case R.id.tv_14_2:
                startActivity(new Intent(this, WebViewActivity2.class));
                break;
            case R.id.tv_14_3:
                startActivity(new Intent(this, ThreeActivity2.class));
                break;
            case R.id.tv_14_4:
                startActivity(new Intent(this, ThreeActivity2.class));
                break;
            case R.id.tv_14_5:
                //使用webView设置白名单操作
                startActivity(new Intent(this, WebViewWhiteActivity.class));
                break;
            case R.id.tv_15:
                startActivity(new Intent(this, WvNativeActivity2.class));
                break;
            case R.id.tv_16:
                startActivity(new Intent(this, WvNativeActivity3.class));
                break;
            default:
                break;
        }
    }

    /**
     * 使用浏览器打开链接
     */
    public void openLink(Context context, String content) {
        if (!TextUtils.isEmpty(content) && content.startsWith("http")) {
            Uri issuesUrl = Uri.parse(content);
            Intent intent = new Intent(Intent.ACTION_VIEW, issuesUrl);
            context.startActivity(intent);
        }
    }


    /**
     * 判断是否有某项权限
     * @param string_permission                 权限
     * @param request_code                      请求码
     * @return
     */
    public boolean checkReadPermission(Context context, String string_permission, int request_code) {
        boolean flag = false;
        int permission = ContextCompat.checkSelfPermission(context, string_permission);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            //已有权限
            flag = true;
        } else {
            //申请权限
            ActivityCompat.requestPermissions((Activity) context, new String[]{string_permission}, request_code);
        }
        return flag;
    }

}
