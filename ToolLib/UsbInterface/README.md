# USB模式A口通信
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明





### 01.基础概念介绍
#### 1.1 什么叫串口通信
- 用安卓平板通过usb转接后与外设备进行通信
    - 开发使用的是usb主机模式，即：安卓机器作为主机，usb外设作为从机进行数据通信。然后主机和联动设备需要交互。


#### 1.2 串口通信场景


#### 1.3 串口通信原理


#### 1.4 串口通信优缺点
- 串口通信优点：
    - 1、不管是PC还是Android设备，串口都是默认支持的通用口，通用性强。
    - 2、无需高危权限，默认就能双向通信，更加安全。
    - 3、两根线就能完成通信，成本低廉。
- 串口通信缺点：
    - 1、因为通信协议是串行的，所以通信效率较低。
    - 2、数据帧的大小限制最多9位，对于2字节编码的数据帧支持不友好。



### 02.常见思路和做法
#### 2.1 串口通信实践步骤
- 1.发现设备
    - 通过UsbManager这个系统提供的类，我们可以枚举出当前连接的所有usb设备，我们主要需要的是UsbDevice对象。
    - UsbDevice，这个类就代表了Android所连接的usb设备。
    ```
    UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
    Map<String, UsbDevice> usbList = usbManager.getDeviceList();
    ```
- 2.打开设备
    - 需要打开刚刚搜索到的usb设备，我们可以将平板与usb外设之间的连接想象成一个通道，只有把通道的门打开后，两边才能进行通信。
    - 注意：在没有定制的android设备上首次访问usb设备的时候，默认是没有访问权限的，因此首先要判断对当前要打开的usbDevice是否有访问权限。
    - 需要和usb外设建立一个UsbDeviceConnection，大部分情况下还需要对usb串口进行一些配置，比如波特率,停止位,数据控制等，不然两边配置不同，收到的数据会乱码。
- 3.数据传输
    - 1.向usb外设发送数据。使用usbDeviceConnection.bulkTransfer这个函数用于在给定的端口进行数据传输。返回值代表发送成功的字节数，如果返回-1，那就是发送失败了。
    - 2.接受usb外设发送来的数据。找到了数据输入端口usbEndpointIn，因为数据的输入是不定时的，因此我们可以另开一个线程，来专门接受数据。





### 03.Api调用说明




### 04.遇到的坑分析




### 05.其他问题说明



### 06.参考博客说明
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








