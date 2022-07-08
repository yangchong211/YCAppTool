package com.yc.jetpack.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.yc.architecturelib.navigation.navigate
import com.yc.jetpack.R
import com.yc.jetpack.study.navigation.NavigationActivity

class NavigationFragment : Fragment(), View.OnClickListener {

    private lateinit var btnNav: Button
    private lateinit var btnBottom: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_navigation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(view: View) {
        view.run {
            btnNav = view.findViewById(R.id.btn_nav)
            btnBottom = view.findViewById(R.id.btn_bottom)
        }
        btnNav.setOnClickListener(this)
        btnBottom.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_nav ->{

            }
            R.id.btn_bottom ->{
                navigate(R.id.navigationActivity)
                //findNavController().navigate(R.id.navigationActivity)
            }
        }
    }

}