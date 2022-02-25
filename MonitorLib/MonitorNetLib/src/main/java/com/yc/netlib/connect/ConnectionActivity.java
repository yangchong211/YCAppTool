package com.yc.netlib.connect;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.netlib.R;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 测试类
 */
public class ConnectionActivity extends AppCompatActivity {

    private static final String TAG = "ConnectionClass-Sample";
    private ConnectionManager mConnectionClassManager;
    private DeviceBandwidthSampler mDeviceBandwidthSampler;
    private ConnectionChangedListener mListener;
    private TextView mTextView;
    private View mRunningBar;
    private String mURL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1601463169772&di=80c295c40c3c236a6434a5c66cb84c41&imgtype=0&src=http%3A%2F%2Fimg1.kchuhai.com%2Felite%2F20200324%2Fhead20200324162648.jpg";
    /**
     * 尝试的次数
     */
    private int mTries = 0;
    private ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_con);
        initConnection();
        initFindViewById();
    }

    private void initConnection() {
        mConnectionClassManager = ConnectionManager.getInstance();
        mDeviceBandwidthSampler = DeviceBandwidthSampler.getInstance();
        mListener = new ConnectionChangedListener();
    }

    private void initFindViewById() {
        findViewById(R.id.test_btn).setOnClickListener(testButtonClicked);
        mTextView = findViewById(R.id.connection_class);
        mTextView.setText(mConnectionClassManager.getCurrentBandwidthQuality().toString());
        mRunningBar = findViewById(R.id.runningBar);
        mRunningBar.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mConnectionClassManager.remove(mListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnectionClassManager.register(mListener);
    }

    /**
     * 侦听器在connectionclass更改时更新UI
     */
    private class ConnectionChangedListener implements ConnectionStateChangeListener {

        @Override
        public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
            mConnectionClass = bandwidthState;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextView.setText(mConnectionClass.toString());
                }
            });
        }
    }

    private final View.OnClickListener testButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DownloadImage().execute(mURL);
        }
    };

    @SuppressLint("StaticFieldLeak")
    private class DownloadImage extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            //开始检测网络速度
            mDeviceBandwidthSampler.startSampling();
            mRunningBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... url) {
            String imageURL = url[0];
            try {
                URLConnection connection = new URL(imageURL).openConnection();
                connection.setUseCaches(false);
                connection.connect();
                InputStream input = connection.getInputStream();
                try {
                    byte[] buffer = new byte[1024];
                    // Do some busy waiting while the stream is open.
                    while (input.read(buffer) != -1) {
                    }
                } finally {
                    input.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error while downloading image.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            //停止采集
            mDeviceBandwidthSampler.stopSampling();
            if (mConnectionClass == ConnectionQuality.UNKNOWN && mTries < 10) {
                mTries++;
                //重新去下载图片
                new DownloadImage().execute(mURL);
            }
            if (!mDeviceBandwidthSampler.isSampling()) {
                mRunningBar.setVisibility(View.GONE);
            }
        }
    }
}
