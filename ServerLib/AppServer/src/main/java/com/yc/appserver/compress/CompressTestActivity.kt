package com.yc.appserver.compress

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yc.appserver.R
import com.yc.logclient.LogUtils
import com.yc.roundcorner.view.RoundTextView


class CompressTestActivity : AppCompatActivity() {

    private lateinit var tvTest1: RoundTextView
    private lateinit var tvTest2: RoundTextView
    private lateinit var tvTest3: RoundTextView


    companion object{
        fun startActivity(context: Context) {
            try {
                val target = Intent()
                target.setClass(context, CompressTestActivity::class.java)
                context.startActivity(target)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compress_main)
        initView()
        initListener()
        LogUtils.i("Log test info : LogTestActivity is create")
    }

    private fun initView() {
        tvTest1 = findViewById(R.id.tv_vibrator_1)
        tvTest2 = findViewById(R.id.tv_vibrator_2)
        tvTest3 = findViewById(R.id.tv_vibrator_3)
    }

    private fun initListener() {
        tvTest1.setOnClickListener {

        }
    }


}