# 相机打开和采集库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明
#### 1.1 相机打开和采集
- 打开相机和采集涉及的类
    - CameraManager 用于打开相机和检测相关的功能
    - CameraDevice 打开相机后，相机的实体对象，用于操作相机捕获，相机回调等操作
    - ImagerReader 专门做图像读取采集的，最终捕获图像是Image格式
- 相机图像渲染涉及的类
    - SurfaceView或TextureView。相机预览图像发送到SurfaceView或TextureView渲染出图像



#### 1.2 CameraManager说明
- CameraManager类概述
    - CameraManager是用于检测、表征和连接到 CameraDevices 的系统服务管理器。专门用于 检测 和 打开相机，以及 获取相机设备特性。
- CameraManager 是一个负责查询和建立相机连接的系统服务，它的功能不多，这里列出几个 CameraManager 的关键功能：
    - 1）、将相机信息封装到 Camera Characteristics 中，并提获取 CameraCharacteristics 实例的方式。
    - 2）、根据指定的相机 ID 打开相机设备（openCamera）。
    - 3）、提供将闪光灯设置成手电筒模式的快捷方式。
- AvailabilityCallback抽象类
    - 注册相机设备变为可用或不可打开的回调。
    - 当不再使用相机或连接新的可拆卸相机时，相机将变得可用。 当某些应用程序或服务开始使用相机时，或者当可移动相机断开连接时，它们将变得不可用。
- TorchCallback抽象类
    - 相机闪光灯模式变得不可用、禁用或启用的回调。
    - 当手电筒模式处于禁用或启用状态时，才可通过 setTorchMode 设置。扩展此回调并将子类的实例传递给 registerTorchCallback 以收到此类状态更改的通知。
- String[] getCameraIdList()
    - 获取当前连接的相机设备列表，这个 id 通常都是从 0 开始并依次递增的。对于一般的手机而言:
    - 后置摄像头一般为 “0”，常量值为 CameraCharacteristics.LENS_FACING_FRONT；
    - 前置摄像头一般为 “1”，常量值为 CameraCharacteristics.LENS_FACING_BACK。
- CameraCharacteristics getCameraCharacteristics(String cameraId)
    - 根据 cameraId 获取对应相机设备的特征。返回一个 CameraCharacteristics，类比于旧 API 中的 Camera.Parameter 类，里面封装了相机设备固有的所有属性功能。
- void openCamera(String cameraId, final CameraDevice.StateCallback callback, Handler handler)
    - 打开指定的相机设备，该方法使用当前进程 uid 继续调用 openCameraForUid(cameraId, callback, handler, USE_CALLING_UID) 方法。
    - handler ： 指定回调执行的线程。传 null 时默认使用当前线程的 Looper，我们通常创建一个后台线程来处理。




#### 1.3 CameraDevice说明
- CameraDevice类概述
    - CameraDevice 类表示连接到 Android 设备的单个相机，允许以高帧速率对图像捕获和后处理进行细粒度控制。
- 如何获取CameraDevice实例
    - 通过 CameraManager 的 openCamera() 方法打开相机，在 CameraDevice.StateCallback 的 onOpened(CameraDevice camera) 方法中可获得 CameraDevice 的实例。
- 内部类CameraDevice.StateCallback说明
    - 当相机状态发生变化时，会调用该类相应的回调方法。
    - onOpened，当相机成功打开时回调该方法，接下来可以执行创建预览的操作
    - onDisconnected，当相机断开连接时回调该方法，应该在此执行释放相机的操作
    - onError，当相机打开失败时，应该在此执行释放相机的操作
    - onClosed，当相机关闭时回调该方法，这个方法可以不用实现
- CaptureRequest.Builder createCaptureRequest(int templateType)
    - 使用指定模板创建一个 CaptureRequest.Builder 用于新的捕获请求构建。
- void createCaptureSession(List outputs, CameraCaptureSession.StateCallback callback, Handler handler)
    - 使用一个指定的 Surface 输出列表创建一个相机捕捉会话



#### 1.4 ImagerReader说明
- ImagerReader类介绍
    - 简单来说就是图像读取器。ImageReader可以直接获取屏幕渲染数据，得到屏幕数据是image格式。
    - 图像数据封装在Image对象中，可以同时访问多个此类对象，最多可达 maxImages构造函数参数指定的数量。
    - 通过 ImageReader 发送到 ImageReader 的新图像Surface将排队，直到通过acquireLatestImage() 或acquireNextImage()调用访问。
    - 由于内存限制，如果 ImageReader 没有以等于生产速率的速率获取和释放图像，则图像源最终将在尝试渲染到 Surface 时停止或丢弃图像。
- 一些核心的API说明
    - setOnImageAvailableListener，注册一个侦听器，以便在 ImageReader 中提供新图像时调用该侦听器。
    - newInstance，通过工厂方法 newInstance函数或构建器模式ImageReader.Builder并使用。
    - getSurface，获取一个Surface可用于Images为此 生产的ImageReader。Surface用于各种 API 的绘图目标。



### 02.常见思路和做法
#### 2.1 如何打开相机
- 如何打开相机操作
    - 第一种方式：使用最原始方式，通过intent打开原生相机。
    - 第二种方式：通过CameraManager打开(openCamera)指定的相机设备。
    - 第三种方式：
- 打开相机后如何如何判断相机可用与不可用
    - 通过CameraManager注册相机设备监听，具体的api是：registerAvailabilityCallback
    - onCameraAvailable。相机已经可以使用，并且回调可以使用相机的ID
    - onCameraUnavailable。之前可用的相机已无法使用，如果应用程序具有当前已断开连接的相机的活动 CameraDevice 实例，则该应用程序将收到断开连接错误。
- 如何判断打开相机成功或异常
    - 具体可以看：CameraDevice.StateCallback 执行的回调。打开相机成功会回调到onOpened，打开相机异常会回调onError。



#### 2.2 获取设备相机
- 使用CameraManager对象调用getCameraIdList()
    - 获取当前连接的相机设备列表，这个 id 通常都是从 0 开始并依次递增的。对于一般的手机而言:
    - 后置摄像头一般为 “0”，常量值为 CameraCharacteristics.LENS_FACING_FRONT；
    - 前置摄像头一般为 “1”，常量值为 CameraCharacteristics.LENS_FACING_BACK。
- 有些相机有多个，比如说四五个摄像头
    - 若要打开指定的摄像头，则必须先知道该摄像头的id才可以。



#### 2.3 打开相机后采集
- 在打开相机成功之后操作
    - 在openCamera打开相机后，会执行onOpened回调，这表明打开相机成功了。然后拿到当前相机对象：CameraDevice
- 理解采集的内容是什么
    - 相机打开后，捕获的是一个个画面。那么这个时候，采集数据，是针对这种一个个画面，进行相机捕捉会话！
    - 具体则是使用：cameraDevice.createCaptureSession(surfaces, stateCallback, null);



#### 2.4 获取采集图像
- 第一步：创建图像读取器
    - 通过ImageReader.newInstance创建图像采集器，可以指定宽高和格式。
- 第二步：添加图像读取器监听
    - 调用setOnImageAvailableListener，注册一个侦听器，以便在 ImageReader 中提供新图像时调用该侦听器。如果有图像，则获取图像image对象。
- 第三步：绑定图像读取器surface到相机上
    - 获取ImageReader的Surface，然后添加到CameraDevice配置的Surface输出列表，最后使用相机创建一个捕捉会话。
- 第四步：异常情况处理
    - 如果打开相机异常，或者相机断开连接，则关闭图像读取器






### 03.Api调用说明



### 04.遇到的坑分析
#### 4.1 辨别硬件支持等级
- 在 Camera2 中，相机设备支持的硬件等级有
    - LEVEL_3 > FULL > LIMIT > LEGACY
    - 对于大多数的Android设备而言，都会支持到 FULL 或 LIMIT。当支持到 FULL 等级的相机设备，将拥有比旧 API 强大的新特性，如 30fps 全高清连拍，帧之间的手动设置，RAW 格式的图片拍摄，快门零延迟以及视频速拍等，否则和旧 API 功能差别不大。
- 如何获取相机的特性
    - 通过 CameraCharacteristics 类获取相机设备的特性，包括硬件等级的支持等级。





### 05.其他问题说明


#### 5.3 参考博客连接
- Android Camera之createCaptureSession()
    - https://blog.csdn.net/weixin_42463482/article/details/119858829
- 相机Camera官方文档
    - https://developer.android.google.cn/reference/android/hardware/camera2/package-summary




