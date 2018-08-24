package com.ns.yc.lifehelper.ui.other.bookReader.view.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantZssqApi;
import com.ns.yc.lifehelper.api.http.bookReader.BookReaderModel;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.inter.listener.OnRvItemClickListener;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.DetailDiscussionComment;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.DetailDiscussionContent;
import com.ns.yc.lifehelper.ui.other.bookReader.view.adapter.BookDetailCommentAdapter;
import com.ns.yc.lifehelper.ui.other.bookReader.view.adapter.BookDetailDiscussionAdapter;
import com.ns.yc.lifehelper.utils.image.ImageUtils;
import com.ns.yc.lifehelper.utils.time.FormatUtils;
import com.ns.yc.lifehelper.weight.readerBook.BookContentTextView;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/10/18
 * 描    述：综合讨论区详情页面
 * 修订历史：
 * ================================================
 */
public class BookDetailDiscussionActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.recyclerView)
    YCRefreshView recyclerView;
    private BookDetailDiscussionAdapter adapter;
    private HeaderViewHolder headerViewHolder;
    private String id;
    private int start = 0;
    private int limit = 20;

    private List<DetailDiscussionComment.CommentsBean> mBestCommentList = new ArrayList<>();
    private BookDetailCommentAdapter mBestCommentListAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle_bar;
    }

    @Override
    public void initView() {
        initIntent();
        initToolBar();
        initRecycleView();
    }

    private void initIntent() {
        if(getIntent()!=null){
            id = getIntent().getStringExtra("id");
        }else {
            id = "";
        }
    }

    private void initToolBar() {
        toolbarTitle.setText("综合讨论区详情页面");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }


    @Override
    public void initData() {
        getBookDiscussionDetail(id);
        getBestComments(id);
        getBookDiscussionComments(id, start, limit);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_menu:
                finish();
                break;
            default:
                break;
        }
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final RecycleViewItemLine line = new RecycleViewItemLine(this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        recyclerView.addItemDecoration(line);
        adapter = new BookDetailDiscussionAdapter(this);
        recyclerView.setAdapter(adapter);
        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        start = start + adapter.getAllData().size();
                        getBookDiscussionComments(id, start, limit);
                    } else {
                        adapter.pauseMore();
                    }
                } else {
                    adapter.pauseMore();
                    Toast.makeText(BookDetailDiscussionActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMoreClick() {

            }
        });

        //设置没有数据
        adapter.setNoMore(R.layout.view_recycle_no_more, new RecyclerArrayAdapter.OnNoMoreListener() {
            @Override
            public void onNoMoreShow() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    Toast.makeText(BookDetailDiscussionActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    Toast.makeText(BookDetailDiscussionActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //设置错误
        adapter.setError(R.layout.view_recycle_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });
        AddHeader();
    }

    private void AddHeader() {
        adapter.removeAllHeader();
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View view = LayoutInflater.from(BookDetailDiscussionActivity.this)
                        .inflate(R.layout.header_book_discussion_detail, parent, false);
                return view;
            }

            @Override
            public void onBindView(View headerView) {
                headerViewHolder = new HeaderViewHolder(headerView);
            }
        });
    }

    static class HeaderViewHolder {

        @BindView(R.id.ivBookCover)
        ImageView ivAvatar;
        @BindView(R.id.tvBookTitle)
        TextView tvNickName;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvContent)
        BookContentTextView tvContent;
        @BindView(R.id.tvBestComments)
        TextView tvBestComments;
        @BindView(R.id.rvBestComments)
        RecyclerView rvBestComments;
        @BindView(R.id.tvCommentCount)
        TextView tvCommentCount;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);           //view绑定
        }
    }

    /**
     * 获取综合讨论区帖子详情
     */
    private void getBookDiscussionDetail(String id) {
        BookReaderModel model = BookReaderModel.getInstance(this);
        Subscription rxSubscription = model.getBookDiscussionDetail(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DetailDiscussionContent>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DetailDiscussionContent content) {
                        if(content!=null && content.isOk()){
                            DetailDiscussionContent.PostBean post = content.getPost();
                            ImageUtils.loadImgByPicassoPerson(BookDetailDiscussionActivity.this,
                                    ConstantZssqApi.IMG_BASE_URL+post.getAuthor().getAvatar(),
                                    R.drawable.avatar_default,
                                    headerViewHolder.ivAvatar);
                            headerViewHolder.tvNickName.setText(post.getAuthor().getNickname());
                            headerViewHolder.tvTime.setText(FormatUtils.getDescriptionTimeFromDateString(post.getCreated()));
                            headerViewHolder.tvTitle.setText(post.getTitle());
                            headerViewHolder.tvContent.setText(post.getContent());
                            headerViewHolder.tvCommentCount.setText(String.format(BookDetailDiscussionActivity.this.getString(R.string.comment_comment_count), post.getCommentCount()));
                        }
                    }
                });
    }


    /**
     * 获取神评论列表
     */
    private void getBestComments(String id) {
        BookReaderModel model = BookReaderModel.getInstance(this);
        Subscription rxSubscription = model.getBestComments(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DetailDiscussionComment>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DetailDiscussionComment list) {
                        if(list!=null && list.isOk()){
                            if (list.getComments().isEmpty()) {
                                gone(headerViewHolder.tvBestComments, headerViewHolder.rvBestComments);
                            } else {
                                mBestCommentList.addAll(list.getComments());
                                headerViewHolder.rvBestComments.setHasFixedSize(true);
                                headerViewHolder.rvBestComments.setLayoutManager(new LinearLayoutManager(BookDetailDiscussionActivity.this));
                                RecycleViewItemLine line = new RecycleViewItemLine(BookDetailDiscussionActivity.this, LinearLayout.HORIZONTAL,
                                        SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
                                headerViewHolder.rvBestComments.addItemDecoration(line);
                                mBestCommentListAdapter = new BookDetailCommentAdapter(BookDetailDiscussionActivity.this, mBestCommentList);
                                mBestCommentListAdapter.setOnItemClickListener(new OnRvItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position, Object data) {

                                    }
                                });
                                headerViewHolder.rvBestComments.setAdapter(mBestCommentListAdapter);
                                visible(headerViewHolder.tvBestComments, headerViewHolder.rvBestComments);
                            }
                        }
                    }
                });
    }

    /**
     * 获取综合讨论区帖子详情内的评论列表
     */
    private void getBookDiscussionComments(String id, final int start, final int limit) {
        BookReaderModel model = BookReaderModel.getInstance(BookDetailDiscussionActivity.this);
        Subscription rxSubscription = model.getBookDiscussionComments(id, start + "", limit + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DetailDiscussionComment>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(DetailDiscussionComment list) {
                        if(list!=null && list.isOk()){
                            if(adapter==null){
                                adapter = new BookDetailDiscussionAdapter(BookDetailDiscussionActivity.this);
                            }

                            if(start==0){
                                if(list.isOk()){
                                    if(list.getComments()!=null && list.getComments().size()>0){
                                        recyclerView.showRecycler();
                                        adapter.clear();
                                        adapter.addAll(list.getComments());
                                        adapter.notifyDataSetChanged();
                                        recyclerView.setRefreshing(false);
                                    }else {
                                        recyclerView.setRefreshing(false);
                                    }
                                }
                            }else {
                                if(list.isOk()){
                                    if(list.getComments()!=null && list.getComments().size()>0){
                                        recyclerView.showRecycler();
                                        adapter.addAll(list.getComments());
                                        adapter.notifyDataSetChanged();
                                    }else {
                                        adapter.stopMore();
                                    }
                                }
                            }
                        }
                    }
                });
    }

    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }


}
