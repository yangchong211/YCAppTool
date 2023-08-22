# 基础组件库
#### 目录介绍
- 01.该库介绍
- 02.万能伸展折叠控件
- 03.自定义小红点
- 04.多种阴影效果实践
- 05.自定义圆角控件
- 06.超级圆角控件库


### 01.该库介绍



### 02.万能伸展折叠控件
- 伸展折叠控件介绍
    - 自定义折叠和展开布局，在不用改变原控件的基础上，就可以实现折叠展开功能，入侵性极低。
    - 主要的思路是，设置一个折叠时的布局高度，设置一个内容展开时的高度，然后利用属性动画去动态改变布局的高度。
    - 可以设置折叠和展开的监听事件，方便开发者拓展其他需求。可以设置动画的时间。
    - 可以支持支持常见的文本折叠，流失布局标签折叠，或者RecyclerView折叠等功能。十分方便，思路也比较容易理解，代码不超过300行……
- **如何引用**
    ```
    implementation 'com.github.yangchong211.YCWidgetLib:ExpandLib:1.0.5'
    ```
- 具体使用可看：
    - [万能伸展折叠控件](https://github.com/yangchong211/YCWidgetLib/tree/master/ExpandLib)



### 03.自定义小红点
- 自定义小红点介绍
    - 自定义红点控件，不用修改之前的代码，完全解耦，既可以设置红点数字控件，使用十分方便。
    - 网上看到有些案例是继承View，然后去测量和绘制红点的相关操作，此案例则不需要这样，继承TextView也可以完成设置红点功能。
    - 可以支持设置在TextView，Button，LinearLayout，RelativeLayout，TabLayout等等控件上……
    - 大概的原理是：继承TextView，然后设置LayoutParams，设置内容，设置Background等等属性，然后在通过addView添加到父控件中。
- **如何引用**
    ```
    implementation 'com.github.yangchong211.YCWidgetLib:RedDotView:1.0.5'
    ```
- 具体使用可看：
    - [自定义小红点](https://github.com/yangchong211/YCWidgetLib/tree/master/RedDotView)



### 04.多种阴影效果实践
- 阴影效果有哪些实现方式
    - 第一种：使用CardView，但是不能设置阴影颜色
    - 第二种：采用shape叠加，存在后期UI效果不便优化
    - 第三种：UI切图
    - 第四种：自定义View
- 方案对比
    - 第一个方案的CardView渐变色和阴影效果很难控制，只能支持线性或者环装形式渐变，这种不满足需要，因为阴影本身是一个四周一层很淡的颜色包围，在一个矩形框的层面上颜色大概一致，而且这个CardView有很多局限性，比如不能修改阴影的颜色，不能修改阴影的深浅。所以这个思路无法实现这个需求。
    - 第二个采用shape叠加，可以实现阴影效果，但是影响UI，且阴影部分是占像素的，而且不灵活。
    - 第三个方案询问了一下ui。他们给出的结果是如果使用切图的话那标注的话很难标，身为一个优秀的设计师大多对像素点都和敏感，界面上的像素点有一点不协调那都是无法容忍的。
    - 第四种方案采用api实现阴影效果，支持多种自定义效果，方便调用，目前来说应该比较友好
- **如何引用**
    ```
    implementation 'com.github.yangchong211.YCWidgetLib:ShadowConfig:1.0.5'
    implementation 'com.github.yangchong211.YCWidgetLib:CardViewLib:1.0.5'
    ```
- 具体使用可看：
    - [多种阴影效果实践](https://github.com/yangchong211/YCWidgetLib/tree/master/CardViewLib)



### 05.自定义圆角控件
- 自定义小红点介绍
    - LinearLayout、RelativeLayout、FrameLayout、ConstraintLayout支持圆角。支持边框，可使用Color的Selector
- 目前设置控件圆角有哪些方式
    - 第一种：比如给TextView设置Shape圆角，非常常见的使用。shape常见，简单直观。缺点是项目中xml，越写越多
    - 第二种：使用背景图片。使用切图没什么说的，使用起来不方便
    - 第三种：自定义控件实现。自定义控件，弥补shape上不足，采用attr属性设置圆角，那样圆角样式多，使用起来方便
    - 第四种：使用ViewOutlineProvider裁剪View
    - 第五种：使用CardView，官方支持阴影和圆角控件，需要嵌套一层，有点不方便
    - 第六种：使用Glide加载图片设置圆角，一般用于图片设置，比较方便
- 具有特点
    - LinearLayout、RelativeLayout、FrameLayout、ConstraintLayout支持圆角
    - ImageView、TextView、View、Button支持圆角
    - CircleImageView（圆形图片）
    - 支持边框
    - 可正常设置ripple（波纹不会突破边框）
    - 使用 xml 或者 代码 进行配置，使用简单
- api调用如下所示，直接拿来用即可
    - 针对ViewGroup类型：RoundLinearLayout；RoundFrameLayout；RoundConstraintLayout；RoundLinearLayout
    - 针对View类型：RoundView；RoundTextView；RoundImageView；RoundButton；CircleImageView
- **如何引用**
    ```
    implementation 'com.github.yangchong211.YCWidgetLib:RoundCorners:1.0.5'
    ```
- 具体使用可看：
    - [自定义圆角控件](https://github.com/yangchong211/YCWidgetLib/tree/master/RoundCorners)



























