
### 05.如何使用该库Api
- 在module项目中添加依赖
    ``` java
    implementation project(path: ':module-manager')
    annotationProcessor project(path: ':module-compiler')
    ```
- 在module通信组件中定义接口，注意需要继承IRouteApi接口
    ``` java
    public interface IUpdateManager extends IRouteApi {

        void checkUpdate(UpdateManagerCallBack updateManagerCallBack);
    
        interface UpdateManagerCallBack {
            void updateCallBack(boolean isNeedUpdate);
        }
    
    }
    ```
- 在需要实现服务的组件中写接口实现类，注意需要添加注解
    ``` java
    @RouteImpl(IUpdateManager.class)
    public class UpdateImpl implements IUpdateManager {
        @Override
        public void checkUpdate(UpdateManagerCallBack updateManagerCallBack) {
            //省略
        }
    }
    ```
- 如何获取服务的实例对象
    ``` java
    //无返回值的案例
    //设置监听
    IUpdateManager iUpdateManager = TransferManager.getInstance().getApi(IUpdateManager.class);
    iUpdateManager.checkUpdate(new IUpdateManager.UpdateManagerCallBack() {
       @Override
       public void updateCallBack(boolean isNeedUpdate) {
           
       }
    });
    
    //有返回值的案例
    userApi = TransferManager.getInstance().getApi(IUserManager.class);
    String userInfo = userApi.getUserInfo();
    ```
- 看一下编译器生成的代码
    - build--->generated--->ap_generated_sources--->debug---->out---->com.yc.api.contract
    - 这段代码什么意思：编译器生成代码，并且该类是继承自己自定义的接口，存储的是map集合，key是接口class，value是接口实现类class，直接在编译器把接口和实现类存储起来。用的时候直接取……
    ``` java
    public class IUpdateManager$$Contract implements IRouteContract {
      @Override
      public void register(IRegister register) {
        register.register(IUpdateManager.class, UpdateImpl.class);
      }
    }
    ```
    - ![image](https://img-blog.csdnimg.cn/20210305111650620.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70)





### 07.关于其他知识点
- Didn't find class "com.yc.api.contract.IUserManager$$Contract" on path
    - 注解生成的代码失败导致出现这个问题。为什么会出现这种情况？修改gradle的构建版本……
    ``` java
    public class IUpdateManager$$Contract implements IApiContract {
      @Override
      public void register(IRegister register) {
        register.register(IUpdateManager.class, UpdateImpl.class);
      }
    }
    ```
- 关于apt编译器不能生成代码的问题，可能会有这么一些关键点
    - 第一查看module的依赖，如果没有依赖请先添加依赖
    ``` java
    implementation project(path: ':api-manager')
    annotationProcessor project(path: ':api-compiler')
    ```
    - 第二查看写完wirter的流没有关闭，会造成生成文件，但文件内容为空，或者不全；
    - 第三可能是Android Gradle及构建版本问题，我的是3.4.1 + 5.2.1，会出现不兼容的情况，大神建议3.3.2 ＋ 4.10.1以下都可以。听了建议降低版本果然构建编译，新的文件生成了。



### 08.关于添加混淆代码
- 代码混淆
    ``` java
    -keep class com.yc.api.**{*;}
    -keep public class * implements com.yc.api.** { *; }
    ```
- 不需要在额外添加通信接口实现类的混淆代码
    - 为什么，因为针对继承com.yc.api.**的子类，会忽略混淆。已经处理……



### 09.关于几个重要问题
- 注解是如何生成代码的？也就是javapoet原理……
    - 这个javapoet工具，目前还紧紧是套用ARouter，创建类名，添加接口，添加注解，添加方法，添加修饰符，添加函数体等等。也就是说将一个类代码拆分成n个部分，然后逆向拼接到一起。最后去write写入代码……
    - 但是，怎么拼接和并且创建.java文件的原理，待完善。目前处于会用……
- Class.forName(name)反射如何找到name路径的这个类，从jvm层面分析？
    - 待完善
- new和Class.forName("").newInstance()创建对象有何区别？
    ``` java
    A a = (A)Class.forName("com.yc.demo.com.yc.other.impl.UpdateImpl").newInstance();
    A a = new A()；
    ```
    - 它们的区别在于创建对象的方式不一样，前者(newInstance)是使用类加载机制，后者(new)是创建一个新类。
    - 为什么会有两种创建对象方式？
        - 主要考虑到软件的可伸缩、可扩展和可重用等软件设计思想。
    - 从JVM的角度上看：
        - 我们使用关键字new创建一个类的时候，这个类可以没有被加载。但是使用newInstance()方法的时候，就必须保证：1、这个类已经加载；2、这个类已经连接了。
        - 而完成上面两个步骤的正是Class的静态方法forName()所完成的，这个静态方法调用了启动类加载器，即加载 java API的那个加载器。
        - 现在可以看出，newInstance()实际上是把new这个方式分解为两步，即首先调用Class加载方法加载某个类，然后实例化。 这样分步的好处是显而易见的。我们可以在调用class的静态加载方法forName时获得更好的灵活性，提供给了一种降耦的手段。
    - 区别
        - 首先，newInstance( )是一个方法，而new是一个关键字；其次，Class下的newInstance()的使用有局限，因为它生成对象只能调用无参的构造函数，而使用 new关键字生成对象没有这个限制。






