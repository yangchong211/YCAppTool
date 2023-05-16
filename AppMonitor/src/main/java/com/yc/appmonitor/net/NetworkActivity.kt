package com.yc.appmonitor.net

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yc.appmonitor.MonitorApplication
import com.yc.appmonitor.R
import com.yc.appmonitor.net.NetworkActivity
import com.yc.appmonitor.net.NetworkModel.DataListener
import com.yc.netlib.ui.NetRequestActivity
import com.yc.toastutils.ToastUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*

/**
 * <pre>
 * @author 杨充
 * blog  : https://github.com/yangchong211
 * time  : 2017/8/22
 * desc  : MVC中的Controller层
 * revise:
</pre> *
 */
class NetworkActivity : AppCompatActivity() {
    private var mResponseTextView: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)
        mResponseTextView = findViewById(R.id.response_textView)
        findViewById<View>(R.id.test_button).setOnClickListener { sendRequest() }
        findViewById<View>(R.id.btn2).setOnClickListener { sendRequest2() }
        findViewById<View>(R.id.btn2).setOnClickListener { sendRequest3() }
        findViewById<View>(R.id.btn3).setOnClickListener { sendRequest3() }
        findViewById<View>(R.id.btn4).setOnClickListener { NetRequestActivity.start(this@NetworkActivity) }
        testLeak()
    }

    private fun sendRequest() {
        mResponseTextView!!.text = "loading..."
        object : Thread() {
            override fun run() {
                super.run()
                val testUrl = "https://www.wanandroid.com/article/query/0/json"
                val mapParams: MutableMap<String, Any> = HashMap()
                mapParams["k"] = "Android"
                OkHttpManager.getInstance().post(testUrl, mapParams, object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.e(TAG, "e = " + e.message)
                        runOnUiThread { mResponseTextView!!.text = e.message }
                    }

                    override fun onResponse(call: Call, response: Response) {
                        runOnUiThread {
                            val body: String
                            try {
                                body = response.body!!.string()
                                Log.d(TAG, body)
                                mResponseTextView!!.text = body
                            } catch (e: IOException) {
                                e.printStackTrace()
                                mResponseTextView!!.text = e.message
                            }
                        }
                    }
                })
            }
        }.start()
    }

    private fun sendRequest2() {
        mResponseTextView!!.text = "loading..."
        val testUrl = "https://www.wanandroid.com/banner/json"
        val networkModel = NetworkModel()
        networkModel.getData(testUrl, object : DataListener<String?> {
            override fun onSuccess(body: String?) {
                runOnUiThread { mResponseTextView!!.text = body }
            }

            override fun onError(error: String) {
                runOnUiThread { mResponseTextView!!.text = error }
            }
        })
    }

    private fun sendRequest3() {
        mResponseTextView!!.text = "loading..."
        val testUrl = "https://www.wanandroid.com/friend/json"
        val networkModel = NetworkModel()
        networkModel.getData(testUrl, object : DataListener<String?> {
            override fun onSuccess(body: String?) {
                runOnUiThread { mResponseTextView!!.text = body }
            }

            override fun onError(error: String) {
                runOnUiThread { mResponseTextView!!.text = error }
            }
        })
    }

    companion object {
        private const val TAG = "MainActivity"
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, NetworkActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
//        val refWatcher: RefWatcher = MonitorApplication.getRefWatcher(this)
//        refWatcher.watch(this)
    }

    private fun testLeak() {
        Handler().postDelayed(Runnable() {
            ToastUtils.showRoundRectToast("模拟内存泄漏")
        }, 100000)
    }

}