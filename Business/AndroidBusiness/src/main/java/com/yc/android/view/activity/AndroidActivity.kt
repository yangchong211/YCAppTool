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
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.yc.baseclasslib.adapter.BaseFragmentPagerAdapter
import com.yc.configlayer.constant.Constant
import com.yc.intent.log.IntentLogger
import com.yc.library.base.mvp.BaseActivity
import com.yc.library.web.WebViewActivity
import com.yc.statusbar.bar.StateAppBar
import com.yc.toastutils.ToastUtils
import com.yc.toolutils.AppLogUtils
import com.ycbjie.android.R
import com.ycbjie.android.presenter.AndroidPresenter
import com.ycbjie.android.tools.KotlinTest
import com.ycbjie.android.tools.base.KotlinConstant.HOME
import com.ycbjie.android.view.fragment.AndroidHomeFragment
import com.ycbjie.android.view.fragment.AndroidKnowledgeFragment
import com.ycbjie.android.view.fragment.AndroidProfileFragment
import com.ycbjie.android.view.fragment.AndroidProjectFragment
import kotlinx.android.synthetic.main.base_android_bar.*
import kotlinx.android.synthetic.main.base_android_bar.toolbar
import kotlinx.coroutines.*

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

    /**
     * var关键字声明可变属性
     * val关键字声明只读属性
     * 属性的类型在后面，变量名在前面，中间加冒号和空格
     */
    private var presenter : AndroidPresenter? = null

    /**
     * 定义局部变量和常量
     * 0. Kotlin声明变量与Java声明变量有些不一样，Java变量类型在前，变量名在后，
     *   而Kotlin则相反，变量名在前，变量类型在后，中间加:(冒号)
     *   并且Kotlin可以自动判断变量的类型。
     *
     * 1.常量
     *      常量使用val关键字，val代表只读
     *
     * 2.变量
     *      变量使用var关键字，val代表可变
     *
     * val是线程安全的，并且必须在定义时初始化，所以不需要担心 null 的问题
     * 强烈推荐能用val的地方就用val
     */
    private var mTvTitleLeft :TextView?=null
    private var mLlTitleMenu :FrameLayout?=null
    private var mToolbarTitle :TextView?=null
    private var mIvRightImg :ImageView?=null
    private var viewPager: ViewPager? = null
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
                val a = count%7
                when(a){
                    0 -> {
                        coroutineScope()
                    }
                    1 -> {
                        startRun1()
                    }
                    2 -> {
                        startRun2()
                    }
                    3 -> {
                        startRun3()
                    }
                    4 -> {
                        //startRun4()
                    }
                    5 -> {

                    }
                    6 -> {

                    }
                }
                count++
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 定义函数
     * 1.与Java定义函数的区别在于：Kotlin在定义函数的时候要加个fun关键词，函数的返回值前后不同，Java的在前面，kotlin在后面
     * 2.如果一个函数只有一个并且是表达式函数体并且是返回类型自动推断的话，可以直接这样写
     *   fun getResult(a: Int, b: Int) = a + b
     * 3.如果函数返回一个无意义的值，相当于Java的void，则可以这样写
     *   fun initView(){}
     */
    override fun getContentView(): Int {
        return R.layout.activity_wan_android
    }


    /**
     * 1. ?: Elvis 操作符
     *       val l = b?.length ?: -1
     *       如果 ?: 左侧表达式非空，elvis 操作符就返回其左侧表达式，否则返回右侧表达式。
     *       注意:当且仅当左侧为空时，才会对右侧表达式求值。
     *
     * 2. !! 操作符
     *      这是为空指针爱好者准备的，非空断言运算符（!!）将任何值转换为非空类型，若该值为空则抛出异常
     *      能不用!!操作符就不要用。。。
     *
     * 3. ?. 操作符
     *      使用 ?. 操作符，就先判空，如果不为空则赋值
     *
     * 4. ?= 操作符
     *      使用 ?= 操作符，当前面的值不为空取前面的值，否则取后面的值，这和java中三目运算符类似
     */
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
        val adapter = BaseFragmentPagerAdapter(supportFragmentManager)
        adapter.addFragmentList(fragments, listTitle)
        tabLayout?.setupWithViewPager(viewPager)
        //?.tabMode = TabLayout.MODE_SCROLLABLE
        viewPager?.offscreenPageLimit = mainTitles.size
        viewPager?.adapter = adapter
        viewPager?.offscreenPageLimit = fragments.size
    }

    /**
     * 关键字
     * object           为同时声明一个类及其实例
     * typealias        类型别名为现有类型提供替代名称
     * as               是一个中缀操作符，as是不安全的转换操作符，如果as转换失败，会抛出一个异常，这就是不安全的。
     * as?              as?与as类似，也是转换操作符，但是与as不同的是，as?是安全的，也就是可空的，可以避免抛出异常，在转换失败是会返回null
     * fun              表示声明一个函数
     * in               用于指定for循环中迭代的对象
     * !in              表示与in相反，用作中缀操作符以检查一个值不属于一个区间、一个集合或者其他定义contains方法的实体。
     * is和!is          是否符合给定类型，类似与Java的instanceOf，is操作符或其否定形式!is来检查对象是否符合给定类型
     * constructor      声明一个主构造函数或次构造函数
     * init             主构造函数不能包含任何的代码。初始化的代码可以放到以init关键字作为前缀的初始化块中：
     * where            用于指定泛型多个类型的上界约束
     *
     */



    private fun startRun1(){
        //这段代码就是启动一个协程，并启动，延迟1秒后打印world，就从这个launch方法进行切入
        /*GlobalScope.launch {
            //延迟（挂起）1000毫秒，注意这不会阻塞线程
            delay(1000L)
            AppLogUtils.i("AndroidActivity"+"启动一个协程")
        }*/

        //设置启动模式
        val job = GlobalScope.launch(start = CoroutineStart.LAZY) {
            delay(1000L)
            AppLogUtils.i("AndroidActivity"+"设置启动模式")
        }
        AppLogUtils.i("AndroidActivity"+"hello world")
        job.start()
    }

    private fun startRun2(){
        //协程间是如何切换的
        //这段代码先打印出next，然后延迟1秒钟后打印出now，像是android里handler的post和postDelay方法。
        GlobalScope.launch(Dispatchers.Default){
            AppLogUtils.i("AndroidActivity"+"协程间是如何切换的")
            AppLogUtils.i("AndroidActivity---${Thread.currentThread().name}")
            launch {
                delay(1000)
                AppLogUtils.i("AndroidActivity"+"now")
            }
            AppLogUtils.i("AndroidActivity"+"next")
        }
    }

    private fun startRun3(){
        //取消协程
        val job = GlobalScope.launch {
            delay(1000L)
            AppLogUtils.i("AndroidActivity"+"World!")
        }
        job.cancel()
        AppLogUtils.i("AndroidActivity"+"Hello,")
    }


    private suspend fun startRun4(){
        val job = GlobalScope.launch {
            delay(1000L)
            AppLogUtils.i("AndroidActivity"+"World!")
            delay(1000L)
        }
        AppLogUtils.i("AndroidActivity"+"Hello,")
        job.join()
        AppLogUtils.i("AndroidActivity"+"Good！")
        //依次打印
        //Hello,
        //World!
        //Good！
    }


    private fun coroutineScope() {
        val uiScope = CoroutineScope(Dispatchers.Main)
        val job = uiScope.launch {
            AppLogUtils.i("AndroidActivity" + "模拟发送请求")
            val deffer = async(Dispatchers.Default) {
                getResult()
            }
            val result = deffer.await()
            AppLogUtils.i("AndroidActivity---获取 $result")
        }
        //调用该方法取消这个协程的进行
        //job.cancel()
        //先执行模拟发送请求，隔2秒后，执行模拟请求时间，再然后执行获取
    }

    private suspend fun getResult(): String {
        delay(2000L)
        AppLogUtils.i("AndroidActivity"+"模拟请求时间")
        return "result"
    }


    fun test(){
        val test = KotlinTest()
        test.test()
    }

}
