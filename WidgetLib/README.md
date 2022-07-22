# 基础组件库
#### 目录介绍
- 03.超级圆角控件库



### 03.超级圆角控件库
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

















