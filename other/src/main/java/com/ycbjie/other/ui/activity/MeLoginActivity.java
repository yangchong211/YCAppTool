package com.ycbjie.other.ui.activity;

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

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.ns.yc.yccustomtextlib.pwdEt.PasswordEditText;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.other.R;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/9/27
 *     desc  : 登录页面
 *     revise: 关于密码自定义控件，直接将EditText改成PasswordEditText即可
 *             demo地址，欢迎star：https://github.com/yangchong211/YCCustomText
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_LOGIN_ACTIVITY)
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
        initFindById();
        initToolBar();
    }

    private void initFindById() {
        toolbar = findViewById(R.id.toolbar);
        tvTitleLeft = findViewById(R.id.tv_title_left);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        llSearch = findViewById(R.id.ll_search);
        ivRightImg = findViewById(R.id.iv_right_img);
        tvTitleRight = findViewById(R.id.tv_title_right);

        tvPersonUsername = findViewById(R.id.tv_person_username);
        tvPersonPassword = findViewById(R.id.tv_person_password);
        btnPersonLogin = findViewById(R.id.btn_person_login);
        tvPersonRegister = findViewById(R.id.tv_person_register);
        tvWeiXinLogin = findViewById(R.id.tv_wei_xin_login);
        tvQqLogin = findViewById(R.id.tv_qq_login);
        tvSinaLogin = findViewById(R.id.tv_sina_login);
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
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            finish();

        } else if (i == R.id.tv_person_register) {
            startActivity(MeRegisterActivity.class);

        } else if (i == R.id.btn_person_login) {
            goToLogin();

        } else {
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
                //跳转首页
            }
        });
        pd.setMessage(getString(R.string.login_state));
    }


}
