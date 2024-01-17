package com.yc.appserver.log

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yc.appserver.R
import com.yc.logclient.LogUtils
import com.yc.monitorfilelib.FileExplorerActivity
import com.yc.roundcorner.view.RoundTextView


class LogTestActivity : AppCompatActivity() {

    private lateinit var tvTest0: RoundTextView
    private lateinit var tvTest1: RoundTextView
    private lateinit var tvTest2: RoundTextView
    private lateinit var tvTest3: RoundTextView
    private lateinit var tvTest4: RoundTextView
    private lateinit var tvTest5: RoundTextView
    private lateinit var tvTest6: RoundTextView
    private lateinit var tvTest7: RoundTextView

    companion object{
        fun startActivity(context: Context) {
            try {
                val target = Intent()
                target.setClass(context, LogTestActivity::class.java)
                context.startActivity(target)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actvitiy_test_log)
        initView()
        initListener()
        LogUtils.i("Log test info : LogTestActivity is create")
    }

    private fun initView() {
        tvTest0 = findViewById(R.id.tv_test0)
        tvTest1 = findViewById(R.id.tv_test1)
        tvTest2 = findViewById(R.id.tv_test2)
        tvTest3 = findViewById(R.id.tv_test3)
        tvTest4 = findViewById(R.id.tv_test4)
        tvTest5 = findViewById(R.id.tv_test5)
        tvTest6 = findViewById(R.id.tv_test6)
        tvTest7 = findViewById(R.id.tv_test7)
    }

    private fun initListener() {
        tvTest0.setOnClickListener {
            LogUtils.i("tvTest0")
            FileExplorerActivity.startActivity(this@LogTestActivity)
        }
        tvTest1.setOnClickListener {
            Toast.makeText(this,"主线程打印日志",Toast.LENGTH_SHORT).show()
            for (i in 0..100){
                LogUtils.i("Log test info : click text view $i")
            }
            for (i in 0..100){
                LogUtils.d("Log debug info : click text view $i")
            }
            for (i in 0..100){
                LogUtils.e("Log error info : click text view $i")
            }
            for (i in 0..100){
                LogUtils.w("Log warn info : click text view $i")
            }
            for (i in 0..100){
                LogUtils.v("Log verbose info : click text view $i")
            }
            Toast.makeText(this,"主线程打印日志结束",Toast.LENGTH_SHORT).show()
        }
        tvTest2.setOnClickListener {
            Toast.makeText(this,"子线程打印日志",Toast.LENGTH_SHORT).show()
            for (i in 0..100){
                Thread(Runnable {
                    LogUtils.i("thread info info : click text view $i")
                }).start()
            }
            for (i in 0..100){
                Thread(Runnable {
                    LogUtils.d("thread debug info : click text view $i")
                }).start()
            }
            for (i in 0..100){
                Thread(Runnable {
                    LogUtils.e("thread error info : click text view $i")
                }).start()

            }
            for (i in 0..100){
                Thread(Runnable {
                    LogUtils.w("thread warn info : click text view $i")
                }).start()
            }
            for (i in 0..100){
                Thread(Runnable {
                    LogUtils.v("thread verbose info : click text view $i")
                }).start()
            }
        }
        tvTest3.setOnClickListener {
            LogUtils.i("Log test info : click text view test3")
            Toast.makeText(this,"测试",Toast.LENGTH_SHORT).show()
        }
        tvTest4.setOnClickListener {

        }
        tvTest5.setOnClickListener {

        }
        tvTest6.setOnClickListener {

        }
        tvTest7.setOnClickListener {

        }
    }


}