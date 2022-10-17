# 高清大图加载库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.该库性能分析


### 01.基础概念介绍
#### 1.1 如何加载高清大图
- 遇到的问题描述
    - 通过压缩图片可以取得很好的效果，但有时候效果就不那么美好了，例如长图像清明上河图，像这类的长图，如果我们直接压缩展示的话，这张图完全看不清，很影响体验。
- 如何解决加载大图
    - 可以采用局部展示，然后滑动查看的方式去展示图片。利用BitmapRegionDecoder来局部展示图片的，展示的是一块矩形区域。


#### 1.2 BitmapRegionDecoder
- BitmapRegionDecoder 提供了一系列newInstance，最终都是通过数据流来得到对象。很好理解，加载图片肯定要有图片文件才行。
- 适用场景 : 当一张图片非常大 , 在手机中只需要显示其中一部分内容 , BitmapRegionDecoder 非常有用 。
- 主要作用 : BitmapRegionDecoder 可以从图像中 解码一个矩形区域 。相当于手在滑动的过程中，计算当前显示区域的图片绘制出来。
- 基本使用流程 : 先创建，后解码 。调用 newInstance 方法 , 创建 BitmapRegionDecoder 对象 ；然后调用 decodeRegion 方法 , 获取指定 Rect 矩形区域的解码后的 Bitmap 对象


#### 1.3 图片分片加载


#### 1.4 


### 02.常见思路和做法




### 03.Api调用说明



### 04.遇到的坑分析



### 05.该库性能分析
#### 5.1 局部加载原理
- BitmapRegionDecoder



#### 5.5 参考博客
- Android图片加载优化方案
    - https://juejin.cn/post/7096017926028787726
- BitmapRegionDecoder
    - https://developer.aliyun.com/article/861565
- 博客
    - https://blog.csdn.net/weixin_42169971/article/details/117309337











