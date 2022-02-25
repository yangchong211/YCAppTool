package com.ycbjie.live.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ycbjie.alivelib.R;
import com.ycbjie.live.alive.YcKeepAlive;
import com.ycbjie.live.constant.YcConstant;
import com.ycbjie.live.receiver.DeviceStatusReceiver;
import com.ycbjie.live.receiver.NotificationClickReceiver;
import com.ycbjie.live.utils.NotificationUtils;
import com.ycbjie.live.utils.ServiceUtils;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : 本地服务（双进程守护之本地进程)
 *     revise:
 * </pre>
 */
public final class LocalService extends Service {

    private DeviceStatusReceiver mDeviceStatusReceiver;
    private MusicControlReceiver mMusicControlReceiver;
    /**
     * 控制暂停
     */
    private boolean mIsPause = true;
    /**
     * 无声音乐保活
     */
    private MediaPlayer mMediaPlayer;
    private GuardBinder mBinder;
    private Handler mHandler;
    private boolean mIsBoundRemoteService;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mBinder == null) {
            mBinder = new GuardBinder();
        }
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mIsPause = pm != null && pm.isScreenOn();
        if (mHandler == null) {
            mHandler = new Handler();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (YcKeepAlive.sUseSilenceMusic) {
            //播放无声音乐
            if (mMediaPlayer == null) {
                mMediaPlayer = MediaPlayer.create(this, R.raw.novioce);
                if (mMediaPlayer != null) {
                    mMediaPlayer.setVolume(0f, 0f);
                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            if (!mIsPause) {
                                if (YcKeepAlive.sRunMode == YcKeepAlive.RunMode.ROGUE) {
                                    play();
                                } else {
                                    if (mHandler != null) {
                                        mHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                play();
                                            }
                                        }, 5000);
                                    }
                                }
                            }
                        }
                    });
                    play();
                }
            }
        }
        //像素保活
        if (mDeviceStatusReceiver == null) {
            mDeviceStatusReceiver = new DeviceStatusReceiver();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mDeviceStatusReceiver, intentFilter);
        //屏幕点亮状态监听，用于单独控制音乐播放
        if (mMusicControlReceiver == null) {
            mMusicControlReceiver = new MusicControlReceiver();
        }
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(YcConstant.KEEP_ACTION_OPEN_MUSIC);
        intentFilter2.addAction(YcConstant.KEEP_ACTION_CLOSE_MUSIC);
        intentFilter2.addAction(YcConstant.KEEP_ACTION_OPEN_MUSIC_ONCE);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMusicControlReceiver, intentFilter2);
        //启用前台服务，提升优先级
        if (YcKeepAlive.sForegroundNotification != null) {
            Intent intent2 = new Intent(getApplicationContext(), NotificationClickReceiver.class);
            intent2.setAction(YcConstant.ACTION_CLICK_NOTIFICATION);
            Notification notification = NotificationUtils.createNotification(this, YcKeepAlive.sForegroundNotification.getTitle(), YcKeepAlive.sForegroundNotification.getDescription(), YcKeepAlive.sForegroundNotification.getIconRes(), intent2);
            startForeground(YcConstant.KEY_NOTIFICATION_ID, notification);
        }
        //绑定守护进程
        try {
            Intent intent3 = new Intent(this, RemoteService.class);
            mIsBoundRemoteService = this.bindService(intent3, mConnection, Context.BIND_ABOVE_CLIENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //隐藏服务通知
        try {
            if (YcKeepAlive.sForegroundNotification == null || !YcKeepAlive.sForegroundNotification.isShow()) {
                startService(new Intent(this, HideForegroundService.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (YcKeepAlive.sKeepLiveService != null) {
            YcKeepAlive.sKeepLiveService.onWorking();
        }
        return START_STICKY;
    }

    private void play() {
        if (YcKeepAlive.sUseSilenceMusic) {
            if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
        }
    }

    private void playOnce() {
        if (YcKeepAlive.sUseSilenceMusic) {
            if (mMediaPlayer != null) {
                mMediaPlayer.start();
            }
        }
    }

    private void pause() {
        if (YcKeepAlive.sUseSilenceMusic) {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        }
    }

    /**
     * 音乐播放控制广播
     */
    public class MusicControlReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            if (YcConstant.KEEP_ACTION_OPEN_MUSIC.equals(action)) {
                mIsPause = false;
                play();
            } else if (YcConstant.KEEP_ACTION_CLOSE_MUSIC.equals(action)) {
                mIsPause = true;
                pause();
            } else if (YcConstant.KEEP_ACTION_OPEN_MUSIC_ONCE.equals(action)) {
                playOnce();
            }
        }
    }

    private final class GuardBinder extends com.ycbjie.live.service.GuardAidl.Stub {

        @Override
        public void wakeUp(String title, String description, int iconRes) throws RemoteException {

        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (ServiceUtils.isServiceRunning(getApplicationContext(), YcConstant.KEY_LOCAL_SERVICE_NAME)) {
                Intent remoteService = new Intent(LocalService.this,
                        RemoteService.class);
                LocalService.this.startService(remoteService);
                Intent intent = new Intent(LocalService.this, RemoteService.class);
                mIsBoundRemoteService = LocalService.this.bindService(intent, mConnection, Context.BIND_ABOVE_CLIENT);
            }
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm != null && pm.isScreenOn();
            if (isScreenOn) {
                LocalBroadcastManager.getInstance(
                        getApplicationContext()).sendBroadcast(new Intent(YcConstant.KEEP_ACTION_CLOSE_MUSIC));
            } else {
                LocalBroadcastManager.getInstance(
                        getApplicationContext()).sendBroadcast(new Intent(YcConstant.KEEP_ACTION_OPEN_MUSIC));
            }
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                if (mBinder != null && YcKeepAlive.sForegroundNotification != null) {
                    com.ycbjie.live.service.GuardAidl guardAidl = com.ycbjie.live.service.GuardAidl.Stub.asInterface(service);
                    guardAidl.wakeUp(YcKeepAlive.sForegroundNotification.getTitle(), YcKeepAlive.sForegroundNotification.getDescription(), YcKeepAlive.sForegroundNotification.getIconRes());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mConnection != null) {
            try {
                if (mIsBoundRemoteService) {
                    unbindService(mConnection);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        try {
            unregisterReceiver(mDeviceStatusReceiver);
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mMusicControlReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (YcKeepAlive.sKeepLiveService != null) {
            YcKeepAlive.sKeepLiveService.onStop();
        }
    }
}
