package com.ycbjie.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ycbjie.android.view.activity.AndroidActivity

class InitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isTaskRoot){
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intent.action == Intent.ACTION_MAIN){
                finish()
                return
            }
        }
        //相当于对intent进行拓展
        //可以理解为T类型扩展一个入参为空，返回值为空的函数，这样我们就可以在传入的函数内部，也就是apply闭包内访问T中的成员。
        //跳转首页
        startActivity(Intent(this,AndroidActivity::class.java).apply {
            action = intent.action
            data = intent.data
            intent.extras?.let { extras ->
                putExtras(extras)
            }
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        })

//        val intent = Intent(this, AndroidActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.putExtra("yc", "yc")
//        startActivity(intent)
    }

}