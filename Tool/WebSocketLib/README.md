# websocket长连接服务

#使用办法

  //        WebSocketSetting webSocketSetting = new WebSocketSetting();
  //        重连次数
  //        webSocketSetting.setReconnectFrequency(10);
  //        webSocketSetting.setConnectUrl("wss://echo.websocket.org");
  //        心跳包间隔秒
  //        webSocketSetting.setConnectionLostTimeout(5);
  //        设置监听
  //        SocketCallBackManager.getInstance().addListener(new ISocketListener() {
  //            @Override
  //            public void onConnected() {
  //                Log.i("MainActivity", "onConnected()");
  //            }
  //
  //            @Override
  //            public void onConnectFailed(Throwable e) {
  //                Log.e("MainActivity", "onConnectFailed()  " + e.getMessage());
  //            }
  //
  //            @Override
  //            public void onDisconnect() {
  //                Log.e("MainActivity", "onDisconnect()  ");
  //            }
  //
  //            @Override
  //            public <T> void onMessage(String message) {
  //                Log.e("MainActivity", "onMessage()  string     " + message);
  //            }
  //
  //            @Override
  //            public <T> void onMessage(ByteBuffer bytes) {
  //                Log.e("MainActivity", "onMessage()  ByteBuffer   " + bytes.array().length);
  //            }
  //        });
    //        开始连接
    //        WebSocketClientManager.getInstance().connect(webSocketSetting);
  //       发送文本消息
  //        WebSocketClientManager.getInstance().send("你好啊服务端");
  //       发送字节流
  //        WebSocketClientManager.getInstance().send(bytes);