# 圆角控件库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析



### 01.基础概念介绍
- 业务背景介绍
    - 在显示图片是有时候需要显示圆角图片，我们应该都知道圆角显示肯定是更加耗费内存和性能，会导致图片的过度绘制等问题。
- 给控件设置圆角，代码层面一般怎么做？
    - 第一种：比如给TextView设置Shape圆角，大概的原理：
    - 第二种：自定义控件实现，大概原理：
    - 第三种：使用Glide加载图片设置圆角，大概原理：


### 02.常见思路和做法
- 目前设置控件圆角有哪些方式
    - 第一种：比如给TextView设置Shape圆角，非常常见的使用
    - 第二种：使用背景图片
    - 第三种：自定义控件实现
    - 第四种：使用ViewOutlineProvider裁剪View
    - 第五种：使用CardView
    - 第六种：使用Glide加载图片设置圆角
- 各种设置圆角的优缺点对比
    - 第一种：shape常见，简单直观。缺点是项目中xml，越写越多
    - 第二种：使用切图没什么说的，使用起来不方便
    - 第三种：自定义控件，弥补shape上不足，采用attr属性设置圆角，那样圆角样式多，使用起来方便
    - 第四种：用于实现view阴影和轮廓
    - 第五种：使用CardView，官方支持阴影和圆角控件
    - 第六种：使用Glide加载圆角，一般用于图片设置，比较方便




### 03.Api调用说明
- 具有特点
    - LinearLayout、RelativeLayout、FrameLayout、ConstraintLayout支持圆角
    - ImageView、TextView、View、Button支持圆角
    - CircleImageView（圆形图片）
    - 支持边框
    - 可正常设置ripple（波纹不会突破边框）
    - 使用 xml 或者 代码 进行配置，使用简单
- api调用如下所示，直接拿来用即可


### 04.遇到的坑分析












- 感谢和参考
    - https://github.com/H07000223/FlycoRoundView
    - https://github.com/KuangGang/RoundCorners











