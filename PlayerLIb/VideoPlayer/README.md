# 视频播放器UI抽取封装
#### 目录介绍
- 01.播放器UI封装背景
    - 1.1 遇到的问题
    - 1.2 多业务难复用
- 02.播放器UI封装目标
    - 2.1 方便自定义
    - 2.2 和业务解藕
    - 2.3 使用简单
    - 2.4 适合多业务场景
- 03.播放器UI架构
    - 3.1 UI整体架构
    - 3.2 UI层UML类图架构
    - 3.3 关于依赖关系
- 04.播放器UI接口设计
    - 4.1 播放和UI分离思想
    - 4.2 视频器Player接口
    - 4.3 控制器Controller接口
    - 4.4 视图ControlView接口
    - 4.5 player和view交互
- 05.播放器播放业务逻辑
    - 5.1 播放器设备方向监听
    - 5.2 音频焦点改变监听
    - 5.3 手势改变音量&亮度
- 06.播放器如何自定义操作
    - 6.1 添加自定义视图
    - 6.2 添加统一埋点
- 07.播放器一些其他设计
    - 6.1 稳定性设计说明
    - 6.2 测试case设计
    - 6.3 拓展性相关设计



### 00.视频播放器通用框架
- 基础封装视频播放器player，可以在ExoPlayer、MediaPlayer，声网RTC视频播放器内核，原生MediaPlayer可以自由切换
- 对于视图状态切换和后期维护拓展，避免功能和业务出现耦合。比如需要支持播放器UI高度定制，而不是该lib库中UI代码
- 针对视频播放，音频播放，播放回放，以及视频直播的功能。使用简单，代码拓展性强，封装性好，主要是和业务彻底解耦，暴露接口监听给开发者处理业务具体逻辑
- 该播放器整体架构：播放器内核(自由切换) +  视频播放器 + 边播边缓存 + 高度定制播放器UI视图层
- 项目地址：https://github.com/yangchong211/YCVideoPlayer
- 关于视频播放器整体功能介绍文档：https://juejin.im/post/6883457444752654343


### 01.播放器UI封装背景
#### 1.1 遇到的问题
- 播放器内核和UI层耦合
    - 也就是说视频player和ui操作柔和到了一起，尤其是两者之间的交互。比如播放中需要更新UI进度条，播放异常需要显示异常UI，都比较难处理播放器状态变化更新UI操作
- UI难以自定义或者修改麻烦
    - 比如常见的视频播放器，会把视频各种视图写到xml中，这种方式在后期代码会很大，而且改动一个小的布局，则会影响大。这样到后期往往只敢加代码，而不敢删除代码……
    - 有时候难以适应新的场景，比如添加一个播放广告，老师开课，或者视频引导业务需求，则需要到播放器中写一堆业务代码。迭代到后期，违背了开闭原则，视频播放器需要做到和业务分离


#### 1.2 多业务难复用
- 发展中遇到的问题。播放器可支持多种场景下的播放，多个产品会用到同一个播放器，这样就会带来一个问题，一个播放业务播放器状态发生变化，其他播放业务必须同步更新播放状态，各个播放业务之间互相交叉，随着播放业务的增多，开发和维护成本会急剧增加, 导致后续开发不可持续。 



### 02.播放器UI封装目标
#### 2.1 方便自定义
- 把视频状态View当作Component
    - Component 是组件的意思。这里充分利用了组件化思想，把一个个状态视频view当作一个个组件。避免把所有的视图都放到一起去写，避免代码臃肿。


#### 2.2 和业务解藕
- 一定要解耦合
    - 播放器player与视频UI解耦：支持添加自定义视频视图，比如支持添加自定义广告，新手引导，或者视频播放异常等视图，这个需要较强的拓展性。
- 业务和视频解耦合
    - 视频缓存，给视频设置封面，视频播放完成后的操作。这些逻辑，不能写到视频播放器里面，可以暴露给外部开发者设置，必须和业务解耦合。
    


#### 2.3 使用简单
- 视频播放器结构需要清晰。这个是指该视频播放器能否看了文档后快速上手，知道封装的大概流程。方便后期他人修改和维护，因此需要将视频播放器功能分离。
- 比如切换内核+视频播放器(player+controller+view)


#### 2.4 适合多业务场景
- 适合多种业务场景。比如适合播放单个视频，多个视频，以及列表视频，或者类似抖音那种一个页面一个视频，还有小窗口播放视频。也就是适合大多数业务场景


### 03.播放器UI架构
#### 3.1 UI整体架构
- UI整体架构图如下所示
    - ![image](https://img-blog.csdnimg.cn/20201012215233584.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)


#### 3.2 UI层UML类图架构
- UI层UML类图如下所示
    - 待完善
- 工程项目中代码结构说明
    - ![image](https://img-blog.csdnimg.cn/20201013094115174.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM3NzAwMjc1,size_16,color_FFFFFF,t_70#pic_center)


#### 3.3 关于依赖关系


### 04.播放器UI接口设计
#### 4.1 播放和UI分离思想
- 方便播放业务发生变化
    - 播放状态变化是导致不同播放业务场景之间交叉同步，解除播放业务对播放器的直接操控，采用接口监听进行解耦。
    - 比如：player+controller+view+interface
- 具体来说如何理解呢
    - player，一套player视频播放接口，负责VideoPlayer播放逻辑。
    - controller，一套视图UI接口，负责UI相关添加，移除视图View的操作逻辑。
    - view，一套视图View接口，负责具体设置进度改变view状态，监听播放状态改变View
    - 两者之间的桥梁使用 wrapper ，同时继承player和controller接口，拿到两边api。


#### 4.2 定义视频器Player接口
- 关于视频播放器
    - 定义一个视频播放器InterVideoPlayer接口，操作视频播放，暂停，缓冲，进度设置，设置播放模式等多种操作。
    - 然后写一个播放器接口的具体实现类，在这个里面拿到内核播放器player，然后做相关的实现操作。



#### 4.3 控制器Controller接口
- 关于视频视图View
    - 定义一个视图InterVideoController接口，主要负责视图显示/隐藏，播放进度，锁屏，状态栏等操作。
    - 然后写一个播放器视图接口的具体实现类，在这里里面inflate视图操作，然后接口方法实现，为了方便后期开发者自定义view，因此需要addView操作，将添加进来的视图用map集合装起来。



#### 4.4 视图ControlView接口
- 播放器player和controller交互
    - 在player中创建BaseVideoController对象，这个时候需要把controller添加到播放器中，这个时候有两个要点特别重要，需要把播放器状态监听，和播放模式监听传递给控制器
    - setPlayState设置视频播放器播放逻辑状态，主要是播放缓冲，加载，播放中，暂停，错误，完成，异常，播放进度等多个状态，方便控制器做UI更新操作
    - setPlayerState设置视频播放切换模式状态，主要是普通模式，小窗口模式，正常模式三种其中一种，方便控制器做UI更新



#### 4.5 player和view交互
- 播放器player和view交互
    - 这块非常关键，举个例子，视频播放失败需要显示控制层的异常视图View；播放视频初始化需要显示loading，然后更新UI播放进度条等。都是播放器和视图层交互
    - 可以定义一个类，同时实现InterVideoPlayer接口和InterVideoController接口，这个时候会重新这两个接口所有的方法。此类的目的是为了在InterControlView接口实现类中既能调用VideoPlayer的api又能调用BaseVideoController的api


### 06.播放器如何自定义操作
#### 6.1 添加自定义视图
- 如何添加自定义播放器视图
    - 添加了自定义播放器视图，比如添加视频广告，可以选择跳过，选择播放暂停。那这个视图view，肯定是需要操作player或者获取player的状态的。这个时候就需要暴露监听视频播放的状态接口监听
    - 首先定义一个InterControlView接口，也就是说所有自定义视频视图view需要实现这个接口，该接口中的核心方法有：绑定视图到播放器，视图显示隐藏变化监听，播放状态监听，播放模式监听，进度监听，锁屏监听等
    - 在BaseVideoController中的状态监听中，通过InterControlView接口对象就可以把播放器的状态传递到子类中


#### 6.2 添加统一埋点





### 06.播放Player和UI通信
- 比如，在自定义view视图中，我想调用VideoPlayer的api又能调用BaseVideoController的api，该如何实现呢？
    - 当创建了下面的对象，就可以同时拿到player和controller中的api方法呢，这里面省略一部分代码，具体看demo案例
    ``` java
    public class ControlWrapper implements InterVideoPlayer, InterVideoController {
        
        private InterVideoPlayer mVideoPlayer;
        private InterVideoController mController;
        
        public ControlWrapper(@NonNull InterVideoPlayer videoPlayer, @NonNull InterVideoController controller) {
            mVideoPlayer = videoPlayer;
            mController = controller;
        }
        
        @Override
        public void start() {
            mVideoPlayer.start();
        }
    
        @Override
        public boolean isShowing() {
            return mController.isShowing();
        }
    
        @Override
        public void setLocked(boolean locked) {
            mController.setLocked(locked);
        }

    }
    ```



### 07.如何添加自定义播放视图
- 比如，现在有个业务需求，需要在视频播放器刚开始添加一个广告视图，等待广告倒计时120秒后，直接进入播放视频逻辑。相信这个业务场景很常见，大家都碰到过，使用该播放器就特别简单，代码如下所示：
- 首先创建一个自定义view，需要实现InterControlView接口，重写该接口中所有抽象方法，这里省略了很多代码，具体看demo。
    ``` java
    public class AdControlView extends FrameLayout implements InterControlView, View.OnClickListener {
    
        private ControlWrapper mControlWrapper;
        public AdControlView(@NonNull Context context) {
            super(context);
            init(context);
        }
    
        private void init(Context context){
            LayoutInflater.from(getContext()).inflate(R.layout.layout_ad_control_view, this, true);
        }
       
        /**
         * 播放状态
         * -1               播放错误
         * 0                播放未开始
         * 1                播放准备中
         * 2                播放准备就绪
         * 3                正在播放
         * 4                暂停播放
         * 5                正在缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，缓冲区数据足够后恢复播放)
         * 6                暂停缓冲(播放器正在播放时，缓冲区数据不足，进行缓冲，此时暂停播放器，继续缓冲，缓冲区数据足够后恢复暂停
         * 7                播放完成
         * 8                开始播放中止
         * @param playState                     播放状态，主要是指播放器的各种状态
         */
        @Override
        public void onPlayStateChanged(int playState) {
            switch (playState) {
                case ConstantKeys.CurrentState.STATE_PLAYING:
                    mControlWrapper.startProgress();
                    mPlayButton.setSelected(true);
                    break;
                case ConstantKeys.CurrentState.STATE_PAUSED:
                    mPlayButton.setSelected(false);
                    break;
            }
        }
    
        /**
         * 播放模式
         * 普通模式，小窗口模式，正常模式三种其中一种
         * MODE_NORMAL              普通模式
         * MODE_FULL_SCREEN         全屏模式
         * MODE_TINY_WINDOW         小屏模式
         * @param playerState                   播放模式
         */
        @Override
        public void onPlayerStateChanged(int playerState) {
            switch (playerState) {
                case ConstantKeys.PlayMode.MODE_NORMAL:
                    mBack.setVisibility(GONE);
                    mFullScreen.setSelected(false);
                    break;
                case ConstantKeys.PlayMode.MODE_FULL_SCREEN:
                    mBack.setVisibility(VISIBLE);
                    mFullScreen.setSelected(true);
                    break;
            }
            //暂未实现全面屏适配逻辑，需要你自己补全
        }
    }
    ```
- 然后该怎么使用这个自定义view呢？很简单，在之前基础上，通过控制器对象add进来即可，代码如下所示
    ``` java
    controller = new BasisVideoController(this);
    AdControlView adControlView = new AdControlView(this);
    adControlView.setListener(new AdControlView.AdControlListener() {
        @Override
        public void onAdClick() {
            BaseToast.showRoundRectToast( "广告点击跳转");
        }
    
        @Override
        public void onSkipAd() {
            playVideo();
        }
    });
    controller.addControlComponent(adControlView);
    //设置控制器
    mVideoPlayer.setController(controller);
    mVideoPlayer.setUrl(proxyUrl);
    mVideoPlayer.start();
    ```


### 08.关于播放器视图层级
- 视频播放器为了拓展性，需要暴露view接口供外部开发者自定义视频播放器视图，通过addView的形式添加到播放器的控制器中。
    - 这就涉及view视图的层级性。控制view视图的显示和隐藏是特别重要的，这个时候在自定义view中就需要拿到播放器的状态
- 举一个简单的例子，基础视频播放器
    - 添加了基础播放功能的几个播放视图。有播放完成，播放异常，播放加载，顶部标题栏，底部控制条栏，锁屏，以及手势滑动栏。如何控制它们的显示隐藏切换呢？
    - 在addView这些视图时，大多数的view都是默认GONE隐藏的。比如当视频初始化时，先缓冲则显示缓冲view而隐藏其他视图，接着播放则显示顶部/底部视图而隐藏其他视图
- 比如有时候需要显示两种不同的自定义视图如何处理
    - 举个例子，播放的时候，点击一下视频，会显示顶部title视图和底部控制条视图，那么这样会同时显示两个视图。
    - 点击顶部title视图的返回键可以关闭播放器，点击底部控制条视图的播放暂停可以控制播放条件。这个时候底部控制条视图FrameLayout的ChildView在整个视频的底部，顶部title视图FrameLayout的ChildView在整个视频的顶部，这样可以达到上下层都可以相应事件。
- 那么FrameLayout层层重叠，如何让下层不响应事件
    - 在最上方显示的层加上： android:clickable="true" 可以避免点击上层触发底层。或者直接给控制设置一个background颜色也可以。
- 比如基础播放器的视图层级是这样的
    ``` java
    //添加自动完成播放界面view
    CustomCompleteView completeView = new CustomCompleteView(mContext);
    completeView.setVisibility(GONE);
    this.addControlComponent(completeView);
    
    //添加错误界面view
    CustomErrorView errorView = new CustomErrorView(mContext);
    errorView.setVisibility(GONE);
    this.addControlComponent(errorView);
    
    //添加与加载视图界面view，准备播放界面
    CustomPrepareView prepareView = new CustomPrepareView(mContext);
    thumb = prepareView.getThumb();
    prepareView.setClickStart();
    this.addControlComponent(prepareView);
    
    //添加标题栏
    titleView = new CustomTitleView(mContext);
    titleView.setTitle(title);
    titleView.setVisibility(VISIBLE);
    this.addControlComponent(titleView);
    
    if (isLive) {
        //添加底部播放控制条
        CustomLiveControlView liveControlView = new CustomLiveControlView(mContext);
        this.addControlComponent(liveControlView);
    } else {
        //添加底部播放控制条
        CustomBottomView vodControlView = new CustomBottomView(mContext);
        //是否显示底部进度条。默认显示
        vodControlView.showBottomProgress(true);
        this.addControlComponent(vodControlView);
    }
    //添加滑动控制视图
    CustomGestureView gestureControlView = new CustomGestureView(mContext);
    this.addControlComponent(gestureControlView);
    ```


### 09.视频播放器重力感应监听
- 区别视频几种不同的播放模式
    - 正常播放时，设置检查系统是否开启自动旋转，打开监听
    - 全屏模式播放视频的时候，强制监听设备方向
    - 在小窗口模式播放视频的时候，取消重力感应监听
    - 注意一点。关于是否开启自动旋转的重力感应监听，可以给外部开发者暴露一个方法设置的开关。让用户选择是否开启该功能
- 首先写一个类，然后继承OrientationEventListener类，注意视频播放器重力感应监听不要那么频繁。表示500毫秒才检测一次……
    ``` java
    public class OrientationHelper extends OrientationEventListener {
    
        private long mLastTime;
    
        private OnOrientationChangeListener mOnOrientationChangeListener;
    
        public OrientationHelper(Context context) {
            super(context);
        }
    
        @Override
        public void onOrientationChanged(int orientation) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastTime < 500) {
                return;
            }
            //500毫秒检测一次
            if (mOnOrientationChangeListener != null) {
                mOnOrientationChangeListener.onOrientationChanged(orientation);
            }
            mLastTime = currentTime;
        }
    
    
        public interface OnOrientationChangeListener {
            void onOrientationChanged(int orientation);
        }
    
        public void setOnOrientationChangeListener(OnOrientationChangeListener onOrientationChangeListener) {
            mOnOrientationChangeListener = onOrientationChangeListener;
        }
    }
    ```
- 关于播放器播放模式状态发生变化时，需要更新是开启重力感应监听，还是关闭重力感应监听。代码如下所示 
    ``` java
    /**
     * 子类重写此方法并在其中更新控制器在不同播放器状态下的ui
     * 普通模式，小窗口模式，正常模式三种其中一种
     * MODE_NORMAL              普通模式
     * MODE_FULL_SCREEN         全屏模式
     * MODE_TINY_WINDOW         小屏模式
     */
    @CallSuper
    protected void onPlayerStateChanged(@ConstantKeys.PlayMode int playerState) {
        switch (playerState) {
            case ConstantKeys.PlayMode.MODE_NORMAL:
                //视频正常播放是设置监听
                if (mEnableOrientation) {
                    //检查系统是否开启自动旋转
                    mOrientationHelper.enable();
                } else {
                    //取消监听
                    mOrientationHelper.disable();
                }
                break;
            case ConstantKeys.PlayMode.MODE_FULL_SCREEN:
                //在全屏时强制监听设备方向
                mOrientationHelper.enable();
                break;
            case ConstantKeys.PlayMode.MODE_TINY_WINDOW:
                //小窗口取消重力感应监听
                mOrientationHelper.disable();
                break;
        }
    }
    ```





















