package com.yc.appservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import com.yc.statusbar.BuildConfig;
import com.yc.toolutils.AppLogUtils;
import com.yc.toolutils.AppSignUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author yangchong
 *     blog  :
 *     time  : 2018/05/30
 *     desc  : 用于aidl多进程通信服务service
 *     revise:
 * </pre>
 */
public class AppInfoService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        AppLogUtils.i("AppInfoService--IBinder:");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppLogUtils.i("AppInfoService--onCreate:");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppLogUtils.i("AppInfoService--onDestroy:");
    }

    /**
     * 1.核心，Stub里面的方法运行的binder池中。
     * 2.Stub类并非我们自己创建的，而是AIDL自动生成的。
     *   系统会为每个AIDL接口在build/generated/source/aidl下生成一个文件夹，它的名称跟你命名的AIDL文件夹一样
     * 3.Stub类，是一个内部类，他本质上是一个Binder类。当服务端和客户端位于同一个进程时，方法调用不会走跨进程的transact过程，
     *   当两者处于不同晋城市，方法调用走transact过程，这个逻辑由Stub的内部代理类Proxy完成。
     */
    private final IBinder binder = new ICheckAppInfoManager.Stub() {
        @Override
        public List<AppInfo> getAppInfo(String sign) throws RemoteException {
            List<AppInfo> list=new ArrayList<>();
            String aidlCheckAppInfoSign = AppSignUtils.getAidlCheckAppInfoSign();
            AppLogUtils.e("AppInfoService--AppInfoService",aidlCheckAppInfoSign+"-------------"+sign);
            if(!aidlCheckAppInfoSign.equals(sign)){
                return list;
            }
            list.add(new AppInfo("app版本号(versionName)", BuildConfig.VERSION_NAME));
            list.add(new AppInfo("app版本名称(versionCode)", BuildConfig.VERSION_CODE+""));
            list.add(new AppInfo("app包名", getPackageName()));
            list.add(new AppInfo("app作者", "杨充"));
            list.add(new AppInfo("app渠道", "channel"));
            list.add(new AppInfo("token", "token"));
            return list;
        }


        @Override
        public boolean setToken(String sign, String token) throws RemoteException {
            if(!AppSignUtils.getAidlCheckAppInfoSign().equals(sign)){
                return false;
            }
            AppLogUtils.i("AppInfoService--setToken:"+ token);
            return true;
        }

        @Override
        public boolean setChannel(String sign, String channel) throws RemoteException {
            if(!AppSignUtils.getAidlCheckAppInfoSign().equals(sign)){
                return false;
            }
            AppLogUtils.i("AppInfoService--setChannel:"+ channel);
            return true;
        }

        @Override
        public boolean setAppAuthorName(String sign, String name) throws RemoteException {
            if(!AppSignUtils.getAidlCheckAppInfoSign().equals(sign)){
                return false;
            }
            AppLogUtils.i("AppInfoService--setAppAuthorName:"+ name);
            return true;
        }
    };

}

