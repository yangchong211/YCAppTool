package com.yc.audioplayer.inter;

import com.yc.audioplayer.bean.AudioPlayData;

public interface PlayStateListener {

    void onStartPlay(AudioPlayData playData);

    void onCompletePlay(AudioPlayData playData);

    void onError(AudioPlayData playData) ;

}
