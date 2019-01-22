package com.ycbjie.android.view.fragment

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.ycbjie.android.contract.AndroidKnowledgeContract
import com.ycbjie.android.model.bean.TreeBean
import com.ycbjie.android.presenter.AndroidKnowledgePresenter
import com.ycbjie.android.view.activity.AndroidActivity
import com.ycbjie.android.view.activity.KnowledgeTreeDetailActivity
import com.ycbjie.android.view.adapter.AndroidKnowledgeAdapter
import com.blankj.utilcode.util.SizeUtils
import com.ycbjie.android.R
import com.ycbjie.library.base.mvp.BaseLazyFragment
import com.ycbjie.android.network.ResponseBean
import kotlinx.android.synthetic.main.base_android_recycle.*
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine


class AndroidKnowledgeFragment : BaseLazyFragment()  , AndroidKnowledgeContract.View{

    var presenter : AndroidKnowledgePresenter? = null
    private lateinit var adapter: AndroidKnowledgeAdapter
    private var activity : AndroidActivity?= null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AndroidActivity
    }

    override fun onDetach() {
        super.onDetach()
        activity = null
    }

    override fun getContentView(): Int {
        return R.layout.base_android_recycle
    }

    override fun initView(view: View) {
        presenter = AndroidKnowledgePresenter(this)
        initRecyclerView()
    }

    override fun initListener() {
        adapter.setOnItemClickListener { position: Int ->
            if (adapter.allData.size>position && position>=0){
                KnowledgeTreeDetailActivity.lunch(activity, adapter.allData[position], 0)
            }
        }
        adapter.knowledgeItemClick = object : AndroidKnowledgeAdapter.KnowledgeItemListener{
            override fun knowledgeItemClick(bean: TreeBean, index: Int, position: Int) {
                KnowledgeTreeDetailActivity.lunch(activity, adapter.allData[position], index)
            }
        }
    }

    override fun initData() {

    }


    override fun onLazyLoad() {
        recyclerView.showProgress()
        presenter?.getKnowledgeTree()
    }


    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView!!.setLayoutManager(linearLayoutManager)
        val line = RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1f), Color.parseColor("#f5f5f7"))
        recyclerView!!.addItemDecoration(line)
        adapter = AndroidKnowledgeAdapter(activity)
        recyclerView!!.adapter = adapter
        recyclerView!!.setRefreshing(false)
        recyclerView!!.scrollTo(0, 0)
        recyclerView!!.scrollBy(0, 0)
        recyclerView!!.setRefreshListener({
            onLazyLoad()
        })
    }

    override fun getTreeSuccess(bean: ResponseBean<List<TreeBean>>?) {
       if(bean!=null){
           val data = bean.data
           adapter.addAll(data)
           adapter.notifyDataSetChanged()
           recyclerView.showRecycler()
       }else{
           recyclerView.showEmpty()
       }
    }

}