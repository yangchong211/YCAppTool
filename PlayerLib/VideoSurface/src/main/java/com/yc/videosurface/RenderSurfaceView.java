/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.yc.videosurface;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/9/21
 *     desc  : 重写SurfaceView，适配视频的宽高和旋转
 *     revise:
 * </pre>
 */
public class RenderSurfaceView extends SurfaceView implements ISurfaceView {

    /**
     * 优点：可以在一个独立的线程中进行绘制，不会影响主线程；使用双缓冲机制，播放视频时画面更流畅
     * 缺点：Surface不在View hierachy中，它的显示也不受View的属性控制，所以不能进行平移，缩放等变换，
     *      也不能放在其它ViewGroup中。SurfaceView 不能嵌套使用。
     *
     * SurfaceView双缓冲
     *      1.SurfaceView在更新视图时用到了两张Canvas，一张frontCanvas和一张backCanvas。
     *      2.每次实际显示的是frontCanvas，backCanvas存储的是上一次更改前的视图，当使用lockCanvas（）获取画布时，
     *        得到的实际上是backCanvas而不是正在显示的frontCanvas，之后你在获取到的backCanvas上绘制新视图，
     *        再unlockCanvasAndPost（canvas）此视图，那么上传的这张canvas将替换原来的frontCanvas作为新的frontCanvas，
     *        原来的frontCanvas将切换到后台作为backCanvas。
     */

    private MeasureHelper mMeasureHelper;
    @Nullable
    private IPlayerSurface mPlayerSurface;

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (callback!=null){
            getHolder().removeCallback(callback);
        }
    }

    public RenderSurfaceView(Context context) {
        super(context);
        init();
    }

    private void init(){
        mMeasureHelper = new MeasureHelper();
        SurfaceHolder holder = this.getHolder();
        //holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(callback);
    }

    /**
     * 关联AbstractPlayer
     * @param player                        player
     */
    @Override
    public void attachToPlayer(@NonNull IPlayerSurface player) {
        this.mPlayerSurface = player;
    }

    /**
     * 设置视频宽高
     * @param videoWidth                    宽
     * @param videoHeight                   高
     */
    @Override
    public void setVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth > 0 && videoHeight > 0) {
            mMeasureHelper.setVideoSize(videoWidth, videoHeight);
            requestLayout();
        }
    }

    /**
     * 设置视频旋转角度
     * @param degree                        角度值
     */
    @Override
    public void setVideoRotation(int degree) {
        mMeasureHelper.setVideoRotation(degree);
        setRotation(degree);
    }

    /**
     * 设置screen scale type
     * @param scaleType                     类型
     */
    @Override
    public void setScaleType(int scaleType) {
        mMeasureHelper.setScreenScale(scaleType);
        requestLayout();
    }

    /**
     * 获取真实的RenderView
     * @return                              view
     */
    @Override
    public View getView() {
        return this;
    }

    /**
     * 截图
     * @return                              bitmap
     */
    @Override
    public Bitmap doScreenShot() {
        return getDrawingCache();
    }

    /**
     * 释放资源
     */
    @Override
    public void release() {
        if (callback!=null){
            getHolder().removeCallback(callback);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int[] measuredSize = mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measuredSize[0], measuredSize[1]);
    }

    /**
     * 记得一定要重新写这个方法，如果角度发生了变化，就重新绘制布局
     * 设置视频旋转角度
     * @param rotation                  角度
     */
    @Override
    public void setRotation(float rotation) {
        if (rotation != getRotation()) {
            super.setRotation(rotation);
            requestLayout();
        }
    }

    private final SurfaceHolder.Callback callback = new SurfaceHolder.Callback(){
        /**
         * 创建的时候调用该方法
         * @param holder                        holder
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (mPlayerSurface != null){
                Surface surface = holder.getSurface();
                mPlayerSurface.setSurface(surface);
            }
        }

        /**
         * 视图改变的时候调用方法
         * @param holder
         * @param format
         * @param width
         * @param height
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        /**
         * 销毁的时候调用该方法
         * @param holder
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };



}
