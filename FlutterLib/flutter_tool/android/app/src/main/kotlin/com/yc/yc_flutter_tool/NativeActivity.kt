package com.yc.yc_flutter_tool

import android.app.Activity
import android.os.Bundle
import android.view.View

class NativeActivity : Activity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native)
        findViewById<View>(R.id.btn_flutter).setOnClickListener {
            finish()
        }
        findViewById<View>(R.id.btn_1).setOnClickListener {

        }
        findViewById<View>(R.id.btn_2).setOnClickListener {

        }
        findViewById<View>(R.id.btn_3).setOnClickListener {

        }
        findViewById<View>(R.id.btn_4).setOnClickListener {

        }
    }


}