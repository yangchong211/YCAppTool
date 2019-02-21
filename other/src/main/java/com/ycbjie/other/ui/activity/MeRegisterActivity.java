package com.ycbjie.other.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ns.yc.yccustomtextlib.pwdEt.PasswordEditText;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.other.R;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/9/27
 *     desc  : 注册页面
 *     revise: 关于密码自定义控件，直接将EditText改成PasswordEditText即可
 *             demo地址，欢迎star：https://github.com/yangchong211/YCCustomText
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_REGISTER_ACTIVITY)
public class MeRegisterActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private TextView tvTitleRight;
    private AutoCompleteTextView tvPersonUsername;
    private AutoCompleteTextView tvPersonCode;
    private PasswordEditText tvPersonPassword;
    private PasswordEditText tvPersonPasswordAgain;
    private CheckBox cbIsAgree;
    private TextView tvAbout;
    private Button btnPersonRegister;

    @Override
    public int getContentView() {
        return R.layout.activity_me_register;
    }



    @Override
    public void initView() {
        initFindById();
        initToolBar();
    }

    private void initFindById() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        tvTitleRight = findViewById(R.id.tv_title_right);
        tvPersonUsername = findViewById(R.id.tv_person_username);
        tvPersonCode = findViewById(R.id.tv_person_code);
        tvPersonPassword = findViewById(R.id.tv_person_password);
        tvPersonPasswordAgain = findViewById(R.id.tv_person_password_again);
        cbIsAgree = findViewById(R.id.cb_is_agree);
        tvAbout = findViewById(R.id.tv_about);
        btnPersonRegister = findViewById(R.id.btn_person_register);
    }

    private void initToolBar() {
        toolbarTitle.setText("注册账号");
        tvTitleRight.setText("更多");
        tvTitleRight.setVisibility(View.VISIBLE);
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        btnPersonRegister.setOnClickListener(this);
        tvTitleRight.setOnClickListener(this);
    }


    @Override
    public void initData() {

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            finish();
        } else if (i == R.id.btn_person_register) {
            goToRegister();
        } else if (i == R.id.tv_title_right){
            Bundle bundle = new Bundle();
            bundle.putString(Constant.URL,Constant.GITHUB);
            bundle.putString(Constant.TITLE,"关于更多内容");
            ARouterUtils.navigation(ARouterConstant.ACTIVITY_LIBRARY_WEB_VIEW,bundle);
        }
    }


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
            ToastUtils.showRoundRectToast("用户名不能为空");
            return;
        }
        if(TextUtils.isEmpty(code)){
            ToastUtils.showRoundRectToast("验证码不能为空");
            return;
        }
        if(TextUtils.isEmpty(pwd)){
            ToastUtils.showRoundRectToast("用户名不能为空");
            return;
        }
        if(!pwd.equals(pwdAgain)){
            ToastUtils.showRoundRectToast("两次输入密码不同");
            return;
        }

        final ProgressDialog pd = new ProgressDialog(MeRegisterActivity.this);
        pd.setMessage(getResources().getString(R.string.register_state));
        if(!pd.isShowing()){
            pd.show();
        }
    }


}
