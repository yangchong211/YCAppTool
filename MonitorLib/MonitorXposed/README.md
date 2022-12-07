# 安全检查工具库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明


### 02.常见思路和做法
#### 2.1 Xposed框架的检测
- Xposed框架的检测如何实践
    - 第一种方式：获取当前设备所有运行的APP，根据安装包名对应用进行检测判断是否有Xposed环境。
    - 第二种方式：通过自造异常来检测堆栈信息，判断异常堆栈中是否包含Xposed等字符串。
    - 第三种方式：通过ClassLoader检查是否已经加载了XposedBridge类和XposedHelpers类来检测。
    - 第四种方式：获取DEX加载列表，判断其中是否包含XposedBridge.jar等字符串。
    - 第五种方式：检测Xposed相关文件，通过读取/proc/self/maps文件，查找Xposed相关jar或者so文件来检测。
- 每一种方式详细说明
    - 第一种：针对这种检测手段，用户可以自定义APP的权限，禁止相关APP获取应用列表即可绕过检测。也可以修改Xposed Installer包名。
    - 第二种：在正常的Android系统启动过程中，init进程会去解析init.rc文件启动一系列的服务，其中就有app_process进程，在app_process执行过程中，会设置自身进程名为Zygote，启动com.android.internal.os.ZygoteInit.Main方法。而Xposed修改了app_process进程，会先启动de.robv.android.xposed.XposedBridge.Main方法，再由它去启动com.android.internal.os.ZygoteInit.Main方法，因此堆栈信息中会多出一些内容。
    - 第三种：可以在应用内部关闭Xposed框架Hook的总开关，使其无法对应用程序进行Hook。


### 03.Api调用说明



### 04.遇到的坑分析


### 05.其他问题说明



### 参考博客
- Xposed框架的检测和突破方式
    - https://zhuanlan.zhihu.com/p/451667935
- 浅谈虚拟框架VirtualApp原理 & 检测方案
    - https://juejin.cn/post/6964673582924300296#heading-15
- VirtualApp官方介绍
    - https://jitpack.io/p/asLody/VirtualApp
