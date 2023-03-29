package com.yc.blesample.chat.client;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.yc.blesample.chat.chat.ChatService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class ClientService implements ChatService {

    private static final String TAG = "ClientService";

    public static final String clientUuid = "aa87c0d0-afac-11de-8a39-0800200c9a66";


    private static ClientService instance;

    private static Handler mainHandler;

    private BluetoothDevice device;

    private BluetoothSocket mSocket;

    private OutputStream mOutputStream;

    private InputStream mInputStream;

    private ClientService() {

    }

    public static ClientService getInstance(Handler handler) {
        mainHandler = handler;

        if (instance == null) {
            Log.i(TAG, "instance is null");
            synchronized (ClientService.class) {
                if (instance == null) {
                    instance = new ClientService();
                }
            }
        }

        return instance;

    }

    public void connect(final BluetoothDevice connectDevice, final String uuid) {

        if (device != null && mSocket != null && device.getAddress().equals(device.getAddress()) && mSocket.isConnected()) {

            Log.i(TAG, "the same device to connect,return");

            return;
        }

        this.device = connectDevice;

        Log.i(TAG, "device:" + device.getName());

        Log.i(TAG, "uuid:" + uuid);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid));

                    mSocket.connect();

                    mOutputStream = mSocket.getOutputStream();

                    mInputStream = mSocket.getInputStream();

                    crateReadThread();

                    mainHandler.sendEmptyMessage(CONNECTED_SUCCESS);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "client connect error:" + e.getMessage());
                    mainHandler.sendEmptyMessage(CONNECTED_FAIL);
                }

            }
        }).start();
    }

    private void crateReadThread() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while (true) {


                        int length = 0;

                        while (length == 0) {
                            length = mInputStream.available();
                        }

                        byte[] bytes = new byte[length];


                        mInputStream.read(bytes);

                        String text = new String(bytes, "UTF-8");

                        Log.i(TAG, "receive:" + text);

                        Message msg = mainHandler.obtainMessage();

                        msg.obj = text;

                        msg.what = READ_DATA_SUCCESS;

                        mainHandler.sendMessage(msg);
                    }

                } catch (IOException e) {
                    mainHandler.sendEmptyMessage(READ_DATA_FAIL);
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void write(String text) {

        if (mSocket != null) {
            if (!mSocket.isConnected()) {
                mainHandler.sendEmptyMessage(ClientService.BLUETOOTH_SOCKET_CLOSED);
                Log.i(TAG, "write fail when socket was closed");
                return;
            }
        }

        Message msg = mainHandler.obtainMessage();
        String data = text;
        if (mOutputStream != null) {
            try {
                mOutputStream.write(text.getBytes());

                mOutputStream.flush();
                msg.what = WRITE_DATA_SUCCESS;
            } catch (IOException e) {
                e.printStackTrace();
                data = e.getMessage();
                msg.what = WRITE_DATA_FAIL;
            }
        }

        msg.obj = data;

        mainHandler.sendMessage(msg);
    }


    public void cancel() {
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mSocket = null;
        }

        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mOutputStream = null;
        }

        if (mInputStream != null) {
            try {
                mInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mInputStream = null;
        }

    }


}
