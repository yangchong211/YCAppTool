package com.ns.yc.lifehelper.api.http.bookReader;

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

import retrofit2.http.GET;
import retrofit2.http.Path;
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
public interface BookReaderApi {

    /**
     * http://api.zhuishushenqi.com/book-list?duration=last-seven-days&sort=collectorCount&start=0&limit=20&gender=male
     * 获取主题书单列表
     * 本周最热：duration=last-seven-days&sort=collectorCount
     * 最新发布：duration=all&sort=created
     * 最多收藏：duration=all&sort=collectorCount
     *
     * @param tag    都市、古代、架空、重生、玄幻、网游
     * @param gender male、female
     * @param limit  20
     * @return
     */
    @GET("/book-list")
    Observable<ReaderSubjectBean> getBookLists(@Query("duration") String duration,
                                               @Query("sort") String sort,
                                               @Query("start") String start,
                                               @Query("limit") String limit,
                                               @Query("tag") String tag,
                                               @Query("gender") String gender);


    /**
     * 获取所有排行榜
     *
     * @return
     */
    @GET("/ranking/gender")
    Observable<ReaderTopBookBean> getTopRanking();


    /**
     * http://api.zhuishushenqi.com/ranking/582ed5fc93b7e855163e707d
     * 获取单一排行榜
     * 周榜：rankingId->_id
     * 月榜：rankingId->monthRank
     * 总榜：rankingId->totalRank
     *
     * @return
     */
    @GET("/ranking/{rankingId}")
    Observable<SubHomeTopBean> getRanking(@Path("rankingId") String rankingId);


    /**
     * 获取分类
     *
     * @return
     */
    @GET("/cats/lv2/statistics")
    Observable<ReaderCategoryBean> getCategoryList();


    /**
     * http://api.zhuishushenqi.com/book/by-categories?gender=male&type=hot&major=%E7%8E%84%E5%B9%BB&minor=&start=0&limit=20
     * 按分类获取书籍列表
     * @param gender male、female
     * @param type   hot(热门)、new(新书)、reputation(好评)、over(完结)
     * @param major  玄幻
     * @param minor  东方玄幻、异界大陆、异界争霸、远古神话
     * @param limit  50
     * @return
     */
    @GET("/book/by-categories")
    Observable<ReaderCategoryList> getBooksByCats(@Query("gender") String gender,
                                                  @Query("type") String type,
                                                  @Query("major") String major,
                                                  @Query("minor") String minor,
                                                  @Query("start") int start,
                                                  @Query("limit") int limit);


    /**
     * 详情内容
     * @param bookId
     * @return
     */
    @GET("/book/{bookId}")
    Observable<ReaderDetailBook> getBookDetail(@Path("bookId") String bookId);

    /**
     * 热门评论
     *
     * @param book
     * @return
     */
    @GET("/post/review/best-by-book")
    Observable<ReaderDetailReviews> getHotReview(@Query("book") String book);


    /**
     * 热门书籍
     *
     * @param bookId
     * @param limit
     * @return
     */
    @GET("/book-list/{bookId}/recommend")
    Observable<ReaderDetailRecommend> getRecommendBookList(@Path("bookId") String bookId,
                                                           @Query("limit") String limit);

    /**
     * 获取综合讨论区帖子列表
     * 全部、默认排序  http://api.zhuishushenqi.com/post/by-block?block=ramble&duration=all&sort=updated&type=all&start=0&limit=20&distillate=
     * 精品、默认排序  http://api.zhuishushenqi.com/post/by-block?block=ramble&duration=all&sort=updated&type=all&start=0&limit=20&distillate=true
     *
     * @param block      ramble:综合讨论区
     *                   original：原创区
     * @param duration   all
     * @param sort       updated(默认排序)
     *                   created(最新发布)
     *                   comment-count(最多评论)
     * @param type       all
     * @param start      0
     * @param limit      20
     * @param distillate true(精品)
     * @return
     */
    @GET("/post/by-block")
    Observable<ReaderBookDiscussionList> getBookDiscussionList(@Query("block") String block,
                                                               @Query("duration") String duration,
                                                               @Query("sort") String sort,
                                                               @Query("type") String type,
                                                               @Query("start") String start,
                                                               @Query("limit") String limit,
                                                               @Query("distillate") String distillate);


    /**
     * 获取书评区帖子列表
     * 全部、全部类型、默认排序  http://api.zhuishushenqi.com/post/review?duration=all&sort=updated&type=all&start=0&limit=20&distillate=
     * 精品、玄幻奇幻、默认排序  http://api.zhuishushenqi.com/post/review?duration=all&sort=updated&type=xhqh&start=0&limit=20&distillate=true
     *
     * @param duration   all
     * @param sort       updated(默认排序)
     *                   created(最新发布)
     *                   helpful(最有用的)
     *                   comment-count(最多评论)
     * @param type       all(全部类型)、xhqh(玄幻奇幻)、dsyn(都市异能)...
     * @param start      0
     * @param limit      20
     * @param distillate true(精品) 、空字符（全部）
     * @return
     */
    @GET("/post/review")
    Observable<ReaderBookReviewList> getBookReviewList(@Query("duration") String duration,
                                                       @Query("sort") String sort,
                                                       @Query("type") String type,
                                                       @Query("start") String start,
                                                       @Query("limit") String limit,
                                                       @Query("distillate") String distillate);


    /**
     * 获取书荒区帖子列表
     * 全部、默认排序  http://api.zhuishushenqi.com/post/help?duration=all&sort=updated&start=0&limit=20&distillate=
     * 精品、默认排序  http://api.zhuishushenqi.com/post/help?duration=all&sort=updated&start=0&limit=20&distillate=true
     *
     * @param duration   all
     * @param sort       updated(默认排序)
     *                   created(最新发布)
     *                   comment-count(最多评论)
     * @param start      0
     * @param limit      20
     * @param distillate true(精品) 、空字符（全部）
     * @return
     */
    @GET("/post/help")
    Observable<ReaderBookHelpList> getBookHelpList(@Query("duration") String duration,
                                                   @Query("sort") String sort,
                                                   @Query("start") String start,
                                                   @Query("limit") String limit,
                                                   @Query("distillate") String distillate);


    /**
     * 获取综合讨论区帖子详情
     *
     * @param disscussionId->_id
     * @return
     */
    @GET("/post/{disscussionId}")
    Observable<DetailDiscussionContent> getBookDiscussionDetail(@Path("disscussionId") String disscussionId);

    /**
     * 获取神评论列表(综合讨论区、书评区、书荒区皆为同一接口)
     *
     * @param disscussionId->_id
     * @return
     */
    @GET("/post/{disscussionId}/comment/best")
    Observable<DetailDiscussionComment> getBestComments(@Path("disscussionId") String disscussionId);

    /**
     * 获取综合讨论区帖子详情内的评论列表
     *
     * @param disscussionId->_id
     * @param start              0
     * @param limit              30
     * @return
     */
    @GET("/post/{disscussionId}/comment")
    Observable<DetailDiscussionComment> getBookDiscussionComments(@Path("disscussionId") String disscussionId,
                                                                   @Query("start") String start,
                                                                   @Query("limit") String limit);


    /**
     * 获取书荒区帖子详情
     *
     * @param helpId->_id
     * @return
     */
    @GET("/post/help/{helpId}")
    Observable<DetailBookHelp> getBookHelpDetail(@Path("helpId") String helpId);

    /**
     * 获取书评区、书荒区帖子详情内的评论列表
     *
     * @param bookReviewId->_id
     * @param start             0
     * @param limit             30
     * @return
     */
    @GET("/post/review/{bookReviewId}/comment")
    Observable<DetailDiscussionComment> getBookReviewComments(@Path("bookReviewId") String bookReviewId,
                                                              @Query("start") String start,
                                                              @Query("limit") String limit);


    /**
     * 获取书单详情
     * @return
     */
    @GET("/book-list/{bookListId}")
    Observable<DetailBookSubjectList> getBookListDetail(@Path("bookListId") String bookListId);

}
