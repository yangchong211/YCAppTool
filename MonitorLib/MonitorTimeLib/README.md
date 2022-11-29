# 时间打点器工具库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明
- 耗时就需要有效准确的数据
    - 应用在启动或者跳入某个页面时是否流畅，时间是否太长，仅仅通过肉眼来观察是非常不准确的，并且在不同设备和环境会有完全不同的表现。
- 业务逻辑统计耗时需手动打点
    - 比如音视频聊天，当从桌面悬浮窗点击回到通话页面，需要统计这之间的准确耗时，则需要开发手动打点统计。
- 如何计算时间打点差值
    - 开始时埋点，启动结束时埋点，然后计算二者差值。


### 02.常见思路和做法
- 目前打点统计耗时方案有多种
    - 第一种：直接用代码标记当前打点时间，然后在打点结束处，计算与之前点位的差值。对代码侵入性非常的大。
    - 第二种：在项目核心部分添加打点，插桩也是可以的。比如统计activity启动耗时，但这种不适合业务打点统计耗时。
    - 第三种：统计耗时封装，使用Map集合存储打点情况，定义start和end方法，然后在end方法中计算耗时时差。
- 方案对比分析说明
    - 第一种：对代码入侵大，且代码不容易维护，这种会导致代码乱七八糟，很不友好。
    - 第二种：采用插桩方案，目前市面上有些统计函数耗时，采用这种可以无缝统计耗时。自动化比较高，无耦合。
    - 第三种：适用于业务打点，比如音视频聊天中，统计前后台到某页面耗时，统计业务逻辑耗时，这种统一管理比较友好。



### 03.Api调用说明
- 首先初始化时间打点库
    ``` java
    // 初始化打点计时器
    // 第一个参数是是否开启打点，如果是true则打点，如果是false则表示不会记录打点
    // 第二个参数是自定义打印接口适配器，主要是自定义打印日志
    TimeMonitorHelper.init(true,null);
    // 设置自定义监听日志listener，所有的打点都会回调该方法
    TimeMonitorHelper.setOnMonitorListener(new OnMonitorListener() {
        @Override
        public void onMonitorResult(String processName, String result) {
            AppLogUtils.d("TimeMonitor result: " + result);
        }
    });
    ```
- 如何高效打点。注意start和end打点的actionName必须是一样才会计算耗时
    ``` java
    TimeMonitorHelper.start("startActivity统计耗时");
    TimeMonitorHelper.end("startActivity统计耗时");
    ```



### 04.遇到的坑分析
#### 4.1 currentTimeMillis性能问题
- https://blog.csdn.net/androidstarjack/article/details/119466232
- https://www.jianshu.com/p/060e045bfe3c



### 05.其他问题说明




