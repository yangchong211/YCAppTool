package com.yc.appmonitor.apm

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.yc.appmonitor.R
import com.yc.monitorthread.ThreadBlockMessage
import com.yc.monitorthread.ThreadBlockWatchDog
import com.yc.monitorthread.ThreadBlockedListener
import com.yc.toastutils.ToastUtils
import com.yc.toolutils.system.AppLogUtils
import com.yc.toolutils.stack.StackTraceUtils
import okhttp3.*
import java.io.IOException
import java.util.*


/**
 * @author: 杨充
 * @email  : yangchong211@163.com
 * @time   : 2022/04/28
 * @desc   : apm小工具，模拟一些操作，然后方便测试apm统计数据
 * @revise :
 */
class ApmTestActivity : FragmentActivity() {

    private var mFragment: ListFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apm_test)
        initFragment()
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
//        val refWatcher: RefWatcher = MonitorApplication.getRefWatcher(this)
//        refWatcher.watch(this)
    }

    private fun initFragment() {
        mFragment = ListFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.rl_home, mFragment!!).commit()
    }

    private fun initData() {
        val lvItemList: MutableList<ListFragment.LvItem> = ArrayList()
        lvItemList.add(ListFragment.LvItem("模拟日志", object : ListFragment.OnClick {
            override fun click(view: View?) {
                ToastUtils.showRoundRectToast("模拟日志")
                for (i in 0..9999) {
                    when {
                        i % 5 == 1 -> {
                            AppLogUtils.v("AppLogUtils", "AppLogUtils:$i")
                        }
                        i % 5 == 2 -> {
                            AppLogUtils.d("AppLogUtils", "dlog:$i")
                        }
                        i % 5 == 3 -> {
                            AppLogUtils.i("AppLogUtils", "ilog:$i")
                        }
                        i % 5 == 4 -> {
                            AppLogUtils.w("AppLogUtils", "wlog:$i")
                        }
                        else -> {
                            AppLogUtils.e("AppLogUtils", "elog:$i")
                        }
                    }
                }
            }
        }))
        lvItemList.add(ListFragment.LvItem("崩溃模拟", object : ListFragment.OnClick {
            override fun click(view: View?) {
                ToastUtils.showRoundRectToast("崩溃模拟")
                // 执行就崩, 如果应用启动后8秒内崩溃, 则判定为启动崩溃进行上报
                throw RuntimeException("Monitor Exception")
            }
        }))
        lvItemList.add(ListFragment.LvItem("ANR模拟", object : ListFragment.OnClick {
            override fun click(view: View?) {
                ToastUtils.showRoundRectToast("ANR模拟")
                // 任意处主线程执行, 执行后手动在屏幕上频繁滑动数下, 等几秒就会有ANR弹窗、数据上报
                SystemClock.sleep(20000)
                StackTraceUtils.print3(Thread.currentThread())
            }
        }))
        lvItemList.add(ListFragment.LvItem("自定义错误模拟", object : ListFragment.OnClick {
            override fun click(view: View?) {
                try {
                    val activity: Activity? = null
                    activity!!.isDestroyed
                } catch (e: Exception) {
                    ToastUtils.showRoundRectToast("自定义错误模拟")
                    StackTraceUtils.print1(e)
                    StackTraceUtils.print2(e)
                }
            }
        }))
        lvItemList.add(ListFragment.LvItem("卡顿模拟", object : ListFragment.OnClick {
            override fun click(view: View?) {
                ToastUtils.showRoundRectToast("卡顿模拟")
                try {
                    Thread.sleep(2600)
                    testSeriousBlock()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                StackTraceUtils.print5()
            }
        }))
        lvItemList.add(ListFragment.LvItem("卡顿页面", object : ListFragment.OnClick {
            override fun click(view: View?) {
                ToastUtils.showRoundRectToast("卡顿页面")
                startActivity(
                    Intent(
                        this@ApmTestActivity,
                        BlockListActivity::class.java
                    )
                )
            }
        }))
        lvItemList.add(ListFragment.LvItem("hybrid网页监控", object : ListFragment.OnClick {
            override fun click(view: View?) {
                ToastUtils.showRoundRectToast("hybrid网页监控")
            }
        }))
        //网络监控支持OkHttp
        lvItemList.add(ListFragment.LvItem("网络监控模拟", object : ListFragment.OnClick {
            override fun click(view: View?) {
                ToastUtils.showRoundRectToast("网络监控模拟")
                try {
                    executeGetAsnWithLog("http://mock-api.com/Rz3yJMnM.mock/get")
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                StackTraceUtils.print4(Thread.currentThread())
            }
        }))
        lvItemList.add(ListFragment.LvItem("网络错误监控模拟", object : ListFragment.OnClick {
            override fun click(view: View?) {
                ToastUtils.showRoundRectToast("网络错误监控模拟")
                try {
                    executeGetAsnWithLog("http://aa.bb.com/get")
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                StackTraceUtils.print5()
            }
        }))
        lvItemList.add(ListFragment.LvItem("内存OOM模拟", object : ListFragment.OnClick {
            override fun click(view: View?) {
                ToastUtils.showRoundRectToast("内存OOM模拟")
                OOMMaker.createOOM()
                StackTraceUtils.print5()
            }
        }))
        lvItemList.add(ListFragment.LvItem("事件上报模拟", object : ListFragment.OnClick {
            override fun click(view: View?) {
                ToastUtils.showRoundRectToast("事件上报模拟")
            }
        }))
        lvItemList.add(ListFragment.LvItem("模拟内存泄漏", object : ListFragment.OnClick {
            override fun click(view: View?) {
                testLeak()
            }
        }))
        lvItemList.add(ListFragment.LvItem("模拟线程执行耗时操作", object : ListFragment.OnClick {
            override fun click(view: View?) {
                ToastUtils.showRoundRectToast("模拟线程执行耗时操作")
                testThread()
            }
        }))
        mFragment?.addAllList(lvItemList)
    }

    private fun testThread() {
        val blockedThread = Thread(Runnable {
            Thread.sleep(2000)
        })
        ThreadBlockWatchDog.getInstance(blockedThread)
            .setTimeoutInterval(500).setThreadBlockedListener(object : ThreadBlockedListener {
                override fun onThreadBlocked(message: ThreadBlockMessage) {
                    AppLogUtils.e("ApmMonitorHelper ", "on thread blocked" + message.fillInStackTrace().message)
                    message.printStackTrace()
                }
            }).start()
    }

    private fun testLeak() {
        Handler().postDelayed(Runnable() {
            ToastUtils.showRoundRectToast("模拟内存泄漏")
        }, 100000)

        //省略
        if (mResource == null) {
            mResource = TestResource()
        }
        mResource?.getColor(this)

        startAsyncTask()

        AppManager.getInstance(this)
    }

    private var mResource: TestResource? = null
    class TestResource {
        //里面代码引用上下文，Activity.this会导致内存泄漏
        fun getColor(activity: ApmTestActivity){
            activity.resources.getColor(R.color.alpha_05_black)
        }
    }

    //匿名内部类也会持有外部类实例的引用
    fun startAsyncTask() {
        object : AsyncTask<Void?, Void?, Void?>() {
            //1
            override fun doInBackground(vararg params: Void?): Void? {
                while (true);
            }
        }.execute()
    }

    class AppManager private constructor(context: Context) {
        private val context: Context

        companion object {
            private var instance: AppManager? = null
            fun getInstance(context: Context): AppManager? {
                if (instance == null) {
                    instance = AppManager(context)
                }
                return instance
            }
        }

        init {
            this.context = context
        }
    }


    private fun testSeriousBlock() {
        try {
            Thread.sleep(2600)
        } catch (e: Exception) {
        }
    }

    private fun executeGetAsnWithLog(url: String): String {
        val builder = OkHttpClient.Builder()
        val thisClient: OkHttpClient = builder.build()
        val request: Request = Request.Builder()
            .url(url)
            .build()
        val call = thisClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                response.close()
            }
        })
        return ""
    }

    object OOMMaker {
        var a: ArrayList<ByteArray?> = ArrayList<ByteArray?>()
        fun createOOM() {
            while (true) {
                a.add(ByteArray(2097152))
            }
        }

        fun encreaseMem() {
            a.add(ByteArray(15728640))
        }
    }
}