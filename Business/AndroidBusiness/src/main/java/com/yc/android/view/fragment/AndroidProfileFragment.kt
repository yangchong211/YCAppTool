package com.yc.android.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.yc.configlayer.constant.Constant
import com.yc.library.base.mvp.BaseLazyFragment
import com.yc.library.web.WebViewActivity
import com.yc.toastutils.ToastUtils
import com.yc.toolutils.AppLogUtils
import com.yc.toolutils.AppSpUtils
import com.yc.android.R
import com.yc.android.tools.base.KotlinConstant
import com.yc.android.tools.AndroidUtils
import com.yc.android.tools.KotlinUtils
import com.yc.android.view.activity.AndroidActivity
import com.yc.android.view.activity.AndroidCollectActivity
import com.yc.android.view.activity.AndroidLoginActivity
import kotlinx.android.synthetic.main.fragment_android_profile.*
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * <pre>
 *     @author 杨充
 *     blog  :
 *     time  : 2018/01/30
 *     desc  : kotlin学习：
 *     revise: 我的页面
 * </pre>
 */
class AndroidProfileFragment : BaseLazyFragment(), View.OnClickListener {

    private var activity: AndroidActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AndroidActivity
    }

    override fun onDetach() {
        super.onDetach()
        activity = null
    }

    override fun onResume() {
        super.onResume()
        checkLoginStates()
    }

    override fun getContentView(): Int {
        return R.layout.fragment_android_profile
    }

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        tvVersionName.text = "V" + KotlinUtils.getVersionCode(activity!!)
        val versionCode = AndroidUtils.getVersionCode(activity!!)
        AppLogUtils.e("Android$versionCode")
    }

    override fun initListener() {
        llProfile.setOnClickListener(this)
        rlTheme.setOnClickListener(this)
        rlCollect.setOnClickListener(this)
        rlAbout.setOnClickListener(this)
        rlMore.setOnClickListener(this)
        rlSetting.setOnClickListener(this)
        rlOther.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun onLazyLoad() {

    }


    private fun checkLoginStates() {
        if (AppSpUtils.getInstance().getInt(KotlinConstant.USER_ID)==0){
            tvName.text = "未登录，点击去登陆"
            tvOther.text = "未登录，点击去登陆"
        }else{
            tvName.text = AppSpUtils.getInstance().getString(KotlinConstant.USER_NAME)
            tvOther.text = "登陆状态：已登录，点击即可退出登陆"
        }
    }


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.llProfile ->{
                if (AppSpUtils.getInstance().getInt(KotlinConstant.USER_ID)==0){
//                    ActivityUtils.startActivity(AndroidLoginActivity::class.java)
                }else{
                    ToastUtils.showRoundRectToast("已经登陆啦")
                }
            }
            R.id.rlTheme ->{

            }
            R.id.rlCollect ->{
                if (AppSpUtils.getInstance().getInt(KotlinConstant.USER_ID)==0){
//                    ActivityUtils.startActivity(AndroidLoginActivity::class.java)
                }else{
                    AndroidCollectActivity.lunch(activity)
                }
            }
            R.id.rlAbout ->{

            }
            R.id.rlMore ->{
                WebViewActivity.lunch(activity, Constant.GITHUB, "我的GitHub")
            }
            R.id.rlOther ->{
                if (AppSpUtils.getInstance().getInt(KotlinConstant.USER_ID)==0){
                    //ToastUtils.showRoundRectToast("还未登陆")
                    AndroidLoginActivity.lunch(activity)
                }else{
                    //开始退出登陆
                    //设置启动模式
                    val job = GlobalScope.launch(start = CoroutineStart.LAZY) {
                        delay(500L)
                        AndroidActivity.startActivity(activity,KotlinConstant.FIND)
                        AppLogUtils.i("AndroidActivity"+"设置启动模式")
                    }
                    AppLogUtils.i("AndroidActivity"+"hello world")
                    AppSpUtils.getInstance().put(KotlinConstant.USER_ID,0)
                    AppSpUtils.getInstance().put(KotlinConstant.USER_NAME, "")
                    AppSpUtils.getInstance().put(KotlinConstant.USER_EMAIL, "")
                    ToastUtils.showRoundRectToast("退出登陆")
                    job.start()
                }
            }
        }
    }

}