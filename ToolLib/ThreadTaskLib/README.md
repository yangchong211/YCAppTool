## TaskScheduler


Android的日常开发中，经常需要处理异步任务，系统提供的Handler和Asynctask实在不方便，一些开源的第三方库由于过于庞大和复杂，例如RxJava，虽然很方便，但RxJava在团队中不容易推广，所以实现了一个简洁、方便、实用的异步库。

**该异步库已经用于多个日活过百万线上项目中，暂时没发现什么问题，由于线上项目对崩溃率的要求比较严格，可以规避了执行异步任务或UI操作等带来的崩溃**。这个库因为单独抽离出来，实际线上会有些差别，但核心思想是一样的，相对原来的更简洁一些。

这个库用起来会很简单，只提供少量的接口，但功能会很强大，其实目的也就是这，就是利用已经相对完善的jdk或者sdk提供的类进行一定的封装，让异步任务更简洁，没必要提供很多看起来功能很全但实际中基本上用不到的接口，达到一看到接口基本就能看懂该怎么用，避免臃肿。

## 绑定生命周期

**支持当通过Handler的方式在Activity、Fragment中执行或延迟执行匿名内部类的runnable时，当onDestroy时，runnable自动被移除，极大简化使用,避免内存泄漏**

在Activity里或者Fragment里使用时,在onDestory不需要显示的移除匿名内部类（注意support版本要大于27，官方建议一直用最新的compileSdkVersion和supportVersion）

```java

TaskScheduler.runOnUIThread(this,new Runnable() {
    @Override
    public void run() {
        
    }
},5000);
```
可指定在特定生命周期移除未执行任务，如onStop

```java
TaskScheduler.runOnUIThread(this, Lifecycle.Event.ON_STOP, new Runnable() {
    @Override
    public void run() {
        Log.i("LifeFragment","runTask with life on Stop");
    }
},5000);
```

外部传入任意Handler

```java
TaskScheduler.runLifecycleRunnable(this,anyHandler,,new Runnable() {
    @Override
    public void run() {
        
    }
},5000);


```


## Using

#### 简单的不需要回调的异步任务

```java
TaskScheduler.execute(new Runnable() {
    @Override
    public void run() {
    // DO BACKGROUND WORK
    }
});
```

####  异步需要回调的任务

```java
Task task = new Task<String>() {

    @Override
    public String doInBackground()  {
        return "background task";
    }

    @Override
    public void onSuccess(String result) {
       //回调到主线程
    }

    @Override
    public void onFail(Throwable throwable) {
        super.onFail(throwable);
        //doInBackground 里发生错误时回调
    }

    @Override
    public void onCancel() {
        super.onCancel();
        //任务被取消时回调
    }
});
```
doInBackground 和 onSuccess(Object result)时默认必须实现的接口，其他实现都是可选的。除了doInBackground在异步线程中执行，其他的都会回调到主线程执行。

**执行任务**

```java
TaskScheduler.execute(task);

```

**取消任务**

将会回调到onCancel(),没法真正取消正在执行的任务，只是结果不在onSuccess里回调， 不一定能让任务停止，和AsyncTask同样道理，可参考之前写的一篇博客[为什么AsyncTask的cancel不能真正的让线程终止运行](http://silencedut.com/2016/07/08/基于最新版本的AsyncTask源码解读及AsyncTask的黑暗面/)

```java
TaskScheduler.cancel(task);

```

**超时任务**

如果任务超时，将会回调到onCancel()

```java
TaskScheduler.executeTimeOutTask(timeOutMillis,task);

```

**周期性任务**

如果任务超时，将会回调到onCancel()

```java

主线程,Io线程可选
TaskScheduler.scheduleUITask( SchedulerTask task);

取消任务
TaskScheduler.stopScheduleTask(SchedulerTask task) 

```

**其他的一些常用方法**


```java
/**
*获取回调到handlerName线程的handler.一般用于在一个后台线程执行同一种任务，避免线程安全问题。如数据库，文件操作
*/
Handler provideHandler(String handlerName)

 /**
 * 提供一个公用的异步handler
 */
Handler ioHandler()

/**
*常用的handler的操作
*/
runOnUIThread(@NonNull Runnable runnable)

runOnUIThread(Runnable runnable,long delayed)

removeUICallback(@NonNull Runnable runnable)


```




