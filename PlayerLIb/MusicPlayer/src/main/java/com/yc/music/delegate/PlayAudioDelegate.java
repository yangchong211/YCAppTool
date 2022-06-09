package com.yc.music.delegate;

import com.yc.music.inter.InterPlayAudio;
import com.yc.music.inter.OnPlayerEventListener;
import com.yc.music.model.AudioBean;
import com.yc.music.service.PlayAudioService;

public class PlayAudioDelegate implements InterPlayAudio {

    private final InterPlayAudio mDelegate ;

    public PlayAudioDelegate(InterPlayAudio iAudio) {
        this.mDelegate = iAudio;
    }

    @Override
    public void init(PlayAudioService service) {
        if (null != this.mDelegate) {
            this.mDelegate.init(service);
        }
    }

    @Override
    public void play(int position) {
        if (null != this.mDelegate) {
            this.mDelegate.play(position);
        }
    }

    @Override
    public void play() {
        if (null != this.mDelegate) {
            this.mDelegate.play();
        }
    }

    @Override
    public void play(AudioBean music) {
        if (null != this.mDelegate) {
            this.mDelegate.play(music);
        }
    }

    @Override
    public void stop() {
        if (null != this.mDelegate) {
            this.mDelegate.stop();
        }
    }

    @Override
    public void playPause() {
        if (null != this.mDelegate) {
            this.mDelegate.playPause();
        }
    }

    @Override
    public void release() {
        if (null != this.mDelegate) {
            this.mDelegate.release();
        }
    }

    @Override
    public void pause() {
        if (null != this.mDelegate) {
            this.mDelegate.pause();
        }
    }

    @Override
    public void seekTo(int progress) {
        if (null != this.mDelegate) {
            this.mDelegate.seekTo(progress);
        }
    }

    @Override
    public void next() {
        if (null != this.mDelegate) {
            this.mDelegate.next();
        }
    }

    @Override
    public void prev() {
        if (null != this.mDelegate) {
            this.mDelegate.prev();
        }
    }

    @Override
    public void updatePlayProgress() {
        if (null != this.mDelegate) {
            this.mDelegate.updatePlayProgress();
        }
    }

    @Override
    public boolean isPlaying() {
        if (null != this.mDelegate) {
            this.mDelegate.isPlaying();
        }
        return false;
    }

    @Override
    public boolean isPreparing() {
        if (null != this.mDelegate) {
            this.mDelegate.isPreparing();
        }
        return false;
    }

    @Override
    public long getCurrentPosition() {
        if (null != this.mDelegate) {
            this.mDelegate.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public boolean isDefault() {
        if (null != this.mDelegate) {
            this.mDelegate.isDefault();
        }
        return false;
    }

    @Override
    public boolean isPausing() {
        if (null != this.mDelegate) {
            this.mDelegate.isPausing();
        }
        return false;
    }

    @Override
    public AudioBean getPlayingMusic() {
        if (null != this.mDelegate) {
            this.mDelegate.getPlayingMusic();
        }
        return null;
    }

    @Override
    public int getPlayingPosition() {
        if (null != this.mDelegate) {
            this.mDelegate.getPlayingPosition();
        }
        return 0;
    }
}
