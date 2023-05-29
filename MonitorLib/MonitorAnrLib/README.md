# ANR检测和捕获
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明




### 01.基础概念说明
- ANR-WatchDog 是检查 ANR （Android 无响应） 问题的利器。
    - 实现原理： ANR-WatchDog 创建一个监测线程，该线程不断往 UI 线程 post 一个任务，然后睡眠固定时间，等该线程重新起来后检测之前 post 的任务是否执行了，如果任务未被执行，则生成 ANRError, 并终止进程。




### 02.常见思路和做法
#### 2.1 核心思路
- ANR-WatchDog机制原理不复杂，它内部启动了一个子线程，定时通过主线程Handler发送Message，然后定时去检查Message的处理结果。
- 通俗来说就是利用了Android系统MessageQueue队列的排队处理特性。通过主线程Handler发送消息到MessageQueue队列，5秒去看下这个Message有没有被消费，如果消费了则代表没有卡顿，如果没有，则代表有卡顿，当然这个5秒是可调节的。
                                          

#### 2.2 如何监控ANR
- WatchDog为卡顿监控代码增加ANR的线程监控
    - 在发送消息时，在ANR线程中保存一个状态，主线程消息执行完后再Reset标志位。如果在ANR线程中收到发送消息后，超过一定时间没有复位，就可以任务发生了ANR。
- WatchDog监控ANR缺点
    - 无法准确判断是否真正出现ANR，只能说明APP发生了UI阻塞，需要进行二次校验。校验的方式就是等待手机系统出现发生了Error的进程，并且Error类型是NOT_RESPONDING（值为2）。
    - 在每次出现ANR弹框前，Native层都会发出signal为SIGNAL_QUIT（值为3）的信号事件，也可以监听此信号。
    - 无法得到完整ANR日志，隶属于卡顿优化的方式。


### 03.Api调用说明



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

