# 组件化项目实践
#### 目录介绍
- 01.项目整体概述介绍
- 02.组件化演变的过程
- 03.组件化实践的步骤
- 04.组件化是如何交互
- 05.组件化避坑的指南
- 06.公共基础库的介绍
- 07.功能组件库的介绍
- 08.服务组件库的介绍
- 09.



### 01.项目整体概述
#### 1.1 项目背景说明
- APP迭代维护成本增高
    - APP版本不断迭代：新功能，业务模块数量不断增加；业务上的处理逻辑越变越复杂；同时每个模块代码也变得越来越多。这就引发问题，所维护代码成本越来越高，稍微一改动可能就牵一发而动全身，改个小的功能点就需要回归整个APP测试，这就对开发和维护带来很大的挑战。
- 多人组合需要组件化
    - APP架构方式是单一工程模式，业务规模扩大，随之带来的是团队规模扩大，每个移动端软件开发人员势必要熟悉如此之多代码，如果不按照一定的模块组件机制去划分，将很难进行多人协作开发。随着单一项目变大，在单一工程代码耦合严重，每修改一处代码后都需要重新编译打包测试，导致非常耗时。


#### 1.2 遇到问题记录
- 代码量膨胀，不利于维护和功能迭代
    - 项目工程构建速度慢，在一些电脑上写两句代码，重新编译整个项目，有的甚至更长。
- 不同模块之间代码耦合严重，有时候修改一处代码而牵动许多模块
    - 每个模块之间都有引用第三方库，但有些第三方库版本不一致，导致打包APP时候代码冗余，容易引起版本冲突。
- 代码历史遗留问题
    - 现有项目基于以前其他人项目基础上开发，经手的人次过多，存在着不同的代码风格，项目中代码规范乱，类似的功能写法却不一样，导致不统一。


#### 1.3 基础概念介绍
- 什么是组件化呢？
    - 组件化是基于组件可重用的目的上，将一个大的软件系统按照分离关注点的形式，拆分成多个独立的组件，做到更少的耦合和更高的内聚。
- 模块化和组件化区别
    - 简单来说，组件化相比模块化粒度更小，两者的本质思想都是一致的，都是把大往小的方向拆分，都是为了复用和解耦，只不过模块化更加侧重于业务功能的划分，偏向于复用，组件化更加侧重于单一功能的内聚，偏向于解耦。



#### 1.4 开发设计目标
- 组件化的目标
    - 组件化的目标之一就是降低整体工程（app）与组件的依赖关系，缺少任何一个组件都是可以存在并正常运行的。
- 对组件化层次划分
    - 需要结构清晰，拆分粒度符合设计规范。方便迁移，按需加载。针对业务庞大，每个人员可以负责自己独立的业务组件……
- 可以做到技术沉淀
    - 比如针对功能组件，服务组件，还有基础组件，可以沉淀出来做成中台公共库。方便维护，在多个APP中可以复用组件。


#### 1.5 组件化改造阻力
- 商业化项目稳定性保证
    - 商业化项目，很强调稳定性。有些项目做了很多年，不敢轻易改动，害怕组件化改造会带来不可估量的影响和线上bug。
- 不同层人看待技术角度不同
    - 公司领导层和一线程序员存在对技术不同想法。开发想着如何用一些新技术去改造项目提升自己技术能力；领导想着技术是偏下游，确保稳定性，如何提升业务收益。
- 改造收益比较难衡量
    - 



### 02.组件化演变的过程
#### 2.1 以前App说明
- 传统APP架构图
    - ![image](http://upload-images.jianshu.io/upload_images/4432347-1047b1cdf15fd59a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
- 存在的问题
    - 普遍使用的 Android APP 技术架构，往往是在一个界面中存在大量的业务逻辑，而业务逻辑中充斥着各种网络请求、数据操作等行为，整个项目中也没有模块的概念，只有简单的以业务逻辑划分的文件夹，并且业务之间也是直接相互调用、高度耦合在一起的。
    - 单一工程模型下的业务关系，总的来说就是：你中有我，我中有你，相互依赖，无法分离，多个开发代码越维护越臃肿，耦合严重。如下图：
    - ![image](https://upload-images.jianshu.io/upload_images/4432347-ab213414e69fef5a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



#### 2.2 现在App架构图
- 按照不同层级架构图




### 03.组件化实践的步骤
#### 3.1 组件化考虑问题
- 考虑的问题：分而治之，并行开发，一切皆组件。要实现组件化，无论采用什么样的技术方式，需要考虑以下方面问题：
- 代码解耦：对已存在的项目进行模块拆分，模块分为两种类型，一种是作为依赖库对外提供；另一种是业务组件模块，专门处理业务逻辑等功能，这些业务组件模块最终负责组装APP。
- 组件间通信：页面跳转可以使用路由；业务组件业务调用可以采用SPI或者接口反射
- 组件生命周期：组件是否可以做到按需、动态使用、因此就会涉及到组件加载、卸载等管理问题。
- 集成调试：在开发阶段如何做到按需编译组件？一次调试中可能有一两个组件参与集成，这样编译时间就会大大降低，提高开发效率。
- 告别结构臃肿：让各个业务变得相对独立，业务组件在组件模式下可以独立开发，而在集成模式下又可以变为AAR包集成到“APP壳工程”中，组成一个完整功能的 APP。



#### 3.2 组件化架构拆分
- 主工程(壳工程代码，多favor，debug助手等)：
    - 除了一些全局配置和主 Activity 之外，不要包含太多的业务代码。有的也叫做空壳app，主要是依赖业务组件进行运行。
- 业务组件(主要是业务层拆分的组件)：
    - 最上层的业务，每个组件表示一条完整的业务线，彼此之间互相独立。原则上来说：各个业务组件之间不能有直接依赖！对于测试的时候，需要依赖多个业务组件的功能进行集成测试的时候。可以使用app壳进行多组件依赖管理运行。
- 功能组件(分为服务组件和中台组件)：
    - 该案例中分为，登录注册组件，分享组件，支付组件，Hybrid组件等等。同时注意，可能会涉及多个业务组件对某个功能组件进行依赖！
- 基础组件(分为工具组件和视图组件)：
    - 支撑上层业务组件运行的基础业务服务。此部分组件为上层业务组件提供基本的功能支持。基础组件库中主要有，网络请求，图片加载，通信机制，工具类，分享功能，支付功能等等。当然，我把一些公共第三方库放到了这个基础组件中！
- 这样拆分的目的
    - 架构分层将模块化带来的网状依赖结构改造成树状依赖结构(上层依赖下层)，降低了依赖的复杂度，保障各层之间的依赖不劣化。



### 04.组件化是如何交互
#### 4.1 组件初始化功能



#### 4.2 组件间页面跳转
- 这是组件化工程模型下的业务关系，业务之间将不再直接引用和依赖，而是通过“路由”这样一个中转站间接产生联系。推荐使用的阿里开源的路由框架。



#### 4.3 组件间业务通信
- 组件化不同model业务相互调用通信
    - 接口 + 实现类 + 反射。实现通信



#### 4.4 业务耦合逐渐劣化
- 随着时间的推移，各个业务线的代码边界会像组件化之前的主工程一样逐渐劣化，耦合会越来越严重。
    - 第一种解决方式：使用 sourceSets 的方式将不同的业务代码放到不同的文件夹，但是 sourceSets 的问题在于，它并不能限制各个 sourceSet 之间互相引用，所以这种方式并不太友好！
    - 第二种解决方式：下沉，抽取需求为共同类，通过不同组件传值而达到调用关系，这样只需要改工具类即可改需求。但是这种只是符合需求一样，但是用在不同模块的场景。



### 05.组件化避坑的指南
#### 5.1 避免组件依赖恶化
- 分层架构，技术人员定义了每一层组件的依赖规范，以防止不合理的循环依赖，保证整体依赖不劣化。在分层依赖规范中，高层可以依赖低层、实现可以依赖接口，接口层没有依赖，且优先以前向声明为主。


#### 5.2 组件化时资源名冲突
- 资源名冲突有哪些？
    - 比如，color，shape，drawable，图片资源，布局资源，或者anim资源等等，都有可能造成资源名称冲突。这是为何了，有时候大家负责不同的模块，如果不是按照统一规范命名，则会偶发出现该问题。
    - 尤其是如果string， color，dimens这些资源分布在了代码的各个角落，一个个去拆，非常繁琐。其实大可不必这么做。因为android在build时，会进行资源的merge和shrink。res/values下的各个文件（styles.xml需注意）最后都只会把用到的放到intermediate/res/merged/../valus.xml，无用的都会自动删除。并且最后我们可以使用lint来自动删除。所以这个地方不要耗费太多的时间。
- 解决办法
    - 这个问题也不是新问题了，第三方SDK基本都会遇到，可以通过设置 resourcePrefix 来避免。设置了这个值后，你所有的资源名必须以指定的字符串做前缀，否则会报错。但是 resourcePrefix 这个值只能限定 xml 里面的资源，并不能限定图片资源，所有图片资源仍然需要你手动去修改资源名。
- 个人建议
    - 将color，shape等放到基础库组件中，因为所有的业务组件都会依赖基础组件库。在styles.xml需注意，写属性名字的时候，一定要加上前缀限定词。假如说不加的话，有可能会在打包成aar后给其他模块使用的时候，会出现属性名名字重复的冲突。


#### 5.3 关于依赖优化记录
- 查看依赖树，在项目根目录下执行如下命令，将依赖导出到文件：
    ``` java
    ./gradlew app:dependencies > log_depend.txt
    ```
- implementation：
    - 只能在内部使用此模块，比如我在一个library中使用implementation依赖了gson库，然后我的主项目依赖了library，那么，我的主项目就无法访问gson库中的方法。这样的好处是编译速度会加快，推荐使用implementation的方式去依赖
- compile（api）
    - 这种是我们最常用的方式，使用该方式依赖的库将会参与编译和打包。 
- compileOnly
    - 使用场景：有多个library，只要确保有一个module中该依赖能参与到打包即可，其他的可以使用compileOnly。运行时不需要，例如仅源代码注解或注释处理器






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
Copyright 2017 yangchong211（github.com/yangchong211）

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














