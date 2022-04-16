package com.yc.jetpack.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import com.yc.jetpack.R
import com.yc.jetpack.ui.fragment.HomeFragment
import com.yc.jetpack.ui.fragment.SplashFragment


class JetpackActivity : AppCompatActivity(){

    private lateinit var navController : NavController

    companion object{
        fun startActivity(context : Context){
            context.startActivity(Intent(context,JetpackActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jetpack_main)
        initNavController()
    }

    private fun initNavController() {
        navController = findNavController(R.id.home_nav_host_fragment)
        navController.navigate(R.id.homeFragment)
        /*navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if ((destination as FragmentNavigator.Destination).className != SplashFragment::class.java.name){

            }
            if ((destination as FragmentNavigator.Destination).className != HomeFragment::class.java.name){

            }
        }*/
    }


}