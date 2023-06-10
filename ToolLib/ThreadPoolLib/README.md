### 目录介绍
- 01.开源库介绍
- 02.遇到的需求
- 03.为何用线程池
- 04.封装库功能
- 05.封装库使用
- 06.部分重点代码
- 07.线程池思考


### 01.开源库介绍
- 轻量级线程池封装库，支持线程执行过程中状态回调监测(包含成功，失败，异常等多种状态)；支持创建异步任务，并且可以设置线程的名称，延迟执行时间，线程优先级，回调callback等；可以根据自己需要创建自己需要的线程池，一共有四种；线程异常时，可以打印异常日志，避免崩溃。
- 关于线程池，对于开发来说是十分重要，但是又有点难以理解或者运用。关于写线程池的博客网上已经有很多了，但是一般很少有看到的实际案例或者封装的库，许多博客也仅仅是介绍了线程池的概念，方法，或者部分源码分析，那么为了方便管理线程任务操作，所以才想结合实际案例是不是更容易理解线程池，更多可以参考代码。




### 02.遇到的需求
#### 1.1 遇到的问题有哪些？
- 继承Thread，或者实现接口Runnable来开启一个子线程，无法准确地知道线程什么时候执行完成并获得到线程执行完成后返回的结果
- 当线程出现异常的时候，如何避免导致崩溃问题，也就是说如何捕获在run方法中执行异常的信息……
- 执行一个耗时操作，怎么样才能处理执行前start，执行中run，执行成功回调success，执行失败回调error等状态呢？



#### 1.2 预期想要达到的目标
- 如何在实际开发中配置线程的优先级
- 开启一个线程，是否可以监听Runnable接口中run方法操作的过程，比如监听线程的状态开始，成功，异常，完成等多种状态。
- 开启一个线程，是否可以监听Callable<T>接口中call()方法操作的过程，比如监听线程的状态开始，错误异常，完成等多种状态。


#### 1.3 多线程通过实现Runnable弊端
- **1.3.1 一般开启线程的操作如下所示**
    ```
    new Thread(new Runnable() {
        @Override
        public void run() {
            //做一些任务
        }
    }).start();
    ```
    - 创建了一个线程并执行，它在任务结束后GC会自动回收该线程。
    - 在线程并发不多的程序中确实不错，而假如这个程序有很多地方需要开启大量线程来处理任务，那么如果还是用上述的方式去创建线程处理的话，那么将导致系统的性能表现的非常糟糕。



- **1.3.2 主要的弊端有这些，可能总结并不全面**
    - 大量的线程创建、执行和销毁是非常耗cpu和内存的，这样将直接影响系统的吞吐量，导致性能急剧下降，如果内存资源占用的比较多，还很可能造成OOM
    - 大量的线程的创建和销毁很容易导致GC频繁的执行，从而发生内存抖动现象，而发生了内存抖动，对于移动端来说，最大的影响就是造成界面卡顿
    - 线程的创建和销毁都需要时间，当有大量的线程创建和销毁时，那么这些时间的消耗则比较明显，将导致性能上的缺失




#### 1.4 使用线程池一些思考
- 线程池这个定义就是个容易让人误解的术语，因为 ExecutorService 除了通常意义上“池”的功能，还提供了更全面的线程管理、任务提交等方法。
- Executor 框架可不仅仅是线程池，至少下面几点值得深入学习：
    - 掌握 Executor 框架的主要内容，至少要了解组成与职责，掌握基本开发用例中的使用。
    - 对线程池和相关并发工具类型的理解，甚至是源码层面的掌握。
    - 实践中有哪些常见问题，基本的诊断思路是怎样的。
    - 如何根据自身应用特点合理使用线程池。





### 03.为何用线程池
- 使用线程池管理线程优点
    - ①降低系统资源消耗，通过重用已存在的线程，降低线程创建和销毁造成的消耗；
    - ②提高系统响应速度，当有任务到达时，无需等待新线程的创建便能立即执行；
    - ③方便线程并发数的管控，线程若是无限制的创建，不仅会额外消耗大量系统资源，更是占用过多资源而阻塞系统或oom等状况，从而降低系统的稳定性。线程池能有效管控线程，统一分配、调优，提供资源使用率；
    - ④更强大的功能，线程池提供了定时、定期以及可控线程数等功能的线程池，使用方便简单。
- 在现实应用中，理解应用与线程池的交互和线程池的内部工作过程，可以参考下图。
    - ![image](https://img-blog.csdn.net/20180820215057504)
- 为了让你能对线程生命周期有个更加清晰的印象，这里画了一个简单的状态流转图，对线程池的可能状态和其内部方法之间进行了对应。
    - ![image](https://img-blog.csdn.net/20180820220845460)




### 04.封装库功能
- 支持线程执行过程中状态回调监测(包含成功，失败，异常等多种状态)
- 支持线程异常检测，并且可以打印异常日志，能自动将catch异常信息传递给用户，避免出现crash
- 支持设置线程属性，比如名称，延时时长，优先级，callback
- 支持异步开启线程任务，支持监听异步回调监听，并且回传数据
- 方便集成，方便使用，可以灵活选择创建不同的线程池



### 05.封装库具体使用
#### 5.1 一键集成
- 如下所示
    ```
    //异步线程池库，简易版本
    implementation 'com.github.yangchong211.YCThreadPool:ThreadPoolLib:1.3.8'
    //简易版本线程池库
    implementation 'com.github.yangchong211.YCThreadPool:EasyExecutor:1.3.8'
    ```

#### 5.2 在application中初始化库
- 只需要在一处中初始化，即可全局使用……
    ```
    public class App extends Application{

        private static App instance;
        private PoolThread executor;

        public static synchronized App getInstance() {
            if (null == instance) {
                instance = new App();
            }
            return instance;
        }

        public App(){}

        @Override
        public void onCreate() {
            super.onCreate();
            instance = this;
            //初始化线程池管理器
            initThreadPool();
        }

        /**
         * 初始化线程池管理器
         */
        private void initThreadPool() {
            // 创建一个独立的实例进行使用
            executor = PoolThread.ThreadBuilder
                    .createFixed(5)
                    .setPriority(Thread.MAX_PRIORITY)
                    .setCallback(new LogCallback())
                    .build();
        }

        /**
         * 获取线程池管理器对象，统一的管理器维护所有的线程池
         * @return                      executor对象
         */
        public PoolThread getExecutor(){
            return executor;
        }
    }


    //自定义回调监听callback，可以全局设置，也可以单独设置。都行
    public class LogCallback implements ThreadCallback {

        private final String TAG = "LogCallback";

        @Override
        public void onError(String name, Throwable t) {
            Log.e(TAG, "LogCallback"+"------onError"+"-----"+name+"----"+Thread.currentThread()+"----"+t.getMessage());
        }

        @Override
        public void onCompleted(String name) {
            Log.e(TAG, "LogCallback"+"------onCompleted"+"-----"+name+"----"+Thread.currentThread());
        }

        @Override
        public void onStart(String name) {
            Log.e(TAG, "LogCallback"+"------onStart"+"-----"+name+"----"+Thread.currentThread());
        }
    }
    ```


#### 5.3 最简单的runnable线程调用方式
- 关于设置callback回调监听，我这里在app初始化的时候设置了全局的logCallBack，所以这里没有添加，对于每个单独的执行任务，可以添加独立callback。
    ```
    PoolThread executor = App.getInstance().getExecutor();
            executor.setName("最简单的线程调用方式");
            executor.setDeliver(new AndroidDeliver());
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Log.e("MainActivity","最简单的线程调用方式");
                }
            });
    ```


#### 5.4 普通Callable任务
- 代码如下所示
    ```
    PoolThread executor = App.getInstance().getExecutor();
    executor.setName("延迟时间执行任务");
    executor.setDelay(2, TimeUnit.SECONDS);
    executor.setDeliver(new AndroidDeliver());
    Future<String> submit = executor.submit(new Callable<String>() {
        @Override
        public String call() throws Exception {
            Log.d("PoolThreadstartThread4","startThread4---call");
            Thread.sleep(2000);
            String str = "小杨逗比";
            return str;
        }
    });
    try {
        String result = submit.get();
        Log.d("PoolThreadstartThread4","startThread4-----"+result);
    } catch (InterruptedException e) {
        e.printStackTrace();
    } catch (ExecutionException e) {
        e.printStackTrace();
    }
    ```




#### 5.5 最简单的异步回调
- 如下所示
    ```
    PoolThread executor = App.getInstance().getExecutor();
            executor.setName("异步回调");
            executor.setDelay(2,TimeUnit.MILLISECONDS);
            // 启动异步任务
            executor.async(new Callable<Login>(){
                @Override
                public Login call() throws Exception {
                    // 做一些耗时操作
                    return null;
                }
            }, new AsyncCallback<Login>() {
                @Override
                public void onSuccess(Login user) {
                    Log.e("AsyncCallback","成功");
                }

                @Override
                public void onFailed(Throwable t) {
                    Log.e("AsyncCallback","失败");
                }

                @Override
                public void onStart(String threadName) {
                    Log.e("AsyncCallback","开始");
                }
            });
    ```


#### 5.6 其他api说明
- api说明
    ```
    //设置为当前的任务设置线程名
    executor.setName("延迟时间执行任务");
    //设置当前任务的延迟时间
    executor.setDelay(2, TimeUnit.SECONDS);
    //设置当前任务的线程传递
    executor.setDeliver(new AndroidDeliver());
    
    //关闭线程池操作
    executor.stop();
    //销毁的时候可以调用这个方法
    executor.close();
    executor.setCallback(new ThreadCallback() {
        @Override
        public void onError(String threadName, Throwable t) {
    
        }
    
        @Override
        public void onCompleted(String threadName) {
    
        }
    
        @Override
        public void onStart(String threadName) {
    
        }
    });
    ```



### 06.部分重点代码
- 比如，自定义类，继承runnable类的代码如下所示
    ```
    public final class RunnableWrapper implements Runnable {

        private String name;
        private NormalCallback normal;
        private Runnable runnable;
        private Callable callable;

        public RunnableWrapper(ThreadConfigs configs) {
            this.name = configs.name;
            this.normal = new NormalCallback(configs.callback, configs.deliver, configs.asyncCallback);
        }

        /**
         * 启动异步任务，普通的
         * @param runnable              runnable
         * @return                      对象
         */
        public RunnableWrapper setRunnable(Runnable runnable) {
            this.runnable = runnable;
            return this;
        }

        /**
         * 异步任务，回调用于接收可调用任务的结果
         * @param callable              callable
         * @return                      对象
         */
        public RunnableWrapper setCallable(Callable callable) {
            this.callable = callable;
            return this;
        }

        /**
         * 自定义xxRunnable继承Runnable，实现run方法
         * 详细可以看我的GitHub：https://github.com/yangchong211
         */
        @Override
        public void run() {
            //获取线程
            Thread current = Thread.currentThread();
            //重置线程
            ThreadToolUtils.resetThread(current, name, normal);
            //开始
            normal.onStart(name);
            //注意需要判断runnable，callable非空
            // avoid NullPointException
            if (runnable != null) {
                runnable.run();
            } else if (callable != null) {
                try {
                    Object result = callable.call();
                    //监听成功
                    normal.onSuccess(result);
                } catch (Exception e) {
                    //监听异常
                    normal.onError(name, e);
                }
            }
            //监听完成
            normal.onCompleted(name);
        }
    }
    ```
- 自定义回调Callback接口
    - 添加回调接口AsyncCallback和ThreadCallback
        - 注意，这个写的自定义callback，需要添加多种状态，你可以自定义其他状态。看完了这里再回过头看看RunnableWrapper中run方法和CallableWrapper中call方法的逻辑。[博客](https://github.com/yangchong211/YCBlogs)
    - **为什么要这样设计**
        - 它可以让程序员准确地知道线程什么时候执行完成并获得到线程执行完成后返回的结果。
        - AsyncCallback，在这个类中，可以看到三种状态[这个是在自定义Runnable中的run方法中实现]，并且成功时可以携带结果，在异常时还可以打印异常日志。
        - ThreadCallback，在这个类中，可以看到三种状态[这个是在自定义Callable<T>中的call方法中实现]，并且在异常时可以打印异常日志，在开始和完成时可以打印线程名称
    - 案例中的代码
        ```
        public interface ThreadCallback {
            /**
             * 当线程发生错误时，将调用此方法。
             * @param threadName            正在运行线程的名字
             * @param t                     异常
             */
            void onError(String threadName, Throwable t);

            /**
             * 通知用户知道它已经完成
             * @param threadName            正在运行线程的名字
             */
            void onCompleted(String threadName);

            /**
             * 通知用户任务开始运行
             * @param threadName            正在运行线程的名字
             */
            void onStart(String threadName);
        }

        public interface AsyncCallback<T> {
            /**
             * 成功时调用
             * @param t         泛型
             */
            void onSuccess(T t);

            /**
             * 异常时调用
             * @param t         异常
             */
            void onFailed(Throwable t);


            /**
             * 通知用户任务开始运行
             * @param threadName            正在运行线程的名字
             */
            void onStart(String threadName);
        }
        ```


### 07.线程池思考
- 线程池虽然为提供了非常强大、方便的功能，但是也不是银弹，使用不当同样会导致问题。我这里介绍些典型情况，经过前面的分析，很多方面可以自然的推导出来。
    - 避免任务堆积。比如 newFixedThreadPool 是创建指定数目的线程，但是其工作队列是无界的，如果工作线程数目太少，导致处理跟不上入队的速度，这就很有可能占用大量系统内存，甚至是出现 OOM。诊断时，你可以使用 jmap 之类的工具，查看是否有大量的任务对象入队。
    - 避免过度扩展线程。通常在处理大量短时任务时，使用缓存的线程池，比如在最新的 HTTP/2 client API 中，目前的默认实现就是如此。我们在创建线程池的时候，并不能准确预计任务压力有多大、数据特征是什么样子（大部分请求是 1K 、100K 还是 1M 以上？），所以很难明确设定一个线程数目。
    - 另外，如果线程数目不断增长（可以使用 jstack 等工具检查），也需要警惕另外一种可能性，就是线程泄漏，这种情况往往是因为任务逻辑有问题，导致工作线程迟迟不能被释放。建议你排查下线程栈，很有可能多个线程都是卡在近似的代码处。




### 其他内容介绍
#### 关于其他内容介绍
![image](https://upload-images.jianshu.io/upload_images/4432347-7100c8e5a455c3ee.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



#### 02.关于我的博客
- github：https://github.com/yangchong211
- 简书：http://www.jianshu.com/u/b7b2c6ed9284
- csdn：http://my.csdn.net/m0_37700275
- 喜马拉雅听书：http://www.ximalaya.com/zhubo/71989305/
- 开源中国：https://my.oschina.net/zbj1618/blog
- 泡在网上的日子：http://www.jcodecraeer.com/member/content_list.php?channelid=1
- 邮箱：yangchong211@163.com
- 阿里云博客：https://yq.aliyun.com/users/article?spm=5176.100- 239.headeruserinfo.3.dT4bcV
- segmentfault头条：https://segmentfault.com/u/xiangjianyu/articles
- 掘金：https://juejin.im/user/5939433efe88c2006afa0c6e



#### 03.勘误及提问
- 如果有疑问或者发现错误，可以在相应的 issues 进行提问或勘误。如果喜欢或者有所启发，欢迎star，对作者也是一种鼓励。



#### 04.关于LICENSE
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
