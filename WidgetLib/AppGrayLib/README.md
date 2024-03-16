# App置灰方案实践库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.原理深度分析
- 05.其他问题说明



### 01.基础概念说明
#### 1.1 项目背景说明
- 当在特殊的某一个日子
    - 我们会表达我们的悼念，缅怀、纪念之情，APP会在某一日设置成黑灰色。比如清明节这天很多App都设置了符合主题的灰色模式。


#### 1.2 设计目标
- App置灰目标
    - 可以设置全局置灰，也可以设置单独的页面置灰，最好是简单化调用封装的API更好。


#### 1.3 实现灰色核心代码
- 实现灰色核心代码如下所示：
    ```
    Paint paint = new Paint();
    ColorMatrix cm = new ColorMatrix();
    cm.setSaturation(0);
    mPaint.setColorFilter(new ColorMatrixColorFilter(cm));
    view.setLayerType(View.LAYER_TYPE_HARDWARE, paint);
    ```


### 02.常见思路和做法
#### 2.1 设置灰色核心
- Canvas和Paint
    - App 页面上的 View 都是通过 Canvas + Paint 画出来的。Canvas 对应画布，Paint 对应画笔，两者结合，就能画出 View。
- 实现思路分析说明
    - 实现灰度化的思路应该从 Android 系统界面绘制原理出发寻找实现方案。系统是通过 Paint 将内容绘制到界面上的，Paint 中可以设置 ColorMatrix ，界面置灰可以通过通过使用颜色矩阵（ColorMatrix）来实现。
- 创建灰色模式的核心代码
    - setSaturation方法用于设置矩阵以影响颜色的饱和度，即0为灰色，1为彩色。
- Activity页面置灰思路
    - Activity页面肯定包含很多不同的View。然后改变View核心是操作Paint，那么如何要设置Activity都是灰色，则可以设置根布局DecorView。


#### 2.2 实现方案探讨
- 实现方案探讨有哪些？
    - 第一种：给Activity的顶层View设置置灰，实现全局置灰效果。这样就可以实现啦，这种方式还是比较简单的。
    - 第二种：该方法使用自定义layout，在dispatchDraw方法的时候，添加一层黑白色的bitmap，让界面开起来成为黑白模式。
    - 第三种：除了给Activity外，有些特殊控件需要置灰，比如webView、H5页面、视频等。使用View设置灰色！
    - 第四种：设置两套不同的资源文件，根据全局置灰开关，选择加载不同的图片和色值。
- 这些方式优缺点分析
    - 第一种：DecorView 是 Activity 窗口的根视图，根视图通过 ColorMatrix 设置置灰应该可以在全部子元素有效。需要用到BaseActivity中。
    - 第二种：其本质还是属于自定义View，实现灰色本质都是使用 Paint 进行的绘制，使用起来稍微麻烦一些。这种入侵性比较大！
    - 第三种：不仅支持Activity，还可以支持其他各种View设置灰色，相当于第一种方案的加强版本。
    - 第四种：实现起来工作繁琐、工作量大。且会导致 App 包增大很多，用户体验差。


#### 2.3 页面灰色设置思路
- 如何实现全局App设置灰色
    - 第一种：注册 registerActivityLifecycleCallbacks 回调，回调中通过 activity 实例同样可以拿到 DecorView 然后设置灰色。
    - 第二种：在 BaseActivity 里面处理，这样所有继承 BaseActivity 的都会生效。
    - 第三种：利用hook监听到每个DecorView的创建，并且拿到View本身。然后再对布局进行了重绘。
- 思路对比分析
    - 第一种：监听所有activity，比较方便，入侵性小。
    - 第二种：放到base类中不太友好，因为目前项目中base父类有好几个。
    - 第三种：完全解耦合，一行代码即可设置全局灰色，但使用了反射有点性能损耗。



#### 2.4 如何控制设置开关
- 设置灰色的开关该怎么做呢？
    - 第一种方式：把网络请求的接口放在了欢迎页中进行
    - 第二种方式：配置AB测试开关
- 这些方式优缺点分析
    - 第一种分析：只获取一个值，耗时并不会特别严重，并不影响用户的体验，受到网络环境影响。
    - 第二种分析：相比于接库，配置起来简单，也方便开发，产品配置开关。


### 03.Api调用说明
- Api调用如下所示
    ``` java
    AppGrayHelper.getInstance().setType(1).setGray(true).initGrayApp(this,true);
    ```
- 如何实现App全局灰色
    ```
    //使用注册ActivityLifecycleCallbacks监听，设置所有activity布局灰色
    AppGrayHelper.getInstance().setGray(true).initGrayApp(this,true)
    //使用hook设置全局灰色
    AppGrayHelper.getInstance().setGray(true).setGray3()
    ```
- 如何实现单独页面灰色
    ```
    AppGrayHelper.getInstance().setGray1(window)
    AppGrayHelper.getInstance().setGray2(window.decorView)
    ```
- 如何实现Dialog和PopupWindow灰色
    ```
    AppGrayHelper.getInstance().setGray(true).setGray2(view)
    ```



### 04.原理深度分析
#### 4.1 颜色矩阵说明
- 颜色矩阵是什么东西？
    - 在 Android 中，系统使用一个颜色矩阵 ColorMatrix，来处理图像的色彩效果。通过这个类，可以很方便地改变矩阵值来处理颜色效果。
- ColorMatrix 提供了以下关键方法进行对颜色的处理：
    - setRotate(int axis, float degree) ：设置颜色的色调，第一个参数，系统分别使用0、1、2 来代表 Red、Green、Blue 三种颜色的处理；而第二个参数，就是需要处理的值。
    - setSaturation(float sat)：设置颜色的饱和度，参数代表设置颜色饱和度的值，当饱和度为0时，图像就变成灰度图像。
    - setScale(float rScale, float gScale, float bScale , float aScale)：对于亮度进行处理。
- setSaturation 方法
    - 设置颜色矩阵的饱和度，0 是灰色的，1 是原图。通过调用 setSaturation (0) 方法即可将图像置为灰色 。


#### 4.2 Window全局灰色原理
- App 中 Window 的添加最终都会走到 WindowManagerGlobal 的 addView 方法：
    - WindowManagerGlobal 是一个全局单例，其中 mViews 是一个集合，App 中所有的 Window 在添加的时候都会被它给存起来。
    ``` java
    //WindowManagerGlobal#addView
    public void addView(View view, ViewGroup.LayoutParams params,
            Display display, Window parentWindow) {
        synchronized (mLock) {
            // 将 view 添加到 mViews，mViews 是一个 ArrayList 集合
            mViews.add(view);
            // 最后通过 viewRootImpl 来添加 window
            try {
                root.setView(view, wparams, panelParentView);
            } 
        }  
    }
    ```
- 如何设置Dialog置灰
    - 可以通过 Hook 拿到 mViews 中所有的 View 然后进行黑白化设置，这样不管是 Activity，Dialog，PopupWindow 还是其他一些 Window 组件，都会变成黑白化。
- Hook具体该怎么实践
    - 1、确认 Hook 点（被劫持的原始对象我们称之为 Hook 点）；2、定义代理类；3、使用代理对象替换 Hook 点
- WindowManagerGlobal 中 mViews 实现 App 黑白化
    - 1、Hook WindowManagerGlobal 中的 mViews ，将其改成可感知数据的 ArrayList 集合
    - 2、监听 mViews 的 add 操作，然后对 View 进行黑白化设置




### 05.其他问题说明
#### 5.1 顶层View黑化思考
- 找到当前View树一个合适的父View
    - 对父类View进行黑白化设置或者替换为自定义黑白化View，因为父 View 的 Canvas 和 Paint 是往下分发的，所以它所包含的子 View 都会黑白化处理。
- 那么如何理解View向下分发呢
    - 待完善


#### 5.2 Dialog置灰思考
- 设置Activity灰色后弹窗没有灰
    - Dialog 创建了新的 PhoneWindow，使用了 PhoneWindow 的 DecorView 模板。Dialog的DecorView和Activity的不是同一个！
- 设置Activity灰色后Pop没有灰
    - 


#### 5.3 SurfaceView无法置灰
- SurfaceView为何无法置灰
    - 使用了setLayerType进行重绘，而SurfaceView是有独立的Window，脱离布局内的Window，运行在其他线程，不影响主线程的绘制，所以当前方案无法使SurfaceView变色。
- 解决方案：
    - 1、使用TextureView。2、看下这个SurfaceView是否可以设置滤镜，正常都是一些三方或者自制的播放器。


- https://juejin.cn/post/7167300200921301028