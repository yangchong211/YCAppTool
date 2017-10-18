package com.ns.yc.lifehelper.ui.me.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.ui.main.WebViewActivity;
import com.ns.yc.lifehelper.utils.AppUtil;
import com.ns.yc.lifehelper.utils.GoToScoreUtils;
import com.ns.yc.ycutilslib.loadingDialog.ViewLoading;
import com.ns.yc.ycutilslib.switchButton.SwitchButton;
import com.pedaily.yc.ycdialoglib.selector.CustomSelectDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/12
 * 描    述：我的设置中心页面
 * 修订历史：
 * ================================================
 */
public class MeSettingActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_title_left)
    TextView tvTitleLeft;
    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_right_img)
    ImageView ivRightImg;
    @Bind(R.id.ll_search)
    FrameLayout llSearch;
    @Bind(R.id.tv_title_right)
    TextView tvTitleRight;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rl_set_go_star)
    RelativeLayout rlSetGoStar;
    @Bind(R.id.switch_button)
    SwitchButton switchButton;
    @Bind(R.id.switch_night)
    SwitchButton switchNight;
    @Bind(R.id.tv_set_cache_size)
    TextView tvSetCacheSize;
    @Bind(R.id.rl_set_clean_cache)
    RelativeLayout rlSetCleanCache;
    @Bind(R.id.rl_set_revise_pwd)
    RelativeLayout rlSetRevisePwd;
    @Bind(R.id.tv_is_cert)
    TextView tvIsCert;
    @Bind(R.id.rl_set_phone)
    RelativeLayout rlSetPhone;
    @Bind(R.id.rl_set_about_us)
    RelativeLayout rlSetAboutUs;
    @Bind(R.id.rl_set_binding)
    RelativeLayout rlSetBinding;
    @Bind(R.id.tv_exit)
    TextView tvExit;
    private ViewLoading mLoading;
    private MeSettingActivity activity;

    @Override
    public int getContentView() {
        return R.layout.activity_me_setting;
    }

    @Override
    public void initView() {
        activity = MeSettingActivity.this;
        initToolBar();
        initLoading();
    }

    private void initToolBar() {
        toolbarTitle.setText("设置中心");
    }

    private void initLoading() {
        // 添加Loading
        mLoading = new ViewLoading(this, Constant.loadingStyle) {
            @Override
            public void loadCancel() {

            }
        };
        if (!mLoading.isShowing()) {
            mLoading.show();
        }
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        rlSetGoStar.setOnClickListener(this);
    }

    @Override
    public void initData() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
            }
        }, 2000);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.rl_set_go_star:
                goToStar();
                break;
        }
    }

    /**
     * 去应用市场评分
     */
    private void goToStar() {
        ArrayList<String> installAppMarkets = GoToScoreUtils.getInstallAppMarkets(activity);
        final ArrayList<String> filterInstallMarkets = GoToScoreUtils.getFilterInstallMarkets(activity, installAppMarkets);
        final ArrayList<String> markets = new ArrayList<>();
        if(filterInstallMarkets.size()>0){
            //过滤
            for(int a=0 ; a<filterInstallMarkets.size() ; a++){
                Log.e("应用市场++++",filterInstallMarkets.get(a));
                String pkg = filterInstallMarkets.get(a);
                if(installAppMarkets.contains(pkg)){
                    markets.add(pkg);
                }
            }
            List<String> names = new ArrayList<>();
            for(int b=0 ; b<markets.size() ; b++){
                com.blankj.utilcode.util.AppUtils.AppInfo appInfo = com.blankj.utilcode.util.AppUtils.getAppInfo(markets.get(b));
                String name = appInfo.getName();
                names.add(name);
            }
            showDialog(activity , new CustomSelectDialog.SelectDialogListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GoToScoreUtils.launchAppDetail(activity,"com.zero2ipo.harlanhu.pedaily",markets.get(position));
                }
            }, names);
        }else {
            //投资界应用宝评分链接
            String QQUrl = "http://android.myapp.com/myapp/detail.htm?apkName=com.zero2ipo.harlanhu.pedaily";
            Intent intent = new Intent(activity, WebViewActivity.class);
            intent.putExtra("url", QQUrl);
            activity.startActivity(intent);
        }
    }

    /**展示对话框视图，构造方法创建对象*/
    public CustomSelectDialog showDialog(Activity activity , CustomSelectDialog.SelectDialogListener listener, List<String> names) {
        CustomSelectDialog dialog = new CustomSelectDialog(activity, R.style.transparentFrameWindowStyle, listener, names);
        if (AppUtil.isActivityLiving(activity)) {
            dialog.show();
        }
        return dialog;
    }

}
