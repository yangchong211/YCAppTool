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

class NavigationFragment : Fragment(), View.OnClickListener {

    private var tvText: TextView? = null
    private var btnDestination: Button? = null
    private var btnAction: Button? = null
    private var btnArguments: Button? = null
    private var btnBean: Button? = null

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
            btnArguments = view.findViewById(R.id.btn_arguments)
            btnBean = view.findViewById(R.id.btn_bean)
        }
        btnDestination?.setOnClickListener(this)
        btnAction?.setOnClickListener(this)
        btnArguments?.setOnClickListener(this)
        btnBean?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            //这种是直接指向 fragment 标签的ID
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
            //这种是直接指向 fragment 标签的action
            //注意，这个action的id必须是在 NavigationFragment 下面
            R.id.btn_action ->{
                //Navigation.createNavigateOnClickListener(R.id.home_dest_one, null)
                findNavController().navigate(R.id.home_dest_one, null)
            }
            R.id.btn_arguments ->{

            }
            R.id.btn_bean ->{

            }
        }
    }

}