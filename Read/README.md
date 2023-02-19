#### 目录介绍
# 优化专题
- 专题1：崩溃治理，崩溃监听，崩溃原理，以及捕获
- 专题2：ANR原理，信息收集，分析，监控，治理
- 专题3：卡顿原因，卡顿监控，卡顿治理
- 专题4：网络优化，弱网，监控
- 专题5：内存监控，内存治理，OOM，内存如何分配
- 专题6：进程包活，进程是如何低进程杀死
- 专题7：启动优化，页面打开优化，统计耗时
- 专题8：UI优化，UI工具建设
- 专题9：稳定性优化，hook疑难问题解决
- 专题10：安全防御，挂载，xposed，虚拟
- 专题11：工具化建设，内存收集工具，ping工具
- 专题12：图片优化，大图检测，大图加载


# 实践专题
- 专题1：组件化实践，组件化通信(Apt工具)，SPI接口隔离
- 专题2：线程池优化，线程优化，串行线程
- 专题3：webView封装，优化，踩坑
- 专题4：跨进程通信，各种跨进程方案罗列，库实践
- 专题5：悬浮窗设计实践，小工具
- 专题6：App状态管理，App广播状态监听
- 专题7：plugin开发，task统计耗时，ASM统计方法耗时
- 专题8：国际化，语言切换实践，favor管理
- 专题9：磁盘缓存方案设计，内存缓存，sp缓存，mmkv，file缓存
- 专题10：动画，富文本，屏幕适配，画廊，glide实现加载速度
- 专题11：自定义控件，测量，布局，绘制
- 专题12：jetpack系列，nav，liveData，viewModel等
- 专题13：弹窗系列，dialog，dialogFragment，toast等
- 专题14：数据处理，sp，file，二级缓存，mkkv


# 架构专题
- 专题1：架构设计，sdk设计规范，基类设计
- 专题2：日志框架实现，日志上传，分享
- 专题3：base封装，activity启动，base类封装，懒加载等
- 专题3：lint工具代码检查开发


# 音视频专项
- 专题1：语音播放
- 专题2：视频播放


# 硬件专项
- 专题1：蓝牙，蓝牙熟悉，蓝牙实践
- 专题2：Wi-Fi


# jetpack系列
- nav，liveData，viewModel等


----------------------------------------------------------------------------------------------------------------


# 优化专题
- 专题1：崩溃治理，崩溃监听，崩溃原理，以及捕获
    - Native层崩溃的监听工具BreakPad：https://blog.csdn.net/qq_32019367/article/details/125184273
    - so库崩溃治理：https://juejin.cn/post/7114181318644072479




### 00.大厂分享
- 抖音：https://juejin.cn/team/6930545192860647431/posts
- https://juejin.cn/post/7124180647958020104


### 01.事件总线
- https://github.com/JeremyLiao/LiveEventBus
- https://github.com/JeremyLiao/SmartEventBus


### 03.崩溃库
- https://github.com/android-notes/Cockroach
- https://github.com/Sunzxyong/Recovery
- https://github.com/bugtags/Bugtags-Android
- https://github.com/MindorksOpenSource/CrashReporter

- spi通信交互
- https://github.com/luqinx/sp


### 08.读写库
- https://github.com/JeremyLiao/FastSharedPreferences
- https://github.com/orhanobut/hawk
- https://github.com/scottyab/secure-preferences


### 参考
https://github.com/happylishang/Collie
https://github.com/Kyson/AndroidGodEye/blob/master/README_zh.md

### 09.路由框架
- https://github.com/meituan/WMRouter


### 10.hook库
- https://github.com/eleme/lancet


### 11.优化反射
- https://github.com/eleme/Intimate
- 启动优化：https://github.com/idisfkj/android-startup
- 组件化初始化：https://github.com/bingoogolapple/AppInit


### 好玩网站
- https://github.com/sun0225SUN/Awesome-Love-Code
- https://github.com/arunboy/love


#### 1.3 性能监控框架
- 首先是异常崩溃方面的，另外则是性能监控方面的，但是他们整体是划分在一起的，都属于线上性能监控体系的。
- Crash相关的，可以从爱奇艺的xCrash学起。包含了崩溃日志，ANR以及native crash，因为版本适配的问题ANR在高版本上已经不是这么好捞了，还有就是native crash相关的。是一个非常牛逼的库了。
    - https://github.com/iqiyi/xCrash
- 线上的性能监控框架可以从腾讯的Matrix学起, Matrix首页上也有介绍，比如fps，卡顿，IO，电池，内存等等方面的监控。其中卡顿监控涉及到的就是方法前后插桩，同时要有函数的mapping表，插桩部分整体来说比较简单感觉。
    - https://github.com/Tencent/matrix
- 关于线上内存相关的，推荐各位可以学习下快手的koom, 对于hprof的压缩比例听说能达到70%，也能完成线上的数据回捞以及监控等等，是一个非常屌的框架。
    - https://github.com/KwaiAppTeam/KOOM
- 其实上述几个库都还是有一个本质相关的东西，那么就是plthook,这个上面三个库应该都有对其的使用，之前是爱奇艺的xhook，现在是字节的bhook, 这个大佬也是我的偶像之一了，非常离谱了算是。
    - https://github.com/iqiyi/xHook
    - https://github.com/bytedance/bhook
- Android 性能采集之Fps,Memory,Cpu 和 Android IO监控。
    - https://juejin.cn/post/6890754507639095303
    - https://juejin.cn/post/6900810600188739592
- kotlin
    - https://juejin.cn/post/7103011782591004680#heading-77


### 13.优秀博客
- 西瓜视频稳定性建设
    - https://juejin.cn/post/6953430618726203399
    - https://mp.weixin.qq.com/s/DWOQ9MSTkKSCBFQjPswPIQ
- 从 jCenter 迁移到 MavenCentral 完整方案
    - https://www.jianshu.com/p/a243d3c96cea
- aidl优化工具
    - https://github.com/JeremyLiao/InterfaceLoader
- gradle系统学习
    - https://github.com/JeremyLiao/android-gradle-study
- SDK开发的一点点心得 | 经验之谈
    - https://juejin.cn/post/6952870833761026079


### JVM
- https://github.com/doocs/jvm

### 15.性能优化
- Apk瘦身-去除R.class
    - https://www.jianshu.com/p/a1da7fbc6739


### 稳定性
- https://blog.51cto.com/u_15233911/2826427
- 腾讯Matrix源码
    - https://www.yuque.com/ibaozi/ygq8yc


- 一线大厂中的大型App全套性能优化实战方案
    - https://github.com/liuyangbajin/Performance
- tts
    - https://github.com/gotev/android-speech
- 蓝牙
    - https://github.com/kongzue/BTLinker
    - https://github.com/Jasonchenlijian/FastBle
- Wi-Fi
    - https://github.com/kongzue/WifiLinker
    - https://github.com/kongqw/AndroidWiFiManager
    - https://github.com/leavesCZY/WifiFileTransfer
- 系统分享
    - https://www.jianshu.com/p/28f25dcf81cd



- 系列课程
    - https://juejin.cn/post/6844903577861750792
- 系列课程
    - https://blog.csdn.net/m0_37796683/category_10088599.html


- 【协程】ViewModelScope源码解析
    - https://blog.csdn.net/yechaoa/article/details/118165611
- https://github.com/hegaojian/JetpackMvvm
- Android实现程序前后台切换效果
    - https://blog.51cto.com/u_15329896/3412393

### 超级好文
- 优秀个人博主
    - https://juejin.cn/user/4318537403878167/posts
- Android系统服务-WMS
    - https://www.jianshu.com/p/4d34edb6b054
- 货拉拉SSL证书踩坑之旅
    - https://juejin.cn/post/7186837003026038843
- 货拉拉用户端体验优化--启动优化篇
    - https://juejin.cn/post/7087773731061235743
- 货拉拉Android稳定性治理
    - https://juejin.cn/post/7100743641953468452






### Java笔记
- Java编程思想
    - https://github.com/quanke/think-in-java
    - https://github.com/apachecn/thinking-in-java-zh



- 如何查看ANR信息？如何查看App发生ANR日志文件？
- 捕获异常，是如何执行到Thread中dispatchUncaughtException这个方法
- 什么是fd文件？
- 线程为何跟异常联系在一起。主线程能捕获子线程异常嘛。






备注：预约武汉看牙，具体未定。预约成功后插入计划之中

2月27日，周一
1.早上8点到武昌，10点半到洗马高中
2.下午3点从洗马出发到武汉，5点到东西湖新房
3.一起去盒马生鲜购物到6点，晚上7点多烛光晚餐

2月28日，周二
1.上午10点半到宜家荟聚中心商场【证件照】，服务是3小时，可现场自取
2.下午给媳妇看衣服(定制旗袍，江汉路刘伟章旗袍)
3.如果时间充足，打卡园博园，这几个地点都在一块

3月1日，周三
1.上午去薇拉了解拍婚纱照，试妆
2.下午武汉植物园看郁金香
3.预约傍晚去求婚，江边夜景，第三方布置

3月2日【农历2月11】，周四
1.领证：时间是10点30——11点，江汉区民政局【江汉区发展大道234号】
2.公证：东西湖区房产公证处
3.车辆过户：东西湖车管所
4.下午办完事情回洗马高中

总部地址：武汉市东西湖区四明路西湖广场3号楼1门7楼
电话：027-83895646、83212148
微信公众号：武汉市东西湖公证处
官网：http://www.whdxhgz.com/