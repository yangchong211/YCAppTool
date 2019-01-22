package com.ycbjie.other.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ns.yc.ycmultiinputviewlib.MultiEditInputView;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.other.R;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/9/11
 *     desc  : 意见反馈页面
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_OTHER_FEEDBACK)
public class MeFeedBackActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private TextView tvCallMe;
    private TextView tvSendSms;
    private TextView tvSendEmail;
    private TextView tvWxMe;
    private MultiEditInputView mevView;
    private TextView tvCommitFeedback;
    private String phone = "13667225184";
    private String contentText;

    @Override
    public int getContentView() {
        return R.layout.activity_me_feed_back;
    }

    @Override
    public void initView() {
        initFindById();
        initToolBar();
        initMevView();
    }

    private void initFindById() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        mevView = findViewById(R.id.mev_view);
        tvCallMe = findViewById(R.id.tv_call_me);
        tvSendSms = findViewById(R.id.tv_send_sms);
        tvSendEmail = findViewById(R.id.tv_send_email);
        tvWxMe = findViewById(R.id.tv_wx_me);
        tvCommitFeedback = findViewById(R.id.tv_commit_feedback);
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
        tvCommitFeedback.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            finish();
        } else if (i == R.id.tv_call_me) {
            toCallMe();
        } else if (i == R.id.tv_send_sms) {
            toSendSms();
        } else if (i == R.id.tv_send_email) {
            toSendEmail();
        } else if (i == R.id.tv_wx_me) {
            toWxMe();
        } else if (i == R.id.tv_commit_feedback){
            contentText = mevView.getContentText();
            if(TextUtils.isEmpty(contentText)){
                ToastUtils.showToast("输入内容不能为空");
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(Constant.CONTENT, contentText);
            setResult(999, intent);
            finish();
        }
    }

    private void toCallMe() {
        Intent callIntent = IntentUtils.getCallIntent(phone);
        startActivity(callIntent);
    }


    private void toSendSms() {
        contentText = mevView.getContentText();
        if(TextUtils.isEmpty(contentText)){
            ToastUtils.showToast("输入内容不能为空");
            return;
        }
        Intent smsIntent = IntentUtils.getSendSmsIntent(phone, contentText);
        startActivity(smsIntent);
    }

    private void toSendEmail() {
        contentText = mevView.getContentText();
        if(TextUtils.isEmpty(contentText)){
            ToastUtils.showToast("输入内容不能为空");
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
            ToastUtils.showToast("检查到您手机没有安装微信，请安装后使用该功能");
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
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

}
