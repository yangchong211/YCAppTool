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
- App壳工程依赖很多自己封装的基础库，那么如何将基础库中的catch异常上报到APM平台上呢？
    - 基础库作为一个功能比较独立的lib，总不可能依赖APM组件吧。因此，就采用抽象类隔离！App壳工程继承抽象类，lib库直接调用抽象类。
- 举一个简单的例子说明该思路
    - 比如，我在视频播放器库依赖接口层(被壳工程依赖)，然后调用代码如下所示
    ``` java
    ExceptionReporter.report("Unable to get from disk cache-", e);
    ```
    - 然后，在app壳工程中具体操作如下所示
    ``` java
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


### 03.Api调用说明
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




### 04.遇到的坑分析





