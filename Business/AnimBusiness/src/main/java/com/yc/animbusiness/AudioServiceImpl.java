package com.yc.animbusiness;

import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.yc.spi.annotation.ServiceProvider;

//@ServiceProvider(AudioServiceProvider.class)
public class AudioServiceImpl implements AudioServiceProvider{

    @Override
    public void stop() {
        ToastUtils.showRoundRectToast("停止播放");
    }

    @Override
    public void pause() {
        ToastUtils.showRoundRectToast("暂停播放");
    }

    @Override
    public void resume() {
        ToastUtils.showRoundRectToast("恢复播放");
    }
}
