package com.ycbjie.android.view.fragment

import android.content.Intent
import android.graphics.Color
import android.provider.Settings
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.pedaily.yc.ycdialoglib.toast.ToastUtils
import com.ycbjie.android.R
import com.ycbjie.android.contract.AndroidProjectContract
import com.ycbjie.android.model.bean.HomeData
import com.ycbjie.android.model.bean.ProjectListBean
import com.ycbjie.android.model.bean.TreeBean
import com.ycbjie.android.network.ResponseBean
import com.ycbjie.android.presenter.AndroidProjectPresenter
import com.ycbjie.android.view.activity.AndroidDetailActivity
import com.ycbjie.android.view.adapter.AndroidProjectAdapter
import com.ycbjie.android.view.adapter.AndroidProjectTreeAdapter
import com.ycbjie.library.base.mvp.BaseLazyFragment
import com.ycbjie.library.utils.DoShareUtils
import kotlinx.android.synthetic.main.fragment_android_project.*
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter

class AndroidProjectFragment : BaseLazyFragment()  , AndroidProjectContract.View, View.OnClickListener {

    var presenter : AndroidProjectPresenter? = null
    private lateinit var listsAdapter: AndroidProjectAdapter
    private lateinit var kindsAdapter: AndroidProjectTreeAdapter
    private var kinds = mutableListOf<TreeBean>()
    private var selectProject: TreeBean? = null

    override fun getContentView(): Int {
        return R.layout.fragment_android_project
    }

    override fun initView(view: View) {
        presenter = AndroidProjectPresenter(this)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        drawerLayout.setScrimColor(Color.TRANSPARENT)
        initProjectsRecyclerView()
        initRProjectTreeRecyclerView()
        initRefresh()
    }

    override fun initListener() {
        tvKind.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun onLazyLoad() {
        rvList.showProgress()
        presenter?.getProjectTree()
    }


    override fun onClick(v: View?) {
        when (v?.id){
            R.id.tvKind ->{
                changeRightPage()
            }
        }
    }

    private fun changeRightPage() {
        if (drawerLayout.isDrawerOpen(flRight)) {
            drawerLayout.closeDrawer(flRight)
        } else {
            drawerLayout.openDrawer(flRight)
        }
    }


    private fun initProjectsRecyclerView() {
        //val表示常量
        val linearLayoutManager = LinearLayoutManager(activity)
        rvList.recyclerView.layoutManager = linearLayoutManager
        listsAdapter = AndroidProjectAdapter(activity)
        rvList.adapter = listsAdapter
        listsAdapter.setOnItemClickListener { position ->
            if (listsAdapter.allData.size > position && position > -1) {
                //条目点击事件
                val homeData: HomeData = listsAdapter.allData[position] as HomeData
                AndroidDetailActivity.lunch(activity, homeData, homeData.collect, homeData.id)
            }
        }
        listsAdapter.setOnItemChildClickListener(object : AndroidProjectAdapter.OnItemChildClickListener {
            override fun onChildClick(view: View, position: Int) {
                //子View点击事件
                when(view.id){
                    R.id.flLike->{
                        ToastUtils.showRoundRectToast("后期添加收藏")
                    }
                    R.id.ivMore->{
                        val homeData = listsAdapter.allData[position]
                        DoShareUtils.shareText(activity,homeData.link,homeData.title)
                    }
                }
            }
        })

        //加载更多
        listsAdapter.setMore(R.layout.view_recycle_more, object : RecyclerArrayAdapter.OnMoreListener {
            override fun onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (listsAdapter.allData.size > 0) {
                        presenter?.getProjectTreeList(selectProject!!.id, false)
                    } else {
                        listsAdapter.pauseMore()
                    }
                } else {
                    listsAdapter.pauseMore()
                    ToastUtils.showRoundRectToast("没有网络")
                }

            }

            override fun onMoreClick() {
                if (NetworkUtils.isConnected()) {
                    if (listsAdapter.allData.size > 0) {
                        presenter?.getProjectTreeList(selectProject!!.id, false)
                    } else {
                        listsAdapter.pauseMore()
                    }
                } else {
                    listsAdapter.pauseMore()
                    ToastUtils.showRoundRectToast("没有网络")
                }
            }
        })

        //设置没有数据
        listsAdapter.setNoMore(R.layout.view_recycle_no_more, object : RecyclerArrayAdapter.OnNoMoreListener {
            override fun onNoMoreShow() {
                if (NetworkUtils.isConnected()) {
                    listsAdapter.resumeMore()
                } else {
                    ToastUtils.showRoundRectToast("没有网络")
                }
            }

            override fun onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    listsAdapter.resumeMore()
                } else {
                    ToastUtils.showRoundRectToast("没有网络")
                }
            }
        })

        //设置错误
        listsAdapter.setError(R.layout.view_recycle_error, object : RecyclerArrayAdapter.OnErrorListener {
            override fun onErrorShow() {
                listsAdapter.resumeMore()
            }

            override fun onErrorClick() {
                listsAdapter.resumeMore()
            }
        })
    }

    private fun initRProjectTreeRecyclerView() {
        rvKinds.layoutManager = LinearLayoutManager(activity)
        kindsAdapter = AndroidProjectTreeAdapter(activity)
        rvKinds.adapter = kindsAdapter
        kindsAdapter.setOnItemClickListener { position ->
            if (kindsAdapter.allData.size > position && position > -1) {
                //条目点击事件
                //关闭侧滑。请求数据
                drawerLayout.closeDrawer(flRight)
                selectProject = kinds[position]
                //结束更多刷新
                //listAdapter?.loadMoreEnd(true)
                kindsAdapter.setSelect(selectProject!!)
                tvKind.text = selectProject!!.name
                presenter?.getProjectTreeList(selectProject!!.id, true)
                kindsAdapter.notifyDataSetChanged()
            }
        }
    }



    private fun initRefresh() {
        rvList.setRefreshListener {
            if (NetworkUtils.isConnected()) {
                presenter?.getProjectTreeList(selectProject!!.id, true)
            } else {
                rvList.setRefreshing(false)
                ToastUtils.showRoundRectToast( "网络不可用")
            }
        }
    }


    override fun setProjectTreeSuccess(bean: List<TreeBean>) {
        kinds = bean as MutableList<TreeBean>
        selectProject = kinds[0]
        kindsAdapter.clear()
        kindsAdapter.addAll(bean)
        kindsAdapter.setSelect(selectProject!!)
        kindsAdapter.notifyDataSetChanged()

        tvKind.text = selectProject!!.name
        presenter?.getProjectTreeList(selectProject!!.id, true)

    }

    override fun setProjectListByCidSuccess(bean: ResponseBean<ProjectListBean>, refresh: Boolean) {
        rvList.setRefreshing(false)
        if (refresh) {
            val data = bean.data
            val size = data?.size
            LogUtils.e("size数量-----$size")
            listsAdapter.clear()
            //这种为什么不行？？？？
            listsAdapter.addAll(data?.datas!!)
            listsAdapter.notifyDataSetChanged()
            rvList.showRecycler()
        } else {
            if (bean.data?.size  != 0) {
                listsAdapter.addAll(bean.data?.datas!!)
                listsAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun setProjectListByCidNetError() {
        rvList.setErrorView(R.layout.view_custom_network_error)
        rvList.showError()
        val network = rvList.findViewById(R.id.ll_set_network) as LinearLayout
        network.setOnClickListener {
            if (NetworkUtils.isConnected()) {
                onLazyLoad()
            } else {
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(intent)
            }
        }
    }

    override fun setProjectListByCidError() {
        rvList.setErrorView(R.layout.view_custom_data_error)
        rvList.showError()
        val errorView = rvList.findViewById(R.id.ll_error_view) as LinearLayout
        errorView.setOnClickListener { onLazyLoad() }
    }

}