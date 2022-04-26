package com.yc.jetpack.study.model

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yc.jetpack.R

class ViewModelFragmentB : Fragment() {

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
     * ViewModel类，有参数
     */
    private var mHasParamsViewModel: HasParamsViewModel? = null

    private fun initViewModel() {
        // 获取带有参数的ViewModel
        val viewModeFactory = ViewModelActivity.HasParamsFactory(SavedDataRepository())
        mHasParamsViewModel = ViewModelProvider(this,viewModeFactory)
            .get(HasParamsViewModel::class.java)

        //添加监听
        mHasParamsViewModel?.getName()?.observe(viewLifecycleOwner,{
            Log.d("返回的数据是：","${it?.toString()}")
            tvName?.text = "FragmentB，有参数model案例：$it"
        })

        //请求操作
        mHasParamsViewModel?.getSupportData()
        var index = 1
        btnSave?.setOnClickListener {
            mHasParamsViewModel?.saveNewName("FragmentB，杨充，就是一个逗比"+ index++)
        }
    }

}