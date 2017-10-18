package com.ns.yc.lifehelper.ui.me.view;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.IntentUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.utils.PermissionsUtils;
import com.pedaily.yc.ycdialoglib.ToastUtil;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/21
 * 描    述：意见反馈页面
 * 修订历史：
 * ================================================
 */
public class MeFeedBackActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.tv_call_me)
    TextView tvCallMe;
    @Bind(R.id.tv_send_sms)
    TextView tvSendSms;
    @Bind(R.id.tv_send_email)
    TextView tvSendEmail;
    @Bind(R.id.tv_wx_me)
    TextView tvWxMe;
    private MeFeedBackActivity activity;
    private String phone = "13667225184";

    @Override
    public int getContentView() {
        return R.layout.activity_me_feed_back;
    }

    @Override
    public void initView() {
        PermissionsUtils.verifyCallPermissions(this);
        activity = MeFeedBackActivity.this;
        initToolBar();
    }

    private void initToolBar() {
        toolbarTitle.setText("意见反馈");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        tvCallMe.setOnClickListener(this);
        tvSendSms.setOnClickListener(this);
        tvSendEmail.setOnClickListener(this);
        tvWxMe.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.tv_call_me:
                toCallMe();
                break;
            case R.id.tv_send_sms:
                toSendSms();
                break;
            case R.id.tv_send_email:
                toSendEmail();
                break;
            case R.id.tv_wx_me:
                toWxMe();
                break;
        }
    }

    private void toCallMe() {
        Intent callIntent = IntentUtils.getCallIntent(phone);
        startActivity(callIntent);
    }


    private void toSendSms() {
        Intent smsIntent = IntentUtils.getSendSmsIntent(phone, "发送短信");
        startActivity(smsIntent);
    }

    private void toSendEmail() {
        Uri uri = Uri.parse("yangchong211@163.com");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分"); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分"); // 正文
        startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
    }


    private void toWxMe() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ToastUtil.showToast(activity,"检查到您手机没有安装微信，请安装后使用该功能");
        }
    }

}
