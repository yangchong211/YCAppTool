# 反思|Android 事件分发机制的设计与实现

> **反思** 系列博客是我的一种新学习方式的尝试，该系列起源和目录请参考 [这里](https://github.com/qingmei2/android-programming-profile/blob/master/src/%E5%8F%8D%E6%80%9D%E7%B3%BB%E5%88%97/%E5%8F%8D%E6%80%9D%7C%E7%B3%BB%E5%88%97%E7%9B%AE%E5%BD%95.md) 。

## 概述

`Android`体系本身非常宏大，源码中值得思考和借鉴之处众多。以整体事件分发机制为例，其整个流程涉及到了 **系统启动流程**（`SystemServer`）、**输入管理**(`InputManager`)、**系统服务和UI的通信**（`ViewRootImpl` + `Window` + `WindowManagerService`）、**事件分发** 等等一系列的环节。

对于 **事件分发** 环节而言，不可否认非常重要，但`Android`系统 **整体事件分发机制** 也是一名优秀`Android`工作者需要去了解的，本文笔者将针对`Android` **事件分发整体机制和设计思路** 进行描述，其整体结构如下图：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/android/core/event_dispatcher/image.0018xsd5yv1ynl.png)

## 整体思路

### 1.架构设计

`Android`系统中将输入事件定义为`InputEvent`，而`InputEvent`根据输入事件的类型又分为了`KeyEvent`和`MotionEvent`，前者对应键盘事件，后者则对应屏幕触摸事件，这些事件统一由系统输入管理器`InputManager`进行分发。

在系统启动的时候，`SystemServer`会启动窗口管理服务`WindowManagerService`，`WindowManagerService`在启动的时候就会通过启动系统输入管理器`InputManager`来负责监控键盘消息。

`InputManager`负责从硬件接收输入事件，并将事件分发给当前激活的窗口（`Window`）处理，这里我们将前者理解为 **系统服务**，将后者理解为应用层级的 **UI**, 因此需要有一个中介负责 **服务** 和 **UI** 之间的通信，于是`ViewRootImpl`类应运而生。

### 2.建立通信

`ActivityThread`负责控制`Activity`的启动过程，在`performLaunchActivity()`流程中，`ActivityThread`会针对`Activity`创建对应的`PhoneWindow`和`DecorView`实例，而之后的`handleResumeActivity()`流程中则会将`PhoneWindow`（ **应用** ）和`InputManagerService`( **系统服务** )通信以建立对应的连接，保证UI可见并能够对输入事件进行正确的分发，这之后`Activity`就会成为可见的。

如何在应用程序和系统服务之间建立通信？`Android`中`Window`和`InputManagerService`之间的通信实际上使用的`InputChannel`,`InputChannel`是一个`pipe`，底层实际是通过`socket`进行通信：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/android/core/event_dispatcher/image.256e9cnvm76.png)

在`ActivityThread`的`handleResumeActivity()`流程中, 会通过`WindowManagerImpl.addView()`为当前的`Window`创建一个`ViewRootImpl`实例，当`InputManager`监控到硬件层级的输入事件时，会通知`ViewRootImpl`对输入事件进行底层的事件分发。

### 3.事件分发

与`View`的 [布局流程](https://github.com/qingmei2/android-programming-profile/blob/master/src/反思系列/View/反思%7CAndroid%20View机制设计与实现：布局流程.md) 和 [测量流程](https://github.com/qingmei2/android-programming-profile/blob/master/src/%E5%8F%8D%E6%80%9D%E7%B3%BB%E5%88%97/View/%E5%8F%8D%E6%80%9D%7CAndroid%20View%E6%9C%BA%E5%88%B6%E8%AE%BE%E8%AE%A1%E4%B8%8E%E5%AE%9E%E7%8E%B0%EF%BC%9A%E6%B5%8B%E9%87%8F%E6%B5%81%E7%A8%8B.md) 相同，`Android`事件分发机制也使用了 **递归** 的思想，因为一个事件最多只有一个消费者，所以通过责任链的方式将事件自顶向下进行传递，找到事件的消费者（这里是指一个`View`）之后，再自底向上返回结果。

读到这里，读者应该觉得非常熟悉了，但实际上这里描述的事件分发流程为UI层级的事件分发——它只是事件分发流程整体的一部分。读者需要理解，`ViewRootImpl`从`InputManager`获取到新的输入事件时，会针对输入事件通过一个复杂的 **责任链** 进行底层的递归，将不同类型的输入事件（比如 **屏幕触摸事件** 和 **键盘输入事件** ）进行不同策略的分发，而只有部分符合条件的 **屏幕触摸事件** 最终才有可能进入到UI层级的事件分发：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/android/core/event_dispatcher/image.exuh532xv1f.png)

> 如图所示，蓝色箭头描述的流程才是UI层级的事件分发。

为了方便理解，本文使用了以下两个词汇：**应用整体的事件分发** 和 **UI层级的事件分发** ——需要重申的是，这两个词汇虽然被分开讲解，但其本质仍然属于一个完整 **事件分发的责任链**，后者只是前者的一小部分而已。

## 架构设计

### 1.InputEvent：输入事件分类概述

`Android`系统中将输入事件定义为`InputEvent`，而`InputEvent`根据输入事件的类型又分为了`KeyEvent`和`MotionEvent`：

```java
// 输入事件的基类
public abstract class InputEvent implements Parcelable { }

public class KeyEvent extends InputEvent implements Parcelable { }

public final class MotionEvent extends InputEvent implements Parcelable { }
```

`KeyEvent`对应了键盘的输入事件，那么什么是`MotionEvent`？顾名思义，`MotionEvent`就是移动事件，鼠标、笔、手指、轨迹球等相关输入设备的事件都属于`MotionEvent`，本文我们简单地将其视为 **屏幕触摸事件**。

用户的输入种类繁多，由此可见，`Android`输入系统的设计中，将 **输入事件** 抽象为`InputEvent`是有必要的。

### 2.InputManager：系统输入管理器

`Android`系统的设计中，`InputEvent`统一由系统输入管理器`InputManager`进行分发。在这里`InputManager`是`native`层级的一个类，负责与硬件通信并接收输入事件。

那么`InputManager`是如何初始化的呢？这里就要涉及到`Java`层级的`SystemServer`了，我们知道`SystemServer`进程中包含着各种各样的系统服务，比如`ActivityManagerService`、`WindowManagerService`等等，`SystemServer`由`zygote`进程启动, 启动过程中对`WindowManagerService`和`InputManagerService`进行了初始化:

```java
public final class SystemServer {

  private void startOtherServices() {
     // 初始化 InputManagerService
     InputManagerService inputManager = new InputManagerService(context);
     // WindowManagerService 持有了 InputManagerService
     WindowManagerService wm = WindowManagerService.main(context, inputManager,...);

     inputManager.setWindowManagerCallbacks(wm.getInputMonitor());
     inputManager.start();
  }
}
```

`InputManagerService`的构造器中，通过调用native函数，通知`native`层级初始化`InputManager`:

```java
public class InputManagerService extends IInputManager.Stub {

  public InputManagerService(Context context) {
    // ...通知native层初始化 InputManager
    mPtr = nativeInit(this, mContext, mHandler.getLooper().getQueue());
  }

  // native 函数
  private static native long nativeInit(InputManagerService service, Context context, MessageQueue messageQueue);
}
```

`SystemServer`会启动窗口管理服务`WindowManagerService`，`WindowManagerService`在启动的时候就会通过`InputManagerService`启动系统输入管理器`InputManager`来负责监控键盘消息。

对于本文而言，`framework`层级相关如`WindowManagerService`（窗口管理服务）、`native`层级的源码、`SystemServer` 亦或者 `Binder`跨进程通信并非重点，读者仅需了解 **系统服务的启动流程** 和 **层级关系** 即可，参考下图：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/android/core/event_dispatcher/image.2m7iml9m5qg.png)

### 3.ViewRootImpl：窗口服务与窗口的纽带

`InputManager`将事件分发给当前激活的窗口（`Window`）处理，这里我们将前者理解为系统层级的 **（窗口）服务**，将后者理解为应用层级的 **窗口**, 因此需要有一个中介负责 **服务** 和 **窗口** 之间的通信，于是`ViewRootImpl`类应运而生。

`ViewRootImpl`作为链接`WindowManager`和`DecorView`的纽带，同时实现了`ViewParent`接口，`ViewRootImpl`作为整个控件树的根部，它是`View`树正常运作的动力所在，控件的测量、布局、绘制以及输入事件的分发都由`ViewRootImpl`控制。

那么`ViewRootImpl`是如何被创建和初始化的，而 **（窗口）服务** 和 **窗口** 之间的通信又是如何建立的呢？

## 建立通信

### 1.ViewRootImpl的创建

既然`Android`系统将 **（窗口）服务** 与 **窗口** 的通信建立交给了`ViewRootImpl`，那么`ViewRootImpl`必然持有了两者的依赖，因此了解`ViewRootImpl`是如何创建的就非常重要。

我们知道，`ActivityThread`负责控制`Activity`的启动过程，在`ActivityThread.performLaunchActivity()`流程中，`ActivityThread`会针对`Activity`创建对应的`PhoneWindow`和`DecorView`实例，而在`ActivityThread.handleResumeActivity()`流程中，`ActivityThread`会将获取当前`Activity`的`WindowManager`，并将`DecorView`和`WindowManager.LayoutParams`(布局参数)作为参数调用`addView()`函数：

```java
// 伪代码
public final class ActivityThread {

  @Override
  public void handleResumeActivity(...){
    //...
    windowManager.addView(decorView, windowManagerLayoutParams);
  }
}
```

`WindowManager.addView()`实际上就是对`ViewRootImpl`进行了初始化，并执行了`setView()`函数：

```java
// 1.WindowManager 的本质实际上是 WindowManagerImpl
public final class WindowManagerImpl implements WindowManager {

   @Override
   public void addView(@NonNull View view, @NonNull ViewGroup.LayoutParams params) {
       // 2.实际上调用了 WindowManagerGlobal.addView()
       WindowManagerGlobal.getInstance().addView(...);
   }
}

public final class WindowManagerGlobal {

   public void addView(...) {
      // 3.初始化 ViewRootImpl，并执行setView()函数
      ViewRootImpl root = new ViewRootImpl(view.getContext(), display);
      root.setView(view, wparams, panelParentView);
   }
}

public final class ViewRootImpl {

  public void setView(View view, WindowManager.LayoutParams attrs, View panelParentView) {
      // 4.该函数就是控测量（measure）、布局(layout)、绘制（draw）的开始
      requestLayout();
      // ...
      // 5.此外还有通过Binder建立通信，这个下文再提
  }
}
```

`Android`系统的`Window`机制并非本文重点，读者可简单理解为`ActivityThread.handleResumeActivity()`流程中最终创建了`ViewRootImpl`，并通过`setView()`函数对`DecorView`开始了绘制流程的三个步骤。

### 2.通信的建立

完成了`ViewRootImpl`的创建之后，如何完成系统输入服务和应用程序进程的连接呢？

`Android`中`Window`和`InputManagerService`之间的通信实际上使用的`InputChannel`,`InputChannel`是一个`pipe`，底层实际是通过`socket`进行通信。在`ViewRootImpl.setView()`过程中，也会同时注册`InputChannel`：

```java
public final class InputChannel implements Parcelable { }
```

上文中，我们提到了`ViewRootImpl.setView()`函数，在该函数的执行过程中，会在`ViewRootImpl`中创建`InputChannel`，`InputChannel`实现了`Parcelable`， 所以它可以通过`Binder`传输。具体是通过`addDisplay()`将当前`window`加入到`WindowManagerService`中管理:

```java
public final class ViewRootImpl {

  public void setView(View view, WindowManager.LayoutParams attrs, View panelParentView) {
      requestLayout();
      // ...
      // 创建InputChannel
      mInputChannel = new InputChannel();
      // 通过Binder在SystemServer进程中完成InputChannel的注册
      mWindowSession.addToDisplay(mWindow, mSeq, mWindowAttributes,
                            getHostVisibility(), mDisplay.getDisplayId(),
                            mAttachInfo.mContentInsets, mAttachInfo.mStableInsets,
                            mAttachInfo.mOutsets, mInputChannel);
  }
}
```

这里涉及到了`WindowManagerService`和`Binder`跨进程通信，读者不需要纠结于详细的细节，只需了解最终在`SystemServer`进程中，`WindowManagerService`根据当前的`Window`创建了`SocketPair`用于跨进程通信，同时并对`App`进程中传过来的`InputChannel`进行了注册，这之后，`ViewRootImpl`里的`InputChannel`就指向了正确的`InputChannel`, 作为`Client`端，其`fd`与`SystemServer`进程中`Server`端的`fd`组成`SocketPair`, 它们就可以双向通信了。

> 对该流程感兴趣的读者可以参考 [这篇文章](https://www.jianshu.com/p/2bff4ecd86c9)。

## 应用整体的事件分发

`App`端与服务端建立了双向通信之后，`InputManager`就能够将产生的输入事件从底层硬件分发过来，`Android`提供了`InputEventReceiver`类，以接收分发这些消息：

```java
public abstract class InputEventReceiver {
    // Called from native code.
    private void dispatchInputEvent(int seq, InputEvent event, int displayId) {
        // ...
    }
}
```

`InputEventReceiver`是一个抽象类，其默认的实现是将接收到的输入事件直接消费掉，因此真正的实现是`ViewRootImpl.WindowInputEventReceiver`类：

```java
public final class ViewRootImpl {

  final class WindowInputEventReceiver extends InputEventReceiver {
    @Override
     public void onInputEvent(InputEvent event, int displayId) {
         // 将输入事件加入队列
         enqueueInputEvent(event, this, 0, true);
     }
  }
}
```

输入事件加入队列之后，接下来就是对事件的分发了，设计者在这里使用了经典的 **责任链** 模式：对于一个输入事件的分发而言，必然有其对应的消费者，在这个过程中为了使多个对象都有处理请求的机会，从而避免了请求的发送者和接收者之间的耦合关系。将这些对象串成一条链，并沿着这条链一直传递该请求，直到有对象处理它为止。

### InputStage

因此，设计者针对事件分发的整个责任链设计了`InputStage`类作为基类，作为责任链中的模版，并实现了若干个子类，为输入事件按顺序分阶段进行分发处理：

```java
// 事件分发不同阶段的基类
abstract class InputStage {
  private final InputStage mNext;  // 指向事件分发的下一阶段
}

// InputStage的子类，象征事件分发的各个阶段

final class ViewPreImeInputStage extends InputStage {}

final class EarlyPostImeInputStage extends InputStage {}

final class ViewPostImeInputStage extends InputStage {}

final class SyntheticInputStage extends InputStage {}

abstract class AsyncInputStage extends InputStage {}

final class NativePreImeInputStage extends AsyncInputStage {}

final class ImeInputStage extends AsyncInputStage {}

final class NativePostImeInputStage extends AsyncInputStage {}
```

输入事件整体的分发阶段十分复杂，比如当事件分发至`SyntheticInputStage`阶段，该阶段为 **综合性处理阶段** ，主要针对轨迹球、操作杆、导航面板及未捕获的事件使用键盘进行处理:

```java
final class SyntheticInputStage extends InputStage {
    @Override
    protected int onProcess(QueuedInputEvent q) {
        // 轨迹球
        if (...) {
            mTrackball.process(event);
            return FINISH_HANDLED;
        } else if (...) {
            // 操作杆
            mJoystick.process(event);
            return FINISH_HANDLED;
        } else if (...) {
            // 导航面板
            mTouchNavigation.process(event);
            return FINISH_HANDLED;
        }
        // 继续转发事件
        return FORWARD;
    }
}
```

比如当事件分发至`ImeInputStage`阶段，即 **输入法事件处理阶段** ，会从事件中过滤出用户输入的字符，如果输入的内容无法被识别，则将输入事件向下一个阶段继续分发：

```java
final class ImeInputStage extends AsyncInputStage {

  @Override
  protected int onProcess(QueuedInputEvent q) {
      if (mLastWasImTarget && !isInLocalFocusMode()) {
          // 获取输入法Manager
          InputMethodManager imm = InputMethodManager.peekInstance();
          final InputEvent event = q.mEvent;
          // imm对事件进行分发
          int result = imm.dispatchInputEvent(event, q, this, mHandler);
          if (result == ....) {
              // imm消费了该输入事件
              return FINISH_HANDLED;
          } else {
              return FORWARD;   // 向下转发
          }
      }
      return FORWARD;           // 向下转发
  }
}
```

当然还有最熟悉的`ViewPostImeInputStage`，即 **视图输入处理阶段** ，主要处理按键、轨迹球、手指触摸及一般性的运动事件，触摸事件的分发对象是View，这也正是我们熟悉的 **UI层级的事件分发** 流程的起点:

```java
final class ViewPostImeInputStage extends InputStage {

  private int processPointerEvent(QueuedInputEvent q) {
    // 让顶层的View开始事件分发
    final MotionEvent event = (MotionEvent)q.mEvent;
    boolean handled = mView.dispatchPointerEvent(event);
    //...
  }
}
```

读到这里读者应该理解了， **UI层级的事件分发只是完整事件分发流程的一部分**，当输入事件（即使是`MotionEvent`）并没有分发到`ViewPostImeInputStage`（比如在 **综合性处理阶段** 就被消费了），那么`View`层的事件分发自然无从谈起，这里再将整体的流程图进行展示以方便理解：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/android/core/event_dispatcher/image.exuh532xv1f.png)

### 组装责任链

现在我们理解了，新分发的事件会通过一个`InputStage`的责任链进行整体的事件分发，这意味着，当新的事件到来时，责任链已经组装好了，那么这个责任链是何时进行组装的？

不难得出，对于责任链的组装，最好是在系统服务和`Window`建立通信成功的时候，而上文中也提到了，通信的建立是执行在`ViewRootImpl.setView()`方法中的，因此在`InputChannel`注册成功之后，即可对责任链进行组装：

```java
public final class ViewRootImpl implements ViewParent {

  public void setView(View view, WindowManager.LayoutParams attrs, View panelParentView) {
     // ...
     // 1.开始根布局的绘制流程
     requestLayout();
     // 2.通过Binder建立双端的通信
     res = mWindowSession.addToDisplay(...)
     mInputEventReceiver = new WindowInputEventReceiver(mInputChannel, Looper.myLooper());
     // 3.对责任链进行组装
     mSyntheticInputStage = new SyntheticInputStage();
     InputStage viewPostImeStage = new ViewPostImeInputStage(mSyntheticInputStage);
     InputStage nativePostImeStage = new NativePostImeInputStage(viewPostImeStage,
            "aq:native-post-ime:" + counterSuffix);
     InputStage earlyPostImeStage = new EarlyPostImeInputStage(nativePostImeStage);
     InputStage imeStage = new ImeInputStage(earlyPostImeStage,
            "aq:ime:" + counterSuffix);
     InputStage viewPreImeStage = new ViewPreImeInputStage(imeStage);
     InputStage nativePreImeStage = new NativePreImeInputStage(viewPreImeStage,
            "aq:native-pre-ime:" + counterSuffix);
     mFirstInputStage = nativePreImeStage;
     mFirstPostImeInputStage = earlyPostImeStage;
     // ...
  }
}
```

这说明`ViewRootImpl.setView()`函数非常重要，该函数也正是`ViewRootImpl`本身职责的体现：

* 1.链接`WindowManager`和`DecorView`的纽带，更广一点可以说是`Window`和`View`之间的纽带；
* 2.完成`View`的绘制过程，包括`measure、layout、draw`过程；
* 3.向DecorView分发收到的用户发起的`InputEvent`事件。

最终整体事件分发流程由如下责任链构成：

> SyntheticInputStage --> ViewPostImeStage --> NativePostImeStage --> EarlyPostImeStage --> ImeInputStage --> ViewPreImeInputStage --> NativePreImeInputStage

### 事件分发结果的返回

上文说到，真正从`Native`层的`InputManager`接收输入事件的是`ViewRootImpl`的`WindowInputEventReceiver`对象，既然负责输入事件的分发，自然也负责将事件分发的结果反馈给`Native`层，作为事件分发的结束：

```java
public final class ViewRootImpl {

  final class WindowInputEventReceiver extends InputEventReceiver {
    @Override
     public void onInputEvent(InputEvent event, int displayId) {
         // 【开始】将输入事件加入队列,开始事件分发
         enqueueInputEvent(event, this, 0, true);
     }
  }
}

// ViewRootImpl.WindowInputEventReceiver 是其子类，因此也持有finishInputEvent函数
public abstract class InputEventReceiver {
  private static native void nativeFinishInputEvent(long receiverPtr, int seq, boolean handled);

  public final void finishInputEvent(InputEvent event, boolean handled) {
     //...
     // 【结束】调用native层函数，结束应用层的本次事件分发
     nativeFinishInputEvent(mReceiverPtr, seq, handled);
  }
}
```

### ViewPostImeInputStage：UI层事件分发的起点

上文已经提到，**UI层级的事件分发** 作为 **完整事件分发流程的一部分**，发生在`ViewPostImeInputStage.processPointerEvent`函数中：

```java
final class ViewPostImeInputStage extends InputStage {

  private int processPointerEvent(QueuedInputEvent q) {
    // 让顶层的View开始事件分发
    final MotionEvent event = (MotionEvent)q.mEvent;
    boolean handled = mView.dispatchPointerEvent(event);
    //...
  }
}
```

这个顶层的`View`其实就是`DecorView`（参见上文 *建立通信-ViewRootImpl的创建* 小节），读者知道，`DecorView`实际上就是`Activity`中`Window`的根布局，它是一个`FrameLayout`。

现在`DecorView`执行了`dispatchPointerEvent(event)`函数，这是不是就意味着开始了`View`的事件分发？

### DecorView的双重职责

`DecorView`作为`View`树的根节点，接收到屏幕触摸事件`MotionEvent`时，应该通过递归的方式将事件分发给子`View`，这似乎理所当然。但实际设计中，设计者将`DecorView`接收到的事件首先分发给了`Activity`，`Activity`又将事件分发给了其`Window`，最终`Window`才将事件又交回给了`DecorView`，形成了一个小的循环：

```java
// 伪代码
public class DecorView extends FrameLayout {

  // 1.将事件分发给Activity
  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
      return window.getActivity().dispatchTouchEvent(ev)
  }

  // 4.执行ViewGroup 的 dispatchTouchEvent
  public boolean superDispatchTouchEvent(MotionEvent event) {
      return super.dispatchTouchEvent(event);
  }
}

// 2.将事件分发给Window
public class Activity {
  public boolean dispatchTouchEvent(MotionEvent ev) {
      return getWindow().superDispatchTouchEvent(ev);
  }
}

// 3.将事件再次分发给DecorView
public class PhoneWindow extends Window {
  @Override
  public boolean superDispatchTouchEvent(MotionEvent event) {
      return mDecor.superDispatchTouchEvent(event);
  }
}
```

事件绕了一个圈子最终回到了`DecorView`这里，对于初次阅读这段源码的读者来说，这里的设计平淡无奇，似乎说它莫名其妙也不过分。事实上这里是 **面向对象程序设计** 中灵活运用 **多态** 这一特征的有力体现——对于`DecorView`而言，它承担了2个职责：

* 1.在接收到输入事件时，`DecorView`不同于其它`View`，它需要先将事件转发给最外层的`Activity`，使得开发者可以通过重写`Activity.onTouchEvent()`函数以达到对当前屏幕触摸事件拦截控制的目的，这里`DecorView`履行了自身（根节点）特殊的职责；
* 2.从`Window`接收到事件时，作为`View`树的根节点，将事件分发给子`View`，这里`DecorView`履行了一个普通的`View`的职责。

实际上，不只是`DecorView`，接下来`View`层级的事件分发中也运用到了这个技巧，对于`ViewGroup`的事件分发来说，其本质是递归思想的体现，在 **递流程** 中，其本身被视为上游的`ViewGroup`，需要自定义`dispatchTouchEvent()`函数，并调用`child.dispatchTouchEvent(event)`将事件分发给下游的子`View`；同时，在 **归流程** 中，其本身被视为一个`View`，需要调用`View`自身的方法已决定是否消费该事件（`super.dispatchTouchEvent(event)`），并将结果返回上游，直至回归到`View`树的根节点，至此整个UI树事件分发流程结束。

同时，读者应该也已理解，平时所说`View`层级的事件分发也只是 **UI层的事件分发** 的一个环节，而 **UI层的事件分发** 又只是 **应用层完整事件分发** 的一个小环节，更遑论后者本身又是`Native`层和应用层之间的事件分发机制的一部分了。

## UI层级事件分发

虽然`View`层级之间的事件分发只是 **UI层级事件分发** 的一个环节，但却是最重要的一个环节，也是本文的重点，上文所有内容都是为本节做系统性的铺垫 ——为了方便阅读，本小节接下来的内容中，**事件分发** 统一泛指 **View层级的事件分发**。

### 1.核心思想

了解 **事件分发** 的代码流程细节，首先需要了解整个流程的最终目的，那就是 **获知事件是否被消费** ，至于事件被哪个角色消费了，怎么被消费的，在外层责任链中的`ViewPostImeInputStage`不关心，其更上层`ViewRootImpl.WindowInputEventReceiver`不关心，`native`层级的`InputManager`自然更不会关心了。

因此，设计者设计出了这样一个函数：

```java
// 对事件进行分发
public boolean dispatchTouchEvent(MotionEvent event);
```

对于事件分发结果的接收者而言，其只关心事件是否被消费，因此返回值被定义为了`boolean`类型：当返回值为`true`，事件被消费，反之则事件未被消费。

上文中我们同样提到了，在`ViewGroup`的事件分发过程中，其本身的`dispatchTouchEvent(event)`和`super.dispatchTouchEvent(event)`完全是两个完全不同的函数，前者履行的是`ViewGroup`的职责，负责将事件分发给子`View`；后者履行的是`View`的职责，负责处理决定事件是否被消费（参见 *应用整体的事件分发-DecorView的双重职责* 小节）。

因此，对于事件分发整体流程，我们可以进行如下定义：

* 1、`ViewGroup`将事件分发给子`View`，当子`View`从`ViewGroup`中接收到事件，若其有`child`，则通过`dispatchTouchEvent(event)`再将事件分发给`child`...以此类推，直至将事件分发到底部的`View`，这也是事件分发的 **递流程**；
* 2、底部的`View`接收到事件时，通过`View`自身的`dispatchTouchEvent(event)`函数判断是否消费事件：
* 2.1 若消费事件，则将结果作为`true`向上层的`ViewGroup`返回，`ViewGroup`接收到`true`，意味着事件已经被消费，因此跳过了是否要消费该事件的判断，直接向上一级继续返回`true`，以此类推直到将`true`结果通知到最上层的`View`节点；
* 2.2 若不消费事件，则向上层返回`false`，`ViewGroup`接收到`false`，意味着事件未被消费，因此其本身执行`super.dispatchTouchEvent(event)`——即执行`View`本身的`dispatchTouchEvent(event)`函数，并将结果向上级返回，以此类推直到将`true`结果通知到最上层的`View`节点。

对于初次了解事件分发机制或者不熟悉递归思想的读者而言，上述文字似乎晦涩难懂，实际上用代码实现却惊人的简单：

```java
// 伪代码实现
// ViewGroup.dispatchTouchEvent
public boolean dispatchTouchEvent(MotionEvent event) {
  boolean consume = false;
  // 1.将事件分发给Child
  if (hasChild) {
    consume = child.dispatchTouchEvent();
  }
  // 2.若Child不消费该事件,或者没有child，判断自身是否消费该事件
  if (!consume) {
    consume = super.dispatchTouchEvent();
  }
  // 3.将结果向上层传递
  return consume;
}
```

上述代码中已经将 **事件分发** 最核心的流程表现的淋漓尽致，读者需认真理解和揣摩。`View`层级的事件传递的真正实现虽然复杂，但其本质却和上述代码并不不同，理解了这个基本的流程，接下来对于额外功能扩展的设计与实现也只是时间问题了。

### 2.事件序列与分发链

在上一小节中，读者已经了解事件分发的本质原理就是递归，而目前其实现方式是，每接收一个新的事件，都需要进行一次递归才能找到对应消费事件的`View`，并依次向上返回事件分发的结果。

每个事件都对`View`树进行一次遍历递归？这对性能的影响显而易见，因此这种设计是有改进空间的。

如何针对这个问题进行改进？首先，设计者根据用户的行为对`MotionEvent`中添加了一个`Action`的属性以描述该事件的行为：

* ACTION_DOWN：手指触摸到屏幕的行为
* ACTION_MOVE：手指在屏幕上移动的行为
* ACTION_UP：手指离开屏幕的行为
* ...其它Action，比如`ACTION_CANCEL`...

定义了这些行为的同时，设计者定义了一个叫做 **事件序列** 的概念：针对用户的一次触摸操作，必然对应了一个 **事件序列**，从用户手指接触屏幕，到移动手指，再到抬起手指 ——单个事件序列必然包含`ACTION_DOWN`、`ACTION_MOVE` ... `ACTION_MOVE`、`ACTION_UP` 等多个事件，这其中`ACTION_MOVE`的数量不确定，`ACTION_DOWN`和`ACTION_UP`的数量则为1。

定义了 **事件序列** 的概念，设计者就可以着手对现有代码进行设计和改进，其思路如下：当接收到一个`ACTION_DOWN`时，意味着一次完整事件序列的开始，通过递归遍历找到`View`树中真正对事件进行消费的`Child`，并将其进行保存，这之后接收到`ACTION_MOVE`和`ACTION_UP`行为时，则跳过遍历递归的过程，将事件直接分发给`Child`这个事件的消费者；当接收到`ACTION_DOWN`时，则重置整个事件序列：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/android/core/event_dispatcher/image.0fd92gu1msxn.png)

> 如图所示，其代表了一个`View`树，若序号为4的`View`是实际事件的消费者，那么当接收到`ACTION_DOWN`事件时，上层的`ViewGroup`则会通过递归找到它，接下来该事件序列中的其它事件到来时，也交给4号`View`去处理。

这个思路似乎没有问题，但是目前的设计中我们还缺少一把关键的钥匙，那就是如何在`ViewGroup`中保存实际消费事件的`View`？

为此设计者根据`View`的树形结构，设计了一个`TouchTarget`类，为作为一个成员属性，描述`ViewGroup`下一级事件分发的目标：

```java
public abstract class ViewGroup extends View {
    // 指向下一级事件分发的`View`
    private TouchTarget mFirstTouchTarget;

    private static final class TouchTarget {
        public View child;
        public TouchTarget next;
    }
}
```

这里应用到了树的 **深度优先搜索算法**（Depth-First-Search，简称DFS算法），正如代码所描述的，每个`ViewGroup`都持有一个`mFirstTouchTarget`, 当接收到一个`ACTION_DOWN`时，通过递归遍历找到`View`树中真正对事件进行消费的`Child`，并保存在`mFirstTouchTarget`属性中，依此类推组成一个完整的分发链。

> 比如上文的树形图中，序号为1的`ViewGroup`中的`mFirstTouchTarget`指向序号为2的`ViewGroup`，后者的`mFirstTouchTarget`指向序号为3的`ViewGroup`，依此类推，最终组成了一个 1 -> 2 -> 3 -> 4 事件的分发链。

对于一个 **事件序列** 而言，第一次接收到`ACTION_DOWN`事件时，通过DFS算法为`View`树事件的 **分发链** 进行初始化，在这之后，当接收到同一事件序列的其它事件如`ACTION_MOVE`、`ACTION_UP`时，则会跳过递归流程，将事件直接分发给 **分发链** 下一级的`Child`中：

```java
// ViewGroup.dispatchTouchEvent
public boolean dispatchTouchEvent(MotionEvent event) {
  boolean consume = false;
  // ...
  if (event.isActionDown()) {
    // 1.第一次接收到Down事件，递归寻找分发链的下一级，即消费该事件的View
    // 这里可以看到，递归深度搜索的算法只执行了一次
    mFirstTouchTarget = findConsumeChild(this);
  }

  // ...
  if (mFirstTouchTarget == null) {
    // 2.分发链下一级为空，说明没有子`View`消费该事件
    consume = super.dispatchTouchEvent(event);
  } else {
    // 3.mFirstTouchTarget不为空，必然有消费该事件的`View`，直接将事件分发给下一级
    consume = mFirstTouchTarget.child.dispatchTouchEvent(event);
  }
  // ...
  return consume;
}
```

至此，本小节一开始提到的问题得到了解决。

### 3.事件拦截机制

读者应该都有了解，为了增加 **事件分发** 过程中的灵活性，`Android`为`ViewGroup`层级设计了`onInterceptTouchEvent()`函数并向外暴露给开发者，以达到让`ViewGroup`跳过子`View`的事件分发，提前结束 **递流程** ，并自身决定是否消费事件，并将结果反馈给上层级的`ViewGroup`处理。

额外设计这样一个接口是否有必要？读者认真思考可以得知，这是有必要的，最经典的使用场景就是通过重写`onInterceptTouchEvent()`函数以解决开发中常见的 **滑动冲突** 事件，这里我们不再进行引申，仅探讨设计者是如何设计事件拦截机制的。

实际上事件拦截机制的实现非常简单，我们仅需要在正式的事件分发之前，通过条件分支判断是否需要拦截当前事件的分发即可：

```java
// 伪代码实现
// ViewGroup.dispatchTouchEvent
public boolean dispatchTouchEvent(MotionEvent event) {
  // 1.若需要对事件进行拦截，直接中止事件向下分发，让自身决定是否消费事件，并将结果返回
  if (onInterceptTouchEvent(event)) {
    return super.dispatchInputEvent(event);
  }

  // ...
  // 2.若不拦截当前事件，开始事件分发流程
}
```

此外，为了避免额外的开销，设计者根据 **事件序列** 为 **事件拦截机制** 做出了额外的优化处理，保证了 **事件拦截的判断在一个事件序列中只处理一次**，伪代码简单实现如下：

```java
// ViewGroup.dispatchTouchEvent
public boolean dispatchTouchEvent(MotionEvent event) {
  if (mFirstTouchTarget != null) {
    // 1.若需要对事件进行拦截，直接中止事件向下分发，让自身决定是否消费事件，并将结果返回
    if (onInterceptTouchEvent(event)) {
      // 2.确定对该事件序列拦截后，因此就没有了下一级要分发的Child
      mFirstTouchTarget = null;
      // 下一个事件传递过来时，最外层的if判断就会为false，不会再重复执行onInterceptTouchEvent()了
      return super.dispatchInputEvent(event);
    }
  }

  // ...
  // 3.若不拦截当前事件，开始事件分发流程
}
```

> 为了令代码便于理解，上述伪代码中逻辑实际上是有瑕疵的，读者不必纠结于细节，详细实现请参考源码。

至此，**事件分发** 中 **事件拦截机制** 的设计初衷、流程的实现，以及性能的优化也阐述完毕。

在一步步对细节的填充过程中，**事件分发** 体系的设计已初显峥嵘，但回归本质，这些细节犹如血肉，而核心的思想（即递归）才是骨架，只有骨架搭建起来，细节的血肉才能一点点覆于其上，最终演变为成为生机勃勃的 **事件分发** 完整体系。

## 小结

`Android` 整体的事件分发机制十分复杂，单就一篇文章来说，本文也仅仅只能站在巨人的肩膀上，对整体的轮廓进行一个简略的描述，强烈建议参考本文开篇的思维导图并结合源码进行整体小结。

## 参考 & 额外的话

**这一篇文章就能让我理解Android事件分发机制吗？**

当然不能，即使是笔者对此也只是初窥门径而已，在撰写本文的过程中，笔者参考了许多优秀的学习资料，同样笔者也不认为本文比这些资料讲解的更透彻，读者可以参考这些资料 ——一千个人有一千个哈姆雷特，也许这些优秀的资料相比本文更适合你呢？

* 1.Android源码
> 源码永远是学习过程中最好的老师，RTFSC。
* [2.Android开发艺术探索](https://item.jd.com/11760209.html)
> 神书，书中 **View的事件分发机制** 一节将源码分析到了极致，讲解的非常透彻，**强烈建议** 建议读者源码阅读时参考这本书。
* [3.Android应用程序输入事件分发和处理机制](https://www.kancloud.cn/alex_wsc/androids/472164)
> `framework`层原理分析的神文，懂得自然懂。本文中的部分图片也引自该文。
* [4.View InputEvent事件投递源码分析 1-4](https://www.jianshu.com/p/b7f33f46d33c)
> 非常好的博客系列。
* [5.Android中的ViewRootImpl类源码解析](https://blog.csdn.net/qianhaifeng2012/article/details/51737370)
> 对`ViewRootImpl`讲解非常透彻的一篇博客，本文对于`ViewRootImpl`的主要职责的描述也是参考了此文。
* [6.重学安卓：学习 View 事件分发，就像外地人上了黑车！](https://juejin.im/post/5d3140c951882565dd5a66ef)
> 非常欣赏 [@KunMinX](https://juejin.im/user/58ab0de9ac502e006975d757) 老师博文的风格，大道至简，此文对事件消费过程中的 **消费** 二字的讲解非常透彻，给予了笔者很多启示——另，本文不是黑车（笑）。

---

## 关于我

Hello，我是 [却把清梅嗅](https://github.com/qingmei2) ，如果您觉得文章对您有价值，欢迎 ❤️，也欢迎关注我的 [博客](https://juejin.im/user/588555ff1b69e600591e8462/posts) 或者 [Github](https://github.com/qingmei2)。

如果您觉得文章还差了那么点东西，也请通过**关注**督促我写出更好的文章——万一哪天我进步了呢？

* [我的Android学习体系](https://github.com/qingmei2/blogs)
* [关于文章纠错](https://github.com/qingmei2/blogs/blob/master/error_collection.md)
* [关于知识付费](https://github.com/qingmei2/blogs/blob/master/appreciation.md)
* [关于《反思》系列](https://github.com/qingmei2/blogs/blob/master/src/%E5%8F%8D%E6%80%9D%E7%B3%BB%E5%88%97/%E5%8F%8D%E6%80%9D%7C%E7%B3%BB%E5%88%97%E7%9B%AE%E5%BD%95.md)
