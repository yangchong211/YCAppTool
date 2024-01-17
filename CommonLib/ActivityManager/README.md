#### 目录介绍
- 01.基础概念说明
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明
- 06.技术问题思考&分析




### 01.基础概念
#### 1.0 业务场景说明
- 业务场景说明
    - 对所有的Activity进行栈管理，方便我们对某一个Activity操作或者是对所有的Activity进行操作。还要做到对制定Activity生命周期监听！
    - 比如：在非四大组件的逻辑中，可以通过全局栈管理获取到栈顶Activity引用上下文
    - 比如：双击推出App操作，使用栈管理可以finish掉所有Activity，然后在exit推出。



#### 1.1 掌握基础概念
- activity任务栈概念
    - Android是通过将之前的activity组件和新被激活的activity组件放入同一个任务栈来实现这个功能的。
    - 从用户的角度看，一个任务栈就代表了“一个应用程序”。它实际上是一个栈，里面放着一组被排列好的相关的activity组件。
    - 位于栈底的activity（根activity）就是开启这个任务栈的activity组件，一般情况下，就是应用程序的主界面。而位于栈顶的activity组件即代表当前被激活的activity组件（可接收用户行为的activity）。


#### 1.2 理解任务栈
- 任务栈有什么特性
    - 任务栈中可以包含有某一个activity组件类型的多个实例对象。在任务栈中的activity组件不能被重排序，只能被压栈和出栈。
- 如何理解多个任务栈
    - App会存在多个任务栈吗？可以的，比如开启单例启动模式，则会单独给它开启一个任务栈。


#### 1.3 该库的特点
- 非常好用的activity任务栈管理库
    - 自动化注册。完全解耦合的activity栈管理，拿来即可用，或者栈顶Activity，移除添加，推出某个页面，获取应用注册Activity列表等，可以注册监听某个页面的生命周期，小巧好用。


### 02.常见思路和做法
#### 2.1 常见的实现方式
- 目前市面上较为通用的方式
    - 第一种：写一个activity栈工具类，然后在BaseActivity类中使用工具类，在onCreate添加栈，在onDestroy中移除栈。
    - 第二种：在XxxApplication中添加registerActivityLifecycleListener注册监听，监听所有activity，然后在onCreate添加栈，在onDestroy中移除栈。
    - 第三种：做成单独库组件，是第二种方式的优化版本。定义Activity栈的接口，比如添加，移除，查找，关闭等，基于接口开发。完全解耦合！
    - 第四种：采用实现LifecycleObserver接口，然后使用注解的方式监听Activity生命周期，可以在base类种处理。
- 各种方式的优缺点分析
    - 第一种方式：需要在base类种处理，如果某activity不是继承base类，则会存在一些问题。针对跳转三方库activity也比较难处理！
    - 第二种方式：这种方式比较常见，但是相对来说使用工具类添加和移除activity，代码上也没什么问题。
    - 第三种方式：无需开发人员添加和移除操作，所有逻辑在库种处理，完全和开发层解耦合，且小巧好用，可以快速在新业务线App上使用



#### 2.2 选择合适存储容器
- 启动模式的结构——栈
    - Activity的管理是采用任务栈的形式，任务栈采用“后进先出”的栈结构。
- 由于任务栈特性是先进后出(FILO)
    - 因此可以选择使用Stack作为集合存储Activity对象。




### 03.Api调用说明
#### 3.1 如何依赖
- 直接在maven中依赖如下所示：
    ```
    //activity栈管理
    implementation 'com.github.yangchong211.YCCommonLib:ActivityManager:1.4.9'
    ```


#### 3.2 如何使用Api
- api调用如下所示，直接拿来用即可
    ```
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
    ```
- 如果想监听某个单独activity的生命周期，该怎么做呢？
    ``` java
    //监听某个activity的生命周期，完全解耦合
    ActivityManager.getInstance().registerActivityLifecycleListener(CommonActivity.class, new AbsLifecycleListener() {
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



### 05.其他问题说明
#### 5.1 问题思考一下
- 针对一个App有多个任务栈，如何处理栈的查找逻辑？比如A，B栈，A栈有6个元素，B栈有1个元素


#### 5.2 Activity栈设计
- 栈管理的简单介绍
    - 一个应用程序通常包含多个Activity。每个Activity在设计时都应该以执行某个用户发起的 action 作为核心目标，并且它能启动其它Activity。 
    - Task就是多个Activity的集合，用户操作时与Activity进行交互。这些Activity根据启动顺序压入task中，如果pop task 是按照先进后出的顺序pop。
- Task任务栈的管理
    - Activity依照顺序压入Task并按照“先进后出”的顺序pop，对Task的管理显得十分的必要。
    - taskAffinity属性：如果不设置此属性，默认一个应用程序只有一个栈，这个栈以应用包命为单位。task对于Activity来说就好像它的身份证一样，可以告诉所在的task，自己属于这个task中的一员；拥有相同affinity的多个Activity理论同属于一个task，task自身的affinity决定于根Activity的affinity值。
    - affinity在什么场合应用呢？1.根据affinity重新为Activity选择宿主task；2.启动一个Activity过程中Intent使用了FLAG_ACTIVITY_NEW_TASK标记，根据affinity查找或创建一个新的具有对应affinity的task
- ActivityTask和Activity栈管理
    - http://www.lxiaoyu.com/p/324616
    - https://blog.csdn.net/heng615975867/article/details/108725469











