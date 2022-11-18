# 悬浮窗工具库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明
#### 1.1 什么是转场动画
- Activity 和 Fragment 变换是建立在名叫 Transitions 的安卓新特性之上的。为在不同的 UI 状态之间产生动画效果提供了非常方便的 API。
- Transition 可以被用来实现 Activity 或者 Fragment 切换时的异常复杂的动画效果。


#### 1.2 转场动画概念
- 转场动画核心概念
    - 场景（scenes）和变换（transitions）。场景（scenes）定义了当前的 UI 状态；变换（transitions）则定义了在不同场景之间动画变化的过程。
- 当一个场景改变的时候，transition 主要负责：
    - （1）捕捉在开始场景和结束场景中每个 View 的状态。（2）根据视图一个场景移动到另一个场景的差异创建一个 Animator。


#### 1.3 转场动画场景
- 转场动画听名字就知道它的使用场景了，转场、转场自然是用在场景转换的时候：
    - 转场效果我们一般用在 Activity 切换时的动画效果上；
    - 共享元素一般我们使用在转换的前后两个页面有共同元素时；
    - 同时也可以在 Activity 布局发生场景变化时，让其中的 View 产生相应的过渡动画。
- 在介绍 activity 的切换动画之前我们先来说明一下实现切换 activity 的两种方式：
    - 调用 startActivity 方法启动一个新的 Activity 并跳转其页面。这涉及到了旧的 Activity 的退出动画和新的 Activity 的显示动画。
    - 调用 finish 方法销毁当前的 Activity 返回上一个 Activity 界面。这涉及到了当前 Activity 的退出动画和前一个 Activity 的显示动画。




### 02.常见思路和做法
#### 2.1 转场动画实现的几种方式
- 第一种：activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)，这个是在startActivity 方法或者是 finish 方法调用之后立即执行。
- 第二种：使用 style 的方式定义 Activity 的切换动画。这里的 windowAnimationStyle 就是我们定义 Activity 切换动画的 style，设置这个动画文件则可以为Activity 的跳转配置动画效果。
- 第三种：使用 ActivityOptions 切换动画实现 Activity 跳转动画。Material Design转场动画，三种进入和退出过渡动画效果(Explode，Slide，Fade)，然后开启activity时传递ActivityOptions.makeSceneTransitionAnimation
- 第四种：使用 ActivityOptions 动画共享组件的方式实现跳转 Activity 动画。需要注意共享视图设置的transitionName保持一致
- 第五种：使用 ActivityOptions 之后内置的动画效果通过 style 的方式。
- 关于转场动画，可以看看这篇博客：[Android动画系列---转场动画详解](https://www.jianshu.com/p/78fdcceb9a59)


#### 2.2 Transition实现原理



#### 2.3 View到Activity动画
- 



### 03.Api调用说明



### 04.遇到的坑分析




### 05.其他问题说明




### 参考博客
