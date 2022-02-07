package com.yc.netlib.stetho;





import androidx.annotation.Nullable;

import com.yc.netlib.data.IDataPoolHandleImpl;
import com.yc.netlib.data.NetworkFeedBean;
import com.yc.netlib.utils.NetLogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2019/07/22
 *     desc  : 拦截网络请求操作
 *     revise: 具体参考：StethoInterceptor
 * </pre>
 * Provides easy integration with <a href="http://square.github.io/okhttp/">OkHttp</a> 3.x by way of
 * the new <a href="https://github.com/square/okhttp/wiki/Interceptors">Interceptor</a> system. To
 * <pre>
 *   OkHttpClient client = new OkHttpClient.Builder()
 *       .addNetworkInterceptor(new NetworkInterceptor())
 *       .build();
 * </pre>
 */
public class NetworkInterceptor implements Interceptor {

    private static final String TAG = "NetworkInterceptor";
    private final NetworkEventReporter mEventReporter = NetworkReporterImpl.getInstance();

    @Override
    public Response intercept(Chain chain) throws IOException {
        // 构造一个独特的eventID，一对网络事件（请求和回包）对应一个eventID
        String requestId = mEventReporter.nextRequestId();
        NetLogUtils.d(TAG+"-----requestId-----"+requestId);
        Request request = chain.request();
        NetLogUtils.d(TAG+"-----request-----"+request.toString());

        //从map集合中取数据，如果有则直接返回，如果没有则存储该数据到map中
        NetworkFeedBean networkFeedModel = IDataPoolHandleImpl.getInstance().getNetworkFeedModel(requestId);
        networkFeedModel.setCURL(request.url().toString());

        // 准备发送请求
        RequestBodyHelper requestBodyHelper = null;
        if (mEventReporter.isEnabled()) {
            requestBodyHelper = new RequestBodyHelper(mEventReporter, requestId);
            OkHttpInspectorRequest inspectorRequest = new OkHttpInspectorRequest(requestId, request, requestBodyHelper);
            // 请求即将发送，构造一个OkHttpInspectorRequest，报告给Chrome，此时Network会显示一条请求，处于Pending状态
            mEventReporter.requestWillBeSent(inspectorRequest);

        }
        Response response;
        try {
            // 发送请求，获得回包
            response = chain.proceed(request);
        } catch (IOException e) {
            // 如果发生了IO Exception，则通知Chrome网络请求失败了，显示对应的错误信息
            if (mEventReporter.isEnabled()) {
                mEventReporter.httpExchangeFailed(requestId, e.toString());
            }
            throw e;
        }

        if (mEventReporter.isEnabled()) {
            if (requestBodyHelper != null && requestBodyHelper.hasBody()) {
                requestBodyHelper.reportDataSent();
            }

            Connection connection = chain.connection();
            //写入连接时间
            int connectTimeoutMillis = chain.connectTimeoutMillis();
            networkFeedModel.setConnectTimeoutMillis(connectTimeoutMillis);
            if (connection == null) {
                throw new IllegalStateException(
                        "No connection associated with this request; " +
                                "did you use addInterceptor instead of addNetworkInterceptor?");
            }

            // 回包的header已收到，构造一个OkHttpInspectorResponse,发送给Chrome用于展示
            mEventReporter.responseHeadersReceived(new OkHttpInspectorResponse(requestId, request, response, connection));

            // 展示回包信息
            // body信息题
            ResponseBody body = response.body();
            MediaType contentType = null;
            InputStream responseStream = null;
            if (body != null) {
                contentType = body.contentType();
                responseStream = body.byteStream();
            }

            responseStream = mEventReporter.interpretResponseStream(
                    requestId,
                    contentType != null ? contentType.toString() : null,
                    response.header("Content-Encoding"),
                    responseStream,
                    new DefaultResponseHandler(mEventReporter, requestId));
            if (responseStream != null) {
                response = response.newBuilder()
                        .body(new ForwardingResponseBody(body, responseStream))
                        .build();
            }
        }
        //写入其他信息
        int readTimeoutMillis = chain.readTimeoutMillis();
        int writeTimeoutMillis = chain.writeTimeoutMillis();
        networkFeedModel.setReadTimeoutMillis(readTimeoutMillis);
        networkFeedModel.setWriteTimeoutMillis(writeTimeoutMillis);
        return response;
    }

    /**
     * InspectorRequest
     * 指示将要发送请求，但尚未通过网络传递。
     * 表示将通过HTTP发送的请求。注意，对于许多HTTP实现，构造的请求可能与实际通过网络发送的请求不同。
     * 例如，额外的头，如{@code Host}， {@code User-Agent}， {@code Content-Type}等，可能不是这个请求的一部分，但如果需要，应该注入。
     * 一些堆栈提供对即将发送到服务器的原始请求的检查，这是更好的。
     */
    private static class OkHttpInspectorRequest implements NetworkEventReporter.InspectorRequest {

        private final String mRequestId;
        private final Request mRequest;
        private RequestBodyHelper mRequestBodyHelper;

        public OkHttpInspectorRequest(String requestId, Request request, RequestBodyHelper requestBodyHelper) {
            mRequestId = requestId;
            mRequest = request;
            mRequestBodyHelper = requestBodyHelper;
        }

        @Override
        public String id() {
            return mRequestId;
        }

        @Override
        public String friendlyName() {
            // Hmm, can we do better?  tag() perhaps?
            return null;
        }

        @Nullable
        @Override
        public Integer friendlyNameExtra() {
            return null;
        }

        @Override
        public String url() {
            return mRequest.url().toString();
        }

        @Override
        public String method() {
            return mRequest.method();
        }

        @Nullable
        @Override
        public byte[] body() throws IOException {
            RequestBody body = mRequest.body();
            if (body == null) {
                return null;
            }
            OutputStream out = mRequestBodyHelper.createBodySink(firstHeaderValue("Content-Encoding"));
            BufferedSink bufferedSink = Okio.buffer(Okio.sink(out));
            try {
                body.writeTo(bufferedSink);
            } finally {
                bufferedSink.close();
            }
            return mRequestBodyHelper.getDisplayBody();
        }

        @Override
        public int headerCount() {
            return mRequest.headers().size();
        }

        @Override
        public String headerName(int index) {
            return mRequest.headers().name(index);
        }

        @Override
        public String headerValue(int index) {
            return mRequest.headers().value(index);
        }

        @Nullable
        @Override
        public String firstHeaderValue(String name) {
            return mRequest.header(name);
        }
    }

    /**
     * InspectorResponse
     * 指示将要接收的响应Response，但尚未通过网络传递。
     */
    private static class OkHttpInspectorResponse implements NetworkEventReporter.InspectorResponse {
        private final String mRequestId;
        private final Request mRequest;
        private final Response mResponse;
        private @Nullable
        final Connection mConnection;

        public OkHttpInspectorResponse(String requestId, Request request, Response response, @Nullable Connection connection) {
            mRequestId = requestId;
            mRequest = request;
            mResponse = response;
            mConnection = connection;
        }

        @Override
        public String requestId() {
            return mRequestId;
        }

        @Override
        public String url() {
            return mRequest.url().toString();
        }

        @Override
        public int statusCode() {
            return mResponse.code();
        }

        @Override
        public String reasonPhrase() {
            return mResponse.message();
        }

        @Override
        public boolean connectionReused() {
            // Not sure...
            return false;
        }

        @Override
        public int connectionId() {
            return mConnection == null ? 0 : mConnection.hashCode();
        }

        @Override
        public boolean fromDiskCache() {
            return mResponse.cacheResponse() != null;
        }

        @Override
        public int headerCount() {
            return mResponse.headers().size();
        }

        @Override
        public String headerName(int index) {
            return mResponse.headers().name(index);
        }

        @Override
        public String headerValue(int index) {
            return mResponse.headers().value(index);
        }

        @Nullable
        @Override
        public String firstHeaderValue(String name) {
            return mResponse.header(name);
        }
    }

    private static class ForwardingResponseBody extends ResponseBody {

        private final ResponseBody mBody;
        private final BufferedSource mInterceptedSource;

        public ForwardingResponseBody(ResponseBody body, InputStream interceptedStream) {
            mBody = body;
            mInterceptedSource = Okio.buffer(Okio.source(interceptedStream));
        }

        @Override
        public MediaType contentType() {
            return mBody.contentType();
        }

        @Override
        public long contentLength() {
            return mBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            // close on the delegating body will actually close this intercepted source, but it
            // was derived from mBody.byteStream() therefore the close will be forwarded all the
            // way to the original.
            return mInterceptedSource;
        }
    }


}
