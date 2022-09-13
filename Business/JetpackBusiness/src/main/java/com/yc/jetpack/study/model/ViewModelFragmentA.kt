package com.yc.jetpack.study.model

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yc.jetpack.R

class ViewModelFragmentA : Fragment() {

    private var container: ConstraintLayout? = null
    private var tvName: TextView? = null
    private var btnSave: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vm_a_b, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        initViewModel()
    }

    private fun initView(view: View) {
        container = view.findViewById(R.id.container)
        tvName = view.findViewById(R.id.tv_name)
        btnSave = view.findViewById(R.id.btn_save)
    }

    /**
     * ViewModel类，没有参数
     */
    private var viewModel: NoParamsViewModel? = null

    private fun initViewModel() {
        // 不带参数的ViewModel获取方法使用非常简单
        viewModel = ViewModelProvider(this).get(
            NoParamsViewModel::class.java
        )

        //监听
        viewModel?.getName()?.observe(viewLifecycleOwner, Observer { savedString ->
            val str = savedString ?: this.resources.getString(R.string.app_name)
            tvName?.text = "FragmentA，无参数model案例：$str"
        })
        //监听
        viewModel?.getName()?.observe(this){
            val str = it ?: this.resources.getString(R.string.app_name)
            tvName?.text = "FragmentA，无参数model案例：$str"
        }

        //触发
        var index = 1
        btnSave?.setOnClickListener(){
            viewModel?.saveNewName("FragmentA:你是个逗比" + index++)
        }
    }

}