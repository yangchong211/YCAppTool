# 多屏幕技术库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.副屏幕技术原理


### 01.基础概念介绍
#### 1.1 什么是多屏开发
- 官方文档如下所示
    - https://developer.android.google.cn/reference/android/app/Presentation#public-constructors


#### 1.2 多屏开发核心
- 双屏异显第一种实现方式
    - Android 提供了一个叫 Presentation 类，来实现第二屏， 继承 Presentation 实现第二屏，相当于一个特殊的弹窗窗口（具体实现）


#### 1.3 双屏原理说明
- Android 双屏原理大概如下
    - 自定义一个Presentation类，Android 的标准实现是使用 API Presentation 来实现异显的功能。
    - Presentation 是扩展自 dialog.相当于一个升级版的弹窗，其实还是在同一个activity里面完成的，两个屏幕里面的内容，可以通过同一个activity进行控制和相关数据变动展示。在副屏上显示不同内容。
    - 它的显示内容是依附在主屏的Activity上的，如果Activity被销毁Presentation也不会再显示，主副屏内容会再次恢复成相同的页面。



### 02.常见思路和做法



#### 2.5 副屏幕需要权限
- Android中的副屏开发Android版本需要权限
    ```
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    <uses-permission android:name="android.permission.SYSTEM_OVERLAY_WINDOW" />
    ```



### 03.Api调用说明




### 04.遇到的坑分析
#### 4.1 注意是否存在副屏



### 05.副屏幕技术原理





### 参考博客
- Android三种双屏异显实现方法介绍
  - https://www.ab62.cn/article/35027.html
- Android 副屏开发
  - https://blog.csdn.net/weixin_46444429/article/details/104722967







