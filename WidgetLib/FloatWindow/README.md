# 悬浮窗工具库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明
- Window 有三种类型，分别是应用 Window、子 Window 和系统 Window。
    - 应用Window：z-index在1~99之间，它往往对应着一个Activity。
    - 子Window：z-index在1000~1999之间，它往往不能独立存在，需要依附在父Window上，例如Dialog等。
    - 系统Window：z-index在2000~2999之间，它往往需要声明权限才能创建，例如Toast、状态栏、系统音量条、错误提示框都是系统Window。
- 这些层级范围对应着 WindowManager.LayoutParams 的 type 参数
    - 如果想要 Window 位于所有 Window 的最顶层，那么采用较大的层级即可，很显然系统 Window 的层级是最大的。
- Android显示系统分为3层
    - UI框架层：负责管理窗口中View组件的布局与绘制以及响应用户输入事件
    - WindowManagerService层：负责管理窗口Surface的布局与次序
    - SurfaceFlinger层：将WindowManagerService管理的窗口按照一定的次序显示在屏幕上



### 02.常见思路和做法
- 目前开发悬浮窗的方案有以下几种
    - 第一种：写在base里面或者监听所有activity生命周期，这样每次启动一个新的Activity都要往页面上addView一次。
    - 第二种：采用在Window上添加View的形式，相当于是全局性的悬浮窗。
- 应用内悬浮窗实现流程
    - 第一个是获取WindowManager，然后设置相关params参数。注意配置参数的时候需要注意type
    - 第二个是添加xml或者自定义view到windowManager上
    - 第三个是处理拖拽更改view位置的监听逻辑，分别在down，move，up三个事件处理业务
    - 第四个是吸附左边或者右边，大概的思路是判断手指抬起时候的点是在屏幕左边还是右边
- 关于悬浮窗的权限
    - 当API<18时，系统默认是有悬浮窗的权限，不需要去处理；
    - 当API >= 23时，需要在AndroidManifest中申请权限，为了防止用户手动在设置中取消权限，需要在每次使用时check一下是否有悬浮窗权限存在；
    ``` java
    Settings.canDrawOverlays(this)
    ```
    - 当API > 25时，系统直接禁止用户使用TYPE_TOAST创建悬浮窗。
    ``` java
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    ```


### 03.Api调用说明
- 创建悬浮窗
    ``` java
    floatWindow = new FloatWindow(this.getApplication())
        .setContentView(R.layout.float_window_view)
        .setSize(200, 400)
        .setGravity(Gravity.END | Gravity.BOTTOM, 0, 200)
        // 设置指定的拖拽规则
        .setDraggable(new SpringTouchListener())
        .setOnClickListener(R.id.icon, new IClickListener() {
            @Override
            public void onClick(FloatWindow toast, View view) {
                ToastUtils.showRoundRectToast("我被点击了");
            }
        });
    ```
- 展示和隐藏
    ``` java
    floatWindow.show();
    floatWindow.dismiss();
    ```



### 04.遇到的坑分析
#### 4.1 处理输入法层级关系
- 先看一下问题
    - 微信里的悬浮窗是在输入法之下的，所以交互的同学也要求悬浮窗也要在输入法之下。查看了一下WindowManager源码，悬浮窗的优先级TYPE_APPLICATION_OVERLAY，上面大字写着明明是在输入法之下的，但是实际表现是在输入法之上。


#### 4.2 边界逻辑关闭悬浮窗
- 先看一下问题
    - 谷歌坑人的地方，都没地方设置这个悬浮窗是否只用到app内，所以默认在桌面上也会显示自己的悬浮窗。
    - 比如在微信里显示其他app的悬浮窗，这种糟糕的体验可想而知，用户不给你卸载就真是奇迹了。
- 尝试解决这个问题
    - 为了解决这个问题，最初的实现方式是对所有经过的activity进行记录，显示就加1，页面被挂起就减1，如果减到当前计数为0时说明所有页面已经关闭了，就可以隐藏悬浮窗了。
    - 实际上这么做还是有问题的，在部分手机上如果是在首页按返回键的话仍然不能隐藏，这个又是系统级的兼容性问题。
    - 为了解决这问题，后面又做了一个处理，通过注册registerActivityLifecycleCallbacks监听app的前后台回调，检测到如果当前首页被销毁时，应该将悬浮窗进行隐藏。


#### 4.3 点击多次打开页面
- 问题说明一下
    - 如果你的悬浮窗点击事件是打开页面的话，这里需要注意了，别忘了将这个打开的页面的启动模式设置为singleTop或者是singleTask，从而复用同一个，远离一直按返回的地狱操作。



### 05.其他问题说明
#### 5.1 区分点击和移动
- 用户在手机上操作后会经历那些事件
    - 当用户对应用内一个View控件操作时，我们在程序中对View进行绑定Touch事件的监听(android.view.View.OnTouchListener)后会发现。
    - 用户手指的一次完整操作，一定会经过3个基本的事件，分别是：Down事件(按下)，Move事件(移动)，Up事件(抬起)
- 当用户手指离开屏幕的时候，如何来判断用户的操作意图呢，是点击还是滑动？
    - 可以通过对Move事件移动的距离来判断用户是点击还是滑动操作。主要是获取最小的触摸距离，然后判断手指移动前后的距离，如果大于最小值则是移动



### 参考博客
