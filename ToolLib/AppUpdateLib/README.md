# 版本更新库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析



### 01.基础概念介绍
#### 1.1 版本更新做法
- 



### 02.常见思路和做法
#### 2.1 DownloadManager
- 使用场景：
    - 它是Android系统自带的下载管理工具，此工具可以很好的调度好下载。在没有特殊需求的情况下，一般是推荐使用此工具下载的。另外这个工具下载还有有优势就是可以在下载app完成后直接跳转到安装页面。
- DownloadManager的核心流程
    - 第一步：创建下载请求，这一步主要是设置下载url，标题，内容，通知栏。然后调用enqueue发送一个请求，这个时候会创建一个下载任务，这个请求会分配一个下载task任务id。
    - 第二步：需要注册一个监听。要在下载完成的时候获得一个系统通知（notification），注册一个广播接受者来接收ACTION_DOWNLOAD_COMPLETE广播
- DownloadManager
    - https://www.cnblogs.com/guanxinjing/p/13299949.html
    - https://www.jb51.net/article/220244.htm





### 03.Api调用说明



### 04.遇到的坑分析






