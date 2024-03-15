package com.yc.jetpack.study.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.yc.baseclasslib.adapter.BaseFragmentPagerAdapter
import com.yc.jetpack.R

class ViewModelActivity : AppCompatActivity() {

    private var container: ConstraintLayout? = null
    private var tvName: TextView? = null
    private var btnSave: Button? = null
    private var tvSavedVm: TextView? = null
    private var tvTextNext: TextView? = null
    private var btnSend: Button? = null
    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    private var fragments = mutableListOf<Fragment>()

    companion object{
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ViewModelActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_state)
        initView()
        initTabLayout()
        initNoParamsModel()
        initHasParamsModel()
        initLazyViewModel()
    }

    private fun initView() {
        container = findViewById(R.id.container)
        tvName = findViewById(R.id.tv_name)
        btnSave = findViewById(R.id.btn_save)
        tvSavedVm = findViewById(R.id.tv_saved_vm)
        tvTextNext = findViewById(R.id.tv_text_next)
        btnSend = findViewById(R.id.btn_send)
        tabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
    }

    private fun initTabLayout() {
        fragments.add(ViewModelFragmentA())
        fragments.add(ViewModelFragmentB())
        val mutableList = mutableListOf("无参数", "有参数")
        val adapter = BaseFragmentPagerAdapter(
            supportFragmentManager
        )
        adapter.addFragmentList(fragments,mutableList)
        tabLayout?.setupWithViewPager(viewPager)
        viewPager?.offscreenPageLimit = mutableList.size
        viewPager?.adapter = adapter
        viewPager?.offscreenPageLimit = fragments.size
    }

    /**
     * ViewModel类，没有参数
     */
    private var mNoParamsViewModel: NoParamsViewModel? = null

    @SuppressLint("SetTextI18n")
    private fun initNoParamsModel() {
        // 不带参数的ViewModel获取方法使用非常简单
        mNoParamsViewModel = ViewModelProvider(this).get(
            NoParamsViewModel::class.java
        )

        //监听
        mNoParamsViewModel?.getName()?.observe(this, Observer { savedString ->
            val str = savedString ?: this.resources.getString(R.string.app_name)
            tvName?.text = "Activity无参数model案例：$str"
        })

        //触发
        var index = 1
        btnSave?.setOnClickListener(){
            mNoParamsViewModel?.saveNewName("Activity:你是个逗比" + index++)
        }
    }

    /**
     * ViewModel类，有参数
     */
    private var mHasParamsViewModel: HasParamsViewModel? = null
    @SuppressLint("SetTextI18n")
    private fun initHasParamsModel() {
        // 获取带有参数的ViewModel
        val viewModeFactory = HasParamsFactory(SavedDataRepository())
        mHasParamsViewModel = ViewModelProvider(this,viewModeFactory)
            .get(HasParamsViewModel::class.java)

        //添加监听
        mHasParamsViewModel?.getName()?.observe(this,{
            Log.d("返回的数据是：","${it?.toString()}")
            tvTextNext?.text = "有参数model案例：$it"
        })

        //请求操作
        mHasParamsViewModel?.getSupportData()
        var index = 1
        btnSend?.setOnClickListener {
            mHasParamsViewModel?.saveNewName("杨充，就是一个逗比"+ index++)
        }
    }

    //用户获取有参数model
    class HasParamsFactory(private val repository: SavedDataRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HasParamsViewModel(repository) as T
        }
    }

    /**
     * 懒加载
     */
    private fun initLazyViewModel() {

    }


}