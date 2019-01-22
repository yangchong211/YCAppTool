package com.ycbjie.love.view.typewriter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 类描述:打字机效果
 * 出处:https://github.com/zmywly8866/TypeTextView
 */
public class TypeTextView extends AppCompatTextView {
    private static final int TYPE_TIME_DELAY = 100;
    private Context mContext = null;
    private MediaPlayer mMediaPlayer = null;
    private OnTypeViewListener mOnTypeViewListener = null;
    private String mShowTextString = null;
    private int mTypeTimeDelay = TYPE_TIME_DELAY;
    private Timer mTypeTimer = null;

    public interface OnTypeViewListener {
        void onTypeOver();

        void onTypeStart();
    }

    class TypeTimerTask extends TimerTask {
        TypeTimerTask() {
        }

        public void run() {
            TypeTextView.this.post(new Runnable() {
                public void run() {
                    if (TypeTextView.this.getText().toString().length() < TypeTextView.this.mShowTextString.length()) {
                        TypeTextView.this.setText(TypeTextView.this.mShowTextString.substring(0, TypeTextView.this.getText().toString().length() + 1));
                        TypeTextView.this.startAudioPlayer();
                        TypeTextView.this.startTypeTimer();
                        return;
                    }
                    TypeTextView.this.stopTypeTimer();
                    if (TypeTextView.this.mOnTypeViewListener != null) {
                        TypeTextView.this.mOnTypeViewListener.onTypeOver();
                    }
                }
            });
        }
    }

    public TypeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initTypeTextView(context);
    }

    public TypeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypeTextView(context);
    }

    public TypeTextView(Context context) {
        super(context);
        initTypeTextView(context);
    }

    public void setOnTypeViewListener(OnTypeViewListener onTypeViewListener) {
        this.mOnTypeViewListener = onTypeViewListener;
    }

    public void start(String textString) {
        start(textString, TYPE_TIME_DELAY);
    }

    public void start(final String textString, final int typeTimeDelay) {
        if (!TextUtils.isEmpty(textString) && typeTimeDelay >= 0) {
            post(new Runnable() {
                public void run() {
                    TypeTextView.this.mShowTextString = textString;
                    TypeTextView.this.mTypeTimeDelay = typeTimeDelay;
                    TypeTextView.this.setText("");
                    TypeTextView.this.startTypeTimer();
                    if (TypeTextView.this.mOnTypeViewListener != null) {
                        TypeTextView.this.mOnTypeViewListener.onTypeStart();
                    }
                }
            });
        }
    }

    public void stop() {
        stopTypeTimer();
        stopAudio();
    }

    private void initTypeTextView(Context context) {
        this.mContext = context;
    }

    private void startTypeTimer() {
        stopTypeTimer();
        this.mTypeTimer = new Timer();
        this.mTypeTimer.schedule(new TypeTimerTask(), (long) this.mTypeTimeDelay);
    }

    private void stopTypeTimer() {
        if (this.mTypeTimer != null) {
            this.mTypeTimer.cancel();
            this.mTypeTimer = null;
        }
    }

    private void startAudioPlayer() {
        stopAudio();
    }

    private void playAudio(int audioResId) {
        try {
            stopAudio();
            this.mMediaPlayer = MediaPlayer.create(this.mContext, audioResId);
            this.mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopAudio() {
    }
}
