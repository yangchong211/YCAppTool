package com.yc.android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yc.android.view.activity.AndroidActivity

class InitActivity : AppCompatActivity() {

    companion object{
        //?的作用是方法参数声明非空
        //为了增强逻辑，可以在方法参数上加上"?"，可以避免处理参数值时抛出空指针异常。
        fun startActivity(context: Activity?) {
            val intent = Intent(context, com.yc.android.InitActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isTaskRoot) {
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intent.action == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }
        //相当于对intent进行拓展
        //可以理解为T类型扩展一个入参为空，返回值为空的函数，这样我们就可以在传入的函数内部，也就是apply闭包内访问T中的成员。
        //跳转首页
        val intent = Intent(this, AndroidActivity::class.java)
        //apply函数是这样的，调用某对象的apply函数，在函数范围内，可以任意调用该对象的任意方法，并返回该对象
        intent.apply {
            action = intent.action
            data = intent.data
            //首先let()的定义是这样的，默认当前这个对象作为闭包的it参数，返回值是函数里面最后一行，或者指定return
            intent.extras?.let { extras ->
                putExtras(extras)
            }
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(intent)

//        val intent = Intent(this, AndroidActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.putExtra("yc", "yc")
//        startActivity(intent)
    }

}