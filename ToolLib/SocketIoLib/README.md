# websocket长连接服务

#使用办法

  连接: SocketIoManager.getInstance().connect(url)
  
  断开连接: SocketIoManager.getInstance().disconnect()
  
  监听消息:  SocketIoManager.getInstance().addMessageReciver("news",callback)
  
  发送消息:  SocketIoManager.getInstance().sendMessage(MessageEntity)
  
  