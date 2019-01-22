package com.ycbjie.todo.ui.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.ycbjie.library.constant.Constant;
import com.ycbjie.todo.R;
import com.ycbjie.todo.service.WorkDoNotifyService;
import com.ycbjie.todo.ui.activity.WorkSettingActivity;

import static android.content.Context.ALARM_SERVICE;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/28.
 * 描    述：此版块训练dagger2+MVP
 * 修订历史：
 * ================================================
 */
public class WorkSettingFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.work_setting_fragment);
        initView();

    }

    private void initView() {
        final WorkSettingActivity activity = (WorkSettingActivity) getActivity();
        findPreference(Constant.CONFIG_KEY.SHOW_WEEK_TASK)
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        boolean b = (boolean) newValue;
                        preference.setSummary(b ? "主界面任务列表仅显示本周任务" : "主界面任务列表显示所有任务");
                        return true;
                    }
                });

        findPreference(Constant.CONFIG_KEY.SHOW_AS_LIST)
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        String s = (String) newValue;
                        if (s.equals("list")) {
                            preference.setSummary("列表形式展示任务列表");
                        } else {
                            preference.setSummary("网格形式展示任务列表");
                        }
                        return true;
                    }
                });



        findPreference(Constant.CONFIG_KEY.SHOW_PRIORITY)
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        boolean b = (boolean) newValue;
                        if (b)
                            preference.setSummary("在任务卡片中将显示优先级");
                        else
                            preference.setSummary("在任务卡片中将不会显示优先级");
                        return true;
                    }
                });


        // 备份内容
        findPreference(Constant.CONFIG_KEY.BACKUP)
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        activity.backupClick();
                        return true;
                    }
                });

        // 还原内容
        findPreference(Constant.CONFIG_KEY.RECOVERY)
                .setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        activity.recoveryClick();
                        return true;
                    }
                });

        // 智能提醒完成任务
        findPreference(Constant.CONFIG_KEY.AUTO_NOTIFY)
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        boolean b = (boolean) newValue;
                        if (b) {
                            startNotifyAlarm(activity);
                        } else {
                            cancelNotifyAlarm(activity);
                        }
                        return true;
                    }
                });
    }


    private void startNotifyAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, new Intent(context, WorkDoNotifyService.class), PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constant.AUTO_NOTIFY_INTERVAL_TIME, pendingIntent);
    }

    private void cancelNotifyAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, new Intent(context, WorkDoNotifyService.class), PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
    }


}
