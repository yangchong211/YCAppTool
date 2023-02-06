# Surface自定义库
#### 目录介绍
- 01.基础概念介绍
- 02.常见思路和做法
- 03.Api调用说明
- 04.遇到的坑分析
- 05.其他问题说明



### 01.基础概念说明



### 02.常见思路和做法



### 03.Api调用说明
- 如何使用通用api创建View
    ``` java
    SurfaceFactory surfaceFactory1 = SurfaceViewFactory.create();
    SurfaceFactory surfaceFactory2 = TextureViewFactory.create();
    ISurfaceView surfaceView = surfaceFactory1.createRenderView(this);
    ISurfaceView textureView = surfaceFactory2.createRenderView(this);
    ```
- 如何让SurfaceView或者TextureView和视频Player绑定
    ``` java
    //绑定mMediaPlayer对象
    surfaceView.attachToPlayer(new IPlayerSurface() {
        @Override
        public void setSurface(Surface surface) {
            mMediaPlayer.setSurface(surface);
        }
    });
    ```
- 一些其他api的使用如下所示
```

```



### 04.遇到的坑分析


### 05.其他问题说明





