package com.yc.appmonitor.crash

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yc.appmonitor.R
import com.yc.monitortimelib.monitor.MonitorTimeHelper
import com.yc.toastutils.ToastUtils
import com.yc.crash.nativec.NativeCrashDumper
import java.util.*

/**
 * <pre>
 * @author yangchong
 * email  : yangchong211@163.com
 * time  : 2020/7/10
 * desc  : 制造异常测试类
 * revise:
</pre> *
 */
class CrashTestActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crash_test)
        findViewById<View>(R.id.tv_1).setOnClickListener(this)
        findViewById<View>(R.id.tv_2).setOnClickListener(this)
        findViewById<View>(R.id.tv_3).setOnClickListener(this)
        findViewById<View>(R.id.tv_4).setOnClickListener(this)
        findViewById<View>(R.id.tv_5).setOnClickListener(this)
        findViewById<View>(R.id.tv_6).setOnClickListener(this)
        findViewById<View>(R.id.tv_7).setOnClickListener(this)
        testLeak()
    }

    override fun onResume() {
        super.onResume()
        MonitorTimeHelper.end("startActivity")
    }

    override fun onStop() {
        super.onStop()
        MonitorTimeHelper.start("onStop")
    }

    override fun onDestroy() {
        MonitorTimeHelper.start("finish")
        super.onDestroy()
        MonitorTimeHelper.end("onStop")
//        val refWatcher: RefWatcher = MonitorApplication.getRefWatcher(this)
//        refWatcher.watch(this)
    }

    override fun finish() {
        super.finish()
        MonitorTimeHelper.end("finish")
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.tv_1) {
            MonitorTimeHelper.start("Click")
            "12.3".toInt()
        } else if (id == R.id.tv_2) {
            val list = ArrayList<String>()
            list[5]
            MonitorTimeHelper.end("Click")
        } else if (id == R.id.tv_3) {
            val activity: Activity? = null
            activity!!.isDestroyed
        } else if (id == R.id.tv_4) {
            Thread(Runnable { Toast.makeText(this@CrashTestActivity, "吐司", Toast.LENGTH_SHORT).show() }).start()
        } else if (id == R.id.tv_5) {
            Handler().post { throw RuntimeException("handler异常") }
        } else if (id == R.id.tv_6) {
            val list = ArrayList<Int>()
            for (i in 0..99) {
                list.add(i)
            }
            val iterator: Iterator<Int> = list.iterator()
            while (iterator.hasNext()) {
                val integer = iterator.next()
                if (integer % 2 == 0) {
                    list.remove(integer)
                }
            }
        } else if (id == R.id.tv_7) {
            NativeCrashDumper.getInstance().nativeCrash()
//            CrashListActivity.startActivity(this)
        }
    }

    private fun testLeak() {
        Handler().postDelayed(Runnable() {
            ToastUtils.showRoundRectToast("模拟内存泄漏")
        }, 100000)
    }

}