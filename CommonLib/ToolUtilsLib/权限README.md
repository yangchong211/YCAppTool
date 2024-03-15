#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.权限申请原理


### 01.基础概念介绍
#### 1.1 权限概念介绍
- 权限 Permission：
    - 权限是Android的一种安全机制，主要用来保护用户、应用的数据安全。Android系统将权限分为三个保护级别：普通、危险和签名权限。
- 普通权限
    - 普通权限就是在AndroidManifest.xml中注册即可，不需要动态申请。在应用安装后就会授予，用户无法撤销应用的这些权限。
- 危险权限
    - 危险权限也得在AndroidManifest.xml中注册，不过用到的时候需要动态申请：运行时检测有没有该权限，没有就提示用户授权。
- 签名权限
    - 在尝试使用某权限的应用签名证书，和定义该权限的应用证书一致时才会授予。一般自定义权限会采用这种签名的机制，防止其它三方应用也申请自定义的权限。
- adb命令查看权限
    - 通过adb命令可以查看手机中的所有权限：    adb shell pm list permissions
    - 授权某个app指定的permission：adb pm grant <package_name> <permission_name>



#### 1.2 权限组概念
- 什么是权限组
    - 一组权限要么一个都没有授权，要么只要授权其中一个全部权限自动获取(以后可能会有变化，埋个坑)。
    - 比如：STORAGE权限组(WRITE_EXTERNAL_STORAGE，READ_EXTERNAL_STORAGE)



#### 1.3 注册权限授予
- Android6.0(SDK版本为23)之前的版本
    - 安装App页面会列出当前app所注册的所有权限，无同意与否按钮，只有安装和取消，开发App时只需要在清单文件中注册所需的对应权限即可。
- Android自6.0(SDK版本为23)开始，将权限分为普通权限，危险权限，特殊权限。
    - 而其中的危险权限需要在调用某些系统方法之前需要用户手动授予对应权限，包括PHONE,LOCATION,STORAGE等多个权限组。如果在没授权的情况下直接调用相关方法，就会抛出，应用也随之崩溃。
    ``` java
    java.lang.SecurityException: getDeviceId: has android.permission.READ_PHONE_STATE.
    ```
    - 而要解决以上的报错问题，可以自行封装权限处理类工具，也可使用一些开源的权限工具进行处理。
- 权限判断的代码离不开这几处核心逻辑
    ``` java
    //判断某个权限是否已经被同意
    ContextCompat.checkSelfPermission(context, perm) ==  PackageManager.PERMISSION_GRANTED)
    //请求某个权限，调用后会弹出权限系统弹窗
    ActivityCompat.requestPermissions((Activity) object, perms, requestCode);
    ```
- 权限申请回调
    - 用户响应系统权限对话框后，系统就会调用应用的onRequestPermissionsResult()方法。系统会传入用户对权限对话框的响应以及开发者自定义的请求代码
    ```
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    ```
- 注：如果用户拒绝权限且不让再次显示系统权限授权弹窗的话，最好是提供端内可点击进入手机系统的权限设置页面以让用户可以选择开启对应权限。



#### 1.4 自定义权限
- 使用自定义权限
    - 应用可以自定义自己的权限。定义权限使用<permission/>标签。
- 举个例子：
    - 友商的应用要跳转我们的Activity，Activity必须要设置exported为true。但是这样又不安全，这意味着其它任意应用都可以启动暴露的Activity。这时可以在AndroidManifest.xml中给Activity加上权限。
    ``` java
    <activity
        android:exported="true"
        android:permission="com.quibbler.SUBSCRIBE">
    </activity>
    ```
- 将我们自定义的权限给三方，三方应用只需要在自己的AndroidManifest中添加声明即可。
    ``` java
    <!--申请自定义权限-->
    <uses-permission android:name="com.quibbler.SUBSCRIBE" />
    ```
- 在尝试使用某权限的应用签名证书，和定义该权限的应用证书一致时才会授予。
    - 一般自定义权限会采用这种签名的机制，防止其它三方应用也申请自定义的权限。比如腾讯系、阿里系的应用有相同的证书，保证内部自定义的权限不会被“外族”使用。




### 04.遇到的坑分析




### 05.权限申请原理
- http://quibbler.cn/?thread-559.htm




