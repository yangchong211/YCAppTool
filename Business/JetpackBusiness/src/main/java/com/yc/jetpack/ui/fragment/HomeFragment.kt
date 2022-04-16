package com.yc.jetpack.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.yc.jetpack.R
import com.yc.jetpack.study.navigation.NavigationActivity

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var tvNavigation: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        view.run {
            tvNavigation = view.findViewById(R.id.tv_navigation)
        }
        tvNavigation.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_navigation ->{
                val target = Intent()
                target.setClass(context!!, NavigationActivity::class.java)
                context?.startActivity(target)
            }
        }
    }

}