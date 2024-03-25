package com.yc.audioplayer.easy;

import android.speech.tts.UtteranceProgressListener;

public abstract class GetLastSegUtteranceProgressListener extends UtteranceProgressListener {

    private String lastSegId = "";

    public void setLastSegId(String lastSegId) {
        this.lastSegId = lastSegId;
    }

    @Override
    public void onDone(String utteranceId) {
        onDone(utteranceId, utteranceId.equals(lastSegId));
    }

    public abstract void onDone(String utteranceId , boolean isLastSeg);
}
