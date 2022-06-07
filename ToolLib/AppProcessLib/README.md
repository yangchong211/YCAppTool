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



### 02.常见思路和做法
- 第一种：一个思路就是通过统计当前活动的 Activity 数目来计算。
    - 在 Activity#onStart 中来检测前一个状态是否是“后台”，如果是，则触发“切换到前台”事件，并将活动 Activity 数目加1。 
    - 在 Activity#onStop 中并将活动 Activity 数目减1。如果活动的 Activity 数目等于0，就认为当前应用处于“后台”状态，并触发“切换到后台”事件。
- 第二种：增强版本统计统计，借鉴官方代码ProcessLifecycleOwner前后台统计
    - 对您的应用程序进入前台或进入后台做出反应并且您不需要毫秒精度来接收生命周期事件的用例很有用。弥补第一种方案可能存在的不足……


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


### 03.遇到的坑分析
- 在关掉/点亮屏幕的情况下，不会触发 onStart 和 onStop 回调。只会触发 onPause 和 onResume。
    - 测试好像部分手机存在这种情况



