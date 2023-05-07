package com.yc.appgrpc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.toastutils.ToastUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class HelloWorldActivity extends AppCompatActivity {

    private Button sendButton;
    private EditText hostEdit;
    private EditText portEdit;
    private EditText messageEdit;
    private TextView resultText;

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, HelloWorldActivity.class);
            target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helloworld);
        sendButton = findViewById(R.id.send_button);
        hostEdit = findViewById(R.id.host_edit_text);
        portEdit = findViewById(R.id.port_edit_text);
        messageEdit = findViewById(R.id.message_edit_text);
        resultText = findViewById(R.id.grpc_response_text);
        resultText.setMovementMethod(new ScrollingMovementMethod());

        Uri uri = Uri.parse("https://www.wanandroid.com");
        String host = uri.getHost();
        int port = uri.getPort();
        hostEdit.setText(host);
        messageEdit.setText("yangchong");
        portEdit.setText(port+"");
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hostEdit.getText().toString().length() == 0){
                    ToastUtils.showRoundRectToast("host不能为空");
                    return;
                }
                if (messageEdit.getText().toString().length() == 0){
                    ToastUtils.showRoundRectToast("message不能为空");
                    return;
                }
                if (portEdit.getText().toString().length() == 0){
                    ToastUtils.showRoundRectToast("port不能为空");
                    return;
                }
                sendMessage(v);
            }
        });
    }

    public void sendMessage(View view) {
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(hostEdit.getWindowToken(), 0);
        sendButton.setEnabled(false);
        resultText.setText("");
        new GrpcTask(this).execute(
                        hostEdit.getText().toString(),
                        messageEdit.getText().toString(),
                        portEdit.getText().toString());
    }

    private static class GrpcTask extends AsyncTask<String, Void, String> {

        private final WeakReference<Activity> activityReference;
        private ManagedChannel channel;

        private GrpcTask(Activity activity) {
            this.activityReference = new WeakReference<Activity>(activity);
        }

        @Override
        protected String doInBackground(String... params) {
            String host = params[0];
            String message = params[1];
            String portStr = params[2];
            int port = TextUtils.isEmpty(portStr) ? 0 : Integer.valueOf(portStr);
            try {
                //构建Channel
                channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                //构建服务请求API代理
                GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);
                //构建请求实体，HelloRequest是自动生成的实体类
                HelloRequest request = HelloRequest.newBuilder().setName(message).build();
                //进行请求并且得到响应数据
                HelloReply reply = stub.sayHello(request);
                //得到请求响应的数据
                String replyMessage = reply.getMessage();
                return replyMessage;
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                pw.flush();
                return String.format("Failed... : %n%s", sw);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                channel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            Activity activity = activityReference.get();
            if (activity == null) {
                return;
            }
            TextView resultText = (TextView) activity.findViewById(R.id.grpc_response_text);
            Button sendButton = (Button) activity.findViewById(R.id.send_button);
            resultText.setText(result);
            sendButton.setEnabled(true);
        }
    }
}
