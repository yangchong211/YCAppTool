# 音视频焦点抢占
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.该库性能分析



### 01.基础概念介绍
#### 1.1 什么叫做焦点抢占
- 如果手机上安装了两个音频播放器，当一个正在播放的时候，打开第二个播放歌曲，有没有发现第一个自动暂停了……
- 如果你在听音频的同时，又去打开了其它视频APP，你会发现音频APP暂停播放了……
- 如果你正在听音频或者看视频时，来电话了，那么音视频便会暂停。挂了电话后音乐又继续播放，视频则需要点击按钮播放，是不是很奇怪
- 当你收到消息，比如微信消息，并且有消息声音的时候，那么听音频的那一瞬间，音频的声音会变小了，然后过会儿又恢复了。是不是很有意思。
- 别蒙圈，这个就叫做音频捕获和丢弃焦点。



#### 1.2 为何处理音视频焦点抢占
- 为何处理焦点抢占
    - 如果不处理捕获与丢弃音频焦点的话，那么同时开几个音视频播放器，就会出现多个声音。那样会很嘈杂，一般线上的APP都会做这个处理。
- 为了协调设备的音频输出
    - android提出了Audio Focus机机制，获取audio focus必须调用AudioManager的requestAudioFocus()方法。



### 02.常见思路和做法
#### 2.1 处理音频焦点思路步骤
- 简单来说，就是这三步逻辑方法
    - 在onCreate方法中调用初始化方法
    - 在播放音视频的时候开始请求捕获音频焦点
    - 在音视频销毁的时候开始丢弃音频焦点


#### 2.2 失去音视频焦点场景
- 失去焦点有三种类型
    - 1.失去短暂焦点
    - 2.失去永久焦点
    - 3.Ducking
- 失去焦点原理说明
    - 当重新获得焦点的时候，如果通话结束，恢复播放；获取音量并且恢复音量。这个情景应该经常遇到。
    - 当永久丢失焦点，比如同时打开播放器，则停止或者暂停播放，否则出现两个声音
    - 当短暂丢失焦点，比如比如来了电话或者微信视频音频聊天等等，则暂停或者停止播放
    - 当瞬间丢失焦点，比如手机来了通知。前提是你的通知是震动或者声音时，会短暂地将音量减小一半。当然你也可以减小三分之一，哈哈！


#### 2.3 请求和放弃焦点
- 请求和放弃音频焦点
    - AudioFocus这个其实是音频焦点，一般情况下音视频播放器都会处理这个音频焦点的，在其丢失音频焦点的情况会将音频暂停或者停止的逻辑的，等到再次获取到音频焦点的情况下会再次恢复播放的。
    - 音频获取焦点可以通过requestAudioFocus()方法获得，在音频焦点成功获取后，该方法会返回AUDIOFOCUS_REQUEST_GRANTED常量，否则，会返回AUDIOFOCUS_REQUEST_FAILED常量。
    - 音视频失去焦点abandonAudioFocus()方法，这会通知系统您的App不再需要音频焦点，并移除相关OnAudioFocusChangeListener的注册。如果释放的是短暂音调焦点，那么被打断的音频会被继续播放。


#### 2.4 输出通道切换监听
- Audio 输出通道有很多
    - Speaker、headset、bluetooth A2DP等。通话或播放音乐等使用Audio输出过程中，可能发生Audio输出通道的切换。
- 输出通道切换的场景
    - 比如，插入有线耳机播放音乐时，声音是从耳机发出的；而此时拔出耳机，Audio输出通道会发生切换。
    - 如果音乐播放器不做处理，Audio输出是被切换到扬声器的，声音直接从Speaker发出。



### 03.Api调用说明
#### 3.1 依赖说明
- 如何依赖该库
    ```
    //音视频焦点抢占库
    implementation 'com.zuoyebang.iot.union:audio_helper:0.0.1'
    ```

#### 3.2 该库API说明
- 创建监听音频焦点抢占处理listener
    ``` kotlin
    audioFocus = AudioFocusHelper(this.applicationContext, object : OnAudioFocusChangeListener {
        override fun onLoss() {
            LogUtils.i("Log test AudioFocusHelper : 音频焦点永久性丢失（此时应暂停播放）")
        }
        override fun onLossTransient() {
            LogUtils.i("Log test AudioFocusHelper : 音频焦点暂时性丢失（此时应暂停播放）")
        }
        override fun onLossTransientCanDuck() {
            LogUtils.i("Log test AudioFocusHelper : 音频焦点暂时性丢失（此时只需降低音量，不需要暂停播放）")
        }
        override fun onGain(lossTransient: Boolean, lossTransientCanDuck: Boolean) {
            LogUtils.i("Log test AudioFocusHelper : 重新获取到音频焦点 , $lossTransient , $lossTransientCanDuck")
        }
    })
    ```
- 请求焦点和放弃焦点方法
    ``` kotlin
    audioFocus?.requestAudioFocus()
    audioFocus?.abandonAudioFocus()
    ```
- 用于监听来了电话处理逻辑
    ``` kotlin
    becomeNoiseHelper = BecomeNoiseHelper(this,object : BecomeNoiseHelper.OnBecomeNoiseListener{
        override fun onBecomeNoise() {
            LogUtils.i("Log test AudioFocusHelper : 监听 become noise 事件")
        }
    })
    becomeNoiseHelper?.registerBecomeNoiseReceiver()
    becomeNoiseHelper?.unregisterBecomeNoiseReceiver()
    ```



### 04.遇到的坑分析




### 05.该库性能分析













