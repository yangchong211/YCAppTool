#### 目录介绍
- 01.阴影实现方式
- 02.实现阴影Api
- 03.设置阴影注意点
- 04.阴影效果原理
- 05.Api使用说明文档
- 06.优化点分析




### 01.阴影实现方式
- 阴影效果有哪些实现方式
    - 第一种：使用CardView，但是不能设置阴影颜色
    - 第二种：采用shape叠加，存在后期UI效果不便优化
    - 第三种：UI切图
    - 第四种：自定义View
    - 第五种：自定义Drawable
- 否定上面前两种方案原因分析？
    - 第一个方案的CardView渐变色和阴影效果很难控制，只能支持线性或者环装形式渐变，这种不满足需要，因为阴影本身是一个四周一层很淡的颜色包围，在一个矩形框的层面上颜色大概一致，而且这个CardView有很多局限性，比如不能修改阴影的颜色，不能修改阴影的深浅。所以这个思路无法实现这个需求。
    - 第二个采用shape叠加，可以实现阴影效果，但是影响UI，且阴影部分是占像素的，而且不灵活。
    - 第三个方案询问了一下ui。他们给出的结果是如果使用切图的话那标注的话很难标，身为一个优秀的设计师大多对像素点都和敏感，界面上的像素点有一点不协调那都是无法容忍的。
- 网上一些介绍阴影效果方案
    - 所有在深奥的技术，也都是为需求做准备的。也就是需要实践并且可以用到实际开发中，这篇文章不再抽象介绍阴影效果原理，理解三维空间中如何处理偏移光线达到阴影视差等，网上看了一些文章也没看明白或者理解。这篇博客直接通过调用api实现预期的效果。
    - 多个drawable叠加，使用layer-list可以将多个drawable按照顺序层叠在一起显示，默认情况下，所有的item中的drawable都会自动根据它附上view的大小而进行缩放，layer-list中的item是按照顺序从下往上叠加的，即先定义的item在下面，后面的依次往上面叠放
- 阴影是否占位
    - 使用CardView阴影不占位，不能设置阴影颜色和效果
    - 使用shape阴影是可以设置阴影颜色，但是是占位的
- 几种方案优缺点对比分析
    - CardView	优点：自带功能实现简单 缺点：自带圆角不一定可适配所有需求
    - layer(shape叠加)	优点：实现形式简单 缺点：效果一般
    - 自定义实现	优点：实现效果好可配置能力高 缺点：需要开发者自行开发
    


### 02.实现阴影Api
#### 2.1 自定义View
- 思考一下如何实现View阴影效果？
    - 首先要明确阴影的实现思路是什么，其实就是颜色导致的视觉错觉。说白了就是在你的Card周围画一个渐变的体现立体感的颜色。基于上述思路，我们在一个在一个view上画一个矩形的图形，让他周围有渐变色的阴影即可。于是我们想起几个API：
    - 类：Paint 用于在Android上画图的类，相当于画笔
    - 类：Canvas 相当于画布，Android上的view的绘制都与他相关
    - 方法：paint.setShadowLayer可以给绘制的图形增加阴影，还可以设置阴影的颜色
- paint.setShadowLayer(float radius, float dx, float dy, int shadowColor);
    - 这个方法可以达到这样一个效果，在使用canvas画图时给视图顺带上一层阴影效果。
- 简单介绍一下这几个参数：
    - radius: 阴影半径，主要可以控制阴影的模糊效果以及阴影扩散出去的大小。
    - dx：阴影在X轴方向上的偏移量
    - dy: 阴影在Y轴方向上的偏移量
    - shadowColor： 阴影颜色。
- 终于找到了设置颜色的，通过设置shadowColor来控制视图的阴影颜色。


#### 2.2 自定义Drawable
- 注意的是：这种方式实现的阴影，其目标View需要关闭硬件加速。




### 03.设置阴影注意点
- 其中涉及到几个属性，阴影的宽度，view到ViewGroup的距离，如果视图和父布局一样大的话，那阴影就不好显示，如果要能够显示出来就必须设置clipChildren=false。
- 还有就是视图自带的圆角，大部分背景都是有圆角的，比如上图中的圆角，需要达到高度还原阴影的效果就是的阴影的圆角和背景保持一致。



### 04.阴影效果原理
#### 4.1 原生阴影实现方案
- 阴影原理
    - 阴影效果的实现采用的是Android原生的View的属性，拔高Z轴。Z轴会让View产生阴影的效果。
    - 可以理解为有一束斜光投向屏幕，Z 轴值越大，离光就越近，阴影的范围就越大；Z 轴值越小，离光就越远，阴影的范围就越小。
- 如何设置阴影？
    - 阴影的效果由Z轴（elevation）、光源（shadowColor）和环境阴影透明度（ambientShadowAlpha）三者综合决定的。实际产生的阴影是由这两个光源组合产生的。
- 除了设置上面的三个条件Z轴（elevation）、光源（shadowColor）和环境阴影透明度（ambientShadowAlpha），还有其他几个点需要注意的：
    - 一定要设置背景，而且是不透明的背景，因为阴影是通过投影来产生的，没有或透明的背景是无法产生的阴影的。
    - 设置了背景就一定会产生阴影？不一定，这里感觉有bug，有些背景图片无法产生阴影，也有人遇到：在background是图片时、直接设置具体颜色值时容易无效，比如：#ffaacc。background设置shape时效果最好；
    - 阴影是绘制在父控件的空间上的，所以在子控件和父控件的边界之间要留一定的空间来绘制阴影；


#### 4.2 setShadowLayer原理
- setShadowLayer()函数使用了高斯模糊算法，高斯模糊算法的定义很简单。
    - setShadowLayer函数只有对文字绘制阴影支持硬件加速，其他都不支持硬件加速，所以一般都会将其关闭。
- setShadowLayer不起作用
    - 这个方法不支持硬件加速，所以我们要测试时必须先关闭硬件加速。加上这一句 setLayerType(LAYER_TYPE_SOFTWARE, null); 




### 05.Api使用说明文档
- 十分简单，如下所示。ShadowLayout是一个FrameLayout布局的拓展！
    ``` java
    <com.yc.shadow.layout.ShadowLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:shadow_cornerRadius="18dp"
        app:shadow_dx="0dp"
        app:shadow_dy="0dp"
        app:shadow_shadowColor="#63F8C5B8"
        app:shadow_shadowLimit="8dp">
    
        <TextView
            android:background="@color/white"/>
    
    </com.yc.shadow.layout.ShadowLayout>
    ```


### 06.优化点分析
- 在createShadowBitmap方法中，其实也可以看到需要创建bitmap对象。大家都知道bitmap比较容易造成内存过大，如果是给recyclerView中的item设置阴影效果，那么如何避免重复创建，这时候可以用到缓存。所以可以在上面的基础上再优化一下代码。
- 先创建key，主要是用于map集合的键。这里为何用对象Key作为map的键呢，这里是借鉴了glide缓存图片的思路，可以创建Key对象的时候传入bitmap名称和宽高属性，并且需要重写hashCode和equals方法。
- 然后存取操作，在查找的时候，通过Key进行查找。注意：Bitmap需要同时满足三个条件（高度、宽度、名称）都相同时才能算是同一个 Bitmap。


