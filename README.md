# 目录介绍
* 1.关于前言介绍
* 2.关于接口说明
* 3.关于模块说明
* 4.关于相关特性说明
* 5.关于部分功能说明
* 6.关于开源项目说明
* 7.关于参考项目说明
* 8.关于版本更新说明
* 9.关于获取更多信息
* 10.App图片展示
* 11.关于其他说明
* 12.关于云测试情况
* 13.关于bug管理说明
* 14.关于我的博客

### 0.关于博客笔记大整理，持续更新迭代
- 可以直接看：[博客笔记](http://www.jianshu.com/p/53017c3fc75d)
- 如果你感觉还行，请给一个star，如果你觉得哪里有问题，也可以直接把问题提给我，我会修改的。业余的小案例，定期更新，持续更新

### 1.关于前言介绍
- 关于开发的APP有：[投资界](http://openbox.mobilem.360.cn/index/d/sid/3583538)，[新芽](http://openbox.mobilem.360.cn/index/d/sid/3425699)，可以在应用市场上下载
- 此项目属于业余时间练手的项目，接口数据来源均来自网络，自己付费购买了阿里云和极速网络接口数据，同时使用了部分免费接口，如果存在侵权情况，请第一时间告知。
- 本项目仅做学习交流使用，API数据内容所有权归原作公司所有，请勿用于其他用途


### 2.关于接口说明
- 部分图片：用的是七牛云存储图片
- 新闻接口：阿里云，天行，干货集中营等接口
- 新闻接口：极速数据
- 阅读小说：追书神器接口

### 3.关于模块说明
- 新闻部分（天行新闻，微信精选新闻，阿里云热门新闻，干货集中营新闻等等）
- 音乐部分（音乐播放器，自动搜索本地音乐文件）
- 视频部分（视频播放器，自动搜索本地视频文件）
- 图片部分（干货集中营美女图片，解析(html)某网站图片）
- 豆瓣电影，音乐，读书（豆瓣接口）
- 唐诗，宋词，元曲
- 快递查询
- 天气，自定义七日天气折线图，背景可以设置动态图，模仿华为天气App
- 简易记事本
- 绘画板
- 计时器（闹钟）
- 仿360杀毒小工具（通讯，软件管家，缓存清理，流量统计，通讯卫士等功能）
- 其他功能（更换皮肤，跳转应用市场评分，夜间模式，版本更新，）
- 部分板块功能正在完善中，主要是用于练手，因此界面不是很美观
- 干货集中营
- 超文本笔记本，可以支持文字，图片，动态图混排
- 画廊欣赏图片


### 4.关于相关特性说明
- 侧滑菜单：DrawerLayout+NavigationView
- 基本遵循Google Material Design设计风格。
- 透明状态栏使用与版本适配。
- 图片加载picasso，Glide加载监听，获取缓存，圆角图片，高斯模糊。
- list条目点击水波纹效果
- CoordinatorLayout+Behavior实现标题栏渐变。
- BGA结合RecyclerView下拉刷新上拉加载。
- 缓存使用Realm数据库


### 5.关于部分功能说明
- 版本更新功能说明（简单下载，DownloadManager下载管理器介绍）
- Fragment中返回键处理（例如Fragment是H5网页，点击返回是返回上一层，而不是销毁该宿主Activity）
- 关于计时器AlarmManager（闹钟计时），Timer，TimerTask（倒计时），CountDownTimer（封装好的倒计时，加锁），区别与实际应用
- 关于WebView优化以及使用时相关注意事项
- 关于内存泄漏检测及注意事项，4种应用（本案例可以直接上传代码解决）
- 关于App相关优化（图片优化，启动优化，代码优化等等）
- 关于Annotation注释使用介绍，通过案例更加通俗理解注释的作用
- 关于事件冲突解决思路与方案
- 关于全局弹窗的实现及注意事项
- 关于自定义Dialog，Toast，PopupWindow等
- 关于线程并发的难题以及解决方案（这个是一同事给的提示，debug无法检测）
- 关于平时开发中遇到的bug总结
- 关于设计模式，最近正在重新阅读《Android源码实际模式解析与实战》，并且逐步引用设计模式。具体可参考开源项目：【YCDialog】(https://github.com/yangchong211/YCDialog)，


### 6.关于开源项目说明
- 上拉刷新下拉加载使用BGA刷新控件，功能强大
- 图片加载：picasso
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
    

### 9.关于获取更多信息
- 知乎：https://www.zhihu.com/people/yang-chong-69-24/pins/posts
- 领英：https://www.linkedin.com/in/chong-yang-049216146/
- 简书：http://www.jianshu.com/u/b7b2c6ed9284
- csdn：http://my.csdn.net/m0_37700275
- 网易博客：http://yangchong211.blog.163.com/
- 新浪博客：http://blog.sina.com.cn/786041010yc
- github：https://github.com/yangchong211
- 喜马拉雅听书：http://www.ximalaya.com/zhubo/71989305/
- 脉脉：yc930211
- 360图书馆：http://www.360doc.com/myfiles.aspx
- 开源中国：https://my.oschina.net/zbj1618/blog
- 泡在网上的日子：http://www.jcodecraeer.com/member/content_list.php?channelid=1
- 邮箱：yangchong211@163.com
- 阿里云博客：https://yq.aliyun.com/users/article?spm=5176.100239.headeruserinfo.3.dT4bcV

### 10.App图片展示
- 启动页
- 启动页随机加载网路图片，自定义倒计时器
- ![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-47-12.png)
- 主页面
- 侧滑+底部导航：DrawerLayout+NavigationView+TabLayout，屏蔽ViewPager的左右滑动，如图所示
- 首页list新闻加载数据来于本地json
- ![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-12-38.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-11-24-19-29-39-957_com.ns.yc.life.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-13-22.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-12-45.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-12-56.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-13-08.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-13-16.png)
- 新闻页及新闻详情页面
- ![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-13-52.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-14-08.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-14-49.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-15-36.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-15-21.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-15-45.png)
- 图片页面
- ![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-15-41.png)
- 音乐页面
- ![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-10-18-19-44-26.png)
- 视频页面
- ![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-10-18-19-39-21.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-10-18-19-39-28.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-10-18-19-39-32.png)
- 小说阅读器页面
- ![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-16-25.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-16-30.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-16-37.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-16-41.png)
- 豆瓣评论页面及详情页面
- ![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-17-08.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-17-12.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-17-17.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-17-24.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-17-36.png)
- 唐诗宋词元曲页面
- ![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-17-43.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-17-49.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-48-10.png)
- 天气页面
- ![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-53-23.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-53-26.png)
- 干货集中营
- ![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-10-31-19-48-39.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-11-13-18-44-25.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-11-13-18-44-35.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-11-13-18-44-15.png)
- 图文混排超文本
- ![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-10-31-19-46-58.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-10-31-19-48-13.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-11-13-18-55-55.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-11-13-18-56-05.png)
- 记事本页面
- ![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-18-19.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-18-36.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-18-51.png)
- 七日天气
- ![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-11-13-18-42-02.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-11-13-18-42-05.png)
- 快递页面
- ![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-17-57.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-18-06.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-18-51.png)
- 时钟页面
- ![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-55-39.png)
- 其他页面
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-18-06.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-18-51.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-20-44.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-20-40.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-20-20.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-20-05.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-19-51.png)
![image](https://github.com/yangchong211/LifeHelper/blob/master/image/Screenshot_2017-09-30-17-56-32.png)


### 11.其他说明
- 11.1注意问题
- a.淡黄色警告，当然大多数淡黄色的警告并不会导致程序崩溃，但是对于对代码质量有追求的我来说，是最大程度上避免了淡黄色的警告
- b.注意成员变量和局部变量不要滥用，我看了一些代码，许多把局部变量直接创建成了成员变量，它们在内存中开辟空间后回收周期不同。

### 12.关于云测试情况


### 13.关于bug管理说明



### 14.关于我的博客











































