package com.yc.toollayer;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2017/6/29
 *     desc  : 跳转应用市场评分
 *     revise: 包名参考：https://www.jianshu.com/p/cfb7f212a5a2
 * </pre>
 */
public final class GoToScoreUtils {

    /**
     * 需求，跳转Android应用市场
     * 1.android存在多个应用市场，当用户手机上有多个应用市场，该跳转哪一个
     * 2.当用户没有应用市场，该如何处理
     * 3.能否获取应用市场当集合，谈后弹窗让用户选择跳转到哪一个
     */

    private static final String schemaUrl = "market://details?id=";

    /**
     * 直接跳转到应用宝
     * @param context                           上下文
     * @param packageName                       包名
     */
    public static void goToMarketQQ(Context context, String packageName) {
        try {
            Uri uri = Uri.parse(schemaUrl + packageName);
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.setClassName("com.tencent.android.qqdownloader",
                    "com.tencent.pangu.link.LinkProxyActivity");
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "您的手机没有安装应用宝", Toast.LENGTH_SHORT).show();
        }
    }

    public static void test(Context context){
        ArrayList<String> installAppMarkets = getInstallAppMarkets(context);
        ArrayList<String> filterInstallMarkets = getFilterInstallMarkets(context, installAppMarkets);

    }
    /**
     * 首先先获取手机上已经安装的应用市场
     * 获取已安装应用商店的包名列表
     * 获取有在AndroidManifest 里面注册<category android:name="android.intent.category.APP_MARKET" />的app
     * @param context                           上下文
     * @return
     */
    public static ArrayList<String> getInstallAppMarkets(Context context) {
        //默认的应用市场列表，有些应用市场没有设置APP_MARKET通过隐式搜索不到
        ArrayList<String> pkgList = new ArrayList<>();
        //将我们上传的应用市场都传上去
        pkgList.add("com.xiaomi.market");                       //小米应用商店
        pkgList.add("com.lenovo.leos.appstore");                //联想应用商店
        pkgList.add("com.oppo.market");                         //OPPO应用商店
        pkgList.add("com.tencent.android.qqdownloader");        //腾讯应用宝
        pkgList.add("com.qihoo.appstore");                      //360手机助手
        pkgList.add("com.baidu.appsearch");                     //百度手机助手
        pkgList.add("com.huawei.appmarket");                    //华为应用商店
        pkgList.add("com.wandoujia.phoenix2");                  //豌豆荚
        pkgList.add("com.hiapk.marketpho");                     //安智应用商店
        pkgList.add("com.bbk.appstore");                        //VIVO应用商店
        pkgList.add("com.pp.assistant");                        //PP手机助手
        pkgList.add("com.android.vending");                     //Google Play Store
        pkgList.add("com.dragon.android.pandaspace");           //91手机助手
        ArrayList<String> installedMarketPkgs = getInstalledMarketPkgs(context);
        //取两个list并集,去除重复
        //注意，这里需要过滤一下，应为防止有些应用市场咱们没有上传应用导致跳转失败
        pkgList.removeAll(installedMarketPkgs);
        installedMarketPkgs.addAll(pkgList);
        return installedMarketPkgs;
    }

    /**
     * 获取当前手机上的应用商店数量
     * @param context                           上下文
     * @return
     */
    public static ArrayList<String> getInstalledMarketPkgs(Context context) {
        ArrayList<String> pkgs = new ArrayList<>();
        if (context == null){
            return pkgs;
        }
        Intent intent = new Intent();
        //intent.setAction("android.intent.action.MAIN");
        //intent.addCategory("android.intent.category.APP_MARKET");
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse(schemaUrl));
        PackageManager pm = context.getPackageManager();
        // 通过queryIntentActivities获取ResolveInfo对象
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        if (infos == null || infos.size() == 0){
            return pkgs;
        }
        int size = infos.size();
        for (int i = 0; i < size; i++) {
            String pkgName = "";
            try {
                ActivityInfo activityInfo = infos.get(i).activityInfo;
                pkgName = activityInfo.packageName;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(pkgName)){
                pkgs.add(pkgName);
            }
        }
        for (int i=0 ; i<pkgs.size() ; i++){
            String s = pkgs.get(i);
            LogUtils.i("MarketTools", "应用市场----"+s);
        }
        return pkgs;
    }

    /**
     * 过滤出已经安装的包名集合
     * @param context                   上下文
     * @param pkgs                      待过滤包名集合
     * @return                          已安装的包名集合
     */
    public static ArrayList<String> getFilterInstallMarkets(Context context,ArrayList<String> pkgs) {
        List<AppUtils.AppInfo> mAppInfo = new ArrayList<>();
        mAppInfo.clear();
        ArrayList<String> appList = new ArrayList<>();
        if (context == null || pkgs == null || pkgs.size() == 0){
            return appList;
        }
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPkgs = pm.getInstalledPackages(0);
        int li = installedPkgs.size();
        int lj = pkgs.size();
        for (int j = 0; j < lj; j++) {
            for (int i = 0; i < li; i++) {
                String installPkg = "";
                String checkPkg = pkgs.get(j);
                PackageInfo packageInfo = installedPkgs.get(i);
                try {
                    installPkg = packageInfo.packageName;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(installPkg))
                    continue;
                if (installPkg.equals(checkPkg)) {
                    // 如果非系统应用，则添加至appList,这个会过滤掉系统的应用商店，如果不需要过滤就不用这个判断
                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        //将应用相关信息缓存起来，用于自定义弹出应用列表信息相关用
                        AppUtils.AppInfo appInfo = AppUtils.getAppInfo();
                        appInfo.setName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
                        appInfo.setIcon(packageInfo.applicationInfo.loadIcon(context.getPackageManager()));
                        appInfo.setPackageName(packageInfo.packageName);
                        appInfo.setVersionCode(packageInfo.versionCode);
                        appInfo.setVersionName(packageInfo.versionName);
                        mAppInfo.add(appInfo);
                        appList.add(installPkg);
                    }
                    break;
                }
            }
        }
        return appList;
    }


    /**
     * 打开应用市场，跳转到应用市场app详情界面
     * @param appPkg    App的包名
     * @param marketPkg 应用市场包名
     */
    public static void launchAppDetail(Context context , String appPkg, String marketPkg) {
        try {
            if (TextUtils.isEmpty(appPkg)){
                return;
            }
            //商店中使用包名来唯一标识区分应用
            Uri uri = Uri.parse(schemaUrl + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg)) {
                intent.setPackage(marketPkg);
            }
            //添加新栈flag标记
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException anf) {
            LogUtils.e("MarketTools", "要跳转的应用市场不存在!");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("MarketTools", "其他错误：" + e.getMessage());
        }
    }

    /**
     * 判断本地是否存在某个APP
     * @param context       上下文
     * @param pkgName       应用的包名
     * @return              true：该app存在；false：该app不存在
     */
    public static boolean isPkgInstalled(Context context, String pkgName) {
        if ("".equals(pkgName) || pkgName.length() <= 0) {
            return false;
        }
        PackageInfo packageInfo ;
        try {
            synchronized (context){
                packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
            }
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /***
     * 不指定包名
     * @param mContext
     */
    public static void startMarket(Context mContext) {
        //得到包名
        String packageName = mContext.getPackageName();
        startMarket(mContext, packageName);
    }

    /**
     * 指定包名
     * 根据手机厂商，跳转该厂商的应用市场
     * @param mContext                              上下文
     * @param packageName                           包名
     */
    public static boolean startMarket(Context mContext, String packageName) {
        try {
            //获得手机厂商
            String deviceBrand = getDeviceBrand();
            //根据厂商获取对应市场的包名
            //大写
            String brandName = deviceBrand.toUpperCase();
            if (TextUtils.isEmpty(brandName)) {
                LogUtils.e("MarketTools", "没有读取到手机厂商~~");
                return false;
            }
            String marketPackageName = getBrandName(brandName);
            if (null == marketPackageName || "".equals(marketPackageName)) {
                //手机不再列表里面,去尝试寻找
                //跳转百度
                boolean isExit1 = isPkgInstalled(mContext, PACKAGE_NAME.BAIDU_PACKAGE_NAME);
                if (isExit1) {
                    startMarket(mContext, packageName, PACKAGE_NAME.BAIDU_PACKAGE_NAME);
                    return true;
                }
                //跳转应用宝
                boolean isExit2 = isPkgInstalled(mContext, PACKAGE_NAME.TENCENT_PACKAGE_NAME);
                if (isExit2) {
                    startMarket(mContext, packageName, PACKAGE_NAME.TENCENT_PACKAGE_NAME);
                    return true;
                }
                //跳转华为
                boolean isExit3 = isPkgInstalled(mContext, PACKAGE_NAME.HUAWEI_PACKAGE_NAME);
                if (isExit3) {
                    startMarket(mContext, packageName, PACKAGE_NAME.HUAWEI_PACKAGE_NAME);
                    return true;
                }
                //跳转小米
                boolean isExit4 = isPkgInstalled(mContext, PACKAGE_NAME.XIAOMI_PACKAGE_NAME);
                if (isExit4) {
                    startMarket(mContext, packageName, PACKAGE_NAME.XIAOMI_PACKAGE_NAME);
                    return true;
                }
            }
            startMarket(mContext, packageName, marketPackageName);
            return true;
        } catch (ActivityNotFoundException anf) {
            LogUtils.e("MarketTools", "要跳转的应用市场不存在!");
        } catch (Exception e) {
            LogUtils.e("MarketTools", "其他错误：" + e.getMessage());
        }
        return false;
    }

    /***
     * 指定包名，指定市场
     * @param mContext
     * @param packageName
     * @param marketPackageName
     */
    public static void startMarket(Context mContext, String packageName, String marketPackageName) {
        try {
            launchAppDetail(mContext, packageName, marketPackageName);
        } catch (ActivityNotFoundException anf) {
            LogUtils.e("MarketTools", "要跳转的应用市场不存在!");
        } catch (Exception e) {
            LogUtils.e("MarketTools", "其他错误：" + e.getMessage());
        }
    }


    private static String getBrandName(String brandName) {
        if (BRAND.HUAWEI_BRAND.equals(brandName)) {
            //华为
            return PACKAGE_NAME.HUAWEI_PACKAGE_NAME;
        } else if (BRAND.OPPO_BRAND.equals(brandName)) {
            //oppo
            return PACKAGE_NAME.OPPO_PACKAGE_NAME;
        } else if (BRAND.VIVO_BRAND.equals(brandName)) {
            //vivo
            return PACKAGE_NAME.VIVO_PACKAGE_NAME;
        } else if (BRAND.XIAOMI_BRAND.equals(brandName)) {
            //小米
            return PACKAGE_NAME.XIAOMI_PACKAGE_NAME;
        } else if (BRAND.LENOVO_BRAND.equals(brandName)) {
            //联想
            return PACKAGE_NAME.LIANXIANG_PACKAGE_NAME;
        } else if (BRAND.QH360_BRAND.equals(brandName)) {
            //360
            return PACKAGE_NAME.QH360_PACKAGE_NAME;
        } else if (BRAND.MEIZU_BRAND.equals(brandName)) {
            //魅族
            return PACKAGE_NAME.MEIZU_PACKAGE_NAME;
        } else if (BRAND.HONOR_BRAND.equals(brandName)) {
            //华为
            return PACKAGE_NAME.HUAWEI_PACKAGE_NAME;
        } else if (BRAND.XIAOLAJIAO_BRAND.equals(brandName)) {
            //小辣椒
            return PACKAGE_NAME.ZHUOYI_PACKAGE_NAME;
        } else if (BRAND.ZTE_BRAND.equals(brandName)) {
            //zte
            return PACKAGE_NAME.ZTE_PACKAGE_NAME;
        } else if (BRAND.NIUBIA_BRAND.equals(brandName)) {
            //努比亚
            return PACKAGE_NAME.NIUBIA_PACKAGE_NAME;
        } else if (BRAND.ONE_PLUS_BRAND.equals(brandName)) {
            //OnePlus
            return PACKAGE_NAME.OPPO_PACKAGE_NAME;
        } else if (BRAND.MEITU_BRAND.equals(brandName)) {
            //美图
            return PACKAGE_NAME.MEITU_PACKAGE_NAME;
        } else if (BRAND.SONY_BRAND.equals(brandName)) {
            //索尼
            return PACKAGE_NAME.GOOGLE_PACKAGE_NAME;
        } else if (BRAND.GOOGLE_BRAND.equals(brandName)) {
            //google
            return PACKAGE_NAME.GOOGLE_PACKAGE_NAME;
        }
        return "";
    }

    /**
     * 获取手机厂商
     */
    private static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    public static class BRAND {
        public static final String HUAWEI_BRAND = "HUAWEI";//HUAWEI_PACKAGE_NAME
        public static final String HONOR_BRAND = "HONOR";//HUAWEI_PACKAGE_NAME
        public static final String OPPO_BRAND = "OPPO";//OPPO_PACKAGE_NAME
        public static final String MEIZU_BRAND = "MEIZU";//MEIZU_PACKAGE_NAME
        public static final String VIVO_BRAND = "VIVO";//VIVO_PACKAGE_NAME
        public static final String XIAOMI_BRAND = "XIAOMI";//XIAOMI_PACKAGE_NAME
        public static final String LENOVO_BRAND = "LENOVO";//LIANXIANG_PACKAGE_NAME //Lenovo
        public static final String ZTE_BRAND = "ZTE";//ZTE_PACKAGE_NAME
        public static final String XIAOLAJIAO_BRAND = "XIAOLAJIAO";//ZHUOYI_PACKAGE_NAME
        public static final String QH360_BRAND = "360";//QH360_PACKAGE_NAME
        public static final String NIUBIA_BRAND = "NUBIA";//NIUBIA_PACKAGE_NAME
        public static final String ONE_PLUS_BRAND = "ONEPLUS";//OPPO_PACKAGE_NAME
        public static final String MEITU_BRAND = "MEITU";//MEITU_PACKAGE_NAME
        public static final String SONY_BRAND = "SONY";//GOOGLE_PACKAGE_NAME
        public static final String GOOGLE_BRAND = "GOOGLE";//GOOGLE_PACKAGE_NAME
        public static final String HTC_BRAND = "HTC";//未知应用商店包名
        public static final String ZUK_BRAND = "ZUK";//未知应用商店包名
    }

    /**
     * 华为，oppo,vivo,小米，360，联想，魅族，安智，百度，阿里，应用宝，goog，豌豆荚，pp助手
     */
    public static class PACKAGE_NAME {
        public static final String OPPO_PACKAGE_NAME = "com.oppo.market";//oppo
        public static final String VIVO_PACKAGE_NAME = "com.bbk.appstore";//vivo
        public static final String HUAWEI_PACKAGE_NAME = "com.huawei.appmarket";//华为
        public static final String QH360_PACKAGE_NAME = "com.qihoo.appstore";//360
        public static final String XIAOMI_PACKAGE_NAME = "com.xiaomi.market";//小米
        public static final String MEIZU_PACKAGE_NAME = "com.meizu.mstore";//，魅族
        public static final String LIANXIANG_PACKAGE_NAME = "com.lenovo.leos.appstore";//联想
        public static final String ZTE_PACKAGE_NAME = "zte.com.market";//zte
        public static final String ZHUOYI_PACKAGE_NAME = "com.zhuoyi.market";//卓易
        public static final String GOOGLE_PACKAGE_NAME = "com.android.vending";//google
        public static final String NIUBIA_PACKAGE_NAME = "com.nubia.neostore";//努比亚
        public static final String MEITU_PACKAGE_NAME = "com.android.mobile.appstore";//美图
        public static final String BAIDU_PACKAGE_NAME = "com.baidu.appsearch";//baidu
        public static final String TENCENT_PACKAGE_NAME = "com.tencent.android.qqdownloader";//应用宝
        public static final String PPZHUSHOU_PACKAGE_NAME = "com.pp.assistant";//pp助手
        public static final String ANZHI_PACKAGE_NAME = "com.goapk.market";//安智市场
        public static final String WANDOUJIA_PACKAGE_NAME = "com.wandoujia.phonenix2";//豌豆荚
        public static final String SUONI_PACKAGE_NAME = "com.android.vending";//索尼
    }

}
