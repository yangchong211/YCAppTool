# 异常&事件&日志上报库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析



### 01.基础概念介绍
- 关于事件埋点，异常上报到APM，日志打印到file文件等。这些比较重的逻辑一般写在app壳工程，而想统计一些基础库中的埋点事件，catch异常上报APM，该怎么做？
- lib封装库尽可能小和功能独立，不建议依赖APM，埋点库等。我想要一个功能库，你却给我一个大礼包库，想想这个是不是太难了。
- 因此，定义了基础接口上报库，异常上报&埋点事件上报&log日志上报等。都可以采用抽象接口+实现类去实现lib数据统计……




### 02.常见思路和做法
#### 2.1 如何上报日志或崩溃
- App壳工程依赖很多自己封装的基础库，那么如何将基础库中的catch异常上报到APM平台上呢？
    - 基础库作为一个功能比较独立的lib，总不可能依赖APM组件吧。因此，就采用抽象类隔离！App壳工程继承抽象类，lib库直接调用抽象类。
- 举一个简单的例子说明该思路
    - 比如，我在视频播放器库依赖接口层(被壳工程依赖)，然后调用代码如下所示
    ``` java
    ExceptionReporter.report("Unable to get from disk cache-", e);
    ```
    - 然后，在app壳工程中具体操作如下所示
    ``` java
    ExceptionReporter.setExceptionReporter(ExceptionHelperImpl())
    public class ExceptionReporterImpl extends ExceptionReporter {
        @Override
        protected void reportCrash(Throwable throwable) {
            //壳工程中可以拿到APM，比如上传到bugly平台上
        }
    
        @Override
        protected void reportCrash(String tag, Throwable throwable) {
    
        }
    }
    ```


#### 2.2 如何在lib中调用壳中权限说明弹窗
- 针对申请隐私权限需要弹窗说明
    - 比如以前可以在app启动的时候，一下子申请完所有权限。但是这种已经不符合规范了，要求必须在使用的时候申请权限，并且要有弹窗说明。一句话概括为那里使用那里申请权限！
- 举一个例子加深理解
    - 比如你有一个二维码扫描库，你在扫描的时候需要申请相机权限，并且先要弹出弹窗说明文案；比如你有个相册库，你在打开相册的时候，需要申请读写权限。
- 二维码库和相册库已经自己申请权限，如何复用壳工程中的权限说明弹窗？
    - 具体方案：采用接口隔离，具体的实现类放到壳工程中实现。具体可以看看我的这个基础空间通用接口库：[EventUploadLib](https://github.com/yangchong211/YCAppTool/tree/master/CommonLib/EventUploadLib)



### 03.Api调用说明
#### 3.1 上报lib库中异常&日志到壳工程
- api调用如下所示，直接拿来用即可。下面这些调用可以用在lib库中，特轻量级上报
    ``` java
    //上报日志
    LoggerReporter.report("DiskLruCacheHelper" , "lru disk file path : " + directory.getPath());
    LoggerReporter.report("DiskLruCacheHelper clear cache");
    //上报异常
    ExceptionReporter.report("Unable to get from disk cache", e);
    ExceptionReporter.report(ioe);
    //上报event事件，通常用来埋点
    EventReporter.report("initCacheSuccess")
    EventReporter.report("initCacheSuccess",map)
    ```
- 需要在壳工程代码中，添加具体实现类。代码如下所示：
    ``` java
    //LoggerReporter，ExceptionReporter，EventReporter都是类似的
    public class LoggerReporterImpl extends LoggerReporter {
        @Override
        protected void reportLog(String eventName) {
            
        }
    
        @Override
        protected void reportLog(String eventName, String message) {
    
        }
    }
    ```
- 最后在壳工程中初始化一下。
    ``` java
    ExceptionReporter.setExceptionReporter(ExceptionHelperImpl())
    LoggerReporter.setEventReporter(LoggerReporterImpl())
    EventReporter.setEventReporter(EventReporterImpl())
    ```


#### 3.2 上报lib库中权限弹窗到壳工程
- 在lib库中调用如下代码：
    ```
    //相机权限
    PermissionDialog.permissionDialog(1, new PermissionListener() {
        @Override
        public void dialogClickSure() {
            ActivityCompat.requestPermissions(ISCameraActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    
        @Override
        public void dialogClickCancel() {
    
        }
    });
    ```
- 在壳工程代码【依赖了该库】写一个具体实现类
    ``` java
    public class PermissionDialogImpl extends PermissionDialog {
        @Override
        protected void showPermissionDialog(int type, PermissionListener listener) {
            if (type ==1 ){
                //弹出读写权限说明弹窗，然后根据弹窗的按钮触发，通过listener回调给lib库
                listener.dialogClickSure();
            } else if (type == 2){
                //弹出获取相机权限说明弹窗
                listener.dialogClickCancel();
            }
        }
    }
    ```
- 需要注意的是，必须在使用前初始化一下
    ``` java
    PermissionDialog.setPermissionDialog(new PermissionDialogImpl());
    ```


### 04.遇到的坑分析





