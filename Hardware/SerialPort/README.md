# 串口通信工具库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.原理和遇到的坑
- 05.其他问题说明





### 01.基础概念介绍
#### 1.1 理解USB接口
- 如何理解USB
    - USB是英文Universal Serial Bus（通用串行总线）的缩写，是一个外部总线标准，用于规范电脑与外部设备的连接和通讯。
    - AOA协议是Google公司推出的用于实现Android设备与外围设备之间USB通信的协议。
    - 该协议拓展了Android设备USB接口的功能，为基于Android系统的智能设备应用于数据采集和设备控制领域提供了条件。



#### 1.2 USB开发步骤
- Android USB 配件必须遵从 Android Open Accessory（AOA）协议，该协议定义了配件如何检测和建立与 Android 设备的通信。配件应执行以下步骤：
    - 等待并检测连接的设备
    - 确定设备的配件模式支持
    - 尝试以配件模式下启动设备（如果需要）
    - 如果设备支持 AOA，与设备建立通信
- UsbManager官方文档
    - https://developer.android.google.cn/reference/android/hardware/usb/



#### 1.3 USB主机和配件
- 在 USB 配件模式下（USB accessory）
    - 外部 USB 硬件充当 USB 主机。配件示例可能包括机器人控制器、扩展坞、诊断和音乐设备、自助服务终端、读卡器等等。
    - 这样，不具备主机功能的 Android 设备就能够与 USB 硬件互动。Android USB 配件必须设计为与 Android 设备兼容，并且必须遵守 Android 配件通信协议。
- 在 USB 主机模式下（USB host）
    - Android 设备充当主机。设备示例包括数码相机、键盘、鼠标和游戏控制器。即：安卓平板作为主机，usb外设作为从机进行数据通信。


#### 1.4 两种模式区别
- 两种模式之间的差异。
    - 当 Android 设备处于主机模式时，它会充当 USB 主机并为总线供电。
    - 当 Android 设备处于 USB 配件模式时，所连接的 USB 硬件（本例中为 Android USB 配件）充当主机并为总线供电。


#### 1.5 USB开发相关类
- android.hardware.usb包下提供了USB开发的相关类。
- 我们需要了解UsbManager、UsbDevice、UsbInterface、UsbEndpoint、UsbDeviceConnection、UsbRequest、UsbConstants。
    - 1、UsbManager:获得Usb的状态，与连接的Usb设备通信。
    - 2、UsbDevice：Usb设备的抽象，它包含一个或多个UsbInterface，而每个UsbInterface包含多个UsbEndpoint。Host与其通信，先打开UsbDeviceConnection，使用UsbRequest在一个端点（endpoint）发送和接收数据。
    - 3、UsbInterface：定义了设备的功能集，一个UsbDevice包含多个UsbInterface，每个Interface都是独立的。
    - 4、UsbEndpoint：endpoint是interface的通信通道。
    - 5、UsbDeviceConnection：host与device建立的连接，并在endpoint传输数据。
    - 6、UsbRequest：usb 请求包。可以在UsbDeviceConnection上异步传输数据。注意是只在异步通信时才会用到它。
    - 7、UsbConstants：usb常量的定义，对应linux/usb/ch9.h






### 02.常见思路和做法
#### 2.1 开发场景说明
- 开发使用的是usb主机模式，即：安卓平板作为主机，usb外设作为从机进行数据通信。
    - 开发使用的是usb主机模式，即：安卓机器作为主机，usb外设作为从机进行数据通信。然后主机和联动设备需要交互。


#### 2.2 开发思路步骤
- 第一步：发现设备。通过UsbManager调用getDeviceList可以获取当前连接的所有usb设备。
    - 通过UsbManager这个系统提供的类，我们可以枚举出当前连接的所有usb设备，我们主要需要的是UsbDevice对象。
    - UsbDevice，这个类就代表了Android所连接的usb设备。
    ```
    UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    Map<String, UsbDevice> usbList = usbManager.getDeviceList();
    ```
- 第二步：打开设备。可以将机具与usb外设之间的连接想象成一个通道，只有把通道的门打开后，两边才能进行通信。
    - 需要打开刚刚搜索到的usb设备，我们可以将平板与usb外设之间的连接想象成一个通道，只有把通道的门打开后，两边才能进行通信。
    - 注意：在没有定制的android设备上首次访问usb设备的时候，默认是没有访问权限的，因此首先要判断对当前要打开的usbDevice是否有访问权限。
    - 需要和usb外设建立一个UsbDeviceConnection，大部分情况下还需要对usb串口进行一些配置，比如波特率,停止位,数据控制等，不然两边配置不同，收到的数据会乱码。
    - 2.1 申请权限
    - 2.2 获得连接口UsbInterface
    - 2.3 获得连接端口UsbEndpoint
- 第三步：数据传输。已经可以与usb外设进行数据传输
    - 向usb外设发送数据。使用usbDeviceConnection.bulkTransfer这个函数用于在给定的端口进行数据传输。返回值代表发送成功的字节数，如果返回-1，那就是发送失败了。
    - 接受usb外设发送来的数据。找到了数据输入端口usbEndpointIn，因为数据的输入是不定时的，因此我们可以另开一个线程，来专门接受数据。
    - 3.1 发送数据。调用bulkTransfer方法发送数据
    - 3.2 接收数据。找到了数据输入端口usbEndpointIn，因为数据的输入是不定时的，因此我们可以另开一个线程，来专门接受数据



### 03.Api调用说明

### 04.原理和遇到的坑


### 05.其他问题说明


#### 5.4 其他推荐
- https://github.com/kongqw/AndroidSerialPort
- https://github.com/felHR85/UsbSerial
- https://github.com/licheedev/Android-SerialPort-Tool
- https://github.com/xmaihh/Android-Serialport
- https://github.com/cl-6666/serialPort/tree/master


#### 5.5 博客
- USB识别开发
  - https://blog.csdn.net/c19344881x/article/details/124838289
- Android USB转串口通信开发实例详解【好案例】
  - https://blog.csdn.net/u011555996/article/details/86220900
- Android Usb（OTG）串口通信
  - https://blog.csdn.net/MSONG93/article/details/130730467
- 安卓与串口通信-实践篇
  - https://blog.csdn.net/sinat_17133389/article/details/130788942
- Android的USB通信（AOA连接）
  - https://blog.csdn.net/CJohn1994/article/details/124669291
- Android-USB通信
  - https://blog.csdn.net/dream_xang/article/details/124274920


