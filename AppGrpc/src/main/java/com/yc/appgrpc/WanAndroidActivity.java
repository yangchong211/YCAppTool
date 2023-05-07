package com.yc.appgrpc;

import android.app.Activity;
import android.content.Context;
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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class WanAndroidActivity extends AppCompatActivity {

    private Button sendButton;
    private EditText hostEdit;
    private EditText portEdit;
    private EditText messageEdit;
    private TextView resultText;

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
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                channel = ChannelHelper.newChannel(host,port);
                //构建服务请求API代理
                GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);
                //构建请求实体
                //HelloRequest是自动生成的实体类
                HelloRequest request = HelloRequest.newBuilder().setName(message).build();
                //进行请求
                HelloReply reply = stub.sayHello(request);
                return reply.getMessage();
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
            ChannelHelper.shutdown(channel);
            Activity activity = activityReference.get();
            if (activity == null) {
                return;
            }
            TextView resultText = activity.findViewById(R.id.grpc_response_text);
            Button sendButton = activity.findViewById(R.id.send_button);
            resultText.setText(result);
            sendButton.setEnabled(true);
        }
    }
}
