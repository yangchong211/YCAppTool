# 隐私合规检查库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.测试case罗列
- 05.遇到的坑分析




### 01.基础概念介绍
#### 1.1 隐私合规说明
- 场景说明：
    - 未经用户同意，存在收集`IMEI`、设备`id`，`sim`，`androidId`，设备`MAC`地址和软件安装列表、通讯录和短信的行为。
- 整改建议：
    - 隐私政策隐私弹窗必须使用明确的“同意\拒绝”按钮；只有当用户点击“同意”后，APP和SDK才能调用系统接口和读取收集用户的信息。
- 客户端如何做？
    - ①用户在点击隐私政策协议“同意”按钮前，APP和SDK不能调用系统的敏感权限接口，特别是能获取IMEI、IMSI、MAC、IP、Android、已安装应用列表、硬件序列表、手机号码、位置等等信息的系统接口。
    - ②集成的第三方SDK建议升级到最新版本，用户在点击隐私政策协议“同意”按钮后，SDK再进行初始化。
    - ③技术同学可在“同意”按钮上加入判定函数，当用户点击“同意”后，APP和SDK再执行调用系统接口的相关函数行为。



#### 1.2 静态检测工具
- 开发了一个针对 Android APK 的敏感方法调用的静态检查工具。
    - 检查关键字，对于一些敏感 API 调用，例如 deviceId、oaid、sim、androidId、imei 等相关的调用。其实只要能检测到这些相关 API 里的一些关键字，找出整个 APP 里面有哪些地方直接调用了这些方法就可以了。
- 针对的上述的一些场景，这个工具具有两个方向的工作：
    - APK 包的扫描，检查出整个APK中，哪些地方有对包含上面这些 API 关键字的直接调用。
    - 运行时检查。针对运行时频繁调用这个场景，还是需要在运行时辅助检查特定API的调用情况。



### 02.常见思路和做法
#### 2.1 监控java线程创建和销毁
- 拦截了 Thread 类以及 Thread 类所有子类的 run 方法，在 run 方法开始执行和退出的时候进行拦截，就可以知道进程内部所有 Java 线程创建和销毁的时机；
    ```
    class ThreadMethodHook extends XC_MethodHook{
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            Thread t = (Thread) param.thisObject;
            PrivacyHelper.getLogger().log("thread:" + t + ", started..");
        }
        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
            Thread t = (Thread) param.thisObject;
            PrivacyHelper.getLogger().log("thread:" + t + ", exit..");
        }
    }
    DexposedBridge.hookAllConstructors(Thread.class, new XC_MethodHook() {
        @Override
        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
            Thread thread = (Thread) param.thisObject;
            Class<?> clazz = thread.getClass();
            if (clazz != Thread.class) {
                PrivacyHelper.getLogger().log( "found class extend Thread:" + clazz);
                DexposedBridge.findAndHookMethod(clazz, "run", new ThreadMethodHook());
            }
            PrivacyHelper.getLogger().log("Thread: " + thread.getName() + " class:" + thread.getClass() +  " is created.");
        }
    });
    DexposedBridge.findAndHookMethod(Thread.class, "run", new ThreadMethodHook());
    ```


#### 2.2 监控dex文件的加载
- 如下所示
    ```
    DexposedBridge.findAndHookMethod(DexFile.class, "loadDex", String.class, String.class, int.class, new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            String dex = (String) param.args[0];
            String odex = (String) param.args[1];
            Log.i(TAG, "load dex, input:" + dex + ", output:" + odex);
        }
    });
    ```



### 03.Api调用说明
- 该库你只需要在`debug`包下依赖即可，无需你做什么初始化。
    ``` java
    PrivacyHelper.start(new NormalMethodList());
    ```
- 如果你觉得`NormalMethodList`中的隐私合规`api`不符合你的要求，你可以重写`HookMethodList`接口实现类，比如MyMethodList。
    ``` java
    PrivacyHelper.start(new MyMethodList());
    ```



### 04.测试case罗列



### 05.遇到的坑分析
#### 5.1 使用epic库缺陷
- 1.要使用epic的hook功能，需要依赖libepic.so库文件。
    - 但此库文件不支持默认cpu架构：armeabi。如果app仅支持armeabi架构的话，需要在编译时将armeabi-v7a架构的库文件打包入app中。否则app运行会因为找不到库文件而崩溃。
- 2.在hook方法时，尤其是hook系统方法时，非常有必要加try、catch Throwable异常捕获。
    - 因为可能会遇到在hook时由于访问权限等问题而导致抛出异常，进而崩溃。而即使加上异常捕获，有时也会遇到直接在native层出现异常，try、catch无法捕获的问题，那就没法解决了，这一点要注意。
- epic 具体原理分析可以看这篇文章
    - https://weishu.me/2017/11/23/dexposed-on-art/


#### 5.5 参考博客
- 利用Xposed工具检测隐私api调用
    - https://github.com/ChenJunsen/Hegui3.0
- 用户隐私安全规划
    - https://wiki.zuoyebang.cc/pages/viewpage.action?pageId=319020634
- epic是weishu大神开源的一个Hook框架，支持ART上的Java方法HOOK。
    - epic相当于ART上的Dexposed，所以也是Xposed-Style Method Hook。
- http://www.45fan.com/article.php?aid=1yyCmAXXM31pnFuG
- Android 隐私合规静态检查
    - https://mp.weixin.qq.com/s/U06HZb1P5Z2bK_ibtJk9KA
    - https://juejin.cn/post/7046269884207988744
- 隐私权限整理笔记：https://blog.csdn.net/zjy764219923/article/details/123559789







































