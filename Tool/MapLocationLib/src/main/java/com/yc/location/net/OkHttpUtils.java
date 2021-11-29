package com.yc.location.net;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpUtils {

    private static OkHttpClient client = null;

    public static void init() {
        if (client==null){
            client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();
        }
    }

    public static HttpResponse post(String url, final byte[] bytearray) throws IOException {
        try {
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/x-www-form-urlencoded"),bytearray);
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Content-Encoding", "gzip")
                    .addHeader("Encode-Version", "1.0")
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();


            HttpResponse rsp = new HttpResponse();
            rsp.httpRespCode = response.code();
            ResponseBody body = response.body();
            rsp.body = body.string();
            call.cancel();
            return rsp;
        } catch (Throwable e) {
            throw new IOException(e);
        }
    }


    public static class HttpResponse {
        public int httpRespCode = 0;
        public String body = null;
    }
}
