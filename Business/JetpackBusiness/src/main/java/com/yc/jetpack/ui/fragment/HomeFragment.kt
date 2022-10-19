package com.yc.jetpack.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yc.architecturelib.navigation.navigate
import com.yc.baseclasslib.fragment.FragmentLifecycleHelper
import com.yc.jetpack.study.workmanager.WorkManagerActivity
import com.yc.jetpack.R
import com.yc.jetpack.study.binding.SolutionActivity
import com.yc.jetpack.study.lifecycle.LifecycleActivity
import com.yc.jetpack.study.model.ViewModelActivity
import com.yc.jetpack.study.room.RoomActivity

class HomeFragment : Fragment(), View.OnClickListener {
    
    private var tvNavigation: TextView? = null
    private var tvLiveData: TextView? = null
    private var tvViewModel: TextView? = null
    private var tvDataBinding: TextView? = null
    private var tvLifecycle: TextView? = null
    private var tvPaging: TextView? = null
    private var tvRoom: TextView? = null
    private var tvWorkManager: TextView? = null

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
        FragmentLifecycleHelper.getInstance()?.addFragmentLifecycle(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        FragmentLifecycleHelper.getInstance()?.removeFragmentLifecycle(this)
    }

    private fun initView(view: View) {
        tvNavigation = view.findViewById(R.id.tv_navigation)
        tvLiveData = view.findViewById(R.id.tv_live_data)
        tvViewModel = view.findViewById(R.id.tv_view_model)
        tvDataBinding = view.findViewById(R.id.tv_data_binding)
        tvLifecycle = view.findViewById(R.id.tv_lifecycle)
        tvPaging = view.findViewById(R.id.tv_paging)
        tvRoom = view.findViewById(R.id.tv_room)
        tvWorkManager = view.findViewById(R.id.tv_work_manager)

        tvNavigation?.setOnClickListener(this)
        tvLiveData?.setOnClickListener(this)
        tvViewModel?.setOnClickListener(this)
        tvDataBinding?.setOnClickListener(this)
        tvLifecycle?.setOnClickListener(this)
        tvPaging?.setOnClickListener(this)
        tvRoom?.setOnClickListener(this)
        tvWorkManager?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_navigation ->{
                navigate(R.id.navigationFragment)
            }
            R.id.tv_live_data ->{
                navigate(NavigationFragmentDirections.actionLiveDataActivity())
            }
            R.id.tv_view_model ->{
                navigate(R.id.viewModelActivity)
            }
            R.id.tv_lifecycle ->{
                activity?.let {
                    LifecycleActivity.startActivity(it)
                }
            }
            R.id.tv_data_binding ->{
                activity?.let {
                    SolutionActivity.startActivity(it)
                }
            }
            R.id.tv_room ->{
                activity?.let {
                    RoomActivity.startActivity(it)
                }
            }
            R.id.tv_work_manager ->{
                activity?.let {
                    WorkManagerActivity.startActivity(it)
                }
            }
        }
    }

}