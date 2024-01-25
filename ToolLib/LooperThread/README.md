# 线程循环抽象库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.原理和遇到的坑
- 05.其他问题说明



### 01.基础概念介绍
#### 1.1 业务背景介绍
- 实时推送
    - 使用轮询的方式可以实现实时推送功能。当客户端连接到服务器之后，服务器就可以通过轮询不断的向客户端推送实时消息。
- 监控系统
    - 轮询可以实现监控系统的功能。当需要监控某个系统的状态时，可以使用轮询不断的查询该系统的状态信息，并及时的进行预防措施，或者做数据上报操作。
- 二维码扫码
    - 比如超市的支付硬件设备，会一直轮训扫码。当你靠近二维码支付的时候，就立马读取到二维码数据进行支付。



#### 1.2 什么是轮询
- 轮询（Poll）
    - 是一种计算机程序查询状态的方式，是指定期访问一个或多个设备或其他资源以获取它们的状态。
- 简单理解轮询
    - 简单理解就是 App 端每隔一定的时间重复请求的操作就叫做轮询操作，比如：App 端每隔一段时间上报一次定位信息，App 端每隔一段时间拉去一次用户状态等，这些应该都是轮询请求。



#### 1.3 需求技术分析
- 业务需求的实现目标
    - 1.项目中多处要使用到轮询，刚开始在业务代码中实现，随着业务增多，轮询功能和业务耦合严重。为了让该功能完全解耦合，因此简单封装！
    - 2.不管是哪一种技术方式实现轮询操作，功能上都可以满足业务需求，都需要有开始轮询，停止轮询，销毁等API方法。抽象一套统一的API接口
- 注意事项说明
    - 轮训操作一般都有一定的生命周期，比如在某个页面打开时启动轮训操作，在某个页面关闭时取消轮训操作；
    - 轮训操作的请求间隔需要根据具体的需求确定，还要注意轮询；




### 02.常见思路和做法
#### 2.1 实现业务轮训思路
- 具体有哪些可以实现轮训执行任务的操作
    - 第一种方案：使用Thread.sleep实现轮询。一直做while循环
    - 第二种方案：使用ScheduledExecutorService实现轮询
    - 第三种方案：使用Timer实现定时性周期性任务轮训
    - 第四种方案：使用Handler不断发送消息来实现轮训
- 从轮询执行角度分析任务执行类型
    - 第一种属于固定速率执行，每个执行都相对于初始执行的计划执行时间进行调度。如果由于任何原因而延迟执行，则两个或更多执行将快速连续发生以“赶上”。
    - 第二种属于固定延迟执行，每个执行都相对于前一次执行的实际执行时间进行调度。如果由于任何原因延迟执行，后续执行也将被延迟。
- 每一种方案对应的执行类型说明
    - 第一种，第四种：是属于固定延迟执行
    - 第二种，第三种：是属于固定速率执行




#### 2.2 使用Thread.sleep实现轮询
- 简单来说，写一个线程执行while死循环操作。然后使用sleep让当前线程睡眠，然后执行任务操作。
    ```
    public void simplePolling() {
        while (true) {
            try {
                // 线程睡眠1秒钟
                Thread.sleep(1000);
                // 轮询的代码
                System.out.println("polling...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    ```


#### 2.3 使用ScheduledExecutorService实现轮询
- 通过创建ScheduledExecutorService线程池实现轮询，scheduleAtFixedRate方法会在0秒后开始轮询，并且每隔1秒进行轮询。
    ```
    public void scheduledPolling() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            // 轮询的代码
            System.out.println("polling...");
        }, 0, 1, TimeUnit.SECONDS);
    }
    ```


#### 2.4 使用Timer实现定时性周期性任务轮训
- 使用timer定时执行TimerTask，然后开始延迟轮询操作
    ```
    timer = new Timer();
    timer.schedule(new CommitTimer(), 0, getSleepTime());
    private class CommitTimer extends TimerTask {
        @Override
        public void run() {
            // 轮询的代码
            doAction();
        }
    }
    ```



#### 2.5 使用Handler不断发送消息来实现轮训
- 在HandleMessage方法中执行任务，任务结束后向MessageQueue中添加延时消息。
    ```
    HandlerThread handlerThread = new HandlerThread("LooperThread");
    handlerThread.start();
    handler = new Handler(handlerThread.getLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_GET_COMPARE_RESULT) {
                // 轮询的代码
                doAction();
                //向MessageQueue中添加延时消息，后重复执行以上任务
                handler.sendEmptyMessageDelayed(MSG_GET_COMPARE_RESULT, getSleepTime());
            }
        }
    };
    ```


#### 2.6 方式有什么区别
- 使用Thread.sleep实现轮询
    - 使用Thread.sleep实现轮询，当销毁之后，没有办法再次开始循环，因为线程interrupt，线程死亡。再次使用需要新创建线程对象！
- 使用ScheduledExecutorService实现轮询
    - 使用ScheduledExecutorService线程池实现轮训，销毁之后还是可以再次开始循环操作。
- 使用Timer实现定时性周期性任务轮训
    - 使用timer定时执行TimerTask，如果有异步任务，下次任务开始执行时需要判断上次任务是否完成，从而导致任务间隔时间不可控。
- 使用Handler不断发送消息来实现轮训
    - 如果有异步任务，只需在异步任务执行完毕后再向MessageQueue中添加延时消息，任务间隔时间可控。



#### 2.7 执行轮询任务分析
- 使用Thread.sleep实现轮询
    - 问题1：线程sleep出现了异常是否会中断循环？
    - 答案是：无法通过中断形式退出线程。因为在sleep里catch到了InterruptedException。然后在这里线程的状态被更新了。所以线程并不能停止。当进行Thread.interrupted进行判断时，发现还是是false。
    - 问题2：如何跳出while循环操作不执行doAction任务？
    - 答案是：直接通过一个布尔值变量控制，注意它并没有干掉线程循环，只是通过条件让其不执行任务操作。
    - 问题3：使用sleep方法延迟阻塞线程，对轮询的性能消耗是否有大的影响？有没有更好的方式？
    - 答案是：探讨中






### 03.Api调用说明
#### 3.1 切换方式API不变
- 第一步：创建对象，关于多种方式，不管使用哪一种，Api都是一样的。
    ```
    private final IDoAction defaultLoopThread = new DefaultLoopThread(){
        @Override
        public void doAction() {
            super.doAction();
            //todo 做你的轮训业务操作
        }
    };
    ```


#### 3.2 最简单的API调用
- 第二步：调用Api
    ```
    //启动线程
    defaultLoopThread.startThread();
    //开始循环
    defaultLoopThread.beginLoop();
    //暂停循环
    defaultLoopThread.endLoop();
    //释放操作
    defaultLoopThread.release();
    ```


#### 3.3 一些自定义功能
- 比如想要设置轮训的间隔时间，还有轮训的控制条件，该怎么处理？
    ```
    private final DefaultLoopThread defaultLoopThread = new DefaultLoopThread(){
        @Override
        public void doAction() {
            super.doAction();
            //轮询做的操作
        }

        @Override
        public boolean isLoop() {
            //是否轮训的判断条件
            return true;
        }

        @Override
        public long getSleepTime() {
            //轮训的时间间隔
            return 1000;
        }
    };
    ```



### 04.原理和遇到的坑
#### 4.1 scheduleAtFixedRate原理
- https://blog.csdn.net/Dwade_mia/article/details/89512722
- https://www.dovov.com/androidvs-scheduleatfixedrate.html


#### 4.2 销毁做的操作



#### 4.3 遇到的坑说明


### 05.其他问题说明
#### 5.1 轮询性能探讨分析


#### 5.2 如何优化轮询策略

- https://codeleading.com/article/93393410652/






