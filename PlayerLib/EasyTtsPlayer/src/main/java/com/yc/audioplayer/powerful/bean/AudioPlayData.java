package com.yc.audioplayer.powerful.bean;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     GitHub : <a href="https://github.com/yangchong211/YCVideoPlayer">...</a>
 *     time  : 2018/8/6
 *     desc  : 链表结果的播放数据
 *     revise:
 * </pre>
 */
public final class AudioPlayData {

    /**
     * 默认是普通级别
     */
    public AudioTtsPriority mPriority = AudioTtsPriority.NORMAL_PRIORITY;
    /**
     * 播放类型
     */
    public AudioTtsMode audioTtsMode;
    /**
     * tts文本内容
     */
    private String mTts;
    /**
     * 资源id
     */
    private int mRawId;
    /**
     * 当前data
     */
    private AudioPlayData mCurrent;
    /**
     * 下一个data
     */
    private AudioPlayData mNext;

    private AudioPlayData() {
        mCurrent = this;
        mCurrent.mNext = mNext;
    }

    private AudioPlayData(int mRawId) {
        this();
        this.mRawId = mRawId;
        audioTtsMode = AudioTtsMode.RAW_ID;
    }

    private AudioPlayData(String mTts , AudioTtsMode mode) {
        this();
        this.mTts = mTts;
        audioTtsMode = mode;
    }

    public String getTts() {
        return mTts;
    }

    public int getRawId() {
        return mRawId;
    }

    public AudioPlayData getNext() {
        return mNext;
    }

    public static class Builder {

        private AudioPlayData mHeaderPlayData;
        private AudioPlayData mCurrentPlayData;
        private AudioTtsPriority mPriority;

        public Builder(AudioTtsPriority priority) {
            this.mPriority = priority;
        }

        public Builder() {
        }

        public Builder tts(String string) {
            AudioPlayData data = new AudioPlayData(string,AudioTtsMode.TTS);
            if (mHeaderPlayData == null) {
                mHeaderPlayData = data;
            } else {
                mCurrentPlayData.mNext = data;
            }
            mCurrentPlayData = data;
            if (mPriority != null) {
                data.mPriority = mPriority;
            }
            return this;
        }

        public Builder url(String string) {
            AudioPlayData data = new AudioPlayData(string,AudioTtsMode.URL);
            if (mHeaderPlayData == null) {
                mHeaderPlayData = data;
            } else {
                mCurrentPlayData.mNext = data;
            }
            mCurrentPlayData = data;
            if (mPriority != null) {
                data.mPriority = mPriority;
            }
            return this;
        }

        public Builder rawId(int rawId) {
            AudioPlayData data = new AudioPlayData(rawId);
            if (mHeaderPlayData == null) {
                if (mPriority != null) {
                    data.mPriority = mPriority;
                }
                mHeaderPlayData = data;
            } else {
                mCurrentPlayData.mNext = data;
            }
            mCurrentPlayData = data;
            return this;
        }

        public AudioPlayData build() {
            return mHeaderPlayData;
        }

    }

    @Override
    public String toString() {
        return "AudioPlayData{" +
                "mPriority=" + mPriority +
                ", audioTtsMode=" + audioTtsMode +
                ", mTts='" + mTts + '\'' +
                ", mRawId=" + mRawId +
                ", mNext=" + mNext +
                '}';
    }
}
