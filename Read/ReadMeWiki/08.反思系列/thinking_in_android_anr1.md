# 反思｜Android 输入系统 & ANR机制的设计与实现

> **反思** 系列博客是我的一种新学习方式的尝试，该系列起源和目录请参考 [这里](https://github.com/qingmei2/android-programming-profile/blob/master/src/%E5%8F%8D%E6%80%9D%E7%B3%BB%E5%88%97/%E5%8F%8D%E6%80%9D%7C%E7%B3%BB%E5%88%97%E7%9B%AE%E5%BD%95.md) 。

## 概述

对于`Android`开发者而言，`ANR`是一个老生常谈的问题，站在面试者的角度，似乎说出 **「不要在主线程做耗时操作」** 就算合格了。

但是，`ANR`机制到底是什么，其背后的原理究竟如何，为什么要设计出这样的机制？这些问题时时刻刻会萦绕脑海，而想搞清楚这些，就不得不提到`Android`自身的 **输入系统** （`Input System`）。

`Android`自身的 **输入系统** 又是什么？一言以蔽之，任何与`Android`设备的交互——我们称之为 **输入事件**，都需要通过 **输入系统** 进行管理和分发；这其中最靠近上层，并且最典型的一个小环节就是`View`的 **事件分发** 流程。

这样看来，**输入系统** 本身确实是一个非常庞大复杂的命题，并且，越靠近底层细节，越容易有一种 **只见树木不见树林** 之感，反复几次，直至迷失在细节代码的较真中，一次学习的努力尝试付诸东流。

因此，**控制住原理分析的粒度，在宏观的角度，系统地了解输入系统本身的设计理念，并引申到实际开发中的`ANR`现象的原理和解决思路** ，是一个非常不错的理论与实践相结合的学习方式，这也正是笔者写作本文的初衷。

本文篇幅较长，思维导图如下：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/blogs/2020/input_system.png)

## 一、自顶向下探索

谈到`Android`系统本身，首先，必须将 **应用进程** 和 **系统进程** 有一个清晰的认知，前者一般代表开发者依托`Android`平台本身创造开发的应用；后者则代表 `Android`系统自身创建的核心进程。

这里我们抛开 **应用进程** ，先将视线转向 **系统进程**，因为 **输入系统** 本身是由后者初始化和管理调度的。

`Android`系统在启动的时候,会初始化`zygote`进程和由`zygote`进程`fork`出来的`SystemServer`进程；作为 **系统进程** 之一，`SystemServer`进程会提供一系列的系统服务，而接下来要讲到的`InputManagerService`也正是由 `SystemServer` 提供的。

在`SystemServer`的初始化过程中，`InputManagerService`(下称`IMS`)和`WindowManagerService`(下称`WMS`)被创建出来；其中`WMS`本身的创建依赖`IMS`对象的注入：

```Java
// SystemServer.java
private void startOtherServices() {
 // ...
 InputManagerService inputManager = new InputManagerService(context);
 // inputManager作为WindowManagerService的构造参数
 WindowManagerService wm = WindowManagerService.main(context,inputManager, ...);
}
```

在 **输入系统** 中，`WMS`非常重要，其负责管理`IMS`、`Window`与`ActivityManager`之间的通信，这里点到为止，后文再进行补充，我们先来看`IMS`。

顾名思义，`IMS`服务的作用就是负责输入模块在`Java`层级的初始化，并通过`JNI`调用，在`Native`层进行更下层输入子系统相关功能的创建和预处理。

在`JNI`的调用过程中，`IMS`创建了`NativeInputManager`实例，`NativeInputManager`则在初始化流程中又创建了`EventHub`和`InputManager`:

```Cpp
NativeInputManager::NativeInputManager(jobject contextObj, jobject serviceObj, const sp<Looper>& looper) : mLooper(looper), mInteractive(true) {
    // ...
    // 创建一个EventHub对象
    sp<EventHub> eventHub = new EventHub();
    // 创建一个InputManager对象
    mInputManager = new InputManager(eventHub, this, this);
}
```

此时我们已经处于`Native`层级。读者需要注意，对于整个`Native`层级而言，其向下负责与`Linux`的设备节点中获取输入，向上则与靠近用户的`Java`层级相通信，可以说是非常重要。而在该层级中，`EventHub`和`InputManager`又是最核心的两个角色。

这两个角色的职责又是什么呢？首先来说`EventHub`，它是底层 **输入子系统** 中的核心类，负责从物理输入设备中不断读取事件（`Event`)，然后交给`InputManager`,后者内部封装了`InputReader`和`InputDispatcher`，用来从`EventHub`中读取事件和分发事件：

```Cpp
InputManager::InputManager(...) {
    mDispatcher = new InputDispatcher(dispatcherPolicy);
    mReader = new InputReader(eventHub, readerPolicy, mDispatcher);
    initialize();
}
```

简单来看，`EventHub`建立了`Linux`与输入设备之间的通信，`InputManager`中的`InputReader`和`InputDispatcher`负责了输入事件的读取和分发，在 **输入系统** 中，两者的确非常重要。

这里借用网上的图对此进行一个简单的概括：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/qingmei2-blogs-art/blogs/2020/QQ20200816-135006%402x.w0j0n7b4e5.png)

## 二、EventHub 与 epoll 机制

对于`EventHub`的具体实现，绝大多数`App`开发者也许并不需要去花太多时间深入——简单了解其职责，然后一笔带过似乎是笔划算的买卖。

但是在`EventHub`的实现细节中笔者发现，其对`epoll`机制的利用是一个非常经典的学习案例，因此，花时间稍微深入了解也绝对是一举两得。

上文说到，`EventHub`建立了`Linux`与输入设备之间的通信，其实这种描述是不准确的，那么，`EventHub`是为了解决什么问题而设计的呢，其具体又是如何实现的？

### 1、多输入设备与输入子系统

我们知道，`Android`设备可以同时连接多个输入设备，比如 **屏幕** 、 **键盘** 、 **鼠标** 等等，用户在任意设备上的输入都会产生一个中断，经由`Linux`内核的中断处理及设备驱动转换成一个`Event`，最终交给用户空间的应用程序进行处理。

`Linux`内核提供了一个便于将不同设备不同数据接口统一转换的抽象层，只要底层输入设备驱动程序按照这层抽象接口实现，应用就可以通过统一接口访问所有输入设备，这便是`Linux`内核的 **输入子系统**。

那么 **输入子系统** 如何是针对接收到的`Event`进行的处理呢？这就不得不提到`EventHub`了，它是底层`Event`处理的枢纽，其利用了`epoll`机制，不断接收到输入事件`Event`，然后将其向上层的`InputReader`传递。

### 2、什么是epoll机制

这是常见于面试`Handler`相关知识点时的一道进阶题，变种问法是：「既然`Handler`中的`Looper`中通过一个死循环不断轮询，为什么程序没有因为无限死循环导致崩溃或者`ANR`?」

读者应该知道，`Handler`简单的利用了`epoll`机制，做到了消息队列的阻塞和唤醒。关于`epoll`机制，这里有一篇非常经典的解释，不了解其设计理念的读者 **有必要** 了解一下：

> [知乎：epoll或者kqueue的原理是什么?](https://www.zhihu.com/question/20122137/answer/14049112)

参考上文，这里我们对`epoll`机制进行一个简单的总结:

> `epoll`可以理解为`event poll`，不同于忙轮询和无差别轮询，在 **多个输入流** 的情况下，`epoll`只会把哪个流发生了怎样的I/O事件通知我们。此时我们对这些流的操作都是有意义的。

`EventHub`中使用`epoll`的恰到好处——多个物理输入设备对应了多个不同的输入流，通过`epoll`机制，在`EventHub`初始化时，分别创建`mEpollFd`和`mINotifyFd`；前者用于监听设备节点是否有设备文件的增删，后者用于监听是否有可读事件，创建管道，让`InputReader`来读取事件：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/qingmei2-blogs-art/blogs/2020/image2.wn7m6kwmz8.png)

## 三、事件的读取和分发

本章节将对`InputReader`和`InputDispatcher`进行系统性的介绍。

### 1、InputReader：读取事件

`InputReader`是什么？简单理解`InputReader`的作用，通过从`EventHub`获取事件后，将事件进行对应的处理，然后将事件进行封装并添加到`InputDispatcher`的队列中，最后唤醒`InputDispatcher`进行下一步的事件分发。

乍得一看，在 **输入系统** 的`Native`层中，`InputReader`似乎平凡无奇，但越是看似朴实无华的事物，在整个流程中往往占据绝对重要的作用。

首先，`EventHub`传过来的`Event`除了普通的 **输入事件** 外，还包含了设备本身的**增、删、扫描** 等事件，这些额外的事件处理并没有直接交给`InputDispatcher`去分发，而是在`InputReader`中进行了处理。

当某个时间发生——可能是用户 **按键输入**，或者某个 **设备插入**，亦或 **设备属性被调整** ，`epoll_wait()`返回并将`Event`存入。

这之后，`InputReader`对输入事件进行了一次读取，因为不同设备对事件的处理逻辑又各自不同，因此`InputReader`内部持有一系列的`Mapper`对事件进行 **匹配** ，如果不匹配则忽略事件，反之则将`Event`封装成一个新的`NotifyArgs`数据对象，准备存入队列中，即唤醒`InputDispatcher`进行分发。

巧妙的是，在唤醒`InputDispatcher`进行分发之前，`InputReader`在自己的线程中先执行了一个很特殊的 **拦截操作** 环节。

### 2、输入事件的拦截和转换

读者知道，在应用开发中，一些特殊的输入事件是无法通过普通的方式进行拦截的；比如音量键，`Power`键，电话键，以及一些特殊的组合键，这里我们通称为 **系统按键**。

这点无可厚非，虽然`Android`系统对于开发者足够的开放，但是一切都是有限制的，绝大多数的 **用户按键** 通常可以被应用拦截处理，但是 **系统按键** 绝对不行——这种限制往往能够给予用户设备安全最后的保障。

因此，在`InputReader`唤醒`InputDispatcher`进行事件分发之前，`InputReader`在自己的线程中进行了两轮拦截处理。

首先的第一轮拦截操作就是对 **系统按键** 级别的 **输入事件** 进行处理，对于手机而言，这个工作是在`PhoneWindowManager`中完成；举例来说，当用户按了`Power`（电源）键，`Android`设备本身会切唤醒或睡眠——即亮屏和息屏。

这也正是「在技术论坛中，通常对 **系统按键** 拦截处理的技术方案，基本都是需要修改`PhoneWindowManager`的源码」的原因。

接下来输入事件进入到第二轮的处理中，如果用户在`Setting->Accessibility`中选择打开某些功能，以 **手势识别** 为例，`Android`的`AccessbilityManagerService`（辅助功能服务) 可能会根据需要转换成新的`Event`，比如说两根手指头捏动的手势最终会变成`ZoomEvent`。

需要注意的是，这里的拦截处理并不会真正将事件 **消费** 掉，而是通过特殊的方式将事件进行标记（`policyFlags`），然后在`InputDispatcher`中处理。

至此，`InputReader`对 **输入事件** 完整的一轮处理到此结束，这之后，`InputReader`又进入了新一轮等待。

### 3、InputDispatcher：分发事件

当`wake()`函数将在`Looper`中睡眠等待的`InputDispatcher`唤醒时，`InputDispatcher`开始新一轮事件的分发。

> 准确来说，`InputDispatcher`被唤醒时，`wake()`函数实际是在`InputManagerService`的线程中执行的，即整个流程的线程切换顺序为`InputReaderThread` -> `InputManagerServiceThread` -> `InputDispatcherThread`。

`InputDispatcher`的线程负责将接收到的 **输入事件** 分发给 **目标应用窗口**，在这个过程中，`InputDispatcher`首先需要对上个环节中标记了需要拦截的 **系统按键** 相关事件进行拦截，被拦截的事件至此不再向下分发。

这之后，`InputDispatcher`进入了本文最关键的一个环节——调用 `findFocusedWindowTargetLocked()`获取当前的 **焦点窗口** ，同时检测目标应用是否有`ANR`发生。

如果检测到目标窗口处于正常状态，即`ANR`并未发生时，`InputDispatcher`进入真正的分发程序，将事件对象进行新一轮的封装，通过`SocketPair`唤醒目标窗口所在进程的`Looper`线程，即我们应用进程中的主线程，后者会读取相应的键值并进行处理。

表面来看，整个分发流程似乎干净简洁且便于理解，但实际上`InputDispatcher`整个流程的逻辑十分复杂，试想一次事件分发要横跨3个线程的流程又怎会简单？

此外，`InputDispatcher`还负责了 **ANR** 的处理，这又导致整个流程的复杂度又上升了一个层级，这个流程我们在后文的`ANR`章节中进行更细致的分析，因此先按住不提。

接下来，我们来看看整个 **输入事件** 的分发流程中， **应用进程** 是如何与 **系统进程** 建立相应的通信链接的。

### 4、通过Socket建立通信

> 关于 **跨进程通信的建立** 这一节，笔者最初打算作为一个大的章节来讲，但是对于整个 **输入系统** 而言，其似乎又只是一个 **重要非必需** 的知识点。最终，笔者将其放在一个小节中进行简单的描述，有兴趣的读者可以在文末的参考链接中查阅更详尽的资料。

我们知道，`InputReader`和`InputDispatcher`运行在`system_server` **系统进程** 中，而用户操作的应用都运行在自己的 **应用进程** 中；这里就涉及到跨进程通信，那么 **应用进程** 是如何与 **系统进程** 建立通信的呢？

让我们回到文章最初`WindowManagerService(WMS)`和`InputManagerService(IMS)`初始化的流程中来，当`IMS`以及其他的系统服务初始化完成之后，应用程序开始启动。

如果一个应用程序有`Activity`（只有`Activity`能够接受用户输入），那么它要将自己的`Window`注册到`WMS`中。

在这里，`Android`使用了`Socket`而不是`Binder`来完成。`WMS`中通过`OpenInputChannelPair`生成了两个`Socket`的`FD`， 代表一个双向通道的两端：向一端写入数据，另外一端便可以读出；反之，如果一端没有写入数据，另外一端去读，则陷入阻塞等待。

最终`InputDispatcher`中建立了目标应用的`Connection`对象，代表与远端应用的窗口建立了链接；同样，应用进程中的`ViewRootImpl`创建了`WindowInputEventReceiver`用于接受`InputDispatchor`传过来的事件：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/qingmei2-blogs-art/blogs/2020/image.k2k65nfp1fp.png)

这里我们对该次 **跨进程通信建立流程** 有了初步的认知，对于`Android`系统而言，`Binder`是最广泛的跨进程通信的应用方式，但是`Android`系中跨进程通信就仅仅只用到了`Binder`吗？答案是否定的，至少在 **输入系统** 中，除了`Binder`之外，`Socket`同样起到了举足轻重的作用。

那么新的问题就来了，这里为什么选择`Socket`而不是选择`Binder`呢，关于这个问题的解释，笔者找到了一个很好的版本：

> `Socket`可以实现异步的通知，且只需要两个线程参与（`Pipe`两端各一个），假设系统有`N`个应用程序，跟输入处理相关的线程数目是 `N+1` (`1`是`Input Dispatcher`线程）。然而，如果用`Binder`实现的话，为了实现异步接收，每个应用程序需要两个线程，一个`Binder`线程，一个后台处理线程（不能在`Binder`线程里处理输入，因为这样太耗时，将会堵塞住发送端的调用线程）。在发送端，同样需要两个线程，一个发送线程，一个接收线程来接收应用的完成通知，所以，`N`个应用程序需要 `2（N+1)`个线程。相比之下，`Socket`还是高效多了。

现在，**应用进程** 能够收到由`InputDispatcher`处理完成并分发过来的 **输入事件** 了。至此，我们来到了最熟悉的应用层级事件分发流程。对于这之后 **应用层级的事件分发**，可以阅读下述笔者的另外两篇文章，本文不赘述。

> * [Android 事件分发机制的设计与实现](https://juejin.im/post/6844903926446161927)
> * [Android 事件拦截机制的设计与实现](https://juejin.im/post/6844904128397705230)
