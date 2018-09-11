package com.ns.yc.lifehelper.ui.me.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp.BaseActivity;
import com.ns.yc.yccustomtextlib.pwdEt.PasswordEditText;
import com.pedaily.yc.ycdialoglib.customToast.ToastUtil;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/27
 * 描    述：注册页面
 * 修订历史：
 * 关于密码自定义控件，直接将EditText改成PasswordEditText即可
 * demo地址，欢迎star：https://github.com/yangchong211/YCCustomText
 * ================================================
 */
public class MeRegisterActivity extends BaseActivity implements View.OnClickListener {

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
    @Bind(R.id.tv_person_username)
    AutoCompleteTextView tvPersonUsername;
    @Bind(R.id.tv_person_code)
    AutoCompleteTextView tvPersonCode;
    @Bind(R.id.tv_person_password)
    PasswordEditText tvPersonPassword;
    @Bind(R.id.tv_person_password_again)
    PasswordEditText tvPersonPasswordAgain;
    @Bind(R.id.cb_is_agree)
    CheckBox cbIsAgree;
    @Bind(R.id.tv_about)
    TextView tvAbout;
    @Bind(R.id.btn_person_register)
    Button btnPersonRegister;

    @Override
    public int getContentView() {
        return R.layout.activity_me_register;
    }

    @Override
    public void initView() {
        initToolBar();
    }

    private void initToolBar() {
        toolbarTitle.setText("注册账号");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        btnPersonRegister.setOnClickListener(this);
    }


    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.btn_person_register:
                goToRegister();
                break;
            default:
                break;
        }
    }


    @Override
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
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }


    private void goToRegister() {
        final String name = tvPersonUsername.getText().toString().trim();
        String code = tvPersonCode.getText().toString().trim();
        final String pwd = tvPersonPassword.getText().toString().trim();
        String pwdAgain = tvPersonPasswordAgain.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            ToastUtil.showToast(MeRegisterActivity.this,"用户名不能为空");
            return;
        }
        if(TextUtils.isEmpty(code)){
            ToastUtil.showToast(MeRegisterActivity.this,"验证码不能为空");
            return;
        }
        if(TextUtils.isEmpty(pwd)){
            ToastUtil.showToast(MeRegisterActivity.this,"用户名不能为空");
            return;
        }
        if(!pwd.equals(pwdAgain)){
            ToastUtil.showToast(MeRegisterActivity.this,"两次输入密码不同");
            return;
        }

        final ProgressDialog pd = new ProgressDialog(MeRegisterActivity.this);
        pd.setMessage(getResources().getString(R.string.register_state));
        if(!pd.isShowing()){
            pd.show();
        }
    }


}
