# 认识MediaPlayer
#### 目录介绍
- **1.关于此视频封装库介绍**
    - 1.1 MediaPlayer简单介绍
- **2.相关方法详解**
    - 2.1 获得MediaPlayer实例
    - 2.2 设置播放文件
    - 2.3 其他方法
- **3.生命周期**
    - 3.1 生命周期图[摘自网络]
    - 3.2 周期状态说明
- **4.播放视频**
    - 4.1 播放res/raw音频文件
    - 4.2 播放本地Uri
    - 4.3 播放网络文件
- **5.MediaPlayer + SurfaceView播放视频**
    - 5.1 为什么要这样
    - 5.2 SurfaceView局限性
- **6.VideoView播放视频**
    - 6.1 VideoView介绍
    - 6.2 使用方法代码
- **7.MediaPlayer+TextureView**
    - 7.1 为什么使用TextureView
    - 7.2 如何实现视频播放功能




### 1.关于此视频封装库介绍
#### 1.1 MediaPlayer简单介绍
- MediaPlayer类是Androd多媒体框架中的一个重要组件，通过该类，我们可以以最小的步骤来获取，解码和播放音视频。它支持三种不同的媒体来源：
    - 本地资源
    - 内部URI，比如你可以通过ContentResolver来获取
    - 外部URL(流)
- 对于Android支持的媒体格式列表，可见：Supported Media Formats文档在播放网络上的视频流时，Android原生的MediaPlayer支持两种协议，HTTP和RTSP，这两种协议最大的不同是，RTSP协议支持实时流媒体的播放，而HTTP协议不支持。因为VideoView的底层实现是MediaPlayer，因此VideoView也支持以上两种协议。但是Android原生MediaPalyer支持的协议（不支持RTMP、MMS等）和封装格式实在太有限了，如果我们想播放那些它不支持的视频，这时候就需要第三方播放器了，很多第三方播放器的底层实现都是基于FFmpeg



### 2.相关方法详解
#### 2.1 获得MediaPlayer实例
- 可以直接new或者调用create方法创建：
    ```
    MediaPlayer mp = new MediaPlayer();
    MediaPlayer mp = MediaPlayer.create(this, R.raw.test);
    //无需再调用setDataSource
    ```
- 另外create还有这样的形式：
    ```
    create(Context context, Uri uri, SurfaceHolder holder) 
    ```
    - 通过Uri和指定 SurfaceHolder 抽象类创建一个多媒体播放器。



#### 2.2 设置播放文件
- setDataSource()方法有多个，里面有这样一个类型的参数：
    - FileDescriptor在使用这个API的时候，需要把文件放到res文件夹平级的assets文件夹里，然后使用下述代码设置DataSource：
    ```
    MediaPlayer.create(this, R.raw.test); //①raw下的资源
    mp.setDataSource("/sdcard/test.mp3"); //②本地文件路径
    mp.setDataSource("http://www.xxx.com/music/test.mp3");//③网络URL文件
    ```



#### 2.3 其他方法
- 相关常见方法如下所示
    ```
    getCurrentPosition( )：得到当前的播放位置
    getDuration() ：得到文件的时间
    getVideoHeight() ：得到视频高度
    getVideoWidth() ：得到视频宽度
    isLooping()：是否循环播放
    isPlaying()：是否正在播放
    pause()：暂停
    prepare()：准备(同步)
    prepareAsync()：准备(异步)
    release()：释放MediaPlayer对象
    reset()：重置MediaPlayer对象
    seekTo(int msec)：指定播放的位置（以毫秒为单位的时间）
    setAudioStreamType(int streamtype)：指定流媒体的类型
    setDisplay(SurfaceHolder sh)：设置用SurfaceHolder来显示多媒体
    setLooping(boolean looping)：设置是否循环播放
    setOnBufferingUpdateListener(MediaPlayer.OnBufferingUpdateListener listener)：网络流媒体的缓冲监听
    setOnCompletionListener(MediaPlayer.OnCompletionListener listener)：网络流媒体播放结束监听
    setOnErrorListener(MediaPlayer.OnErrorListener listener)：设置错误信息监听
    setOnVideoSizeChangedListener(MediaPlayer.OnVideoSizeChangedListener listener)：视频尺寸监听
    setScreenOnWhilePlaying(boolean screenOn)：设置是否使用SurfaceHolder显示
    setVolume(float leftVolume, float rightVolume)：设置音量
    start()：开始播放
    stop()：停止播放
    ```

### 3.生命周期
#### 3.1 生命周期图[摘自网络]
- ![iamge](http://img.blog.csdn.net/20161128093058740)



#### 3.2 周期状态说明
- **状态1：Idel(空闲)状态**
    - 当 mediaplayer创建或者执行reset()方法后处于这个状态。
- **状态2：Initialized(已初始化)状态**
    - 当调用mediaplayer的setDataResource()方法给mediaplayer设置播放的数据源后，mediaplayer会处于该状态。
- **状态3：Prepared(准备就续)状态**
    - 设置完数据源后，调用mediaplayer的prepare()方法，让mediaplayer准备播放。值得一提的是，这里除了prepare()方法，还有prepareAsnyc()方法，此方法是异步方法，一般用于网络视频的缓冲。当缓冲完毕后，就会触发准备完毕的事件。我们要做的就是监听该事件(OnPreparedListener)，当缓冲完成时，执行相应的操作。在此状态上，我们可以调用seekTo()方法定位视频，此方法不改变mediaplayer的状态；亦可调用stop()放弃视频播放，使mediaplayer处于Stopped状态。一般我们会在此状态上调用start()方法开始播放视频。
- **状态4：Started(开始)状态**
    - 当处于Prepared状态、Paused状态和PlayebackCompeleted状态时，调用Started()方法即可进入该状态。在该状态中，mediaplayer开始播放视频，可以通过seekTo()方法和start()方法改变视频播放的进度，当Looping为真且播放完毕后，它会重新开始播放（即循环播放）；否则播放完毕后，会触发事件并调用OnCompletionaListener.OnCompletion()方法，进行特定操作，并进入PlaybackCompleted状态。在此状态中，亦可调用pause()方法或者stop()方法让视频暂停或停止，此时mediaplayer分别处于Stopped和Paused状态。
- **状态5：Stopped(停止)状态**
    - 当 mediaplayer处于Prepared、Started、Paused、PlaybackCompleted状态时，调用stop()方法即可进入本状态。应特别注意的是，在本状态中，若想重新开始播放，不能直接调用start()方法，必须调用prepare()方法或prepareAsync()方法重新让mediaplayer处于Prepared状态方可调用start()方法播放视频。
- **状态6：Paused(暂停)状态**
    - 当mediaplayer处于Started状态是，调用pause()方法即可进入本状态。在本状态里，可直接调用start()方法使，mediaplayer回到Started状态，亦可调用stop()方法停止视频播放，让播放器处于停止态。
- **状态7：PlaybackCompleted(播放完成)状态**
    - 当mediaplayer播放完成且Looping为假时即可进入本状态。在本状态可调用start()方法使mediaplayer回到Started状态（注意此时是从头开始播放）；亦可调用stop()方法使mediaplayer处于停止态，结束播放。
- **状态8：Error(错误)状态**
    - 当mediaplayer出现错误时处于此状态。



### 4.播放视频
#### 4.1 播放res/raw音频文件
- res/raw目录下的音频文件，创建MediaPlayer调用的是create方法，第一次启动播放前不需要再调用prepare()，如果是使用构造方法构造的话，则需要调用一次prepare()方法！
    ```
    @Override 
    public void onClick(View v) { 
      switch (v.getId()){ 
        case R.id.btn_play: 
           if(isRelease){
                 mPlayer = MediaPlayer.create(this,R.raw.fly);
                 isRelease = false;
            }
            mPlayer.start(); //开始播放 
            break; 
    
         case R.id.btn_pause:
            mPlayer.pause(); //停止播放
            break; 
    
         case R.id.btn_stop:
            mPlayer.reset(); //重置MediaPlayer 
            mPlayer.release(); //释放MediaPlayer 
            isRelease = true;
            break;
    }
    ```


#### 4.2 播放本地Uri
- 如下所示
    ```
    Uri myUri = ....;   /**initialize Uri here*/
    MediaPlayer mediaPlayer = new MediaPlayer();
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    mediaPlayer.setDataSource(getApplicationContext(), myUri);
    mediaPlayer.prepare();
    mediaPlayer.start();
    ```

#### 4.3 播放网络文件
- 如下所示
    ```
    String url = "http://........"; // your URL here 
    MediaPlayer mediaPlayer = new MediaPlayer();
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    mediaPlayer.setDataSource(url);
    mediaPlayer.prepare(); // might take long! (for buffering, etc)
    mediaPlayer.start();
    ```


### 5.MediaPlayer + SurfaceView播放视频
#### 5.1 为什么要这样
- MediaPlayer主要用于播放音频，没有提供图像输出界面，所以我们需要借助其他的组件来显示MediaPlayer播放的图像输出，我们可以使用SurfaceView来显示


### 5.2 SurfaceView局限性
- 在Android总播放视频可以直接使用VideoView，VideoView是通过继承自SurfaceView来实现的。
    - SurfaceView的大概原理就是在现有View的位置上创建一个新的Window，内容的显示和渲染都在新的Window中。这使得SurfaceView的绘制和刷新可以在单独的线程中进行，从而大大提高效率。
    - 由于SurfaceView的内容没有显示在View中而是显示在新建的Window中， 使得SurfaceView的显示不受View的属性控制，不能进行平移，缩放等变换，也不能放在其它RecyclerView或ScrollView中，一些View中的特性也无法使用。



### 6.VideoView播放视频
#### 6.1 VideoView介绍
- 使用VideoView时，视频宽度等于VideoView布局宽，但是高是自适应的，自动调整宽高比到视频原始比例，所以不会有拉伸。

#### 6.2 使用方法代码
- 如下所示
    ```
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.play);
        videoview = (VideoView) findViewById(R.id.video);
    
        mMediaController = new MediaController(this);
        videoview.setMediaController(mMediaController);
    
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loadView(url.getText().toString());
            }
        });
    }
    
    
    public void loadView(String path) {
        Uri uri = Uri.parse(path);
        videoview.setVideoURI(uri);
        videoview.start();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
     //         mp.setLooping(true);
                mp.start();// 播放
                Toast.makeText(MainActivity.this, "开始播放！", Toast.LENGTH_LONG).show();
            }
        });
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(MainActivity.this, "播放完毕", Toast.LENGTH_SHORT).show();
            }
        });
    }
    ```


### 7.MediaPlayer+TextureView
#### 7.1 为什么使用TextureView
- TextureView是在4.0(API level 14)引入的，与SurfaceView相比，它不会创建新的窗口来显示内容。它是将内容流直接投放到View中，并且可以和其它普通View一样进行移动，旋转，缩放，动画等变化。TextureView必须在硬件加速的窗口中使用。
- TextureView被创建后不能直接使用，必须要在它被它添加到ViewGroup后，待SurfaceTexture准备就绪才能起作用（看TextureView的源码，TextureView是在绘制的时候创建的内部SurfaceTexture）。


#### 7.2 如何实现视频播放功能
- SurfaceTexture的准备就绪、大小变化、销毁、更新等状态变化时都会回调相对应的方法。当TextureView内部创建好SurfaceTexture后，在监听器的onSurfaceTextureAvailable方法中，用SurfaceTexture来关联MediaPlayer，作为播放视频的图像数据来源。
- SurfaceTexture作为数据通道，把从数据源（MediaPlayer）中获取到的图像帧数据转为GL外部纹理，交给TextureVeiw作为View heirachy中的一个硬件加速层来显示，从而实现视频播放功能。



















