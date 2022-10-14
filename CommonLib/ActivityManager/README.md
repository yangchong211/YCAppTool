#### 目录介绍
- 01.基础概念
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析



### 01.基础概念
- activity任务栈概念
    - Android是通过将之前的activity组件和新被激活的activity组件放入同一个任务栈来实现这个功能的。
    - 从用户的角度看，一个任务栈就代表了“一个应用程序”。它实际上是一个栈，里面放着一组被排列好的相关的activity组件。位于栈底的activity（根activity）就是开启这个任务栈的activity组件，一般情况下，就是应用程序的主界面。而位于栈顶的activity组件即代表当前被激活的activity组件（可接收用户行为的activity）。
- 任务栈有什么特性
    - 任务栈中可以包含有某一个activity组件类型的多个实例对象。在任务栈中的activity组件不能被重排序，只能被压栈和出栈。
- 非常好用的activity任务栈管理库
    - 自动化注册。完全解耦合的activity栈管理，拿来即可用，或者栈顶Activity，移除添加，推出某个页面，获取应用注册Activity列表等，可以注册监听某个页面的生命周期，小巧好用。


### 02.常见思路和做法
- 由于任务栈特性是先进后出(FILO)
    - 因此可以选择使用Stack作为集合存储Activity对象


### 03.Api调用说明
#### 3.1 如何依赖


#### 3.2 如何使用Api
- api调用如下所示，直接拿来用即可
    ``` java
    //退出应用程序
    ActivityManager.getInstance().appExist();
    //查找指定的Activity
    Activity commonActivity = ActivityManager.getInstance().get(CommonActivity.class);
    //判断界面Activity是否存在
    boolean exist = ActivityManager.getInstance().isExist(CommonActivity.class);
    //移除栈顶的activity
    ActivityManager.getInstance().pop();
    //结束所有Activity
    ActivityManager.getInstance().finishAll();
    //结束指定的Activity
    ActivityManager.getInstance().finish(CommonActivity.this);
    //判断activity任务栈是否为空
    ActivityManager.getInstance().isEmpty();
    //获取栈顶的Activity
    Activity activity = ActivityManager.getInstance().peek();
    //判断activity是否处于栈顶
    ActivityManager.getInstance().isActivityTop(this,"MainActivity");
    //添加 activity 入栈
    ActivityManager.getInstance().add(CommonActivity.this);
    //移除 activity 出栈
    ActivityManager.getInstance().remove(CommonActivity.this);
    //监听某个activity的生命周期，完全解耦合
    ActivityManager.getInstance().registerActivityLifecycleListener(CommonActivity.class, new ActivityLifecycleListener() {
        @Override
        public void onActivityCreated(@Nullable Activity activity, Bundle savedInstanceState) {
            super.onActivityCreated(activity, savedInstanceState);
        }
    
        @Override
        public void onActivityStarted(@Nullable Activity activity) {
            super.onActivityStarted(activity);
        }
    });
    //移除某个activity的生命周期，完全解耦合
    //ActivityManager.getInstance().registerActivityLifecycleListener(CommonActivity.this,listener);
    ```



### 04.遇到的坑分析






