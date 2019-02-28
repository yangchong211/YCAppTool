package com.ycbjie.android.view.fragment

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.SizeUtils
import com.pedaily.yc.ycdialoglib.toast.ToastUtils
import com.yc.cn.ycbannerlib.banner.BannerView
import com.ycbjie.android.R
import com.ycbjie.android.base.BaseItemView
import com.ycbjie.android.base.KotlinConstant
import com.ycbjie.android.contract.AndroidHomeContract
import com.ycbjie.android.model.bean.BannerBean
import com.ycbjie.android.model.bean.HomeData
import com.ycbjie.android.model.bean.HomeListBean
import com.ycbjie.android.network.ResponseBean
import com.ycbjie.android.presenter.AndroidHomePresenter
import com.ycbjie.android.view.activity.AndroidActivity
import com.ycbjie.android.view.activity.AndroidDetailActivity
import com.ycbjie.android.view.activity.AndroidLoginActivity
import com.ycbjie.android.view.adapter.AndroidHomeAdapter
import com.ycbjie.android.view.adapter.BannerPagerAdapter
import com.ycbjie.library.base.mvp.BaseFragment
import com.ycbjie.library.utils.DoShareUtils
import com.ycbjie.library.web.view.WebViewActivity
import org.yczbj.ycrefreshviewlib.YCRefreshView
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine


class AndroidHomeFragment : BaseFragment<AndroidHomePresenter>() , AndroidHomeContract.View{


    private var recyclerView : YCRefreshView ?=null
    private var activity: AndroidActivity? = null
    private var presenter: AndroidHomePresenter? = null
    private var page: Int = 0
    private lateinit var adapter: AndroidHomeAdapter
    private var mBanner: BannerView? = null
    /**
     * 循环轮询的数据
     */
    private val bannerLists = mutableListOf<BannerBean>()
    /**
     * kotlin数组的几种形式，编译成java代码对应的则是：
     * private final int[] a = new int[4];
     * private final Integer b = new Integer(Integer.valueOf(4));
     * private final Integer c = new Integer((Integer)null);
     * 注意要点：
     *      后面两种方法都对基本类型做了装箱处理，产生了额外的开销。
     *      所以当需要声明非空的基本类型数组时，应该使用xxxArray，避免自动装箱。
     */
    var a : IntArray = IntArray(4)
    var b : Array<Int> = arrayOf(4)
    var c : Array<Int?> = arrayOf(null)
    var d : Array<String> = arrayOf()


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        //as 是一个中缀操作符，as是不安全的转换操作符，如果as转换失败，会抛出一个异常，这就是不安全的。
        activity = context as AndroidActivity?
        //activity = context as? Activity
    }

    override fun onDetach() {
        super.onDetach()
        activity = null
    }

    override fun onResume() {
        super.onResume()
        if (mBanner!=null){
            mBanner?.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mBanner!=null){
            mBanner?.pause()
        }
    }


    override fun getContentView(): Int {
        return R.layout.base_easy_recycle
    }

    override fun initView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView) as YCRefreshView
        presenter = AndroidHomePresenter(this)
        initRecyclerView()
    }


    override fun initListener() {
        adapter.setOnItemClickListener { position ->
            val homeData: HomeData = adapter.allData[position] as HomeData
            AndroidDetailActivity.lunch(activity, homeData, homeData.collect, homeData.id)
        }
        adapter.setOnItemChildClickListener { view, position ->
            if(adapter.allData.size>position && position>=0){
                when (view.id){
                    //收藏
                    R.id.flLike ->{
                        if (SPUtils.getInstance().getInt(KotlinConstant.USER_ID)==0){
                            ToastUtils.showRoundRectToast(activity?.resources?.
                                    getString(R.string.collect_fail_pls_login))
                            AndroidLoginActivity.lunch(activity)
                        }else{
                            val homeData = adapter.allData[position]
                            val selectId = homeData.id
                            if (homeData.collect) {
                                presenter?.unCollectArticle(selectId)
                                addCollectStatus(homeData,position)
                            } else {
                                removeCollectStatus(homeData,position)
                                presenter?.collectInArticle(selectId)
                            }
                        }
                    }
                    //分享
                    R.id.ivMore ->{
                        val data = adapter.allData[position]
                        DoShareUtils.shareText(activity,data.link,data.title)
                    }
                }
            }
        }
    }

    override fun initData() {
        recyclerView?.showProgress()
        presenter?.getHomeList(page)
        presenter?.getBannerData(true)
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView?.setLayoutManager(linearLayoutManager)
        val line = RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1f), Color.parseColor("#f5f5f7"))
        recyclerView?.addItemDecoration(line)
        adapter = AndroidHomeAdapter(activity)
        recyclerView?.adapter = adapter
        recyclerView?.setRefreshing(false)
        recyclerView?.scrollTo(0, 0)
        recyclerView?.scrollBy(0, 0)
        recyclerView?.setRefreshListener {
            if (NetworkUtils.isConnected()) {
                page = 0
                presenter?.getHomeList(page)
            } else {
                recyclerView?.setRefreshing(false)
                ToastUtils.showToast( "没有网络")
            }
        }
        //加载更多
        adapter.setMore(R.layout.view_recycle_more, object : RecyclerArrayAdapter.OnMoreListener {
            override fun onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.allData.size > 0) {
                        page++
                        presenter?.getHomeList(page)
                    } else {
                        adapter.pauseMore()
                    }
                } else {
                    adapter.pauseMore()
                    ToastUtils.showToast( "网络不可用")
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
                    ToastUtils.showToast( "网络不可用")
                }
            }

            override fun onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore()
                } else {
                    ToastUtils.showToast( "网络不可用")
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
        adapter.setOnItemClickListener { position: Int ->
            if (adapter.allData.size>position && position>=0){
                val data = adapter.allData[position]
                WebViewActivity.lunch(activity, data.link!!, data.title!!)
            }
        }
    }

    override fun setBannerView(bean: ResponseBean<List<BannerBean>>?) {
        if(bean?.data != null){
            adapter.removeAllHeader()
            val itemView = object : BaseItemView(activity, R.layout.base_view_banner) {
                override fun setBindView(headerView: View) {
                    bannerLists.addAll(bean.data!!)
                    mBanner = headerView.findViewById(R.id.banner) as BannerView
                    mBanner?.setHintGravity(1)
                    mBanner?.setAnimationDuration(1000)
                    mBanner?.setPlayDelay(2000)
                    mBanner?.setHintPadding(0, 0, 0, SizeUtils.dp2px(10f))
                    mBanner?.setAdapter(BannerPagerAdapter(activity, bannerLists))
                    mBanner?.setOnBannerClickListener { position: Int ->
                        if(position>=0 && bannerLists.size>position){
                            val bannerBean = bean.data!![position]
                            WebViewActivity.lunch(activity, bannerBean.url!!, bannerBean.title!!)
                        }
                    }
                }
            }
            adapter.addHeader(itemView)
        }
    }


    private fun addCollectStatus(homeData: HomeData, position: Int) {
        homeData.collect = false
        adapter.notifyItemChanged(position)
        //adapter.notifyDataSetChanged()
    }


    private fun removeCollectStatus(homeData: HomeData, position: Int) {
        homeData.collect = true
        adapter.notifyItemChanged(position)
        //adapter.notifyDataSetChanged()
    }

    override fun setDataView(bean: ResponseBean<HomeListBean>) {
        if(bean.data!=null){
            recyclerView?.showRecycler()
            adapter.addAll(bean.data?.datas)
        }else{
            recyclerView?.showEmpty()
        }
    }

    override fun setNetWorkErrorView() {
        recyclerView?.showError()
    }

    override fun setDataErrorView() {
        recyclerView?.showError()
    }

    override fun unCollectArticleSuccess() {
        ToastUtils.showToast("取消收藏成功")
    }

    override fun unCollectArticleFail(t: Throwable) {
        ToastUtils.showRoundRectToast(t.message)
    }

    override fun collectInArticleSuccess() {
        ToastUtils.showRoundRectToast("收藏成功")
    }

    override fun collectInArticleFail(t: Throwable) {
        ToastUtils.showRoundRectToast(t.message)
    }

}