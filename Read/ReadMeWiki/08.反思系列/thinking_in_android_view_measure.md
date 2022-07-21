# 反思|Android View机制设计与实现：测量流程

> **反思** 系列博客是我的一种新学习方式的尝试，该系列起源和目录请参考 [这里](https://github.com/qingmei2/android-programming-profile/blob/master/src/%E5%8F%8D%E6%80%9D%E7%B3%BB%E5%88%97/%E5%8F%8D%E6%80%9D%7C%E7%B3%BB%E5%88%97%E7%9B%AE%E5%BD%95.md) 。

## 概述

`Android`本身的`View`体系非常宏大，源码中值得思考和借鉴之处众多，以`View`本身的绘制流程为例，其经过`measure`测量、`layout`布局、`draw`绘制三个过程，最终才能够将其绘制出来并展示在用户面前。

本文将针对绘制过程中的 **测量流程** 的设计思想进行系统地归纳总结，读者需要对`View`的`measure()`相关知识有初步的了解：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/android/core/view/image.mcipzl9ajip.png)

## 整体思路

`View`的测量机制本质非常简单，顾名思义，其目的便是 **测量控件的宽高值**，围绕该目的，`View`的设计者通过代码编织了一整套复杂的逻辑：

* 1、对于子`View`而言，其本身宽高直接受限于父`View`的 **布局要求**，举例来说，父`View`被限制宽度为`40px`,子`View`的最大宽度同样也需受限于这个数值。因此，在测量子`View`之时，子`View`必须已知父`View`的布局要求，这个 **布局要求**，  `Android`中通过使用 `MeasureSpec` 类来进行描述。

* 2、对于完整的测量流程而言，父控件必然依赖子控件宽高的测量；若子控件本身未测量完毕，父控件自身的测量亦无从谈起。`Android`中`View`的测量流程中使用了非常经典的 **递归思想**：对于一个完整的界面而言，每个页面都映射了一个`View`树，其最顶端的父控件测量开始时，会通过 **遍历** 将其 **布局要求** 传递给子控件，以开始子控件的测量，子控件在测量过程中也会通过 **遍历** 将其 **布局要求** 传递给它自己的子控件，如此往复一直到最底层的控件...这种通过**遍历**自顶向下传递数据的方式我们称为 **测量过程中的“递”流程**。而当最底层位置的子控件自身测量完毕后，其父控件会将所有子控件的宽高数据进行聚合，然后通过对应的 **测量策略** 计算出父控件本身的宽高，测量完毕后，父控件的父控件也会根据其所有子控件的测量结果对自身进行测量，这种从底部向上传递各自的测量结果，最终完成最顶层父控件的测量方式我们称为**测量过程中的“归”流程**，至此界面整个`View`树测量完毕。

对于绘制流程不甚熟悉的开发者而言，上述文字似乎晦涩难懂，但这些文字的概括其本质却是绘制流程整体的设计思想，**读者不应该将本文视为源码分析，而应该将自己代入到设计的过程中** ，当深刻理解整个流程的设计思路之后，测量流程代码地设计和编写自然行云流水一气呵成。

## 布局要求

在整个 **测量流程** 中， **布局要求** 都是一个非常重要的核心名词，`Android`中通过使用 `MeasureSpec` 类来对其进行描述。

为什么说 **布局要求** 非常重要呢，其又是如何定义的呢？这要先从结果说起，对于单个`View`来说，测量流程的结果无非是获取控件自身宽和高的值，`Android`提供了`setMeasureDimension()`函数，开发者仅需要将测量结果作为参数并调用该函数，便可以视为`View`完成了自身的测量：

```Java
protected final void setMeasuredDimension(int measuredWidth, int measuredHeight) {
 // measuredWidth 测量结果，View的宽度
 // measuredHeight 测量结果，View的高度
 // 省略其它代码...

 // 该方法的本质就是将测量结果存起来，以便后续的layout和draw流程中获取控件的宽高
 mMeasuredWidth = measuredWidth;
 mMeasuredHeight = measuredHeight;
}
```

需要注意的是，子控件的测量过程本身还应该依赖于父控件的一些布局约束，比如：

* 1.父控件固定宽高只有`${x}px`，子控件设置为`layout_height="${y}px"`;  
* 2.父控件高度为`wrap_content`(包裹内容)，子控件设置为`layout_height="match_parent"`;  
* 3.父控件高度为`match_parent`(填充)，子控件设置为`layout_height="match_parent"`;  

这些情况下，因为无法计算出准确控件本身的宽高值，简单的通过`setMeasuredDimension()`函数似乎不可能达到测量控件的目的，因为 **子控件的测量结果是由父控件和其本身共同决定的** （这个下文会解释），而父控件对子控件的布局约束，便是前文提到的 **布局要求**，即`MeasureSpec`类。

### MeasureSpec类

从面向对象的角度来看，我们将`MeasureSpec`类设计成这样：

```Java
public final class MeasureSpec {

  int size;     // 测量大小
  Mode mode;    // 测量模式

  enum Mode { UNSPECIFIED, EXACTLY, AT_MOST }

  MeasureSpec(Mode mode, int size){
    this.mode = Mode;
    this.size = size;
  }

  public int getSize() { return size; }

  public Mode getMode() { return mode; }
}
```

在设计的过程中，我们将布局要求分成了2个属性。**测量大小** 意味着控件需要对应大小的宽高，**测量模式** 则表示控件对应的宽高模式：

> * UNSPECIFIED：父元素不对子元素施加任何束缚，子元素可以得到任意想要的大小；日常开发中自定义View不考虑这种模式，可暂时先忽略；   
> * EXACTLY：父元素决定子元素的确切大小，子元素将被限定在给定的边界里而忽略它本身大小；这里我们理解为控件的宽或者高被设置为 `match_parent` 或者指定大小，比如`20dp`；
> * AT_MOST：子元素至多达到指定大小的值；这里我们理解为控件的宽或者高被设置为`wrap_content`。

巧妙的是，`Android`并非通过上述定义`MeasureSpec`对象的方式对 **布局要求** 进行描述，而是使用了更简单的二进制的方式，用一个32位的`int`值进行替代：

```Java
public static class MeasureSpec {
    private static final int MODE_SHIFT = 30; //移位位数为30
    //int类型占32位，向右移位30位，该属性表示掩码值，用来与size和mode进行"&"运算，获取对应值。
    private static final int MODE_MASK  = 0x3 << MODE_SHIFT;

    //00左移30位，其值为00 + (30位0)
    public static final int UNSPECIFIED = 0 << MODE_SHIFT;
    //01左移30位，其值为01 + (30位0)
    public static final int EXACTLY     = 1 << MODE_SHIFT;
    //10左移30位，其值为10 + (30位0)
    public static final int AT_MOST     = 2 << MODE_SHIFT;

    // 根据size和mode，创建一个测量要求
    public static int makeMeasureSpec(int size, int mode) {
        return size + mode;
    }

    // 根据规格提取出mode，
    public static int getMode(int measureSpec) {
        return (measureSpec & MODE_MASK);
    }

    // 根据规格提取出size
    public static int getSize(int measureSpec) {
        return (measureSpec & ~MODE_MASK);
    }
}
```

这个`int`值中，前2位代表了测量模式，后30位则表示了测量的大小，对于模式和大小值的获取，只需要通过位运算即可。

以宽度举例来说，若我们设置宽度=5px（二进制对应了101），那么`mode`对应`EXACTLY`,在创建测量要求的时候，只需要通过二进制的相加，便可得到存储了相关信息的`int`值：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/android/core/view/image.29lx5bauvgy.png)

而当需要获得`Mode`的时候只需要用`measureSpec`与`MODE_TASK`相与即可，如下图：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/android/core/view/image.9gd1lev69d.png)

同理，想获得`size`的话只需要只需要`measureSpec`与`~MODE_TASK`相与即可，如下图：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/android/core/view/image.ppkjtxeikvp.png)

现在读者对`MeasureSpec`类有了初步地认识，在`Android`绘制过程中，`View`宽或者高的 **布局要求** 实际上是通过32位的`int`值进行的描述, 而`MeasureSpec`类本身只是一个静态方法的容器而已。

至此`MeasureSpec`类所代表的 **布局要求** 已经介绍完毕，这里我们浅尝辄止，其在后文的 **整体测量流程** 中占有至关重要的作用，届时我们再进行对应的引申。

## 测量单个控件

只考虑单个控件的测量，整个过程需要定义三个重要的函数，分别为：

* `final void measure(int widthMeasureSpec, int heightMeasureSpec)`：执行测量的函数;  
* `void onMeasure(int widthMeasureSpec, int heightMeasureSpec)`：真正执行测量的函数，开发者需要自己实现自定义的测量逻辑;  
* `final void setMeasuredDimension(int measuredWidth, int measuredHeight)`：完成测量的函数；  

为什么说需要定义这样三个函数？

### 1.measure()入口函数：标记测量的开始

首先父控件需要通过调用子控件的`measure()`函数，并同时将宽和高的 **布局要求** 作为参数传入，标志子控件本身测量的开始：

```Java
// 这个是父控件的代码，让子控件开始测量
child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
```

对于`View`的测量流程，其必然包含了2部分：**公共逻辑部分** 和 **开发者自定义测量的逻辑部分**，为了保证公共逻辑部分代码的安全性，设计者将`measure()`方法配置了`final`修饰符:

```Java
public final void measure(int widthMeasureSpec, int heightMeasureSpec) {
  // ... 公共逻辑

  // 开发者需要自己重写onMeasure函数，以自定义测量逻辑
  onMeasure(widthMeasureSpec, heightMeasureSpec);
}
```

开发者不能重写`measure()`函数，并将View自定义测量的策略通过定义一个新的`onMeasure()`接口暴露出来供开发者重写。

### 2.onMeasure()函数：自定义View的测量策略

`onMeasure()`函数中，`View`自身也提供了一个默认的测量策略:

```Java
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
            getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
}
```

以宽度为例，通过这样获取`View`默认的宽度：

> `getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec)`

* 1.在某些情况下（比如自身设置了`minWidth`或者`background`属性），`View`需要通过`getSuggestedMinimumWidth()`函数作为默认的宽度值：

```Java
protected int getSuggestedMinimumWidth() {
    return (mBackground == null) ? mMinWidth : max(mMinWidth, mBackground.getMinimumWidth());
}
```

* 2.这之后，将所得结果作为参数传递到`getDefaultSize(minWidth, widthMeasureSpec)`函数中，根据 **布局要求** 计算出`View`最后测量的宽度值：

```Java
public static int getDefaultSize(int size, int measureSpec) {
    // 宽度的默认值
    int result = size;
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);

    // 根据不同的测量模式，返回的测量结果不同
    switch (specMode) {
      // 任意模式，宽度为默认值
      case MeasureSpec.UNSPECIFIED:
          result = size;
          break;
      // match_parent、wrap_content则返回布局要求中的size值
      case MeasureSpec.AT_MOST:
      case MeasureSpec.EXACTLY:
          result = specSize;
          break;
    }
    return result;
}
```

> 上述代码中，View的默认测量策略也印证了，即使View设置的是`layout_width="wrap_content"`,其宽度也会填充父布局（效果同`match_parent`），高度依然。

### 3.setMeasuredDimension()函数：标志测量的完成

`setMeasuredDimension(width，height)`函数的存在意义非常重要，在`onMeasure()`执行自定义测量策略的过程中，调用该函数标志着`View`的测量得出了结果:

```Java
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // 普遍意义上，setMeasuredDimension()标志着测量结束
    setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
            getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
}

protected final void setMeasuredDimension(int measuredWidth, int measuredHeight) {
 // measuredWidth 测量结果，View的宽度
 // measuredHeight 测量结果，View的高度
 // 省略其它代码...

 // 该方法的本质就是将测量结果存起来，以便后续的layout和draw流程中获取控件的宽高
 mMeasuredWidth = measuredWidth;
 mMeasuredHeight = measuredHeight;
}
```

该函数被设计为由`protected final`修饰，这意味着只能由子类进行调用而不能重写。

函数调用完毕，开发者可以通过`getMeasuredWidth()`或者`getMeasuredHeight()`来获取`View`测量的宽高，代码设计大概是这样：

```Java
public final int getMeasuredWidth() {
    return mMeasuredWidth;
}
public final int getMeasuredHeight() {
    return mMeasuredHeight;
}

// 如何使用
int width = view.getMeasuredWidth();
int height = view.getMeasuredHeight()
```

经过`measure()` -> `onMeasure()` -> `setMeasuredDimension()`函数的调用，最终`View`自身测量流程执行完毕。

## 完整测量流程

对于一个完整的界面而言，每个页面都映射了一个`View`树，见微知著，了解了单个`View`的测量过程，从宏观的角度思考，`View`树整体的测量流程将如何实现？

### 1、设计思路

首先需要理解的是，每种`ViewGroup`的子类的测量策略（也就是`onMeasure()`函数内的逻辑）不尽相同，比如`RelativeLayout`或者`LinearLayout`宽高的测量策略自然不同，但整体思路都大同小异，即 **遍历** 测量所有子控件，根据父控件自身测量策略进行宽高的计算并得出测量结果。

以 **竖直方向布局** 的`LinearLayout`为例，如何完成`LinearLayout`高度的测量？本文抛去不重要的细节，化繁为简，将`LinearLayout`高度的测量策略简单定义为 **遍历获取所有子控件，将高度累加** ，所得值即自身高度的测量结果——如果不知道每个子控件的高度，`LinearLayout`自然无法测量出本身的高度。

因此对于`View`树整体的测量而言，控件的测量实际上是 **自底向上** 的，正如文章开篇 **整体思路** 一节所描述的：

> 对于完整的测量流程而言，父控件必然依赖子控件宽高的测量；若子控件本身未测量完毕，父控件自身的测量亦无从谈起。

此外，因为子控件的测量逻辑受限于父控件传过来的 **布局要求**（MeasureSpec）, 因此整体逻辑应该是：

* 1. 测量开始时，由顶层的父控件将布局要求传递给子控件，以通知子控件开始执行测量；  
* 2. 子控件根据测量策略计算出自身的布局要求，再传递给下一级的子控件，通知子控件开始测量，如此往复，直至到达最后一级的子控件；
* 3. 最后一级的子控件测量完毕后，执行`setMeasuredDimension()`函数，其父控件根据自己的测量策略，将所有`child`的宽高和布局属性进行对应的计算（比如上文中`LinearLayout`就是计算所有子控件高度的和），得到自己本身的测量宽高；
* 4. 该控件通过调用`setMeasuredDimension()`函数完成测量，这之后，它的父控件再根据其自身测量策略完成测量，如此往复，直至完成顶层级`View`的测量，自此，整个页面测量完毕。

这里的设计体现出了经典的 **递归思想**，1、2步骤，开始测量的通知自顶至下，我们称之为测量步骤的 **递流程**，3、4步骤，测量完毕的顺序却是自底至顶，我们称之为测量步骤的 **归流程**。

### 2、递流程的实现

现在根据上一小节的设计思路，开始对 **递流程** 进行编码实现。

在整个**递流程**中，`MeasureSpec`所代表的 **布局要求** 占有至关重要的作用，了解了它在这个过程中的意义，也就理解了为什么我们常说 **子控件的测量结果是由父控件和其本身共同决定的**。

依然以 **竖直方向布局** 的`LinearLayout`为例，我们需要遍历测量其所有的子控件，因此，在`onMeasure()`函数中，第一次我们编码如下：

```Java
// 1.0版本的LinearLayout
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
  // 1.通过遍历，对每个child进行测量
  for(int i = 0 ; i < getChildCount() ; i++){  
    View child = getChildAt(i);
    // 2.直接测量子控件
    child.measure(widthMeasureSpec, heightMeasureSpec);
  }
  // ...
  // 3.所有子控件测量完毕...
  // ...
}
```

这里关注`int heightMeasureSpec`参数，我们知道，这个32位int类型的值，包含了父布局传过来高度的 **布局要求**：测量的大小和模式。现在我们思考，若父布局传过来大小的是屏幕的高度，那么将其作为参数直接执行`child.measure(widthMeasureSpec, heightMeasureSpec)`，让子控件直接开始测量，是合理的吗？

答案当然是否定的，试想这样一个简单的场景，若`LinearLayout`本身设置了`padding`值，那么子控件的最大高度便不能再达到`heightMeasureSpec`中size的大小了，但是如果像上述代码中的步骤2一样，直接对子控件进行测量，子控件就可以从`heightMeasureSpec`参数中取得屏幕的高度，通过`setMeasuredDimension()`将自己的高度设置和父控件高度一致——这导致了`padding`值配置的失效，并不符合预期。

因此，我们需要额外设计一个可重写的函数，用于自定义对`child`的测量：

```Java
protected void measureChild(View child, int parentWidthMeasureSpec,
            int parentHeightMeasureSpec) {
		// 获取子元素的布局参数
    final LayoutParams lp = child.getLayoutParams();
    // 通过padding值，计算出子控件的布局要求
    final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
            mPaddingLeft + mPaddingRight, lp.width);
    final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
            mPaddingTop + mPaddingBottom, lp.height);
    // 将新的布局要求传入measure方法，完成子控件的测量
    child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
}
```

我们定义了`measureChild()`函数，其作用是计算子控件的布局要求，并把新的布局要求传给子控件，再让子控件根据新的布局要求进行测量，这样就解决了上述的问题，由此也说明了为什么 **子控件的测量结果是由父控件和其本身共同决定的**。

这里我们注意到我们设计了一个`getChildMeasureSpec()`函数，那么这个函数是做什么的呢？

#### getChildMeasureSpec()函数

`getChildMeasureSpec()`函数的作用是根据父布局的`MeasureSpec`和`padding`值，计算出对应子控件的`MeasureSpec`，因为这个函数的逻辑是可以复用的，因此将其定义为一个静态函数：

```Java
public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
    //获取父View的测量模式
    int specMode = MeasureSpec.getMode(spec);
    //获取父View的测量大小
    int specSize = MeasureSpec.getSize(spec);
    //父View计算出的子View的大小，子View不一定用这个值
    int size = Math.max(0, specSize - padding);
    //声明变量用来保存实际计算的到的子View的size和mode即大小和模式
    int resultSize = 0;
    int resultMode = 0;
    switch (specMode) {
    //如果父容器的模式是Exactly即确定的大小
    case MeasureSpec.EXACTLY:
    	//子View的高度或宽度>0说明其实一个确切的值，因为match_parent和wrap_content的值是<0的
        if (childDimension >= 0) {
            resultSize = childDimension;
            resultMode = MeasureSpec.EXACTLY;
            //子View的高度或宽度为match_parent
        } else if (childDimension == LayoutParams.MATCH_PARENT) {
            resultSize = size;//将size即父View的大小减去边距值所得到的值赋值给resultSize
            resultMode = MeasureSpec.EXACTLY;//指定子View的测量模式为EXACTLY
           //子View的高度或宽度为wrap_content
        } else if (childDimension == LayoutParams.WRAP_CONTENT) {
            resultSize = size;//将size赋值给result
            resultMode = MeasureSpec.AT_MOST;//指定子View的测量模式为AT_MOST
        }
        break;
    //如果父容器的测量模式是AT_MOST
    case MeasureSpec.AT_MOST:
        if (childDimension >= 0) {
            resultSize = childDimension;
            resultMode = MeasureSpec.EXACTLY;
        } else if (childDimension == LayoutParams.MATCH_PARENT) {
            resultSize = size;
            // 因为父View的大小是受到限制值的限制,所以子View的大小也应该受到父容器的限制并且不能超过父View  
            resultMode = MeasureSpec.AT_MOST;
        } else if (childDimension == LayoutParams.WRAP_CONTENT) {
            resultSize = size;
            resultMode = MeasureSpec.AT_MOST;
        }
        break;
    //如果父容器的测量模式是UNSPECIFIED即父容器的大小未受限制
    case MeasureSpec.UNSPECIFIED:
    	//如果自View的宽和高是一个精确的值
        if (childDimension >= 0) {
        	  //子View的大小为精确值
            resultSize = childDimension;
            //测量的模式为EXACTLY
            resultMode = MeasureSpec.EXACTLY;
            //子View的宽或高为match_parent
        } else if (childDimension == LayoutParams.MATCH_PARENT) {
        	  //因为父View的大小是未定的，所以子View的大小也是未定的
            resultSize = 0;
            resultMode = MeasureSpec.UNSPECIFIED;
        } else if (childDimension == LayoutParams.WRAP_CONTENT) {
            resultSize = 0;
            resultMode = MeasureSpec.UNSPECIFIED;
        }
        break;
    }
    //根据resultSize和resultMode调用makeMeasureSpec方法得到测量要求，并将其作为返回值
    return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
}
```

逻辑分支相对较多，注释中已经将子控件 **布局要求** 的计算逻辑写清楚了，总结如下图，[原图链接](https://www.jianshu.com/p/1dab927b2f36)：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/android/core/view/image.ct3u0fy6b1q.png)

> 为什么说这个函数非常重要？因为这个函数才是 **子控件的测量结果是由父控件和其本身共同决定的** 最直接的体现，同时，在不同的布局模式下（`match_parent`、`wrap_content`、指定`dp/px`），其对应子控件的布局要求的返回值亦不同，建议读者认真理解这段代码。

回到前文，现在我们对`onMeasure()`的方法定义如下：

```Java
// 2.0版本的LinearLayout
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
  // 1.通过遍历，对每个child进行测量
  for(int i = 0 ; i < getChildCount() ; i++){  
     View child = getChildAt(i);
     // 2.计算新的布局要求，并对子控件进行测量
     measureChild(child, widthMeasureSpec, heightMeasureSpec);
  }
  // ...
  // 3.所有子控件测量完毕...
  // ...
}
```

### 3、归流程的实现

现在，所有子控件测量完毕，接下来 **归流程** 的实现就很简单了，将所有`child`的`height`进行累加，并调用 `setMeasuredDimension()`结束测量即可：

```Java
// 3.0版本的LinearLayout
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
  // 1.通过遍历，对每个child进行测量
  for(int i = 0 ; i < getChildCount() ; i++){  
     View child = getChildAt(i);
     // 2.计算新的布局要求，并对子控件进行测量
     measureChild(child, widthMeasureSpec, heightMeasureSpec);
  }
  // 3.完成子控件的测量,对高度进行累加
  int height = 0;
  for(int i = 0 ; i < getChildCount() ; i++){  
      height += child.getMeasuredHeight();  
  }
  // 4.完成LinearLayout的测量
  setMeasuredDimension(width, height);
}
```

乍一看，似乎很难体现出整个流程的 **递归** 性，实际上当我们宏观从`View`树的树顶顺着往下整理思路，代码逻辑的执行顺序一目了然：

![](https://raw.githubusercontent.com/qingmei2/qingmei2-blogs-art/master/android/core/view/image.w9prhgpqbrd.png)

> 如图所示，实线代表了测量流程中整体自顶向下的 **递流程**， 而虚线代表了自底向上的 **归流程**。

至此，测量流程整体实现完毕。

## 参考

* Android源码
* [Android开发艺术探索](https://item.jd.com/11760209.html)
* [Android中measure过程、WRAP_CONTENT详解以及xml布局文件解析流程浅析](https://blog.csdn.net/qinjuning/article/details/8074262)
* [Android手把手带你清晰梳理自定义View的工作全流程](https://blog.csdn.net/carson_ho/article/details/98477394)

---

## 关于我

Hello，我是 [却把清梅嗅](https://github.com/qingmei2) ，如果您觉得文章对您有价值，欢迎 ❤️，也欢迎关注我的 [博客](https://juejin.im/user/588555ff1b69e600591e8462/posts) 或者 [Github](https://github.com/qingmei2)。

如果您觉得文章还差了那么点东西，也请通过**关注**督促我写出更好的文章——万一哪天我进步了呢？

* [我的Android学习体系](https://github.com/qingmei2/blogs)
* [关于文章纠错](https://github.com/qingmei2/blogs/blob/master/error_collection.md)
* [关于知识付费](https://github.com/qingmei2/blogs/blob/master/appreciation.md)
* [关于《反思》系列](https://github.com/qingmei2/blogs/blob/master/src/%E5%8F%8D%E6%80%9D%E7%B3%BB%E5%88%97/%E5%8F%8D%E6%80%9D%7C%E7%B3%BB%E5%88%97%E7%9B%AE%E5%BD%95.md)
