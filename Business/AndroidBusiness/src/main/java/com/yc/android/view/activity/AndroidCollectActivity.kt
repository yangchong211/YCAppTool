package com.yc.android.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.library.base.mvp.BaseActivity
import com.yc.statusbar.bar.StateAppBar
import com.yc.toastutils.ToastUtils
import com.yc.toolutils.AppNetworkUtils
import com.yc.toolutils.AppSizeUtils
import com.yc.android.R
import com.yc.android.contract.AndroidCollectContract
import com.yc.android.model.bean.ProjectListBean
import com.yc.android.network.ResponseBean
import com.yc.android.presenter.AndroidCollectPresenter
import com.yc.android.view.adapter.AndroidCollectAdapter
import kotlinx.android.synthetic.main.base_android_bar.*
import kotlinx.android.synthetic.main.base_android_recycle.*
import org.yczbj.ycrefreshviewlib.inter.OnErrorListener
import org.yczbj.ycrefreshviewlib.inter.OnMoreListener
import org.yczbj.ycrefreshviewlib.inter.OnNoMoreListener
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine



class AndroidCollectActivity : BaseActivity<AndroidCollectPresenter>()
        , AndroidCollectContract.View{

    private var presenter : AndroidCollectPresenter ?= null
    private var page = 1
    //lateinit 本身的含义是延迟初始化，但是在编译时必须保证 lateinit 修饰的参数被初始化，否则编译之后运行会报错
    //慎用，因为防止后面没有进行初始化
    private lateinit var adapter : AndroidCollectAdapter
    //如果编译不过，则可以使用下面这种写法
    //private var adapter1 : AndroidHomeAdapter ? = null

    companion object {
        fun lunch(context: Activity?) {
            context?.startActivity(Intent(context, AndroidCollectActivity::class.java))
        }
    }

    override fun getContentView(): Int {
        return R.layout.base_easy_recycle_bar
    }

    override fun initView() {
        StateAppBar.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorTheme))
        initToolBar()
        presenter = AndroidCollectPresenter(this)
        initRecyclerView()
    }

    private fun initToolBar() {
        toolbar_title.visibility = View.VISIBLE
        toolbar_title.text = "我的收藏"
        ll_title_menu.setOnClickListener {
            onBackPressed()
        }
    }

    override fun initListener() {

    }

    override fun initData() {
        recyclerView.showProgress()
        presenter?.getCollectArticleList(page)
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(linearLayoutManager)
        val line = RecycleViewItemLine(this, LinearLayout.HORIZONTAL,
                AppSizeUtils.dp2px(this,1f), Color.parseColor("#f5f5f7"))
        recyclerView.addItemDecoration(line)
        adapter = AndroidCollectAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.setRefreshListener {
            if (AppNetworkUtils.isConnected()){
                page = 0
                presenter?.getCollectArticleList(page)
            }else{
                recyclerView?.setRefreshing(false)
                ToastUtils.showRoundRectToast( "没有网络")
            }
        }
        //加载更多
        adapter.setMore(R.layout.view_recycle_more, object : OnMoreListener {
            override fun onMoreShow() {
                if (AppNetworkUtils.isConnected()) {
                    if (adapter.allData.size > 0) {
                        page++
                        presenter?.getCollectArticleList(page)
                    } else {
                        adapter.pauseMore()
                    }
                } else {
                    adapter.pauseMore()
                    ToastUtils.showRoundRectToast( "网络不可用")
                }
            }

            override fun onMoreClick() {
            }
        })

        //设置没有数据
        adapter.setNoMore(R.layout.view_recycle_no_more, object : OnNoMoreListener {
            override fun onNoMoreShow() {
                if (AppNetworkUtils.isConnected()) {
                    adapter.resumeMore()
                } else {
                    ToastUtils.showRoundRectToast( "网络不可用")
                }
            }

            override fun onNoMoreClick() {
                if (AppNetworkUtils.isConnected()) {
                    adapter.resumeMore()
                } else {
                    ToastUtils.showRoundRectToast( "网络不可用")
                }
            }
        })

        //设置错误
        adapter.setError(R.layout.view_recycle_error, object : OnErrorListener {
            override fun onErrorShow() {
                adapter.resumeMore()
            }

            override fun onErrorClick() {
                adapter.resumeMore()
            }
        })
        adapter.setOnItemClickListener { position: Int ->
            if (adapter.allData.size>position && position>=0){

            }
        }
    }

    override fun setNetWorkErrorView() {
        recyclerView.showError()
    }


    override fun setDataErrorView(message: String?) {
        ToastUtils.showRoundRectToast(message)
        recyclerView.showError()
    }


    override fun setDataView(bean: ResponseBean<ProjectListBean>?) {
        if (bean!=null){
            recyclerView?.showRecycler()
            adapter.addAll(bean.data?.datas)
        }else{
            recyclerView.showEmpty()
        }
    }


}