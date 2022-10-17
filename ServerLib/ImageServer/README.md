# 图片加载库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析



### 01.基础概念介绍
#### 1.1 图片框架加载流程
- 概括来说，图片加载包含封装，解析，下载，解码，变换，缓存，显示等操作。


#### 1.2 图片框架设计
- 封装参数：从指定来源，到输出结果，中间可能经历很多流程，所以第一件事就是封装参数，这些参数会贯穿整个过程；
- 解析路径：图片的来源有多种，格式也不尽相同，需要规范化；比如glide可以加载file，io，id，网络等各种图片资源
- 读取缓存：为了减少计算，通常都会做缓存；同样的请求，从缓存中取图片（Bitmap）即可；
- 查找文件/下载文件：如果是本地的文件，直接解码即可；如果是网络图片，需要先下载；比如glide这块是发起一个请求
- 解码：这一步是整个过程中最复杂的步骤之一，有不少细节；比如glide中解析图片数据源，旋转方向，图片头等信息
- 变换和压缩：解码出Bitmap之后，可能还需要做一些变换处理（圆角，滤镜等），还要做图片压缩；
- 缓存：得到最终bitmap之后，可以缓存起来，以便下次请求时直接取结果；比如glide用到三级缓存
- 显示：显示结果，可能需要做些动画（淡入动画，crossFade等）。



#### 1.6 Glide加载圆角原理
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
#### 2.1 BitmapShader
- 使用BitmapShader来渲染图像的基本步骤
    - 第一步：创建BitmapShader类的对象；
    - 第二步：通过Paint的setShader方法来设置渲染对象；
    - 第三步：在绘制图像时使用已经设置了setShader()方法的画笔。



### 03.Api调用说明




### 04.遇到的坑分析














