package com.yc.networklib.test;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.yc.easyexecutor.DelegateTaskExecutor;
import com.yc.toolutils.system.AppLogUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetTestHelper {

    private static final String PING_ADDRESS = "http://www.qq.com/";

    /**
     * 单例对象
     */
    private volatile static NetTestHelper sInstance;

    /**
     * 单例模式
     *
     * @return ActivityManager对象
     */
    public static NetTestHelper getInstance() {
        if (sInstance == null) {
            synchronized (NetTestHelper.class) {
                if (sInstance == null) {
                    sInstance = new NetTestHelper();
                }
            }
        }
        return sInstance;
    }

    private NetTestHelper() {

    }

    public void testNetwork(OnNetTestListener listener) {
        pingOutside(listener);
    }

    public void pingNetwork(OnNetTestListener listener) {
        DelegateTaskExecutor.getInstance().executeOnCpu(() -> {
            boolean pingBaidu = pingBaidu();
            if (listener != null) {
                listener.onTest(pingBaidu, "");
            }
        });
    }

    /**
     * ping功能，有响应代表成功
     *
     * @param onTestListener 回调
     */
    public void pingOutside(OnNetTestListener onTestListener) {
        get(PING_ADDRESS, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (onTestListener != null) {
                    if (e.toString().contains("UnknownHostException")
                            || e.toString().contains("unreachable")) {
                        onTestListener.onTest(false, "网络不通，请检查网络设置");
                    } else if (e.toString().contains("SocketTimeoutException")) {
                        onTestListener.onTest(false, "网络连接超时，请检查网络设置");
                    } else {
                        onTestListener.onTest(false, "网络检测失败，" + e.getMessage());
                    }
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    if (onTestListener != null) {
                        if (response.body() != null && response.body().string().contains("腾讯")) {
                            onTestListener.onTest(true, "互联网连接正常");
                        } else {
                            onTestListener.onTest(false, "需要认证的网络");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    onTestListener.onTest(false, "IOException");
                }
            }
        });
    }

    /**
     * get请求
     *
     * @param url      地址
     * @param callback 回调
     */
    private void get(String url, Callback callback) {
        //String url = "http://www.qq.com/";
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        OkHttpClient okHttpClient = builder.build();
        //默认就是GET请求，可以不写
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    public void pingOutUrl(OnNetTestListener onTestListener) {
        DelegateTaskExecutor.getInstance().executeOnDiskIO(new Runnable() {
            @Override
            public void run() {
                boolean success = get(PING_ADDRESS);
                if (onTestListener != null) {
                    onTestListener.onTest(success, "");
                }
            }
        });
    }

    private boolean get(String url) {
        boolean isOnline;
        HttpURLConnection conn = null;
        try {
            // 利用string url构建URL对象
            URL mURL = new URL(url);
            // 调用URL的openConnection()方法,获取HttpURLConnection对象
            conn = (HttpURLConnection) mURL.openConnection();
            // 设置请求方法为get
            conn.setRequestMethod("GET");
            // 设置读取超时为5秒
            conn.setReadTimeout(5000);
            // 设置连接网络超时为10秒
            conn.setConnectTimeout(10000);
            // 调用此方法就不必再使用conn.connect()方法
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                //返回从打开的连接读取的输入流
                InputStream is = conn.getInputStream();
                String response = getStringFromInputStream(is);
                isOnline = !TextUtils.isEmpty(response);
            } else {
                isOnline = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isOnline = false;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return isOnline;
    }

    private String getStringFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 模板代码 必须熟练
        byte[] buffer = new byte[1024 * 4];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        // 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
        String state = os.toString();
        os.close();
        return state;
    }

    private boolean pingBaidu() {
        BufferedReader br = null;
        Process process = null;
        String result = null;
        try {
            // 除非百度挂了，否则用这个应该没问题~
            String ip = "www.baidu.com";
            //ping3次
            process = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);
            // 读取ping的内容，可不加。
            InputStream input = process.getInputStream();
            br = new BufferedReader(new InputStreamReader(input));
            StringBuilder stringBuffer = new StringBuilder();
            String content = "";
            while ((content = br.readLine()) != null) {
                stringBuffer.append(content);
            }
            // 返回值判断ping是否成功
            // PING的状态
            // process.waitFor() 返回0，当前网络可用
            // process.waitFor() 返回1，需要网页认证的wifi
            // process.waitFor() 返回2，当前网络不可用
            int status = process.waitFor();
            if (status == 0) {
                result = "successful~" + stringBuffer;
                return true;
            } else {
                result = "failed~ cannot reach the IP address";
            }
        } catch (IOException e) {
            result = "failed~ IOException";
        } catch (InterruptedException e) {
            result = "failed~ InterruptedException";
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    result = "failed~ RuntimeException";
                }
            }
            AppLogUtils.d("ping network result = " + result);
        }
        return false;
    }

    public boolean pingAddress() {
        String ipAddress = "www.baidu.com";
        try {
            //-c 1是指ping的次数为1次，-w 3是指超时时间为3s
            Process process = Runtime.getRuntime()
                    .exec("ping -c 1 -w 3 " + ipAddress);
            //status为0表示ping成功
            int status = process.waitFor();
            if (status == 0) {
                return true;
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
