package com.yc.jetpack.study.navigation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yc.jetpack.R


class SampleArgsFragment : Fragment() {

    private val args: SampleArgsFragmentArgs by navArgs()
    private var tvArgsContent: TextView? = null
    private var btnNavArgsJump: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sample_args, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().actionBar?.title = "带参数传递的Fragment"
        initView(view)
    }

    private fun initView(view: View) {
        view.run {
            tvArgsContent = view.findViewById(R.id.tv_sample_args_content)
            btnNavArgsJump = view.findViewById(R.id.btn_nav_args_jump)
        }
        tvArgsContent?.text = if (args.argumentFlag == 0) {
            args.argumentNormal
        } else {
            args.argumentBean.title
        }
        btnNavArgsJump?.setOnClickListener{
            findNavController().navigate(R.id.action_to_home_dest)
        }
    }

}
