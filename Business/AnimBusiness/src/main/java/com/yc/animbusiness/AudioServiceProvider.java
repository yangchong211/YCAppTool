package com.yc.animbusiness;

import com.yc.spi.annotation.ServiceProviderInterface;

//@ServiceProviderInterface
public interface AudioServiceProvider {

    /**
     * 停止播放
     */
    void stop();

    /**
     * 暂停播放
     */
    void pause();

    /**
     * 恢复播放
     */
    void resume();

}
