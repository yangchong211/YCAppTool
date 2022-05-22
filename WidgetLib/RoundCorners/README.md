# 目录介绍
- 01.该库介绍
- 02.效果展示
- 03.如何使用


### 01.该库介绍


1.使用Path的addRoundRect方法，将需要剪切的圆角半径进行设置。
2.所有View和ViewGroup的绘制都需要经过draw方法，在draw结束之后使用第一步的Path和PorterDuffXfermode来设置颜色混合模式，将圆形外部进行背景颜色混合。
3.注意在draw中减少创建对象次数。


- 感谢和参考
    - https://github.com/H07000223/FlycoRoundView
    - https://github.com/KuangGang/RoundCorners











