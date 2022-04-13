package com.yc.jetpack.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.yc.jetpack.R
import com.yc.jetpack.common.navigate
import com.yc.jetpack.ui.activity.JetpackActivity

class LoginFragment : Fragment() {

    private lateinit var btnLogin: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin = view.findViewById(R.id.btn_login)

        btnLogin.setOnClickListener {
            val intent = Intent(context, JetpackActivity::class.java)
            context!!.startActivity(intent)
        }
    }

}