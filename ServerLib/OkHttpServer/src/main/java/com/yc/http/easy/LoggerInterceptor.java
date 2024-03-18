package com.yc.http.easy;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LoggerInterceptor implements Interceptor {
    private static final String TAG = "NetWork";

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logForRequest(request);
        Response response = chain.proceed(request);
        return logForResponse(response);
    }

    private Response logForResponse(Response response) {
        try {
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            Log.e(TAG, "Response[\nUrl: " + clone.request().url() + ", \nCode: "
                    + clone.code() + ",\nProtocol:" + clone.protocol()
                    + ", \nMessage: " + clone.message() + " ]");
            ResponseBody body = clone.body();
            if (body != null) {
                MediaType mediaType = body.contentType();
                if (mediaType != null) {
                    if (isText(mediaType)) {
                        String resp = body.string();
                        Log.e(TAG, "Response Content[\nType: "
                                + mediaType.toString() + ", \nContent: " + resp + " ]");
                        Response build = response.newBuilder().body(
                                ResponseBody.create(mediaType, resp)).build();
                        return build;
                    } else {
                        Log.e(TAG, "Response Content[\nType: "
                                + mediaType.toString() + ",\nContent: maybe [file part] , " +
                                "too large too print , ignored! ]");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();

            if (headers != null && headers.size() > 0) {
                Log.e(TAG, "Request[\nUrl: " + url + "   ,\nMethod: "
                        + request.method() + "\nHeader: " + headers.toString() + "]");
            } else {
                Log.e(TAG, "Request[\nUrl: " + url
                        + "   ,\nMethod: " + request.method() + "]");
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    if (isText(mediaType)) {
                        Log.e(TAG, "Request Content [\nType:" + mediaType.toString()
                                + " ,\nContent: " + bodyToString(request) + "]");
                    } else {
                        Log.e(TAG, "Request Content [\nType:" + mediaType.toString()
                                + " ,\nContent:  maybe [file part] , too large too print , ignored!]");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isText(MediaType mediaType) {
        return mediaType.type() != null && mediaType.type().equals("text")
                || mediaType.subtype() != null && (mediaType.subtype().equals("json")
                || mediaType.subtype().equals("xml")
                || mediaType.subtype().equals("html")
                || mediaType.subtype().equals("x-www-form-urlencoded"));
    }

    private String bodyToString(final Request request) {
        try {
            Request copy = request.newBuilder().build();
            Buffer buffer = new Buffer();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(copy.body()).writeTo(buffer);
            }
            return buffer.readUtf8();
        } catch (IOException e) {
            return "something error when show requestBody.";
        }
    }
}
