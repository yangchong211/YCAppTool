# YCFlutterUtils
#### 目录介绍
- 01.事件通知bus工具类
- 02.颜色Color工具类
- 03.日期转化工具类
- 04.File文件工具类
- 05.Sql数据库工具类
- 06.Json转化工具类
- 07.Log日志打印工具类
- 08.屏幕参数工具类
- 09.Sp轻量存储工具类
- 10.辅助计算工具类
- 11.加密和解密工具类
- 12.Num格式处理工具类
- 13.设备参数获取工具类
- 14.图片处理工具类
- 15.网络处理工具类
- 16.常用正则工具类
- 17.Object常用工具类
- 18.验证相关工具类
- 19.路由管理工具类
- 20.Bus事件工具类
- 21.Text文本工具类
- 22.i18拓展工具类
- 23.Time时间工具类
- 24.SPI帮助工具类
- 25.计时器辅助工具类
- 26.常见拓展工具类
- 27.内存缓存工具类
- 28.清除操作工具类
- 29.编解码相关工具类
- 30.反射相关工具类
- 31.资源相关工具类
- 32.Snackbar工具类
- 33.转化相关工具类
- 34.全局异常捕获工具
- 40.其他相关工具类


### 01.事件通知bus工具类
- 事件总线
    - 通常实现了订阅者模式，订阅者模式包含发布者和订阅者两种角色。
- 第一种方式：使用map集合存储key为事件eventName，value为EventCallback实现bus
    ``` dart
    //注册监听bus
    _subscription = EventBusService.instance.eventBus.on<EventMessage>().listen((event) {
          String name = event.eventName;
          //前后台切换发生了变化
          if (name == "eventBus1") {
            var busMessage = event.arguments["busMessage"];
            setState(() {
              message1 = busMessage;
            });
          }
        });
    
    //发送消息
    EventBusService.instance.eventBus.fire(EventMessage(
      "eventBus1",
      arguments: {"busMessage": "发送bus消息1"},
    ));
    
    //页面销毁清除bus
    if (_subscription != null) {
      _subscription.cancel();
      _subscription = null;
    }
    ```
- 第二种方式：使用StreamController实现bus事件通知
    ```
    //注册监听消息
    bus.on("eventBus2", (arg) {
      var busMessage = arg;
      setState(() {
        message2 = "接收消息：" + busMessage;
      });
    });
    
    //发送消息
    var arg = "发送bus消息1";
    bus.emit("eventBus2", arg);
    
    //移除消息
    bus.off("eventBus2", (arg) {
    
    });
    ```



### 02.颜色Color工具类
- Color hexToColor(String color, {Color defaultColor})
    - 将#A357D6颜色转化为16进制的Color
- Color toColor(String color , {Color defaultColor}) 
    - 将#FF6325颜色或者#50A357D6转化为16进制的Color
- String colorString(Color color)
    - 将color颜色转变为字符串
- bool isHexadecimal(String color)
    - 检查字符串是否为十六进制



### 03.日期转化工具类
- DateTime getNowDateTime()
    - 获取当前日期返回DateTime
- DateTime getYesterday()
    - 获取昨天日期返回DateTime
- DateTime getNowUtcDateTime()
    - 获取当前日期返回DateTime(utc)
- int getNowDateMs()                    
    - 获取当前毫秒值，返回int
- String getNowDateString()             
    - 获取现在日期字符串，默认是：yyyy-MM-dd HH:mm:ss，返回字符串
- String formatDate(DateTime dateTime, {String format}) 
    - 格式化时间，第一个字段是dateTime，第二个可选项表示格式
- String formatDateString(String dateStr, {bool isUtc, String format}) 
    - 格式化日期字符串，第一个字段例如：'2021-07-18 16:03:10'，第二个字段例如："yyyy/M/d HH:mm:ss"
- String formatDateMilliseconds(int ms, {bool isUtc = false, String format})
    - 格式化日期毫秒时间，第一个字段例如：1213423143312，第二个字段例如："yyyy/M/d HH:mm:ss"
- String getWeekday(DateTime dateTime,{String languageCode = 'en', bool short = false})
    - 获取dateTime是星期几，返回字符串，主要返回中文或者英文
- String getWeekdayByMilliseconds(int milliseconds,{bool isUtc = false, String languageCode = 'en', bool short = false}) 
    - 同上，获取毫秒值对应是星期几
- bool isToday(int milliseconds, {bool isUtc = false, int locMs})
    - 根据时间戳判断是否是今天
- bool isYesterday(DateTime dateTime, DateTime locDateTime)
    - 根据时间判断是否是昨天


### 04.File文件工具类
#### 4.1 文件存储工具类
- Future<String> getTempDir()
    - 获取一个临时目录(缓存)，系统可以随时清除
- Future<String> getAppDocDir() 
    - 获取应用程序的目录，用于存储只有它可以访问的文件。只有当应用程序被删除时，系统才会清除目录。
- Future<File> getAppFile(String fileName)
    - 初始化文件路径，默认选中应用程序的目录
- Future<String> readStringDir(String fileName)
    - 获取存在文件中的数据，默认读到应用程序的目录
- Future<File> writeJsonFileDir(Object obj,String fileName)
    - 写入json文件，默认写到应用程序的目录
- Future<File> writeStringDir(String string , String filePath)
    - 利用文件存储字符串，默认写到应用程序的目录
- Future<bool> clearFileDataDir(String fileName)
    - 清除缓存数据
- Future<bool> deleteFileDataDir(String fileName)
    - 删除缓存文件
- Future<File> writeJsonCustomFile(Object obj,String filePath)
    - 写入json文件，自定义路径
- Future<File> writeStringFile(String string , String filePath)
    - 利用文件存储字符串，自定义路径
- Future<String> readStringCustomFile(String filePath)
    - 获取自定义路径文件存中的数据


#### 4.2 文件管理工具类
- Directory createDir(String path)
    - 同步创建文件
- Future<Directory> createDirSync(String path)
    - 异步创建文件
- Future<String> getTempPath({String fileName, String dirName,}) 
    - 获取设备上临时目录的路径，该目录没有备份，适合存储下载文件的缓存。
- Future<String> getAppDocPath({String fileName, String dirName,}) 
    - 获取应用程序的目录，用于存储只有它可以访问的文件。只有当应用程序被删除时，系统才会清除目录。
- Future<String> getStoragePath({String fileName, String dirName,})
    - 应用程序可以访问顶层存储的目录的路径
- Future<Directory> createTempDir({String dirName})
    - 创建临时目录
- Future<Directory> createAppDocDir({String dirName}) 
    - 创建获取应用程序的目录
- Future<Directory> createStorageDir({String dirName}) 
   - 创建应用程序可以访问顶层存储的目录的路径



### 06.Json转化工具类
- printJson(Object object)
    - 单纯的Json格式输出打印
- printJsonEncode(Object object)
    - 单纯的Json格式输出打印
- String encodeObj(dynamic value)
    - 将对象[值]转换为JSON字符串
- T getObj<T>(String source, T f(Map v))
    - 转换JSON字符串到对象
- T getObject<T>(dynamic source, T f(Map v))
    - 转换JSON字符串或JSON映射[源]到对象
- List<T> getObjList<T>(String source, T f(Map v))
    - 转换JSON字符串列表[源]到对象列表
- List<T> getObjectList<T>(dynamic source, T f(Map v))
    - 转换JSON字符串或JSON映射列表[源]到对象列表


### 07.Log日志打印工具类
- LogUtils.init(tag : "yc",isDebug: true , maxLen: 128);
    - 初始化日志，自定义tag，是否是debug环境，日志最大长度，这几个字段都是非必选
    - 注意，日志只有在debug环境下才可以打印出来，默认是false
- void d(Object object, {String tag})
    - 打印debug日志
- void e(Object object, {String tag}) 
    - 打印error日志
- void v(Object object, {String tag}) 
    - 打印v日志
- void i(Object object, {String tag}) 
    - 打印info日志
- void w(Object object, {String tag}) 
    - 打印ware警告日志


### 08.屏幕参数工具类
- ScreenUtils.screenWidthDp
    - 当前设备宽度 dp
- ScreenUtils.screenHeightDp
    - 当前设备高度 dp
- ScreenUtils.pixelRatio
    - 设备的像素密度
- ScreenUtils.screenWidth
    - 当前设备宽度 px = dp * 密度
- ScreenUtils.screenHeight
    - 当前设备高度 px = dp * 密度
- ScreenUtils.statusBarHeight
    - 状态栏高度 dp 刘海屏会更高
- ScreenUtils.bottomBarHeight;
    - 底部安全区距离 dp
- ScreenUtils.textScaleFactory
    - 像素的字体像素数，字体的缩放比例


### 11.加密和解密工具类
- String encodeMd5(String data)
    - md5 加密字符串，这种是不可逆的
- String encodeBase64(String data) 
    - Base64加密字符串
- String decodeBase64(String data)
    - Base64解密字符串
- String xorBase64Encode(String res, String key)
    - 异或对称 Base64 加密
- String xorBase64Decode(String res, String key) 
    - 异或对称 Base64 解密


### 12.Num格式处理工具类
- bool isNum(String s)
    - 检查字符串是int还是double
- int getIntByValueString(String valueStr, {int defValue = 0})
    - 将数字字符串转int。如果字符串不是数字，则转化为0
- double getDoubleByValueString(String valueStr, {double defValue = 0}) 
    - 数字字符串转double。如果字符串不是数字，则转化为0
- num getNumByValueString(String valueStr, {int fractionDigits}) 
    - 将数字字符串转num，数字保留x位小数
- num getNumByValueDouble(double value, int fractionDigits)
    - 浮点数字保留x位小数
- double addNum(num a, num b)
    - 两个数相加（防止精度丢失）
- double subtractNum(num a, num b)
    - 两个数相减（防止精度丢失）
- double multiplyNum(num a, num b)
    - 两个数相乘（防止精度丢失）
- double divideNum(num a, num b)
    - 两个数相除（防止精度丢失）
- Decimal addDecString(String a, String b)
    - 两个数相加（防止精度丢失）
- Decimal subtractDecString(String a, String b)
    - 两个数相减（防止精度丢失）
- Decimal multiplyDecString(String a, String b)
    - 两个数相乘（防止精度丢失）
- Decimal divideDecString(String a, String b)
    - 两个数相除（防止精度丢失）



### 14.图片处理工具类
- 其他待完善，补充圆角，圆形切割图片，以及处理本地等图片的方法。
- MemoryImage base64ToImage(String base64String)
    - 将base64流转化为图片
- String fileToBase64(File imgFile)
    - 将图片file转化为base64
- Future networkImageToBase64(String url)
    - 将网络链接图片转化为base64
- Future assetImageToBase64(String path)
    - 将asset图片转化为base64
- CachedNetworkImage showNetImageWh(String url, double width, double height)
    - 加载网络图片，并且指定宽高大小。使用默认预加载loading和错误视图
- CachedNetworkImage showNetImageWhError(String url,double width, double height , LoadingErrorWidgetBuilder error)
    - 加载网络图片，并且指定宽高大小。传入错误视图
- CachedNetworkImage showNetImageWhPlaceError(String url,double width, double height ,PlaceholderWidgetBuilder place,LoadingErrorWidgetBuilder error)
    - 加载网络图片，并且指定宽高大小。传入预加载，错误视图
- CachedNetworkImage showNetImageWhClip(String url,double width, double height , double circular)
    - 加载网络图片，并且指定宽高大小。切割圆角
- CachedNetworkImage showNetImageCircle(String url, double radius)
    - 加载网络图片，切割圆形图片。


### 16.常用正则工具类
- bool isMobileSimple(String input)
    - 简单验证手机号
- bool isMobileExact(String input)
    - 精确验证是否是手机号
- bool isTel(String input)
    - 验证电话号码
- bool isIDCard(String input)
    - 返回输入是否匹配身份证号码的正则表达式
- bool isIDCard15(String input)
    - 验证身份证号码 15 位
- bool isIDCard18(String input)
    - 简单验证身份证号码 18 位
- bool isIDCard18Exact(String input)
    - 精确验证身份证号码 18 位
- bool isEmail(String input)
    - 验证邮箱
- bool isURL(String input)
    - 验证 URL
- bool isZh(String input)
    - 验证汉字
- bool isDate(String input) 
    - 验证用户名
- bool isIP(String input)
    - 验证 IP 地址
- bool isQQ(String input)
    - 判断是否是qq


### 17.Object常用工具类
- bool isNull(dynamic s)
    - 判断对象是否为null
- bool isNullOrBlank(dynamic s)
    - 检查数据是否为空或空(空或只包含空格)
- bool isEmptyString(String str)
    - 判断字符串是否为空
- bool isEmptyList(Iterable list)
    - 判断集合是否为空
- bool isEmptyMap(Map map)
    - 判断字典是否为空
- bool isEmpty(Object object)
    - 判断object对象是否为空
- bool isNotEmpty(Object object)
    - 判断object是否不为空
- bool compareListIsEqual(List listA, List listB) 
    - 比较两个集合是否相同
- int getLength(Object value)
    - 获取object的长度



### 18.验证相关工具类
- 验证相关工具类
    ```
    isNumericOnly                            : 检查字符串是否只包含数字
    isAlphabetOnly                           : 检查字符串是否只包含字母。(没有空格)
    isBool                                   : 检查字符串是否为布尔值
    isVector                                 : 检查string是否为vector文件
    isImage                                  : 检查字符串是否为图像文件
    isAudio                                  : 检查字符串是否为音频文件
    isVideo                                  : 检查字符串是否为视频文件
    isTxt                                    : 检查字符串是否为txt文本文件
    isDocument                               : 检查字符串是否为doc文件
    isExcel                                  : 检查字符串是否为excel文件
    isPPT                                    : 检查字符串是否为ppt文件
    isAPK                                    : 检查字符串是否为apk文件
    isPDF                                    : 检查字符串是否为pdf文件
    isHTML                                   : 检查字符串是否为html文件
    isURL                                    : 检查字符串是否为url文件
    isEmail                                  : 检查字符串是否为email文件
    isDateTime                               : 检查字符串是否为时间
    isMD5                                    : 检查字符串是否为md5
    isSHA1                                   : 检查字符串是否为sha1
    isSHA256                                 : 检查字符串是否为sha256
    isIPv4                                   : 检查字符串是否为ipv4
    isIPv6                                   : 检查字符串是否为ipv6
    isPalindrome                             : 检查字符串是否为回文
    isCaseInsensitiveContains                : 检查a是否包含b(将大小写字母视为相同或解释)。
    isCaseInsensitiveContainsAny             : 检查a中是否包含b或b中是否包含a(将大小写字母视为相同)。
    isCamelCase                              : 检查字符串值是否驼峰大小写
    isCapitalize                             : 检查字符串值是否大写
    ```


### 21.Text文本工具类
- bool isEmpty(String text) 
    - 判断文本内容是否为空
- bool startsWith(String str, Pattern prefix, [int index = 0])
    - 判断字符串是以xx开头
- bool contains(String str, Pattern searchPattern, [int startIndex = 0])
    - 判断字符串中是否包含xx
- String abbreviate(String str, int maxWidth, {int offset = 0})
    - 使用点缩写字符串。比如：yangchong，maxWidth=7，则输出结果：yang...
- int compare(String str1, String str2)
    - 比较两个字符串是否相同
- int hammingDistance(String str1, String str2) 
    - 比较两个长度一样的字符串有几个字符不同
- String formatDigitPattern(String text, {int digit = 4, String pattern = ' '})
    - 每隔 x位 加 pattern。比如用来格式化银行卡
- String formatSpace4(String text)
    - 每隔4位加空格
- String hideNumber(String phoneNo,{int start = 3, int end = 7, String replacement = '****'})
    - 隐藏手机号中间n位，比如隐藏手机号 13667225184 为 136****5184
- String replace(String text, Pattern from, String replace)
    - 替换字符串中的数据
- List<String> split(String text, Pattern pattern) 
    - 按照规则切割字符串，返回数组
- String reverse(String text)
    - 反转字符串



### 22.i18拓展工具类
- LocatizationExtensionState类中：String getString(String id)
    - 获取不同Locales渠道语言内容。举例：使用：var name = context.getString("name");
- LocatizationExtensionContext类中：String getString(String id)
    - 获取不同Locales渠道语言内容
- 如何添加不同渠道语言内容。写在 main 函数中 runApp 之前
    ``` dart
    AppLocalizations.supportedLocales = [
        const Locale('en', 'US'),
        const Locale('pt', 'BR'),
        const Locale('ja', 'JP'),
        const Locale('zh', 'CN'),
    ];
    ```


### 23.Time时间工具类


### 24.SPI帮助工具类
- spi简单介绍
    - Service Locator可以将接口（抽象基类）与具体实现分离和解耦合，同时允许通过接口从App中的任何位置访问具体实现。
    ```
    //第一步注册
    GetIt serviceLocator = GetIt.instance;
    getIt.registerSingleton<BusinessService>(new BusinessServiceImpl());
    
    //第二步使用
    BusinessService businessService = serviceLocator<BusinessService>();
    businessService.noneBusinessPattern();
    
    //第三步解绑
    serviceLocator.resetLazySingleton<BusinessService>();
    ```



### 25.计时器辅助工具类
- new TimerUtils(mInterval: 1000,mTotalTime: 60)
    - 创建倒计时器
- new TimerUtils()
    - 创建倒计时器
- setTotalTime(int totalTime)
    - 设置倒计时总时间
- setInterval(int interval) 
    - 设置Timer间隔
- startTimer() 
    - 启动定时Timer
- updateTotalTime(int totalTime)
    - 重设倒计时总时间
- isActive() 
    - 判断Timer是否启动
- pauseTimer()
    - 暂停倒计时器
- cancel() 
    - 取消计时器
- setOnTimerTickCallback(OnTimerTickCallback callback) 
    - 设置倒计时器的监听



### 26.常见拓展工具类
#### 26.1 int拓展类：ExtensionInt
- ExtensionInt拓展类
    ```
    isPalindrome                             : 检查int是否为回文
    isOneAKind                               : 检查所有数据是否具有相同的值
    toBinary                                 : 转换int值为二进制
    toBinaryInt                              : 转换int值为二进制int
    fromBinary                               : 转换int值为二进制字符串
    ```


#### 26.2 List拓展类：ExtensionList
- ExtensionList拓展类
    ```
    toJsonString                             : 将list转化为json字符串
    getJsonPretty                            : 将list转化为json字符串，换行
    valueTotal                               : 获取num列表的总值(int/double)
    isNull                                   : 判断对象是否为null
    isNullOrBlank                            : 检查数据是否为空或空(空或只包含空格)
    ```


#### 26.3 Map拓展类：ExtensionMap
- ExtensionMap拓展类【set拓展类同理】
    ```
    toJsonString                             : 将map转化为json字符串
    getJsonPretty                            : 将map转化为json字符串换行
    isNull                                   : 判断对象是否为null
    isNullOrBlank                            : 检查数据是否为空或空(空或只包含空格)
    ```


#### 26.4 String拓展类：ExtensionString
- ExtensionString拓展类
    ```
    isNull                                   : 判断对象是否为null
    isNullOrBlank                            : 检查数据是否为空或空(空或只包含空格)
    isNumericOnly                            : 检查字符串是否只包含数字
    isAlphabetOnly                           : 检查字符串是否只包含字母。(没有空格)
    isBool                                   : 检查字符串是否为布尔值
    isVector                                 : 检查string是否为vector文件
    isImage                                  : 检查字符串是否为图像文件
    isAudio                                  : 检查字符串是否为音频文件
    isVideo                                  : 检查字符串是否为视频文件
    isTxt                                    : 检查字符串是否为txt文本文件
    isDocument                               : 检查字符串是否为doc文件
    isExcel                                  : 检查字符串是否为excel文件
    isPPT                                    : 检查字符串是否为ppt文件
    isAPK                                    : 检查字符串是否为apk文件
    isPDF                                    : 检查字符串是否为pdf文件
    isHTML                                   : 检查字符串是否为html文件
    isURL                                    : 检查字符串是否为url文件
    isEmail                                  : 检查字符串是否为email文件
    isDateTime                               : 检查字符串是否为时间
    isMD5                                    : 检查字符串是否为md5
    isSHA1                                   : 检查字符串是否为sha1
    isSHA256                                 : 检查字符串是否为sha256
    isIPv4                                   : 检查字符串是否为ipv4
    isIPv6                                   : 检查字符串是否为ipv6
    isPalindrome                             : 检查字符串是否为回文
    isCaseInsensitiveContains                : 检查a是否包含b(将大小写字母视为相同或解释)。
    isCaseInsensitiveContainsAny             : 检查a中是否包含b或b中是否包含a(将大小写字母视为相同)。
    isCamelCase                              : 检查字符串值是否驼峰大小写
    isCapitalize                             : 检查字符串值是否大写
    ```



### 33.转化相关工具类
- String toBinary(int i)
    - 转换int值为二进制
- int toBinaryInt(int i)
    - 转换int值为二进制
- int fromBinary(String binaryStr)
    - 转换二进制为int值
- String capitalize(String s, {bool firstOnly = false})
    - 字符串内的每个单词都要大写
- String capitalizeFirst(String s) 
    - 字符串的首字母大写，其他字母小写
- String removeAllWhitespace(String s)
    - 删除字符串内的所有空格
- String numericOnly(String s, {bool firstWordOnly = false})
    - 提取字符串的数值


### 34.全局异常捕获工具
- 针对flutter全局异常捕获，可以使用：handle_exception
    ```
    //如果使用，在main方法中，如下所示：
    hookCrash(() {
      runApp(MainApp());
    });
    ```
- 捕获一场打印输出：
    ```
    I/flutter ( 9506): yc e  — — — — — — — — — — st — — — — — — — — — — — — —
    I/flutter ( 9506): yc e | handle_exception :  e---->MissingPluginException(No implementation found for method getAll on channel plugins.flutter.io/shared_
    I/flutter ( 9506): yc e | preferences)
    I/flutter ( 9506): yc e  — — — — — — — — — — ed — — — — — — — — — ---— —
    I/flutter ( 9506): yc e  handle_exception :  stack---->
    ```


### 40.其他相关工具类





