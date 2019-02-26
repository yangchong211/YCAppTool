package com.ycbjie.android.model.helper

import com.ycbjie.android.model.bean.*
import com.ycbjie.android.network.AddCookieInterceptor
import com.ycbjie.android.network.GetCookieInterceptor
import com.ycbjie.android.network.RequestApi
import com.ycbjie.android.network.ResponseBean
import com.ycbjie.library.http.RetrofitWrapper
import io.reactivex.Observable
import okhttp3.Interceptor
import org.json.JSONObject


class AndroidHelper private constructor() {

    private var mApiService : RequestApi ?= null
    private var interceptor = ArrayList<Interceptor>()

    init {
        interceptor.add(AddCookieInterceptor())
        interceptor.add(GetCookieInterceptor())
        mApiService = RetrofitWrapper
                .getInstance(RequestApi.HOST,interceptor)
                .create(RequestApi::class.java)
    }


    companion object {
        private var model: AndroidHelper? = null
        // 初始化单例
        fun instance() : AndroidHelper {
            if (model == null) {
                model = AndroidHelper()
            }
            return model as AndroidHelper
        }
    }

    /**
     * 获取主页文章
     */
    fun getHomeList(page: Int): Observable<ResponseBean<HomeListBean>> {
        return mApiService!!.getHomeList(page)
    }

    /**
     * 获取首页banner数据
     */
    fun getBanner(): Observable<ResponseBean<List<BannerBean>>> {
        return mApiService!!.getBanner()
    }


    /**
     * 获取知识树
     */
    fun getKnowledgeTree(): Observable<ResponseBean<List<TreeBean>>> {
        return mApiService!!.getKnowledgeTreeList()
    }


    /**
     * 获取项目树
     */
    fun getProjectTree(): Observable<ResponseBean<List<TreeBean>>> {
        return mApiService!!.getProjectTree()
    }


    /**
     * 根据项目分类id获取项目列表
     */
    fun getProjectListByCid(page: Int, cid: Int): Observable<ResponseBean<ProjectListBean>> {
        return mApiService!!.getProjectListByCid(page, cid)
    }


    /**
     * 获取知识体系的文章
     */
    fun getKnowledgeList(page: Int, cid: Int): Observable<ResponseBean<ProjectListBean>> {
        return mApiService!!.getKnowledgeList(page, cid)
    }

    /**
     * 网址导航
     */
    fun getNaviJson(): Observable<ResponseBean<MutableList<NaviBean>>> {
        return mApiService!!.getNaviJson()
    }

    /**
     * 取消收藏
     */
    fun unCollectArticle(selectId: Int): Observable<ResponseBean<JSONObject>> {
        return mApiService!!.unCollectArticle(selectId)
    }

    /**
     * 取消收藏
     */
    fun collectInArticle(selectId: Int): Observable<ResponseBean<JSONObject>> {
        return mApiService!!.collectInArticle(selectId)
    }

    /**
     * 收藏网站
     */
    fun collectWebsite(name: String,link: String): Observable<ResponseBean<JSONObject>> {
        return mApiService!!.collectWebsite(name,link)
    }

    /**
     * 获取热词
     */
    fun getRecommendSearchTag(): Observable<ResponseBean<MutableList<SearchTag>>> {
        return mApiService!!.getRecommendSearchTag()
    }


    /**
     * 搜索
     */
    fun search(page: Int, text: String): Observable<ResponseBean<ProjectListBean>> {
        return mApiService!!.search(page, text)
    }

    /**
     * 登陆
     */
    fun login(name: String, pwd: String): Observable<ResponseBean<LoginBean>> {
        return mApiService!!.userLogin(name, pwd)
    }


    /**
     * 获取收藏的文章列表
     */
    fun getCollectArticleList(page: Int): Observable<ResponseBean<ProjectListBean>> {
        return mApiService!!.getCollectArticleList(page)
    }


    /**
     * 获取收藏的网站列表
     */
    fun getCollectWebList(): Observable<ResponseBean<MutableList<SearchTag>>> {
        return mApiService!!.getCollectWebList()
    }



}
