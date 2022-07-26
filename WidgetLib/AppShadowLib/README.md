#### 目录介绍
- 01.阴影效果有哪些实现方式
- 02.实现阴影效果Api
- 03.设置阴影需要注意哪些
- 04.常见Shape实现阴影效果
- 05.如何使用该阴影控件
- 06.优化点分析

https://www.jb51.net/article/252246.htm


### 01.阴影效果实现方式
- 阴影效果有哪些实现方式
    - 第一种：使用CardView，但是不能设置阴影颜色
    - 第二种：采用shape叠加，存在后期UI效果不便优化
    - 第三种：UI切图
    - 第四种：自定义View
- 否定上面前两种方案原因分析？
    - 第一个方案的CardView渐变色和阴影效果很难控制，只能支持线性或者环装形式渐变，这种不满足需要，因为阴影本身是一个四周一层很淡的颜色包围，在一个矩形框的层面上颜色大概一致，而且这个CardView有很多局限性，比如不能修改阴影的颜色，不能修改阴影的深浅。所以这个思路无法实现这个需求。
    - 第二个采用shape叠加，可以实现阴影效果，但是影响UI，且阴影部分是占像素的，而且不灵活。
    - 第三个方案询问了一下ui。他们给出的结果是如果使用切图的话那标注的话很难标，身为一个优秀的设计师大多对像素点都和敏感，界面上的像素点有一点不协调那都是无法容忍的。
    - 在下面开源案例代码中，我会一一展示这几种不同方案实现的阴影效果。
- 网上一些介绍阴影效果方案
    - 所有在深奥的技术，也都是为需求做准备的。也就是需要实践并且可以用到实际开发中，这篇文章不再抽象介绍阴影效果原理，理解三维空间中如何处理偏移光线达到阴影视差等，网上看了一些文章也没看明白或者理解。这篇博客直接通过调用api实现预期的效果。
- 阴影是否占位
    - 使用CardView阴影不占位，不能设置阴影颜色和效果
    - 使用shape阴影是可以设置阴影颜色，但是是占位的



### 02.实现阴影效果Api
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



### 03.设置阴影需要注意哪些
- 其中涉及到几个属性，阴影的宽度，view到Viewgroup的距离，如果视图和父布局一样大的话，那阴影就不好显示，如果要能够显示出来就必须设置clipChildren=false。
- 还有就是视图自带的圆角，大部分背景都是有圆角的，比如上图中的圆角，需要达到高度还原阴影的效果就是的阴影的圆角和背景保持一致。




### 04.常见Shape实现阴影效果
- 多个drawable叠加
    - 使用layer-list可以将多个drawable按照顺序层叠在一起显示，默认情况下，所有的item中的drawable都会自动根据它附上view的大小而进行缩放，layer-list中的item是按照顺序从下往上叠加的，即先定义的item在下面，后面的依次往上面叠放
- 阴影效果代码如下所示
    - 这里有多层，就省略了一些。然后直接通过设置控件的background属性即可实现。
    ```
    <?xml version="1.0" encoding="utf-8"?>
    <layer-list xmlns:android="http://schemas.android.com/apk/res/android">
        <item>
            <shape android:shape="rectangle">
                <solid android:color="@color/indexShadowColor_1" />
                <corners android:radius="5dip" />
                <padding
                    android:bottom="1dp"
                    android:left="1dp"
                    android:right="1dp"
                    android:top="1dp" />
            </shape>
        </item>
        <item>
            <shape android:shape="rectangle">
                <solid android:color="@color/indexShadowColor_2" />
                <corners android:radius="5dip" />
                <padding
                    android:bottom="1dp"
                    android:left="1dp"
                    android:right="1dp"
                    android:top="1dp" />
            </shape>
        </item>
        
        ……
    
        <item>
            <shape android:shape="rectangle">
                <corners android:radius="5dip" />
                <solid android:color="@color/indexColor" />
            </shape>
        </item>
    </layer-list>
    ```



### 05.如何使用该阴影控件
- 十分简单，如下所示




### 06.优化点分析
- 在createShadowBitmap方法中，其实也可以看到需要创建bitmap对象。大家都知道bitmap比较容易造成内存过大，如果是给recyclerView中的item设置阴影效果，那么如何避免重复创建，这时候可以用到缓存。所以可以在上面的基础上再优化一下代码。
- 先创建key，主要是用于map集合的键。这里为何用对象Key作为map的键呢，这里是借鉴了glide缓存图片的思路，可以创建Key对象的时候传入bitmap名称和宽高属性，并且需要重写hashCode和equals方法。
- 然后存取操作，在查找的时候，通过Key进行查找。注意：Bitmap需要同时满足三个条件（高度、宽度、名称）都相同时才能算是同一个 Bitmap。


