# 反思|Android View机制设计与实现：布局流程

> **反思** 系列博客是我的一种新学习方式的尝试，该系列起源和目录请参考 [这里](https://github.com/qingmei2/android-programming-profile/blob/master/src/%E5%8F%8D%E6%80%9D%E7%B3%BB%E5%88%97/%E5%8F%8D%E6%80%9D%7C%E7%B3%BB%E5%88%97%E7%9B%AE%E5%BD%95.md) 。

## 概述

`Android`本身的`View`体系非常宏大，源码中值得思考和借鉴之处众多，以`View`本身的绘制流程为例，其经过`measure`测量、`layout`布局、`draw`绘制三个过程，最终才能够将其绘制出来并展示在用户面前。

相比 **测量流程** ，**布局流程** 相对简单很多，如果读者不了解 **测量流程** ，建议阅读这篇文章：

[反思 | Android View机制设计与实现：测量流程](https://github.com/qingmei2/android-programming-profile/blob/master/src/%E5%8F%8D%E6%80%9D%E7%B3%BB%E5%88%97/View/%E5%8F%8D%E6%80%9D%7CAndroid%20View%E6%9C%BA%E5%88%B6%E8%AE%BE%E8%AE%A1%E4%B8%8E%E5%AE%9E%E7%8E%B0%EF%BC%9A%E6%B5%8B%E9%87%8F%E6%B5%81%E7%A8%8B.md)

## 整体思路

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/android/core/view/image.7b0stxc36sm.png)

**测量流程** 的目的是 **测量控件宽高** ，但只获取控件的宽高实际上是不够的，对于`ViewGroup`而言还需要一套额外的逻辑，负责对所有子控件进行对应策略的布局，这就是 **布局流程**（layout）。

* 1.对于叶子节点的`View`而言，其本身没有子控件，因此一般情况下仅需要记录自己在父控件的位置信息，并不需要处理为子控件布局的逻辑；
* 2.对于整体的布局流程而言，子控件的位置必然交由父控件布置，和 [测量流程](https://github.com/qingmei2/android-programming-profile/blob/master/src/%E5%8F%8D%E6%80%9D%E7%B3%BB%E5%88%97/View/%E5%8F%8D%E6%80%9D%7CAndroid%20View%E6%9C%BA%E5%88%B6%E8%AE%BE%E8%AE%A1%E4%B8%8E%E5%AE%9E%E7%8E%B0%EF%BC%9A%E6%B5%8B%E9%87%8F%E6%B5%81%E7%A8%8B.md) 一样，`Android`中布局流程中也使用了递归思想：对于一个完整的界面而言，每个页面都映射了一个`View`树，其最顶端的父控件开始布局时，会通过自身的布局策略依次计算出每个子控件的位置——值得一提的是，为了保证控件树形结构的 **内部自治性**，每个子控件的位置为 **相对于父控件坐标系的相对位置** ，而不是以屏幕坐标系为准的绝对位置。位置计算完毕后，作为参数交给子控件，令子控件开始布局；如此往复一直到最底层的控件，当所有控件都布局完毕，整个布局流程结束。

对于布局流程不甚熟悉的开发者而言，上述文字似乎晦涩难懂，但这些文字的概括其本质却是布局流程整体的设计思想，**读者不应该将本文视为源码分析，而应该将自己代入到设计的过程中** ，当深刻理解整个流程的设计思路之后，布局流程代码地设计和编写自然行云流水一气呵成。

## 单个View的布局流程

首先思考一个问题，布局流程的本质是测量结束之后，将每个子控件分配到对应的位置上去——既然有子控件，那说明进行布局流程的主体理应是`ViewGroup`，那么作为叶子节点的单个`View`来说，为什么也会有布局流程呢？

读者认真思考可以得出，布局流程实际上是一个复杂的过程，整个流程主要逻辑顺序如下：

* 1.决定是否需要重新进行测量流程`onMeasure()`；
* 2.将自身所在的位置信息进行保存；
* 3.判断本次布局流程是否引发了布局的改变；
* 4.若布局发生了改变，令所有子控件重新布局；
* 5.若布局发生了改变，通知所有观察布局改变的监听发送通知。

整个布局过程中，除了4是`ViewGroup`自身需要做的，其它逻辑对于`View`和`ViewGroup`而言都是公共的——这说明单个`View`也是有布局流程的需求的。

现在将整个布局过程定义三个重要的函数，分别为：

* `void layout(int l, int t, int r, int b)`：控件自身整个布局流程的函数;  
* `void onLayout(boolean changed, int left, int top, int right, int bottom)`：ViewGroup布局逻辑的函数，开发者需要自己实现自定义布局逻辑;
* `void setFrame(int left, int top, int right, int bottom)`：保存最新布局位置信息的函数;

为什么需要定义这样三个函数？

### 1.layout函数：标志布局的开始

现在我们站在单个`View`的角度，首先父控件需要通过调用子控件的`layout()`函数，并同时将子控件的位置（`left、right、top、bottom`）作为参数传入，标志子控件本身布局流程的开始：

```Java
// 伪代码实现
public void layout(int l, int t, int r, int b) {
  // 1.决定是否需要重新进行测量流程（onMeasure）
  if(needMeasureBeforeLayout) {
    onMeasure(mOldWidthMeasureSpec, mOldHeightMeasureSpec)
  }

  // 先将之前的位置信息进行保存
  int oldL = mLeft;
  int oldT = mTop;
  int oldB = mBottom;
  int oldR = mRight;
  // 2.将自身所在的位置信息进行保存；
  // 3.判断本次布局流程是否引发了布局的改变；
  boolean changed = setFrame(l, t, r, b);

  if (changed) {
    // 4.若布局发生了改变，令所有子控件重新布局；
    onLayout(changed, l, t, r, b);
    // 5.若布局发生了改变，通知所有观察布局改变的监听发送通知
    mOnLayoutChangeListener.onLayoutChange(this, l, t, r, b, oldL, oldT, oldR, oldB);
  }
}
```

这里笔者通过伪代码的方式对布局流程进行了描述，实际上`View`本身的`layout()`函数内部虽然多处不同，但核心思想是一致的——`layout()`函数实际上代表了控件自身布局的整个流程，`setFrame()`和`onLayout()`函数都是`layout()`中的一个步骤。

### 2.setFrame函数：保存本次布局信息

为什么需要保存布局信息？因为我们总是有获取控件的宽和高的需求——比如接下来的`onDraw()`绘制阶段；而保存了布局信息，就能通过这些值计算控件本身的宽高：

```Java
public final int getWidth() { return mWidth; }

public final int getHeight() { return mHeight; }
```

由此可见，保存控件的布局信息确实很有必要，Android中将`layout()`函数的四个参数所代表的位置信息，交给了`setFrame()`函数去保存:

```Java
protected boolean setFrame(int left, int top, int right, int bottom) {
    // 布局是否发生了改变
    boolean changed = false;
    // 若最新的布局信息和之前的布局信息不同，则保存最新的布局信息
    if (mLeft != left || mRight != right || mTop != top || mBottom != bottom) {
        changed = true;
        mLeft = left;
        mTop = top;
        mRight = right;
        mBottom = bottom;
    }
    return changed;
}
```

`setFrame()`函数被`protected`修饰，这意味着开发者可以通过重写该函数来定义`View`本身保存布局信息的逻辑，现在将目光转到`mLeft、mTop、mRight、mBottom`四个变量上。

顾名思义，这四个变量对应的自然是`View`自身所在的位置，那么`View`是如何通过这四个变量描述控件的位置信息呢？

### 3.相对位置和绝对位置

通过一张图来看一下这四个变量所代表的意义：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/android/core/view/image.54tj4jprfcy.png)

这时候不可避免的会面临另外一个问题，这个`mLeft、mTop、mRight、mBottom`的值所对应的坐标系是哪里呢？

这里需要注意的是，为了保证控件树形结构的 **内部自治性**，每个子控件的位置为 **相对于父控件坐标系的相对位置** ，而不是以屏幕坐标系为准的绝对位置：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/android/core/view/image.wx5v0m3cjc.png)

> 反过来想，如果这些位置信息是以屏幕坐标系为准，那么就意味着每个叶子节点的`View`会持有保存从根节点`ViewGroup`直到自身父`ViewGroup`每个控件的位置信息，在计算布局时则更为繁琐，很明显是不合理的设计。

既然`View`自身持有了这样的位置信息，实际上前文中获取控件自身宽高的`getWidth()`和`getHeight()`方法就可以重新这样定义：

```Java
public final int getWidth() { return mRight - mLeft; }

public final int getHeight() { return mBottom - mTop; }
```

这也说明了在布局流程中的`setFrame()`函数执行完毕后（且布局确实发生了改变），开发者才能通过`getWidth()`和`getHeight()`方法获取控件正确的宽高值。

### 4.onLayout函数：计算子控件的位置

对于叶子节点的`View`而言，其并没有子控件，因此一般情况下并没有为子控件布局的意义（特殊情况请参考`AppCompatTextView`等类），因此`View`的`onLayout()`函数被设计为一个空的实现：

```Java
protected void onLayout(boolean changed, int left, int top, int right, int bottom) {  }
```

而在`ViewGroup`中，不同类型的`ViewGroup`有不同的布局策略，这些布局策略的逻辑各不相同，因此该方法被设计为抽象接口，开发者必须实现这个方法以定义`ViewGroup`的布局策略：

```Java
@Override
protected abstract void onLayout(boolean changed,int l, int t, int r, int b);
```

> 以`LinearLayout`为例，其布局策略为 **根据排布方向，将其所有子控件按照指定方向依次排列布局**

至此单个`View`的测量流程结束，关于`ViewGroup`的`onLayout`函数细节将在下文进行描述。

## 完整布局流程

相比较测量流程，布局流程相对比较简单，整体思路是，对于一个完整的界面而言，每个页面都映射了一个`View`树，最顶端的父控件开始布局时，会通过自身的布局策略依次计算出每个子控件的位置。位置计算完毕后，作为参数交给子控件，令子控件开始布局；如此往复一直到最底层的控件，当所有控件都布局完毕，整个布局流程结束。

`ViewGroup`虽然重写了`View`的`layout()`函数，但实质上并未进行大的变动，我们大抵可以认为`ViewGroup`和`View`的`layout()`逻辑一致：

```Java
@Override
public final void layout(int l, int t, int r, int b) {
    if (!mSuppressLayout && (mTransition == null || !mTransition.isChangingLayout())) {
        if (mTransition != null) {
            mTransition.layoutChange(this);
        }
        // 仍然是执行View层的layout函数
        super.layout(l, t, r, b);
    } else {
        mLayoutCalledWhileSuppressed = true;
    }
}
```

唯一需要注意的是，开发者必须实现`onLayout()`函数以定义`ViewGroup`的布局策略，这里以 **竖直布局** 的`LinearLayout`的伪代码为例：

```Java
@Override
protected void onLayout(boolean changed, int l, int t, int r, int b) {
  int childTop;
  int childLeft;

  // 遍历所有子View
  for (int i = 0; i < count; i++) {
    // 获取子View
    final View child = getVirtualChildAt(i);
    // 获取子View宽高，注意这里使用的是 getMeasuredWidth 而不是 getWidth
    final int childWidth = child.getMeasuredWidth();
    final int childHeight = child.getMeasuredHeight();

    // 令所有子控件开始布局
    setChildFrame(child, childLeft, childTop, childWidth, childHeight);   
    // 高度累加，下一个子View的 top 就等于上一个子View的 bottom ，符合竖直线性布局从上到下的布局策略   
    childTop += childHeight;      
  }
}

private void setChildFrame(View child, int left, int top, int width, int height) {
    // 这里可以看到，子控件的mRight实际上就是 mLeft + getMeasuredWidth()
    // 而在getWidth()函数中，mRight-mLeft的结果就是getMeasuredWidth()
    // 因此，getWidth() 和 getMeasuredWidth() 是一致的
    child.layout(left, top, left + width, top + height);
}
```

读者需要注意到一个细节，子控件的宽度的获取，我们并未使用`getWidth()`，而是使用了`getMeasuredWidth()`，这就引发了另外一个疑问，这两个函数的区别在哪里。

### getWidth 和 getMeasuredWidth 的区别

首先，从上文中我们得知，`getWidth()`和`getHeight()`函数的相关信息实际上是在`setFrame()`函数执行完毕才准备完毕的——我们大致可以认为是这两个函数 **只有布局流程(layout)执行完毕才能调用**，而在父控件的`onLayout()`函数中，获取子控件宽度和高度时，子控件还并未开始进行布局流程，因此此时不能调用`getWidth()`函数，而只能通过`getMeasuredWidth()`函数获取控件测量阶段结果的宽度。

那么当控件绘制流程执行完毕后，`getWidth()`和`getMeasuredWidth()`函数的值有什么区别呢？从上述`setChildFrame()`函数中的源码可以得知，布局流程执行后，`getWidth()`返回值的本质其实就是`getMeasuredWidth()`——因此本质上，当我们没有手动调用`layout()`函数强制修改控件的布局信息的话，两个函数的返回值大小是完全一致的。

### 整体流程小结

在整个布局流程的设计中，设计者将流程中公共的业务逻辑（保存布局信息、通知布局发生改变的监听等）通过`layout()`函数进行了整合，同时，将`ViewGroup`额外需要的自定义布局策略通过`onLayout()`函数向外暴露出来，针对组件中代码的可复用性和可扩展性进行了合理的设计。

至此，布局流程整体实现完毕。借用 [carson_ho](https://www.jianshu.com/p/158736a2549d) 绘制的流程图对整体布局流程做一个总结：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/android/core/view/image.uvcxxkjn6ac.png)

## 参考

* Android源码
* [Android View框架的layout机制](https://www.cnblogs.com/xyhuangjinfu/p/5435253.html)
* [自定义View Layout过程 - 最易懂的自定义View原理系列](https://www.jianshu.com/p/158736a2549d)

---

## 关于我

Hello，我是 [却把清梅嗅](https://github.com/qingmei2) ，如果您觉得文章对您有价值，欢迎 ❤️，也欢迎关注我的 [博客](https://juejin.im/user/588555ff1b69e600591e8462/posts) 或者 [Github](https://github.com/qingmei2)。

如果您觉得文章还差了那么点东西，也请通过**关注**督促我写出更好的文章——万一哪天我进步了呢？

* [我的Android学习体系](https://github.com/qingmei2/blogs)
* [关于文章纠错](https://github.com/qingmei2/blogs/blob/master/error_collection.md)
* [关于知识付费](https://github.com/qingmei2/blogs/blob/master/appreciation.md)
* [关于《反思》系列](https://github.com/qingmei2/blogs/blob/master/src/%E5%8F%8D%E6%80%9D%E7%B3%BB%E5%88%97/%E5%8F%8D%E6%80%9D%7C%E7%B3%BB%E5%88%97%E7%9B%AE%E5%BD%95.md)
