# 通用存储框架
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明
#### 1.1 项目背景介绍
- 缓存方案到背景思考
    - 项目中很多地方使用缓存方案，有的用sp，有的用mmkv，有的用lru，有的用DataStore，有的用sqlite，如何打造通用api切换操作不同存储方案？
    - 缓存方案众多，且各自使用场景有差异，如何选择合适的缓存方式？同时思考如何替换之前老的存储方案，而不用花费很大的时间成本！
- 针对不同的业务场景，不同的缓存方案。
    - 打造一套通用的方案，屏蔽各种缓存方式的差异性，暴露给外部开发者统一的API，简化使用，提高开发效率和使用效率……


#### 1.2 设计目标
- 打造通用存储库：
    - 设计一个缓存通用方案，其次，它的结构需要很简单，因为很多地方需要用到，再次，它得线程安全。灵活切换不同的缓存方式，使用简单。
- 内部开源该库：
    - 作为技术沉淀，当作专项来推动进展。高复用低耦合，便于拓展，可快速移植，解决各个项目使用内存缓存，sp，mmkv，sql，lru，DataStore的凌乱。抽象一套统一的API接口。


#### 1.3 产生收益分析
- 统一缓存API兼容不同存储方案
    - 打造通用api，抹平了sp，mmkv，sql，lru，dataStore等各种方案的差异性。简化开发者使用，功能强大而使用简单！



### 02.常见思路和做法
#### 2.1 缓存存储有哪些
- 比较常见的是内存缓存以及磁盘缓存。
    - 内存缓存：这里的内存主要指的存储器缓存；磁盘缓存：这里主要指的是外部存储器，手机的话指的就是存储卡。
- 内存缓存：
    - 通过预先消耗应用的一点内存来存储数据，便可快速的为应用中的组件提供数据，是一种典型的以空间换时间的策略。
- 磁盘缓存：
    - 读取磁盘文件要比直接从内存缓存中读取要慢一些，而且需要在一个UI主线程外的线程中进行，因为磁盘的读取速度是不能够保证的，磁盘文件缓存显然也是一种以空间换时间的策略。
- 二级缓存：
    - 内存缓存和磁盘缓存结合。比如，LruCache将图片保存在内存，存取速度较快，退出APP后缓存会失效；而DiskLruCache将图片保存在磁盘中，下次进入应用后缓存依旧存在，它的存取速度相比LruCache会慢上一些。


#### 2.2 常见存储方案
- 内存缓存：存储在内存中，如果对象销毁则内存也会跟随销毁。如果是静态对象，那么进程杀死后内存会销毁。
    - Map，LruCache
- 磁盘缓存：后台应用有可能会被杀死，那么相应的内存缓存对象也会被销毁。当你的应用重新回到前台显示时，你需要用到缓存数据时，这个时候可以用磁盘缓存。
    - SharedPreferences，MMKV，DiskLruCache，SqlLite，DataStore，ACache，Room


#### 2.3 基于接口开发
- 基于接口而非实现编程
    - 理解其中的“接口”两个字。从本质上来看，“接口”就是一组“协议”或者“约定”，是功能提供者提供给使用者的一个“功能列表”。
    - 将接口和实现相分离，封装不稳定的实现，暴露稳定的接口。上游系统面向接口而非实现编程，不依赖不稳定的实现细节，这样当实现发生变化的时候，上游系统的代码基本上不需要做改动，以此来降低耦合性，提高扩展性。
- 定义缓存的通用API接口，这里省略部分代码
    ``` kotlin
    interface ICacheable {
        fun saveXxx(key: String, value: Int)
        fun readXxx(key: String, default: Int = 0): Int
        fun removeKey(key: String)
        fun totalSize(): Long
        fun clearData()
    }
    ```





### 03.Api调用说明
- 通用存储库
    - 支持二级缓存，LRU缓存，磁盘缓存(可以使用sp，mmkv，store，或者DiskLruCache)。不管你使用那种方式的存储，都是一套通用的api，使用几乎是零成本。
- 第一步：通用存储库初始化
    ``` java
    CacheConfig.Builder builder = CacheConfig.Companion.newBuilder();
    //设置是否是debug模式
    CacheConfig cacheConfig = builder.debuggable(BuildConfig.DEBUG)
            //设置外部存储根目录
            .extraLogDir(null)
            //设置lru缓存最大值
            .maxCacheSize(100)
            //内部存储根目录
            .logDir(null)
            //创建
            .build();
    CacheInitHelper.INSTANCE.init(MainApplication.getInstance(),cacheConfig);
    //最简单的初始化
    //CacheInitHelper.INSTANCE.init(CacheConfig.Companion.newBuilder().build());
    ```
- 第二步：存储数据和获取数据
    ```
    //存储数据
    dataCache.saveBoolean("cacheKey1",true);
    dataCache.saveFloat("cacheKey2",2.0f);
    dataCache.saveInt("cacheKey3",3);
    dataCache.saveLong("cacheKey4",4);
    dataCache.saveString("cacheKey5","doubi5");
    dataCache.saveDouble("cacheKey6",5.20);
    
    //获取数据
    boolean data1 = dataCache.readBoolean("cacheKey1", false);
    float data2 = dataCache.readFloat("cacheKey2", 0);
    int data3 = dataCache.readInt("cacheKey3", 0);
    long data4 = dataCache.readLong("cacheKey4", 0);
    String data5 = dataCache.readString("cacheKey5", "");
    double data6 = dataCache.readDouble("cacheKey5", 0.0);
    ```
- 第三步：一些存储说明
    - 关于设置磁盘缓存的路径，需要注意一些问题。建议使用该库默认的路径
    ``` java
    /**
     * log路径，通常这个缓存比较私有的内容
     * 比如sp，mmkv，存储的用户数据
     * 内部存储根目录，举个例子：
     * file:data/user/0/包名/files
     * cache:/data/user/0/包名/cache
     */
    val logDir: String?
    
    /**
     * 额外的log路径，通常缓存一些不私密的内存
     * 比如缓存图片，缓存视频，缓存下载文件，缓存日志等
     *
     * 外部存储根目录，举个例子
     * files:/storage/emulated/0/Android/data/包名/files
     * cache:/storage/emulated/0/Android/data/包名/cache
     */
    val extraLogDir: File?
    ```


### 04.遇到的坑分析


### 05.其他问题说明




