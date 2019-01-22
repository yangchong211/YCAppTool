package com.ns.yc.lifehelper.ui.me.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ycbjie.library.base.config.AppConfig;
import com.ns.yc.lifehelper.model.bean.UpdateBean;
import com.ns.yc.lifehelper.ui.me.contract.MeSettingContract;
import com.ns.yc.lifehelper.ui.me.model.MeAppModel;
import com.ycbjie.library.web.view.WebViewActivity;
import com.ns.yc.lifehelper.utils.DialogUtils;
import com.ns.yc.lifehelper.utils.FileCacheUtils;
import com.ns.yc.lifehelper.utils.GoToScoreUtils;
import com.ycbjie.library.utils.rxUtils.RxUtil;
import com.pedaily.yc.ycdialoglib.dialog.CustomSelectDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.ycbjie.ycthreadpoollib.PoolThread;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：设置中心页
 * 修订历史：
 * ================================================
 */
public class MeSettingPresenter implements MeSettingContract.Presenter {


    private MeSettingContract.View mMeSetView;
    @NonNull
    private CompositeSubscription mSubscriptions;
    private String[] items = {"原图", "默认", "省流"};
    private int yourChoice;

    public MeSettingPresenter(MeSettingContract.View meSetView) {
        this.mMeSetView = meSetView;
        mSubscriptions = new CompositeSubscription();
    }

    /**
     * 开始绑定数据
     */
    @Override
    public void subscribe() {
        initView();
    }

    /**
     * 解除绑定数据
     */
    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    /**
     * 初始化工作
     */
    private void initView() {
        mMeSetView.startLoading();
        mMeSetView.changeSwitchState(AppConfig.INSTANCE.isShowListImg());
        mMeSetView.changeIsShowLauncherImgSwitchState(AppConfig.INSTANCE.isShowGirlImg());
        mMeSetView.changeIsAlwaysShowLauncherImgSwitchState(AppConfig.INSTANCE.isProbabilityShowImg());

        setImageQualityChooseIsEnable(AppConfig.INSTANCE.isShowListImg());
        setIsLauncherAlwaysShowImgEnable(AppConfig.INSTANCE.isShowGirlImg());
        setThumbnailQuality(AppConfig.INSTANCE.getThumbnailQuality());
        showCacheSize();

        boolean isShowListImg = AppConfig.INSTANCE.isShowListImg();
        int imageThumbnailQuality = AppConfig.INSTANCE.getThumbnailQuality();
    }

    /**
     * 根据是否展示list中图片的状态，确定缩略图选项是否可以点击
     */
    private void setImageQualityChooseIsEnable(boolean isEnable) {
        if (isEnable) {
            mMeSetView.setImageQualityChooseEnable();
        } else {
            mMeSetView.setImageQualityChooseUnEnable();
        }
    }

    /**
     * 根据是否启动页展示妹子图，确定随机出现选项是否可以点击
     */
    private void setIsLauncherAlwaysShowImgEnable(boolean isEnable) {
        if (isEnable) {
            mMeSetView.setLauncherImgProbabilityEnable();
        } else {
            mMeSetView.setLauncherImgProbabilityUnEnable();
        }
    }

    /**
     * 显示缩略图质量
     */
    private void setThumbnailQuality(int thumbnailQuality) {
        AppConfig.INSTANCE.setThumbnailQuality(thumbnailQuality);
        mMeSetView.setThumbnailQualityInfo(thumbnailQuality);
    }

    /**
     * 获取缓存的大小
     */
    private void showCacheSize() {
        mMeSetView.showCacheSize();
    }

    @Override
    public void saveIsListShowImg(boolean isListShowImg) {
        AppConfig.INSTANCE.setShowListImg(isListShowImg);
        setImageQualityChooseIsEnable(isListShowImg);
    }

    @Override
    public void saveIsLauncherShowImg(boolean isLauncherShowImg) {
        AppConfig.INSTANCE.setShowGirlImg(isLauncherShowImg);
        setIsLauncherAlwaysShowImgEnable(isLauncherShowImg);
    }

    @Override
    public void saveIsLauncherAlwaysShowImg(boolean isLauncherAlwaysShowImg) {
        AppConfig.INSTANCE.setProbabilityShowImg(isLauncherAlwaysShowImg);
    }

    /**
     * 清理缓存
     */
    @Override
    public void cleanAppCache(final Activity activity) {
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(activity);
        normalDialog.setTitle("温馨提示");
        normalDialog.setMessage("是否清空缓存");
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DialogUtils.showProgressDialog(activity);
                PoolThread executor = AppConfig.INSTANCE.getExecutor();
                executor.setName("load");
                executor.setDelay(2, TimeUnit.MILLISECONDS);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        DialogUtils.dismissProgressDialog();
                        FileCacheUtils.cleanInternalCache(activity);
                        mMeSetView.setClearText();
                    }
                });
            }
        });
        normalDialog.setNegativeButton("关闭", null);
        //创建对话框
        AlertDialog dialog = normalDialog.create();
        //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Override
    public void checkVersion(final String currentVersion) {
        MeAppModel model = MeAppModel.getInstance();
        Subscription rxSubscription = model.getVersionInfo()
                .compose(RxUtil.<UpdateBean>rxSchedulerHelper())
                .compose(RxUtil.<UpdateBean>handleMyResult())
                .subscribe(new Subscriber<UpdateBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showShort("错误"+e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(UpdateBean updateBean) {
                        if (Integer.valueOf(currentVersion.replace(".", "")) <
                                Integer.valueOf(updateBean.getCode().replace(".", ""))) {
                            mMeSetView.showUpdateDialog(updateBean);
                        } else {
                            ToastUtils.showShort("已经是最新版本");
                        }
                    }
                });
        mSubscriptions.add(rxSubscription);
    }

    @Override
    public void exitLogout() {
        mMeSetView.exitLogout();
    }


    /**
     * 展示缩略图质量
     */
    @Override
    public void showPicQualityDialog(Activity activity) {
        yourChoice = -1;
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("缩略图质量");
        // 第二个参数是默认选项，此处设置为0
        dialog.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                yourChoice = which;
            }
        });
        dialog.setNegativeButton("取消",null);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMeSetView.setThumbnailQualityInfo(yourChoice);
            }
        });
        dialog.show();
    }


    /**
     * 跳转应用市场
     */
    @Override
    public void goToStar(final Activity context) {
        ArrayList<String> installAppMarkets = GoToScoreUtils.getInstallAppMarkets(context);
        final ArrayList<String> filterInstallMarkets = GoToScoreUtils.getFilterInstallMarkets(context, installAppMarkets);
        final ArrayList<String> markets = new ArrayList<>();
        if (filterInstallMarkets.size() > 0) {
            //过滤
            for (int a = 0; a < filterInstallMarkets.size(); a++) {
                Log.e("应用市场++++", filterInstallMarkets.get(a));
                String pkg = filterInstallMarkets.get(a);
                if (installAppMarkets.contains(pkg)) {
                    markets.add(pkg);
                }
            }
            List<String> names = new ArrayList<>();
            for (int b = 0; b < markets.size(); b++) {
                AppUtils.AppInfo appInfo = AppUtils.getAppInfo(markets.get(b));
                String name = appInfo.getName();
                names.add(name);
            }
            DialogUtils.showDialog(context, new CustomSelectDialog.SelectDialogListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GoToScoreUtils.launchAppDetail(context, "com.zero2ipo.harlanhu.pedaily", markets.get(position));
                }
            }, names);
        } else {
            //投资界应用宝评分链接
            String qqUrl = "http://android.myapp.com/myapp/detail.htm?apkName=com.zero2ipo.harlanhu.pedaily";
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url", qqUrl);
            context.startActivity(intent);
        }
    }



}
