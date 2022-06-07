#### 目录介绍
- 01.基础概念
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析


### 01.基础概念
- 什么叫“前台”，什么叫“后台”。本文定义如下：
    - 前台，Activity 处在 FOREGROUND 优先级
    - 后台，App进程没有停止，除去在“前台”的所有情况
- 退到后台的方式太多了，大致有：
    - 按Home键
    - 按“最近任务”键
    - 从通知栏启动其他应用
    - 从应用内部启动其他应用
    - 关掉屏幕
- 业务场景分析
    - 监听前后台的目的主要是要做一些业务逻辑，但是有的业务逻辑针对的是整个项目，有的业务逻辑可能只是针对某一个或者某几个类，所以当业务逻辑涉及到仅仅某些类的话，可以采用接口回调。


### 02.常见思路和做法
- 第一种：一个思路就是通过统计当前活动的 Activity 数目来计算。
    - 在 Activity#onStart 中来检测前一个状态是否是“后台”，如果是，则触发“切换到前台”事件，并将活动 Activity 数目加1。 
    - 在 Activity#onStop 中并将活动 Activity 数目减1。如果活动的 Activity 数目等于0，就认为当前应用处于“后台”状态，并触发“切换到后台”事件。
- 第二种：增强版本统计统计，借鉴官方代码ProcessLifecycleOwner前后台统计
    - 对您的应用程序进入前台或进入后台做出反应并且您不需要毫秒精度来接收生命周期事件的用例很有用。弥补第一种方案可能存在的不足……
- 第三种：利用ActivityManager的RunningAppProcessInfo类
    - RunningAppProcessInfo类 则封装了正在运行着的进程信息，当然也包含了正在运行的app的包名，因此我们可以 getRunningAppProcesses() 获取当前运行的app列表，对比自身的包名，来判断本身app是否处于前台运行。但消耗比较大
- 第四种：监听Home键点击
    - 当home键被点击的时候，会发出一个系统广播，在系统收到这个广播以后，会在framework层做一系列操作将当前的app退到后台，然后把事件消费掉不传给应用层，所以这时候 onKeyDown事件也接收不到了…
- 总结：
    - 1、利用ActivityManager的RunningAppProcessInfo类，直接粗暴，官方摒弃，不推荐；
    - 2、监听Home键点击，官方不支持，兼容性差，不稳定，不推荐；
    - 3、利用ActivityLifecycleCallbacks监听所有activity的生命周期，


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
- 在关掉/点亮屏幕的情况下，不会触发 onStart 和 onStop 回调。只会触发 onPause 和 onResume。
    - 测试好像部分手机存在这种情况
- 多进程app前后台判断可能遇到问题
    - 这个在网络上也找不到比较好的方案


