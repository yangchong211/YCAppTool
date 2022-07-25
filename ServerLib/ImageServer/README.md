# 图片加载库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析



### 01.基础概念介绍


#### 1.3 Glide加载圆角原理
- 图片加载库比如Glide，Fresco等
    - 在底层，无非也是使用上面的这两种种方式。早期的使用setXfermode来实现，后来使用BitmapShader实现。使用简单，稳定。
- 比较核心的代码如下所示
    ``` java
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setShader(new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
    drawRoundRect(canvas, paint, width, height);
    ```
- BitmapShader原理介绍
    - BitmapShader总是从左上角开始绘制，利用Canvas绘制特定形状，可以获得特定形状的图形，类似圆形，椭圆，矩形等。



### 02.常见思路和做法
- 使用BitmapShader来渲染图像的基本步骤
    - 第一步：创建BitmapShader类的对象；第二步：通过Paint的setShader方法来设置渲染对象；第三步：在绘制图像时使用已经设置了setShader()方法的画笔。



### 03.Api调用说明




### 04.遇到的坑分析














