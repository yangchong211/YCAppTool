# 图片压缩库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.该库性能分析


### 01.基础概念介绍
#### 1.1 图片基础知识
- 1、位深和色深有什么区别，他们是一个东西吗？
- 2、为什么Bitmap不能直接保存，Bitmap和PNG、JPG到底是什么关系？
- 3、图片占用的内存大小公式：图片分辨率 * 每个像素点大小，这种说法正确吗？
- 4、为什么有时候同一个 app，app >内的同个界面上的同张图片，但在不同设备上所耗内存却不一样？
- 5、同一张图片，在界面上显示的控件大小不同时，它的内存大小也会跟随着改变吗？



#### 1.2 图片基础概念
- ARGB介绍
    - ARGB颜色模型：最常见的颜色模型，设备相关，四种通道，取值均为[0，255]，即转化成二进制位0000 0000 ~ 1111 1111。
    - A：Alpha (透明度) R：Red (红) G：Green (绿) B：Blue (蓝)
- Bitmap概念
    - Bitmap对象本质是一张图片的内容在手机内存中的表达形式。它将图片的内容看做是由存储数据的有限个像素点组成；每个像素点存储该像素点位置的ARGB值。每个像素点的ARGB值确定下来，这张图片的内容就相应地确定下来了。
- Bitmap色彩模式
    - Bitmap.Config是Bitmap的一个枚举内部类，它表示的就是每个像素点对ARGB通道值的存储方案。取值有以下四种：
    - ALPHA_8：每个像素占8位（1个字节），存储透明度信息，没有颜色信息。
    - RGB_565：没有透明度，R=5，G=6，B=5，，那么一个像素点占5+6+5=16位（2字节），能表示2^16种颜色。
    - ARGB_4444：由4个4位组成，即A=4，R=4，G=4，B=4，那么一个像素点占4+4+4+4=16位 （2字节），能表示2^16种颜色。
    - ARGB_8888：由4个8位组成，即A=8，R=8，G=8，B=8，那么一个像素点占8+8+8+8=32位（4字节），能表示2^24种颜色。




### 02.常见思路和做法
#### 2.1 图片压缩思路
- 一般情况下图片压缩的整体思路如下：
- 第一步进行采样率压缩；
- 第二步进行宽高的等比例压缩（微信对原图和缩略图限制了最大长宽或者最小长宽）；
- 第三步就是对图片的质量进行压缩（一般75或者70）；
- 第四部就是采用webP的格式。


#### 2.2 质量压缩
- 在Android中，对图片进行质量压缩，通常我们的实现方式如下所示：
    ``` java
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    //quality 为0～100，0表示最小体积，100表示最高质量，对应体积也是最大
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality , outputStream);
    ```
- 在上述代码中，我们选择的压缩格式是CompressFormat.JPEG，除此之外还有两个选择：
    - 其一，CompressFormat.PNG，PNG格式是无损的，它无法再进行质量压缩，quality这个参数就没有作用了，会被忽略，所以最后图片保存成的文件大小不会有变化；
    - 其二，CompressFormat.WEBP，这个格式是google推出的图片格式，它会比JPEG更加省空间，经过实测大概可以优化30%左右。
- Android质量压缩逻辑，函数compress经过一连串的java层调用之后，最后来到了一个native函数：
    - 具体看：Bitmap.cpp，最后调用了函数encoder->encodeStream(…)编码保存本地。该函数是调用skia引擎来对图片进行编码压缩。


#### 2.3 尺寸压缩
- 采样率压缩
    ``` java
    BitmapFactory.Options options = new BitmapFactory.Options();
    //或者 inDensity 搭配 inTargetDensity 使用，算法和 inSampleSize 一样
    options.inSampleSize = 2; //设置图片的缩放比例(宽和高) , google推荐用2的倍数：
    Bitmap bitmap = BitmapFactory.decodeFile("xxx.png");
    Bitmap compress = BitmapFactory.decodeFile("xxx.png", options);
    ```
- inSampleSize缩放比
    - 从字面上理解，它的含义是: “设置取样大小”。它的作用是：设置inSampleSize的值(int类型)后，假如设为4，则宽和高都为原来的1/4，宽高都减少了，自然内存也降低了。
    - 邻近采样的方式比较粗暴，直接选择其中的一个像素作为生成像素，另一个像素直接抛弃，这样就造成了图片变成了纯绿色，也就是红色像素被抛弃。


#### 2.4 双线性采样
- 双线性采样（Bilinear Resampling）在 Android 中的使用方式一般有两种：
    ``` java
    Bitmap bitmap = BitmapFactory.decodeFile("xxx.png");
    Bitmap compress = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, true);
    
    //或者直接使用 matrix 进行缩放
    Bitmap bitmap = BitmapFactory.decodeFile("xxx.png");
    Matrix matrix = new Matrix();
    matrix.setScale(0.5f, 0.5f);
    bm = Bitmap.createBitmap(bitmap, 0, 0, bit.getWidth(), bit.getHeight(), matrix, true);
    ```
- 看源码可以知道createScaledBitmap函数最终也是使用第二种方式的matrix进行缩放
    - 双线性采样使用的是双线性內插值算法，这个算法不像邻近点插值算法一样，直接粗暴的选择一个像素，而是参考了源像素相应位置周围2x2个点的值，根据相对位置取对应的权重，经过计算之后得到目标图像。



### 03.Api调用说明



### 04.遇到的坑分析



### 05.该库性能分析














