# 目录介绍
* 1.关于前言介绍
* 2.关于接口说明
* 3.关于模块说明
* 4.关于相关特性说明
* 5.关于部分功能说明
* 6.关于开源项目说明
* 7.关于参考项目说明
* 8.关于版本更新说明
- v1.0版本【2016年3月-2017年8月】
- v1.2版本【9月16日】
- v1.3版本【10月31日】
- v1.4版本【11月13日】
- v1.5版本【11月26日】
- v1.6版本【12月12日】
- v1.7版本【18年1月5日】
* 9.关于获取更多信息
* 10.App图片展示
* 11.关于其他说明
* 12.关于云测试情况
* 13.关于bug管理说明
* 14.关于我的博客


### 0.关于博客笔记大整理，持续更新迭代
- 可以直接看：[博客笔记](http://www.jianshu.com/p/53017c3fc75d)
- **如果你感觉还行，请给一个star，如果你觉得哪里有问题，也可以直接把问题提给我，我会修改的。业余的小案例，定期更新，持续更新**


### 1.关于前言介绍
- 关于开发的APP有：[投资界](http://openbox.mobilem.360.cn/index/d/sid/3583538)，[新芽](http://openbox.mobilem.360.cn/index/d/sid/3425699)，可以在应用市场上下载
- 此项目属于业余时间练手的项目，接口数据来源均来自网络，自己付费购买了阿里云和极速网络接口数据，同时使用了部分免费接口，如果存在侵权情况，请第一时间告知。
- 本项目仅做学习交流使用，API数据内容所有权归原作公司所有，请勿用于其他用途


### 2.关于接口说明
- 部分图片：用的是七牛云存储图片
- 新闻接口：阿里云【接口是购买的】，天行，干货集中营等接口
- 新闻接口：极速数据，知乎日报，豆瓣免费的接口
- 阅读小说：追书神器接口


### 3.关于模块说明
- 新闻部分（天行新闻，微信精选新闻，阿里云热门新闻，干货集中营新闻等等）
- 音乐部分（音乐播放器，自动搜索本地音乐文件）
- 视频部分（视频播放器，自动搜索本地视频文件）
- 图片部分（干货集中营美女图片，解析(html)某网站图片）
- 豆瓣电影，音乐，读书（豆瓣接口）
- 唐诗，宋词，元曲。是阿里云接口，每天请求一次，读取缓存数据，避免接口频繁调用。
- 快递查询
- 天气，自定义七日天气折线图，背景可以设置动态图，模仿华为天气App
- 简易记事本，番茄周，学习MVP+Dagger2时是模仿别人写的。
- 绘画板，自定义计时器（闹钟）
- 仿360杀毒小工具（通讯，软件管家，缓存清理，流量统计，通讯卫士等功能）
- 其他功能（更换皮肤，跳转应用市场评分，夜间模式，版本更新，）
- 部分板块功能正在完善中，主要是用于练手，因此界面不是很美观
- 干货集中营，非常感谢干货集中营提供的数据，网上的案例也非常多，适合练手
- 超文本笔记本，可以支持文字，图片，动态图混排
- 画廊欣赏图片


### 4.关于相关特性说明
- 侧滑菜单：DrawerLayout+NavigationView
- 基本遵循Google Material Design设计风格
- 透明状态栏使用与版本适配
- 图片加载picasso，Glide加载监听，获取缓存，圆角图片，高斯模糊
- list条目点击水波纹效果
- CoordinatorLayout+Behavior实现标题栏渐变
- BGA结合RecyclerView下拉刷新上拉加载
- 缓存使用Realm数据库，做数据的增删改查
- 状态管理库与Activity和Fragment结合，可以自由切换不同的状态


### 5.关于部分功能说明
- 5.1 关于工作实际开发中的所遇到的坑，以及bug的解决方案，都清晰地写在了注释中。
- 5.2 关于list页面加载数据，添加了状态，加载数据成功，加载数据失败，加载网络异常，加载数据为空等四种状态。与常见的处理方式有所不同，让View状态的切换和Activity彻底分离开。用builder模式来自由的添加需要的状态View。我在网上看到有些案例，有些是在父类中写出不同状态布局方法，让子类去实现，但是也带来了一个问题，如果不继承父类该怎么处理？有的是把不同的状态视图封装在控件中，那么问题来了，如果不用这个控件怎么办？？？所以借鉴相关案例，然后在学习builder模式的时候也写了一个，应该来说十分灵活，适用于多种场合。
- 关于阿里开源框架Vlayout练习与学习使用，看到这个框架的时候震惊了，一个RecycleView就搞定了复杂的布局，做法是设置不同的adapter，关于使用，可以参考我的博客，建议直接看官方案例。
- 使用okhttp3对网络返回内容做缓存，还有日志、超时重连、头部消息，拦截器的配置
- 使用EventBus开源框架，做Activity，Fragment与service不同组件之间的通信
- 自定义WebView，实现滑动监听。并且优雅处理了回退栈的问题。优雅处理302重定向问题
- 使用RecyclerView实现下拉刷新、上拉加载、侧滑删除、长按拖曳
- 刚开始项目是采用MVC架构写的，之后逐渐更改为MVP结构，会持续更新迭代！
- 版本更新功能说明（简单下载，DownloadManager下载管理器介绍）
- Fragment中返回键处理（例如Fragment是H5网页，点击返回是返回上一层，而不是销毁该宿主Activity）
- 关于计时器AlarmManager（闹钟计时），Timer，TimerTask（倒计时），CountDownTimer（封装好的倒计时，加锁），区别与实际应用
- 关于WebView优化以及使用时相关注意事项。之前又添加了**腾讯x5WebView，相对原生WebView来说，体验更佳**
- 关于内存泄漏检测及注意事项，目前内存泄漏主要使用leakcanary
- 使用jsoup开源库，尝试解析某网站站点DOM，不过数据有时能够获取，有时获取不到。
- 关于App相关优化（图片优化，启动优化，代码优化等等）
- 关于Annotation注解使用介绍，通过案例更加通俗理解注释的作用
- 关于事件冲突解决思路与方案
- 关于全局弹窗的实现及注意事项
- 关于自定义Dialog，Toast，PopupWindow等
- 关于线程并发的难题以及解决方案（这个是一同事给的提示，debug无法检测）
- 关于平时开发中遇到的bug总结
- 关于设计模式，最近正在重新阅读《Android源码实际模式解析与实战》，并且逐步引用设计模式。具体可参考开源项目：【YCDialog】(https://github.com/yangchong211/YCDialog)，


### 6.关于开源项目说明
- 上拉刷新下拉加载使用BGA刷新控件，功能强大
- 图片加载：picasso,glide
- 网络请求：retrofit2+okhttp3+rxandroid
- 获取本地图片使用TakePhoto
- 轮播图：banner
- 导航栏，指示器：tablayout
- recycleView封装类：easyrecyclerview
- 流式布局：鸿洋大神的flowlayout
- 解析HTML：jsoup（解析某网址，遍历解析图片并且展示）
- squareup公司的内存泄漏检测工具
- 自定义的开源项目
    - 1.图片选择器
    - 2.自定义弹窗以及自定义Loading加载
    - 3.自定义启动页圆形倒计时器
    - 4.自定义状态管理器
    - 5.自定义工具类
    - 6.自定义图文混排超文本
    - 7.自定义倒计数文本
    - 8.adapter简单封装
    - 7.自定义轮播图等等


### 7.关于参考项目说明
- 参考的项目有：云阅，开源中国，番茄周，Geek系列，MusicPlayer，仿网易新闻等项目

### 8.关于版本更新说明
- v1.0版本【2016年3月-2017年8月】
- v1.2版本【9月16日】
    - 添加了音乐和视频【Vitamio】版块，包括读取本地视频及音乐，音乐播放，视频播放基础功能
    - 加入了腾讯bugly管理
    - 修复了某些内存泄漏bug，添加了全局弹窗功能【投资界功能】，跳转应用市场【3种方法】，自定义弹窗功能
    - 缓存了部分数据，例如唐诗宋词，快递的公司列表，直接使用realm缓存到本地
    - 由于部分网络Api接口有数据访问量限制，因此添加了同一天之请求一次网络数据功能【同一天其他时间访问走缓存】
- v1.3版本【10月31日】
    - 添加了自定义超文本控件，支持图文混排，并且支持键盘键【x号】删除图片，使用Realm数据库
    - 初步尝试改版干货集中营版本为MVP结构
    - 修复了部分bug，完善了播放器部分
- v1.4版本【11月13日】
    - 完善干货集中营模块代码，修复了加载图片失败bug，支持收藏
    - 优化了超文本记事本功能，增加了闹钟功能，自动发送通知。
    - 继续完善了七日天气版块的功能，增加了设置动态背景，切换背景，修复了时间显示错误bug，TODO，后期添加选择地区，查询天气功能
    - 减少了静态变量，例如图片集合，文字集合等；该为array中设置，节省开辟更多内存空间。
- v1.5版本【11月26日】
    - 学习并且练习MVP+Dagger2架构模式案例，具体可以看数据页->猜你喜欢->番茄模块
    - 使用阿里的开源框架Vlayout，改写了数据页面，具体使用方法可以看我博客，建议一边看博客，一边练习。
    - 修改了部分bugly平台上报的错误
- v1.6版本【12月12日】
    - 修改了部分bug，集成了腾讯x5框架，使用x5的WebView直接加载word，ppt，pdf等文档
- v1.7版本【18年1月5日】
    - 修改了bugly上报错的bug，去掉了一些无用的代码，在开源项目EasyRecycleView的基础上修改了大量源码和去掉了部分代码【本是想等待原作者修改，只是好久都没有更新】，所以自己才改动后再次封装库。具体可以见：https://github.com/yangchong211/YCRefreshView
    - 修改了豆瓣音乐，书籍，天性新闻还有知乎新闻等部分空指针的错误，需要增加字段是否为空的判断，感谢某些同行提醒！！

### 9.关于获取更多信息
- 关于我的博客大汇总，包含技术博客，开源项目，生活博客，喜马拉雅等模块。会持续更新
- 这里简单展示几个思维导图，如果想了解更加详细内容，欢迎直接浏览该网址：http://www.jianshu.com/p/53017c3fc75d
- - ![image](http://p0u62g00n.bkt.clouddn.com/关于开源的项目.png)
![image](http://p0u62g00n.bkt.clouddn.com/技术博客图谱.png)
![image](http://p0u62g00n.bkt.clouddn.com/Android知识图谱.png)
        
### 10.App图片展示
- 整体gif动态图
- ![image](http://p0u62g00n.bkt.clouddn.com/001.gif)
- 启动页
- 启动页随机加载网路图片，自定义倒计时器
- ![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-47-12.png)
- 主页面
- 侧滑+底部导航：DrawerLayout+NavigationView+TabLayout，屏蔽ViewPager的左右滑动，如图所示
- 首页list新闻加载数据来于本地json
- ![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-12-38.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-11-24-19-29-39-957_com.ns.yc.life.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-13-22.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-12-45.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-12-56.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-13-08.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-13-16.png)
- 新闻页及新闻详情页面
- ![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-13-52.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-14-08.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-14-49.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-15-36.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-15-21.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-15-45.png)
- 图片页面
- ![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-15-41.png)
- 音乐页面
- ![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-10-18-19-44-26.png)
- 视频页面
- ![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-10-18-19-39-21.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-10-18-19-39-28.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-10-18-19-39-32.png)
- 小说阅读器页面
- ![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-16-25.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-16-30.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-16-37.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-16-41.png)
- 豆瓣评论页面及详情页面
- ![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-17-08.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-17-12.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-17-17.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-17-24.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-17-36.png)
- 唐诗宋词元曲页面
- ![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-17-43.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-17-49.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-48-10.png)
- 天气页面
- ![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-53-23.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-53-26.png)
- 干货集中营
- ![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-10-31-19-48-39.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-11-13-18-44-25.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-11-13-18-44-35.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-11-13-18-44-15.png)
- 图文混排超文本
- ![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-10-31-19-46-58.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-10-31-19-48-13.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-11-13-18-55-55.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-11-13-18-56-05.png)
- 记事本页面
- ![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-18-19.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-18-36.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-18-51.png)
- 七日天气
- ![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-11-13-18-42-02.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-11-13-18-42-05.png)
- 快递页面
- ![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-17-57.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-18-06.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-18-51.png)
- 时钟页面
- ![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-55-39.png)
- 其他页面
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-18-06.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-18-51.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-20-44.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-20-40.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-20-20.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-20-05.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-19-51.png)
![image](http://p0u62g00n.bkt.clouddn.com/Screenshot_2017-09-30-17-12-38.png)


### 11.其他说明
- 11.1注意问题
- a.淡黄色警告，当然大多数淡黄色的警告并不会导致程序崩溃，但是对于对代码质量有追求的我来说，是最大程度上避免了淡黄色的警告
- b.注意成员变量和局部变量不要滥用，我看了一些代码，许多把局部变量直接创建成了成员变量，它们在内存中开辟空间后回收周期不同。
- c.关于第三方库问题，由于这个是练手项目，所以加入了一些自己平时抽取整理封装的库。在实际的项目中，建议慎重选择第三方库，选择轻量级合适的封装库。
- 11.2关于bugly平台
- a.发现bugly平台上收集错误日志，查看运营统计数据，一共有165人【截止12月18日】使用。如果有朋友也想使用bugly进行统计，麻烦自己再去注册一个账号，更改下集成时的AppId和key。以避免与我的统计bug混淆了，多谢！

### 12.关于云测试情况


### 13.关于bug管理说明
- 目前bug是通过腾讯bugly进行管理，以前在实际项目中用bugtags，后来改为bugly。个人认为bugly平台还是挺不错的，有相当多的常见错误，会有一些好的解决方案介绍。对于bug定位与打印日志，集成符号表库，可以准确地定位用户APP发生Crash的代码位置，Bugly使用符号表对APP发生Crash的程序堆栈进行解析和还原
- **关于我的bug管理，我平时喜欢记录笔记，因此，我把遇到的bug总结写在了文档上。主要包括**
- 1.出现什么bug
- 2.什么情况下出现的
- 3.问题导致的原因
- 4.解决bug的思路或者方法
- 5.部分会做一下知识延伸或者源码分析
- **关于我的bug总结文档，我已经放在了github上，有兴趣的可以看看。我虽然并不是什么大牛，但一直在前行的路上**
- 由于这个笔记是印象笔记写的，因此github网站上可以无法看到印象笔记导出的mht格式的文档，可以下载后看
- 关于bug文档地址：https://github.com/yangchong211/image/tree/master/bug
- 关于内容如下所示，只是一部分：


```
在线bugtags问题大总结01
目录结构
1.GuideActivity页面初始化Realm数据库导致崩溃
2.ChannelActivity页面空指针崩溃
3.关于Picasso切割gif图片为圆形带来的崩溃
4.友盟分享不能传参数为null，否则崩溃
5.登录保存用户关注点空指针崩溃【待定】
6.缓存数据传递数据有误导致崩溃
7.程序ANR无响应导致崩溃【待定】
8.缺少资源文件导致崩溃【待定】【 android 6.0 (23)，HUAWEI:BTV-W09:arm64-v8a】
9.Android无法添加窗口【dialog】崩溃
10.Picasso加载大图OOM崩溃【难点】
11.Android的一个taskdescription的原色应该是不透明的
12.重写父类方法没有重写super方法【例如：onCreate，onPase等】
13.Android包管理器已经死了导致崩溃
14.华为荣耀6plus手机【王鹏提出用户反馈】
15.OkHttp调用问题报错【java.lang.IllegalStateException: closed】
16.Realm数据库升级版本导致崩溃【严重】
17.收藏分享传值为null导致崩溃
18.数据库Realm存入数据为空而崩溃
19.代码引用资源属性而崩溃【注意】
20.角标越界异常导致崩溃
21.开源图片加载框架picasso加载空地址图片而崩溃
22.adapter为空时导致刷新数据崩溃
23.adapter为空时导致刷新数据崩溃
24.外部App下载链接页面，手机有应用宝，点击下载没问题；没有应用宝则崩溃
25.点击手机系统自带返回键导致崩溃【待定】
26.进程启动失败导致崩溃
27.地址非法参数异常 IllegalArgumentException导致崩溃【url地址传入非法参数】
28. ClassNotFoundException: Didn't find class "*****" on path: /data/app/**错误
29.角标越界异常


在线bugtags问题大总结02
目录结构
1.友盟第三方登录QQ图标显示问题
2.Gson解析数据出现问题，原因服务器返回数据不严谨
3.索引越界异常导致崩溃【这个有点奇怪】
4.开启进程检测服务失败导致崩溃【这种及其少见】
5.使用ActionBar导致崩溃，引用主题theme错误【外部】
6.新浪微博，userinfo POS 5
7.跳转外部app导致崩溃【判断是否安装该app】
8.Java代码中除法分母一定不能为0，否则崩溃，加判断【修改源码】
9.索引越界导致异常崩溃【考虑没网的时候】
10.对象为空，获取数据无法填充到对象
11.加载布局，导致OOM，有些奇怪
12.非法状态异常，关于OKHttpUtils拦截器返回数据异常
13.拦截器body方法调用两次
14.关于空指针bug总结排除方法
15.Package manager has died导致崩溃【】
16. WindowManager$BadTokenException: Unable to add window
17.解决Fragment偶发异常java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
18.在Fragment中通过getActivity找不到上下文，报null
19.非法参数异常，不附加窗口管理器【未解决】


在线bugtags问题大总结03
目录结构
1.WindowManager$BadTokenException导致崩溃
2.TimeoutException 连接超时崩溃
3.NumberFormatException 格式转化错误
4.ClassCastException 类型转化异常
5. java.lang.IllegalStateException: Fragment not attached to Activity
6.ArrayIndexOutOfBoundsException 角标越界
7.ClassNotFoundException  类转化异常
8.IllegalArgumentException 非法参数异常
9.OutOfMemoryError 内存溢出
10.ClassNotFoundException 类找不到异常
11.IllegalAccessException 方法中构造方法权限异常
12.java.lang.IllegalStateException: closed 报错 【未解决】
13.java.lang.NoClassDefFoundError,找不到类
14. 
15.线程并发导致数据错乱
16. RuntimeException: Unable to start activity ComponentInfo
17. IllegalArgumentException View添加窗口错误
18.Android出现：Your project path contains non-ASCII characters.
19.SIGSEGV(SEGV_MAPERR)
20.Android Studio之导出JavaDoc出现编码GBK的不可映射字符


在线bugtags问题大总结04
目录结构
1. IllegalArgumentException【参数不匹配异常】
2.NullPointerException【空指针异常】
3.OutOfMemoryError【OOM异常，内存溢出】
4.ClassCastException【类型转换异常】
5.IndexOutOfBoundsException【索引越界异常】
6.IllegalStateException【非法状态异常】
7.UnsatisfiedLinkError【 不满意的链接错误】
8.NoClassDefFoundError【该异常表示找不到类定义】
9.OutOfMemoryError【内存溢出异常】
10.NoClassDefFoundError【类找不到】
11.NumberFormatException【转化异常】
```


### 14.关于我的博客
- github：https://github.com/yangchong211
- 喜马拉雅听书：http://www.ximalaya.com/zhubo/71989305/
- 知乎：https://www.zhihu.com/people/yang-chong-69-24/pins/posts
- 简书：http://www.jianshu.com/u/b7b2c6ed9284
- csdn：http://my.csdn.net/m0_37700275
- 新浪博客：http://blog.sina.com.cn/786041010yc
































