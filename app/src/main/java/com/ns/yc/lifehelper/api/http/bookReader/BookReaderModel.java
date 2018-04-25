package com.ns.yc.lifehelper.api.http.bookReader;

import android.content.Context;

import com.ns.yc.lifehelper.api.constantApi.ConstantZssqApi;
import com.ns.yc.lifehelper.api.RetrofitWrapper;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.DetailBookHelp;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.DetailBookSubjectList;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.DetailDiscussionComment;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.DetailDiscussionContent;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderBookDiscussionList;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderBookHelpList;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderBookReviewList;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderCategoryBean;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderCategoryList;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderDetailBook;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderDetailRecommend;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderDetailReviews;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderSubjectBean;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderTopBookBean;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.SubHomeTopBean;

import retrofit2.http.Query;
import rx.Observable;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/18
 * 描    述：接口
 * 修订历史：
 * ================================================
 */
public class BookReaderModel {

    public static BookReaderModel instance;

    private BookReaderApi service;

    public BookReaderModel(Context context) {
        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantZssqApi.API_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())   // 添加Rx适配器
                .addConverterFactory(GsonConverterFactory.create())         // 添加Gson转换器
                .client(okHttpClient)
                .build();
        service = retrofit.create(BookReaderApi.class);*/

        service = RetrofitWrapper
                .getInstance(ConstantZssqApi.API_BASE_URL)
                .create(BookReaderApi.class);
    }

    public static BookReaderModel getInstance(Context context){
        if(instance == null) {
            instance = new BookReaderModel(context);
        }
        return instance;
    }

    /**
     * 获取主题书单列表
     * @param duration
     * @param sort
     * @param start
     * @param limit
     * @param tag
     * @param gender
     * @return
     */
    public Observable<ReaderSubjectBean> getBookLists(String duration, String sort, String start, String limit, String tag, String gender) {
        return service.getBookLists(duration, sort, start, limit, tag, gender);
    }

    /**
     * 获取排行榜数据
     * @return
     */
    public Observable<ReaderTopBookBean> getTopRanking() {
        return service.getTopRanking();
    }

    /**
     * 获取排行榜二级目录list数据
     * @param rankingId
     * @return
     */
    public Observable<SubHomeTopBean> getRanking(String rankingId) {
        return service.getRanking(rankingId);
    }

    /**
     * 获取分类列表数据
     * @return
     */
    public synchronized Observable<ReaderCategoryBean> getCategoryList() {
        return service.getCategoryList();
    }

    /**
     * 按分类获取书籍列表
     *
     * @param gender male、female
     * @param type   hot(热门)、new(新书)、reputation(好评)、over(完结)
     * @param major  玄幻
     * @param minor  东方玄幻、异界大陆、异界争霸、远古神话
     * @param limit  50
     * @return
     */
    public Observable<ReaderCategoryList> getBooksByCats(String gender, String type, String major, String minor, int start, @Query("limit") int limit) {
        return service.getBooksByCats(gender, type, major, minor, start, limit);
    }

    /**
     * 详情内容
     * @param bookId
     * @return
     */
    public Observable<ReaderDetailBook> getBookDetail(String bookId) {
        return service.getBookDetail(bookId);
    }

    /**
     * 热门评论
     *
     * @param book
     * @return
     */
    public Observable<ReaderDetailReviews> getHotReview(String book) {
        return service.getHotReview(book);
    }

    /**
     * 热门书籍
     * @param bookId
     * @param limit
     * @return
     */
    public Observable<ReaderDetailRecommend> getRecommendBookList(String bookId, String limit) {
        return service.getRecommendBookList(bookId, limit);
    }

    /**
     * 综合讨论区
     * @param block
     * @param duration
     * @param sort
     * @param type
     * @param start
     * @param limit
     * @param distillate
     * @return
     */
    public Observable<ReaderBookDiscussionList> getBookDiscussionList(String block, String duration, String sort, String type, String start, String limit, String distillate) {
        return service.getBookDiscussionList(block, duration, sort, type, start, limit, distillate);
    }

    /**
     * 书评区
     * @param duration
     * @param sort
     * @param type
     * @param start
     * @param limit
     * @param distillate
     * @return
     */
    public Observable<ReaderBookReviewList> getBookReviewList(String duration, String sort, String type, String start, String limit, String distillate) {
        return service.getBookReviewList(duration, sort, type, start, limit, distillate);
    }

    /**
     * 书荒互助区
     * @param duration
     * @param sort
     * @param start
     * @param limit
     * @param distillate
     * @return
     */
    public Observable<ReaderBookHelpList> getBookHelpList(String duration, String sort, String start, String limit, String distillate) {
        return service.getBookHelpList(duration, sort, start, limit, distillate);
    }

    /**
     * 女生区
     * @param block
     * @param duration
     * @param sort
     * @param type
     * @param start
     * @param limit
     * @param distillate
     * @return
     */
    public Observable<ReaderBookDiscussionList> getBookGirlList(String block, String duration, String sort, String type, String start, String limit, String distillate) {
        return service.getBookDiscussionList(block, duration, sort, type, start, limit, distillate);
    }

    /**
     * 获取综合讨论区帖子详情
     * @param id
     * @return
     */
    public Observable<DetailDiscussionContent> getBookDiscussionDetail(String id) {
        return service.getBookDiscussionDetail(id);
    }

    /**
     * 获取神评论列表
     */
    public Observable<DetailDiscussionComment> getBestComments(String id) {
        return service.getBestComments(id);
    }

    /**
     * 获取综合讨论区帖子详情内的评论列表
     */
    public Observable<DetailDiscussionComment> getBookDiscussionComments(String disscussionId, String start, String limit) {
        return service.getBookDiscussionComments(disscussionId, start, limit);
    }

    /**
     * 获取书荒区帖子详情
     */
    public Observable<DetailBookHelp> getBookHelpDetail(String helpId) {
        return service.getBookHelpDetail(helpId);
    }

    /**
     *  获取书评区、书荒区帖子详情内的评论列表
     */
    public Observable<DetailDiscussionComment> getBookReviewComments(String bookReviewId, String start, String limit) {
        return service.getBookReviewComments(bookReviewId, start, limit);
    }

    /**
     * 获取书单详情
     */
    public Observable<DetailBookSubjectList> getBookListDetail(String bookListId) {
        return service.getBookListDetail(bookListId);
    }

}
