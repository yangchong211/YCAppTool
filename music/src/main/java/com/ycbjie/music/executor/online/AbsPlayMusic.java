package com.ycbjie.music.executor.online;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.blankj.utilcode.util.NetworkUtils;
import com.ycbjie.music.R;
import com.ycbjie.music.executor.inter.IExecutor;
import com.ycbjie.music.model.bean.AudioBean;


public abstract class AbsPlayMusic implements IExecutor<AudioBean> {

    private Activity mActivity;
    int mCounter = 0;

    AbsPlayMusic(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void execute() {
        checkNetwork();
    }

    private void checkNetwork() {
        if (!NetworkUtils.isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle(R.string.tips);
            builder.setMessage(R.string.play_tips);
            builder.setPositiveButton(R.string.play_tips_sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getPlayInfoWrapper();
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            Dialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            getPlayInfoWrapper();
        }
    }

    private void getPlayInfoWrapper() {
        onPrepare();
        getPlayInfo();
    }

    /**
     * 获取播放信息
     */
    abstract void getPlayInfo();

    void checkCounter(AudioBean music) {
        onExecuteSuccess(music);
    }
}
