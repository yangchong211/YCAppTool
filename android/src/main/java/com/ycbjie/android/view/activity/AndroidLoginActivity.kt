package com.ycbjie.android.view.activity


import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.pedaily.yc.ycdialoglib.loading.ViewLoading
import com.pedaily.yc.ycdialoglib.toast.ToastUtils
import com.ycbjie.android.R
import com.ycbjie.android.base.KotlinConstant
import com.ycbjie.android.contract.AndroidLoginContract
import com.ycbjie.android.model.bean.LoginBean
import com.ycbjie.android.presenter.AndroidLoginPresenter
import com.ycbjie.library.base.mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_android_login.*
import kotlinx.android.synthetic.main.base_android_bar.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * <pre>
 *     @author 杨充
 *     blog  :
 *     time  : 2017/05/30
 *     desc  : 登陆页面
 *     revise:
 * </pre>
 */
class AndroidLoginActivity : BaseActivity<AndroidLoginPresenter>(),
        View.OnClickListener , AndroidLoginContract.View{

    /**
     * kotlin可以直接通过id找到控件，之前的findviewbyid就可以不用写了
     * 要先在module的build.gradle中配置：apply plugin: 'kotlin-android-extensions'
     * AndroidStudio会自动引用：import kotlinx.android.synthetic.main.activity_android_login.*
     *
     * 自定义控件，或者需要前缀view.findViewById的就需要findViewById呢……
     */
    private var presenter : AndroidLoginPresenter ?= null

    companion object {
        fun lunch(content: Activity?){
            content?.startActivity(Intent(content,AndroidLoginActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.unSubscribe()
    }

    override fun getContentView(): Int {
        return R.layout.activity_android_login
    }

    override fun initView() {
        presenter = AndroidLoginPresenter(this)
        tv_title_left.text = "开始登陆"
        toolbar_title.text = "登陆页面"
        checkEditTextInput()
    }

    override fun initListener() {
        btn_login.setOnClickListener(this)
        ll_title_menu.setOnClickListener(this)
        tv_register.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll_title_menu -> {
                finish()
            }
            R.id.tv_register -> {
                AndroidRegisterActivity.lunch(this@AndroidLoginActivity)
            }
            R.id.btn_login -> {
                if (checkContent(true)) {
                    ViewLoading.show(this@AndroidLoginActivity)
                    val name = et_username.text.toString().trim()
                    val pwd = et_password.text.toString().trim()
                    presenter?.startLogin(name,pwd)
                }
            }
        }
    }

    /**
     * 检测输入密码不能少于6位
     */
    private fun checkEditTextInput() {
        et_password.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString()
                if (text.length>6){
                    et_password.error = null
                }else{
                    et_password.error = "至少输入六位"
                }
            }
        })
    }

    private fun checkContent(login: Boolean): Boolean {
        if (login)
        et_username.error = null
        et_password.error = null

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

        if (cancel) {
            focusView?.requestFocus()
        } else {
            return true
        }
        return false
    }

    override fun loginSuccess(bean: LoginBean) {
        //协程间是如何切换的，注意这里只是测试协程
        //这段代码先打印出next，然后延迟1秒钟后打印出now，像是android里handler的post和postDelay方法。
        GlobalScope.launch(Dispatchers.Default){
            ViewLoading.dismiss(this@AndroidLoginActivity)
            LogUtils.i("AndroidLoginActivity"+"协程间是如何切换的")
            LogUtils.i("AndroidLoginActivity---${Thread.currentThread().name}")
            launch {
                delay(1000)
                LogUtils.i("AndroidLoginActivity"+"now")
                finish()
                AndroidActivity.startActivity(this@AndroidLoginActivity,KotlinConstant.HOME)
            }
            SPUtils.getInstance().put(KotlinConstant.USER_ID,bean.id)
            SPUtils.getInstance().put(KotlinConstant.USER_NAME, bean.username!!)
            SPUtils.getInstance().put(KotlinConstant.USER_EMAIL, bean.email!!)
            ToastUtils.showRoundRectToast("登陆成功")
            LogUtils.i("AndroidLoginActivity"+"next")
        }
    }


    override fun loginError(message: String?) {
        ViewLoading.dismiss(this@AndroidLoginActivity)
        ToastUtils.showRoundRectToast("登陆失败，请稍后再试")
    }


}