package com.ycbjie.android.network

import com.ycbjie.android.model.bean.*
import io.reactivex.Observable
import org.json.JSONObject
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * <pre>
 *     @author 杨充
 *     blog  :
 *     time  : 2018/05/30
 *     desc  : kotlin学习
 *     revise: 请求接口封装
 *             关于flutter版本的玩Android项目，可以看：https://github.com/yangchong211/ycflutter
 * </pre>
 */
interface RequestApi {

    companion object {
        var HOST: String = "http://www.wanandroid.com/"
    }

    /**
     * 获取主页文章
     */
    @GET("/article/list/{page}/json")
    fun getHomeList(
            @Path("page") page: Int
    ): Observable<ResponseBean<HomeListBean>>

    /**
     * 获取首页banner数据
     */
    @GET("banner/json")
    fun getBanner(): Observable<ResponseBean<List<BannerBean>>>


    /**
     * 用户登陆
     */
    @POST("user/login")
    fun userLogin(
            @Query("username") userName: String?,
            @Query("password") password: String?
    ): Observable<ResponseBean<LoginBean>>

    /**
     * 用户注册
     */
    @POST("user/register")
    fun userRegister(
            @Query("username") userName: String?,
            @Query("password") password: String?,
            @Query("repassword") rePassword: String?
    ): Observable<ResponseBean<JSONObject>>

    /**
     * 获取知识树
     */
    @GET("tree/json")
    fun getKnowledgeTreeList(

    ): Observable<ResponseBean<List<TreeBean>>>


    /**
     * 获取项目树
     */
    @GET("project/tree/json")
    fun getProjectTree(

    ): Observable<ResponseBean<List<TreeBean>>>


    /**
     * 根据项目分类id获取项目列表
     */
    @GET("project/list/{page}/json")
    fun getProjectListByCid(
            @Path("page") page: Int,
            @Query("cid") cid: Int
    ): Observable<ResponseBean<ProjectListBean>>

    /**
     * 获取知识体系的文章
     */
    @GET("article/list/{page}/json")
    fun getKnowledgeList(@Path("page") page: Int,
                         @Query("cid") cid: Int): Observable<ResponseBean<ProjectListBean>>

    /**
     * 获取热词
     */
    @GET("hotkey/json")
    fun getRecommendSearchTag(): Observable<ResponseBean<MutableList<SearchTag>>>

    /**
     * 搜索
     */
    @POST("article/query/{page}/json")
    fun search(@Path("page") page: Int,
               @Query("k") text: String?): Observable<ResponseBean<ProjectListBean>>

    /**
     * 网址导航
     */
    @GET("navi/json")
    fun getNaviJson(): Observable<ResponseBean<MutableList<NaviBean>>>


    /**
     * 获取收藏的文章列表
     */
    @GET("lg/collect/list/{page}/json")
    fun getCollectArticleList(@Path("page") page: Int): Observable<ResponseBean<ProjectListBean>>

    /**
     * 获取收藏的网站列表
     */
    @GET("lg/collect/usertools/json")
    fun getCollectWebList(): Observable<ResponseBean<MutableList<SearchTag>>>


    /**
     * 收藏站内文章
     */
    @POST("lg/collect/{id}/json")
    fun collectInArticle(@Path("id") id: Int): Observable<ResponseBean<JSONObject>>


    /**
     * 收藏站外文章
     */
    @POST("lg/collect/add/json")
    fun collectOutArticle(@Query("title") title: String?,
                          @Query("author") author: String?,
                          @Query("link") link: String?): Observable<ResponseBean<JSONObject>>

    /**
     * 取消收藏
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun unCollectArticle(@Path("id") id: Int): Observable<ResponseBean<JSONObject>>

    /**
     * 收藏网站
     */
    @POST("lg/collect/addtool/json")
    fun collectWebsite(@Query("name") name: String?,
                       @Query("link") link: String?): Observable<ResponseBean<JSONObject>>

    /**
     * 取消网站收藏
     */
    @POST("lg/collect/deletetool/json")
    fun unCollectWebsite(@Query("id") id: Int): Observable<ResponseBean<JSONObject>>

    /**
     * 编辑收藏的网站
     */
    @POST("lg/collect/updatetool/json")
    fun updateWebsite(@Query("id") id: String?,
                      @Query("name") name: String?,
                      @Query("link") link: String?): Observable<ResponseBean<JSONObject>>


}