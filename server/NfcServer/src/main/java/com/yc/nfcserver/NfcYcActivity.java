package com.yc.nfcserver;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class NfcYcActivity extends BaseNfcActivity {

    private TextView tvNFCMessage;
    private Button btnClean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_scan);
        btnClean = findViewById(R.id.btn_clean);
        tvNFCMessage = findViewById(R.id.tv_show_nfc);
        resolveIntent(getIntent());
        Log.e(TAG, "onCreate: ");
        if (mNfcAdapter == null) {
            Toast.makeText(NfcYcActivity.this, "您的手机不支持nfc", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNFCMessage.setText("");
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e(TAG, "onNewIntent: ");
        setIntent(intent);
        if (mNfcAdapter != null) {
            //有nfc功能
            if (mNfcAdapter.isEnabled()) {
                //nfc功能打开了
                resolveIntent(getIntent());
            } else {
                Toast.makeText(NfcYcActivity.this, "请打开nfc功能", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    //初次判断是什么类型的NFC卡
    private void resolveIntent(Intent intent) {
        NdefMessage[] msgs = NfcUtil.getNdefMsg(intent); //重点功能，解析nfc标签中的数据
        Log.e(TAG, "resolveIntent: "+msgs);
        if (msgs == null) {
            Toast.makeText(NfcYcActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            setNFCMsgView(msgs);
        }

    }

    /**
     * 显示扫描后的信息
     *
     * @param ndefMessages ndef数据
     */
    @SuppressLint("SetTextI18n")
    private void setNFCMsgView(NdefMessage[] ndefMessages) {
        try{
            if (ndefMessages == null || ndefMessages.length == 0) {
                Toast.makeText(NfcYcActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            tvNFCMessage.setText("");
            List<InterNdefRecord> records = NdefMessageParser.parse(ndefMessages[0]);
            final int size = records.size();
            for (int i = 0; i < size; i++) {
                InterNdefRecord record = records.get(i);
                tvNFCMessage.append(record.getViewText());
            }
            if (!TextUtils.isEmpty(tvNFCMessage.getText().toString())){
                Log.e(TAG, "setNFCMsgView: "+tvNFCMessage.getText().toString());
                if (NfcUtil.isHttpUrl(tvNFCMessage.getText().toString())){
                    startBrowser(tvNFCMessage.getText().toString());
                    finish();
                }
            }
        }catch (Exception ex ){
            finish();
        }
    }

    /**
     * 启动浏览器
     * @param url
     */
    public void startBrowser(String url) {
        if (!TextUtils.isEmpty(url)) {
            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            // 注意此处的判断intent.resolveActivity()可以返回显示该Intent的Activity对应的组件名
            // 官方解释 : Name of the component implementing an activity that can display the intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                final ComponentName componentName = intent.resolveActivity(getPackageManager());
                Log.e(TAG, "componentName = " + componentName.getClassName());
                startActivity(Intent.createChooser(intent, "请选择浏览器"));
            } else {
                Toast.makeText(getApplicationContext(), "没有匹配的程序", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
