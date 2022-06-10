package com.yc.ycvideoplayer.video.tiktok;

import android.content.Context;

import com.yc.video.inter.ISurfaceView;
import com.yc.video.surface.SurfaceFactory;
import com.yc.video.surface.RenderTextureView;


public class TikTokRenderViewFactory extends SurfaceFactory {

    public static TikTokRenderViewFactory create() {
        return new TikTokRenderViewFactory();
    }

    @Override
    public ISurfaceView createRenderView(Context context) {
        return new TikTokRenderView(new RenderTextureView(context));
    }
}
