package com.yc.jetpack.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.yc.jetpack.R
import com.yc.jetpack.common.BaseConstant
import com.yc.toolutils.sp.AppSpUtils

class WelcomeFragment : Fragment() {

    lateinit var btnLogin: Button
    lateinit var btnRegister: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin = view.findViewById(R.id.btn_login)
        btnRegister = view.findViewById(R.id.btn_register)

        btnLogin.setOnClickListener {
            val navOption = navOptions {
                anim {
                    enter = R.anim.common_slide_in_right
                    exit = R.anim.common_slide_out_left
                    popEnter = R.anim.common_slide_in_left
                    popExit = R.anim.common_slide_out_right
                }
            }

            val name = AppSpUtils.getInstance().getString(BaseConstant.SP_USER_NAME)
            // Navigation 传递参数
            val bundle = Bundle()
            bundle.putString(BaseConstant.ARGS_NAME,name)

            //跳转到指定页面并且传递参数
            findNavController().navigate(R.id.login, bundle,navOption)
        }
        btnRegister.setOnClickListener {
            // 利用SafeArgs传递参数
            val action = WelcomeFragmentDirections
                .actionWelcomeToRegister()
                .setEMAIL("yangchong211@gamil.com")
            findNavController().navigate(action)
        }
    }



}