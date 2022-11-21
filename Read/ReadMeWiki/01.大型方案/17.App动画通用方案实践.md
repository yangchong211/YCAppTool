#### 目录介绍
- 01.整体概述
    - 1.1 项目背景
    - 1.2 遇到问题
    - 1.3 基础概念介绍
    - 1.4 设计目标
    - 1.5 产生收益分析
- 02.动画基础介绍
    - 2.1 逐帧动画
    - 2.2 补间动画
    - 2.3 属性动画
    - 2.4 过渡动画
    - 2.5 属性和补间动画区别
    - 2.6 其他动画
- 03.各个动画使用场景
    - 3.1 帧动画使用场景
    - 3.2 补间动画场景
    - 3.3 属性动画场景
    - 3.4 过渡动画场景
    - 3.5 其他动画场景
- 04.动画的设计思想
    - 4.1 逐帧动画思想
    - 4.2 补间动画思想
    - 4.3 属性动画思想
    - 4.4 过渡动画思想
- 05.动画的原理分析
    - 5.1 逐帧动画原理
    - 5.2 补间动画原理
    - 5.3 属性动画原理
    - 5.4 过渡动画原理
    




### 01.整体概述
#### 1.1 项目背景
- Android 动画在开发中是不可或缺的功能，或者说是界面灵动的添加剂。那你是否总结过 Android 中总共为开发者提供了多少种方式的动画呢？


#### 1.2 遇到问题


#### 1.3 基础概念介绍


#### 1.4 设计目标


#### 1.5 产生收益分析


### 02.动画基础介绍
#### 2.1 逐帧动画
- 逐帧动画简单
    - 也叫Drawable Animation动画，是最简单最直观动画类型。可以把它想像成播放电影，一堆的图片连贯起来，按照顺序播放就成了动画。
- 逐帧动画实现方式有两种：
    - 第一种在res/drawable目录下新建动画XML文件；第二种创建AnimationDrawable对象然后将将资源图片添加进来播放即可。



#### 2.2 补间动画
- 对于 Animation 动画实现机制
    - 实现机制是，在每次进行绘图的时候，通过对整块画布的矩阵进行变换，从而实现一种视图坐标的移动，但实际上其在 View内部真实的坐标位置及其他相关属性始终恒定。
- 补间动画特点
    - 无需关注每一帧，只需要定义动画开始与结束两个关键帧，并指定动画变化的时间与方式等。
- 补间动画实现方式
    - 第一种，通过xml配置，把文件放到res/anim/目录下，alpha，rotate，scale，translate，set(一个持有其它动画元素的容器)
    - 第二种，通过代码实现，AlphaAnimation，RotateAnimation，ScaleAnimation，TranslateAnimation，AnimationSet(四种的组合容器管理类)
- 补间动画使用注意点
    - 补间动画执行之后并未改变 View 的真实布局属性值。切记这一点，譬如在 Activity 中有一个 Button 在屏幕上方，设置了平移动画移动到屏幕下方然后保持动画最后执行状态呆在屏幕下方，这时如果点击屏幕下方动画执行之后的 Button 是没有任何反应的，而点击原来屏幕上方没有 Button 的地方却响应的是点击Button的事件。



#### 2.3 属性动画
- 为何有属性动画的背景。主要是补间动画存在一些缺点
    - a.作用对象局限：View 。即补间动画 只能够作用在视图View上，即只可以对一个Button、TextView、甚至是LinearLayout、或者其它继承自View的组件进行动画操作，但无法对非View的对象进行动画操作
    - b.没有改变View的属性，只是改变视觉效果
    - c.动画效果单一，无法满足复杂的动画效果
- 属性动画特点
    - 作用对象：任意 Java 对象，不再局限于 视图View对象
    - 实现的动画效果：可自定义各种动画效果，不再局限于4种基本变换：平移、旋转、缩放 & 透明度
- 对于 Animator 动画实现机制
    - Animator动画的实现机制说起来其实更加简单一点，因为他其实只是计算动画开启之后，结束之前，到某个时间点得时候，某个属性应该有的值，然后通过回调接口去设置具体值。
    - 其实 Animator 内部并没有针对某个 view 进行刷新，来实现动画的行为，动画的实现是在设置具体值的时候，方法内部自行调取的类似 invalidate 之类的方法实现的。也就是说使用 Animator 内部的属性发生了变化。
- 属性动画的原理
    - 属性动画，要求对象有这个属性的set方法，执行时会根据传入的 属性初始值、最终值，在每帧更新时调用set方法设置当前时刻的 属性值。
    - 随着时间推移，set的属性值会接近最终值，从而达到动画效果。如果没传入初始值，那么对象还要有get方法，用于获取初始值。
    - 见PropertyValuesHolder的setupValue、setAnimatedValue方法。
      



#### 2.4 过渡动画
- 什么是转场动画
    - Activity 和 Fragment 变换是建立在名叫 Transitions 的安卓新特性之上的。为在不同的 UI 状态之间产生动画效果提供了非常方便的 API。
    - Transition 可以被用来实现 Activity 或者 Fragment 切换时的异常复杂的动画效果。
- 转场动画核心概念
    - 场景（scenes）和变换（transitions）。场景（scenes）定义了当前的 UI 状态；变换（transitions）则定义了在不同场景之间动画变化的过程。
- 当一个场景改变的时候，transition 主要负责：
    - 1）捕捉在开始场景和结束场景中每个 View 的状态。2）根据视图一个场景移动到另一个场景的差异创建一个 Animator。





#### 2.5 属性和补间动画区别
- View 动画/视图动画：
    - View 动画只能为 View 添加动画效果，且不能监听 View 相关属性的变化过程。
    - View 动画提供的动画能力较为单一，目前只支持帧动画、缩放动画、位移动画、旋转动画、透明度动画以及这些动画的集合动画。
    - View动画改变的是 View 的绘制效果，View 的真正位置和相关属性并不会改变，这也就造成了点击事件的触发区域是动画前的位置而不是动画后的位置的原因。
- 属性动画
    - 属性动画作用对象不局限在 View 上，而是任何提供了 Getter 和 Setter 方法的对象的属性上。
    - 属性动画没有直接改变 View 状态的能力，而是通过动态改变 View 相关属性的方式来改变 View 的显示效果。
    - 属性动画使用更方便，可以用更简洁的代码实现相关的动画效果。
    - 属性动画上手难度较高，对于 propertyName 需要自己去挖掘，或者自己通过 Wrapper 的方式去自定义 propertyName。
- 效果上区别
    - 属性动画才是真正的实现了view的移动，补间动画对view的移动更像是在不同地方绘制了一个影子，实际对象还是处于原来的地方。当动画的repeatCount设置为无限循环时，如果在Activity退出时没有及时将动画停止，属性动画会导致Activity 无法释放而导致内存泄漏，而补间动画却没问题。xml文件实现的补间动画，复用率极高。在 Activity切换，窗口弹出时等情景中有着很好的效果。
    - 补间动画还有一个致命的缺陷，就是它只是改变了View的显示效果而已，而不会真正去改变View的属性。什么意思呢？比如说，现在屏幕的左上角有一个按钮，然后我们通过补间动画将它移动到了屏幕的右下角，现在你可以去尝试点击一下这个按钮，点击事件是绝对不会触发的，因为实际上这个按钮还是停留在屏幕的左上角，只不过补间动画将这个按钮绘制到了屏幕的右下角而已。
- 或者更简单一点说
    - 前者属性动画，改变控件属性，（比如平移以后点击有事件触发）
    - 后者补间动画，只产生动画效果（平移之后点无事件触发） 




#### 2.6 其他动画
- 触摸反馈动画
    - Ripple 波纹效果有两种。使用也非常简单，只要将上面两种效果设置为控件的背景或者前景就好了，同时需要给控件设置点击事件、或把控件设置为可点击 android:clickable="true"
    ```
    //有边界
    ?android:attr/selectableItemBackground
    //无边界 （要求API21以上）
    ?android:attr/selectableItemBackgroundBorderless 
    ```
- 揭露动画（Reveal Effect）
    - 揭露动画在系统中很常见，就是类似波纹的效果， 从某一个点向四周展开或者从四周向某一点聚合起来。
- 视图状态动画（Animate View State Changes）
    - View 在状态改变时执行的动画效果。和通过 selector 选择器给 Button 设置不同状态下的背景效果是一样一样的。


### 03.各个动画使用场景
#### 3.1 帧动画使用场景
- 帧动画这个很好理解
    - 其实就和看的动画片一样，每一帧代表一个画面动作，当快速逐帧显示时，速度到达人眼无法分辨每一帧时，就达到了动画的效果。
- 还有几种特殊的使用场景：
    - 第一种是设备的开关机动画；第二种是“复杂” 的动画效果，看似不可能完成的动画
- 注意要点说明
    - 常常使用到的是，当我们需要一些比较复杂的图片动画显示效果时，其他动画又实现不了，这时我们可以考虑帧动画，但要注意防止 OOM。


#### 3.2 补间动画场景
- 最常见是作用在View上
    - View 一般会用作直接作用页面中的 View 上，实现基本的动画效果：平移、旋转、缩放、透明度、或前几者的交集：
- 还有几种特殊的使用场景：
    - 第一种：给 PopupWindow 设置显示隐藏的动画效果。或者给 Dialog 设置弹出动画效果。一般是设置style样式中添加xml动画
    - 第二种：给 Activity 设置页面跳转、退出动画效果。借助设置 overridePendingTransition(int enterAnim, int exitAnim) 方法。跟在 startActivity() 或 finish() 后面，在页面转换时就显示上面方法设置的切换动画效果。
    - 第三种：给 ViewGroup 设置子控件的进场动画效果。设置一条 android:layoutAnimation="@anim/anim_layout" 的属性
    - 第四种：给 Activity 转场动画配置theme主题。设置windowAnimationStyle的动画样式
- 举第一个例子给弹窗设置动画效果
    ```
    //第一步：调用弹窗中api设置视图动画属性
    popupWindow.setAnimationStyle(R.style.pop_anim);
    //第二步：做成一个 `style`, 在 `res/values/styles.xml` 文件里加上动画属性
    <style name="pop_anim">
        <item name="android:windowEnterAnimation">@anim/pop_in</item>
        <item name="android:windowExitAnimation">@anim/pop_out</item>
    </style>
    ```
- 举第二个例子使用theme配置style动画样式。然后直接配置Activity的theme主题就OK
    ```
    <style name="ActivityTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <item name="android:windowAnimationStyle">@style/ActivityAnim</item>
    </style>
    <style name="ActivityAnim">
        <item name="android:activityOpenEnterAnimation">@anim/activity_in</item>
        <item name="android:activityOpenExitAnimation">@anim/activity_out</item>
        <item name="android:activityCloseEnterAnimation">@anim/activity_close_in</item>
        <item name="android:activityCloseExitAnimation">@anim/activity_close_out</item>
    </style>
    ```


#### 3.3 属性动画场景
- 最常见的场景
    - 基本上视图动画作用在 View 上的动画效果，属性动画都可以实现；
- 其他的使用场景说明
    - 在自定义 View 时，需要实现一些复杂的动画效果，或对 View 的一些特殊属性值进行动画变更时，视图动画无法实现时；
    - 属性动画你也可以用在非动画场景，比如，你在自定义 View 需要一个有一定规律（根据特定差值器变化）且可监听的数值变化器，这个时候借助属性动画是再合适不过了。
- 第一种使用XML属性动画方式
    ``` xml
    //Animator anim = AnimatorInflater.loadAnimator(this, R.animator.object_animator);
    AnimatorSet anim = (AnimatorSet) AnimatorInflater.loadAnimator(myContext,R.animtor.set_animator);
    anim.setTarget(myObject);
    anim.start();
    ```
- 第二种使用代码属性动画方式
    ``` java
    ObjectAnimator mAnimator = ObjectAnimator.ofFloat(view, type, start, end);
    mAnimator.start();
    ```


#### 3.4 过渡动画场景
- 转场动画听名字就知道它的使用场景了，转场、转场自然是用在场景转换的时候：
    - 转场效果我们一般用在 Activity 切换时的动画效果上；
    - 共享元素一般我们使用在转换的前后两个页面有共同元素时；
    - 同时也可以在 Activity 布局发生场景变化时，让其中的 View 产生相应的过渡动画。
- 在介绍 activity 的切换动画之前我们先来说明一下实现切换 activity 的两种方式：
    - 调用 startActivity 方法启动一个新的 Activity 并跳转其页面。这涉及到了旧的 Activity 的退出动画和新的 Activity 的显示动画。
    - 调用 finish 方法销毁当前的 Activity 返回上一个 Activity 界面。这涉及到了当前 Activity 的退出动画和前一个 Activity 的显示动画。
- Activity转场动画实现的几种方式
    - 第一种：activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)，这个是在startActivity 方法或者是 finish 方法调用之后立即执行。
    - 第二种：使用 style 的方式定义 Activity 的切换动画。这里的 windowAnimationStyle 就是我们定义 Activity 切换动画的 style，设置这个动画文件则可以为Activity 的跳转配置动画效果。
    - 第三种：使用 ActivityOptions 切换动画实现 Activity 跳转动画。Material Design转场动画，三种进入和退出过渡动画效果(Explode，Slide，Fade)，然后开启activity时传递ActivityOptions.makeSceneTransitionAnimation
    - 第四种：使用 ActivityOptions 动画共享组件的方式实现跳转 Activity 动画。需要注意共享视图设置的transitionName保持一致
    - 第五种：使用 ActivityOptions 之后内置的动画效果通过 style 的方式。
    - 第六种：过渡模式，
    - 关于转场动画，可以看看这篇博客：[Android动画系列---转场动画详解](https://www.jianshu.com/p/78fdcceb9a59)




#### 3.5 其他动画场景
- 触摸反馈动画（Ripple Effect）
    - 所谓触摸反馈动画就是一种点击效果，作用在可点击的 View 上时，当有点击事件时会有涟漪般的反馈效果，使用在 按钮 上是再好不过了。
- 揭露动画（Reveal Effect）
    - 可以用在 Activity 里面的 View 动画效果，用来揭露某个隐藏 View 的显示；也可以使用在 Activity 跳转过渡动画中。
- 视图状态动画（Animate View State Changes）
    - 当 View 的状态改变时，希望此时显示的效果和静态效果有所区分，即显示效果也做出相应的改变，比如 Z 轴抬高，大小改变、或其他动画效果等。


### 04.动画的设计思想
#### 4.1 逐帧动画思想


#### 4.2 补间动画思想


#### 4.3 属性动画思想


#### 4.4 过渡动画思想



### 05.动画的原理分析
- 需要有一个明确的思路
    - 例如：源码的入口的选择、甚至对其实现进行简单的猜测，源码分析相当于一个验证的过程，带着一个目标去看源码，这样的话，分析和理解起来更为方便。



#### 5.1 逐帧动画原理



#### 5.2 补间动画原理
- 先了解一下View绘制的步骤
    - 要了解Android动画是如何加载出来的，首先要了解Android View 是如何组织在一起的。每个窗口是一颗View树，RootView是DecorView，在布局文件中声明的布局都是DecorView的子View是通过setContentView来设置进入窗口内容的。
    - 因为View的布局就是一棵树，所以绘制的时候也是按照树形结构来遍历每个View进行绘制，ViewRoot.java中 draw函数准备好Canvas后 调用 mView.draw(canvas)，这里的mView是DecorView。
- **下面看一下View递归绘制的几个步骤:**
    - 1.绘制背景
    - 2.如果需要,保存画布(canvas),为淡入淡出做准备
    - 3.通过调用View.onDraw(canvas)绘制View本身的内容
    - 4.通过 dispatchDraw(canvas)绘制自己的孩子,dispatchDraw->drawChild->child.draw(canvas) 这样的调用过程被用来保证每个子 View 的 draw 函数都被调用
    - 5.如果需要，绘制淡入淡出相关的内容并恢复保存的画布所在的层（layer）
    - 6.绘制修饰的内容（例如滚动条）
- 当一个 ChildView 要重画时
    - 它会调用其成员函数 invalidate() 函数将通知其 ParentView 这个 ChildView 要重画，这个过程一直向上遍历到 ViewRoot，当 ViewRoot 收到这个通知后就会调用上面提到的 ViewRoot 中的 draw 函数从而完成绘制。
    - Android 动画就是通过 ParentView 来不断调整 ChildView 的画布坐标系来实现的。
- 这个时候再来看补间动画原理
    - 待完善



#### 5.3 属性动画原理
- 属性动画原理官方介绍
    - [参看Android官方文档](https://developer.android.com/guide/topics/graphics/prop-animation.html)
- 属性动画常见类描述
    - 平时使用属性动画的重点就在于 AnimatorSet、ObjectAnimator、TimeAnimator、ValueAnimator。
    | java 类名 | xml 关键字 | 描述信息 |
    | :-------: | :------- | :----- |
    | ValueAnimator | `<animator>` 放置在 res/animator/ 目录下 | 在一个特定的时间里执行一个动画 | 
    | TimeAnimator | 不支持/点我查看原因 | 时序监听回调工具 | 
    | ObjectAnimator | `<objectAnimator>` 放置在 res/animator/ 目录下 | 一个对象的一个属性动画 | 
    | AnimatorSet | `<set>` 放置在 res/animator/ 目录下 | 动画集合 | 
- 相比ValueAnimator类，ObjectAnimator还做了许多操作，ObjectAnimator与 ValueAnimator类的区别： 
    - ValueAnimator 类是先改变值，然后 手动赋值 给对象的属性从而实现动画；是 间接 对对象属性进行操作；
    - ObjectAnimator 类是先改变值，然后 自动赋值 给对象的属性从而实现动画；是 直接 对对象属性进行操作；



##### 5.3.1 ObjectAnimator
- ObjectAnimator是什么
    - 继承自 ValueAnimator，允许你指定要进行动画的对象以及该对象的一个属性。该类会根据计算得到的新值自动更新属性。
- ObjectAnimator如何使用
    - ObjectAnimator 类提供了 ofInt、ofFloat、ofObject 这个三个常用的方法，这些方法都是设置动画作用的元素、属性、开始、结束等任意属性值。 
    - 当属性值（上面方法的参数）只设置一个时就把通过getXXX反射获取的值作为起点，设置的值作为终点；如果设置两个（参数）， 那么一个是开始、另一个是结束。
- ObjectAnimator代码案例
    ``` java
    ObjectAnimator
        .ofFloat(imageView,"translationX",300, 200, 100)
        .setInterpolator(LinearInterpolator)
        .setDuration(500)
        .start();
    ```
- ObjectAnimator的注意事项
    - ObjectAnimator 的动画原理是不停的调用 setXXX 方法更新属性值，所有使用 ObjectAnimator 更新属性时的前提是 Object 必须声明有getXXX和setXXX方法。 
    - 我们通常使用 ObjectAnimator 设置 View 已知的属性来生成动画，而一般 View 已知属性变化时都会主动触发重绘图操作， 所以动画会自动实现；
    - 但是也有特殊情况，譬如作用 Object 不是 View，或者作用的属性没有触发重绘，或者我们在重绘时需要做自己的操作，添加动画监听然后在onAnimationUpdate中做具体逻辑。
- 源码流程分析过程
    - -> ObjectAnimator#ofFloat，传入了一个target和propName，然后在设置Values
        - -> ObjectAnimator#setFloatValues，这里如果外部传递的values不为空，则会调用super方法
        - -> ValueAnimator#setFloatValues，构造一个PropertyValuesHolder对象，然后保存view在动画期间的属性和值，记住是动画期间的
        - -> PropertyValuesHolder#setFloatValues，存储了我们的mValueType ，此外还存了一个mIntKeyframeSet。
        - -> KeyframeSet#ofFloat，单纯的理解是，Keyframe的集合，而Keyframe叫做关键帧，为一个动画保存time/value（时间与值）对。
    - -> ObjectAnimator#setInterpolator，记录下插值器，我们这里也线性插值器，如果没有设置则默认是这个
    - -> ObjectAnimator#setDuration，就是简单在mDuration中记录了一下动画的持续时间，这个sDurationScale默认为1，用于调整观察动画，比如你可以调整为10，动画就会慢10倍的播放。
    - -> ObjectAnimator#start，先调用AnimationHandler单例对象关闭之前的动画，然后调用super中start方法
        - -> ValueAnimator#start，最终调用到了start(boolean playBackwards)方法
        - -> ValueAnimator#start(boolean playBackwards)，首先设置一些关于动画标记位，然后一些变量属性
        - -> ValueAnimator#addAnimationCallback，生成一个AnimationHandler对象，getOrCreateAnimationHandler就是在当前线程变量ThreadLocal中取出来，没有的话，则创建一个，然后set进去。
        - -> ValueAnimator#setCurrentFraction，这个里面初始化动画配置，设置当前Fraction，然后将计算的Fraction得到的



##### 5.3.2 ValueAnimator
- ValueAnimator是什么
    - 属性动画中的时间驱动，管理着动画时间的开始、结束属性值，相应时间属性值计算方法等。包含所有计算动画值的核心函数以及每一个动画时间节点上的信息、一个动画是否重复、是否监听更新事件等，并且还可以设置自定义的计算类型。
- ValueAnimator注意点
    - ValueAnimator 只是动画计算管理驱动，设置了作用目标，但没有设置属性，需要通过 updateListener 里设置属性才会生效。
- ValueAnimator代码案例
    ``` java
    ValueAnimator animator = ValueAnimator.ofFloat(0, mContentHeight);  //定义动画
    animator.setTarget(view);   //设置作用目标
    animator.setDuration(5000).start();
    animator.addUpdateListener(new AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation){
            float value = (float) animation.getAnimatedValue();
            view.setXXX(value);  //必须通过这里设置属性值才有效
            view.mXXX = value;  //不需要setXXX属性方法
        }
    });
    ```
- 源码流程分析过程




##### 5.3.3 ViewPropertyAnimator
- 为何会有ViewPropertyAnimator？
    - 之前我们要设置一个View控件旋转 360 的代码是这样：ObjectAnimator.ofFloat(btn,"rotation",360).setDuration(200).start();
    - 而现在我们使用 ViewPropertyAnimator 后是这样：btn.animate().rotation(360).setDuration(200);
- 如何使用ViewPropertyAnimator
    - 用 View#animate() 方法来获取一个 ViewPropertyAnimator 的对象实例，支持链式操作。
    - 可以看见通过 View 的 animate() 方法可以得到一个 ViewPropertyAnimator 的属性动画（有人说他没有继承 Animator 类，是的，他是成员关系，不是之前那种继承关系）。
- ViewPropertyAnimator复杂属性动画使用简洁性
    ``` java
    AnimatorSet set = new AnimatorSet();
    set.playTogether( ObjectAnimator.ofFloat(btn,"alpha",0.5f),
            ObjectAnimator.ofFloat(btn,"rotation",360),
            ObjectAnimator.ofFloat(btn,"scaleX",1.5f),
            ObjectAnimator.ofFloat(btn,"scaleY",1.5f),
            ObjectAnimator.ofFloat(btn,"translationX",0,50),
            ObjectAnimator.ofFloat(btn,"translationY",0,50)
    );
    set.setDuration(5000).start();
    
    //使用 ViewPropertyAnimator 设置代码如下：
    btn.animate().alpha(0.5f).rotation(360).scaleX(1.5f).scaleY(1.5f)
                 .translationX(50).translationY(50).setDuration(5000);
    ```





#### 5.4 过渡动画原理
##### 5.4.1 overridePendingTransition原理



##### 5.4.2 windowAnimationStyle原理



##### 5.4.3 Transition库原理













# 参考博客
- https://github.com/OCNYang/Android-Animation-Set
- Material Components—预备役选手Transition
    - https://mp.weixin.qq.com/s/I7rUg8z539P8OaQEfpmQgA
- 带你走一波Transition Animator转场动画相关事项
    - https://juejin.cn/post/6844904032033603591
- Android属性动画,看完这篇够用了吧
    - https://juejin.cn/post/6846687601118691341#heading-3
- “为什么属性动画移动后仍可点击”，你怎么答？
    - https://mp.weixin.qq.com/s/hiWxzJkDjvDoClOUuJI7CA




