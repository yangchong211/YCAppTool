package com.ns.yc.lifehelper.ui.other.myNews.videoNews;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constant.Constant;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.main.view.activity.WebViewQQActivity;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.contract.TxWeChatContract;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.model.bean.WeChatBean;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.presenter.TxWeChatPresenter;
import com.ns.yc.lifehelper.ui.other.myNews.weChat.view.adapter.TxWeChatAdapter;
import com.ns.yc.lifehelper.utils.statusbar.StatusBarAllUtil;
import com.ns.yc.ycutilslib.blurView.RealTimeBlurView;
import com.pedaily.yc.ycdialoglib.customToast.ToastUtil;
import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import java.util.List;
import butterknife.Bind;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @author yc
 * @date 2018/2/27
 */
public class VideoNewsActivity extends BaseActivity implements TxWeChatContract.View, View.OnClickListener {


    @Bind(R.id.cl)
    CoordinatorLayout cl;
    @Bind(R.id.iv_bg_image)
    ImageView ivBgImage;
    @Bind(R.id.iv_header_image)
    ImageView ivHeaderImage;
    @Bind(R.id.iv_header_title)
    TextView ivHeaderTitle;
    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.fl_share)
    FrameLayout flShare;
    @Bind(R.id.ll_tool_bar)
    RelativeLayout llToolBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    @Bind(R.id.blur_view)
    RealTimeBlurView blurView;

    private TxWeChatContract.Presenter presenter = new TxWeChatPresenter(this);
    /**
     * CollapsingToolbarLayout 折叠状态
     */
    private Constant.CollapsingToolbarLayoutState state;
    private int num = 10;
    private int page = 1;
    private TxWeChatAdapter adapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        presenter.subscribe();
        super.onCreate(savedInstanceState);
    }


    @Override
    public int getContentView() {
        return R.layout.activity_video_news;
    }

    @Override
    public void initView() {
        float v = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, this.getResources().getDisplayMetrics());
        blurView.setBlurRadius(v);
        Glide.with(this)
                .load(R.drawable.ic_person_logo_big)
                .placeholder(R.drawable.ic_person_logo_big)
                .error(R.drawable.ic_person_logo_big)
                .crossFade(200)
                // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .bitmapTransform(new BlurTransformation(this,18,4))
                .into(ivBgImage);
        StatusBarAllUtil.setTranslucentForCoordinatorLayout(this,0);
        setFabDynamicState();
        initRecycleView();
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        flShare.setOnClickListener(this);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(int position) {
                if(position>-1 && adapter.getAllData().size()>position){
                    WebViewQQActivity.toWhere(new WebViewQQActivity.Builder()
                            .setContext(VideoNewsActivity.this)
                            .setId(adapter.getAllData().get(position).getUrl())
                            .setImgUrl(adapter.getAllData().get(position).getPicUrl())
                            .setTitle(adapter.getAllData().get(position).getTitle())
                            .setUrl(adapter.getAllData().get(position).getUrl())
                            .setType(Constant.LikeType.TYPE_WE_CHAT));
                }
            }
        });
    }

    @Override
    public void initData() {
        recyclerView.showProgress();
        page = 1;
        presenter.getNews(num, page);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.fl_share:
                ToastUtil.showToast(this,"分享功能");
                break;
            default:
                break;
        }
    }


    /**
     * 根据 CollapsingToolbarLayout 的折叠状态，设置 FloatingActionButton 的隐藏和显示
     */
    private void setFabDynamicState() {
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != Constant.CollapsingToolbarLayoutState.EXPANDED) {
                        // 修改状态标记为展开
                        state = Constant.CollapsingToolbarLayoutState.EXPANDED;
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != Constant.CollapsingToolbarLayoutState.COLLAPSED) {
                        // 修改状态标记为折叠
                        state = Constant.CollapsingToolbarLayoutState.COLLAPSED;
                        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
                        layoutParams.height = SizeUtils.dp2px(180);
                        appBar.setLayoutParams(layoutParams);
                    }
                } else {
                    if (state != Constant.CollapsingToolbarLayoutState.INTERNEDIATE) {
                        // 修改状态标记为中间
                        state = Constant.CollapsingToolbarLayoutState.INTERNEDIATE;
                    }
                }
            }
        });
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TxWeChatAdapter(this);
        recyclerView.setAdapter(adapter);
        addHeader();
        //加载更多
        adapter.setMore(R.layout.view_recycle_more, new RecyclerArrayAdapter.OnMoreListener() {
            @Override
            public void onMoreShow() {
                if (NetworkUtils.isConnected()) {
                    if (adapter.getAllData().size() > 0) {
                        page++;
                        presenter.getNews(num, page);
                    } else {
                        adapter.pauseMore();
                    }
                } else {
                    adapter.pauseMore();
                    ToastUtil.showToast(VideoNewsActivity.this, "网络不可用");
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
                    ToastUtil.showToast(VideoNewsActivity.this, "网络不可用");
                }
            }

            @Override
            public void onNoMoreClick() {
                if (NetworkUtils.isConnected()) {
                    adapter.resumeMore();
                } else {
                    ToastUtil.showToast(VideoNewsActivity.this, "网络不可用");
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
        /*recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isConnected()) {
                    page = 1;
                    presenter.getNews(num, page);
                } else {
                    recyclerView.setRefreshing(false);
                    ToastUtil.showToast(VideoNewsActivity.this, "网络不可用");
                }
            }
        });*/
    }

    private void addHeader() {
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                return LayoutInflater.from(VideoNewsActivity.this).inflate(
                        R.layout.head_video_news_top, parent, false);
            }


            @Override
            public void onBindView(View headerView) {

            }
        });
    }


    @Override
    public void setErrorView() {
        recyclerView.setErrorView(R.layout.view_custom_data_error);
        recyclerView.showError();
        LinearLayout llErrorView = (LinearLayout) recyclerView.findViewById(R.id.ll_error_view);
        llErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
            }
        });
    }


    @Override
    public void setNetworkErrorView() {
        recyclerView.setErrorView(R.layout.view_custom_network_error);
        recyclerView.showError();
        LinearLayout llSetNetwork = (LinearLayout) recyclerView.findViewById(R.id.ll_set_network);
        llSetNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.isConnected()) {
                    initData();
                } else {
                    Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void setView(List<WeChatBean.NewslistBean> newsList) {
        if (adapter != null) {
            adapter.clear();
        } else {
            adapter = new TxWeChatAdapter(this);
        }
        adapter.addAll(newsList);
        adapter.notifyDataSetChanged();
        recyclerView.showRecycler();
    }


    @Override
    public void setEmptyView() {
        recyclerView.setEmptyView(R.layout.view_custom_empty_data);
        recyclerView.showEmpty();
    }


    @Override
    public void setViewMore(List<WeChatBean.NewslistBean> newsList) {
        if(newsList.size()>0){
            adapter.addAll(newsList);
            adapter.notifyDataSetChanged();
        }else {
            adapter.stopMore();
        }
    }


    @Override
    public void stopMore() {
        adapter.stopMore();
    }


}
