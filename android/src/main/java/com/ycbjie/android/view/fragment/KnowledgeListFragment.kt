package com.ycbjie.android.view.fragment

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.ycbjie.android.contract.KnowledgeListContract
import com.ycbjie.android.model.bean.HomeData
import com.ycbjie.android.model.bean.ProjectListBean
import com.ycbjie.android.model.bean.TreeBean
import com.ycbjie.android.presenter.KnowledgeListPresenter
import com.ycbjie.android.view.adapter.KnowledgeListAdapter
import com.blankj.utilcode.util.NetworkUtils
import com.pedaily.yc.ycdialoglib.toast.ToastUtils
import com.ycbjie.android.R
import com.ycbjie.library.base.mvp.BaseLazyFragment
import kotlinx.android.synthetic.main.base_android_recycle.*
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter


class KnowledgeListFragment : BaseLazyFragment()  , KnowledgeListContract.View{

    private lateinit var listsAdapter: KnowledgeListAdapter
    private var treeBean: TreeBean? = null
    private var data = mutableListOf<HomeData>()

    companion object {
        var TREE_BEAN = "treeBean"
    }

    private val presenter: KnowledgeListPresenter by lazy {
        KnowledgeListPresenter(this)
    }

    override fun getContentView(): Int {
        return R.layout.base_android_recycle
    }

    override fun initView(view: View?) {
        initRecyclerView()
        initRefresh()
    }


    override fun initListener() {
        listsAdapter.setOnItemClickListener({ position ->
            if (listsAdapter.allData.size > position && position > -1) {
                //条目点击事件

            }
        })
    }

    override fun initData() {

    }

    override fun onLazyLoad() {
        recyclerView.showProgress()
        getData(true)
    }


    private fun getData(isRefresh: Boolean) {
        val bundle = arguments
        if(bundle!=null){
            treeBean = bundle.getSerializable(TREE_BEAN) as TreeBean
            if (treeBean == null) {
                return
            }
            recyclerView.setRefreshing(true)
            presenter.getKnowledgeList(treeBean!!.id, isRefresh)
        }
    }


    private fun initRecyclerView() {
        //val表示常量
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.recyclerView.layoutManager = linearLayoutManager
        listsAdapter = KnowledgeListAdapter(activity)
        recyclerView.adapter = listsAdapter


        //加载更多
        listsAdapter.setMore(R.layout.view_recycle_more, object : RecyclerArrayAdapter.OnMoreListener {
            override fun onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (listsAdapter.allData.size > 0) {

                    } else {
                        listsAdapter.pauseMore()
                    }
                } else {
                    listsAdapter.pauseMore()
                    ToastUtils.showRoundRectToast("网络不可用")
                }

            }

            override fun onMoreClick() {
                if (NetworkUtils.isConnected()) {
                    if (listsAdapter.allData.size > 0) {

                    } else {
                        listsAdapter.pauseMore()
                    }
                } else {
                    listsAdapter.pauseMore()
                    ToastUtils.showRoundRectToast("网络不可用")
                }
            }
        })

        //设置没有数据
        listsAdapter.setNoMore(R.layout.view_recycle_no_more, object : RecyclerArrayAdapter.OnNoMoreListener {
            override fun onNoMoreShow() {
                if (NetworkUtils.isConnected()) {
                    listsAdapter.resumeMore()
                } else {
                    ToastUtils.showRoundRectToast("网络不可用")
                }
            }

            override fun onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    listsAdapter.resumeMore()
                } else {
                    ToastUtils.showRoundRectToast("网络不可用")
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

    private fun initRefresh() {
        recyclerView.setRefreshListener {
            if (NetworkUtils.isConnected()) {
                onLazyLoad()
            } else {
                recyclerView.setRefreshing(false)
                ToastUtils.showRoundRectToast("网络不可用")
            }
        }
    }

    override fun loadAllArticles(bean: ProjectListBean?, refresh: Boolean) {
        if (refresh) {
            data.clear()
        }
        this.data.addAll(bean?.datas!!)
        listsAdapter.addAll(data)
        recyclerView.setRefreshing(false)
        listsAdapter.notifyDataSetChanged()
    }

    override fun getKnowledgeFail(message: String, refresh: Boolean) {
        if(NetworkUtils.isConnected()){
            recyclerView?.showError()
        }else{
            recyclerView.showError()
        }
    }


}