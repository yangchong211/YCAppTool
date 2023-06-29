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
    - 轮询可以实现监控系统的功能。当需要监控某个系统的状态时，可以使用轮询不断的查询该系统的状态信息，并及时的进行预防措施。
- 二维码扫码
    - 比如超市的支付硬件设备，会一直轮训扫码。当你靠近二维码支付的时候，就立马读取到二维码数据进行支付。


#### 1.2 什么是轮训
- 轮询（Poll）
    - 是一种计算机程序查询状态的方式，是指定期访问一个或多个设备或其他资源以获取它们的状态。


### 02.常见思路和做法
#### 2.1 实现业务轮训思路
- 具体有哪些可以实现轮训执行任务的操作
    - 第一种方案：使用Thread.sleep实现轮询。一直做while循环
    - 第二种方案：使用ScheduledExecutorService实现轮询
    - 第三种方案：使用Timer实现定时性周期性任务轮训


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


#### 2.4 两种方式有什么区别
- 相同点对比分析
    - 两种方式都可以实现轮训的操作。功能上都可以满足业务需求。开始循环，暂停循环，销毁循环等。
- 不同点对比分析
    - 第一种方式使用Thread.sleep实现轮询，当销毁之后，没有办法再次开始循环，因为线程interrupt，线程死亡。再次使用需要新创建线程对象！
    - 第二种方式使用ScheduledExecutorService线程池实现轮训，销毁之后还是可以再次开始循环操作。



### 03.Api调用说明
#### 3.1 最简单的API调用
- 第一步：创建对象，两种选其一即可。
    ```
    private final DefaultLoopThread defaultLoopThread = new DefaultLoopThread(){
        @Override
        public void doAction() {
            super.doAction();
            //todo 做你的轮训业务操作
        }
    };
    private final DefaultScheduledThread defaultLoopThread = new DefaultScheduledThread(){
        @Override
        public void doAction() {
            super.doAction();
            //todo 做你的轮训业务操作
        }
    };
    ```
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


#### 3.2 一些自定义功能
- 比如想要设置轮训的间隔时间，还有轮训的控制条件，该怎么处理？
    ```
    private final DefaultLoopThread defaultLoopThread = new DefaultLoopThread(){
        @Override
        public void doAction() {
            super.doAction();
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


#### 4.2


### 05.其他问题说明





