package com.yc.videosurface;

import android.content.Context;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 实现类
 *     revise:
 * </pre>
 */
public class TextureViewFactory extends SurfaceFactory {

    public static TextureViewFactory create() {
        return new TextureViewFactory();
    }

    @Override
    public ISurfaceView createRenderView(Context context) {
        //创建TextureView
        return new RenderTextureView(context);
    }
}
