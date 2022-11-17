package com.yc.animbusiness.transition

import android.animation.*
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.RESTART
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.annotation.RequiresApi
import com.yc.animbusiness.R
import kotlinx.android.synthetic.main.activity_object_animal.*


class ObjectAnimatorActivity : BaseActivity(), View.OnClickListener {

    var isChange = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_animal)
        title = "属性动画示例"
        btn_translation_x.setOnClickListener(this)
        btn_rotation.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)
        btn_viewProperty_mulity.setOnClickListener(this)
        btn_animator_set.setOnClickListener(this)
        start_object_animator.setOnClickListener(this)
        pause_object_animator.setOnClickListener(this)
        reverse_object_animator.setOnClickListener(this)
        resume_object_animator.setOnClickListener(this)
        end_object_animator.setOnClickListener(this)
        cancel_object_animator.setOnClickListener(this)
        imag_view.animate().withEndAction { Log.i("ObjectAnimatorActivity", "withEndAction---") }
        imag_view.animate()
            .withStartAction { Log.i("ObjectAnimatorActivity", "withStartAction---") }
        imag_view.animate().setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                Log.i("ObjectAnimatorActivity", "onAnimationRepeat-------------")
            }

            override fun onAnimationEnd(animation: Animator?) {
                Log.i("ObjectAnimatorActivity", "onAnimationEnd-------------")
            }

            override fun onAnimationCancel(animation: Animator?) {
                Log.i("ObjectAnimatorActivity", "onAnimationCancel-------------")
            }

            override fun onAnimationStart(animation: Animator?) {
                Log.i("ObjectAnimatorActivity", "onAnimationStart-------------")
            }

        })
    }

    lateinit var mObjectAnim: ObjectAnimator
    var isSelect = true
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.btn_translation_x -> {
                isChange = if (isChange) {
                    imag_view.animate().run {
                        translationX(0f) //设置左移
                        duration = 1000 //设置动画时间
                        setInterpolator(LinearInterpolator()) //插值器 设置线性
                    }
                    false
                } else {
                    imag_view.animate().run {
                        translationX(400f) //设置移动至400f
                        duration = 1000 //动画时长
                        setInterpolator(LinearInterpolator()) //插值器 设置线性
                    }
                    true
                }
            }
            R.id.btn_rotation -> {
                imag_view.animate().rotation(180f).setDuration(1000)
            }
            R.id.btn_cancel -> {
                imag_view.animate().cancel()
            }

            R.id.btn_viewProperty_mulity -> {
                if (isSelect) {
//                    imag_view.animate().scaleX(1.5f).scaleY(1.5f).alpha(0f).duration =
//                        2000
                    var propertyValueHolder1 = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.5f)
                    var propertyValueHolder2 = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.5f)
                    var propertyValueHolder3 = PropertyValuesHolder.ofFloat("alpha", 1f, 0f)
                    ObjectAnimator.ofPropertyValuesHolder(
                        imag_view,
                        propertyValueHolder1,
                        propertyValueHolder2,
                        propertyValueHolder3
                    ).setDuration(2000).start()

                } else {
                    var propertyValueHolder1 = PropertyValuesHolder.ofFloat("scaleX", 1.5f, 1f)
                    var propertyValueHolder2 = PropertyValuesHolder.ofFloat("scaleY", 1.5f, 1f)
                    var propertyValueHolder3 = PropertyValuesHolder.ofFloat("alpha", 0f, 1f)
                    ObjectAnimator.ofPropertyValuesHolder(
                        imag_view,
                        propertyValueHolder1,
                        propertyValueHolder2,
                        propertyValueHolder3
                    ).setDuration(2000).start()
//                    imag_view.animate().scaleX(1.0f).scaleY(1.0f).alpha(1f).duration =
//                        2000
                }
                isSelect = !isSelect
            }
            R.id.btn_animator_set -> {
                var animator1 = ObjectAnimator.ofFloat(imag_view, "alpha", 0f, 1f)
                animator1.interpolator = AccelerateDecelerateInterpolator()
                var animator3 = ObjectAnimator.ofFloat(imag_view, "scaleX", 0f, 1f)
                var animator2 = ObjectAnimator.ofFloat(imag_view, "scaleY", 0f, 1f)
                var animator4 = ObjectAnimator.ofFloat(imag_view, "translationX", 0f, 200f)
                animator4.interpolator = LinearInterpolator()
                AnimatorSet().apply {
                    play(animator1).with(animator2).with(animator3).before(animator4)
                    duration = 2000
                    start()
                }
//                AnimatorSet().apply {
//                    playTogether(animator2,animator3)
//                    playSequentially(animator1,animator4)
//                    duration = 2000
//                    start()
//                }
            }

            R.id.start_object_animator -> {
                mObjectAnim = ObjectAnimator.ofFloat(circle_view, "progress", 0f, 80f).apply {
                    duration = 10000
                    interpolator = OvershootInterpolator()
                    repeatCount = INFINITE//无限循环
                    repeatMode = RESTART //重新开始
                    addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                        override fun onAnimationUpdate(animation: ValueAnimator?) {
//                            Log.i("ObjectAnimatorActivity","addUpdateListener -- onAnimationUpdate ------------------------------------")
                        }
                    })
                    addListener(
                        object : Animator.AnimatorListener {
                            override fun onAnimationRepeat(animation: Animator?) {
                                Log.i(
                                    "ObjectAnimatorActivity",
                                    "AnimatorListener -- onAnimationRepeat------------------------------------"
                                )
                            }

                            override fun onAnimationEnd(animation: Animator?) {
                                Log.i(
                                    "ObjectAnimatorActivity",
                                    "AnimatorListener -- onAnimationEnd------------------------------------"
                                )
                            }

                            override fun onAnimationCancel(animation: Animator?) {
                                Log.i(
                                    "ObjectAnimatorActivity",
                                    "AnimatorListener -- onAnimationCancel------------------------------------"
                                )
                            }

                            override fun onAnimationStart(animation: Animator?) {
                                Log.i(
                                    "ObjectAnimatorActivity",
                                    "AnimatorListener -- onAnimationStart------------------------------------"
                                )
                            }
                        }
                    )
                }
                mObjectAnim.start()
                Log.i("ObjectAnimatorActivity", "点击开始状态 --${!mObjectAnim.isStarted}")

                var keyframe = Keyframe.ofFloat(0f, 0f)
                var keyframe1 = Keyframe.ofFloat(0.5f, 95f)
                var keyFrame2 = Keyframe.ofFloat(1f, 80f)
                var holder =
                    PropertyValuesHolder.ofKeyframe("progress", keyframe, keyframe1, keyFrame2)
                ObjectAnimator.ofPropertyValuesHolder(circle_view, holder).setDuration(3000).start()
            }
            R.id.end_object_animator -> {
                mObjectAnim.end()
                Log.i("ObjectAnimatorActivity", "点击结束状态 --${!mObjectAnim.isRunning}")

            }
            R.id.cancel_object_animator -> {
                mObjectAnim.cancel()
                Log.i("ObjectAnimatorActivity", "点击取消状态 -- ")
            }
            R.id.pause_object_animator -> {
                if (mObjectAnim.isRunning) {
                    mObjectAnim.pause()
                    Log.i("ObjectAnimatorActivity", "点击暂停状态 --${mObjectAnim.isPaused}")
                }
            }
            R.id.reverse_object_animator -> {
                Log.i("ObjectAnimatorActivity", "点击反向状态 --")
                mObjectAnim.reverse()
            }
            R.id.resume_object_animator -> {
                mObjectAnim.resume()
                Log.i("ObjectAnimatorActivity", "点击继续执行 --")
            }


        }
    }
}
