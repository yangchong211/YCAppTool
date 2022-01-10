#### 目录介绍
- 01.该库具有的功能
- 02.崩溃处理模块
- 03.网络分析库模块
- 04.ping库模块
- 05.该库如何使用
- 06.后续的需求说明
- 07.异常栈轨迹原理
- 08.部分问题反馈
- 09.其他内容说明




### 01.该库具有的功能
- **崩溃处理相关模块**：
    - 崩溃重启操作，崩溃记录日志操作，崩溃日志列表支持查询，删除，查看详情，分享，保存文本，以及截图等操作。
- **网络分析库模块**：
    - 网络流程分析，记录每个网络请求->响应数据，方便查看很全面的请求头信息，响应头信息，以及body实体，以及网络连接，dns解析，TLS连接，请求响应等时间差……
- **ping库模块**：
    - 通过ping检测网络问题，帮助诊断，这个在Android中检查域名的诊断信息……
- **最大特点**
    - 入侵性低，你不用改动愿项目代码，几行代码设置即可使用这几个模块功能，已经用于多个实际项目中。如果觉得可以，麻烦star一下……


### 02.崩溃处理模块
#### 2.1 异常崩溃介绍
- 异常崩溃后思考的一些问题
    - 1.是否需要恢复activity栈，以及所在崩溃页面数据
    - 2.crash信息保存和异常捕获，是否和百度bug崩溃统计sdk等兼容。是否方便接入
    - 3.是否要回到栈顶部的那个activity(保存栈信息)
    - 4.崩溃后需要收集哪些信息。手机信息，app信息，崩溃堆栈，内存信息等
    - 5.异常崩溃如何友好退出，以及崩溃后调用重启app是否会出现数据异常
    - 6.针对native代码崩溃，如何记录日志写到文件中
- 该库可以做一些什么
    - 1.在Android手机上显示闪退崩溃信息，并且崩溃详情信息可以保存，分享给开发
        - 主要是测试同学在测试中发现了崩溃，然后跑过去跟开发说，由于不容易复现导致开发童鞋不承认……有时候用的bug统计不是那么准！
    - 2.对于某些设备，比如做Kindle开发，可以设置崩溃重启app操作
    - 3.暴露了用户上传自己捕获的crash数据，以及崩溃重启的接口监听操作
    - 4.一个崩溃日志保存到一个文件中，文件命名规则【版本+日期+异常】：V1.0_2020-09-02_09:05:01_java.lang.NullPointerException.txt
    - 5.崩溃日志list可以获取，支持查看日志详情，并且可以分享，截图，以及复制崩溃信息
    - 6.收集崩溃日志包括，设备信息，进程信息，崩溃信息(Java崩溃、Native崩溃 or ANR)
    - 7.收集崩溃时的内存信息（OOM、ANR、虚拟内存耗尽等，很多崩溃都跟内存有直接关系），完善中


#### 2.2 截图如下所示
![image](https://img-blog.csdnimg.cn/20200902194445625.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)
![image](https://img-blog.csdnimg.cn/20200902194445623.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)
![image](https://img-blog.csdnimg.cn/20200902194445622.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)
![image](https://img-blog.csdnimg.cn/20200902194445576.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)


#### 2.3崩溃后日志记录
![image](https://img-blog.csdnimg.cn/20200904095027529.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)


#### 2.4 崩溃流程图
![image](https://img-blog.csdnimg.cn/2020090417061146.jpg)


#### 2.5 该库优势分析
- 低入侵性接入该lib，不会影响你的其他业务。暴露崩溃重启，以及支持开发者自己捕获crash数据的接口！能够收集崩溃中的日志写入文件，记录包括设备信息，进程信息，崩溃信息(Java崩溃、Native崩溃 or ANR)，以及崩溃时内存信息到file文件中。支持用户获取崩溃列表，以及跳转崩溃日志详情页面，并且可以将崩溃日志分享，截长图，复制等操作。可以方便测试和产品给开发提出那种偶发性bug的定位日志，免得对于偶发行崩溃，开发总是不承认……开发总是不承认……



### 03.网络分析库模块
#### 3.1 网络分析库模块
- 参考stetho库地址
    - https://github.com/facebook/stetho
- 功能
    - Stetho 是 Facebook 开源的一个 Android 调试工具。
    - 是一个 Chrome Developer Tools 的扩展，可用来检测应用的网络、数据库、WebKit 、SharePreference等方面的功能。
    - 开发者也可通过它的 dumpapp 工具提供强大的命令行接口来访问应用内部。
- 那么既然网络请求添加StethoInterceptor，既可以拦截网络请求和响应信息，发送给Chrome。那么能不能自己拿来用……
    - 可以的，直接致敬Stetho库，扒代码
- StethoInterceptor大概流程
    - 整个流程我们可以简化为：发送请求时，给Chrome发了条消息，收到请求时，再给Chrome发条消息（具体怎么发的可以看NetworkEventReporterImpl的实现）
    - 两条消息通过EventID联系起来，它们的类型分别是OkHttpInspectorRequest 和 OkHttpInspectorResponse，两者分别继承自NetworkEventReporter.InspectorRequest和NetworkEventReporter.InspectorResponse。
    - 我们只要也继承自这两个类，在自己的网络库发送和收到请求时，构造一个Request和Response并发送给Chrome即可。
- 如何拿来用
    - 既然Android中使用到facebook的stetho库，可以拦截手机请求请求，然后去Chrome浏览器，在浏览器地址栏输入：chrome://inspect 。即可查看请求信息。
    - 那么能不能把这个拿到的请求信息，放到集合中，然后在Android的页面中展示呢？这样方便开发和测试查看网络请求信息，以及请求流程中的消耗时间（比如dns解析时间，请求时间，响应时间，共耗时等等）
- OkHttp如何进行各个请求环节的耗时统计呢？
    - OkHttp 版本提供了EventListener接口，可以让调用者接收一系列网络请求过程中的事件，例如DNS解析、TSL/SSL连接、Response接收等。
    - 通过继承此接口，调用者可以监视整个应用中网络请求次数、流量大小、耗时(比如dns解析时间，请求时间，响应时间等等)情况。
    - 因此统计耗时，需要致敬EventListener实现，照搬代码即可。
- 如何消耗记录时间
    - 在OkHttp库中有一个EventListener类。该类是网络事件的侦听器。扩展这个类以监视应用程序的HTTP调用的数量、大小和持续时间。
    - 所有启动/连接/获取事件最终将接收到匹配的结束/释放事件，要么成功(非空参数)，要么失败(非空可抛出)。
    - 比如，可以在开始链接记录时间；dns开始，结束等方法解析记录时间，可以计算dns的解析时间。
    - 比如，可以在开始请求记录时间，记录connectStart，connectEnd等方法时间，则可以计算出connect连接时间。


#### 3.2 网络拦截库功能
- 网络请求拦截
    - 记录每个页面网络请求的数据，保存在map中，方便查看网络请求的请求头，响应头，响应body
    - 针对获取的网络数据，记录网络状态，请求耗时，请求方式，完善了网络日志包括的绝大部分字段信息展示
- 流量统计
    - 目前只是针对get，post网络请求以及上传流量统计，并且针对get和post比例展示，以及抓包数量统计
    - 后期完善比如glide加载图片消耗的流量统计
- 网络消耗时间
    - 网络请求到完成耗时，dns消耗时间，连接消耗时间，TLS连接开始结束消耗时间，request请求消耗时间，response响应消耗时间等等
- 设备信息
    - 获取手机设备信息，包括硬件厂商，版本，root，包名等信息
    - 获取本机wifi信息，主要是wifi的名称，mac地址，ip地址，dns信息，子网掩码等
    - 获取服务端信息，根据网络请求host，获取服务端ip，mac，是ipv4还是ipv6等，这个还在完善中
- ping网络诊断
    - 致敬网易ping方案，应用于Android项目中，获取Android项目域名，拿到ping信息
- 全局悬浮按钮
    - 为了方便查看每个activity页面的网络请求数据，因此在每个activity页面显示全局悬浮按钮，点击即可跳入查看数据页面
    - 这个一行代码设置即可，无任何耦合作用，低入侵


#### 3.3 网络请求接口信息
- 请求接口如下所示
    - https://www.wanandroid.com/friend/json
- General
    - Request URL: https://www.wanandroid.com/friend/json
    - Request Method: GET
    - Status Code: 200 OK
    - Remote Address: 47.104.74.169:443
    - Referrer Policy: no-referrer-when-downgrade
- Response Header
    - HTTP/1.1 200 OK
    - Server: Apache-Coyote/1.1
    - Cache-Control: private
    - Expires: Thu, 01 Jan 1970 08:00:00 CST
    - Content-Type: application/json;charset=UTF-8
    - Transfer-Encoding: chunked
    - Date: Thu, 10 Sep 2020 01:05:47 GMT
- Request Header
    - Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
    - Accept-Encoding: gzip, deflate, br
    - Accept-Language: zh-CN,zh;q=0.9
    - Cache-Control: no-cache
    - Connection: keep-alive
    - Cookie: JSESSIONID=5D6302E64E9734210FA231A6FAF5799E; Hm_lvt_90501e13a75bb5eb3d067166e8d2cad8=1598920692,1599007288,1599094016,1599629553; Hm_lpvt_90501e13a75bb5eb3d067166e8d2cad8=1599699419
    - Host: www.wanandroid.com
    - Pragma: no-cache
    - Sec-Fetch-Dest: document
    - Sec-Fetch-Mode: navigate
    - Sec-Fetch-Site: none
    - Upgrade-Insecure-Requests: 1
    - User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36
- Response返回body
    - 这里省略
- 看截图如下
    - ![image](https://img-blog.csdnimg.cn/20200910095754628.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)


#### 3.5案例截图如下
![image](https://img-blog.csdnimg.cn/2020090921524345.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)
![image](https://img-blog.csdnimg.cn/2020090921524393.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)
![image](https://img-blog.csdnimg.cn/20200910095645227.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)
![image](https://img-blog.csdnimg.cn/20200914194859602.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)
![image](https://img-blog.csdnimg.cn/20200923103832877.jpeg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)



### 04.ping库模块
#### 4.1 ping在Android的应用
- 为了检查网络，在android上也可以通过ping来查看是否网络通。
- 实现方案有哪些
    - 通过后台线程执行ping命令的方式模拟traceroute的过程，缺点就是模拟过程较慢，timeout的出现比较频繁
    - 通过编译开源网络检测库iputilsC代码的方式对traceroute进行了套接字发送ICMP报文模拟，可以明显提高检测速度
- 关于代码ping的过程信息
    - 开启一个AsyncTask，在doInBackground方法中开始解析，这个是入口。
    - 添加头部信息，主要包括：开始诊断 + 输出关于应用、机器、网络诊断的基本信息 + 输出本地网络环境信息
    - tcp三次握手操作
        - 开始执行链接，这里有两个重要信息。一个是ip集合，另一个是InetAddress数组，遍历【长度是ip集合length】，然后执行请求
        - 创建socketAddress，有两个参数，一个是ip，一个是端口号80，然后for循环执行socket请求
        - 在执行socket请求的时候，如果有监听到超时SocketTimeoutException异常则记录数据，如果有异常则记录数据
        - 当出现发生timeOut,则尝试加长连接时间，注意连续两次连接超时,停止后续测试。连续两次出现IO异常,停止后续测试
        - 当然只要有一次完整执行成功的流程，那么则记录三次握手操作成功
    - 诊断ping信息, 同步过程。这个主要是直接通过ping命令监测网络
        - 创建一个NetPing对象，设置每次ping发送数据包的个数为4个
        - 然后ping本机ip地址，ping本地网观ip地址，ping本地dns。这个ping的指令是啥？这个主要是用java中的Runtime执行指令……
    - 开始诊断traceRoute
        - 先调用原生jni代码，调用jni c函数执行traceroute过程。如果发生了异常，再调用java代码执行操作……
        - 然后通过ping命令模拟执行traceroute的过程，比如：ping -c 1 -t 1 www.jianshu.com
        - 如果成功获得trace:IP，则再次发送ping命令获取ping的时间



#### 4.2 ping使用截图
![image](https://img-blog.csdnimg.cn/20200910145919655.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)


### 05.该库如何使用
#### 5.1 崩溃库
- 如何引入该库
    ``` java
    //崩溃重启库
    implementation 'cn.com.yc:ToolLib:1.2.3'
    //网络拦截日志库
    implementation 'cn.com.yc:NetLib:1.0.1'
    //GitHub代码
    https://github.com/yangchong211/YCAndroidTool
    ```
- 初始化代码如下所示。建议在Application中初始化……
    ``` java
    CrashHandler.getInstance().init(this, new CrashListener() {
        /**
         * 重启app
         */
        @Override
        public void againStartApp() {
            CrashToolUtils.reStartApp1(App.this,1000);
            //CrashToolUtils.reStartApp2(App.this,1000, MainActivity.class);
            //CrashToolUtils.reStartApp3(AppManager.getAppManager().currentActivity());
        }

        /**
         * 自定义上传crash，支持开发者上传自己捕获的crash数据
         * @param ex                        ex
         */
        @Override
        public void recordException(Throwable ex) {
            //自定义上传crash，支持开发者上传自己捕获的crash数据
            //StatService.recordException(getApplication(), ex);
        }
    });
    ```
- 关于重启App的操作有三种方式api
    ``` java
    //开启一个新的服务KillSelfService，用来重启本APP【使用handler延迟】
    CrashToolUtils.reStartApp1(App.this,1000);
    //用来重启本APP[使用闹钟，整体重启，临时数据清空（推荐）]
    CrashToolUtils.reStartApp2(App.this,1000, MainActivity.class);
    //检索获取项目中LauncherActivity，然后设置该activity的flag和component启动app【推荐】
    CrashToolUtils.reStartApp3(AppManager.getAppManager().currentActivity());
    ```
- 关于获取崩溃目录api
    ``` java
    //崩溃文件存储路径：/storage/emulated/0/Android/data/你的包名/cache/crashLogs
    //崩溃页面截图存储路径：/storage/emulated/0/Android/data/你的包名/cache/crashPics
    String crashLogPath = ToolFileUtils.getCrashLogPath(this);
    String crashPicPath = ToolFileUtils.getCrashPicPath(this);
    ```
- 关于崩溃日志记录
    - 日志记录路径：/storage/emulated/0/Android/data/你的包名/cache/crashLogs
    - 日志文件命名：V1.0_2020-09-02_09:05:01_java.lang.NullPointerException.txt【版本+日期+异常】
- 关于跳转错误日志list列表页面
    - 跳转日志列表页面如下所示，这里调用一行代码即可。点击该页面list条目即可进入详情
    ``` java
    CrashToolUtils.startCrashListActivity(this);
    ```
- 那么如何获取所有崩溃日志的list呢。建议放到子线程中处理！！
    ``` java
    List<File> fileList = ToolFileUtils.getCrashFileList(this);

    //如果是要自己拿到这些文件，建议根据时间来排个序
    //排序
    Collections.sort(fileList, new Comparator<File>() {
        @Override
        public int compare(File file01, File file02) {
            try {
                //根据修改时间排序
                long lastModified01 = file01.lastModified();
                long lastModified02 = file02.lastModified();
                if (lastModified01 > lastModified02) {
                    return -1;
                } else {
                    return 1;
                }
            } catch (Exception e) {
                return 1;
            }
        }
    });
    ```
- 如何删除单个文件操作
    ``` java
    //返回true表示删除成功
    boolean isDelete = ToolFileUtils.deleteFile(file.getPath());
    ```
- 如何删除所有的文件。建议放到子线程中处理！！
    ``` java
    File fileCrash = new File(ToolFileUtils.getCrashLogPath(CrashListActivity.this));
    ToolFileUtils.deleteAllFiles(fileCrash);
    ```
- 如何获取崩溃文件中的内容
    ``` java
    //获取内容
    String crashContent = ToolFileUtils.readFile2String(filePath);
    ```
- 还有一些关于其他的api，如下。这个主要是方便测试同学或者产品，避免开发不承认那种偶发性崩溃bug……
    ```
    //拷贝文件，两个参数分别是源文件，还有目标文件
    boolean copy = ToolFileUtils.copyFile(srcFile, destFile);
    //分享文件。这个是调用原生的分享
    CrashLibUtils.shareFile(CrashDetailsActivity.this, destFile);
    //截图崩溃然后保存到相册。截图---> 创建截图存储文件路径---> 保存图片【图片质量，缩放比还有采样率压缩】
    final Bitmap bitmap = ScreenShotsUtils.measureSize(this,view);
    String crashPicPath = ToolFileUtils.getCrashPicPath(CrashDetailsActivity.this) + "/crash_pic_" + System.currentTimeMillis() + ".jpg";
    boolean saveBitmap = CrashLibUtils.saveBitmap(CrashDetailsActivity.this, bitmap, crashPicPath);
    ```
- 异常恢复原理
    - 第一种方式，开启一个新的服务KillSelfService，用来重启本APP。
        ``` java
        CrashToolUtils.reStartApp1(App.this,1000);
        ```
    - 第二种方式，使用闹钟延时，然后重启app
        ``` java
        CrashToolUtils.reStartApp2(App.this,1000, MainActivity.class);
        ```
    - 第三种方式，检索获取项目中LauncherActivity，然后设置该activity的flag和component启动app
        ``` java
        CrashToolUtils.reStartApp3(AppManager.getAppManager().currentActivity());
        ```
    - 关于app启动方式详细介绍
        - [App启动介绍](https://github.com/yangchong211/YCAndroidTool/blob/master/read/06.App%E9%87%8D%E5%90%AF%E5%87%A0%E7%A7%8D%E6%96%B9%E5%BC%8F.md)


#### 5.2 如何网络拦截
- 如下所示
    ``` java
    new OkHttpClient.Builder()
        //配置工厂监听器。主要是计算网络过程消耗时间
        .eventListenerFactory(NetworkListener.get())
        //主要是处理拦截请求，响应等信息
        .addNetworkInterceptor(new NetworkInterceptor())
        .build()
    ```
- 如何开启悬浮按钮
    ```
    //建议只在debug环境下显示，点击去网络拦截列表页面查看网络请求数据
    NetworkTool.getInstance().setFloat(getApplication());
    ```
- 该库目的
    - 做成悬浮全局按钮，点击按钮可以查看该activity页面请求接口，可以查看请求几个接口，以及接口请求到响应消耗流量
    - 方便查看网络请求流程，比如dns解析时间，请求时间，响应时间
    - 方便测试查看请求数据，方便抓包。可以复制request，respond，body等内容。也可以截图


#### 5.3 如何使用ping
- 直接创建一个ping，需要传递一个网址url
    ``` java
    _netDiagnoService = new NetDiagnoService(getContext(), getContext().getPackageName()
            , versionName, userId, deviceId, host, this);
    _netDiagnoService.execute();
    ```
- 如何取消ping
    ``` java
    if (_netDiagnoService!=null){
        _netDiagnoService.cancel(true);
        _netDiagnoService = null;
    }
    ```
- 或者直接停止ping。停止线程允许，并把对象设置成null
    ``` java
    _netDiagnoService.stopNetDialogsis();
    ```
- 关于监听
    ``` java
    /**
     * 诊断结束，输出全部日志记录
     * @param log                       log日志输出
     */
    @Override
    public void OnNetDiagnoFinished(String log) {
        setText(log);
    }

    /**
     * 监控网络诊断过程中的日志输出
     * @param log                       log日志输出
     */
    @Override
    public void OnNetDiagnoUpdated(String log) {
        showInfo += log;
        setText(showInfo);
    }
    ```


### 06.后续的需求说明
- 可能不兼容
    - 该库尚未通过多进程应用程序进行测试。如果您使用这种配置进行测试，请提供反馈！
    - 如果您的应用程序初始化监听或错误活动崩溃，则有可能进入无限重启循环（在大多数情况下，库会对此进行检查，但在极少数情况下可能会发生）。
    - 修复Android P反射限制导致的Activity生命周期异常无法finish Activity问题。某些机型还是不兼容……
- App崩溃收集信息说明
    - 收集崩溃时的基本信息
        - 进程（前台进程还是后台进程）
        - 线程（是否是 UI 线程）
        - 崩溃堆栈（具体崩溃在系统的代码，还是我们自己的代码里面）
        - 崩溃堆栈类型（Java 崩溃、Native 崩溃 or ANR）
    - 收集崩溃时的系统信息
        - 机型、系统、厂商、CPU、ABI、Linux 版本等。（寻找共性）
        - Logcat。（包括应用、系统的运行日志，其中会记录 App 运行的一些基本情况）
    - 收集崩溃时的内存信息（OOM、ANR、虚拟内存耗尽等，很多崩溃都跟内存有直接关系）
        - 系统剩余内存。（系统可用内存很小 – 低于 MemTotal 的 10%时，OOM、大量 GC、系统频繁自杀拉起等问题都非常容易出现）
        - 虚拟内存（但是很多类似OOM、tgkill 等问题都是虚拟内存不足导致的）
        - 应用使用内存（得出应用本身内存的占用大小和分布）
        - 线程数
    - 收集崩溃时的应用信息
        - 崩溃场景（崩溃发生在哪个 Activity 或 Fragment，发生在哪个业务中）
        - 关键操作路径（记录关键的用户操作路径，这对我们复现崩溃会有比较大的帮助）
        - 其他自定义信息（不同应用关心的重点不一样。例如运行时间、是否加载了补丁、是否是全新安装或升级等）



### 07.异常栈轨迹原理
- Android发生异常为何崩溃
    - 一旦线程出现抛出异常，并且我们没有捕捉的情况下，JVM将调用Thread中的dispatchUncaughtException方法把异常传递给线程的未捕获异常处理器。发现最后会使用到Thread.getDefaultUncaughtExceptionHandler()
    - 既然Android遇到异常会发生崩溃，然后找一些哪里用到设置setDefaultUncaughtExceptionHandler，即可定位到RuntimeInit类。
    - 具体可以找到RuntimeInit类，然后在找到KillApplicationHandler类。首先看该类的入口main方法--->commonInit()--->，然后接着往下走，找到setDefaultUncaughtExceptionHandler代码。当出现异常是try-catch，并且在finally中直接kill杀死app操作。
    - 详细可以看：[Android项目崩溃分析](https://github.com/yangchong211/YCAndroidTool/blob/master/read/07.Android%E9%A1%B9%E7%9B%AE%E5%B4%A9%E6%BA%83%E5%88%86%E6%9E%90.md)
- 崩溃后异常堆栈链是如何形成的
    - 待完善，看：[异常栈轨迹处理](https://github.com/yangchong211/YCAndroidTool/blob/master/read/04.%E5%BC%82%E5%B8%B8%E6%A0%88%E8%BD%A8%E8%BF%B9%E5%A4%84%E7%90%86.md)



### 08.部分问题反馈
- 该异常捕获实效了是什么情况？
    - Thread.setDefaultUncaughtExceptionHandler(handler) 方法如果被多次调用的话，会以最后一次传递的 handler 为准，所以如果用了第三方的统计模块，可能会出现失灵的情况。对于这种情况，在设置默认 hander 之前，可以先通过 getDefaultUncaughtExceptionHandler() 方法获取并保留旧的 hander，然后在默认 handler 的uncaughtException 方法中调用其他 handler 的 uncaughtException 方法，保证都会收到异常信息。
- 关于上传日志介绍
    - 设置该异常初始化后，在进入全局异常时系统就提示尽快收集信息，进程将被结束，因此不可以在此时做网络上传崩溃信息。可以在此时将错误日志写入到file文件或者sp中。
    - 比如：通过SharedPreferences将错误日志的路径写入配置文件中，在启动的时候先检测该配置文件是否有错误日志信息，如果有则读取文件，然后实现日志上传。上传完成后删除该sp文件……
- 使用looper可以拦截崩溃和anr吗
    - 可以实现拦截UI线程的崩溃，耗时性能监控。但是也并不能够拦截所有的异常。如果在Activity的onCreate出现崩溃，导致Activity创建失败，那么就会显示黑屏。
    - fork出app进程后，在ActivityThread中，在main方法的最后调用了 Looper.loop()，在这个方法中处理主线程的任务调度，一旦执行完这个方法就意味着APP被退出了。
    - 果主线程发生了异常，就会退出循环，意味着APP崩溃，所以我们我们需要进行try-catch，避免APP退出，再启动一个 Looper.loop() 去执行主线程任务，就不会退出。
    - looper拦截崩溃或者anr，存在一个巨大的问题，就是按钮点不动或者无反应。有可能导致出现其他问题……这个需要慎重使用



### 09.其他内容说明
- 崩溃重启和异常库混淆
    - -keep class com.com.yc.toollib.** { *; }
    - -keepnames class com.com.yc.toollib.** { *; }
- 网络拦截日志库混淆
    - -keep class com.com.yc.netlib.** { *; }
    - -keepnames class com.com.yc.netlib.** { *; }
- 该库笔记介绍
    - [崩溃原理深度探索](https://github.com/yangchong211/YCAndroidTool/blob/master/read/02.%E5%B4%A9%E6%BA%83%E5%8E%9F%E7%90%86%E6%B7%B1%E5%BA%A6%E6%8E%A2%E7%B4%A2.md)
    - [常驻应用崩溃后处理](https://github.com/yangchong211/YCAndroidTool/blob/master/read/03.%E5%B8%B8%E9%A9%BB%E5%BA%94%E7%94%A8%E5%B4%A9%E6%BA%83%E5%90%8E%E5%A4%84%E7%90%86.md)
    - [异常栈轨迹处理](https://github.com/yangchong211/YCAndroidTool/blob/master/read/04.%E5%BC%82%E5%B8%B8%E6%A0%88%E8%BD%A8%E8%BF%B9%E5%A4%84%E7%90%86.md)
    - [Loop拦截崩溃和ANR](https://github.com/yangchong211/YCAndroidTool/blob/master/read/05.Loop%E6%8B%A6%E6%88%AA%E5%B4%A9%E6%BA%83%E5%92%8CANR.md)
    - [App重启几种方式](https://github.com/yangchong211/YCAndroidTool/blob/master/read/06.App%E9%87%8D%E5%90%AF%E5%87%A0%E7%A7%8D%E6%96%B9%E5%BC%8F.md)
- 其他项目推荐
    - [1.开源博客汇总](https://github.com/yangchong211/YCBlogs)
    - [2.降低Crash崩溃库](https://github.com/yangchong211/YCAndroidTool)
    - [3.视频播放器封装库](https://github.com/yangchong211/YCVideoPlayer)
    - [4.状态切换管理器封装库](https://github.com/yangchong211/YCStateLayout)
    - [5.复杂RecyclerView封装库](https://github.com/yangchong211/YCRefreshView)
    - [6.弹窗封装库](https://github.com/yangchong211/YCDialog)
    - [7.版本更新封装库](https://github.com/yangchong211/YCUpdateApp)
    - [8.状态栏封装库](https://github.com/yangchong211/YCStatusBar)
    - [9.轻量级线程池封装库](https://github.com/yangchong211/YCThreadPool)
    - [10.轮播图封装库](https://github.com/yangchong211/YCBanner)
    - [11.音频播放器](https://github.com/yangchong211/YCAudioPlayer)
    - [12.画廊与图片缩放控件](https://github.com/yangchong211/YCGallery)
    - [13.Python多渠道打包](https://github.com/yangchong211/YCWalleHelper)
    - [14.整体侧滑动画封装库](https://github.com/yangchong211/YCSlideView)
    - [15.Python爬虫妹子图](https://github.com/yangchong211/YCMeiZiTu)
    - [17.自定义进度条](https://github.com/yangchong211/YCProgress)
    - [18.自定义折叠和展开布局](https://github.com/yangchong211/YCExpandView)
    - [19.商品详情页分页加载](https://github.com/yangchong211/YCShopDetailLayout)
    - [20.在任意View控件上设置红点控件](https://github.com/yangchong211/YCRedDotView)
    - [21.仿抖音一次滑动一个页面播放视频库](https://github.com/yangchong211/YCScrollPager)




