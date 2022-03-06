package com.yc.appstatuslib.broadcast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

import com.yc.appstatuslib.AppStatusManager;
import com.yc.appstatuslib.info.BatteryInfo;

/**
 * 因为系统规定监听电量变化的广播接收器不能静态注册，所以这里只能使用动态注册的方式了。
 */
public class BatteryBroadcastReceiver extends BroadcastReceiver {

    private BatteryInfo mBatteryInfo = new BatteryInfo();
    private final AppStatusManager mManager;

    public BatteryBroadcastReceiver(AppStatusManager manger) {
        this.mManager = manger;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                    BatteryManager.BATTERY_STATUS_UNKNOWN);
            int health = intent.getIntExtra("health", 0);
            // 当前电量
            int level = intent.getIntExtra("level", 0);
            // 总电量
            int scale = intent.getIntExtra("scale", 0);
            //
            int plugged = intent.getIntExtra("plugged", 0);
            int voltage = intent.getIntExtra("voltage", 0);
            int temperature = intent.getIntExtra("temperature", 0);
            String technology = intent.getStringExtra("technology");
            BatteryInfo batteryInfo = BatteryInfo.buildBattery(status, health, level,
                    scale, plugged, voltage, temperature, technology);
            if (this.notify(batteryInfo) && this.mManager != null) {
                this.mBatteryInfo = batteryInfo;
                this.mManager.dispatcherBatteryState(mBatteryInfo);
            }
            this.mBatteryInfo = batteryInfo;
        }
    }

    public boolean notify(BatteryInfo batteryInfo) {
        return this.mBatteryInfo == null ||
                this.mBatteryInfo.level != batteryInfo.level ||
                !this.mBatteryInfo.status.equals(batteryInfo.status) ||
                !this.mBatteryInfo.health.equals(batteryInfo.health) ||
                !this.mBatteryInfo.plugged.equals(batteryInfo.plugged) ||
                this.mBatteryInfo.temperature != batteryInfo.temperature;
    }

    public BatteryInfo getBatteryInfo() {
        return this.mBatteryInfo;
    }
}

