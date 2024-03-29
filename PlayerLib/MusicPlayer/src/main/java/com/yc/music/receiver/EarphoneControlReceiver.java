package com.yc.music.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.KeyEvent;

import com.yc.music.config.MusicPlayAction;
import com.yc.music.service.PlayAudioService;


/**
 * 耳机线控，仅在5.0以下有效，5.0以上被{@link MediaSessionCompat}接管。
 */
public class EarphoneControlReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (event == null || event.getAction() != KeyEvent.ACTION_UP) {
            return;
        }
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_MEDIA_PLAY:
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            case KeyEvent.KEYCODE_HEADSETHOOK:
                PlayAudioService.startCommand(context, MusicPlayAction.TYPE_START_PAUSE);
                break;
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                PlayAudioService.startCommand(context, MusicPlayAction.TYPE_NEXT);
                break;
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                PlayAudioService.startCommand(context, MusicPlayAction.TYPE_PRE);
                break;
            default:
                break;
        }
    }
}
