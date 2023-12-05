# APM内存采集工具
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明
#### 1.1 APP内存基础概念
- 程序内存被分为2部分：
    - native和dalvik
    - dalvik就是我们普通的Java使用内存分析堆栈的时候使用的内存。我们创建的对象是在这里面分配的。
    - 对于内存的限制是 native+dalvik 不能超过最大限制，Android 原生系统一般默认16M。
    - 但是国内手机一般都是特殊定制的，都有修改系统的内存大小，所有有时候，要查看具体应用系统分配的内存大小，还是需要实际去测试的,


#### 1.2 两种方式获取内存
- 分别有两种方式
    - 方式1: 使用ActivityManager获取内存
    - 方式2: 使用Runtime方法获取内存
- 对比分析可以知道



#### 1.3 获取进程内存信息
- 调用ActivityManager的getProcessMemoryInfo(int[] pids)方法。
    - 此方法返回一个或多个进程的内存使用情况的信息。从 Android Q 开始，对于常规应用程序，此方法将只返回有关调用者 uid 运行的进程的内存信息；没有其他进程内存信息可用将为零。
    - 同样在 Android Q 中，此 API 允许的采样率受到很大限制，如果调用速度更快，将收到与上一次调用相同的数据。





### 02.常见思路和做法
#### 2.1 思路分析说明
- 第一步：开启Activity Manager
    - 通过Activity Manager来获取进程信息和进程内存信息。Activity Manager是一个系统服务，可以用来管理应用的活动（Activity）和进程（Process）。
- 第二步：获取进程信息
    - 需要获取当前运行的所有进程的信息。通过Activity Manager的getRunningAppProcesses()方法可以获得一个List<ActivityManager.RunningAppProcessInfo>，其中包含了每个正在运行的进程的信息。
- 第三步：获取进程内存信息
    - 通过遍历进程列表，我们可以获取到每个进程的内存信息。
    - 其中ActivityManager.RunningAppProcessInfo对象的pid属性表示进程的ID，通过Debug.MemoryInfo类的getMemoryInfo(pid)方法可以获取进程的内存信息。
- 第四步：计算App内存大小
    - 可以通过memoryInfo对象的 getTotalPss()方法获取到进程的总内存大小。需要注意的是，内存大小以KB为单位。







### 03.Api调用说明



### 04.遇到的坑分析


### 05.其他问题说明

10.36.189.78

### 参考博客
- android获取进程内存使用信息
  - https://blog.csdn.net/CJohn1994/article/details/126493311
- 爆肝写了个Android性能监测工具
  - https://zhuanlan.zhihu.com/p/526679243
  - https://www.jianshu.com/p/a283409c3d1c
