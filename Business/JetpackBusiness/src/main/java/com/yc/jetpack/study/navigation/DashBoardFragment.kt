package com.yc.jetpack.study.navigation

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.yc.jetpack.R

class DashBoardFragment : Fragment(), View.OnClickListener {

    private var tvText: TextView? = null
    private var btnDestination: Button? = null
    private var btnAction: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_navigation_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.navigation_main_menu, menu)
    }

    private fun initView(view: View) {
        view.run {
            tvText = view.findViewById(R.id.tv_text)
            btnDestination = view.findViewById(R.id.btn_destination)
            btnAction = view.findViewById(R.id.btn_action)
        }
        btnDestination?.setOnClickListener(this)
        btnAction?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_action ->{
                // 利用SafeArgs传递参数
                Navigation.createNavigateOnClickListener(R.id.next_one_action, null)

                /*val action = FlowStepFragmentDirections
                    .nextOneAction()
                    .setFlowStepNumber(1)
                findNavController().navigate(action)*/
            }
            R.id.btn_destination->{
                //val navController = Navigation.findNavController(activity!!,R.id.my_nav_host_fragment)
                //navController.navigate(R.id.flow_step_one_dest)

                //findNavController().navigate(R.id.flow_step_one_dest, null)

                val navOption = navOptions {
                    anim {
                        enter = R.anim.common_slide_in_right
                        exit = R.anim.common_slide_out_left
                        popEnter = R.anim.common_slide_in_left
                        popExit = R.anim.common_slide_out_right
                    }
                }
                findNavController().navigate(R.id.flow_step_one_dest, null, navOption)
            }
        }
    }

}