#### 目录介绍
- 01.基础概念
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念
#### 1.1 业务场景说明
- 业务场景说明
    - Toast只会弹出一段信息，告诉用户某某事情已经发生了，过一段时间后就会自动消失。它不会阻挡用户的任何操作。
    - Toast是没有焦点，而且Toast显示的时间有限，过一定的时间就会自动消失。


#### 1.2 吐司是系统级别
- 吐司是系统级别的窗口
    - 经常看到的一个场景就是你在你的应用出调用了多次 Toast.show函数，然后退回到桌面，结果发现桌面也会弹出 Toast，就是因为系统的 Toast 使用了系统窗口，具有高的层级。


### 02.常见思路和做法
#### 2.1 先来思考几个问题
- 吐司：吐司弹出后过一段时间自动消失是如何实现的？连续多次Show后吐司连续吐出是如何实现的？如何设计自动销毁？
- 吐司：吐司如何实现跨进程通信？为什么要使用Aidl技术？为何activity销毁后toast仍会显示？
- 吐司：如何将View加载到吐司视图中？其核心原理是什么？为什么不支持输入事件？
- 吐司：吐司中token作用是什么？Binder具体起到什么作用？开发者为何要引入Binder？


#### 2.2 创建源码流程



#### 2.3 展示源码流程


#### 2.4 隐藏流程分析



### 03.Api调用说明
#### 3.1 如何依赖
- 直接在maven中依赖如下所示：
    ``` java
    //activity栈管理
    implementation 'com.github.yangchong211.YCCommonLib:ActivityManager:1.4.9'
    ```


#### 3.2 如何使用Api
- api调用如下所示，直接拿来用即可
    ``` java

    ```



### 04.遇到的坑分析
#### 4.1 最简单使用的弊端
- 一行代码调用，十分方便，但是这样存在一种弊端。
    ```
    Toast.makeText(this,"吐司",Toast.LENGTH_SHORT).show();
    ```
- 使用中遇到的问题：
    - 例如，当点击有些按钮，需要吐司进行提示时；快速连续点击了多次按钮，Toast就触发了多次。
    - 系统会将这些Toast信息提示框放到队列中，等前一个Toast信息提示框关闭后才会显示下一个Toast信息提示框。可能导致Toast就长时间关闭不掉了。
    - 又或者我们其实已在进行其他操作了，应该弹出新的Toast提示，而上一个Toast却还没显示结束。
- 解决办法避免重复创建
    - 先判断Toast对象是否为空，如果是空的情况下才会调用makeText()方法来去生成一个Toast对象，否则就直接调用setText()方法来设置显示的内容，最后再调用show()方法将Toast显示出来。
    - 由于不会每次调用的时候都生成新的Toast对象，因此刚才我们遇到的问题在这里就不会出现。
    ```
    /**
     * 吐司工具类    避免点击多次导致吐司多次，最后导致Toast就长时间关闭不掉了
     * 注意：这里如果传入context会报内存泄漏；传递activity.getApplicationContext()
     * @param content       吐司内容
     */
    private static Toast toast;
    public static void showToast(String content) {
        checkContext();
        if (toast == null) {
            toast = Toast.makeText(mApp, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
    ```




### 05.其他问题说明









