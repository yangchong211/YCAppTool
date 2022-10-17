# 图片工具库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明


### 01.基础概念介绍
#### 1.1 图片的常见格式
- 第一种常见格式：PNG
    - 无法进行质量压缩
- 第二种格式：JPG/JPEG
    - 最常见格式
- 第三种格式：WEBP
    - 相比png，webp格式的图片更节省空间，可以减少Apk的大小。
- 第四种格式：SVG
    - 如果担心太多的分辨率图片会导致Apk变大，那可以使用SVG矢量图，只需要一个图片，可以无损的放大与缩小。
- 第五种格式：iconfont
    - 可以在iconfont网站下载图片，如果是自己的设计我们同样可以上传到iconfont自己的项目中，然后再下载代码。
    - 将资源中iconfont.ttf是我们需要导入到项目中的。我们需要把iconfont.ttf导入到项目的assets目录下。
    - 需要注意的是在xml中直接使用unicode是可以的，但是在Java中直接使用是不行的。
      

#### 1.4 Bitmap像素是否可以更改
- 两种创建Bitmap的方式，一种是使用BitmapFactory，一种是使用Bitmap静态方法。
    - 假如我有一张图片，绿色的颜色非常丰富，但绿色很暗，这个时候我想要修改该图片每个像素中的绿色值，用这两种方式创建的Bitmap都能直接修改吗？不能……
    - 所有使用 BitmapFactory创建的Bitmap都是不可更改其像素值的，只有通过Bitmap以下几种函数创建的Bitmap才能完成如上需求。
- 只有这三个方法生成的Bitmap可以进行像素更改操作
    ``` java
    copy(Bitmap.Config config,boolean isMutable)
    createBitmap(int width,int height,Bitmap.Config config)
    createScaledBitmap(Bitmap src,int dstWidth,int dstHeight,boolean filter)
    ```



#### 1.5 图片一些注意事项
- 同样图片显示在大小不相同的ImageView上，内存是一样吗？
    - 图片占据内存空间大小与图片在界面上显示的大小没有关系。
- 图片放在res不同目录，加载的内存是一样的吗？
    - 最终图片加载进内存所占据的大小会不一样，因为系统在加载 res 目录下的资源图片时，会根据图片存放的不同目录做一次分辨率的转换，而转换的规则是：新图的高度 = 原图高度 * (设备的 dpi / 目录对应的 dpi )
     



### 02.常见思路和做法
#### 2.2 如何将jpg转png
- 将jpg转png的方案如下
    - 第一种：直接修改文件的后缀名
    - 第二种：将jpg图片加载成bitmap，然后调用bitmap中的compress
    - 第三种：利用一些三方软件工具转码修改图片



#### 2.3 如何将图片置灰
- 大概的操作步骤
    - 第一步：获取原始图片的宽高，然后创建一个bitmap可变位图对象。
    - 第二步：创建画板canvas对象，然后创建画笔paint。然后调用canvas.drawBitmap方法绘制图片
    - 第三步：对画笔进行修饰，设置画笔颜色属性，这里使用到了ColorMatrix，核心就是设置饱和度为0，即可绘制灰色内容




#### 2.4 判断图片旋转方向
- 在Android中使用ImageView显示图片的时候发现图片显示不正，方向偏了或者倒过来了。
    - 解决这个问题很自然想到的两步走，首先是要自动识别图像方向，计算旋转角度，然后对图像进行旋转并显示。
- 识别图像方向
    - 首先在这里提一个概念EXIF(Exchangeable Image File Format，可交换图像文件)。简而言之，Exif是一个标准，用于电子照相机（也包括手机、扫描器等）上，用来规范图片、声音、视屏以及它们的一些辅助标记格式。
    - Exif支持的格式如下：图像；压缩图像文件：JPEG、DCT；非压缩图像文件：TIFF；音频；RIFF、WAV
    - Android提供了对JPEG格式图像Exif接口支持，可以读取JPEG文件metadata信息，参见ExifInterface。这些Metadata信息总的来说大致分为三类：日期时间、空间信息（经纬度、高度）、Camera信息（孔径、焦距、旋转角、曝光量等等）。
- 关于图像旋转
    - 获取了图片的旋转方向后，然后再设置图像旋转。最后Bitmap提供的静态createBitmap方法，可以对图片设置旋转角度。


#### 2.5 保存图片且刷相册



### 03.Api调用说明
#### 3.1 如何依赖该库
- 依赖maven仓库如下所示
    ``` java
    
    ```


#### 3.2 如何使用Api
- 获取bitmap相关api
    ``` java
    //通过资源id获取bitmap位图
    Bitmap bitmap = AppBitmapUtils.getBitmap(this,R.drawable.bg_kites_min);
    //将图片设置成灰色
    Bitmap greyBitmap = AppBitmapUtils.greyBitmap(bitmap);
    //旋转Bitmap的角度，获取一张新的图片
    Bitmap rotatingImage = AppBitmapUtils.rotatingImage(bitmap, degree);
    ```
- 图片保存到本地文件api
    ``` kotlin
    val image = AppFileUtils.getExternalFilePath(this, "image")
    val fileName = image + File.separator + "yc12345678.png"
    val saveBitmapAsPng = BitmapUtils.saveBitmapAsPng(greyBitmap, fileName)
    
    //把图片加入到系统图库中
    ImageSaveUtils.insertImage(ImageActivity.this,file);
    ```
- 计算图片一些属性api
    ``` java
    //读取图片属性：旋转的角度
    int degree = PicCalculateUtils.readPictureDegree(this, path);
    ```
- 将view转化为bitmap相关api
    ``` java
    
    ```




### 04.遇到的坑分析
#### 4.1 刷新图片库弊端
- 调用系统提供的插入图库的方法：
    ``` java
    sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
    ```
    - 上面那条广播是扫描整个sd卡的广播，如果你sd卡里面东西很多会扫描很久，所以这样子用户体现很不好。
- 解决办法如下所示
    ``` java
    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/YC/image.jpg"))););
    ```





### 05.其他问题说明
#### 5.1 保存图片到相册思考
- 在本地保存了文件，然后插入图库。删除本地文件，图库中图片会消失吗？



### 参考和学习
- 抖音 Android 性能优化：
    - https://juejin.cn/post/7096059314233671694
- Android图片格式转换为JPG
    - https://blog.csdn.net/iblade/article/details/79153769






