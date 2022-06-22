package com.yc.appstatuslib.info;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 *     @author: yangchong
 *     email  : yangchong211@163.com
 *     time   : 2017/5/18
 *     desc   : app电量信息
 *     revise :
 * </pre>
 */
public final class AppBatteryInfo {

    public String technology = "";
    public int temperature;
    public int voltage;
    public int level;
    public int scale;
    public String status = "";
    public String health = "";
    public String plugged = "";
    public String humanTime = "";

    public AppBatteryInfo() {
    }

    public static AppBatteryInfo buildBattery(int status, int health, int level, int scale,
                                              int plugged, int voltage, int temperature,
                                              String technology) {
        AppBatteryInfo batteryInfo = new AppBatteryInfo();
        batteryInfo.status = getStatus(status);
        batteryInfo.health = getHealth(health);
        batteryInfo.level = level;
        batteryInfo.scale = scale;
        batteryInfo.temperature = temperature;
        batteryInfo.technology = technology;
        batteryInfo.voltage = voltage;
        batteryInfo.plugged = getPlugged(plugged);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss:SSS", Locale.US);
        batteryInfo.humanTime = dateFormat.format(new Date());
        return batteryInfo;
    }

    private static String getStatus(int status) {
        switch(status) {
            case 1:
                return "unknown";
            case 2:
                return "charging";
            case 3:
                return "discharging";
            case 4:
                return "not charging";
            case 5:
                return "full";
            default:
                return "unknown-status - " + status;
        }
    }

    private static String getPlugged(int plugged) {
        switch(plugged) {
            case 1:
                return "plugged ac";
            case 2:
                return "plugged usb";
            case 4:
                return "plugged wireless";
            case 3:
            default:
                return "unknown-Plugged - " + plugged;
        }
    }

    private static String getHealth(int health) {
        switch(health) {
            case 1:
                return "unknown";
            case 2:
                return "good";
            case 3:
                return "overheat";
            case 4:
                return "dead";
            case 5:
                return "voltage";
            case 6:
                return "unspecified failure";
            case 7:
                return "BATTERY_HEALTH_COLD";
            default:
                return "unknown-health - " + health;
        }
    }

    @Override
    public String toString() {
        return toStringInfo();
    }

    public String toStringInfo(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("technology : ").append(this.technology).append("\n");
        stringBuilder.append("temperature : ").append(this.temperature).append("\n");
        stringBuilder.append("voltage : ").append(this.voltage).append("\n");
        stringBuilder.append("level : ").append(this.level).append("\n");
        stringBuilder.append("scale : ").append(this.scale).append("\n");
        stringBuilder.append("status : ").append(this.status).append("\n");
        stringBuilder.append("health : ").append(this.health).append("\n");
        stringBuilder.append("plugged : ").append(this.plugged);
        return stringBuilder.toString();
    }

}

