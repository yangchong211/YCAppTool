package com.yc.ycvideoplayer.video.tiktok;

import android.content.Context;

import com.yc.videosurface.ISurfaceView;
import com.yc.videosurface.RenderTextureView;
import com.yc.videosurface.SurfaceFactory;


public class TikTokRenderViewFactory extends SurfaceFactory {

    public static TikTokRenderViewFactory create() {
        return new TikTokRenderViewFactory();
    }

    @Override
    public ISurfaceView createRenderView(Context context) {
        return new TikTokRenderView(new RenderTextureView(context));
    }
}
