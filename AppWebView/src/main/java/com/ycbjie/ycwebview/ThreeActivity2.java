package com.ycbjie.ycwebview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.yc.webviewlib.client.JsX5WebViewClient;
import com.yc.webviewlib.view.X5WebView;


public class ThreeActivity2 extends AppCompatActivity {

    private X5WebView webView;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                //退出网页
                webView.goBack();
                return true;
            }else {
                handleFinish();
            }
        }
        return false;
    }

    public void handleFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        try {
            if (webView != null) {
                webView.destroy();
                webView = null;
            }
        } catch (Exception e) {
            Log.e("X5WebViewActivity", e.getMessage());
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yc_web_view);
        webView = findViewById(R.id.web_view);

        String url = "file:///android_asset/editer/index.html";
        webView.loadUrl(url);
    }

    private class YcX5WebViewClient extends JsX5WebViewClient {
        /**
         * 构造方法
         *
         * @param webView 需要传进来webview
         * @param context 上下文
         */
        public YcX5WebViewClient(X5WebView webView, Context context) {
            super(webView, context);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("网络拦截--------1------", url);
            //tel:协议---拨打电话
            if (url.startsWith("tel:")) {
                //直接调出界面，不需要权限
                Intent sendIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(sendIntent);
                //或者
                //直接拨打，需要权限<uses-permission android:name="android.permission.CALL_PHONE"/>
                //Intent sendIntent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
                //startActivity(sendIntent);
                //否则键盘回去，页面显示"找不到网页"
                return true;
            } else if (url.startsWith("sms:") || url.startsWith("smsto:") || url.startsWith("mms:") || url.startsWith("mmsto:")) {
                //直接调出界面，不需要权限
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(sendIntent);

                //或者
                //打开短信页面，不需要权限
                //Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                //startActivity(sendIntent);

                //或者
                //import android.telephony.SmsManager;
                //SmsManager smsg = SmsManager.getDefault();//----看不到已发送信息。。。
                //smsg.sendTextMessage("10086", null, "tttttt", null, null);

                //或者
                //---可以看到已发的信息
                //ContentValues values = new ContentValues();
                //values.put("address", "10086");
                //values.put("body", "contents");
                //ContentResolver contentResolver = getContentResolver();
                //contentResolver.insert(Uri.parse("content://sms/sent"), values);
                // contentResolver.insert(Uri.parse("content://sms/inbox"), values);
                //<uses-permission android:name="android.permission.SEND_SMS"/>
                //<uses-permission android:name="android.permission.READ_SMS"/>
                //<uses-permission android:name="android.permission.WRITE_SMS"/>
                //否则键盘回去，页面显示"找不到网页"
                return true;
            } else if (url.startsWith("mailto:")) {
                //打开发邮件窗口
                Intent mailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                startActivity(mailIntent);
                //<uses-permission android:name="android.permission.SEND_TO"/>
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d("网络拦截--------2------",request.getUrl().toString());
            String url = request.getUrl().toString();
            //tel:协议---拨打电话
            if(url.startsWith("tel:")) {
                //直接调出界面，不需要权限
                Intent sendIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(sendIntent);
                //或者
                //直接拨打，需要权限<uses-permission android:name="android.permission.CALL_PHONE"/>
                //Intent sendIntent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
                //startActivity(sendIntent);
                //否则键盘回去，页面显示"找不到网页"
                return true;
            } else if(url.startsWith("sms:")||url.startsWith("smsto:")||url.startsWith("mms:")||url.startsWith("mmsto:")) {
                //直接调出界面，不需要权限
                Intent sendIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(sendIntent);

                //或者
                //打开短信页面，不需要权限
                //Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                //startActivity(sendIntent);

                //或者
                //import android.telephony.SmsManager;
                //SmsManager smsg = SmsManager.getDefault();//----看不到已发送信息。。。
                //smsg.sendTextMessage("10086", null, "tttttt", null, null);

                //或者
                //---可以看到已发的信息
                //ContentValues values = new ContentValues();
                //values.put("address", "10086");
                //values.put("body", "contents");
                //ContentResolver contentResolver = getContentResolver();
                //contentResolver.insert(Uri.parse("content://sms/sent"), values);
                // contentResolver.insert(Uri.parse("content://sms/inbox"), values);
                //<uses-permission android:name="android.permission.SEND_SMS"/>
                //<uses-permission android:name="android.permission.READ_SMS"/>
                //<uses-permission android:name="android.permission.WRITE_SMS"/>
                //否则键盘回去，页面显示"找不到网页"
                return true;
            } else if (url.startsWith("mailto:")) {
                try {
                    //打开发邮件窗口
                    Intent mailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                    startActivity(mailIntent);
                } catch (Exception e){
                    e.printStackTrace();
                }
                //<uses-permission android:name="android.permission.SEND_TO"/>
                return true;
            }
            return super.shouldOverrideUrlLoading(view, request);
        }
    }

}
