# 蓝牙工具库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.该库性能分析



### 00.问题思考问答
- 如何搜索蓝牙设备？搜索到很多设备后如何过滤公司的硬件设备？
- 如何进行配对，如何判断配对是否成功？如何解除配对？



### 01.基础概念介绍



#### 1.3 蓝牙核心关键类
- BluetoothAdapter：表示本地蓝牙适配器（蓝牙无线装置），是所有蓝牙交互的入口点。借助该类，您可以发现其他蓝牙设备、查询已绑定（已配对）设备的列表、使用已知的 MAC 地址实例化 BluetoothDevice，以及通过创建 BluetoothServerSocket 侦听来自其他设备的通信。
- BluetoothDevice：表示远程蓝牙设备。借助该类，您可以通过 BluetoothSocket 请求与某个远程设备建立连接，或查询有关该设备的信息，例如设备的名称、地址、类和绑定状态等。
- BluetoothSocket：表示蓝牙套接字接口（类似于 TCP Socket）。这是允许应用使用 InputStream 和 OutputStream 与其他蓝牙设备交换数据的连接点。
- BluetoothServerSocket：表示用于侦听传入请求的开放服务器套接字（类似于 TCP ServerSocket）。如要连接两台 Android 设备，其中一台设备必须使用此类开放一个服务器套接字。当远程蓝牙设备向此设备发出连接请求时，该设备接受连接，然后返回已连接的 BluetoothSocket。
- BluetoothClass：描述蓝牙设备的一般特征和功能。这是一组只读属性，用于定义设备的类和服务。
- BluetoothProfile：表示蓝牙配置文件的接口。蓝牙配置文件是适用于设备间蓝牙通信的无线接口规范。举个例子：免提配置文件。
- BluetoothHeadset：提供蓝牙耳机支持，以便与手机配合使用。这包括蓝牙耳机配置文件和免提 (v1.5) 配置文件。
- BluetoothA2dp：定义如何使用蓝牙立体声音频传输配置文件 (A2DP)，通过蓝牙连接将高质量音频从一个设备流式传输至另一个设备。
- BluetoothHealth：表示用于控制蓝牙服务的健康设备配置文件代理。
- BluetoothHealthCallback：用于实现 BluetoothHealth 回调的抽象类。您必须扩展此类并实现回调方法，以接收关于应用注册状态和蓝牙通道状态变化的更新内容。
- BluetoothHealthAppConfiguration：表示第三方蓝牙健康应用注册的应用配置，该配置旨在实现与远程蓝牙健康设备的通信。
- BluetoothProfile.ServiceListener：当 BluetoothProfile 进程间通信 (IPC) 客户端连接到运行特定配置文件的内部服务或断开该服务连接时，向该客户端发送通知的接口。


### 02.常见思路和做法
#### 2.1 蓝牙开发步骤




### 03.Api调用说明



### 04.遇到的坑分析




### 05.该库性能分析







