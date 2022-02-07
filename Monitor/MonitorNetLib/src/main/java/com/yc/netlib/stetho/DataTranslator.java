package com.yc.netlib.stetho;

import android.net.Uri;
import android.text.TextUtils;

import com.yc.netlib.data.IDataPoolHandleImpl;
import com.yc.netlib.data.NetworkFeedBean;
import com.yc.netlib.data.NetworkRecord;
import com.yc.netlib.ui.NetworkManager;
import com.yc.netlib.utils.NetLogUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;


public class DataTranslator {


    private static final String TAG = "DataTranslator";
    private static final String GZIP_ENCODING = "gzip";
    private Map<String, Long> mStartTimeMap = new HashMap();


    public void saveInspectorRequest(NetworkEventReporter.InspectorRequest request) {
        String requestId = request.id();
        mStartTimeMap.put(request.id(), System.currentTimeMillis());
        NetLogUtils.i("DataTranslator-----saveInspectorRequest----"+request);
        NetworkFeedBean networkFeedModel = IDataPoolHandleImpl.getInstance().getNetworkFeedModel(requestId);
        //请求url
        String url = request.url();
        if (!TextUtils.isEmpty(url)) {
            String host = Uri.parse(url).getHost();
            //请求host
            networkFeedModel.setHost(host);
            //请求url
            networkFeedModel.setUrl(url);
        }
        //请求方法
        networkFeedModel.setMethod(request.method());
        //遍历请求打印数据
        Map<String, String> headersMap = new HashMap();
        for (int i = 0, count = request.headerCount(); i < count; i++) {
            String name = request.headerName(i);
            String value = request.headerValue(i);
            headersMap.put(name, value);
        }
        networkFeedModel.setRequestHeadersMap(headersMap);
        createRecord(requestId, request);
    }


    private void createRecord(String requestId, NetworkEventReporter.InspectorRequest inspectorRequest) {
        NetworkRecord record = new NetworkRecord();
        record.setRequestId(requestId);
        record.setMethod(inspectorRequest.method());
        record.setRequestLength(readBodyLength(inspectorRequest));
        NetworkManager.get().addRecord(requestId, record);
    }

    private long readBodyLength(NetworkEventReporter.InspectorRequest request) {
        try {
            byte[] body = request.body();
            if (body != null) {
                return body.length;
            }
        } catch (IOException | OutOfMemoryError e) {
            //e.printStackTrace();
        }
        return 0;
    }

    public void saveInspectorResponse(NetworkEventReporter.InspectorResponse response) {
        NetLogUtils.i("DataTranslator-----saveInspectorResponse----"+response);
        String requestId = response.requestId();
        long costTime;
        if (mStartTimeMap!=null){
            if (mStartTimeMap.containsKey(requestId)) {
                long aLong = mStartTimeMap.get(requestId);
                costTime = System.currentTimeMillis() - aLong;
                NetLogUtils.d(TAG, "cost time = " + costTime + "ms");
            } else {
                costTime = -1;
            }
            NetworkFeedBean networkFeedModel = IDataPoolHandleImpl.getInstance().getNetworkFeedModel(requestId);
            //设置响应时间
            //时间差 = 记录请求时间 - 记录响应时间
            //取绝对值就是这个时间
            networkFeedModel.setCostTime(costTime);
            //设置响应状态吗
            networkFeedModel.setStatus(response.statusCode());
            Map<String, String> headersMap = new HashMap();
            for (int i = 0, count = response.headerCount(); i < count; i++) {
                String name = response.headerName(i);
                String value = response.headerValue(i);
                headersMap.put(name, value);
            }
            networkFeedModel.setResponseHeadersMap(headersMap);
        }
    }

    public InputStream saveInterpretResponseStream(String requestId, String contentType, String contentEncoding, InputStream inputStream) {
        NetworkFeedBean networkFeedModel = IDataPoolHandleImpl.getInstance().getNetworkFeedModel(requestId);
        networkFeedModel.setContentType(contentType);
        if (isSupportType(contentType)) {
            ByteArrayOutputStream byteArrayOutputStream = parseAndSaveBody(inputStream, networkFeedModel, contentEncoding);
            InputStream newInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                NetLogUtils.e(TAG+ "----saveInterpretResponseStream---"+ e);
            }
            return newInputStream;
        } else {
            networkFeedModel.setBody(contentType + " is not supported.");
            networkFeedModel.setSize(0);
            return inputStream;
        }
    }

    private ByteArrayOutputStream parseAndSaveBody(InputStream inputStream, NetworkFeedBean networkFeedModel, String contentEncoding) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
            InputStream newStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            BufferedReader bufferedReader;
            if (GZIP_ENCODING.equals(contentEncoding)) {
                GZIPInputStream gzipInputStream = new GZIPInputStream(newStream);
                bufferedReader = new BufferedReader(new InputStreamReader(gzipInputStream));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(newStream));
            }
            StringBuilder bodyBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                bodyBuilder.append(line + '\n');
            }
            String body = bodyBuilder.toString();
            networkFeedModel.setBody(body);
            networkFeedModel.setSize(body.getBytes().length);

            //设置响应数据大小
            NetworkRecord record = NetworkManager.get().getRecord(networkFeedModel.getRequestId());
            record.setResponseLength(body.getBytes().length);
        } catch (IOException e) {
            NetLogUtils.e(TAG+ "----parseAndSaveBody---"+ e);
        }
        return byteArrayOutputStream;
    }

    private boolean isSupportType(String contentType) {
        return contentType.contains("text") || contentType.contains("json");
    }
}
