#### 目录介绍
- 01.整体概述
    - 1.1 项目背景
    - 1.2 遇到问题
    - 1.3 基础概念介绍
    - 1.4 设计目标
    - 1.5 产生收益分析
- 02.老版本组件通信
    - 2.1 项目组件通信
    - 2.2 老版组件通信实践
    - 2.3 存在的问题分析
    - 2.4 解决痛点方案
- 03.设计思路的分析
    - 3.1 Apt设计思路
    - 3.3 静态和动态代理
    - 3.6 apt如何生成代码
    - 3.7 反射一点思考
- 04.Apt实现组件通信
    - 4.1 总体规划设计
    - 4.2 总体设计思路
- 05.方案基础设计
    - 5.1 整体架构图
    - 5.2 UML设计图
    - 5.3 关键流程图
    - 5.4 接口设计图
    - 5.5 模块间依赖关系
- 06.其他设计说明
    - 6.1 性能设计
    - 6.2 稳定性设计
    - 6.3 灰度设计
    - 6.4 降级设计
    - 6.5 异常设计
- 07.其他说明介绍
    - 7.3 遇到问题说明
    - 7.5 参考链接



### 01.整体概述
#### 1.1 项目背景


#### 1.2 遇到问题



#### 1.3 基础概念介绍
- Apt技术介绍
    - APT，就是Annotation Processing Tool的简称，就是可以在代码编译期间对注解进行处理，并且生成Java文件，减少手动的代码输入。
    - 也有人叫它代码生成，其实还是有些区别，在编译时对注解做处理，通过注解，获取必要信息，在项目中生成代码，运行时调用，和直接运行手写代码没有任何区别。
- 组件之间的通信
    - 接口+实现类，使用注解生成代码方式，无需手动注册，将使用步骤简单化，支持组件间以暴露接口提供服务的方式进行通信。


#### 1.4 设计目标
- 注解生成代码自动注册
    - 使用apt注解在编译阶段生成服务接口与实现的映射注册帮助类，其实这部分就相当于是替代了之前在application初始化注入的步骤，获取服务时自动使用帮助类完成注册，不必手动调用注册方法。
- 避免空指针崩溃
    - 无服务实现注册时，使用空对象模式 + 动态代理的设计提前暴露调用错误，主要抛出异常，在测试时就发现问题，防止空指针异常。
- 代码入侵性低
    - 无需改动之前的代码，只需要在之前的接口和接口实现类按照约定添加注解规范即可。其接口+接口实现类还是用之前的，完全无影响……
- 按照需要来加载
    - 首次获取接口服务的时候，用反射生成映射注册帮助类的实例，再返回实现的实例。
- 丰富的代码案例
    - 代码案例丰富，提供丰富的案例，然后多个业务场景，尽可能完善好demo。
- 该库注解生成代码在编译器
    - 在编译器生成代码，并且该类是继承自己自定义的接口，存储的是map集合，key是接口class，value是接口实现类class，直接在编译器把接口和实现类存储起来。用的时候直接取……



#### 1.5 产生收益分析




### 02.老版本组件通信
#### 2.1 项目组件通信
- 组件app分层
    - ![image](https://img-blog.csdnimg.cn/20210310214447978.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70)
- 比如业务组件层划分
    - 组件A，组件B，组件C，组件D，组件E等等，这些业务组件module并不是相互依赖，它们之间是相同的层级！
- 举一个业务案例
    - 比如有个选择用户学员的弹窗，代码写到了组件A中，这个时候组件C和组件D需要复用组件A中的弹窗，该业务逻辑如何处理？
    - 比如组件E是我的用户相关的业务逻辑，App登陆后，组件B和组件C需要用到用户的id去请求接口，这个时候如何获取组件E中用户id呢？
- 该层级下定义一个公共通信组件
    - 接口通信组件【被各个业务组件依赖】，该相同层级的其他业务组件都需要依赖这个通信组件。这个时候各个模块都可以拿到通信组件的类……
- 具体实现方案
    - 比方说，主app中的首页有版本更新，业务组件用户中心的设置页面也有版本更新，而版本升级的逻辑是写在版本更新业务组件中。这个时候操作如下所示
    - ![image](https://img-blog.csdnimg.cn/20200426093500838.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70)


#### 2.2 老版组件通信实践
- 通信组件几个主要类
    - BusinessTransfer，主要是map集合中get获取和put添加接口类的对象，利用反射机制创建实例对象。**该类放到通信组件中**。
    - IUpdateManager，该类是版本更新接口类，定义更新抽象方法。**该类放到通信组件中**。
    - UpdateManagerImpl，该类是IUpdateManager接口实现类，主要是具体业务逻辑的实现。**该类放到具体实现库代码中，比如我的组件**。
- 主要实现的代码如下所示
    ``` java
    //接口
    public interface IUpdateManager extends Serializable {
  
        void checkUpdate(UpdateManagerCallBack updateManagerCallBack);
    
        interface UpdateManagerCallBack {
            void updateCallBack(boolean isNeedUpdate);
        }
    }
    
    //接口实现类
    public class UpdateManagerImpl implements IUpdateManager {
        @Override
        public void checkUpdate(UpdateManagerCallBack updateManagerCallBack) {
            try {
                IConfigService configService = DsxxjServiceTransfer.$().getConfigureService();
                String data = configService.getConfig(KEY_APP_UPDATE);
                if (TextUtils.isEmpty(data)) {
                    if (updateManagerCallBack != null) {
                        updateManagerCallBack.updateCallBack(false);
                    }
                    return;
                }
                ForceUpdateEntity xPageUpdateEntity = JSON.parseObject(data, ForceUpdateEntity.class);
                ForceUpdateManager.getInstance().checkForUpdate(xPageUpdateEntity, updateManagerCallBack);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    //如何使用
    //在初始化时注入
    BusinessTransfer businessTransfer = BusinessTransfer.$();
    businessTransfer.setImpl(BusinessTransfer.BUSINESS_IMPL_UPDATE_MANAGER,
            PACKAGE_NAME + ".base.businessimpl.UpdateManagerImpl");
    ```
- 那么如何调用呢？可以在各个组件中调用，代码如下所示……
    ``` java
    //版本更新
    BusinessTransfer.$().getUpdate().checkUpdate(new IUpdateManager.UpdateManagerCallBack() {
        @Override
        public void updateCallBack(boolean isNeedUpdate) {
            
        }
    });
    ```


#### 2.3 存在的问题分析
- 这种方式存在几个问题
    - 1.注入的时候要填写正确的包名，否则在运行期会出错，且不容易找到；
    - 2.针对接口实现类，不能混淆，否则会导致反射找不到具体的类，因为是根据类的全路径反射创建对象；所以每次写一个接口+实现类，都要在混淆文件中添加一下，比较麻烦……
    - 3.每次添加新的接口通信，都需要手动去注入到map集合，稍微有点麻烦，能否改为自动注册呢？
    - 4.每次还要在Transfer的类中，添加获取该接口对象的方法，能否自动一点？
    - 5.可能出现空指针，一旦忘记没有注入或者反射创建对象失败，则直接导致崩溃……


#### 2.4 解决痛点方案




### 03.设计思路的分析
#### 3.1 Apt设计思路



#### 3.3 静态和动态代理
- 快速理解静态代理
    - 静态代理的实现模式 = 接口(抽象类) + 具体实现类(委托类) + 代理类。比如轻量级线程库中，AbsTaskExecutor(抽象类) + DefaultTaskExecutor(具体实现类) + DelegateTaskExecutor(代理类)
    - 静态代理使用分析：实现业务分工，可以很好的保护实际对象(具体实现类)的业务逻辑对外暴露，从而提高安全性；但是代理对象的一个接口只服务于一种类型的对象，很难在程序规模稍大时胜任。
- 为何需要动态代理
    - 静态代理的拓展性比较差，对于那些实现了之后不怎么改变的功能还可以使用，如果频繁改动，代理类也需要跟着改动。
    - 静态代理委托类必须提前创建，并将其作为代理对象的内部属性。使用中必须一个真实角色必须对应一个代理角色，大量使用会代码膨胀；如果事先并不知道真实角色（委托类），该如何使用代理呢？
- 快速理解动态代理
    - 动态代理场景：不需要提前创建代理对象，而是利用反射机制在运行时创建代理类，从而动态实现代理功能。具体可以看下retrofit，构建接口实例对象就可以调用网络请求，就是用动态代理。



#### 3.6 Apt如何生成代码
- 注解是如何生成代码的？也就是javapoet原理……
    - 这个javapoet工具，目前还紧紧是套用ARouter，创建类名，添加接口，添加注解，添加方法，添加修饰符，添加函数体等等。也就是说将一个类代码拆分成n个部分，然后逆向拼接到一起。最后去write写入代码……
    - 但是，怎么拼接和并且创建.java文件的原理，待完善。目前处于会用……




#### 3.7 反射一点思考
- new和Class.forName("").newInstance()创建对象有何区别？
    ``` java
    A a = (A)Class.forName("com.yc.demo.com.yc.other.impl.UpdateImpl").newInstance();
    A a = new A()；
    ```
- 它们的区别在于创建对象的方式不一样
    - 前者(newInstance)是使用类加载机制，后者(new)是创建一个新类。
    - 为什么会有两种创建对象方式？主要考虑到软件的可伸缩、可扩展和可重用等软件设计思想。
    - 从JVM的角度上看：
        - 我们使用关键字new创建一个类的时候，这个类可以没有被加载。但是使用newInstance()方法的时候，就必须保证：1、这个类已经加载；2、这个类已经连接了。
        - 而完成上面两个步骤的正是Class的静态方法forName()所完成的，这个静态方法调用了启动类加载器，即加载 java API的那个加载器。
        - 现在可以看出，newInstance()实际上是把new这个方式分解为两步，即首先调用Class加载方法加载某个类，然后实例化。 这样分步的好处是显而易见的。我们可以在调用class的静态加载方法forName时获得更好的灵活性，提供给了一种降耦的手段。
- 两者区别
    - 首先，newInstance( )是一个方法，而new是一个关键字；其次，Class下的newInstance()的使用有局限，因为它生成对象只能调用无参的构造函数，而使用new关键字生成对象没有这个限制。



### 04.Apt实现组件通信
#### 4.1 总体规划设计
- ModuleBus主要由三部分组成，包括对外提供的api调用模块、注解模块以及编译时通过注解生产相关的类模块。
    - api-compiler 编译期解析注解信息并生成相应类以便进行注入的模块
    - api-manager 注解的声明和信息存储类的模块，以及开发调用的api功能和具体实现
- 编译生成代码发生在编译器
    - 编译期是在项目编译的时候，这个时候还没有开始打包，也就是没有生成apk呢！框架在这个时期根据注解去扫描所有文件，然后生成路由映射文件。这些文件都会统一打包到apk里！
- 无需初始化操作
    - 先看ARouter，会有初始化，主要是收集路由映射关系文件，在程序启动的时候扫描这些生成的类文件，然后获取到映射关系信息，保存起来。这个封装库不需要初始化，简化步骤，在获取的时候如果没有则在put操作map集合。具体看代码！


#### 4.2 总体设计思路




### 07.其他说明介绍
#### 7.3 遇到问题说明


#### 7.5 参考链接





