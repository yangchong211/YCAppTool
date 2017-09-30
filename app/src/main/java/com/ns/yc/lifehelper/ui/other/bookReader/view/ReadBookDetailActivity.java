package com.ns.yc.lifehelper.ui.other.bookReader.view;

import android.content.Intent;
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

import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.ConstantZssqApi;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.listener.OnRvItemClickListener;
import com.ns.yc.lifehelper.ui.other.bookReader.adapter.GlideRoundTransform;
import com.ns.yc.lifehelper.ui.other.bookReader.adapter.HotReviewAdapter;
import com.ns.yc.lifehelper.ui.other.bookReader.adapter.RecommendBookListAdapter;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderDetailBook;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderDetailRecommend;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderDetailReviews;
import com.ns.yc.lifehelper.ui.other.bookReader.model.BookReaderModel;
import com.ns.yc.lifehelper.ui.weight.DrawableCenterButton;
import com.ns.yc.lifehelper.ui.weight.manager.FullyLinearLayoutManager;
import com.ns.yc.lifehelper.utils.AppUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
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

    @Bind(R.id.tv_title_left)
    TextView tvTitleLeft;
    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_right_img)
    ImageView ivRightImg;
    @Bind(R.id.ll_search)
    FrameLayout llSearch;
    @Bind(R.id.tv_title_right)
    TextView tvTitleRight;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_book_cover)
    ImageView ivBookCover;
    @Bind(R.id.tv_book_list_title)
    TextView tvBookListTitle;
    @Bind(R.id.tv_book_list_author)
    TextView tvBookListAuthor;
    @Bind(R.id.tv_category)
    TextView tvCategory;
    @Bind(R.id.tv_word_count)
    TextView tvWordCount;
    @Bind(R.id.tv_lately_update)
    TextView tvLatelyUpdate;
    @Bind(R.id.btn_join_collection)
    DrawableCenterButton btnJoinCollection;
    @Bind(R.id.btn_read)
    DrawableCenterButton btnRead;
    @Bind(R.id.tv_lately_follower)
    TextView tvLatelyFollower;
    @Bind(R.id.tv_retention_ratio)
    TextView tvRetentionRatio;
    @Bind(R.id.tv_serialize_word_count)
    TextView tvSerializeWordCount;
    @Bind(R.id.tfl_tag)
    TagFlowLayout tflTag;
    @Bind(R.id.tv_long_intro)
    TextView tvLongIntro;
    @Bind(R.id.tv_more_review)
    TextView tvMoreReview;
    @Bind(R.id.rv_hot_review)
    RecyclerView rvHotReview;
    @Bind(R.id.tv_community)
    TextView tvCommunity;
    @Bind(R.id.tv_helpful_yes)
    TextView tvHelpfulYes;
    @Bind(R.id.rl_community)
    RelativeLayout rlCommunity;
    @Bind(R.id.tv_recommend_book_list)
    TextView tvRecommendBookList;
    @Bind(R.id.rv_recommend_book_list)
    RecyclerView rvRecommendBookList;
    private String id;
    private ReadBookDetailActivity activity;

    private List<ReaderDetailReviews.ReviewsBean> mHotReviewList = new ArrayList<>();
    private List<ReaderDetailRecommend.BooklistsBean> mRecommendBookList = new ArrayList<>();
    private HotReviewAdapter hotAdapter;
    private RecommendBookListAdapter reccommendAdapter;

    @Override
    public int getContentView() {
        return R.layout.activity_reader_book_detail;
    }

    @Override
    public void initView() {
        activity = ReadBookDetailActivity.this;
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
        hotAdapter = new HotReviewAdapter(activity, mHotReviewList, this);
        rvHotReview.setAdapter(hotAdapter);

        rvRecommendBookList.setHasFixedSize(true);
        rvRecommendBookList.setLayoutManager(new FullyLinearLayoutManager(this));
        reccommendAdapter = new RecommendBookListAdapter(activity, mRecommendBookList, this);
        rvRecommendBookList.setAdapter(reccommendAdapter);
    }



    /**
     * 获取书的详情内容
     * @param id
     */
    private void getBookDetail(String id) {
        BookReaderModel model = BookReaderModel.getInstance(activity);
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
        BookReaderModel model = BookReaderModel.getInstance(activity);
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
        BookReaderModel model = BookReaderModel.getInstance(activity);
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
        Glide.with(activity)
                .load(ConstantZssqApi.IMG_BASE_URL + data.getCover())
                .placeholder(R.drawable.cover_default)
                .transform(new GlideRoundTransform(activity))
                .into(ivBookCover);
        tvBookListTitle.setText(data.getTitle());
        tvBookListAuthor.setText(String.format(getString(R.string.book_detail_author), data.getAuthor()));
        tvCategory.setText(String.format(getString(R.string.book_detail_category), data.getCat()));
        tvWordCount.setText(AppUtil.formatWordCount(data.getWordCount()));
        tvLatelyUpdate.setText(TimeUtils.getFriendlyTimeSpanByNow(Long.parseLong(data.getUpdated())));
        tvLatelyFollower.setText(String.valueOf(data.getLatelyFollower()));
        tvRetentionRatio.setText(TextUtils.isEmpty(data.getRetentionRatio()) ? "-" : String.format(getString(R.string.book_detail_retention_ratio), data.getRetentionRatio()));
        tvSerializeWordCount.setText(data.getSerializeWordCount() < 0 ? "-" : String.valueOf(data.getSerializeWordCount()));

        tvLongIntro.setText(data.getLongIntro());
        tvCommunity.setText(String.format(getString(R.string.book_detail_community), data.getTitle()));
        tvHelpfulYes.setText(String.format(getString(R.string.book_detail_post_count), data.getPostCount()));

        tagList.clear();
        tagList.addAll(data.getTags());
        initFlowTabLayout(tagList);

    }

    private void initFlowTabLayout(ArrayList<String> tagList) {
        tflTag.setAdapter(new TagAdapter<String>(tagList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(activity).inflate(R.layout.tag_hot, tflTag, false);
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


}
