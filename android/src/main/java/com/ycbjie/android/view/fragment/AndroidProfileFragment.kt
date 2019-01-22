package com.ycbjie.android.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.ycbjie.android.util.AndroidUtils
import com.ycbjie.android.util.KotlinUtils
import com.ycbjie.android.view.activity.AndroidAboutActivity
import com.ycbjie.android.view.activity.AndroidActivity
import com.ycbjie.android.view.activity.AndroidLoginActivity
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.pedaily.yc.ycdialoglib.toast.ToastUtils
import com.ycbjie.android.R
import com.ycbjie.android.base.KotlinConstant
import com.ycbjie.android.view.activity.AndroidCollectActivity
import com.ycbjie.library.base.mvp.BaseLazyFragment
import com.ycbjie.library.constant.Constant
import com.ycbjie.library.web.view.WebViewActivity
import kotlinx.android.synthetic.main.fragment_android_profile.*

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


    override fun getContentView(): Int {
        return R.layout.fragment_android_profile
    }

    @SuppressLint("SetTextI18n")
    override fun initView(view: View) {
        tvVersionName.text = "V" + KotlinUtils.getVersionCode(activity!!)
        val versionCode = AndroidUtils.getVersionCode(activity!!)
        LogUtils.e("Android$versionCode")
        if (SPUtils.getInstance().getInt(KotlinConstant.USER_ID)==0){
            tvName.text = "未登录"
        }else{
            tvName.text = SPUtils.getInstance().getString(KotlinConstant.USER_NAME)
        }
    }

    override fun initListener() {
        llProfile.setOnClickListener(this)
        rlTheme.setOnClickListener(this)
        rlCollect.setOnClickListener(this)
        rlAbout.setOnClickListener(this)
        rlMore.setOnClickListener(this)
        rlSetting.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun onLazyLoad() {

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.llProfile ->{
                if (SPUtils.getInstance().getInt(KotlinConstant.USER_ID)==0){
                    ActivityUtils.startActivity(AndroidLoginActivity::class.java)
                }else{
                    ToastUtils.showRoundRectToast("已经登陆啦")
                }
            }
            R.id.rlTheme ->{

            }
            R.id.rlCollect ->{
                if (SPUtils.getInstance().getInt(KotlinConstant.USER_ID)==0){
                    ActivityUtils.startActivity(AndroidLoginActivity::class.java)
                }else{
                    AndroidCollectActivity.lunch(activity)
                }
            }
            R.id.rlAbout ->{
                AndroidAboutActivity.lunch(activity)
            }
            R.id.rlMore ->{
                WebViewActivity.lunch(activity, Constant.GITHUB, "我的GitHub")
            }
        }
    }

}