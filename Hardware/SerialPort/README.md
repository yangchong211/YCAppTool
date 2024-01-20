# 串口通信工具库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.原理和遇到的坑
- 05.其他问题说明
- 06.技术问题思考&分析



### 01.基础概念介绍
#### 1.1 android-串口-api
- 当前的Android SDK不提供任何用于读写Linux TTY串口的API。您可能会在 HTC Android 手机的连接器上找到此类串行端口。
- 该项目希望提供一个简单的API来通过这些串行端口连接、读取和写入数据。
- 支持的功能有：
    - 列出设备上可用的串行端口，包括 USB 转串行适配器
    - 配置串行端口（波特率、停止位、权限等）
    - 提供标准的 InputStream 和 OutputStream Java 接口
- 该项目不可能实现的功能：
    - 通过 USB 从机接口接收/发送数据


#### 1.2 什么是串口通信
- 串行端口 (SerialPort)简称：串口
    - 主要用于数据被逐位按顺序传送的通讯方式称为串口通讯（简单来讲就是按顺序一位一位地传输数据）。


#### 1.3 串口通信原理
- 串口通信（Serial Communications）的概念非常简单，串口按位（bit）发送和接收字节。
    - 串口用于ASCII码字符的传输。通信使用3根线完成，分别是地线(GND)、发送(TX)、接收(RX)。
    - 由于串口通信是异步的，端口能够在一根线上发送数据同时在另一根线上接收数据。其他线用于握手，但不是必须的。
    - 串口通信最重要的参数是波特率、数据位、停止位和奇偶校验。对于两个进行通信的端口，这些参数必须匹配。


#### 1.4 Android串口通信
- Android SDK并没有在Framework层实现封装关于串口通信的类库。
- Android是基于Linux kernel 2.6上的，所以我们可以像在Linux系统上一样来使用串口。因为Framework层中并没有封装关于串口通信的类库，所以我们需要通过Android NDK来实现打开、读写串口，然后提供接口供JAVA本地调用。




### 02.常见思路和做法
#### 2.1 开发思路步骤
- 串口通信开发思路步骤
    - 第一步：打开串口。
    - 第二步：往串口中写入数据。
    - 第三步：读取串口数据。



### 03.Api调用说明




### 04.原理和遇到的坑
#### 4.1 读取数据如何处理分包
- 读取串口数据，读取数据时很可能会遇到分包的情况，即不能一次性读取正确的完整的数据
    - 解决办法：可以在读取到数据时，让读取数据的线程sleep一段时间，等待数据全部接收完，再一次性读取出来。这样应该可以避免大部分的分包情况
    - 只接收一条数据的情况下，以上方法可以应对数据分包，数据量多的情况下需要考虑是否会因为sleep导致接收多条数据，可以根据通信协议核对包头包尾等参数。
    ```
    //从串口对象中获取输入流
    InputStream inputStream = serialPort.getInputStream();
    //使用循环读取数据，建议放到子线程去
    while (true) {
        if (inputStream.available() > 0) {
            //当接收到数据时，sleep 500毫秒（sleep时间自己把握）
            Thread.sleep(500);
            //sleep过后，再读取数据，基本上都是完整的数据
            byte[] buffer = new byte[inputStream.available()];
            int size = inputStream.read(buffer);
        }
    }
    ```




#### 4.7 Android串口系统
- https://www.cnblogs.com/hackfun/p/7683512.html



### 05.其他问题说明


### 06.技术问题思考&分析


### 参考博客
- https://github.com/licheedev/Android-SerialPort-API/tree/master/serialport
- Android 串口驱动和应用测试
  - https://www.cnblogs.com/hackfun/p/7612617.html
- android6.0 SerialPort 服务
  - https://www.cnblogs.com/hackfun/p/7683512.html
- Android串口通讯SerialPort(使用篇)
  - https://blog.csdn.net/qq_42111674/article/details/123653870
- Android串口通讯SerialPort(浅谈原理)
  - https://blog.csdn.net/qq_42111674/article/details/123737732




