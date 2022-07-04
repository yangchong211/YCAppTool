package com.yc.android.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yc.android.R
import com.yc.android.presenter.AndroidPresenter
import com.yc.android.tools.base.KotlinConstant.HOME
import com.yc.android.view.fragment.AndroidHomeFragment
import com.yc.android.view.fragment.AndroidKnowledgeFragment
import com.yc.android.view.fragment.AndroidProfileFragment
import com.yc.android.view.fragment.AndroidProjectFragment
import com.yc.baseclasslib.viewpager2.DiffFragmentStateAdapter
import com.yc.baseclasslib.viewpager2.OnPageChangeCallback
import com.yc.intent.log.IntentLogger
import com.yc.library.base.config.Constant
import com.yc.library.base.mvp.BaseActivity
import com.yc.library.web.WebViewActivity
import com.yc.statusbar.bar.StateAppBar
import com.yc.toastutils.ToastUtils
import com.yc.toolutils.AppLogUtils
import kotlinx.android.synthetic.main.base_android_bar.*

/**
 * <pre>
 *     @author 杨充
 *     blog  :
 *     time  : 2018/01/30
 *     desc  : kotlin学习：
 *     revise:
 * </pre>
 */
class AndroidActivity : BaseActivity<AndroidPresenter>(){

    private var mTvTitleLeft :TextView?=null
    private var mLlTitleMenu :FrameLayout?=null
    private var mToolbarTitle :TextView?=null
    private var mIvRightImg :ImageView?=null
    private var viewPager: ViewPager2? = null
    private var tabLayout : TabLayout? =null
    /**
     * 创建集合
     *
     */
    private var fragments = mutableListOf<Fragment>()
    private var index: Int = 0      //定义具体的类型
    private var selectIndex: Int = 0
    private var count = 0

    /**
     * 跳转首页
     */
    companion object {
        //?的作用是方法参数声明非空
        //为了增强逻辑，可以在方法参数上加上"?"，可以避免处理参数值时抛出空指针异常。
        fun startActivity(context: Activity?, selectIndex: Int) {
            val intent = Intent(context, AndroidActivity::class.java)
            //intent.addCategory(Intent.CATEGORY_DEFAULT);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.action = Intent.ACTION_VIEW
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("selectIndex", selectIndex)
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IntentLogger.print("intent test : ", intent)
    }

    /**
     * 处理onNewIntent()，以通知碎片管理器 状态未保存。
     * 如果您正在处理新的意图，并且可能是 对碎片状态进行更改时，要确保调用先到这里。
     * 否则，如果你的状态保存，但活动未停止，则可以获得 onNewIntent()调用，发生在onResume()之前，
     * 并试图 此时执行片段操作将引发IllegalStateException。 因为碎片管理器认为状态仍然保存。
     *
     * @param intent intent
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            selectIndex = intent.getIntExtra("selectIndex", HOME)
            //Kotlin 支持在字符串字面值中引用局部变量，只需要在变量名前加上字符$即可
            AppLogUtils.e("索引-------$selectIndex")
            viewPager?.currentItem = selectIndex
            IntentLogger.print("intent test : ", intent)
            IntentLogger.printComponentName("intent component : " , intent)
            IntentLogger.printExtras("intent test : ", intent)
            IntentLogger.printFlags("intent test : ", intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.kotlin_menu_main,menu)
        menu?.add(0, 1, 0, "登录玩Android")
        menu?.add(0, 2, 1, "开发作者介绍")
        menu?.add(0, 3, 2, "分享此软件")
        menu?.add(0, 4, 3, "开源项目介绍")
        menu?.add(0, 5, 4, "简单使用协程")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.actionSearch -> {
                AndroidSearchActivity.lunch(this)
            }
            R.id.actionUrlNav -> {
                NavWebsiteActivity.lunch(this)
            }
            1 -> {
            }
            2 -> {
                ToastUtils.showRoundRectToast("开发作者介绍")
                WebViewActivity.lunch(this, Constant.GITHUB, "我的GitHub")
            }
            3 -> {
                //ToastUtils.showRoundRectToast("分享此软件")
                WebViewActivity.lunch(this, Constant.ZHI_HU, "我的知乎")
            }
            4 -> {
                //ToastUtils.showRoundRectToast("开源项目介绍")
            }
            5 -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getContentView(): Int {
        return R.layout.activity_wan_android
    }

    override fun initView() {
        StateAppBar.setStatusBarColor(this, resources.getColor(R.color.colorTheme))
        initFindViewById()
        initToolBar()
        initFragment()
        initTabLayout()
    }


    /**
     * kotlin可以直接通过id找到控件，之前的findviewbyid就可以不用写了
     * 要先在module的build.gradle中配置：apply plugin: 'kotlin-android-extensions'
     * AndroidStudio会自动引用：import kotlinx.android.synthetic.main.activity_android_login.*
     *
     * 自定义控件，或者需要前缀view.findViewById的就需要findViewById呢……
     */
    private fun initFindViewById() {
        mTvTitleLeft = findViewById(R.id.tv_title_left)
        mLlTitleMenu = findViewById(R.id.ll_title_menu)
        mToolbarTitle = findViewById(R.id.toolbar_title)
        mIvRightImg = findViewById(R.id.iv_right_img)
        viewPager = findViewById(R.id.vp_pager)
        tabLayout = findViewById(R.id.ctl_table)
        mTvTitleLeft?.visibility = View.VISIBLE
        mTvTitleLeft?.textSize = 16.0f
        mTvTitleLeft?.typeface = Typeface.DEFAULT
        mLlTitleMenu?.visibility = View.GONE
        mIvRightImg?.visibility = View.VISIBLE
    }


    override fun initListener() {

    }


    override fun initData() {

    }

    private fun initToolBar() {
        mTvTitleLeft?.text = "首页"
        ll_title_menu.visibility = View.GONE
        toolbar_title.visibility = View.GONE
        toolbar.run {
            setSupportActionBar(toolbar)
            supportActionBar?.title = ""
            setTitleTextColor(Color.WHITE)
        }
    }

    /**
     * Kotlin不需要使用new关键字，直接写：类()
     */
    private fun initFragment() {
        fragments.add(AndroidHomeFragment())
        fragments.add(AndroidKnowledgeFragment())
        fragments.add(AndroidProjectFragment())
        fragments.add(AndroidProfileFragment())
    }

    private fun initTabLayout() {
        val mIconUnSelectIds = this.resources.obtainTypedArray(R.array.android_tab_un_select)
        val mIconSelectIds = this.resources.obtainTypedArray(R.array.android_tab_select)
        val mainTitles = this.resources.getStringArray(R.array.android_title)
        val listTitle = mainTitles.toMutableList()
        mIconUnSelectIds.recycle()
        mIconSelectIds.recycle()
        val adapter = DiffFragmentStateAdapter(this,fragments)
        viewPager?.adapter = adapter
        //adapter.addFragmentList(fragments, listTitle)
        //tabLayout?.setupWithViewPager(viewPager)
        if (tabLayout!=null && viewPager!=null){
            TabLayoutMediator(tabLayout!!, viewPager!!) { tab, position ->
                tab.text = listTitle[position]
            }.attach()
        }
        //值表示应该使用RecyclerView的默认缓存机制，而不是显式地预取并保留页面到当前页面的任意一侧。
        //viewPager?.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        viewPager?.registerOnPageChangeCallback(onPageChangeCallback)
        //是否允许用户滑动切换
        viewPager?.isUserInputEnabled = false
        tabLayout?.tabMode = TabLayout.MODE_FIXED
        viewPager?.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager?.offscreenPageLimit = mainTitles.size
        viewPager?.offscreenPageLimit = fragments.size
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager?.unregisterOnPageChangeCallback(onPageChangeCallback)
    }

    private val onPageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }
    }

}
