package com.yc.blesample.chat.chat;

public interface ChatService {
     int CONNECTED_SUCCESS = 0;

     int CONNECTED_FAIL = 1;

    int WRITE_DATA_FAIL = 2;

    int WRITE_DATA_SUCCESS = 3;

     int READ_DATA_FAIL = 4;

     int READ_DATA_SUCCESS = 5;

     int BLUETOOTH_SOCKET_CLOSED = 6;

    void write(String text);


}
