package com.ns.yc.lifehelper.ui.me.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.api.ConstantKeys;
import com.ns.yc.lifehelper.base.BaseConfig;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.other.imTalk.ui.ImTalkActivity;
import com.ns.yc.lifehelper.utils.EventBusUtils;
import com.ns.yc.lifehelper.utils.IMEMClientUtils;
import com.ns.yc.lifehelper.utils.LogUtils;
import com.ns.yc.yccustomtextlib.pwdEt.PasswordEditText;
import com.pedaily.yc.ycdialoglib.toast.ToastUtil;

import butterknife.Bind;

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
    @Bind(R.id.tv_person_password)
    PasswordEditText tvPersonPassword;
    @Bind(R.id.btn_person_login)
    Button btnPersonLogin;
    @Bind(R.id.tv_person_register)
    TextView tvPersonRegister;
    @Bind(R.id.tv_wei_xin_login)
    TextView tvWeiXinLogin;
    @Bind(R.id.tv_qq_login)
    TextView tvQqLogin;
    @Bind(R.id.tv_sina_login)
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
                startActivity(MeRegisterActivity.class);
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
            ToastUtil.showToast(MeLoginActivity.this,"用户名不能为空");
            return;
        }
        if(TextUtils.isEmpty(pwd)){
            ToastUtil.showToast(MeLoginActivity.this,"密码不能为空");
            return;
        }
        /*String loginName = SPUtils.getInstance(Constant.SP_NAME).getString(ConstantKeys.NAME);
        String loginPwd = SPUtils.getInstance(Constant.SP_NAME).getString(ConstantKeys.PWD);
        if(!name.equals(loginName) || !loginPwd.equals(pwd)){
            ToastUtil.showToast(MeLoginActivity.this,"用户名或者密码错误");
            return;
        }*/

        progressShow = true;
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                LogUtils.e(TAG +"EMClient.getInstance().onCancel");
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.login_state));

        IMEMClientUtils.imLogin(name, pwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                LogUtils.e(TAG +"login: onSuccess");
                // ** manually load all local groups and conversation
                //以下两个方法是为了保证进入主页面后本地会话和群组都 load 完毕。
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                if (!MeLoginActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //注意，一定要放到runOnUiThread方法中吐司，否则崩溃
                        ToastUtil.showToast(MeLoginActivity.this,"登录成功");
                        //EventBusUtils.post(new LoginSuccessEvent(name));
                    }
                });

                Intent intent = new Intent(MeLoginActivity.this, ImTalkActivity.class);
                startActivity(intent);
                Constant.isLogin = true;
                SPUtils.getInstance(Constant.SP_NAME).put(ConstantKeys.NAME,name);
                SPUtils.getInstance(Constant.SP_NAME).put(ConstantKeys.PWD,pwd);
                BaseConfig.INSTANCE.setLogin(true);
                finish();
            }

            @Override
            public void onError(final int code, final String error) {
                LogUtils.e(TAG+"login: onError: " + code);
                if (!progressShow) {
                    return;
                }
                /**
                 * 关于错误码可以参考官方api详细说明
                 * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        Log.d("lzan13", "登录失败 Error code:" + code + ", message:" + error);
                       
                        switch (code) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                ToastUtil.showToast(MeLoginActivity.this, "网络错误 code: " + code + ", message:" + error);
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                ToastUtil.showToast(MeLoginActivity.this, "无效的用户名 code: " + code + ", message:" + error);
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                ToastUtil.showToast(MeLoginActivity.this, "无效的密码 code: " + code + ", message:" + error);
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                ToastUtil.showToast(MeLoginActivity.this, "用户认证失败，用户名或密码错误 code: " + code + ", message:" + error);
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                ToastUtil.showToast(MeLoginActivity.this, "用户不存在 code: " + code + ", message:" + error);
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                ToastUtil.showToast(MeLoginActivity.this, "无法访问到服务器 code: " + code + ", message:" + error);
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                ToastUtil.showToast(MeLoginActivity.this, "等待服务器响应超时 code: " + code + ", message:" + error);
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                ToastUtil.showToast(MeLoginActivity.this, "服务器繁忙 code: " + code + ", message:" + error);
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                ToastUtil.showToast(MeLoginActivity.this, "未知的服务器异常 code: " + code + ", message:" + error);
                                break;
                            default:
                                ToastUtil.showToast(MeLoginActivity.this, "ml_sign_in_failed code: " + code + ", message:" + error);
                                break;
                        }
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
                LogUtils.e(TAG+"login: onProgress");
            }
        });
    }


}
