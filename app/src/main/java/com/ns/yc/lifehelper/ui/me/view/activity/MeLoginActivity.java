package com.ns.yc.lifehelper.ui.me.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;
import com.ns.yc.yccustomtextlib.pwdEt.PasswordEditText;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.base.mvp.BaseActivity;
/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/27
 * 描    述：登录页面
 * 修订历史：
 * 关于密码自定义控件，直接将EditText改成PasswordEditText即可
 * demo地址，欢迎star：https://github.com/yangchong211/YCCustomText
 * ================================================
 */
public class MeLoginActivity extends BaseActivity implements View.OnClickListener {

    TextView tvTitleLeft;
    FrameLayout llTitleMenu;
    TextView toolbarTitle;
    ImageView ivRightImg;
    FrameLayout llSearch;
    TextView tvTitleRight;
    Toolbar toolbar;
    AutoCompleteTextView tvPersonUsername;
    PasswordEditText tvPersonPassword;
    Button btnPersonLogin;
    TextView tvPersonRegister;
    TextView tvWeiXinLogin;
    TextView tvQqLogin;
    TextView tvSinaLogin;

    private boolean progressShow;
    private static final String TAG = "MeLoginActivity";


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_me_login;
    }



    @Override
    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        tvTitleLeft = findViewById(R.id.tv_title_left);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        llSearch = findViewById(R.id.ll_search);
        ivRightImg = findViewById(R.id.iv_right_img);
        tvTitleRight = findViewById(R.id.tv_title_right);

        tvPersonUsername = (AutoCompleteTextView) findViewById(R.id.tv_person_username);
        tvPersonPassword = (PasswordEditText) findViewById(R.id.tv_person_password);
        btnPersonLogin = (Button) findViewById(R.id.btn_person_login);
        tvPersonRegister = (TextView) findViewById(R.id.tv_person_register);
        tvWeiXinLogin = (TextView) findViewById(R.id.tv_wei_xin_login);
        tvQqLogin = (TextView) findViewById(R.id.tv_qq_login);
        tvSinaLogin = (TextView) findViewById(R.id.tv_sina_login);


        initToolBar();
    }


    private void initToolBar() {
        toolbarTitle.setText("注册账号");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        tvPersonRegister.setOnClickListener(this);
        btnPersonLogin.setOnClickListener(this);
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
            case R.id.tv_person_register:
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_REGISTER_ACTIVITY);
                break;
            case R.id.btn_person_login:
                goToLogin();
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
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
     */
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


    private void goToLogin() {
        final String name = tvPersonUsername.getText().toString().trim();
        final String pwd = tvPersonPassword.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            ToastUtils.showToast("用户名不能为空");
            return;
        }
        if(TextUtils.isEmpty(pwd)){
            ToastUtils.showToast("密码不能为空");
            return;
        }
        progressShow = true;
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                LogUtils.e(TAG +"EMClient.getInstance().onCancel");
                progressShow = false;
                MainActivity.startActivity(MeLoginActivity.this,MainActivity.USER);
            }
        });
        pd.setMessage(getString(R.string.login_state));
    }


}
