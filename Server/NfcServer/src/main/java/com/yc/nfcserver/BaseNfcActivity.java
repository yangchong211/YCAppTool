package com.yc.nfcserver;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


/**
 * Description:
 * 子类在onNewIntent方法中进行NFC标签相关操作。
 * launchMode设置为singleTop或singelTask，保证Activity的重用唯一
 * 在onNewIntent方法中执行intent传递过来的Tag数据
 */
public class BaseNfcActivity extends AppCompatActivity {

    /**
     * 使用前台调度系统
     * 借助前台调度系统，Activity 可以拦截 Intent 并声明自己可优先于其他 Activity 处理同一 Intent。
     * 使用此系统涉及为 Android 系统构造一些数据结构，以便将合适的 Intent 发送到您的应用。
     *
     * 具体实践步骤
     * 1.创建一个 PendingIntent 对象，这样 Android 系统会使用扫描到的标签的详情对其进行填充
     * 2.替换以下 Activity 生命周期回调，并添加相应逻辑，以分别在 Activity 失去 (onPause()) 焦点和重新获得 (onResume()) 焦点时启用和停用前台调度。
     *  enableForegroundDispatch() 必须从主线程调用，并且只能在 Activity 在前台运行时调用（在 onResume() 中调用可确保这一点）。
     *  您还需要实现 onNewIntent 回调以处理扫描到的 NFC 标签中的数据。
     */
    protected static final String TAG = "Nfc------";
    protected NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] intentFiltersArray;
    private String[][] techListsArray;

    /**
     * 启动Activity，创建
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //此处adapter需要重新获取，否则无法获取message
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //一旦截获NFC消息，就会通过PendingIntent调用窗口
        //1.创建一个 PendingIntent 对象，这样 Android 系统会使用扫描到的标签的详情对其进行填充
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Log.e(TAG,"onCreate----mNfcAdapter创建-");
        //https://developer.android.google.cn/guide/topics/connectivity/nfc/advanced-nfc#foreground-dispatch
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            /* Handles all MIME based dispatches. You should specify only the ones that you need. */
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }
        intentFiltersArray = new IntentFilter[] {ndef, };
        techListsArray = new String[][] { new String[] { NfcF.class.getName() } };
    }

    /**
     * 获得焦点，按钮可以点击
     */
    @Override
    public void onResume() {
        super.onResume();
        //设置处理优于所有其他NFC的处理
        if (mNfcAdapter != null) {
            //有nfc功能
            if (mNfcAdapter.isEnabled()) {
                //nfc功能打开了
                //隐式启动
                Log.e(TAG,"onResume----mNfcAdapter-隐式启动-");
                //mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
                mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, intentFiltersArray, techListsArray);
            } else {
                Log.e(TAG,"onResume----mNfcAdapter-请打开nfc功能-");
                Toast.makeText(BaseNfcActivity.this, "请打开nfc功能", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * 暂停Activity，界面获取焦点，按钮可以点击
     */
    @Override
    public void onPause() {
        super.onPause();
        //恢复默认状态
        if (mNfcAdapter != null){
            Log.e(TAG,"onResume----mNfcAdapter-恢复默认状态-");
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

}

