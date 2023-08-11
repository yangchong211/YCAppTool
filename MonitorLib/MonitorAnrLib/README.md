# ANR检测和捕获
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明




### 01.基础概念说明
#### 1.1 WatchDog是什么
- ANR-WatchDog 是检查 ANR （Android 无响应） 问题的利器。
    - 实现原理： ANR-WatchDog 创建一个监测线程，该线程不断往 UI 线程 post 一个任务，然后睡眠固定时间，等该线程重新起来后检测之前 post 的任务是否执行了，如果任务未被执行，则生成 ANRError, 并终止进程。


#### 1.2 ANR简单介绍
- ANR的简单理解
    - ANR Activity not responding(页面没有响应) ；ANR Application not responding 应用没有响应
    - Android系统对于一些事件需要在一定的时间范围内完成，如果超过预定时间能未能得到有效响应或者响应时间过长，都会造成ANR。


#### 1.3 为何会发生ANR
- ANR的产生需要满足三个条件
    - 主线程：只有应用程序进程的主线程响应超时才会产生ANR；
    - 超时时间：产生ANR的上下文不同，超时时间也会不同，但只要在这个时间上限内没有响应就会ANR；
    - 输入事件/特定操作：输入事件是指按键、触屏等设备输入事件，特定操作是指BroadcastReceiver和Service的生命周期中的各个函数，产生ANR的上下文不同，导致ANR的原因也会不同；
- 造成ANR的原因一般有两种：
    - 当前的事件没有机会得到处理（即主线程正在处理前一个事件，没有及时的完成或者looper被某种原因阻塞住了）
    - 当前的事件正在处理，但没有及时完成


#### 1.4 ANR发生的场景
- 分别有哪些场景
    - 主线程，被阻塞5秒钟以上，就会抛出ANR对话框。不同的组件发生ANR的时间不一样，主要是四大组件。
    - 点击事件(按键和触摸事件)5s内没被处理: Input event dispatching timed out
- 那些操作会引发ANR
    - 1.Activity，Fragment中暴力相应点击事件有可能会导致ANR；
    - 2.断点调试时，程序可能会出现ANR无限应；
    - 3.主线程做了耗时操作，比如查询数据库数据导致ANR


#### 1.5 四大组件ANR流程
- 四大组件触发ANR的步骤。
    - 大概是：埋下注册超时 ----> 触发超时 ----> 引发超时ANR
- 埋下注册超时
    - 调用startService，在Service进程attach到system_server进程的过程中会调用`realStartServiceLocked()`方法来注册超时
    - ----> ActiveServices 类 realStartServiceLocked 方法
    - ----> ActiveServices 类 bumpServiceExecutingLocked 方法
    - ----> ActiveServices 类 scheduleServiceTimeoutLocked 方法 ， handler发送延迟消息
    - ----> ActivityManagerService 类的 MainHandler 中，有处理handler延迟消息，具体看：SERVICE_TIMEOUT_MSG
- 触发超时，触发超时分析
    - 在system_server进程`AS.realStartServiceLocked()`调用的过程会埋下注册超时, 超时没有启动完成则会超时。
    - 那么什么时候会触发超时的引线呢? 经过Binder等层层调用进入目标进程的主线程handleCreateService()的过程。
    - ----> ActivityThread 类 handleCreateService 方法
    - ----> ActivityManagerService 类 serviceDoneExecuting 方法
    - ----> ActiveServices 类 serviceDoneExecutingLocked 方法
- 引发超时ANR，触发超时分析
    - 介绍了埋下注册超时和触发超时的过程, 如果在超时倒计时结束之前成功拆卸注册超时,那么就没有引发ANR的机会。
    - 但总有些极端情况下无法即时拆除注册超时,导致触发超时了, 其结果就是App发生ANR。
    - 在system_server进程中有一个Handler线程, 名叫”ActivityManager”。当倒计时结束便会向该Handler线程发送 一条信息SERVICE_TIMEOUT_MSG
    - ----> ActivityManagerService 类 handleMessage 方法
    - ----> ActiveServices 类 serviceTimeout 方法



### 02.常见思路和做法
#### 2.1 监控ANR思路
- 触发ANR都会涉及到消息机制
    - 埋下注册超时 ----> 触发超时 ----> 引发超时ANR。这个过程中，其实最终都是通过handler消息去实现的超时ANR触发。
- 监控ANR思路核心思路
    - 启动了一个子线程，定时通过主线程Handler发送Message，然后定时去检查Message的处理结果。
    - 通俗来说就是利用了Android系统MessageQueue队列的排队处理特性。通过主线程Handler发送消息到MessageQueue队列，5秒去看下这个Message有没有被消费，如果消费了则代表没有卡顿，如果没有，则代表有ANR了，当然这个5秒是可调节的。
                                          

#### 2.2 如何监控ANR
- WatchDog为卡顿监控代码增加ANR的线程监控
    - 在发送消息时，在ANR线程中保存一个状态，主线程消息执行完后再Reset标志位。如果在ANR线程中收到发送消息后，超过一定时间没有复位，就可以任务发生了ANR。
- WatchDog监控ANR缺点
    - 无法准确判断是否真正出现ANR，只能说明APP发生了UI阻塞，需要进行二次校验。校验的方式就是等待手机系统出现发生了Error的进程，并且Error类型是NOT_RESPONDING（值为2）。
    - 在每次出现ANR弹框前，Native层都会发出signal为SIGNAL_QUIT（值为3）的信号事件，也可以监听此信号。
    - 无法得到完整ANR日志，隶属于卡顿优化的方式。


### 03.Api调用说明
#### 3.1 最简单的调用API
- 如果在项目中集成，最简单操作
    ``` java
    new ANRWatchDog(5000).setANRListener(new ANRListener() {
        @Override
        public void onAppNotResponding(@NonNull ANRError error) {
            Throwable throwable = error.fillInStackTrace();
            AppLogUtils.d("ANRWatchDog", throwable);
        }
    }).start();
    ```


#### 3.2 其他功能API


### 04.遇到的坑分析
#### 4.1 WatchDog监控弊端
- 1.它会漏报情况，举个例子，比如我们以5秒未响应作为卡顿阈值，如果我们发送监听Message的时间在上一个消息处理的第2-5秒之间，那这种就会产生漏报。
- 2.监听间隔越小，系统开销越大。
- 3.即便监听到了，不好区分卡顿链路，无法提供准确的卡顿堆栈。




### 05.其他问题说明



### 参考博客
- anr日志生成与捕获方式分析
    - https://www.jianshu.com/p/7fa9080cb97e
- ANR异常监测组件 ANR-WatchDog
  - https://zhuanlan.zhihu.com/p/395239450
  - https://blog.csdn.net/qq_30379689/article/details/129056033

