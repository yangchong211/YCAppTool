package com.ycbjie.android.view.activity

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.view.View
import cn.ycbjie.ycstatusbarlib.bar.StateAppBar
import com.blankj.utilcode.util.LogUtils
import com.ycbjie.android.R
import com.ycbjie.android.presenter.AndroidPresenter
import com.ycbjie.library.base.mvp.BaseActivity
import com.ycbjie.library.web.view.WebViewActivity
import kotlinx.android.synthetic.main.activity_android_about.*
import kotlinx.android.synthetic.main.base_android_bar.*

/**
 * <pre>
 *     @author 杨充
 *     blog  :
 *     time  : 2017/05/30
 *     desc  : 关于页面
 *     revise:
 * </pre>
 */
class AndroidAboutActivity : BaseActivity<AndroidPresenter>() {


    object DataProviderManager {
        //只是用作学习使用
        init {
            //对应java中static代码块
            LogUtils.e("DataProviderManager"+"init")
        }
        fun registerDataProvider(provider: String) {
            // ...
            LogUtils.e("DataProviderManager$provider")
        }
    }


    private val HOME_URL = "http://www.wanandroid.com/index"
    private val OPEN_API = "http://www.wanandroid.com/blog/show/2"
    private val PROJECT_URL = "https://github.com/yangchong211"
    private val DEVELOPER_BLOG = "https://blog.csdn.net/m0_37700275/article/details/80863685"
    private val DEVELOPER_GITHUB = "https://github.com/yangchong211"

    companion object {
        fun lunch(context: Activity?) {
            context?.startActivity(Intent(context, AndroidAboutActivity::class.java))
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_android_about
    }

    override fun initView() {
        DataProviderManager.registerDataProvider("单利")
        initToolBar()
        StateAppBar.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorTheme))
    }

    private fun initToolBar() {
        toolbar_title.visibility = View.VISIBLE
        toolbar_title.text = "关于我"
        //使用Lambda表达式
        ll_title_menu.setOnClickListener {
            onBackPressed()
        }
        //直接使用
        /*ll_title_menu.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                onBackPressed()
            }
        })*/
    }

    override fun initListener() {
        tvHome.setOnClickListener {
            WebViewActivity.lunch(this@AndroidAboutActivity, HOME_URL, "WanAndroid主页")
        }
        tvOpenApi.setOnClickListener {
            WebViewActivity.lunch(this@AndroidAboutActivity, OPEN_API, "WanAndroid开放API")
        }
        tvSrc.setOnClickListener {
            WebViewActivity.lunch(this@AndroidAboutActivity, PROJECT_URL, "本软件源码地址")
        }
        tvBlog.setOnClickListener {
            WebViewActivity.lunch(this@AndroidAboutActivity, DEVELOPER_BLOG, "开发者简书地址")
        }
        tvGitHub.setOnClickListener {
            WebViewActivity.lunch(this@AndroidAboutActivity, DEVELOPER_GITHUB, "开发者GitHub主页")
        }
    }

    override fun initData() {

    }

}