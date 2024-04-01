# 时间打点器工具库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明




### 01.基础概念说明
#### 1.1 业务场景说明
- 耗时就需要有效准确的数据
    - 应用在启动或者跳入某个页面时是否流畅，时间是否太长，仅仅通过肉眼来观察是非常不准确的，并且在不同设备和环境会有完全不同的表现。
    - 如何衡量支付业务高效可用，支付耗时也是一个衡量指标。在指标建设中，一些业务的耗时，是非常重要的。
- 业务逻辑统计耗时需手动打点
    - 比如想知道支付耗时，则需要记录刷码/刷卡的识别介质时间，到请求接口支付完成的时间，统计这之间的准确耗时，则需要开发手动打点统计。
    - 如何计算时间打点差值 = 结束时时间 -  开始时时间。实际业务中，有可能有这类场景，在A类中统计开始时间，在B类中统计结束时间。



#### 1.2 耗时监控什么
- 1.业务衡量指标建设。
    - 举一个例子，比如你去超市买东西，扫二维码支付，那么从识别二维码到支付成功的消耗时间是多少，有什么衡量标准。
- 2.一些关键函数耗时
    - 举一个例子，应用中有很多数据库增删该查的工作，那么如何衡量数据库业务稳定性。可以监听函数执行耗时，检测是否有卡顿现象。




### 02.常见思路和做法
#### 2.1 方案罗列和对比
- 目前打点统计耗时方案有多种
    - 第一种：直接用代码标记当前打点时间，然后在打点结束处，计算与之前点位的差值。对代码侵入性非常的大。
    - 第二种：在项目核心部分添加打点，插桩ASM也是可以的。比如统计activity启动耗时，但这种不适合业务打点统计耗时。
    - 第三种：统计耗时封装，使用Map集合存储打点情况，定义start和end方法，然后在end方法中计算耗时时差。使用业务耗时统计。
    - 第四种：拦截器耗时统计，借鉴拦截器思路，可以用于拦截核心方法的耗时逻辑，以及异常。使用函数耗时统计。
- 方案对比分析说明
    - 第一种：对代码入侵大，且代码不容易维护，这种会导致代码乱七八糟，很不友好。
    - 第二种：采用插桩方案，目前市面上有些统计函数耗时，采用这种可以无缝统计耗时。自动化比较高，无耦合。
    - 第三种：适用于业务打点，比如音视频聊天中，统计前后台到某页面耗时，统计业务逻辑耗时，这种统一管理比较友好。
    - 第四种：适合统计一些函数的执行耗时



### 03.Api调用说明
#### 3.1 业务打点耗时统计
- 首先初始化时间打点库
    ```
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
    ```
    TimeMonitorHelper.start("startActivity统计耗时");
    TimeMonitorHelper.end("startActivity统计耗时");
    ```


#### 3.2 统计函数执行耗时
- 首先初始化时间打点库
    ```
    InterceptorManager.getInstance().addInterceptor(new CommonCallback<InterceptorBean>() {
        @Override
        public void result(InterceptorBean interceptorBean) {
            //统计函数耗时
            AppLogUtils.d("addInterceptor: " + interceptorBean.toString());
        }
    });
    InterceptorManager.getInstance().setMonitorCallback(new CommonCallback<MonitorBean>() {
        @Override
        public void result(MonitorBean monitorBean) {
            //监听函数耗时卡顿时间
            AppLogUtils.d("addInterceptor monitorBean : " + monitorBean.toString());
        }
    });
    ```
- 如何高效打点。注意start和end打点的actionName必须是一样才会计算耗时
    ```
    InterceptorManager instance = InterceptorManager.getInstance();
    BaseParam baseParam = new BaseParam("函数名称");
    baseParam.setTimeout(5000);
    instance.handleInterceptor(baseParam, new ResultCallback() {
        @Override
        public BaseResult getResult() {
            BaseResult baseRt = new BaseResult();
            try {
                //这个地方做业务逻辑处理。
                Thread.sleep(6000);
                baseRt.setSuccess(true);
            } catch (Exception e) {
                e.printStackTrace();
                baseRt.setSuccess(false);
                baseRt.setException(e);
                baseRt.setErrMsg(e.toString());
            }
            return baseRt;
        }
    });
    ```



### 04.遇到的坑分析
#### 4.1 耗时时间1ms内的尴尬
- 对主要方法进行性能打点监测，起先用的是System.currentTimeMillis()，然而实操后发现，部分方法的耗时在1ms以内，大约几十微秒的区间，这就尴尬了。
- 所以打算切换到System.nanoTime()，但是问题来了：
    - System.nanoTime()拿的到底是什么时间，能不能准确测量出方法耗时呢？
    - System.nanoTime()本身耗时是怎样的呢？其本身会不会影响到方法的性能数据呢？



#### 4.2 currentTimeMillis性能问题
- currentTimeMillis()方法在性能方面存在一些问题。
    - 分辨率问题：currentTimeMillis()方法的分辨率通常是毫秒级别，这意味着它只能提供毫秒级别的时间精度。如果需要更高的时间精度，比如纳秒级别的精度，就需要使用System.nanoTime()方法。
    - 系统调用开销：currentTimeMillis()方法通常需要通过系统调用来获取当前时间，这会引入一定的开销。尽管这个开销通常很小，但在某些性能敏感的场景下，频繁调用currentTimeMillis()可能会对性能产生一定的影响。
    - 时钟漂移问题：currentTimeMillis()方法返回的时间是基于系统时钟的，而系统时钟可能会受到时钟漂移的影响。时钟漂移是指时钟的速度与实际时间的偏差，这可能导致currentTimeMillis()返回的时间不准确。



### 05.其他问题说明




