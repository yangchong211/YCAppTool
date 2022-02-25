
package com.ycbjie.live.whitelist.impl;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;

import com.ycbjie.live.alive.YcKeepAlive;
import com.ycbjie.live.whitelist.IWhiteListProvider;
import com.ycbjie.live.whitelist.IntentType;
import com.ycbjie.live.whitelist.WhiteList;
import com.ycbjie.live.whitelist.WhiteListIntentWrapper;

import java.util.ArrayList;
import java.util.List;

import static android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/9/19
 *     desc  : 默认的白名单跳转意图数据提供者
 *     revise:
 * </pre>
 */
public class DefaultWhiteListProvider implements IWhiteListProvider {

    @Override
    public List<WhiteListIntentWrapper> getWhiteList(Application application) {
        List<WhiteListIntentWrapper> intentWrappers = new ArrayList<>();

        //Android 7.0+ Doze 模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            PowerManager pm = (PowerManager) application.getSystemService(Context.POWER_SERVICE);
            boolean ignoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(application.getPackageName());
            if (!ignoringBatteryOptimizations) {
                Intent dozeIntent = new Intent(ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                dozeIntent.setData(Uri.parse("package:" + application.getPackageName()));
                intentWrappers.add(new WhiteListIntentWrapper(dozeIntent, IntentType.DOZE));
            }
        }

        //华为 自启管理
        Intent huaweiIntent = new Intent();
        huaweiIntent.setAction("huawei.intent.action.HSM_BOOTAPP_MANAGER");
        intentWrappers.add(new WhiteListIntentWrapper(huaweiIntent, IntentType.HUAWEI));

        //华为 锁屏清理
        Intent huaweiGodIntent = new Intent();
        huaweiGodIntent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
        intentWrappers.add(new WhiteListIntentWrapper(huaweiGodIntent, IntentType.HUAWEI_GOD));

        //小米 自启动管理
        Intent xiaomiIntent = new Intent();
        xiaomiIntent.setAction("miui.intent.action.OP_AUTO_START");
        xiaomiIntent.addCategory(Intent.CATEGORY_DEFAULT);
        intentWrappers.add(new WhiteListIntentWrapper(xiaomiIntent, IntentType.XIAOMI));

        //小米 神隐模式
        Intent xiaomiGodIntent = new Intent();
        xiaomiGodIntent.setComponent(new ComponentName("com.miui.powerkeeper", "com.miui.powerkeeper.ui.HiddenAppsConfigActivity"));
        xiaomiGodIntent.putExtra("package_name", application.getPackageName());
        xiaomiGodIntent.putExtra("package_label", WhiteList.getApplicationName(YcKeepAlive.getApplication()));
        intentWrappers.add(new WhiteListIntentWrapper(xiaomiGodIntent, IntentType.XIAOMI_GOD));

        //三星 5.0/5.1 自启动应用程序管理
        Intent samsungLIntent = application.getPackageManager().getLaunchIntentForPackage("com.samsung.android.sm");
        if (samsungLIntent != null) {
            intentWrappers.add(new WhiteListIntentWrapper(samsungLIntent, IntentType.SAMSUNG_L));
        }

        //三星 6.0+ 未监视的应用程序管理
        Intent samsungMIntent = new Intent();
        samsungMIntent.setComponent(new ComponentName("com.samsung.android.sm_cn", "com.samsung.android.sm.ui.battery.BatteryActivity"));
        intentWrappers.add(new WhiteListIntentWrapper(samsungMIntent, IntentType.SAMSUNG_M));

        //魅族 自启动管理
        Intent meizuIntent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        meizuIntent.addCategory(Intent.CATEGORY_DEFAULT);
        meizuIntent.putExtra("packageName", application.getPackageName());
        intentWrappers.add(new WhiteListIntentWrapper(meizuIntent, IntentType.MEIZU));

        //魅族 待机耗电管理
        Intent meizuGodIntent = new Intent();
        meizuGodIntent.setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.powerui.PowerAppPermissionActivity"));
        intentWrappers.add(new WhiteListIntentWrapper(meizuGodIntent, IntentType.MEIZU_GOD));

        //Oppo 自启动管理
        Intent oppoIntent = new Intent();
        oppoIntent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
        intentWrappers.add(new WhiteListIntentWrapper(oppoIntent, IntentType.OPPO));

        //Oppo 自启动管理(旧版本系统)
        Intent oppoOldIntent = new Intent();
        oppoOldIntent.setComponent(new ComponentName("com.color.safecenter", "com.color.safecenter.permission.startup.StartupAppListActivity"));
        intentWrappers.add(new WhiteListIntentWrapper(oppoOldIntent, IntentType.OPPO_OLD));

        //Vivo 后台高耗电
        Intent vivoGodIntent = new Intent();
        vivoGodIntent.setComponent(new ComponentName("com.vivo.abe", "com.vivo.applicationbehaviorengine.ui.ExcessivePowerManagerActivity"));
        intentWrappers.add(new WhiteListIntentWrapper(vivoGodIntent, IntentType.VIVO_GOD));

        //金立 应用自启
        Intent gioneeIntent = new Intent();
        gioneeIntent.setComponent(new ComponentName("com.gionee.softmanager", "com.gionee.softmanager.MainActivity"));
        intentWrappers.add(new WhiteListIntentWrapper(gioneeIntent, IntentType.GIONEE));

        //乐视 自启动管理
        Intent letvIntent = new Intent();
        letvIntent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
        intentWrappers.add(new WhiteListIntentWrapper(letvIntent, IntentType.LETV));

        //乐视 应用保护
        Intent letvGodIntent = new Intent();
        letvGodIntent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.BackgroundAppManageActivity"));
        intentWrappers.add(new WhiteListIntentWrapper(letvGodIntent, IntentType.LETV_GOD));

        //酷派 自启动管理
        Intent coolpadIntent = new Intent();
        coolpadIntent.setComponent(new ComponentName("com.yulong.android.security", "com.yulong.android.seccenter.tabbarmain"));
        intentWrappers.add(new WhiteListIntentWrapper(coolpadIntent, IntentType.COOLPAD));

        //联想 后台管理
        Intent lenovoIntent = new Intent();
        lenovoIntent.setComponent(new ComponentName("com.lenovo.security", "com.lenovo.security.purebackground.PureBackgroundActivity"));
        intentWrappers.add(new WhiteListIntentWrapper(lenovoIntent, IntentType.LENOVO));

        //联想 后台耗电优化
        Intent lenovoGodIntent = new Intent();
        lenovoGodIntent.setComponent(new ComponentName("com.lenovo.powersetting", "com.lenovo.powersetting.ui.Settings$HighPowerApplicationsActivity"));
        intentWrappers.add(new WhiteListIntentWrapper(lenovoGodIntent, IntentType.LENOVO_GOD));

        //中兴 自启管理
        Intent zteIntent = new Intent();
        zteIntent.setComponent(new ComponentName("com.zte.heartyservice", "com.zte.heartyservice.autorun.AppAutoRunManager"));
        intentWrappers.add(new WhiteListIntentWrapper(zteIntent, IntentType.ZTE));

        //中兴 锁屏加速受保护应用
        Intent zteGodIntent = new Intent();
        zteGodIntent.setComponent(new ComponentName("com.zte.heartyservice", "com.zte.heartyservice.setting.ClearAppSettingsActivity"));
        intentWrappers.add(new WhiteListIntentWrapper(zteGodIntent, IntentType.ZTE_GOD));
        return intentWrappers;
    }
}
