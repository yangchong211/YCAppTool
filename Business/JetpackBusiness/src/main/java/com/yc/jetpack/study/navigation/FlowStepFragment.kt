package com.yc.jetpack.study.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.yc.jetpack.R

class FlowStepFragment : Fragment() {

    private var flowStepNumber :Int ?=null

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
                btn.setOnClickListener(
                    Navigation.createNavigateOnClickListener(R.id.next_one_action)
                )
            }
        }
    }

}