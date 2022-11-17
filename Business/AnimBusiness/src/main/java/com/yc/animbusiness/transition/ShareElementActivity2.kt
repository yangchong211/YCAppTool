package com.yc.animbusiness.transition

import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.view.Window
import androidx.annotation.RequiresApi
import com.yc.animbusiness.R
import com.yc.animbusiness.transition.BaseActivity
import kotlinx.android.synthetic.main.activity_share_element.*

class ShareElementActivity2 : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        window.sharedElementEnterTransition = ChangeBounds()
        window.sharedElementExitTransition = ChangeBounds()
        window.exitTransition = ChangeBounds()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_element2)
        title = "ShareElementActivity"
        button2.setOnClickListener{
            onBackPressed()
        }
    }

}
