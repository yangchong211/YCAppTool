package com.yc.android.view.fragment

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.library.base.mvp.BaseLazyFragment
import com.yc.toolutils.AppSizeUtils
import com.yc.android.R
import com.yc.android.contract.AndroidKnowledgeContract
import com.yc.android.model.bean.TreeBean
import com.yc.android.network.ResponseBean
import com.yc.android.presenter.AndroidKnowledgePresenter
import com.yc.android.view.activity.AndroidActivity
import com.yc.android.view.activity.KnowledgeTreeDetailActivity
import com.yc.android.view.adapter.AndroidKnowledgeAdapter
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
                AppSizeUtils.dp2px(activity,1f), Color.parseColor("#f5f5f7"))
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