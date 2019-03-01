package com.ycbjie.android.view.activity

import android.content.Intent
import android.graphics.Color
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import cn.ycbjie.ycstatusbarlib.bar.StateAppBar
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.SizeUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pedaily.yc.ycdialoglib.toast.ToastUtils
import com.ycbjie.android.R
import com.ycbjie.android.base.KotlinConstant
import com.ycbjie.android.contract.AndroidSearchContract
import com.ycbjie.android.model.bean.ProjectListBean
import com.ycbjie.android.model.bean.SearchTag
import com.ycbjie.android.network.SchedulerProvider
import com.ycbjie.android.presenter.AndroidSearchPresenter
import com.ycbjie.android.view.adapter.AndroidSearchAdapter
import com.ycbjie.library.base.mvp.BaseActivity
import kotlinx.android.synthetic.main.activity_android_search.*
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine

/**
 * <pre>
 *     @author 杨充
 *     blog  :
 *     time  : 2018/05/30
 *     desc  : 搜索页面
 *     revise:
 * </pre>
 */
class AndroidSearchActivity : BaseActivity<AndroidSearchPresenter>() , AndroidSearchContract.View{

    private lateinit var adapter: AndroidSearchAdapter
    private var searTags = mutableListOf<SearchTag>()
    private var historyTags = mutableListOf<String>()
    //private var items = mutableListOf<SearchViewType>()

    companion object {
        fun lunch(context: FragmentActivity?) {
            context?.startActivity(Intent(context, AndroidSearchActivity::class.java))
        }
    }

    private val presenter: AndroidSearchPresenter by lazy {
        AndroidSearchPresenter(this, SchedulerProvider.getInstance()!!)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getContentView(): Int {
        return R.layout.activity_android_search
    }

    override fun initView() {
        StateAppBar.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorTheme))
        initToolBar()
        initRecyclerView()
        getHistory()
    }

    private fun initToolBar() {
        toolbar.run {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
        }
    }

    override fun initListener() {
        initEditTextInputListener()
        ivClear.setOnClickListener {
            edSearch.setText("")
            ivClear.visibility = View.GONE
            showRecommend()
        }
    }

    override fun initData() {
        presenter.getSearchTag()
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(linearLayoutManager)
        val line = RecycleViewItemLine(this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1f), Color.parseColor("#f5f5f7"))
        recyclerView.addItemDecoration(line)
        adapter = AndroidSearchAdapter(this)
        recyclerView?.adapter = adapter
        recyclerView?.setRefreshing(false)
        recyclerView?.scrollTo(0, 0)
        recyclerView?.scrollBy(0, 0)
        recyclerView?.setRefreshListener {
            if (NetworkUtils.isConnected()) {
                presenter.search(edSearch.text.toString(), true)
            } else {
                recyclerView?.setRefreshing(false)
            }
        }
        //加载更多
        adapter.setMore(R.layout.view_recycle_more, object : RecyclerArrayAdapter.OnMoreListener {
            override fun onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.allData.size > 0) {
                        presenter.search(edSearch.text.toString(), false)
                    } else {
                        adapter.pauseMore()
                    }
                } else {
                    adapter.pauseMore()
                }
            }

            override fun onMoreClick() {
            }
        })

        //设置没有数据
        adapter.setNoMore(R.layout.view_recycle_no_more, object : RecyclerArrayAdapter.OnNoMoreListener {
            override fun onNoMoreShow() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore()
                } else {
                }
            }

            override fun onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore()
                } else {

                }
            }
        })

        //设置错误
        adapter.setError(R.layout.view_recycle_error, object : RecyclerArrayAdapter.OnErrorListener {
            override fun onErrorShow() {
                adapter.resumeMore()
            }

            override fun onErrorClick() {
                adapter.resumeMore()
            }
        })
    }


    private fun initEditTextInputListener() {
        edSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val str = s.toString()
                if (str.isNotEmpty()) {
                    ivClear.visibility = View.VISIBLE
                } else {
                    ivClear.visibility = View.GONE
                }
            }
        })

        edSearch.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val str = edSearch.text.toString()
                if (str.isNotEmpty()) {
                    addHistory(str)
                    // 调用接口，开始搜索
                    KeyboardUtils.hideSoftInput(this@AndroidSearchActivity)
                    presenter.search(str, true)
                } else {
                    ToastUtils.showRoundRectToast("搜索文字为空")
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun addHistory(str: String) {
        if (historyTags.size == 10) {
            historyTags.removeAt(historyTags.size - 1)
        }
        if (historyTags.contains(str)) {
            return
        }
        historyTags.add(str)
        val jsonString = Gson().toJson(historyTags, object : TypeToken<MutableList<String>>() {}.type)
        SPUtils.getInstance().put(KotlinConstant.HISTORY_SEARCH, jsonString)
    }


    private fun getHistory() {
        val jsonString = SPUtils.getInstance().getString(KotlinConstant.HISTORY_SEARCH)
        if (!TextUtils.isEmpty(jsonString)) {
            historyTags = Gson().fromJson(jsonString, object : TypeToken<MutableList<String>>() {}.type)
        }
    }

    private fun showRecommend() {

    }

    override fun setSearchTagSuccess(t: MutableList<SearchTag>?) {

    }

    override fun setSearchTagFail(message: String) {
        ToastUtils.showRoundRectToast("获取标签失败")
    }

    override fun setAllData(t: ProjectListBean?, refresh: Boolean) {
        if (t!=null){
            adapter.clear()
            adapter.addAll(t.datas)
            adapter.notifyDataSetChanged()
            recyclerView.showRecycler()
        }else{
            recyclerView.showEmpty()
        }
    }

    override fun setSearchResultSuccess(t: ProjectListBean?, refresh: Boolean) {
        if(t!=null){
            adapter.addAll(t.datas)
            adapter.notifyDataSetChanged()
        }
    }

    override fun setSearchResultFail(message: String) {
        ToastUtils.showRoundRectToast(message)
        recyclerView.showError()
    }


}