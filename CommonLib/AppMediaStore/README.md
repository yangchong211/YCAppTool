# 磁盘分区库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明


### 01.基础概念介绍
#### 1.3 MediaStore概念
- MediaStore是android系统提供的一个多媒体数据库，专门用于存放多媒体信息的，通过ContentResolver即可对数据库进行操作。
    - MediaStore.Files: 共享的文件,包括多媒体和非多媒体信息；
    - MediaStore.Audio: 存放音频信息；
    - MediaStore.Image: 存放图片信息；
    - MediaStore.Video: 存放视频信息；
- 每个内部类中都又包含了Media，Thumbnails和相应的MediaColumns，分别提供了媒体信息，缩略信息和操作字段。


#### 1.4 获取文件mimeType



### 02.常见思路和做法



### 03.Api调用说明



### 04.遇到的坑分析


### 05.其他问题说明




- https://blog.51cto.com/u_15298568/3052632
- https://github.com/javakam/FileOperator

- Android中MediaStore的介绍
    - https://blog.csdn.net/dongxianfei/article/details/122086235
Android 文件处理参考文档 :
    - 数据和文件存储概览 : https://developer.android.google.cn/training/data-storage
    - 访问应用专属文件 : https://developer.android.google.cn/training/data-storage/app-specific#kotlin
    - 保存到共享的存储空间 : https://developer.android.google.cn/training/data-storage/shared
    - 管理存储设备上的所有文件 : https://developer.android.google.cn/training/data-storage/manage-all-files
    - 分享文件 : https://developer.android.google.cn/training/secure-file-sharing
    - 应用安装位置 : https://developer.android.google.cn/guide/topics/data/install-location
    - Android 存储用例和最佳做法 : https://developer.android.google.cn/training/data-storage/use-cases
    - FileProvider : https://developer.android.google.cn/reference/androidx/core/content/FileProvider