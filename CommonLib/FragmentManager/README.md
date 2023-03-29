# Fragment生命周期监听
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析




### 01.基础概念介绍
#### 1.1 业务背景介绍
- 业务背景说明
    - 作业帮智能App


#### 1.2 基础概念介绍
- FragmentLifecycleCallbacks
    -  如果使用的是 fragment，那么这里提供另一种获取生命周期的方法，通过FragmentLifecycleCallbacks可以获取到 fragment 的生命周期回调。



### 02.常见思路和做法
#### 2.1 多种实现思路方式
- 如何监听Fragment各个生命周期回调？
    - FragmentManager#registerFragmentLifecycleCallbacks()注册回调?
    - 实现LifecycleObserver接口？
    - 继承BaseFragment？
- 几种方式优缺点分析
    - XxxLifecycleCallbacks方式优点：可以统一监听所有Fragment，方便管理；不侵入已有代码，耦合性较低；可以操作第三方Fragment的声明周期。
    - XxxLifecycleCallbacks方式缺点：仅能在相应周期回调后操作，是这一方法唯一的缺点。
    - BaseFragment方式：这一方法就是实现一个基类Fragment，在里面实现一些通用的方法，让项目中的fragment都继承它。但是该方法缺点也很明显，由于Java的继承机制只允许一个类拥有唯一父类，所以该方法无法用于第三方框架也使用该方式的场景，并且侵入性强。



#### 2.2 registerFragmentLifecycleCallbacks
- 添加Fragment回调FragmentManager#registerFragmentlifecyclecallbacks()这一API是在support library version 25.1.0中添加的。
- FragmentManager的方法多了一个参数：boolean recursive,如果该参数为true，则会为所有子孙Fragment也注册回调。
- 首先，Fragment的生命周期由FragmentManager通过moveToState()方法调用。同样，这里以onFragmentCreated为例，其调用流程如下：
    - moveToState() -> dispatchOnFragmentCreated(),dispatchOnFragmentCreated()
    ``` java
    void dispatchOnFragmentCreated(@NonNull Fragment f,
            @Nullable Bundle savedInstanceState, boolean onlyRecursive) {
        // 遍历callback回调声明周期方法
        for (FragmentLifecycleCallbacksHolder holder : mLifecycleCallbacks) {
            if (!onlyRecursive || holder.mRecursive) {
                holder.mCallback.onFragmentCreated(mFragmentManager, f, savedInstanceState);
            }
        }
    }
    ```




### 03.Api调用说明
- api调用如下所示，直接拿来用即可
    ``` java
    //一般在onCreate的方法中注册
    FragmentManager.Companion.getInstance().registerActivityLifecycleListener(activity,lifecycleListener);
    //一般在onDestroy的方法中解绑注册
    FragmentManager.Companion.getInstance().unregisterActivityLifecycleListener(activity,lifecycleListener);
    ```
- 关于fragment生命周期回调，这个FragmentLifecycleListener是一个抽象类，包含了绝大多数常见的方法，可根据自己的需求自由实现
    ``` java
    private final FragmentLifecycleListener lifecycleListener = new FragmentLifecycleListener() {
        @Override
        public void onFragmentCreated(@NotNull androidx.fragment.app.FragmentManager fm, @NotNull Fragment f, @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
            super.onFragmentCreated(fm, f, savedInstanceState);
        }
    
        @Override
        public void onFragmentDestroyed(@NotNull androidx.fragment.app.FragmentManager fm, @NotNull Fragment f) {
            super.onFragmentDestroyed(fm, f);
        }
    };
    ```




### 04.遇到的坑分析






