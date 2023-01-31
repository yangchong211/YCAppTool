# 铃声和震动库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.该库性能分析



### 01.基础概念介绍
- 震动器
    - Vibrator(振动器)，是手机自带的振动器，是Android给我们提供的用于机身震动的一个服务。
    - 比如当手机收到推送消息的时候可以设置震动提醒。扫码环节中扫码成功震动提示一下用户，可以给个提示然后震动一下下。
- 修改手机音量
    - 修改系统音量这种操作还是挺常见的，一般在多媒体开发中都多少会涉及到。比如，app上有语音通话，当来电和去电时，需要播放铃声和调节声音。



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
- AudioManager类的音量模式
    ```
    RINGER_MODE_NORMAL  正常
    RINGER_MODE_SILENT  静音
    RINGER_MODE_VIBRATE 震动
    ```
- setStreamVolume和adjustStreamVolume的区别
    - setStreamVolume直接将音量调整到目标值，通常与拖动条配合使用；而adjustStreamVolume是以当前音量为基础，然后调大、调小或调静音。
- 修改手机音量大概的流程
    - 先获取手机音量，然后判断是否大于设定值。如果小于设置值，则设置音量到指定大小。


#### 2.4 设置音频路由
- 音频路由是指 app 在播放音频时使用的音频输出设备。
    - 移动端常见的音频路由有听筒、扬声器、有线耳机和蓝牙耳机。
- 外置播放设备
    - 如果用户使用了有线耳机、蓝牙耳机等外置音频播放设备，则音频只会通过外置设备播放，则无法切换音频路由为听筒或扬声器。
    - 当有多个外置设备时，音频会通过最后一个接入的外置设备播放。
- 一对一音视频通话的场景
    - 有线耳机：媒体音量	
    - 扬声器：通话音量	
    - 听筒：通话音量	
    - 蓝牙耳机：通话音量





### 03.Api调用说明
- 如何设置震动，Api如下所示
    ``` kotlin
    //开始震动
    vibratorHelper?.vibrate(longArrayOf(300L, 800L), 0)
    //取消震动
    vibratorHelper?.cancel()
    //设置震动3秒
    vibratorHelper?.vibrate(3000)
    ```
- 设置播放音频铃声，API如下所示
    ``` kotlin
    //开始播放raw音频铃声资源
    mediaAudioPlayer?.play(MediaAudioPlayer.DataSource.DATA_SOURCE_URI,R.raw.audio_call_ring)
    //开始播放assets音频铃声资源
    mediaAudioPlayer?.play(MediaAudioPlayer.DataSource.DATA_SOURCE_ASSET,"audio_call_ring.mp3")
    //停止播放
    mediaAudioPlayer?.stop()
    //暂停
    mediaAudioPlayer?.pause()
    //销毁
    mediaAudioPlayer?.release()
    //恢复播放
    mediaAudioPlayer?.resume()
    ```
- 手机声音设置，API如下所示
    ``` kotlin
    //获取最大多媒体音量
    val mediaMaxVolume = audioVolumeHelper?.getMediaMaxVolume(4)
    //是否支持铃音
    val enableRingtone = audioVolumeHelper?.enableRingtone()
    //是否支持震动
    val enableVibrate = audioVolumeHelper?.enableVibrate()
    //获取指定类型铃声的当前音量
    val mediaVolume = audioVolumeHelper?.getMediaVolume(4)
    //获取指定类型铃声的响铃模式
    val ringerMode = audioVolumeHelper?.getRingerMode()
    //设置指定类型铃声的响铃模式
    audioVolumeHelper?.setRingerMode(AudioManager.RINGER_MODE_SILENT)
    //设置指定类型铃声的当前音量
    audioVolumeHelper?.setMediaVolume(4,5)
    //设置指定类型铃声的当前音量
    audioVolumeHelper?.setAdjustStreamVolume(4,5)
    //按照比例调整声音
    audioVolumeHelper?.dynamicChangeVolume(4,0.5f)
    ```


### 04.遇到的坑分析
- 使用震动需要检测服务
    - 在设置震动时，需要先检测是否有震动器。
- 需要注意一些注意点
    - 如何是设置震动，建议先判断是否支持震动，如果支持再进行设置；如果是设置铃声，建议先判断是否支持铃声，如果支持再进行设置。



### 05.该库性能分析



### 06.参考博客
- android 代码控制音量
    - https://blog.csdn.net/weixin_42306054/article/details/117526415










