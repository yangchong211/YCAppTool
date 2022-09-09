# 版本更新库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.该库其他介绍



### 01.基础概念介绍
#### 1.1 本库的亮点
- 支持后台下载，支持断点下载。支持监听下载过程，下载成功，失败，异常，下载中，暂停等多种状态
- 用户可以设置是否支持强制更新，还支持用户设置版本更新内容，当内容过长，可以实现滚动模式
- 支持进度条显示，对话框进度条，并且下载中支持通知栏进度条展示，解决8.0通知栏不显示问题
- 由于下载apk到本地需要权限，固在lib中已经处理这个逻辑，只有当有读写权限时才会下载文件，没有权限则跳转设置页面打开权限
- 相比GitHub上几个主流的版本更新库，我这个lib代码量少很多，我觉得最少最精简的代码完成需要的功能就最好
- 适配 Android 7.0 FileProvider，处理了7.0以上安装apk异常问题，在lib中已经配置了fileProvider，直接使用就可以
- 下载完成后自动安装，对于错误的下载链接地址，会下载异常，也可以查看异常的日志
- 当下载完成后，再次弹窗，则会先判断本地是否已经下载，如果下载则直接提示安装
- 支持设置自定义下载文件路径，如果不设置，则直接使用lib中的路径【file/downApk目录下】
- 当apk下载失败，异常，错误等状态，支持重启下载任务。功能十分强大，已经用于正式app多时，你采用拿来主义使用即可，欢迎提出问题。
- 弹窗DialogFragment异常时调用onSaveInstanceState保存状态，重启时取出状态



### 02.常见思路和做法



### 03.Api调用说明
#### 3.1 使用Api说明
- 代码如下所示，就是这么简单
    ``` java
    //设置自定义下载文件路径
    UpdateUtils.APP_UPDATE_DOWN_APK_PATH = "apk" + File.separator + "downApk";
    String  desc = getResources().getString(R.string.update_content_info);
    /**
     * 版本更新
     * @param isForceUpdate                     是否强制更新
     * @param apkUrl                            下载链接
     * @param apkName                           下载apk名称
     * @param desc                              更新文案
     * @param packageName                       包名
     * @param appMd5                            安装包md5值，传null表示不校验
     */
    UpdateFragment.showFragment(MainActivity.this,
                            false,firstUrl,apkName,desc,BuildConfig.APPLICATION_ID,"b291e935d3f5282355192f98306ab489");
    ```


#### 3.2 版本更新流程图
![image](https://upload-images.jianshu.io/upload_images/4432347-e7ba321e3201564c.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#### 3.3 效果图展示
![image](https://upload-images.jianshu.io/upload_images/4432347-e1d32a7fd02832f0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-1879cbf17fbe05fd.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-3ea6614052d7e54f.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-5ac2ce1fbc538880.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-06b8bed3d839ae0f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-6ddebd88af2947b8.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



### 04.遇到的坑分析
#### 4.1 lib库中解决了代码中安装APK文件异常问题
- 关于在代码中安装 APK 文件，在 Android N 以后，为了安卓系统为了安全考虑，不能直接访问软件，需要使用 fileProvider 机制来访问、打开 APK 文件。里面if 语句，就是区分软件运行平台，来对 intent 设置不同的属性。


#### 4.3 更新注意要点
- 注意需要申请读写权限，如果你要使用，可以自定定义通知栏下载UI布局，可以自己设置弹窗UI。这里就不适用正式项目中的UI和图标，图标是使用别人的，请勿商用。
- 针对8.0以后已经解决了通知栏不显示问题，[Notification封装库](https://github.com/yangchong211/YCNotification)，Notification通知栏用法介绍及部分源码解析，https://blog.csdn.net/m0_37700275/article/details/78745024
- 目前针对下载中的状态，有很多种，这里我只是选用了几种主要的状态处理版本更新逻辑。关于下载更多逻辑，可以参考这个下载库：[FileDownloader](https://github.com/lingochamp/FileDownloader)
    ```
     * 下载状态
     * START            开始下载
     * UPLOADING.       下载中
     * FINISH           下载完成，可以安装
     * ERROR            下载错误
     * PAUSED           下载暂停中，继续
    ```
- 关于断点下载逻辑。由于前项目主要是实现版本更新的逻辑，时间人力有限，因此直接使用FileDownloader库，无比强大。




### 05.该库其他介绍





- 参考：http://t.zoukankan.com/iplus-p-4467386.html
- 安装流程：https://www.jianshu.com/p/3a898e17b685



