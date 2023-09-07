# 崩溃统计库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析



### 01.基础概念介绍
#### 1.1 为何崩溃推出App
- 线程中抛出异常以后的处理逻辑
    - 一旦线程出现抛出异常，并且在没有捕捉的情况下，`JVM`将调用`Thread`中的`dispatchUncaughtException`方法把异常传递给线程的未捕获异常处理器。
- 找到Android源码中处理异常捕获入口
    - 既然`Android`遇到异常会发生崩溃，然后找一些哪里用到设置`setDefaultUncaughtExceptionHandler`，即可定位到`RuntimeInit`类。
    - 即在这个里面设置异常捕获`KillApplicationHandler`，发生异常之后，会调用`handleApplicationCrash`打印输出崩溃`crash`信息，最后会杀死应用`app`。


#### 1.2 崩溃的大概流程
- 然后看一下`RuntimeInit`类，由于是java代码，所以首先找`main`方法入口。代码如下所示
    ``` java
    public static final void main(String[] argv) {
        commonInit();
    }
    ```
- 然后再来看一下`commonInit()`方法，看看里面做了什么操作？
    - 可以发现这里调用了`setDefaultUncaughtExceptionHandler`方法，设置了自定义的`Handler`类
    ``` java
    protected static final void commonInit() {
        Thread.setDefaultUncaughtExceptionHandler(new KillApplicationHandler(loggingHandler));
    }
    ```
- 接着看一下`KillApplicationHandler`类，可以发现该类实现了`Thread.UncaughtExceptionHandler`接口
    - 这个就是杀死app逻辑具体的代码。可以看到当出现异常的时候，在finally中会退出进程操作。
    ``` java
    private static class KillApplicationHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            try {
                ActivityManager.getService().handleApplicationCrash(
                        mApplicationObject, new ApplicationErrorReport.ParcelableCrashInfo(e));
            } finally {
                Process.killProcess(Process.myPid());
                System.exit(10);
            }
        }
    }
    ```
- 得出结论如下所示
    - 其实在`fork`出`app`进程的时候，系统已经为`app`设置了一个异常处理，并且最终崩溃后会直接导致执行该`handler`的`finally`方法最后杀死app直接退出app。
- 如何自己捕获App异常
    - 如果你要自己处理，你可以自己实现`Thread.UncaughtExceptionHandler`。而调用`setDefaultUncaughtExceptionHandler`多次，最后一次会覆盖之前的。


#### 1.3 崩溃日志


### 02.常见思路和做法



#### 2.5 统计崩溃次数
- 如何统计某个崩溃异常呢？
    - 举一个简单例子，Attempt to invoke virtual method 'boolean android.app.Activity.isDestroyed()'，统计该页面崩溃次数该怎么做呢？



### 03.Api调用说明



### 04.遇到的坑分析





