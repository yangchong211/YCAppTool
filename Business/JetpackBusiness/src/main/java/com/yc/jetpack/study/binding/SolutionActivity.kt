
package com.yc.jetpack.study.binding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.yc.jetpack.R
import com.yc.jetpack.databinding.ActivityBindingSolutionBinding


class SolutionActivity : AppCompatActivity() {

    private var mViewMode : SimpleViewModelSolution ?= null


    companion object{
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, SolutionActivity::class.java))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val binding: ActivityBindingSolutionBinding =
//            DataBindingUtil.setContentView(this, R.layout.activity_binding_solution)
//
//
//        // 不带参数的ViewModel获取方法使用非常简单
//        mViewMode = ViewModelProvider(this).get(SimpleViewModelSolution::class.java)
//
//        binding.lifecycleOwner = this  // use Fragment.viewLifecycleOwner for fragments
//        binding.viewmodel = mViewMode
//
//
//        findViewById<TextView>(R.id.like_button).setOnClickListener {
//            mViewMode?.onLike()
//        }
    }
}
