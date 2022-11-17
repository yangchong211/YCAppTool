package com.yc.animbusiness.transition

import android.os.Build
import android.os.Bundle
import android.transition.Explode
import android.transition.Slide
import android.transition.TransitionInflater
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.yc.animbusiness.R

open class BaseActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpWindow()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpWindow() {
        window.let {
            it.sharedElementEnterTransition
            it.sharedElementExitTransition
            it.sharedElementReenterTransition
            it.sharedElementReturnTransition
            it.allowEnterTransitionOverlap = false
            it.allowReturnTransitionOverlap = false
            it.exitTransition = TransitionInflater.from(this).inflateTransition(R.transition.fade_transtion)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.enterTransition = Explode().apply {
                    duration = 500
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                it.reenterTransition = Explode().apply {
                    duration = 500
                }
            }
            it.returnTransition = Slide().apply {
                duration = 500
            }
        }
    }
}