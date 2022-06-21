package com.yc.android.view.fragment

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.yc.cn.ycbannerlib.banner.view.BannerView
import com.yc.library.base.mvp.BaseFragment
import com.yc.library.web.WebViewActivity
import com.yc.toastutils.ToastUtils
import com.yc.toolutils.net.AppNetworkUtils
import com.yc.toolutils.AppSizeUtils
import com.yc.toolutils.AppSpUtils
import com.yc.ycshare.DoShareUtils
import com.yc.android.R
import com.yc.android.tools.base.BaseItemView
import com.yc.android.tools.base.KotlinConstant
import com.yc.android.contract.AndroidHomeContract
import com.yc.android.model.bean.BannerBean
import com.yc.android.model.bean.HomeData
import com.yc.android.model.bean.HomeListBean
import com.yc.android.network.ResponseBean
import com.yc.android.presenter.AndroidHomePresenter
import com.yc.android.view.activity.AndroidActivity
import com.yc.android.view.activity.AndroidDetailActivity
import com.yc.android.view.activity.AndroidLoginActivity
import com.yc.android.view.adapter.AndroidHomeAdapter
import com.yc.android.view.adapter.BannerPagerAdapter
import org.yczbj.ycrefreshviewlib.inter.OnErrorListener
import org.yczbj.ycrefreshviewlib.inter.OnMoreListener
import org.yczbj.ycrefreshviewlib.inter.OnNoMoreListener
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine
import org.yczbj.ycrefreshviewlib.view.YCRefreshView


class AndroidHomeFragment : BaseFragment<AndroidHomePresenter>() , AndroidHomeContract.View{


    private var recyclerView : YCRefreshView?=null
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //as 是一个中缀操作符，as是不安全的转换操作符，如果as转换失败，会抛出一个异常，这就是不安全的。
        activity = context as AndroidActivity?
        //activity_test = context as? Activity
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
        view.run {
            recyclerView = view.findViewById(R.id.recyclerView) as YCRefreshView
        }
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
                        if (AppSpUtils.getInstance().getInt(KotlinConstant.USER_ID)==0){
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
                AppSizeUtils.dp2px(activity,1f), Color.parseColor("#f5f5f7"))
        recyclerView?.addItemDecoration(line)
        adapter = AndroidHomeAdapter(activity)
        recyclerView?.adapter = adapter
        recyclerView?.setRefreshing(false)
        recyclerView?.scrollTo(0, 0)
        recyclerView?.scrollBy(0, 0)
        recyclerView?.setRefreshListener {
            if (AppNetworkUtils.isConnected()) {
                page = 0
                presenter?.getHomeList(page)
            } else {
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
                        presenter?.getHomeList(page)
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
                    mBanner?.setHintPadding(0, 0, 0, AppSizeUtils.dp2px(mBanner?.context,10f))
                    mBanner?.setAdapter(BannerPagerAdapter(activity, bannerLists))
                    mBanner?.setOnBannerClickListener { position: Int ->
                        if(position>=0 && bannerLists.size>position){
                            val bannerBean = bean.data!![position]
                            WebViewActivity.lunch(activity, bannerBean.url, bannerBean.title)
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
        ToastUtils.showRoundRectToast("取消收藏成功")
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