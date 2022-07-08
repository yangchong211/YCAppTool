package com.yc.jetpack.study.navigation

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.yc.architecturelib.navigation.findNavController2
import com.yc.architecturelib.navigation.navigateUp
import com.yc.jetpack.R
import com.yc.jetpack.ui.activity.JetpackActivity

class NavigationActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var navView: BottomNavigationView? = null
    private var navController: NavController? = null
    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, NavigationActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_home)
        initView()
        initNavController()
        initActionBar()
        setupBottomNavMenu()
    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbar)
        navView = findViewById(R.id.bottom_nav_view)
    }

    private fun initNavController() {
        //val host: NavHostFragment = supportFragmentManager
        //    .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return
        //navController = host.navController

        navController = Navigation.findNavController(this, R.id.my_nav_host_fragment)
        //下面这个是简化版
        //navController = findNavController(R.id.my_nav_host_fragment)
        navController?.addOnDestinationChangedListener { controller, destination, arguments ->
            //目标发生了变化的回调方法
            val dest: String = try {
                //获取
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                destination.id.toString()
            }

            toolbar?.title = "目标：" + destination.label
            Toast.makeText(
                this@NavigationActivity, "Navigated to $dest",
                Toast.LENGTH_SHORT
            ).show()
            Log.d("NavigationActivity", "Navigated to $dest")
        }
    }

    private fun initActionBar() {
        setSupportActionBar(toolbar)
        appBarConfiguration = AppBarConfiguration(navController!!.graph)
        setupActionBarWithNavController(navController!!, appBarConfiguration)
        toolbar?.setNavigationOnClickListener {
            navController?.navigateUp()
        }
    }

    private fun setupBottomNavMenu() {
        navController?.let {
            navView?.setupWithNavController(it)
            navView?.setBackgroundColor(Color.WHITE)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController2(R.id.my_nav_host_fragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(R.id.my_nav_host_fragment)
    }

}