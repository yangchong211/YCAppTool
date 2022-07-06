package com.yc.appclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yc.appservice.ICheckAppInfoManager;
import com.yc.toolutils.AppSignUtils;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/05/29
 *     desc  : 设置页面
 *     revise:
 * </pre>
 */
public class SetAppActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEtTokenValue;
    private TextView mTvSetToken;
    private EditText mEtChannelValue;
    private TextView mTvSetChannel;
    private EditText mEtAuthorValue;
    private TextView mTvSetAuthor;

    //由AIDL文件生成的Java类
    private ICheckAppInfoManager messageCenter = null;
    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean mBound = false;


    private enum SET_TYPE{
        SET_TOKEN,SET_CHANNEL,SET_AUTHOR
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            Log.e("SetAppActivity","绑定");
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound || connection!=null) {
            Log.e("SetAppActivity","解除");
            unbindService(connection);
            mBound = false;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_app);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        mEtTokenValue = (EditText) findViewById(R.id.et_token_value);
        mTvSetToken = (TextView) findViewById(R.id.tv_set_token);
        mEtChannelValue = (EditText) findViewById(R.id.et_channel_value);
        mTvSetChannel = (TextView) findViewById(R.id.tv_set_channel);
        mEtAuthorValue = (EditText) findViewById(R.id.et_author_value);
        mTvSetAuthor = (TextView) findViewById(R.id.tv_set_author);

    }

    private void initListener() {
        mTvSetToken.setOnClickListener(this);
        mTvSetChannel.setOnClickListener(this);
        mTvSetAuthor.setOnClickListener(this);
    }

    private void initData() {

    }

    /**
     * 跨进程绑定服务
     */
    private void attemptToBindService() {
        Intent intent = new Intent();
        //通过Intent指定服务端的服务名称和所在包，与远程Service进行绑定
        //参数与服务器端的action要一致,即"服务器包名.aidl接口文件名"
        intent.setAction("cn.ycbjie.ycaudioplayer.service.aidl.AppInfoService");
        //Android5.0后无法只通过隐式Intent绑定远程Service
        //需要通过setPackage()方法指定包名
        intent.setPackage(ClientMainActivity.PACK_NAME);
        //绑定服务,传入intent和ServiceConnection对象
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }


    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(getLocalClassName(), "完成绑定aidlServer的AIDLService服务");
            messageCenter = ICheckAppInfoManager.Stub.asInterface(service);
            mBound = true;
            if (messageCenter != null) {
                try {
                    //链接成功
                    Toast.makeText(SetAppActivity.this,"链接成功",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(getLocalClassName(), "无法绑定aidlServer的AIDLService服务");
            mBound = false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_set_token:
                String token = mEtTokenValue.getText().toString();
                if(TextUtils.isEmpty(token)){
                    Toast.makeText(this,"token不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                setMessage(SET_TYPE.SET_TOKEN,token);
                break;
            case R.id.tv_set_channel:
                String setChannel = mEtChannelValue.getText().toString();
                if(TextUtils.isEmpty(setChannel)){
                    Toast.makeText(this,"渠道不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                setMessage(SET_TYPE.SET_CHANNEL,setChannel);
                break;
            case R.id.tv_set_author:
                String setAuthor = mEtAuthorValue.getText().toString();
                if(TextUtils.isEmpty(setAuthor)){
                    Toast.makeText(this,"作者不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                setMessage(SET_TYPE.SET_AUTHOR,setAuthor);
                break;
            default:
                break;
        }
    }


    /**
     * 调用服务端的addInfo方法
     */
    public void setMessage(SET_TYPE type,String content) {
        //如果与服务端的连接处于未连接状态，则尝试连接
        if (!mBound) {
            attemptToBindService();
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (messageCenter == null){
            Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isSuccess = false;
        try {
            switch (type){
                case SET_TOKEN:
                    isSuccess=messageCenter.setToken(AppSignUtils.getSign(ClientMainActivity.PACK_NAME),content);
                    break;
                case SET_CHANNEL:
                    isSuccess=messageCenter.setChannel(AppSignUtils.getSign(ClientMainActivity.PACK_NAME),content);
                    break;
                case SET_AUTHOR:
                    isSuccess=messageCenter.setAppAuthorName(AppSignUtils.getSign(ClientMainActivity.PACK_NAME),content);
                    break;
            }
            if(isSuccess){
                Toast.makeText(this,"设置成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"设置失败",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
