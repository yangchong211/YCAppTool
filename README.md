#### 目录介绍
- 01.项目介绍
- 02.项目运行
- 03.项目部分介绍
- 04.项目组件化结构
- 05.项目版本更新
- 06.项目第三方库
- 07.项目遇到bug
- 08.项目截图展示
- 09.项目优化处理
- 10.组件化博客
- 11.App性能分析
- 12.其他介绍





### 01.项目介绍
#### 1.1 项目简介
- 项目整体架构模式采用：组件化+MVP+Rx+Retrofit+design+Dagger2+阿里VLayout+腾讯X5+腾讯bugly
- 包含的模块：wanAndroid【kotlin】+干货集中营+知乎日报+番茄Todo+微信精选新闻+豆瓣音乐电影小说+小说读书+简易记事本+搞笑视频+经典游戏+其他更多等等
- 此项目属于业余时间练手的项目，接口数据来源均来自网络，如果存在侵权情况，请第一时间告知。本项目仅做学习交流使用，API数据内容所有权归原作公司所有，请勿用于其他用途。
- 可以先下载apk运行到手机上看看效果，下载链接地址：
    - apk如下所示
    - ![image](https://upload-images.jianshu.io/upload_images/4432347-fb9217757051fe88.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
    - [组件化apk的下载地址](https://github.com/yangchong211/LifeHelper/tree/master/read/apk)



### 02.项目运行
- 运行环境要求
    - Android studio 版本需要在3.0之上，compileSdkVersion是28，gradle版本是3.2.1，gradle-wrapper是4.6
- 组件模式和集成模式如何切换
    - 默认模式下，都是属于library形式的组件【app作为空壳主工程依赖所有的组件】，如果想把某一个library形式的组件切换成一个独立可以run的application，则
    - 比如，我想把视频模块，也就是该项目中的video组件切换成可运行的项目。如果想了解组件化更多内容，可以着重看 04.项目组件化结构部分说明
    - 修改yc.gradle文件中，直接将对应的开关置为true即可，然后需要Sync一下，就可以运行该模块
        - 将isVideoApplication = true，就可以切换成application模块，与app主工程解绑，实际开发中，比如你负责这个模块，那么你运行的时候，直接编译该模块，不会编译整个项目而导致耗费大量时间。
        - 同理，设置成false，即可还原成library形式的组件，与app主工程绑定[也就是被主工程依赖]
        ```
        ext {
            isAndroidApplication = false  //玩Android模块开关，false:作为Lib组件存在， true:作为application存在
            isVideoApplication = true  //视频模块开关，false:作为Lib组件存在， true:作为application存在
            isNoteApplication = false  //记事本模块开关，false:作为Lib组件存在， true:作为application存在
            isGameApplication = false  //游戏模块开关，false:作为Lib组件存在， true:作为application存在
        ```
- 模块众多，如何统一修改第三方库，以及build.gradle中的配置信息
    - 已经抽取了公共的build.gradle，详细的代码，可以直接看项目根目录下的yc.gradle文件，修改版本即可
- 切换网络请求方式，比如公司正式项目就有：正式线上环境，测试环境，仿真环境等等
    - 那么如何统一切换呢，直接修改项目根目录下的url.properties文件即可。由于该项目众多接口是网络接口，因此该项目只是提供切换环境的思路代码。
        ```
        //修改TEST_URL即可，0测试环境，1仿真环境，2正式环境
        TEST_URL=0
        PREVIEW_URL=1
        RELEASE_URL=2
        ```
- 编译时间
    - 由于组件化模块众多，第一次编译时间会较长，请耐心等待。项目编译成功之后，后期可以较快运行项目。自己每次运行项目，大概是2到5分钟
    - 如果出现编译失败，可以看看该目录下7.1编译失败，记录常见编译错误。如果其他问题，大都可以谷歌解决。如果编译耗时，可以根据run build查看每个模块的编译时间【也就是找出编译耗时所在，作为程序员不仅要知道编译为何时间长，还要找出那个地方编译时间长】，所有组件(初基础公共组件外)均可和主工程app解绑。
    - ![image](https://upload-images.jianshu.io/upload_images/4432347-4198b15a178b6222.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
- 编译遇到问题
    - 看看7.1 编译报错bug，如果没有找到解决方案，建议谷歌查询一下问题。



### 03.项目部分介绍
#### 3.1 项目包含的模块
- 新闻部分（天行新闻，微信精选新闻，阿里云热门新闻，干货集中营新闻等等）
    - 干货集中营：包含福利，搜索，每日技术新闻，休息视频等众多模块
- 音乐部分（音乐播放器，自动搜索本地音乐文件，在线音乐是百度音乐api）
    - 播放本地音乐，网络音乐[支持下载，播放，分享]，搜索音乐
- 视频部分（视频播放器，自动搜索本地视频文件，还有许多网络搞笑视频）
- 图片部分（干货集中营美女图片），画廊浏览高清大图
- 豆瓣电影，音乐，读书（豆瓣接口）
- 简易ToDo记事本，番茄周，学习MVP+Dagger2时写的。
- 超文本笔记本，可以支持文字，图片，动态图混排，做便签十分方便，注意高清图片会压缩，目前笔记是保存本地
- 技术分享部分(鸿洋玩Android，还有代码家的干活集中营等等)，关于[flutter版本的极致体验玩Android客户端](https://github.com/yangchong211/ycflutter)
    - 玩Android，鸿洋大神的开放接口，
    - 首页轮播图+list：推荐最新的博客
    - 知识体系：对安卓知识体系做整理
    - 登录注册：登录、注册、Cookie持久化
    - 我的收藏页面：依靠Cookie持久化，实现对文章的收藏和展示
    - 项目分类：在WanAndroid上发布的项目
    - 网址导航：展示常用的开发网站
    - 搜索功能：输入搜索、搜索推荐、历史搜索等等
    - 关于我们：鸿洋wanAndroid介绍
- love爱意表达部分，程序员表白神器，简易含蓄的单身程序员可以看看该模块，可以给女朋友一个惊喜！
- 玩Android部分，接口是鸿洋大神开放的api，学习kotlin时所写
- markDown格式笔记本，支持md格式，数据是保存到本地。对于程序员，markDown是十分方便记录笔记
- 游戏部分，包括智慧拼图，还有童年飞机大战游戏，体验非常好玩。
- 其他部分，几乎融合了自己开源的大部分封装库，比如，状态管理，视频库，轮播图，幸运大转盘[老虎机]，画廊，自定义进度条，图片缩放，线程池



#### 3.2 相关特性说明
- 侧滑菜单：DrawerLayout+NavigationView
- 基本遵循Google Material Design设计风格
- 透明状态栏使用与版本适配
- 图片加载picasso，Glide加载监听，获取缓存，圆角图片，高斯模糊
- list条目点击水波纹效果
- CoordinatorLayout+Behavior实现标题栏渐变
- 自定义RecyclerView下拉刷新上拉加载，支持加载loading，空页面，异常界面，有数据界面状态切换
- 缓存使用Realm数据库，做数据的增删改查
- 状态管理库与Activity和Fragment结合，可以自由切换不同的状态


#### 3.3 项目优化点内容
- 项目代码规范；布局优化；代码优化；架构优化；内存泄漏优化；线程优化；Bitmap优化；网络优化；懒加载优化，启动页优化；静态变量优化；电量性能优化；view控件异常销毁保存重要信息优化；去除淡黄色警告优化；使用注解替代枚举优化；glide加速优化；多渠道打包优化状态管理切换优化；TrimMemory和LowMemory优化；轮询操作优化；去除重复依赖库优化；合理运用软引用和弱引用优化；加载loading优化；网络请求异常拦截优化
- 具体可以看目录09.项目优化处理部分内容！！！



### 04.项目组件化结构
#### 4.1 传统APP架构图
- 传统APP架构图
    - 如图所示，从网上摘来的……
    - ![image](http://upload-images.jianshu.io/upload_images/4432347-1047b1cdf15fd59a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
- 存在的问题
    - 普遍使用的 Android APP 技术架构，往往是在一个界面中存在大量的业务逻辑，而业务逻辑中充斥着各种网络请求、数据操作等行为，整个项目中也没有模块的概念，只有简单的以业务逻辑划分的文件夹，并且业务之间也是直接相互调用、高度耦合在一起的。单一工程模型下的业务关系，总的来说就是：你中有我，我中有你，相互依赖，无法分离。如下图：
    - ![image](https://upload-images.jianshu.io/upload_images/4432347-ab213414e69fef5a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#### 4.2 项目组件化结构
- 主工程：
    - 除了一些全局配置和主 Activity 之外，不包含任何业务代码。有的也叫做空壳app
- 业务组件：
    - 最上层的业务，每个组件表示一条完整的业务线，彼此之间互相独立。
    - 该案例中分为：干活集中营，玩Android，知乎日报，微信新闻，头条新闻，搞笑视频，百度音乐，我的记事本，豆瓣音乐读书电影，游戏组件等等。
- 功能组件：
    - 该案例中分为，分享组件，评论反馈组件，支付组件，画廊组件等等。同时注意，可能会涉及多个业务组件对某个功能组件进行依赖！
- 基础组件：
    - 支撑上层业务组件运行的基础业务服务。
    - 该案例中：在基础组件库中主要有，网络请求，图片加载，通信机制，工具类，分享功能，支付功能等等。当然，我把一些公共第三方库放到了这个基础组件中！


#### 4.3 项目组件化架构图，如下所示
- ![image](https://upload-images.jianshu.io/upload_images/4432347-7b3a2c6d4a583e05.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#### 4.4 组件通信是通过路由转发
- 传统以前工程下模块
    - 记得刚开始进入Android开发工作时，只有一个app主工程，后期几乎所有的需求都写在这个app主工程里面。只有简单的以业务逻辑划分的文件夹，并且业务之间也是直接相互调用、高度耦合在一起的。
    - 导致后期改项目为组件化的时候十分痛苦，不同模块之间的业务逻辑实在关联太多，但还是没办法，于是按照步骤一步步实践。终极目标是，告别结构臃肿，让各个业务变得相对独立，业务组件在组件模式下可以独立开发。
- 组件化模式下如何通信
    - 这是组件化工程模型下的业务关系，业务之间将不再直接引用和依赖，而是通过“路由”这样一个中转站间接产生联系。在这个开源项目中，我使用的阿里开源的路由框架。关于Arouter基础使用和代码分析，可以看我这篇博客：[Arouter使用与代码解析](https://github.com/yangchong211/YCBlogs)
    - ![image](https://upload-images.jianshu.io/upload_images/4432347-b68c6a71b703765a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#### 4.5 关于组件遇到的问题
- 那么问题有哪些呢？
    - 组件化时资源名冲突该怎么办？比如，color，shape，drawable，图片资源，布局资源，或者anim资源等等，都有可能造成资源名称冲突。这是为何了，有时候大家负责不同的模块，如果不是按照统一规范命名，则会偶发出现该问题。
    - 业务组件之间联动导致耦合严重？比如，实际开发中，购物车和首页商品分别是两个组件。但是遇到产品需求，比如过节做个活动，发个购物券之类的需求，由于购物车和商品详情页都有活动，因此会造成组件经常会发生联动。
    - 如何做到各个组件化模块能获取到全局上下文？
    - 组件在lib和app之间进行切换，如何处理butterKnife使用问题呢？
    - 当组件是lib时，写代码需要注意哪些语法？
    - 不要乱发bus消息？如果项目中大量的使用eventbus，那么会看到一个类中有大量的onEventMainThread()方法，写起来很爽，阅读起来很痛苦。
    - 页面跳转存在问题？比如，跳转页面需要登陆状态如何拦截，跳转页面传递参数该怎么办，程序意外跳转异常或者失败又该如何处理？
    - 使用Arouter注意事项有哪些？如何让代码变得更加容易让人维护？
    - 直接看我这篇博客：https://juejin.im/post/5c46e6fb6fb9a049a5713bcc


#### 4.6 存在待解决问题
- 动态的管理组件，所以给每个组件添加几个生命周期状态：加载、卸载和降维。为此我们给每个组件增加一个ApplicationLike类，里面定义了onCreate和onStop两个生命周期函数。
    - 看到网上有个方案说：主项目负责加载组件，由于主项目和组件之间是隔离的，那么主项目如何调用组件ApplicationLike的生命周期方法呢，目前采用的是基于编译期字节码插入的方式，扫描所有的ApplicationLike类（其有一个共同的父类），然后通过javassist在主项目的onCreate中插入调用ApplicationLike.onCreate的代码。那么思路有了，具体代码该如何实现？


#### 4.7 集成模式和组件模式
- 可以在yc.gradle文件自由设置切换模式
    - ![image](https://upload-images.jianshu.io/upload_images/4432347-46f929a998cb36cc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



#### 4.7 组件化中Fragment通信难点
- 在网上看到很多博客说，如何拆分组件，按模块拆分，或者按照功能拆分。但很少有提到fragment在拆分组件时的疑问，这个让我很奇怪。
- 先来说一个业务需求，比如一个购物商城app，有4个模块，做法一般是一个activity+4个fragment，这个大家都很熟悉，这四个模块分别是：首页，发现，购物车，我的。然后这几个页面是用fragment写的，共用一个宿主activity，那么在做组件化的时候，我想把它按照业务拆分成首页，发现，购物车和我的四个独立的业务模块。
- 遇到疑问：
    - 如果是拆分成四个独立的业务模块，那么对应的fragment肯定要放到对应的组件中，那么这样操作，当主工程与该业务组件解绑的情况下，如何拿到fragment和传递参数进行通信。
    - Fragment 中 开启Activity带requestCode，开启的Activity关闭后,不会回调Fragment中的onActivityResult。只会调用Fragment 所在Activity的onActivityResult。
    - 多fragment单activity拦截器不管用，难道只能用于拦截activity的跳转？那如果是要实现登录拦截的话，那不是只能在PathReplaceService中进行了？
- 网络解决办法
    - 第一个疑问：由于我使用阿里路由，所以我看到zhi1ong大佬说：用Router跳转到这个Activity，然后带一个参数进去，比方说tab=2，然后自己在onCreate里面自行切换。但后来尝试，还是想问问广大程序员有没有更好的办法。
    - 第二个疑问：还是zhi1ong大佬说，通过广播，或者在Activity中转发这个事件，比方说让Fragment统一依赖一个接口，然后在Activity中转发。


#### 4.8 组件化实践中的切换run
- 你完全可以采取拿来注意，将任意一个组件拿来即用即可。
    - ![image](https://upload-images.jianshu.io/upload_images/4432347-cc01601acabc6728.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



### 05.项目版本更新


### 06.项目第三方库




### 07.项目遇到bug
#### 7.1 编译报错bug
- **Caused by: org.gradle.tooling.BuildException: Failed to execute aapt**
    - 有时候，报这个错误没有明确指出具体的问题代码。网上有的说是检测.9图片出错，或者资源文件出错。但究竟是哪里出错，能否通过日志展示具体的位置，这样更方便排查问题。
    - 第一种解决方案
        - 网上有解决方案说，打开gradle.properties，添加如下内容：android.enableAapt2=false
        - 在2018年之后使用“android.enableAapt2=false”来关闭AAPT2是行不通的，这个方法已经过时
    - 第二种解决方案
        - 在命令行输入，gradlew compileDebugSources，可以查看打印报错的信息，这句话可以控制台输出代码报错的日志。
- **IOException: CreateProcess error=2, 系统找不到指定的文件。**
    - 具体报错日志如下所示
        ```
        IOException: Cannot run program "D:\Program File\AndroidSdk\ndk-bundle\toolchains\mips64el-linux-android-4.9\prebuilt\windows-x86_64\bin\mips64el-linux-android-strip" (in directory "D:\GitHub\LifeHelper\app"): CreateProcess error=2, 系统找不到指定的文件。
        ```
    - 原因分析：Android/Sdk/ndk-bundle/toolchains/mips64el-linux-android-4.9/prebuilt/linux-x86_64/bin/mips64el-linux-android-strip 找不到, 导致编译报错。ndk升级导致的，自己现在是17版本。
    - 第一种解决办法：找到工程目录下local.properties文件，在ndk-bundle后面添加.cmd即可运行
        ```
        ndk.dir=D\:\\Program File\\AndroidSdk\\ndk-bundle.cmd
        sdk.dir=D\:\\Program File\\AndroidSdk
        ```
    - 第二种解决办法：去官网下载一个16或者更低的版本。下载完成后，把16b版本toolchains\mips64el-linux-android-4.9\prebuilt\windows-x86_64的所有文件copy到r17中toolchains\mips64el-linux-android-4.9\prebuilt\windows-x86_64目录下也可以解决
- **Invoke-customs are only supported starting with Android O (–min-api 26)**
    - 错误: -source 1.7 中不支持 lambda 表达式，请使用 -source 8 或更高版本以启用 lambda 表达式
        ```
        android {
            //jdk1.8
            compileOptions {
                sourceCompatibility JavaVersion.VERSION_1_8
                targetCompatibility JavaVersion.VERSION_1_8
            }
        }
        ```
- Process 'command 'build-tools\28.0.3\aapt.exe'' finished with non-zero exit value 1
    - 在StackOverflow上发现有很多人遇到了这个错误，不过解决方法却各不相同。有的建议Clean然后Rebuild，有的建议修改使用内存，有的说是代码问题，也有的说是资源问题，比如本来是jpg图片或者.9图片，文件后缀却是png也会导致问题…
    - 需要定位错误点，在项目根路径，实际是控制台Terminal输入命令：gradlew processDebugResources --debug
    - 从Log中找到了输出的出错信息，这个方法很有效，经常遇到编译类错误，可以用它排除错误！
- Error:Execution ':app:transformClassesWithDexForDebug'.
    - 通过gradle命令查看重复依赖，稍等片刻，会出来一个树状图，其中用"->"会标示出冲突的部分，然后解决重复依赖即可
- DexArchiveMergerException异常：dexarchivemergeexception:合并dex存档时出错
    - 网上解决的方法有很多不同看法，比如：可能是64k引起的问题；可能是打包dex引起的问题；可能是jdk1.8新特性引起的问题；可能是jar包重复引用引起的问题
    ```
    重点异常信息：
    Caused by: com.android.builder.dexing.DexArchiveMergerException: Error while merging dex archives:
    Caused by: com.android.tools.r8.CompilationFailedException: Compilation failed to complete
    Caused by: com.android.tools.r8.utils.AbortException
    ```
    - 但是思考一下，为什么会出现这个问题，什么情况下会出现这个问题？Android Studio 3.0 及以上版本支持所有 Java 7 语言功能，以及部分 Java 8 语言功能（具体因平台版本而异）。
    - 引入D8作为原先Dex的升级版，升级Dex编译器将直接影响构建时间，.dex文件大小，运行时性能。Android Studio 3.0需要主动在gradle.properties文件中新增:android.enableD8=true
    - Android Studio 3.1或之后D8将会被作为默认的Dex编译器。如果遇到问题，你可以通过修改gradle.properties文件里的一个属性恢复到DX android.enableD8=false，除了其他好处外，使用D8还有一个好处，就是支持 脱糖，让Java 8才提供的特性（如lambdas）可以转换成Java 7特性。把脱糖步骤集成进D8影响了所有读或写.class字节码的开发工具，因为它会使用Java 8格式。你可以在gradle文件中设置一个属性，恢复到以前的行为，让脱糖发生在Java编译之后，.class字节码仍遵循Java 7格式：android.enableD8.desugaring = false
    - 所以解决方案如下所示
    ```
    #The option 'android.enableD8' is deprecated and should not be used anymore.
    #Use 'android.enableD8=true' to remove this warning.
    #It will be removed at the end of 2018..
    android.enableD8=false
    android.enableD8.desugaring = false
    ```
- kotlin和realm数据库配置问题
    - 错误配置【参考方案：https://github.com/realm/realm-java/issues/3139】
    ```
    apply plugin: 'com.android.application'
    apply plugin: 'realm-android'
    apply plugin: 'kotlin-android'
    apply plugin: 'kotlin-android-extensions'
    ```
    - 正确配置【注意：realm-android要在kotlin配置之后】
    ```
    apply plugin: 'com.android.application'
    apply plugin: 'kotlin-android'
    apply plugin: 'kotlin-android-extensions'
    apply plugin: 'realm-android'
    ```
- 出现其他编译类错误，可以直接谷歌搜索解决



#### 7.2 项目运行时bug
- 分别列举了实际开发中大部分的异常，主要包括1.异常Exception和2.异常Error
    - 针对开发中异常问题，大概记录的是：该板块是持续更新记录项目bug问题！！
    ```
    - A.详细崩溃日志信息
    - B.查看崩溃类信息
    - C.项目中异常分析
    - D.引发崩溃日志的流程分析
    - F.解决办法
    - G.其他延申
    ```
- [01.崩溃bug日志总结1](https://github.com/yangchong211/YCBlogs/blob/master/bug/01.%E5%B4%A9%E6%BA%83bug%E6%97%A5%E5%BF%97%E6%80%BB%E7%BB%931.md)
    - 1.1 java.lang.UnsatisfiedLinkError找不到so库异常
    - 1.2 java.lang.IllegalStateException非法状态异常
    - 1.3 android.content.res.Resources$NotFoundException
    - 1.4 java.lang.IllegalArgumentException参数不匹配异常
    - 1.5 IllegalStateException：Can't compress a recycled bitmap
    - 1.6 java.lang.NullPointerException空指针异常
    - 1.7 android.view.WindowManager$BadTokenException异常
    - 1.8 java.lang.ClassCastException类转化异常
    - 1.9 Toast运行在子线程问题，handler问题
- [02.崩溃bug日志总结2](https://github.com/yangchong211/YCBlogs/blob/master/bug/02.%E5%B4%A9%E6%BA%83bug%E6%97%A5%E5%BF%97%E6%80%BB%E7%BB%932.md)
    - 1.1 java.lang.ClassNotFoundException类找不到异常
    - 1.2 java.util.concurrent.TimeoutException连接超时崩溃
    - 1.3 java.lang.NumberFormatException格式转化错误
    - 1.4 java.lang.IllegalStateException: Fragment not attached to Activity
    - 1.5 ArrayIndexOutOfBoundsException 角标越界异常
    - 1.6 IllegalAccessException 方法中构造方法权限异常
    - 1.7 android.view.WindowManager$BadTokenException，dialog弹窗异常
    - 1.8 java.lang.NoClassDefFoundError 找不到类异常
    - 1.9 Android出现：Your project path contains non-ASCII characters.
- [03.崩溃bug日志总结3](https://github.com/yangchong211/YCBlogs/blob/master/bug/03.%E5%B4%A9%E6%BA%83bug%E6%97%A5%E5%BF%97%E6%80%BB%E7%BB%933.md)
    - 1.1 OnErrorNotImplementedException【 Can't create handler inside thread that has not called Looper.prepare()】
    - 1.2 adb.exe,start-server' failed -- run manually if necessary
    - 1.3 java.lang.IllegalStateException: ExpectedBEGIN_OBJECT but was STRING at line 1 column 1 path $
    - 1.4 android.content.ActivityNotFoundException: No Activity found to handle Intent
    - 1.5 Package manager has died导致崩溃
    - 1.6 IllegalArgumentException View添加窗口错误
    - 1.7 IllegalStateException: Not allowed to start service Intent异常崩溃
    - 1.8 java.lang.IllegalStateException：Can not perform this action after onSaveInstanceState
    - 1.9 在Fragment中通过getActivity找不到上下文，报null导致空指针异常
- [04.崩溃bug日志总结4]
    - 1.1 IllegalArgumentException导致崩溃【url地址传入非法参数，转义字符】
    - 1.2 ClassNotFoundException: Didn't find class "*****" on path: /data/app/**错误
    - 1.3 NoClassDefFoundError异常【该异常表示找不到类定义】
    - 1.5 java.util.concurrent.ExecutionException: com.android.tools.aapt2.Aapt2Exception



### 08.项目截图展示
#### 8.1 主页截图
![image](https://upload-images.jianshu.io/upload_images/4432347-b3395d138a12e477.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-d1229f8bb096a43e.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-d44fb59e7d05939f.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-c21d6ee6c01868b7.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-3f072cebbdc2a9e8.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-f2173a6b0446bc7f.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-db5b654a60e9e40f.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-8c85d9a86d07c2ef.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



#### 8.2 干活集中营图片
![image](https://upload-images.jianshu.io/upload_images/4432347-e1b3333b5ddbdbed.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-fb39504af7ea0b4d.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-42ba4cfab5c401c7.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-75771f15f18315b3.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-5e0bec10c6d5f9b2.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



#### 8.3 视频模块截图
![image](https://upload-images.jianshu.io/upload_images/4432347-5942faad77556155.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-1d1507eb02e9e118.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-535aec5f088ed91e.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-c2b61a83c1aaecba.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-f4776dfc42c94ebd.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-1b5870de5a3d3318.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#### 8.4 玩Android模块截图
![image](https://github.com/yangchong211/ycflutter/blob/master/iamge/1.jpg)
![image](https://github.com/yangchong211/ycflutter/blob/master/iamge/2.jpg)
![image](https://github.com/yangchong211/ycflutter/blob/master/iamge/3.jpg)
![image](https://github.com/yangchong211/ycflutter/blob/master/iamge/4.jpg)
![image](https://github.com/yangchong211/ycflutter/blob/master/iamge/5.jpg)
![image](https://github.com/yangchong211/ycflutter/blob/master/iamge/15.jpg)
![image](https://github.com/yangchong211/ycflutter/blob/master/iamge/6.jpg)
![image](https://github.com/yangchong211/ycflutter/blob/master/iamge/7.jpg)
![image](https://github.com/yangchong211/ycflutter/blob/master/iamge/8.jpg)
![image](https://github.com/yangchong211/ycflutter/blob/master/iamge/9.jpg)
![image](https://github.com/yangchong211/ycflutter/blob/master/iamge/10.jpg)
![image](https://github.com/yangchong211/ycflutter/blob/master/iamge/11.jpg)



#### 8.5 音乐模块截
![image](https://upload-images.jianshu.io/upload_images/4432347-85f27245ba907f7a.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-b6b218451a44d632.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-57469392a29e006a.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-4005a6556a535e9c.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-44bd0d3220ae7112.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#### 8.6 记事本模块截图



#### 8.7 知乎新闻模块截图
![image](https://upload-images.jianshu.io/upload_images/4432347-8d4527de18c448b0.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-42746c10f19dabcf.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-12b9c77fc1f054dd.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-c40b23e465a0ee20.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-fd88b0830961f534.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-aa735978d23a9841.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-0edc461b29a225cc.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-85a7c39b38dc2609.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



#### 8.8 游戏娱乐模块截图
![image](https://upload-images.jianshu.io/upload_images/4432347-090a3220b169c3da.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-bd879a412c71f1ef.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-1143cca8910865c0.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-9d0cd66352c65859.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-a4d89158984d7bdc.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


#### 8.9 豆瓣模块
![image](https://upload-images.jianshu.io/upload_images/4432347-8d6f455b462a4fc7.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-15050b6f1673dfae.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-1a6f1ca44296fec4.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-ffe294b42826b433.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-efc75287e992b1a7.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-d4aae41c712fe304.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![image](https://upload-images.jianshu.io/upload_images/4432347-2016749f9c5f6a92.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)





### 09.项目优化处理
#### 9.0.1 项目代码规范
- 类，常量，变量，id等命名规范；注释规范；分包规范；代码风格规范
- 注意，由于这块内容很常见，固写成了博客，具体更加详细规格可以参考博客：
    - 使用studio同学建议安装一下阿里编码规约插件，可以直接搜索plugins中搜索Alibaba Java Coding Guidelines即可安装。其GitHub开源项目地址是：[编码规范](https://github.com/alibaba/p3c)
    - 之前阿里分享过关于编码规范的文档，我自己也在此基础上总结了一些代码规范，可以看我这篇文章：[技术博客大总结](https://github.com/yangchong211/YCBlogs)



#### 9.0.2 布局优化
- 使用include标签
    - 比如标题栏actionBar，可以抽取出来。该布局几乎大多数activity都会用到！
- 可以使用ViewStub
    - 这个标签最大的优点是当你需要时才会加载，使用他并不会影响UI初始化时的性能。各种不常用的布局想进度条、显示错误消息等可以使用这个标签，以减少内存使用量，加快渲染速度。
- 视图层级`<merge/>`
    - 这个标签在UI的结构优化中起着非常重要的作用，它可以删减多余的层级，优化UI。但是就有一点不好，无法预览布局效果！
- 自定义全局的状态管理器
    - 针对多状态，有数据，空数据，加载失败，加载异常，网络异常等。针对空数据，加载失败，异常使用viewStub布局，一键设置自定义布局，也是优化的一种
    - 更多可以看我的开源库：[可以切换状态的recyclerView封装库](https://github.com/yangchong211/YCRefreshView)，[可以切换状态的管理器](https://github.com/yangchong211/YCStateLayout)


#### 9.0.3 代码优化
- lint去除无效资源和代码
    - 如何检测哪些图片未被使用
        - 点击菜单栏 Analyze -> Run Inspection by Name -> unused resources -> Moudule ‘app’ -> OK，这样会搜出来哪些未被使用到未使用到xml和图片，如下：
    - 如何检测哪些无效代码
        - 使用Android Studio的Lint，步骤：点击菜单栏 Analyze -> Run Inspection by Name -> unused declaration -> Moudule ‘app’ -> OK



#### 9.0.4 架构优化



#### 9.0.5 内存泄漏优化
- 这个是一个长期在优化的东西，即使你熟悉每一种内存泄漏的场景，也很难彻底解决它。
	* 0.1 错误使用单例造成的内存泄漏
	* 0.2 错误使用静态变量，导致引用后无法销毁
	* 0.3 [**常见**]Handler使用不当造成的内存泄漏
	* 0.4 线程造成的内存泄漏[比较少见]
	* 0.5 非静态内部类创建静态实例造成的内存泄漏
	* 0.6 不需要用的监听未移除会发生内存泄露
	* 0.7 [**常见**]资源未关闭造成的内存泄漏
	* 0.8 未注销EventBus导致的内存泄漏
	* 0.9 [**常见**]持有activity引用未被释放导致内存泄漏
	* 1.0 静态集合使用不当导致的内存泄漏
	* 1.1 动画资源未释放导致内存泄漏
	* 1.2 系统bug之InputMethodManager导致内存泄漏
    * 更加详细的问题分析，及出现场景分析，及解决办法等：[技术博客大总结](https://github.com/yangchong211/YCBlogs)



#### 9.0.6 线程优化
- 将全局线程用线程池管理
    - 直接创建Thread实现runnable方法的弊端
        - 大量的线程的创建和销毁很容易导致GC频繁的执行，从而发生内存抖动现象，而发生了内存抖动，对于移动端来说，最大的影响就是造成界面卡顿
        - 线程的创建和销毁都需要时间，当有大量的线程创建和销毁时，那么这些时间的消耗则比较明显，将导致性能上的缺失
    - 为什么要用线程池
        - 重用线程池中的线程，避免频繁地创建和销毁线程带来的性能消耗；有效控制线程的最大并发数量，防止线程过大导致抢占资源造成系统阻塞；可以对线程进行一定地管理。
    - 使用线程池管理的经典例子
        - RxJava，RxAndroid，底层对线程池的封装管理特别值得参考
    - 关于线程池，线程，多线程的具体内容
        - 参考：[轻量级线程池封装库，支持异步回调，可以检测线程执行的状态](https://github.com/yangchong211/YCThreadPool)
        - 该项目中哪里用到频繁new Thread
            - 保存图片[注意，尤其是大图和多图场景下注意耗时太久]；某些页面从数据库查询数据；设置中心清除图片，视频，下载文件，日志，系统缓存等缓存内容
            - 使用线程池管理库好处，比如保存图片，耗时操作放到子线程中，处理过程中，可以检测到执行开始，异常，成功，失败等多种状态。


#### 9.0.7 Bitmap优化
- 加载图片所占的内存大小计算方式
    - 加载网络图片：bitmap内存大小 = 图片长度 x 图片宽度 x 单位像素占用的字节数【看到网上很多都是这样写的，但是不全面】
    - 加载本地图片：bitmap内存大小 = width * height * nTargetDensity/inDensity 一个像素所占的内存。注意不要忽略了一个影响项：Density
- 第一种加载图片优化处理：压缩图片
    - 质量压缩方法：在保持像素的前提下改变图片的位深及透明度等，来达到压缩图片的目的，这样适合去传递二进制的图片数据，比如分享图片，要传入二进制数据过去，限制500kb之内。
    - 采样率压缩方法：设置inSampleSize的值(int类型)后，假如设为n，则宽和高都为原来的1/n，宽高都减少，内存降低。
    - 缩放法压缩：Android中使用Matrix对图像进行缩放、旋转、平移、斜切等变换的。功能十分强大！
- 第二种加载图片优化：不压缩加载高清图片如何做？
    - 使用BitmapRegionDecoder，主要用于显示图片的某一块矩形区域，如果你需要显示某个图片的指定区域，那么这个类非常合适。
- 关于bitmap更多优化可以看我的这篇博客：[技术博客大总结](https://github.com/yangchong211/YCBlogs)



#### 9.0.8 网络优化
- 图片网络优化
    - 比如我之前看到豆瓣接口，提供一种加载图片方式特别好。接口返回图片的数据有三种，一种是高清大图，一种是正常图片，一种是缩略小图。当用户处于wifi下给控件设置高清大图，当4g或者3g模式下加载正常图片，当弱网条件下加载缩略图【也称与加载图】。
- 网络缓存处理




#### 9.0.9 懒加载优化
- 该优化在新闻类app中十分常见
    - ViewPager+Fragment的搭配在日常开发中也比较常见，可用于切换展示不同类别的页面。
    - 懒加载,其实也就是延迟加载,就是等到该页面的UI展示给用户时,再加载该页面的数据(从网络、数据库等),而不是依靠ViewPager预加载机制提前加载两三个，甚至更多页面的数据。这样可以提高所属Activity的初始化速度,也可以为用户节省流量.而这种懒加载的方式也已经/正在被诸多APP所采用。




#### 9.1.0 启动页优化
- 启动页白屏优化
    - 为什么存在这个问题？
        - 当系统启动一个APP时，zygote进程会首先创建一个新的进程去运行这个APP，但是进程的创建是需要时间的，在创建完成之前，界面是呈现假死状态，于是系统根据你的manifest文件设置的主题颜色的不同来展示一个白屏或者黑屏。而这个黑（白）屏正式的称呼应该是Preview Window，即预览窗口。
        - 实际上就是是activity默认的主题中的android:windowBackground为白色或者黑色导致的。
        - 总结来说启动顺序就是：app启动——Preview Window(也称为预览窗口)——启动页
    - 解决办法
        - 常见有三种，这里解决办法是给当前启动页添加一个有背景的style样式，然后SplashActivity引用当前theme主题，注意在该页面将window的背景图设置为空！
        - 更多关于启动页为什么白屏闪屏，以及不同解决办法，可以看我这篇博客：[App启动页面优化](https://github.com/yangchong211/YCBlogs/blob/master/android/%E4%BC%98%E5%8C%96%E7%9B%B8%E5%85%B3/07.App%E5%90%AF%E5%8A%A8%E9%A1%B5%E9%9D%A2%E4%BC%98%E5%8C%96.md)
- 启动时间优化
    - IntentService子线程分担部分初始化工作
        - 现在application初始化内容有：阿里云推送初始化，腾讯bugly初始化，im初始化，神策初始化，内存泄漏工具初始化，头条适配方案初始化，阿里云热修复……等等。将部分逻辑放到IntentService中处理，可以缩短很多时间。
        - 开启IntentSerVice线程，将部分逻辑和耗时的初始化操作放到这里处理，可以减少application初始化时间
        - 关于IntentService使用和源码分析，性能分析等可以参考博客：[IntentService源码分析](https://github.com/yangchong211/YCBlogs/blob/master/android/%E5%A4%9A%E7%BA%BF%E7%A8%8B/04.IntentService%E6%BA%90%E7%A0%81%E5%88%86%E6%9E%90.md)


#### 9.1.1 静态变量优化
- 尽量不使用静态变量保存核心数据。这是为什么呢？
      - 这是因为android的进程并不是安全的，包括application对象以及静态变量在内的进程级别变量并不会一直呆着内存里面，因为它很有会被kill掉。
      - 当被kill掉之后，实际上app不会重新开始启动。Android系统会创建一个新的Application对象，然后启动上次用户离开时的activity以造成这个app从来没有被kill掉的假象。而这时候静态变量等数据由于进程已经被杀死而被初始化，所以就有了不推荐在静态变量（包括Application中保存全局数据静态数据）的观点。



#### 9.1.2 电量性能优化



#### 9.1.3 view控件异常销毁保存重要信息优化
- view自定义控件异常销毁保存状态
    - 经常容易被人忽略，但是为了追求高质量代码，这个也有必要加上。举个例子！
        ```
        @Override
        protected Parcelable onSaveInstanceState() {
            //异常情况保存重要信息。
            //return super.onSaveInstanceState();
            final Bundle bundle = new Bundle();
            bundle.putInt("selectedPosition",selectedPosition);
            bundle.putInt("flingSpeed",mFlingSpeed);
            bundle.putInt("orientation",orientation);
            return bundle;
        }

        @Override
        protected void onRestoreInstanceState(Parcelable state) {
            if (state instanceof Bundle) {
                final Bundle bundle = (Bundle) state;
                selectedPosition = bundle.getInt("selectedPosition",selectedPosition);
                mFlingSpeed = bundle.getInt("flingSpeed",mFlingSpeed);
                orientation = bundle.getInt("orientation",orientation);
                return;
            }
            super.onRestoreInstanceState(state);
        }
        ```


#### 9.1.4 去除淡黄色警告优化
- 淡黄色警告虽然不会造成崩溃，但是作为程序员还是要尽量去除淡黄色警告，规范代码
    - ![image](https://upload-images.jianshu.io/upload_images/4432347-67bafa5a6af6cbc6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



#### 9.1.5 使用注解替代枚举优化
- 使用注解限定传入类型
    - 比如，尤其是写第三方开源库，对于有些暴露给开发者的方法，需要限定传入类型是有必要的。举个例子：
    - 刚开始的代码
        ```
        /**
         * 设置播放器类型，必须设置
         * 注意：感谢某人建议，这里限定了传入值类型
         * 输入值：111   或者  222
         * @param playerType IjkPlayer or MediaPlayer.
         */
        public void setPlayerType(int playerType) {
            mPlayerType = playerType;
        }
        ```
    - 优化后的代码，有效避免第一种方式开发者传入值错误
        ```
        /**
         * 设置播放器类型，必须设置
         * 注意：感谢某人建议，这里限定了传入值类型
         * 输入值：ConstantKeys.IjkPlayerType.TYPE_IJK   或者  ConstantKeys.IjkPlayerType.TYPE_NATIVE
         * @param playerType IjkPlayer or MediaPlayer.
         */
        public void setPlayerType(@ConstantKeys.PlayerType int playerType) {
            mPlayerType = playerType;
        }

        /**
         * 通过注解限定类型
         * TYPE_IJK                 IjkPlayer，基于IjkPlayer封装播放器
         * TYPE_NATIVE              MediaPlayer，基于原生自带的播放器控件
         */
        @Retention(RetentionPolicy.SOURCE)
        public @interface IjkPlayerType {
            int TYPE_IJK = 111;
            int TYPE_NATIVE = 222;
        }
        @IntDef({IjkPlayerType.TYPE_IJK,IjkPlayerType.TYPE_NATIVE})
        public @interface PlayerType{}
        ```
- 使用注解替代枚举，代码如下所示
    ```
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewStateType {
        int HAVE_DATA = 1;
        int EMPTY_DATA = 2;
        int ERROR_DATA = 3;
        int ERROR_NETWORK = 4;
    }
    ```





#### 9.1.6 glide加速优化
- 在画廊中加载大图
    - 假如你滑动特别快，glide加载优化就显得非常重要呢，具体优化方法如下所示
        ```
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LoggerUtils.e("initRecyclerView"+ "恢复Glide加载图片");
                    Glide.with(ImageBrowseActivity.this).resumeRequests();
                }else {
                    LoggerUtils.e("initRecyclerView"+"禁止Glide加载图片");
                    Glide.with(ImageBrowseActivity.this).pauseRequests();
                }
            }
        });
        ```



#### 9.1.7 多渠道打包优化
- 还在手动打包吗？尝试一下python自动化打包吧……
    - 瓦力多渠道打包的Python脚本测试工具，通过该自动化脚本，自需要run一下或者命令行运行脚本即可实现美团瓦力多渠道打包，打包速度很快。配置信息十分简单，代码中已经注释十分详细。可以自定义输出文件路径，可以修改多渠道配置信息，简单实用。 项目地址：https://github.com/yangchong211/YCWalleHelper



#### 9.1.8 WebView优化



#### 9.1.9 状态管理切换优化
- 以前做法：
    - 直接把这些界面include到main界面中，然后动态去切换界面，后来发现这样处理不容易复用到其他项目中，而且在activity中处理这些状态的显示和隐藏比较乱
    - 利用子类继承父类特性，在父类中写切换状态，但有些界面如果没有继承父类，又该如何处理
    - 或者写一个工具类，动态切换不同的状态，但还是感觉耦合性实在太强。比如说，现在我有不同的页面需要展示不同的空页面状态，感觉就比较麻烦呢！
- 现在做法：
    - 让View状态的切换和Activity彻底分离开，必须把这些状态View都封装到一个管理类中，然后暴露出几个方法来实现View之间的切换。
    - 在不同的项目中可以需要的View也不一样，所以考虑把管理类设计成builder模式来自由的添加需要的状态View。具体案例看我：[状态管理器封装库](https://github.com/yangchong211/YCStateLayout)



#### 9.2.0 TrimMemory和LowMemory优化
- 可以优化什么？
    - 在 onTrimMemory() 回调中，应该在一些状态下清理掉不重要的内存资源。对于这些缓存，只要是读进内存内的都算，例如最常见的图片缓存、文件缓存等。拿图片缓存来说，市场上，常规的图片加载库，一般而言都是三级缓存，所以在内存吃紧的时候，我们就应该优先清理掉这部分图片缓存，毕竟图片是吃内存大户，而且再次回来的时候，虽然内存中的资源被回收掉了，依然可以从磁盘或者网络上恢复它。
- 大概的思路如下所示
    - 在lowMemory的时候，调用Glide.cleanMemory()清理掉所有的内存缓存。
    - 在App被置换到后台的时候，调用Glide.cleanMemory()清理掉所有的内存缓存。
    - 在其它情况的onTrimMemory()回调中，直接调用Glide.trimMemory()方法来交给Glide处理内存情况。



#### 9.2.1 轮询操作优化
- 什么叫轮训请求？
    - 简单理解就是App端每隔一定的时间重复请求的操作就叫做轮训请求，比如：App端每隔一段时间上报一次定位信息，App端每隔一段时间拉去一次用户状态等，这些应该都是轮训请求。比如，电商类项目，某个抽奖活动页面，隔1分钟调用一次接口，弹出一些获奖人信息，你应该某个阶段看过这类轮询操作！
- 具体优化操作
    - 长连接并不是稳定的可靠的，而执行轮训操作的时候一般都是要稳定的网络请求，而且轮训操作一般都是有生命周期的，即在一定的生命周期内执行轮训操作，而长连接一般都是整个进程生命周期的，所以从这方面讲也不太适合。
    - 建议在service中做轮询操作，轮询请求接口，具体做法和注意要点，可以直接看该项目代码。看app包下的LoopRequestService类即可。
    - 大概思路：当用户打开这个页面的时候初始化TimerTask对象，每个一分钟请求一次服务器拉取订单信息并更新UI，当用户离开页面的时候清除TimerTask对象，即取消轮训请求操作。


#### 9.2.2 去除重复依赖库优化
- 我相信你看到了这里会有疑问，网上有许多博客作了这方面说明。但是我在这里想说，如何查找自己项目的所有依赖关系树
    - 注意要点：其中app就是项目mudule名字。 正常情况下就是app！
    ```
    gradlew app:dependencies
    ```
- 关于依赖关系树的结构图如下所示，此处省略很多代码
    ```
    |    |    |    |    |    |    \--- android.arch.core:common:1.1.1 (*)
    |    |    |    |         \--- com.android.support:support-annotations:26.1.0 -> 28.0.0
    |    +--- com.journeyapps:zxing-android-embedded:3.6.0
    |    |    +--- com.google.zxing:core:3.3.2
    |    |    \--- com.android.support:support-v4:25.3.1
    |    |         +--- com.android.support:support-compat:25.3.1 -> 28.0.0 (*)
    |    |         +--- com.android.support:support-media-compat:25.3.1
    |    |         |    +--- com.android.support:support-annotations:25.3.1 -> 28.0.0
    |    |         |    \--- com.android.support:support-compat:25.3.1 -> 28.0.0 (*)
    |    |         +--- com.android.support:support-core-utils:25.3.1 -> 28.0.0 (*)
    |    |         +--- com.android.support:support-core-ui:25.3.1 -> 28.0.0 (*)
    |    |         \--- com.android.support:support-fragment:25.3.1 -> 28.0.0 (*)
    \--- com.android.support:multidex:1.0.2 -> 1.0.3
    ```
- 然后查看哪些重复jar
    - ![image](https://upload-images.jianshu.io/upload_images/4432347-686690b44fb92dca.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
- 然后修改gradle配置代码
    ```
    api (rootProject.ext.dependencies["zxing"]){
        exclude module: 'support-v4'
        exclude module: 'appcompat-v7'
    }
    ```


#### 9.2.3 四种引用优化【灵活运用软引用和弱引用】
- 软引用使用场景
    - **正常是用来处理大图片这种占用内存大的情况**
        - 代码如下所示
        ```
        Bitmap bitmap = bitmaps.get(position);
        //正常是用来处理图片这种占用内存大的情况
        bitmapSoftReference = new SoftReference<>(bitmap);
        if(bitmapSoftReference.get() != null) {
            viewHolder.imageView.setImageBitmap(bitmapSoftReference.get());
        }
        //其实看glide底层源码可知，也做了相关软引用的操作
        ```
    - **这样使用软引用好处**
        - 通过软引用的get()方法，取得bitmap对象实例的强引用，发现对象被未回收。在GC在内存充足的情况下，不会回收软引用对象。此时view的背景显示
        - 实际情况中,我们会获取很多图片.然后可能给很多个view展示, 这种情况下很容易内存吃紧导致oom,内存吃紧，系统开始会GC。这次GC后，bitmapSoftReference.get()不再返回bitmap对象，而是返回null，这时屏幕上背景图不显示，说明在系统内存紧张的情况下，软引用被回收。
        - 使用软引用以后，在OutOfMemory异常发生之前，这些缓存的图片资源的内存空间可以被释放掉的，从而避免内存达到上限，避免Crash发生。
- 弱引用使用场景
    - 弱引用–>随时可能会被垃圾回收器回收，不一定要等到虚拟机内存不足时才强制回收。
    - 对于使用频次少的对象，希望尽快回收，使用弱引用可以保证内存被虚拟机回收。比如handler，如果希望使用完后尽快回收，看下面代码
    ```
    private MyHandler handler = new MyHandler(this);
    private static class MyHandler extends Handler{
        WeakReference<FirstActivity> weakReference;
        MyHandler(FirstActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
            }
        }
    }
    ```
- 到底什么时候使用软引用，什么时候使用弱引用呢？
    - 个人认为，如果只是想避免OutOfMemory异常的发生，则可以使用软引用。如果对于应用的性能更在意，想尽快回收一些占用内存比较大的对象，则可以使用弱引用。
    - 还有就是可以根据对象是否经常使用来判断。如果该对象可能会经常使用的，就尽量用软引用。如果该对象不被使用的可能性更大些，就可以用弱引用。



#### 9.2.4 加载loading优化
- 一般实际开发中会至少有两种loading
    - 第一种是从A页面进入B页面时的加载loading，这个时候特点是显示loading的时候，页面是纯白色的，加载完数据后才显示内容页面。
    - 第二种是在某个页面操作某种逻辑，比如某些耗时操作，这个时候是局部loading[一般用个帧动画或者补间动画]，由于使用频繁，因为建议在销毁弹窗时，添加销毁动画的操作。



#### 9.2.5 网络请求异常拦截优化
- 在获取数据的流程中，访问接口和解析数据时都有可能会出错，我们可以通过拦截器在这两层拦截错误。
    - 1.在访问接口时，我们不用设置拦截器，因为一旦出现错误，Retrofit会自动抛出异常。比如，常见请求异常404，500，503等等。
    - 2.在解析数据时，我们设置一个拦截器，判断Result里面的code是否为成功，如果不成功，则要根据与服务器约定好的错误码来抛出对应的异常。比如，token失效，禁用同账号登陆多台设备，缺少参数，参数传递异常等等。
    - 3.除此以外，为了我们要尽量避免在View层对错误进行判断，处理，我们必须还要设置一个拦截器，拦截onError事件，然后使用ExceptionUtils，让其根据错误类型来分别处理。
    - 具体可以直接看lib中的ExceptionUtils类，那么如何调用呢？入侵性极低，不用改变之前的代码！
    ```
    @Override
    public void onError(Throwable e) {
        //直接调用即可
        ExceptionUtils.handleException(e);
    }
    ```


#### 9.2.6 对象池Pools优化频繁创建和销毁对象
- 使用对象池，可以防止频繁创建和销毁对象而出现内存抖动
    - 在某些时候，我们需要频繁使用一些临时对象，如果每次使用的时候都申请新的资源，很有可能会引发频繁的 gc 而影响应用的流畅性。这个时候如果对象有明确的生命周期，那么就可以通过定义一个对象池来高效的完成复用对象。
    - 具体参考案例，可以看该项目：https://github.com/yangchong211/YCZoomImage




### 10.组件化博客
#### [Android组件化开发实践和案例分享](https://juejin.im/post/5c46e6fb6fb9a049a5713bcc)



### 11.App性能分析
#### 11.1 页面卡顿分析
- Android卡顿原理
    - 通过对Android绘制机制的了解，我们知道造成应用卡顿的根源就在于16ms内不能完成绘制渲染合成过程，因为Android平台的硬件刷新率为60HZ，大概就是16ms刷新一次。
    - 如果没能在16ms内完成这个过程，就会使屏幕重复显示上一帧的内容，即造成了卡顿。在这16ms内，需要完成视图树的所有测量、布局、绘制渲染及合成。
- 避免复杂的视图树
    - 如果视图树复杂，会使整个Traversal过程变长。在开发过程中要控制视图树的复杂程度。减少不必要的层级嵌套。比如使用RelativeLayout可以减少复杂布局的嵌套。
- 避免频繁的requestLayout()
    - 如果频繁的触发requestLayout()，就可能会导致在一帧的周期内，频繁的发生布局计算，这也会导致整个Traversal过程变长。有的ViewGroup类型的控件，比如RelativeLayout，在一帧的周期内会通过两次layout()操作来计算确认子View的位置，这种少量的操作并不会引起能够被注意到的性能问题。
    - 但是如果在一帧的周期内频繁的发生layout()计算，就会导致严重的性能，每次计算都是要消耗时间的！而requestLayout()操作，会向ViewRootImpl中一个名为mLayoutRequesters的List集合里添加需要重新Layout的View，这些View将在下一帧中全部重新layout()一遍。通常在一个控件加载之后，如果没什么变化的话，它不会在每次的刷新中都重新layout()一次，因为这是一个费时的计算过程。所以，如果每一帧都有许多View需要进行layout()操作，可想而知你的界面将会卡到爆。
    - 需要注意，setLayoutParams()最终也会调用requestLayout()，所以也不能烂用！尤其是在滑动改变控件透明度或者位置的时候，避免频繁调用它。
- UI线程被阻塞
    - 如果UI线程受到阻塞，显而易见的是，Traversal过程也将受阻塞！画面卡顿是妥妥的发生啊。这就是为什么大家一直在强调不要在UI线程做耗时操作的原因。通常UI线程的阻塞和以下原因脱不了关系。
    - 在UI线程中进行IO读写数据的操作。把IO操作统统放到子线程中去。
    - 在UI线程中进行复杂的运算操作。运算本身是一个耗时的操作，当然简单的运算几乎瞬间完成，所以不会让你感受到它在耗时。但是对于十分复杂的运算，把复杂的运算操作放到子线程中去。
    - 在UI线程中进行复杂的数据处理。比如数据的加密、解密、编码等等。这些操作都需要进行复杂运算，特别是在数据比较复杂的时候。如果不想获得一个卡到爆的App的话，把复杂数据的处理工作放到子线程中去。
    - 频繁的发生GC，导致UI线程被频繁中断。在Java中，发生GC(垃圾回收)意味着Stop-The-World，就是说其它线程全部会被暂停啊。好可怕！正常的GC导致偶然的画面卡顿是可以接受的，但是频繁发生就让人很蛋疼了！频繁GC的罪魁祸首是内存抖动。简单的说就是在短时间内频繁的创建大量对象，导致达到GC的阀值，然后GC就发生了。如果不想获得一个卡到爆的App的话，把内存的管理做好，即使这是Java。






#### 11.2 内存抖动
- 什么是内存抖动？
    - 是由于短时间内有大量对象进出Young Generiation区导致的，它伴随着频繁的GC。在Java内存管理机制中我提到过内存抖动会引起频繁的GC，从而使UI线程被频繁阻塞，导致画面卡顿。
- 避免发生内存抖动的几点建议：
    - 尽量避免在循环体内创建对象，应该把对象创建移到循环体外。
    - 注意自定义View的onDraw()方法会被频繁调用，所以在这里面不应该频繁的创建对象。
    - 当需要大量使用Bitmap的时候，试着把它们缓存在数组中实现复用。
    - 对于能够复用的对象，同理可以使用对象池将它们缓存起来。
- 内存抖动是由于大量对象在短时间内被配置而引起的，所以要做的就是谨慎对待那些可能会大量创建对象的情况。
    - 这块可以看我的这个开源项目，有效使用对象池避免对象大量创建。[图片缩放控件](https://github.com/yangchong211/YCGallery)






### 12.其他介绍
#### 00.关于其他内容介绍
![image](https://upload-images.jianshu.io/upload_images/4432347-7100c8e5a455c3ee.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



#### 01.关于博客汇总链接
- 1.[技术博客汇总](https://www.jianshu.com/p/614cb839182c)
- 2.[开源项目汇总](https://blog.csdn.net/m0_37700275/article/details/80863574)
- 3.[生活博客汇总](https://blog.csdn.net/m0_37700275/article/details/79832978)
- 4.[喜马拉雅音频汇总](https://www.jianshu.com/p/f665de16d1eb)
- 5.[其他汇总](https://www.jianshu.com/p/53017c3fc75d)



#### 02.关于我的博客
- 我的个人站点：www.yczbj.org，www.ycbjie.cn
- github：https://github.com/yangchong211
- 知乎：https://www.zhihu.com/people/yczbj/posts
- 简书：http://www.jianshu.com/u/b7b2c6ed9284
- csdn：http://my.csdn.net/m0_37700275
- 喜马拉雅听书：http://www.ximalaya.com/zhubo/71989305/
- 开源中国：https://my.oschina.net/zbj1618/blog
- 泡在网上的日子：http://www.jcodecraeer.com/member/content_list.php?channelid=1
- 邮箱：yangchong211@163.com
- 阿里云博客：https://yq.aliyun.com/users/article?spm=5176.100- 239.headeruserinfo.3.dT4bcV
- segmentfault头条：https://segmentfault.com/u/xiangjianyu/articles
- 掘金：https://juejin.im/user/5939433efe88c2006afa0c6e



#### 03.其他封装库推荐
- [0.flutter版本wanAndroid客户端](https://github.com/yangchong211/ycflutter)
- [1.开源博客汇总，汇总了几乎所有的博客，长期更新，持续维护，不断提高质量](https://github.com/yangchong211/YCBlogs)
- [2.开源综合案例，16年写的小案例，包含新闻，todo，记事本，知乎新闻，干货集中营等等](https://github.com/yangchong211/LifeHelper)
- [3.视频播放器封装库，支持常用功能，基于ijkPlayer，封装架构清晰，注释详细，十分方便上手](https://github.com/yangchong211/YCVideoPlayer)
- [4.状态切换管理器封装库，状态切换，让View状态的切换和Activity彻底分离开，入侵性很低，耦合度低](https://github.com/yangchong211/YCStateLayout)
- [5.复杂类型type封装库，自定义支持上拉加载更多，下拉刷新，支持自由切换状态，支持复杂页面](https://github.com/yangchong211/YCRefreshView)
- [6.弹窗封装库，包括Toast，SnackBar，Dialog，DialogFragment，PopupWindow](https://github.com/yangchong211/YCDialog)
- [7.版本更新封装库，轻量级版本更新弹窗，弹窗上支持更新进度条，可以设置普通更新或者强制更新。](https://github.com/yangchong211/YCUpdateApp)
- [8.状态栏封装库，一行代码设置状态栏，适合于绝大多数的使用场景](https://github.com/yangchong211/YCStatusBar)
- [9.开源轻量级线程池封装库，轻量级线程池封装库，支持线程执行过程中状态回调监测；支持创建异步任务](https://github.com/yangchong211/YCThreadPool)
- [10.开源轮播图封装库，ViewPager轮播图，RecyclerView轮播图](https://github.com/yangchong211/YCBanner)
- [11.开源音频播放器，17年上半年学习音频所写的案例](https://github.com/yangchong211/YCAudioPlayer)
- [12.开源画廊与图片缩放控件，支持ViewPager滑动的画廊控件，使用RecyclerView实现画廊浏览](https://github.com/yangchong211/YCGallery)
- [13.Python多渠道打包，Android程序员掌握python自动化打包更加高效，非常方便](https://github.com/yangchong211/YCWalleHelper)
- [14.list页面item整体侧滑动画封装库](https://github.com/yangchong211/YCSlideView)
- [15.Python爬虫妹子图，尽是干货，学习python爬虫案例](https://github.com/yangchong211/YCMeiZiTu)
- [17.自定义百分比进度条，支持圆环，直线等百分比进度条](https://github.com/yangchong211/YCProgress)
- [18.注解学习案例，全面通过系列博客，和开源代码，更加深入理解和运用注解提高开发效率](https://github.com/yangchong211/YCApt)



### 04.勘误及提问
- 如果有疑问或者发现错误，可以在相应的 issues 进行提问或勘误。
- 如果喜欢或者有所启发，欢迎star，对作者也是一种鼓励。转载麻烦注明出处。请挂上“潇湘剑雨”的小名！



#### 05.关于LICENSE
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```














