package com.ns.yc.lifehelper.ui.other.bookReader.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantZssqApi;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.inter.listener.OnRvItemClickListener;
import com.ns.yc.lifehelper.ui.other.bookReader.view.adapter.GlideRoundTransform;
import com.ns.yc.lifehelper.ui.other.bookReader.view.adapter.HotReviewAdapter;
import com.ns.yc.lifehelper.ui.other.bookReader.view.adapter.RecommendBookListAdapter;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderDetailBook;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderDetailRecommend;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderDetailReviews;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.Recommend;
import com.ns.yc.lifehelper.ui.other.bookReader.manager.CollectionsManager;
import com.ns.yc.lifehelper.api.http.bookReader.BookReaderModel;
import com.ns.yc.lifehelper.weight.DrawableCenterButton;
import com.ns.yc.lifehelper.weight.manager.FullyLinearLayoutManager;
import com.ns.yc.lifehelper.utils.AppUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：22017/9/21
 * 描    述：小说阅读器详情页面
 * 修订历史：
 * ================================================
 */
public class ReadBookDetailActivity extends BaseActivity implements View.OnClickListener , OnRvItemClickListener<Object>{

    @BindView(R.id.tv_title_left)
    TextView tvTitleLeft;
    @BindView(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.iv_right_img)
    ImageView ivRightImg;
    @BindView(R.id.ll_search)
    FrameLayout llSearch;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_book_cover)
    ImageView ivBookCover;
    @BindView(R.id.tv_book_list_title)
    TextView tvBookListTitle;
    @BindView(R.id.tv_book_list_author)
    TextView tvBookListAuthor;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.tv_word_count)
    TextView tvWordCount;
    @BindView(R.id.tv_lately_update)
    TextView tvLatelyUpdate;
    @BindView(R.id.btn_join_collection)
    DrawableCenterButton btnJoinCollection;
    @BindView(R.id.btn_read)
    DrawableCenterButton btnRead;
    @BindView(R.id.tv_lately_follower)
    TextView tvLatelyFollower;
    @BindView(R.id.tv_retention_ratio)
    TextView tvRetentionRatio;
    @BindView(R.id.tv_serialize_word_count)
    TextView tvSerializeWordCount;
    @BindView(R.id.tfl_tag)
    TagFlowLayout tflTag;
    @BindView(R.id.tv_long_intro)
    TextView tvLongIntro;
    @BindView(R.id.tv_more_review)
    TextView tvMoreReview;
    @BindView(R.id.rv_hot_review)
    RecyclerView rvHotReview;
    @BindView(R.id.tv_community)
    TextView tvCommunity;
    @BindView(R.id.tv_helpful_yes)
    TextView tvHelpfulYes;
    @BindView(R.id.rl_community)
    RelativeLayout rlCommunity;
    @BindView(R.id.tv_recommend_book_list)
    TextView tvRecommendBookList;
    @BindView(R.id.rv_recommend_book_list)
    RecyclerView rvRecommendBookList;
    private String id;

    private List<ReaderDetailReviews.ReviewsBean> mHotReviewList = new ArrayList<>();
    private List<ReaderDetailRecommend.BooklistsBean> mRecommendBookList = new ArrayList<>();
    private HotReviewAdapter hotAdapter;
    private RecommendBookListAdapter reccommendAdapter;
    private Recommend.RecommendBooks recommendBooks;
    private boolean isJoinedCollections = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_reader_book_detail;
    }

    @Override
    public void initView() {
        initIntentData();
        initToolBar();
        initRecycleView();
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if(intent!=null){
            id = intent.getStringExtra("id");
        }
    }

    private void initToolBar() {
        toolbarTitle.setText("书记详情");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getBookDetail(id);
        getHotReview(id);
        getRecommendBookList(id, "3");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position, Object data) {

    }

    private void initRecycleView() {
        rvHotReview.setHasFixedSize(true);
        rvHotReview.setLayoutManager(new FullyLinearLayoutManager(this));
        hotAdapter = new HotReviewAdapter(ReadBookDetailActivity.this, mHotReviewList, this);
        rvHotReview.setAdapter(hotAdapter);

        rvRecommendBookList.setHasFixedSize(true);
        rvRecommendBookList.setLayoutManager(new FullyLinearLayoutManager(this));
        reccommendAdapter = new RecommendBookListAdapter(ReadBookDetailActivity.this, mRecommendBookList, this);
        rvRecommendBookList.setAdapter(reccommendAdapter);
    }



    /**
     * 获取书的详情内容
     * @param id
     */
    private void getBookDetail(String id) {
        BookReaderModel model = BookReaderModel.getInstance(ReadBookDetailActivity.this);
        Subscription rxSubscription = model.getBookDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReaderDetailBook>() {
                    @Override
                    public void onNext(ReaderDetailBook data) {
                        initViewData(data);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }


    /**
     * 获取书的热门评论
     * @param id
     */
    private void getHotReview(String id) {
        BookReaderModel model = BookReaderModel.getInstance(ReadBookDetailActivity.this);
        Subscription rxSubscription = model.getHotReview(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReaderDetailReviews>() {
                    @Override
                    public void onNext(ReaderDetailReviews data) {
                        if(data!=null && data.getReviews()!=null){
                            hotAdapter.addAll(data.getReviews());
                            hotAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    /**
     * 获取书的回复
     * @param id
     */
    private void getRecommendBookList(String id, String limit) {
        BookReaderModel model = BookReaderModel.getInstance(ReadBookDetailActivity.this);
        Subscription rxSubscription = model.getRecommendBookList(id, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReaderDetailRecommend>() {
                    @Override
                    public void onNext(ReaderDetailRecommend data) {
                        if(data!=null && data.getBooklists()!=null){
                            reccommendAdapter.addAll(data.getBooklists());
                            reccommendAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }



    private ArrayList<String> tagList = new ArrayList<>();
    private void initViewData(ReaderDetailBook data) {
        Glide.with(ReadBookDetailActivity.this)
                .load(ConstantZssqApi.IMG_BASE_URL + data.getCover())
                .placeholder(R.drawable.cover_default)
                .transform(new GlideRoundTransform(ReadBookDetailActivity.this))
                .into(ivBookCover);
        tvBookListTitle.setText(data.getTitle());
        tvBookListAuthor.setText(String.format(getString(R.string.book_detail_author), data.getAuthor()));
        tvCategory.setText(String.format(getString(R.string.book_detail_category), data.getCat()));
        tvWordCount.setText(AppUtil.formatWordCount(data.getWordCount()));
        //tvLatelyUpdate.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(data.getUpdated())));
        tvLatelyUpdate.setText(data.getUpdated());
        tvLatelyFollower.setText(String.valueOf(data.getLatelyFollower()));
        tvRetentionRatio.setText(TextUtils.isEmpty(data.getRetentionRatio()) ? "-" : String.format(getString(R.string.book_detail_retention_ratio), data.getRetentionRatio()));
        tvSerializeWordCount.setText(data.getSerializeWordCount() < 0 ? "-" : String.valueOf(data.getSerializeWordCount()));

        tvLongIntro.setText(data.getLongIntro());
        tvCommunity.setText(String.format(getString(R.string.book_detail_community), data.getTitle()));
        tvHelpfulYes.setText(String.format(getString(R.string.book_detail_post_count), data.getPostCount()));

        tagList.clear();
        tagList.addAll(data.getTags());
        initFlowTabLayout(tagList);

        recommendBooks = new Recommend.RecommendBooks();
        recommendBooks.title = data.getTitle();
        recommendBooks._id = data.get_id();
        recommendBooks.cover = data.getCover();
        recommendBooks.lastChapter = data.getLastChapter();
        recommendBooks.updated = data.getUpdated();

        refreshCollectionIcon();
    }

    private void initFlowTabLayout(ArrayList<String> tagList) {
        tflTag.setAdapter(new TagAdapter<String>(tagList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(ReadBookDetailActivity.this).inflate(R.layout.tag_tv_view, tflTag, false);
                tv.setText(s);
                return tv;
            }
        });
        tflTag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                return true;
            }
        });
    }


    /**
     * 刷新收藏图标
     */
    private void refreshCollectionIcon() {
        if (CollectionsManager.getInstance().isCollected(recommendBooks._id)) {
            initCollection(false);
        } else {
            initCollection(true);
        }
    }

    private void initCollection(boolean coll) {
        if (coll) {
            btnJoinCollection.setText(R.string.book_detail_join_collection);
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.book_detail_info_add_img);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btnJoinCollection.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.shape_common_btn_solid_normal));
            btnJoinCollection.setCompoundDrawables(drawable, null, null, null);
            btnJoinCollection.postInvalidate();
            isJoinedCollections = false;
        } else {
            btnJoinCollection.setText(R.string.book_detail_remove_collection);
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.book_detail_info_del_img);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            btnJoinCollection.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.shape_common_btn_solid_pressed));
            btnJoinCollection.setCompoundDrawables(drawable, null, null, null);
            btnJoinCollection.postInvalidate();
            isJoinedCollections = true;
        }
    }


}
