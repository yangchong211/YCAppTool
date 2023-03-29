package com.yc.blesample.chat.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yc.blesample.R;
import com.yc.blesample.chat.client.ClientService;
import com.yc.blesample.chat.server.ServerService;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends Activity {

    private static final String TAG = "ChatActivity";

    private TextView tvName, tvAddress;

    private EditText etInput;

    private Button btnSend;

    private List<Chat> list;

    private RecyclerView rvChat;

    private ChatAdapter chatAdapter;

    private BluetoothDevice device;
    //uuid为null时，来自服务的聊天框，isClient=false
    private String uuid;

    private boolean isClient;

    private Handler handler;

    private ClientService clientService;

    private ServerService serverService;

    private volatile static boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        initPreData();

        bindView();

        initView();

        setListener();

        registerReceiver();

        initData();
    }

    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        registerReceiver(bluetoothReceiver, intentFilter);
    }

    private void setListener() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etInput.getText().toString();

                while (text.startsWith("\n")) {
                    text = text.substring(2, text.length());
                }
                while (text.endsWith("\n")) {
                    text = text.substring(0, text.length() - 2);
                }


                if (text.length() > 0) {
                    if (isClient) {
                        clientService.write(text);
                    } else {
                        serverService.write(text);
                    }
                    etInput.setText("");
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private void initData() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case ChatService.WRITE_DATA_SUCCESS:
                        Log.i(TAG, "write success");
                        String selfText = (String) msg.obj;
                        Chat selfChat = new Chat(selfText, true);
                        list.add(selfChat);
                        chatAdapter.notifyItemChanged(list.size() - 1);
                        rvChat.scrollToPosition(list.size()-1);

                        break;
                    case ChatService.WRITE_DATA_FAIL:
                        Log.i(TAG, "write fail");


                        exitChatDialog("对方已经退出聊天，即将停止聊天", false);


                    case ChatService.READ_DATA_FAIL:

                        exitChatDialog("对方已经退出聊天，即将停止聊天", false);

                        break;

                    case ChatService.READ_DATA_SUCCESS:
                        Log.i(TAG, "read success");
                        String text = (String) msg.obj;
                        Chat chat = new Chat(text, false);
                        list.add(chat);
                        chatAdapter.notifyItemChanged(list.size() - 1);
                        rvChat.scrollToPosition(list.size()-1);
                        break;

                }
            }
        };

        if (isClient) {
            clientService = ClientService.getInstance(handler);

            clientService.connect(device, uuid);
        } else {
            serverService = ServerService.getInstance(handler);
        }

    }

    private void initPreData() {
        device = getIntent().getParcelableExtra("device");

        uuid = getIntent().getStringExtra("uuid");

        if (uuid == null) {
            isClient = false;
        } else {
            isClient = true;
        }

        if (device == null) {
            Toast.makeText(this, "对方已经退出连接!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void bindView() {

        tvName = findViewById(R.id.tvName);

        tvAddress = findViewById(R.id.tvAddress);

        rvChat = findViewById(R.id.rvChat);

        etInput = findViewById(R.id.etInput);

        btnSend = findViewById(R.id.btnSend);

    }

    private void initView() {
        if (device.getName() == null) {
            tvName.setText("外星人");
        } else {
            tvName.setText(device.getName());
        }

        tvAddress.setText(device.getAddress());

        list = new ArrayList<>();

        chatAdapter = new ChatAdapter(this, list);

        rvChat.setLayoutManager(new LinearLayoutManager(this));

        rvChat.setAdapter(chatAdapter);

    }

    BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, intent.getAction());


            if (intent.getAction().equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                if (clientService != null) {
                    clientService.cancel();
                }

                exitChatDialog("当前连接已断开，请重新连接", false);
            } else if (intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    exitChatDialog("当前连接已断开，请重新连接", false);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(bluetoothReceiver);
        if (isClient) {
            clientService.cancel();
        }

    }

    public void exitChatDialog(String text, boolean cancelable) {
        if (exit) {
            return;
        }

        exit = true;


        AlertDialog dialog = new AlertDialog.Builder(this)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        if (isClient) {
                            clientService.cancel();
                        }
                    }
                })
                .setMessage(text).create();


        dialog.setCancelable(cancelable);


        dialog.show();

        if (isClient) {
            clientService.cancel();
        }


    }

    @Override
    public void onBackPressed() {
        exitChatDialog("退出当前聊天？", false);
    }
}
