package com.yc.kotlinbusiness

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class KotlinHomeActivity : AppCompatActivity(){

    companion object{
        fun startActivity(context: Context){
            context.startActivity(Intent(context,KotlinHomeActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kotlin_activity_main)

        findViewById<TextView>(R.id.tv_1).setOnClickListener {
            startActivity(Intent(this,KotlinScopeActivity::class.java))
        }
        findViewById<TextView>(R.id.tv_2).setOnClickListener {
            startActivity(Intent(this,LifecycleScopeActivity::class.java))
        }
    }

}