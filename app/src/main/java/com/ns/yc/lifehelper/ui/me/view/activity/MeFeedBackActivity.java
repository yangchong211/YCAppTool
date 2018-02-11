package com.ns.yc.lifehelper.ui.me.view.activity;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.utils.PermissionsUtils;
import com.ns.yc.ycmultiinputviewlib.MultiEditInputView;
import com.pedaily.yc.ycdialoglib.customToast.ToastUtil;

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
    @Bind(R.id.mev_view)
    MultiEditInputView mevView;
    private String phone = "13667225184";
    private String contentText;

    @Override
    public int getContentView() {
        return R.layout.activity_me_feed_back;
    }

    @Override
    public void initView() {
        PermissionsUtils.verifyCallPermissions(this);
        initToolBar();
        initMevView();
        //点击空白处隐藏软键盘
        //KeyboardUtils.clickBlankArea2HideSoftInput();
        //KeyboardUtils.toggleSoftInput();
        setListenerToRootView();
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
        contentText = mevView.getContentText();
        if(TextUtils.isEmpty(contentText)){
            ToastUtil.showToast(MeFeedBackActivity.this,"输入内容不能为空");
            return;
        }
        Intent smsIntent = IntentUtils.getSendSmsIntent(phone, contentText);
        startActivity(smsIntent);
    }

    private void toSendEmail() {
        contentText = mevView.getContentText();
        if(TextUtils.isEmpty(contentText)){
            ToastUtil.showToast(MeFeedBackActivity.this,"输入内容不能为空");
            return;
        }
        Uri uri = Uri.parse("yangchong211@163.com");
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分"); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, contentText); // 正文
        startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
    }


    private void toWxMe() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ToastUtil.showToast(MeFeedBackActivity.this, "检查到您手机没有安装微信，请安装后使用该功能");
        }
    }


    private void initMevView() {
        //contentText = mevView.getContentText();
        //String hintText = mevView.getHintText();
        mevView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(MeFeedBackActivity.this);
            }
        });
    }

    /**-----------点击空白处隐藏软键盘，第一种方法----------------------------------------------*/
    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }*/


    /**-----------点击空白处隐藏软键盘，第二种方法----------------------------------------------*/
    private void setListenerToRootView() {
        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int headerHeight = BarUtils.getActionBarHeight(MeFeedBackActivity.this) + BarUtils.getStatusBarHeight(MeFeedBackActivity.this);
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
                if (heightDiff > headerHeight) {
                    Log.e("keyboard", "keyboard is up");
                } else {
                    Log.e("keyboard", "keyboard is hidden");
                }
            }
        });
    }

}
