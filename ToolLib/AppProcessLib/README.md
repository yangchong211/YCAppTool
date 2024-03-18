# 前后台判断工具
#### 目录介绍
- 01.基础概念
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念
#### 1.1 前后台概念
- 什么叫“前台”，什么叫“后台”。本文定义如下：
    - 前台，Activity 处在 FOREGROUND 优先级
    - 后台，App进程没有停止，除去在“前台”的所有情况



#### 1.2 推到后台方式
- 退到后台的方式太多了，大致有：
    - 按Home键
    - 按“最近任务”键
    - 从通知栏启动其他应用
    - 从应用内部启动其他应用
    - 关掉屏幕
- 业务场景分析
    - 监听前后台的目的主要是要做一些业务逻辑，但是有的业务逻辑针对的是整个项目，有的业务逻辑可能只是针对某一个或者某几个类，所以当业务逻辑涉及到仅仅某些类的话，可以采用接口回调。


#### 1.3 App回到桌面周期
- 从app退到桌面会执行到的生命周期是：
    - onPause---->onStop




### 02.常见思路和做法
#### 2.1 方案对比分析
- 第一种：一个思路就是通过统计当前活动的 Activity 数目来计算。
    - 在 Activity#onStart 中来检测前一个状态是否是“后台”，如果是，则触发“切换到前台”事件，并将活动 Activity 数目加1。 
    - 在 Activity#onStop 中并将活动 Activity 数目减1。如果活动的 Activity 数目等于0，就认为当前应用处于“后台”状态，并触发“切换到后台”事件。
- 第二种：增强版本统计统计，借鉴官方代码ProcessLifecycleOwner前后台统计
    - 对您的应用程序进入前台或进入后台做出反应并且您不需要毫秒精度来接收生命周期事件的用例很有用。弥补第一种方案可能存在的不足……
- 第三种：利用ActivityManager的RunningAppProcessInfo类
    - RunningAppProcessInfo类 则封装了正在运行着的进程信息，当然也包含了正在运行的app的包名，因此我们可以 getRunningAppProcesses() 获取当前运行的app列表，对比自身的包名，来判断本身app是否处于前台运行。但消耗比较大
- 第四种：监听Home键点击
    - 当home键被点击的时候，会发出一个系统广播，在系统收到这个广播以后，会在framework层做一系列操作将当前的app退到后台，然后把事件消费掉不传给应用层，所以这时候 onKeyDown事件也接收不到了…


#### 2.2 选择方案总结
- 总结：
    - 1、利用ActivityManager的RunningAppProcessInfo类，直接粗暴，官方摒弃，不推荐；
    - 2、监听Home键点击，官方不支持，兼容性差，不稳定，不推荐；
    - 3、利用ActivityLifecycleCallbacks监听所有activity的生命周期，
- 官方建议
    - 判断前后台，使用ActivityLifecycleCallbacks实现，但是需要注意，建议结合onStart 和 onStop 回调，onPause 和 onResume两种回调综合判断



### 03.Api调用说明
- 第一步：初始化
    ``` java
    AppStateLifecycle.getInstance().init(context);
    ```
- 第二步：添加前后台监听，可以添加多处监听
    ``` java
    AppStateLifecycle.getInstance().registerStateListener(new StateListener() {
        @Override
        public void onInForeground() {
            AppLogUtils.i("app lifecycle state in foreground");
        }
    
        @Override
        public void onInBackground() {
            AppLogUtils.i("app lifecycle state in background");
        }
    });
    ```
- 第三步：获取前后台状态
    ``` java
    //判断是否在后台
    boolean inBackground = AppStateLifecycle.getInstance().isInBackground();
    //判断是否在前台
    boolean inForeground = AppStateLifecycle.getInstance().isInForeground();
    ```


### 04.遇到的坑分析
#### 4.1 屏幕关闭和点亮
- 在关掉/点亮屏幕的情况下，不会触发 onStart 和 onStop 回调。只会触发 onPause 和 onResume。
    - 测试好像部分手机存在这种情况


#### 4.2 多进程前后台问题
- 多进程app前后台判断可能遇到问题
    - 这个在网络上也找不到比较好的方案


#### 4.3 前后台生命周期不执行
- 软件逻辑应该是这样：
    - 当用户退到后台后，执行onStop方法。当用户切换回来时，执行onResume方法。
- 遇到问题描述：
    - 当退出应用又立马切回来的时候，并不会调用onStop和onResume方法。
    - 反反复复试了好几遍，就是存在这个问题，当切换的时间太短时，就监听不到了。导致前后台判断失误……
- 如何避免前后台误判
    - 当你在app内部新开一个Activity或者销毁Activity时，都会引起Activity调用onPause，onStop这些方法。
    - 所以，当接受到onPause，onStop这些事件时，并不知道是来自用户退到后台，还是来自app内部的Activity变化。
    - 因此，这里才设置了一个延时，如果是app内部Activity变动，那么一个Activity的onPause必然会伴随另一个Activity的onResume。




### 05.其他问题说明
#### 5.1 为何增加延时判断
- App内部activity活动或者App推到后台
    - onPause方法都会最先被调用，如果太长时间没有调用onResume，就认为是退到后台，如果很快onResume就被调用了，那就认为这只是app内部的activity在变化。这就是设置延时的作用。
- 这个时间设置多长
    - 这个时长是估算得到的。activity启动时间一般是500到800毫秒(从start到onResume)，销毁的时长一般是300到400毫秒(从finish到onDestroy)。



