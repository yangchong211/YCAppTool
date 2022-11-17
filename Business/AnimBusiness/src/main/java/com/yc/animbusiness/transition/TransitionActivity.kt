package com.yc.animbusiness.transition

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build

import android.os.Bundle
import android.transition.ChangeBounds

import androidx.core.app.ActivityOptionsCompat
import kotlinx.android.synthetic.main.activity_main2.*
import android.util.Pair
import android.view.View
import androidx.annotation.RequiresApi
import com.yc.animbusiness.R


class TransitionActivity : BaseActivity() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setThisUpWindow()
        setContentView(R.layout.activity_main2)
        object_animal.setOnClickListener {
            startActivity(
                Intent(this@TransitionActivity, ObjectAnimatorActivity::class.java),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            )
        }
        translate_animal.setOnClickListener {
            val intent = Intent(this@TransitionActivity, SampleTranslateActivity::class.java)
            val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
            startActivity(intent, bundle)
        }
        scene_animal.setOnClickListener {
            startActivity(
                Intent(this@TransitionActivity, SceneActivity::class.java),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            )
        }
        share_element.setOnClickListener {
            val intent = Intent(this@TransitionActivity, ShareElementActivity1::class.java)
            val sharedView = image_blue
            var strName = image_blue.transitionName
            val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                this@TransitionActivity,
                sharedView,
                strName
            )
            startActivity(intent, transitionActivityOptions.toBundle())
        }
        multiple_share_element.setOnClickListener {
            val pair = Pair(image_blue as View, image_blue.transitionName)
            val pair1 = Pair(text1 as View, text1.transitionName)
            val intent = Intent(this@TransitionActivity, ShareElementActivity2::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this@TransitionActivity,
                pair, pair1
            )
            startActivity(intent, options.toBundle())
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setThisUpWindow() {
        window.sharedElementEnterTransition = ChangeBounds()
        window.sharedElementExitTransition = ChangeBounds()
        window.exitTransition = ChangeBounds()
    }
}


