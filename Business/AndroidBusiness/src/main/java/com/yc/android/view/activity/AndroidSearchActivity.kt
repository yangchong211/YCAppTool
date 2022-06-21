package com.yc.android.view.activity

import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yc.library.base.mvp.BaseActivity
import com.yc.statusbar.bar.StateAppBar
import com.yc.toastutils.ToastUtils
import com.yc.toolutils.net.AppNetworkUtils
import com.yc.toolutils.AppSizeUtils
import com.yc.toolutils.AppSpUtils
import com.yc.toolutils.KeyboardUtils
import com.yc.android.R
import com.yc.android.tools.base.KotlinConstant
import com.yc.android.contract.AndroidSearchContract
import com.yc.android.model.bean.ProjectListBean
import com.yc.android.model.bean.SearchTag
import com.yc.android.network.SchedulerProvider
import com.yc.android.presenter.AndroidSearchPresenter
import com.yc.android.view.adapter.AndroidSearchAdapter
import kotlinx.android.synthetic.main.activity_android_search.*
import org.yczbj.ycrefreshviewlib.inter.OnErrorListener
import org.yczbj.ycrefreshviewlib.inter.OnMoreListener
import org.yczbj.ycrefreshviewlib.inter.OnNoMoreListener
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
                AppSizeUtils.dp2px(this,1f), Color.parseColor("#f5f5f7"))
        recyclerView.addItemDecoration(line)
        adapter = AndroidSearchAdapter(this)
        recyclerView?.adapter = adapter
        recyclerView?.setRefreshing(false)
        recyclerView?.scrollTo(0, 0)
        recyclerView?.scrollBy(0, 0)
        recyclerView?.setRefreshListener {
            if (AppNetworkUtils.isConnected()) {
                presenter.search(edSearch.text.toString(), true)
            } else {
                recyclerView?.setRefreshing(false)
            }
        }
        //加载更多
        adapter.setMore(R.layout.view_recycle_more, object : OnMoreListener {
            override fun onMoreShow() {
                if (AppNetworkUtils.isConnected()) {
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
        adapter.setNoMore(R.layout.view_recycle_no_more, object : OnNoMoreListener {
            override fun onNoMoreShow() {
                if (AppNetworkUtils.isConnected()) {
                    adapter.resumeMore()
                } else {
                }
            }

            override fun onNoMoreClick() {
                if (AppNetworkUtils.isConnected()) {
                    adapter.resumeMore()
                } else {

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
        AppSpUtils.getInstance().put(KotlinConstant.HISTORY_SEARCH, jsonString)
    }


    private fun getHistory() {
        val jsonString = AppSpUtils.getInstance().getString(KotlinConstant.HISTORY_SEARCH)
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