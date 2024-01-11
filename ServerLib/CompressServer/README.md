# 鲁班压缩库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.该库性能分析



### 01.基础概念介绍
#### 1.1 让人头疼的OOM
- 造成OOM的原因之一就是图片，随便一张图片都是好几M，这样的照片加载到app，手机内存容易不够用，自然而然就造成了OOM，所以Android的图片压缩异常重要。
- 单纯对图片进行裁切，但是裁切成多少，压缩成多少却很难控制好，裁切过头图片太小，质量压缩过头则显示效果太差。



#### 1.2 图片基础知识
- 1、位深和色深有什么区别，他们是一个东西吗？
- 2、为什么Bitmap不能直接保存，Bitmap和PNG、JPG到底是什么关系？
- 3、图片占用的内存大小公式：图片分辨率 * 每个像素点大小，这种说法正确吗？
- 4、为什么有时候同一个 app，app >内的同个界面上的同张图片，但在不同设备上所耗内存却不一样？
- 5、同一张图片，在界面上显示的控件大小不同时，它的内存大小也会跟随着改变吗？



#### 1.3 图片基础概念
- ARGB介绍
    - ARGB颜色模型：最常见的颜色模型，设备相关，四种通道，取值均为0，255，即转化成二进制位0000 0000 ~ 1111 1111。
    - A：Alpha (透明度) R：Red (红) G：Green (绿) B：Blue (蓝)
- Bitmap色彩模式
    - Bitmap.Config是Bitmap的一个枚举内部类，它表示的就是每个像素点对ARGB通道值的存储方案。取值有以下四种：
    - ALPHA_8：每个像素占8位（1个字节），存储透明度信息，没有颜色信息。
    - RGB_565：没有透明度，R=5，G=6，B=5，，那么一个像素点占5+6+5=16位（2字节），能表示2^16种颜色。
    - ARGB_4444：由4个4位组成，即A=4，R=4，G=4，B=4，那么一个像素点占4+4+4+4=16位 （2字节），能表示2^16种颜色。
    - ARGB_8888：由4个8位组成，即A=8，R=8，G=8，B=8，那么一个像素点占8+8+8+8=32位（4字节），能表示2^24种颜色。



#### 1.4 Bitmap能直接存储吗
- Bitmap基础概念
    - Bitmap对象本质是一张图片的内容在手机内存中的表达形式。它将图片的内容看做是由存储数据的有限个像素点组成；每个像素点存储该像素点位置的ARGB值。每个像素点的ARGB值确定下来，这张图片的内容就相应地确定下来了。
- Bitmap本质上不能直接存储
    - 为什么？bitmap是一个对象，如果要存储成本地可以查看的图片文件，则必须对bitmap进行编码，然后通过io流写到本地file文件上。



#### 1.5 常见图片压缩
- 主要的压缩方法有两种：其一是质量压缩，其二是下采样压缩。前者是在不改变图片尺寸的情况下，改变图片的存储体积，而后者则是降低图像尺寸，达到相同目的。
- 质量压缩
    - Bitmap.compress()质量压缩，不会对内存产生影响。它是在保持像素的前提下改变图片的位深及透明度等，来达到压缩图片的目的，不会减少图片的像素。进过它压缩的图片文件大小会变小，但是解码成bitmap后占得内存是不变的。
    - 函数 compress 经过一连串的 java 层调用之后，最后来到了一个 native 函数，具体看Bitmap.cpp类中Bitmap_compress方法代码。最后调用了函数 encoder->encodeStream(…) 编码保存本地。该函数是调用 skia 引擎来对图片进行编码压缩。
- 采样率压缩
    - BitmapFactory.Options.inSampleSize内存压缩
    - 解码图片时，设置BitmapFactory.Options类的inJustDecodeBounds属性为true，可以在Bitmap不被加载到内存的前提下，获取Bitmap的原始宽高。而设置BitmapFactory.Options的inSampleSize属性可以真实的压缩Bitmap占用的内存，加载更小内存的Bitmap。
    - 设置inSampleSize之后，Bitmap的宽、高都会缩小inSampleSize倍。例如：一张宽高为2048x1536的图片，设置inSampleSize为4之后，实际加载到内存中的图片宽高是512x384。占有的内存就是0.75M而不是12M，足足节省了15倍。
    - 备注：inSampleSize值的大小不是随便设、或者越大越好，需要根据实际情况来设置。inSampleSize比1小的话会被当做1，任何inSampleSize的值会被取接近2的幂值。




### 02.常见思路和做法
#### 2.1 图片压缩思路
- 一般情况下图片压缩的整体思路如下：
- 第一步进行采样率压缩；
- 第二步进行宽高的等比例压缩（微信对原图和缩略图限制了最大长宽或者最小长宽）；
- 第三步就是对图片的质量进行压缩（一般75或者70）；
- 第四部就是采用webP的格式。



#### 2.2 质量压缩
- 在Android中，对图片进行质量压缩，通常我们的实现方式如下所示：
    ```
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
    ```
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
    ```
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



#### 2.5 鲁班压缩核心思路
- LuBan压缩目前的步骤只占了图片压缩中的第二与第三步，算法逻辑如下：
- 1.判断图片比例值，是否处于以下区间内；
    - [1, 0.5625) 即图片处于 [1:1 ~ 9:16) 比例范围内
    - [0.5625, 0.5) 即图片处于 [9:16 ~ 1:2) 比例范围内
    - [0.5, 0) 即图片处于 [1:2 ~ 1:∞) 比例范围内
    - 简单解释一下：获取图片的比例系数，如果在区间 [1, 0.5625) 中即图片处于 [1:1 ~ 9:16)比例范围内，比例以此类推，如果这个系数小于0.5，那么就给它放到 [1:2 ~ 1:∞)比例范围内。
- 2.判断图片最长边是否过边界值
    - [1, 0.5625) 边界值为：1664 * n（n=1）, 4990 * n（n=2）, 1280 * pow(2, n-1)（n≥3）
    - [0.5625, 0.5) 边界值为：1280 * pow(2, n-1)（n≥1）
    - [0.5, 0) 边界值为：1280 * pow(2, n-1)（n≥1）
    - 步骤二：上去一看一脸懵，1664是什么，n是什么，pow又是什么。。。这写的估计只有作者自己能看懂了。其实就是判断图片最长边是否过边界值，此边界值是模仿微信的一个经验值，就是说1664、4990都是经验值，模仿微信的策略。
    - 至于n，是返回的是options.inSampleSize的值，就是采样压缩的系数，是int型，Google建议是2的倍数，所以为了配合这个建议，代码中出现了小于10240返回的是4这种操作。最后说一下pow，其实是 （长边/1280）, 这个1280也是个经验值，逆向推出来的，解释到这里逻辑也清晰了。
- 3.计算压缩图片实际边长值，以第2步计算结果为准，超过某个边界值则：
    - width / pow(2, n-1)，
    - height/ pow(2, n-1)
    - 步骤三：这个感觉没什么用，还是计算压缩图片实际边长值，人家也说了，以第2步计算结果为准
- 4.计算压缩图片的实际文件大小，以第2、3步结果为准，图片比例越大则文件越大。
    - size = (newW * newH) / (width * height) * m；
    - [1, 0.5625) 则 width & height 对应 1664，4990，1280 * n（n≥3），m 对应 150，300，300；
    - [0.5625, 0.5) 则 width = 1440，height = 2560, m = 200；
    - [0.5, 0) 则 width = 1280，height = 1280 / scale，m = 500；注：scale为比例值
    - 步骤四：这个感觉也没什么用，这个m应该是压缩比。但整个过程就是验证一下压缩完之后，size的大小，是否超过了你的预期，如果超过了你的预期，将进行重复压缩。
- 5.判断第4步的size是否过小
    - [1, 0.5625) 则最小 size 对应 60，60，100
    - [0.5625, 0.5) 则最小 size 都为 100
    - [0.5, 0) 则最小 size 都为 100
    - 步骤五：这一步也没啥用，也是为了后面循环压缩使用。 这个size就是上面计算出来的，最小 size 对应的值公式为：size = (newW * newH) / (width * height) * m，对应的三个值，就是上面根据图片的比例分成的三组，然后计算出来的。
  


### 03.Api调用说明
- 异步调用，内部采用IO线程进行图片压缩，外部调用只需设置好结果监听。压缩一个图片的代码如下所示：
    ``` java
    Luban.with(this)
        .load(photos)//设置压缩图片文件路径，全路径
        .ignoreBy(100)//设置忽略压缩的大小上限
        .setTargetDir(getPath())//设置压缩输出文件目录
        .setCompressListener(new OnCompressListener() {
          @Override
          public void onStart() {
          }
    
          @Override
          public void onSuccess(File file) {
            
          }
    
          @Override
          public void onError(Throwable e) {
          }
        }).launch();
    ```
- 同步调用，请尽量避免在主线程调用以免阻塞主线程，同步压缩图片如下所示：
    ``` java
    //同步获取单个图片file文件
    File file = Luban.with(this).get(list.get(0));
    //同步获取多个图片file文件
    List<File> listFile = Luban.with(this).load(list).get();
    ```




### 04.遇到的坑分析
#### 4.1 遇到的问题
- 解码前没有对内存做出预判
- 质量压缩写死 60
- 没有提供图片输出格式选择
- 不支持多文件合理并行压缩，输出顺序和压缩顺序不能保证一致
- 检测文件格式和图像的角度多次重复创建InputStream，增加不必要开销，增加OOM风险
- 可能出现内存泄漏，需要自己合理处理生命周期
- 图片要是有大小限制，只能进行重复压缩


#### 4.2 技术改造方案
- 解码前利用获取的图片宽高对内存占用做出计算，超出内存的使用RGB-565尝试解码
- 针对质量压缩的时候，提供传入质量系数的接口
- 对图片输出支持多种格式，不局限于File
- 利用协程来实现异步压缩和并行压缩任务，可以在合适时机取消协程来终止任务
- 参考Glide对字节数组的复用，以及InputStream的mark()、reset()来优化重复打开开销
- 利用LiveData来实现监听，自动注销监听。
- 压缩前计算好大小，逆向推导出尺寸压缩系数和质量压缩系数




### 05.该库性能分析
- 鲁班压缩
    - https://blog.csdn.net/qq_27634797/article/details/79424507
- 图片压缩算法-luban
    - https://blog.csdn.net/u010218288/article/details/79392432









