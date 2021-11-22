package com.yc.tracesdk.net;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import com.yc.tracesdk.LogHelper;

public class HttpPostThread extends Thread {
    private final Param mParam;
    private final ResponseListener mResponseListener;
    private volatile HttpURLConnection mConn = null;
    private int DEFAULT_TIME_OUT = 15 * 1000;
    private static final String BOUNDARY = "--------------et567z";

    public HttpPostThread(ResponseListener listener, Param param) {
        mParam = param;
        mResponseListener = listener;
    }

    /** 创建HttpURLConnection */
    private HttpURLConnection prepareConnection() {
        HttpURLConnection conn = null;
        try {
            String urlString = mParam.mUrl.trim();
            URL url = new URL(urlString);

            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setConnectTimeout(DEFAULT_TIME_OUT);
            conn.setReadTimeout(DEFAULT_TIME_OUT);
            conn.setRequestProperty("User-Agent", "");
            conn.setRequestProperty("host", url.getHost());

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            if (!mParam.mPostData.isEmpty()) {
                conn.setRequestProperty("Content-Type", "Multipart/form-data;boundary=" + BOUNDARY);
            } else {
                conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            }
        } catch (MalformedURLException e1) {
            //e1.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
        }

        return conn;
    }

    @Override
    public void run() {
        mConn = prepareConnection();
        if (mConn == null) {
            mInnerResponseListener.onReceiveError(0);
            return;
        }

        try {
            DataOutputStream out = new DataOutputStream(mConn.getOutputStream());

            /* 普通参数数据 */
            for (Map.Entry<String, String> entry : mParam.mPostMap.entrySet()) {
                out.writeBytes("--" + BOUNDARY + "\r\n");
                out.writeBytes("Content-Disposition: form-data; name=\"" + URLEncoder.encode(entry.getKey(), "UTF-8")
                        + "\"\r\n\r\n");
                out.writeBytes(entry.getValue() + "\r\n");
            }

            /* 提交二进制数据 */
            if (!mParam.mPostData.isEmpty()) {
                Set<String> keyset = mParam.mPostData.keySet();
                for (String k : keyset) {
                    byte[] bytes = mParam.mPostData.get(k);

                    out.writeBytes("--" + BOUNDARY + "\r\n");
                    out.writeBytes("Content-Disposition: form-data; name=\"" + k + "\r\n");
                    out.writeBytes("Content-Type: application/x-www-form-urlencoded\r\n\r\n");
                    out.write(bytes);
                    out.writeBytes("\r\n");
                }
            }
            out.writeBytes("--" + BOUNDARY + "--\r\n");

            out.flush();
            out.close();

            mConn.connect();
            int code = mConn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                String content = readInputStream(mConn.getInputStream());
                mInnerResponseListener.onReceiveResponse(content);
            } else {
                mInnerResponseListener.onReceiveError(code);
            }

        } catch (IOException e) {
            //e.printStackTrace();
            mInnerResponseListener.onReceiveError(-1);
        }
    }

    /**
     * 读取流中的数据
     * 
     * @param inStream
     * @return
     * @throws Exception
     */
    public static String readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 6];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toString();
    }

    private ResponseListener mInnerResponseListener = new ResponseListener() {

        @Override
        public void onReceiveResponse(String response) {
            LogHelper.log("mInnerResponseListener#onReceiveResponse:" + response);
            if (mResponseListener != null) {
                mResponseListener.onReceiveResponse(response);
            }
        }

        @Override
        public void onReceiveError(int code) {
            LogHelper.log("mInnerResponseListener#onReceiveError:" + code);
            if (mResponseListener != null) {
                mResponseListener.onReceiveError(code);
            }
        }

    };
}
