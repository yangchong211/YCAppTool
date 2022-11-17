package com.yc.animbusiness.transition

import android.os.Bundle
import android.view.Window
import com.yc.animbusiness.R
import com.yc.animbusiness.transition.BaseActivity
import kotlinx.android.synthetic.main.activity_share_element.*

class ShareElementActivity1 : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_element)
        title = "ShareElementActivity"
        button2.setOnClickListener{
            onBackPressed()
        }
    }

}
