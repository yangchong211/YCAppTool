package com.yc.kernel.impl.tx;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.yc.kernel.inter.AbstractVideoPlayer;

import java.util.Map;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/11/9
 *     desc  : 腾讯player播放器内核
 *     revise:
 * </pre>
 */
public class TxMediaPlayer extends AbstractVideoPlayer {

    private Context mAppContext;

    public TxMediaPlayer(Context context) {
        if (context instanceof Application){
            mAppContext = context;
        } else {
            mAppContext = context.getApplicationContext();
        }
    }


    @Override
    public void initPlayer() {

    }

    @Override
    public void setDataSource(String path, Map<String, String> headers) {

    }

    @Override
    public void setDataSource(AssetFileDescriptor fd) {

    }

    @Override
    public void setSurface(Surface surface) {

    }

    @Override
    public void prepareAsync() {

    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void reset() {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void seekTo(long time) {

    }

    @Override
    public void release() {

    }

    @Override
    public long getCurrentPosition() {
        return 0;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public int getBufferedPercentage() {
        return 0;
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {

    }

    @Override
    public void setVolume(float v1, float v2) {

    }

    @Override
    public void setLooping(boolean isLooping) {

    }

    @Override
    public void setOptions() {

    }

    @Override
    public void setSpeed(float speed) {

    }

    @Override
    public float getSpeed() {
        return 0;
    }

    @Override
    public long getTcpSpeed() {
        return 0;
    }
}
