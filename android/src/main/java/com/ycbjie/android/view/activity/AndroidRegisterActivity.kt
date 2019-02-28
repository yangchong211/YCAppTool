package com.ycbjie.android.view.activity


import android.app.Activity
import android.content.Intent
import android.view.View
import com.ycbjie.android.R
import com.ycbjie.android.presenter.AndroidLoginPresenter
import com.ycbjie.library.base.mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_android_register.*
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
class AndroidRegisterActivity : BaseActivity<AndroidLoginPresenter>(), View.OnClickListener {

    /**
     * kotlin可以直接通过id找到控件，之前的findviewbyid就可以不用写了
     * 要先在module的build.gradle中配置：apply plugin: 'kotlin-android-extensions'
     * AndroidStudio会自动引用：import kotlinx.android.synthetic.main.activity_android_login.*
     *
     * 自定义控件，或者需要前缀view.findViewById的就需要findViewById呢……
     */

    companion object {
        fun lunch(content: Activity?){
            content?.startActivity(Intent(content,AndroidRegisterActivity::class.java))
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_android_register
    }

    override fun initView() {
        toolbar_title.text = "注册页面"
        btn_register.text = "开始注册"
    }

    override fun initListener() {

    }

    override fun initData() {

    }

    override fun onClick(v: View?) {

    }

}