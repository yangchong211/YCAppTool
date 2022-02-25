package com.yc.longevitylib;


import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/30
 *     desc  : 保活
 *     revise:
 * </pre>
 */
public class LongevityMonitor {

    private static final long INIT_DELAY_TIME_OFFSET = 5000L;
    private static final long WATCH_DOG_TIMER_INTERVAL = 15000L;
    private static final long WATCH_DOG_PERIOD_THRESHOLD = 60000L;
    private static final long WATCH_DOG_INTERVAL_UP_LIMIT = 43200000L;
    private static final long TIMESTAMP_NO_VALUE = 0L;
    private static final int PID_NO_VALUE = 0;
    private static Application sApplication;
    private static LongevityMonitorConfig.ILongevityMonitorApolloToggle sToggle;
    private static LongevityMonitorConfig.ILongevityMonitorOmegaEventTrack sEventTrack;
    public static LongevityMonitorConfig.ILongevityMonitorLogger sLogger;
    /**
     * 获取handler对象，并且指定main主线程
     */
    private static final Handler sHandler = new Handler(Looper.getMainLooper());
    private static SharedPreferences sSP;
    private static long sLastLiveTimeStamp;
    private static int sLastPid;
    private static int sLastScreenState;
    public static int sCurrentScreenState = LongevityConstant.LONGEVITY_SCREEN_STATE_ON;
    private static Bundle sSavedInstanceState;
    private static final Runnable sWatchDogRunnable = new Runnable() {
        public void run() {
            if (LongevityMonitor.sToggle.isOpen()) {
                long currentTimestamp = System.currentTimeMillis();
                int currentPid = LongevityMonitor.getCurrentPid();
                long periodMillis = currentTimestamp - LongevityMonitor.sLastLiveTimeStamp;
                if (periodMillis > WATCH_DOG_PERIOD_THRESHOLD && periodMillis < WATCH_DOG_INTERVAL_UP_LIMIT) {
                    if (currentPid == LongevityMonitor.sLastPid) {
                        LongevityMonitor.report(LongevityConstant.LONGEVITY_MONITOR_EVENT_SLEEP,
                                LongevityConstant.LONGEVITY_MONITOR_EVENT_SLEEP_TYPE_SLEEP,
                                periodMillis, currentTimestamp, LongevityMonitor.sLastLiveTimeStamp);
                    } else if (LongevityMonitor.sSavedInstanceState != null) {
                        LongevityMonitor.report(LongevityConstant.LONGEVITY_MONITOR_EVENT_SYSTEM_KILL,
                                "", periodMillis, currentTimestamp,
                                LongevityMonitor.sLastLiveTimeStamp);
                    } else if (LongevityMonitor.sLastScreenState == LongevityConstant.LONGEVITY_SCREEN_STATE_OFF) {
                        LongevityMonitor.report(
                                LongevityConstant.LONGEVITY_MONITOR_EVENT_SLEEP,
                                LongevityConstant.LONGEVITY_MONITOR_EVENT_SLEEP_TYPE_KILL,
                                periodMillis, currentTimestamp, LongevityMonitor.sLastLiveTimeStamp);
                    } else {
                        LongevityMonitor.sLogger.log(
                                LongevityConstant.LONGEVITY_MONITOR_EVENT_USER_KILL + periodMillis);
                    }
                }

                if (periodMillis > WATCH_DOG_INTERVAL_UP_LIMIT) {
                    LongevityMonitor.sLogger.log("interval exceed limit " + periodMillis);
                }

                Editor editor = LongevityMonitor.sSP.edit();
                editor.putLong(LongevityConstant.LONGEVITY_MONITOR_PARAM_FIELD_TS, currentTimestamp);
                editor.putInt(LongevityConstant.LONGEVITY_MONITOR_KEY_PID, currentPid);
                editor.putInt(LongevityConstant.LONGEVITY_MONITOR_KEY_SCREEN_STATE, LongevityMonitor.sCurrentScreenState);
                editor.apply();
                LongevityMonitor.sLastLiveTimeStamp = currentTimestamp;
                LongevityMonitor.sLastPid = currentPid;
                LongevityMonitor.sLastScreenState = LongevityMonitor.sCurrentScreenState;
                LongevityMonitor.sHandler.postDelayed(this, WATCH_DOG_TIMER_INTERVAL);
                LongevityMonitor.sLogger.log("postDelayed-----sWatchDogRunnable");
            } else {
                LongevityMonitor.sLogger.log("boolean isOpen() set false");
            }
        }
    };

    public LongevityMonitor() {
    }

    public static void init(LongevityMonitorConfig config) {
        sApplication = config.getApplication();
        sToggle = config.getToggle();
        sEventTrack = config.getEventTrack();
        sLogger = config.getLogger();
        LongevityScreenReceiver.register(sApplication);
    }

    public static void onActivityCreate(Bundle savedInstanceState) {
        if (sToggle.isOpen()) {
            sLogger.log("savedInstanceState is null ?\t" + (savedInstanceState == null));
            sSavedInstanceState = savedInstanceState;
            sSP = sApplication.getSharedPreferences(LongevityConstant.LONGEVITY_MONITOR_NAME, 0);
            sLastLiveTimeStamp = sSP.getLong(LongevityConstant.LONGEVITY_MONITOR_PARAM_FIELD_TS, TIMESTAMP_NO_VALUE);
            sLastPid = sSP.getInt(LongevityConstant.LONGEVITY_MONITOR_KEY_PID, PID_NO_VALUE);
            sLastScreenState = sSP.getInt(LongevityConstant.LONGEVITY_MONITOR_KEY_SCREEN_STATE, LongevityConstant.LONGEVITY_SCREEN_STATE_NO_VALUE);
            if (sLastLiveTimeStamp == TIMESTAMP_NO_VALUE || sLastPid == PID_NO_VALUE) {
                sLastLiveTimeStamp = System.currentTimeMillis();
                sLastPid = getCurrentPid();
            }

            restartHandler();
        }
    }

    private static void restartHandler() {
        sHandler.removeCallbacks(sWatchDogRunnable);
        sHandler.postDelayed(sWatchDogRunnable, INIT_DELAY_TIME_OFFSET);
        sLogger.log("restartHandler-----"+INIT_DELAY_TIME_OFFSET);
    }

    private static int getCurrentPid() {
        return Process.myPid();
    }

    private static void report(String eventName, String eventType, long periodMillis
            , long currentTimestamp, long lastTimestamp) {
        HashMap<String, String> params = new HashMap();
        params.put(LongevityConstant.LONGEVITY_MONITOR_PARAM_FIELD_EVENT, eventName);
        params.put(LongevityConstant.LONGEVITY_MONITOR_PARAM_FIELD_TYPE, TextUtils.isEmpty(eventType) ? "" : eventType);
        params.put(LongevityConstant.LONGEVITY_MONITOR_PARAM_FIELD_PERIOD, String.valueOf(periodMillis));
        params.put(LongevityConstant.LONGEVITY_MONITOR_PARAM_FIELD_TS, String.valueOf(currentTimestamp));
        params.put(LongevityConstant.LONGEVITY_MONITOR_PARAM_FIELD_TS_LAST, String.valueOf(lastTimestamp));
        sEventTrack.onEvent(params);
        sLogger.log(params.toString());
    }
}
