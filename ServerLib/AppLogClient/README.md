# 日志框架客户端
#### 目录介绍
- 01.基础概念说明
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明


#### 1.3 服务端和客户端
- **什么是客户端**
    - 客户端首先需要绑定服务端的Service，绑定成功后，将服务端返回的Binder对象转换成AIDL接口所属的类型，最后调用AIDL的方法就可以了。
- **什么是服务端**
    - 注意：服务端就是客户端要连接的进程。服务端给客户端一个Service，在这个Service中监听客户端的连接请求，然后创建一个AIDL接口文件，里面是将要实现的方法，注意这个方法是暴露给客户端的的。**在Service中实现这个AIDL接口即可**


#### 1.4 客户端绑定服务端
- 客户端绑定服务端service原理
    - 客户端也非常简单，首先我们连接到服务端Service，在连接成功时，也就是onServiceConnected方法里，通过asInterface(service)方法可以将服务端的Binder对象转换成客户端所需的AIDL的接口的对象。
    - 这种转换是区分进程的，如果是同一进程，那么此方法返回的就是Stub本身，否则返回的就是系统Stub.proxy对象。拿到接口对象之后，我们就能够调用相应方法进行自己的处理。



### 02.常见思路和做法
#### 2.1 AIDL通信简单思路
- 服务端步骤
    - 步骤1：新建定义AIDL文件，并声明该服务需要向客户端提供的接口。补充，如果aidl中有对象，则需要创建对象，并且实现Parcelable
    - 步骤2：在Service子类中实现AIDL中定义的接口方法，并定义生命周期的方法（onCreate、onBind()等等）
    - 步骤3：在AndroidManifest.xml中注册服务 & 声明为远程服务
- 客户端步骤
    - 步骤1：拷贝服务端的AIDL文件到目录下。注意：复制时不要改动任何东西！
    - 步骤2：使用Stub.asInterface接口获取服务器的Binder，根据需要调用服务提供的接口方法
    - 步骤3：通过Intent指定服务端的服务名称和所在包，绑定远程Service



### 03.Api调用说明


### 04.遇到的坑分析
#### 4.1 客户端在子线程中发起通信访问问题
- 当客户端发起远程请求时，客户端会挂起，一直等到服务端处理完并返回数据，所以远程通信是很耗时的，所以不能在子线程发起访问。
- 由于服务端的Binder方法运行在Binder线程池中，所以应采取同步的方式去实现，因为它已经运行在一个线程中呢。



#### 4.2 远程调用失败
- Binder是会意外死亡的。
    - 如果服务端的进程由于某种原因异常终止，会导致远程调用失败，如果我们不知道Binder连接已经断裂，那么客户端就会受到影响。
- 如何解决远端意外失败
    - 提供了连个配对的方法linkToDeath和unlinkToDeath，通过linkToDeath我们可以给Binder设置一个死亡代理，当Binder死亡时，我们就会收到通知。
    ``` java
    // 在创建ServiceConnection的匿名类中的onServiceConnected方法中
    // 设置死亡代理
    messageCenter.asBinder().linkToDeath(deathRecipient, 0);
    /**
     * 给binder设置死亡代理
     */
    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if(messageCenter == null){
                return;
            }
            //解除死亡代理
            messageCenter.asBinder().unlinkToDeath(deathRecipient, 0);
            messageCenter = null;
        }
    };
    ```




### 05.其他问题说明
#### 5.1 问题思考一下


