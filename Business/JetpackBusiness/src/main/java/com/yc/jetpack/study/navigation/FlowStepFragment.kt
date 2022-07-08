package com.yc.jetpack.study.navigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.yc.architecturelib.navigation.navigate
import com.yc.baseclasslib.fragment.BaseVisibilityFragment
import com.yc.baseclasslib.fragment.OnFragmentVisibilityListener
import com.yc.jetpack.R

class FlowStepFragment : BaseVisibilityFragment() {

    private var flowStepNumber :Int ?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        addOnVisibilityChangedListener(listener)
    }

    override fun onDetach() {
        super.onDetach()
        removeOnVisibilityChangedListener(listener)
    }

    private val listener = OnFragmentVisibilityListener { it ->
        if (it){
            Log.d("FlowStepFragment" , "页面可见")
        } else {
            Log.d("FlowStepFragment" , "页面不可见")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        flowStepNumber = arguments?.getInt("flowStepNumber")
        return when(flowStepNumber){
            2 -> inflater.inflate(R.layout.fragment_navigation_step_two, container, false)
            else -> inflater.inflate(R.layout.fragment_navigation_step_one, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn = view.findViewById<View>(R.id.next_button)
        when(flowStepNumber){
            2->{
                btn.setOnClickListener(
                    Navigation.createNavigateOnClickListener(R.id.next_two_action)
                )
            }
            else->{
                btn.setOnClickListener {
                    navigate(R.id.next_one_action)
                }
            }
        }
    }

}