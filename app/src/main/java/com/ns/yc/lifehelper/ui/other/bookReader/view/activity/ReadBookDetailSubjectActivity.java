package com.ns.yc.lifehelper.ui.other.bookReader.view.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.ConstantZssqApi;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.view.adapter.BookDetailSubjectAdapter;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.DetailBookSubjectList;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderSubjectBean;
import com.ns.yc.lifehelper.ui.other.bookReader.model.BookReaderModel;
import com.ns.yc.lifehelper.utils.DoShareUtils;
import com.ns.yc.lifehelper.utils.ImageUtils;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：22017/10/21
 * 描    述：小说阅读器主题书单详情页面
 * 修订历史：
 * ================================================
 */
public class ReadBookDetailSubjectActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    private BookDetailSubjectAdapter adapter;

    private int start = 0;
    private int limit = 20;
    private ReaderSubjectBean.BookListsBean bookListsBean;

    private List<DetailBookSubjectList.BookListBean.BooksBean> mAllBooks = new ArrayList<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.base_refresh_recycle_bar;
    }

    @Override
    public void initView() {
        initToolBar();
        initIntentData();
        initRecycleView();
    }

    private void initIntentData() {
        bookListsBean = (ReaderSubjectBean.BookListsBean) getIntent().getSerializableExtra("bookListsBean");
    }

    private void initToolBar() {
        toolbarTitle.setText("书单详情页面");
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
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(adapter.getAllData().size()>position && position>=0){
                    Intent intent = new Intent(ReadBookDetailSubjectActivity.this,ReadBookDetailActivity.class);
                    intent.putExtra("id",adapter.getAllData().get(position).getBook().get_id());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void initData() {
        recyclerView.showProgress();
        getBookListDetail(bookListsBean.get_id());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_reader_subject_collect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_note:             //收藏

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(ReadBookDetailSubjectActivity.this));
        adapter = new BookDetailSubjectAdapter(ReadBookDetailSubjectActivity.this);
        recyclerView.setAdapter(adapter);
        /*final RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), activity.getResources().getColor(R.color.grayLine));
        recyclerView.addItemDecoration(line);*/
        initHeader();
        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadNextPage();
                            }
                        }, 500);
                    } else {
                        adapter.pauseMore();
                    }
                } else {
                    adapter.pauseMore();
                    Toast.makeText(ReadBookDetailSubjectActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ReadBookDetailSubjectActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    Toast.makeText(ReadBookDetailSubjectActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
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

        //刷新
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected()) {
                    getBookListDetail(bookListsBean.get_id());
                } else {
                    recyclerView.setRefreshing(false);
                    Toast.makeText(ReadBookDetailSubjectActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 添加头布局
     */
    private void initHeader() {
        adapter.removeAllHeader();
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View view = LayoutInflater.from(ReadBookDetailSubjectActivity.this).inflate(R.layout.header_view_book_list_detail, parent, false);
                return view;
            }

            @Override
            public void onBindView(View headerView) {
                headerViewHolder = new HeaderViewHolder(headerView);
                headerViewHolder.btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DoShareUtils.shareText(ReadBookDetailSubjectActivity.this,"分享","书单详情页面");
                    }
                });
            }
        });
    }

    private HeaderViewHolder headerViewHolder;
    static class HeaderViewHolder {

        @Bind(R.id.tvBookListTitle)
        TextView tvBookListTitle;
        @Bind(R.id.tvBookListDesc)
        TextView tvBookListDesc;
        @Bind(R.id.ivAuthorAvatar)
        ImageView ivAuthorAvatar;
        @Bind(R.id.tvBookListAuthor)
        TextView tvBookListAuthor;
        @Bind(R.id.btnShare)
        TextView btnShare;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    /**
     * 获取详情页信息
     * @param id        id值
     */
    private void getBookListDetail(String id) {
        BookReaderModel model = BookReaderModel.getInstance(ReadBookDetailSubjectActivity.this);
        Subscription rxSubscription = model.getBookListDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DetailBookSubjectList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        recyclerView.showError();
                    }

                    @Override
                    public void onNext(DetailBookSubjectList data) {
                        if(data!=null && data.isOk()){
                            recyclerView.showRecycler();
                            showBookListDetail(data);
                        }else {
                            recyclerView.showEmpty();
                        }
                    }
                });
    }


    public void showBookListDetail(DetailBookSubjectList data) {
        headerViewHolder.tvBookListTitle.setText(data.getBookList().getTitle());
        headerViewHolder.tvBookListDesc.setText(data.getBookList().getDesc());
        headerViewHolder.tvBookListAuthor.setText(data.getBookList().getAuthor().getNickname());

        String logo = ConstantZssqApi.IMG_BASE_URL + data.getBookList().getAuthor().getAvatar();
        ImageUtils.loadImgByPicassoPerson(ReadBookDetailSubjectActivity.this, logo,R.drawable.avatar_default,headerViewHolder.ivAuthorAvatar);

        List<DetailBookSubjectList.BookListBean.BooksBean> list = data.getBookList().getBooks();
        mAllBooks.clear();
        mAllBooks.addAll(list);
        adapter.clear();
        loadNextPage();
    }

    /**
     * 加载更多
     */
    private void loadNextPage() {
        if (start < mAllBooks.size()) {
            if (mAllBooks.size() - start > limit) {
                adapter.addAll(mAllBooks.subList(start, start + limit));
            } else {
                adapter.addAll(mAllBooks.subList(start, mAllBooks.size()));
            }
            start += limit;
        } else {
            adapter.addAll(new ArrayList<DetailBookSubjectList.BookListBean.BooksBean>());
        }
    }

}
