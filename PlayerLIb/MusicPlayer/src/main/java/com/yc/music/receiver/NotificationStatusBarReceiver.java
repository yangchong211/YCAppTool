package com.yc.music.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.yc.music.config.MusicPlayAction;
import com.yc.music.service.PlayAudioService;
import com.yc.music.tool.BaseAppHelper;
import com.yc.toolutils.AppLogUtils;


public class NotificationStatusBarReceiver extends BroadcastReceiver {

    public static final String ACTION_STATUS_BAR = "YC_ACTION_STATUS_BAR";
    public static final String EXTRA = "extra";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getAction())) {
            return;
        }
        String extra = intent.getStringExtra(EXTRA);
        if (TextUtils.equals(extra, MusicPlayAction.TYPE_NEXT)) {
            PlayAudioService.startCommand(context, MusicPlayAction.TYPE_NEXT);
            AppLogUtils.e("NotifiyStatusBarReceiver"+"下一首");
        } else if (TextUtils.equals(extra, MusicPlayAction.TYPE_START_PAUSE)) {
            if(BaseAppHelper.get().getMusicService()!=null){
                boolean playing = BaseAppHelper.get().getMusicService().isPlaying();
                if(playing){
                    AppLogUtils.e("NotifiyStatusBarReceiver"+"暂停");
                }else {
                    AppLogUtils.e("NotifiyStatusBarReceiver"+"播放");
                }
                PlayAudioService.startCommand(context, MusicPlayAction.TYPE_START_PAUSE);
            }

        }else if(TextUtils.equals(extra, MusicPlayAction.TYPE_PRE)){
            PlayAudioService.startCommand(context, MusicPlayAction.TYPE_PRE);
            AppLogUtils.e("NotifiyStatusBarReceiver"+"上一首");
        }
    }
}
