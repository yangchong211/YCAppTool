package com.yc.animbusiness.transition

import android.os.Build
import android.os.Bundle
import android.transition.Scene
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.yc.animbusiness.R
import kotlinx.android.synthetic.main.activity_scene.*


class SceneActivity : BaseActivity(), View.OnClickListener {

    companion object {
        val TAG = SceneActivity::class.java.name
    }

    lateinit var scene0: Scene
    lateinit var scene1: Scene
    lateinit var scene2: Scene
    lateinit var scene3: Scene

    lateinit var sceneRoot: ViewGroup


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scene)
        title = "场景Scene示例"
        setLayout()
        initScene()
        initListener()
    }

    private fun setLayout() {
        sceneRoot = findViewById(R.id.frame_layout)

    }

    //初始化多个场景
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initScene() {
//        scene0 = Scene.getSceneForLayout(sceneRoot,R.layout.scene_layout0,this)
//        scene1 = Scene.getSceneForLayout(sceneRoot,R.layout.scene_layout1,this)
//        scene2 = Scene.getSceneForLayout(sceneRoot,R.layout.scene_layout2,this)
//        scene3 = Scene.getSceneForLayout(sceneRoot,R.layout.scene_layout3,this)
        scene0 = Scene(
            sceneRoot,
            LayoutInflater.from(this).inflate(R.layout.scene_layout0, sceneRoot, false)
        )
        scene1 = Scene(
            sceneRoot,
            LayoutInflater.from(this).inflate(R.layout.scene_layout1, sceneRoot, false)
        )
        scene2 = Scene(
            sceneRoot,
            LayoutInflater.from(this).inflate(R.layout.scene_layout2, sceneRoot, false)
        )
        scene3 = Scene(
            sceneRoot,
            LayoutInflater.from(this).inflate(R.layout.scene_layout3, sceneRoot, false)
        )
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun initListener() {
        btn_scene1.setOnClickListener {
            Log.d(TAG, "场景1")
            val transition =
                TransitionInflater.from(this).inflateTransition(R.transition.explore_transtion)
            TransitionManager.go(scene0, transition)
        }
        btn_scene2.setOnClickListener {
            Log.d(TAG, "场景2")
            TransitionManager.go(
                scene1,
                TransitionInflater.from(this).inflateTransition(R.transition.slide_transtion)
            )
        }
        btn_scene3.setOnClickListener {
            Log.d(TAG, "场景3")
            TransitionManager.go(
                scene2,
                TransitionInflater.from(this).inflateTransition(R.transition.slide_transtion)
            )
        }
        btn_scene4.setOnClickListener {
            Log.d(TAG, "场景4")
            TransitionManager.go(
                scene3,
                TransitionInflater.from(this).inflateTransition(R.transition.explore_transtion)
            )
        }
    }

    override fun onClick(v: View?) {

    }

}