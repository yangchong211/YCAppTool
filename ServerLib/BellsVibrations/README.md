# 铃声和震动
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.图片圆角性能



### 01.基础概念介绍



### 02.常见思路和做法
#### 2.1 自定义铃声设置
- android播放声音，一种是SoundPool，一种是MediaPlayer
    - SoundPool 适合播放反映速度要求较高的声效，比如，游戏中的爆炸音效
    - MediaPlayer 适合播放时间比较长的声效，比如，游戏中的背景音乐


#### 2.2 手机震动设置
- 手机震动设置
    - 第一步通过系统服务获得手机震动服务；第二步调用vibrate方法设置震动；第三步设置cancel方法取消震动。


#### 2.3 修改手机音量
- 调节音量，就要对Android系统中的铃音种类有一个认识。
    - Android系统中国一共有6类铃音，分别是通话音，系统音，铃音，媒体音，闹钟音，通知音。
    - Android中音量模式有3类，分别是正常，静音，震动。
- AudioManager类的铃声类型
    ```
    STREAM_VOICE_CALL	通话音	 
    STREAM_SYSTEM	    系统音	 
    STREAM_RING	        铃音	来电与收短信的铃声
    STREAM_MUSIC	    媒体音	音频、视频、游戏等的声音
    STREAM_ALARM	    闹钟音	 
    STREAM_NOTIFICATION	通知音
    ```
- setStreamVolume和adjustStreamVolume的区别
    - setStreamVolume直接将音量调整到目标值，通常与拖动条配合使用；而adjustStreamVolume是以当前音量为基础，然后调大、调小或调静音。
- 修改手机音量大概的流程
    - 1



### 03.Api调用说明




### 04.遇到的坑分析



### 05.图片圆角性能














