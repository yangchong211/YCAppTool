# 生成二维码工具库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明


### 01.基础概念介绍




### 02.常见思路和做法
#### 2.1 扫码二维码思路
- 如何通过zxing实现二维码扫描功能呢
    - 在android/camera目录下看到，关于camera的封装采用的是camera1的api，拿到每一帧的预览数据后发送到解码线程去做识别。
    - 识别二维码核心代码为DecodeHandler#decode方法：
    ```
    /**
    * Decode the data within the viewfinder rectangle, and time how long it took. For efficiency,
    * reuse the same reader objects from one decode to the next.
    *
    * @param data   The YUV preview frame.
    * @param width  The width of the preview frame.
    * @param height The height of the preview frame.
    */
    private void decode(byte[] data, int width, int height) {
      // ...省略部分代码
      PlanarYUVLuminanceSource source = activity.getCameraManager().buildLuminanceSource(data, width, height);
      if (source != null) {
          BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
          try {
              rawResult = multiFormatReader.decodeWithState(bitmap);
          } catch (ReaderException re) {
              // continue
          } finally {
              multiFormatReader.reset();
          }
       }
      // ...省略部分代码
    }
    ```
- 识别过程主要分为四个步骤：
    - 第一步，构建Source，将数据源转为灰度图；
        ```
        // 通过YUV进行二维码识别使用PlanarYUVLuminanceSource，
        // 可以传一个Rect进行裁剪，对裁剪区进行识别以提高速度
        PlanarYUVLuminanceSource(yuvData, width, height, top, left, width, height, false)
        
        // 通过RGB进行二维码识别的话使用RGBLuminanceSource，
        // 这个类在构造方法中将RGB转为灰度图
        luminances[offset] = (byte) ((r + g2 + b) / 4);
        ```
    - 第二步，选择识别算法；
        - GlobalHistogramBinarizer：适合于低端的设备，对手机的CPU和内存要求不高。它选择了全部的黑点来计算，因此无法处理阴影和渐变这两种情况；
        - HybridBinarizer：在执行效率上要慢于GlobalHistogramBinarizer算法，但识别相对更有效。它专门为以白色为背景的连续黑色块二维码图像解析而设计，也更适合用来解析具有严重阴影和渐变的二维码图像。
    - 第三步，将二维码矩阵转为位图；
    - 第四步，识别；
- 解码关键，可以看下面这个博客
    - https://blog.csdn.net/weixin_45883443/article/details/120710482



#### 2.3 给图片添加水印
- 如何给二维码添加中心logo
    - 简单的叠加绘制流程，创建一个空白位图，先画源图像，再在中心地方画水印logo图像即可。



### 03.Api调用说明



### 04.遇到的坑分析



### 05.其他问题说明


### 参考博客
- 条码简介与zxing扫码优化
  - https://juejin.cn/post/7082011589938774052





