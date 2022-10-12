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
#### 2.1 如何获取文件格式
- 看一个场景对图片格式的理解
    - 在存入文件格式为JPG的图片即完成人脸注册，部分用户对JPG格式理解不深，误以为只要以“.JPEG”或者“.jpg”结尾的图片就是JPG格式，甚至有用户还特意把“张三 .png”改成“张三.jpg”伪装成JPG格式来满足要求。
- 关于对文件格式的误区
    - 文件扩展名（.jpg）与文件格式无关，是人们为了便于区分，强加的扩展名。
- 那么如何通过代码判断该图片文件是否是JPG呢？
    - 需要了解图片文件的存储，bmp，jpg，png图片存储差异很大，但是归结起来主要分为三部分：文件头，调色板，数据区；我们可以通过读取文件头来判断该文件的格式。


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