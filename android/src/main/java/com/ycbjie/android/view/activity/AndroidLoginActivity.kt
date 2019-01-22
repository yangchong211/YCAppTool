package com.ycbjie.android.view.activity


import android.text.TextUtils
import android.view.View
import com.ycbjie.android.presenter.AndroidLoginPresenter
import com.blankj.utilcode.util.RegexUtils
import com.ycbjie.android.R
import com.ycbjie.library.base.mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_android_login.*
import kotlinx.android.synthetic.main.base_android_bar.*


/**
 * <pre>
 *     @author 杨充
 *     blog  :
 *     time  : 2017/05/30
 *     desc  : 登陆页面
 *     revise:
 * </pre>
 */
class AndroidLoginActivity : BaseActivity<AndroidLoginPresenter>(), View.OnClickListener {

    /**
     * kotlin可以直接通过id找到控件，之前的findviewbyid就可以不用写了
     * 要先在module的build.gradle中配置：apply plugin: 'kotlin-android-extensions'
     * AndroidStudio会自动引用：import kotlinx.android.synthetic.main.activity_android_login.*
     *
     * 自定义控件，或者需要前缀view.findViewById的就需要findViewById呢……
     */

    override fun getContentView(): Int {
        return R.layout.activity_android_login
    }

    override fun initView() {
        tv_title_left.text = "登陆注册"
    }

    override fun initListener() {
        btn_register.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        ll_title_menu.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_title_menu -> {
                finish()
            }
            R.id.btn_register -> {
                if (checkContent(true)) {

                }
            }
            R.id.btn_login -> {
                if (checkContent(true)) {

                }
            }
        }
    }

    private fun checkContent(login: Boolean): Boolean {
        et_username.error = null
        et_password.error = null
        et_email.error = null

        var cancel = false
        var focusView: View? = null


        if (TextUtils.isEmpty(et_username.text.toString().trim())) {
            et_username.error = "用户名不能为空"
            focusView = et_username
            cancel = true
        }

        if (TextUtils.isEmpty(et_password.text.toString().trim())) {
            et_password.error = "密码不能为空"
            focusView = et_password
            cancel = true
        } else if (et_password.text.length < 6) {
            et_password.error = "密码长度不能小于6位"
            focusView = et_password
            cancel = true
        }

        if (!login) {
            if (TextUtils.isEmpty(et_email.text.toString().trim())) {
                et_email.error = "Email不能为空"
                focusView = et_email
                cancel = true
            } else if (!RegexUtils.isEmail(et_email.text.toString().trim())) {
                et_email.error = "Email格式不正确"
                focusView = et_email
                cancel = true
            }
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            return true
        }
        return false
    }

}