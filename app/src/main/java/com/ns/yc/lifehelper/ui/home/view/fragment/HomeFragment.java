package com.ns.yc.lifehelper.ui.home.view.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.adapter.BaseBannerPagerAdapter;
import com.ns.yc.lifehelper.ui.home.contract.HomeFragmentContract;
import com.ns.yc.lifehelper.ui.home.presenter.HomeFragmentPresenter;
import com.ns.yc.lifehelper.ui.home.view.adapter.HomeBlogAdapter;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;
import com.ns.yc.lifehelper.utils.DialogUtils;
import com.ns.yc.yccardviewlib.CardViewLayout;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.yc.cn.ycbannerlib.banner.BannerView;
import com.yc.cn.ycbannerlib.marquee.MarqueeView;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.base.mvp.BaseFragment;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.model.HomeBlogEntity;
import com.ycbjie.library.utils.handler.HandlerUtils;
import com.ycbjie.library.web.view.WebViewActivity;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/03/22
 *     desc  : Home主页面
 *     revise:
 * </pre>
 */
public class HomeFragment extends BaseFragment<HomeFragmentPresenter> implements
        HomeFragmentContract.View {

    private YCRefreshView mRecyclerView;
    private HomeFragmentContract.Presenter presenter = new HomeFragmentPresenter(this);
    private ArrayList<Bitmap> bitmaps;
    private MainActivity activity;
    private BannerView banner;
    private MarqueeView marqueeView;
    private HomeBlogAdapter adapter;
    private View headerView;
    private CardViewLayout cardViewLayout;


    private HandlerUtils.HandlerReference handler = new HandlerUtils.HandlerReference(
            this, new HandlerUtils.HandlerReference.OnReceiveMessageListener() {
        @Override
        public void handlerMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if (cardViewLayout!=null){
                        cardViewLayout.setVisibility(View.VISIBLE);
                    }
                    updateGalleryView();
                    break;
                case 2:
                    if (cardViewLayout!=null){
                        cardViewLayout.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }
    });

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
        presenter.bindActivity(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (activity != null) {
            activity = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (banner != null) {
            banner.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (banner != null) {
            banner.resume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*if(handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }*/
    }

    @Override
    public int getContentView() {
        return R.layout.base_easy_recycle;
    }

    @Override
    public void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        initRecycleView();
    }

    @Override
    public void initListener() {
        adapter.setOnItemClickListener(position -> {
            if (position > 0 && adapter.getAllData().size() > position) {
                WebViewActivity.lunch(activity,adapter.getAllData().get(position).getUrl()
                        ,adapter.getAllData().get(position).getTitle());
            } else if (position == 0) {
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_ZHIHU_ACTIVITY);
            }
        });
    }


    @Override
    public void initData() {
        presenter.getHomeNewsData();
        presenter.getGalleryData();
    }


    private void initRecycleView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        RecycleViewItemLine line = new RecycleViewItemLine(activity, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        mRecyclerView.addItemDecoration(line);
        adapter = new HomeBlogAdapter(activity);
        initAddHeader();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setRefreshListener(() -> {
            if (NetworkUtils.isConnected()) {
                if (adapter.getAllData().size() > 0) {
                    presenter.getHomeNewsData();
                } else {
                    mRecyclerView.setRefreshing(false);
                }
            } else {
                mRecyclerView.setRefreshing(false);
                ToastUtils.showRoundRectToast("网络不可用");
            }
        });
    }


    private void initAddHeader() {
        adapter.addHeader(new RecyclerArrayAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                headerView = View.inflate(activity, R.layout.head_home_main, null);
                return headerView;
            }

            @Override
            public void onBindView(View header) {
                banner = header.findViewById(R.id.banner);
                TextView tvHomeFirst = header.findViewById(R.id.tv_home_first);
                TextView tvHomeSecond =header.findViewById(R.id.tv_home_second);
                TextView tvHomeThird = header.findViewById(R.id.tv_home_third);
                TextView tvHomeFour = header.findViewById(R.id.tv_home_four);
                marqueeView = header.findViewById(R.id.marqueeView);
                cardViewLayout = header.findViewById(R.id.cardView);
                View.OnClickListener listener = v -> {
                    switch (v.getId()) {
                        case R.id.tv_home_first:
                            ARouterUtils.navigation(ARouterConstant.ACTIVITY_VIDEO_VIDEO);
                            break;
                        case R.id.tv_home_second:
                            ARouterUtils.navigation(ARouterConstant.ACTIVITY_OTHER_AIR_ACTIVITY);
                            break;
                        case R.id.tv_home_third:
                            ARouterUtils.navigation(ARouterConstant.ACTIVITY_TX_NEWS_ACTIVITY);
                            break;
                        case R.id.tv_home_four:
                            ARouterUtils.navigation(ARouterConstant.ACTIVITY_GANK_KNOWLEDGE_ACTIVITY);
                            break;
                        default:
                            break;
                    }
                };
                tvHomeFirst.setOnClickListener(listener);
                tvHomeSecond.setOnClickListener(listener);
                tvHomeThird.setOnClickListener(listener);
                tvHomeFour.setOnClickListener(listener);
                initBanner();
                initMarqueeView();
            }
        });
    }


    /**
     * 初始化轮播图
     */
    private void initBanner() {
        if (headerView != null && banner != null) {
            List<Object> lists = presenter.getBannerData();
            banner.setHintGravity(1);
            banner.setAnimationDuration(1000);
            banner.setPlayDelay(2000);
            banner.setHintPadding(0, 0, 0, SizeUtils.dp2px(10));
            banner.setAdapter(new BaseBannerPagerAdapter(activity, lists));
        }
    }


    private void initMarqueeView() {
        if (marqueeView == null) {
            return;
        }
        ArrayList<String> list = presenter.getMarqueeTitle();
        marqueeView.startWithList(list);
        marqueeView.setOnItemClickListener((position, textView) -> {
            switch (position) {
                case 0:
                    DialogUtils.showCustomPopupWindow(activity);
                    break;
                case 1:
                    Bundle bundle1 = new Bundle();
                    bundle1.putString(Constant.URL,Constant.GITHUB);
                    bundle1.putString(Constant.TITLE,"关于更多内容");
                    ARouterUtils.navigation(ARouterConstant.ACTIVITY_LIBRARY_WEB_VIEW,bundle1);
                    break;
                case 2:
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(Constant.URL,Constant.ZHI_HU);
                    bundle2.putString(Constant.TITLE,"关于我的知乎");
                    ARouterUtils.navigation(ARouterConstant.ACTIVITY_LIBRARY_WEB_VIEW,bundle2);
                    break;
                default:
                    break;
            }
        });
    }


    @Override
    public void setNewsData(List<HomeBlogEntity> list) {
        if (list != null && list.size() > 0) {
            adapter.clear();
            adapter.addAll(list);
            adapter.notifyDataSetChanged();
            mRecyclerView.scrollTo(0,0);
            mRecyclerView.setRefreshing(false);
        }
    }

    @Override
    public void downloadBitmapSuccess(final ArrayList<Bitmap> bitmapList) {
        if(bitmapList!=null && bitmapList.size()>0){
            bitmaps = bitmapList;
            handler.sendEmptyMessageAtTime(1,200);
        }else {
            handler.sendEmptyMessageAtTime(2,200);
        }
    }


    private void updateGalleryView() {
        if(cardViewLayout==null || bitmaps==null || bitmaps.size()==0){
            return;
        }
        cardViewLayout.setAdapter(new CardViewLayout.Adapter() {

            private SoftReference<Bitmap> bitmapSoftReference;

            class ViewHolder {
                ImageView imageView;
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_card_layout;
            }

            @Override
            public void bindView(View view, int position) {
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                if (viewHolder == null) {
                    viewHolder = new ViewHolder();
                    viewHolder.imageView = view.findViewById(R.id.imageView);
                    view.setTag(viewHolder);
                }
                Bitmap bitmap = bitmaps.get(position);
                //正常是用来处理图片这种占用内存大的情况
                bitmapSoftReference = new SoftReference<>(bitmap);
                if(bitmapSoftReference.get() != null) {
                    viewHolder.imageView.setImageBitmap(bitmapSoftReference.get());
                } else {
                    viewHolder.imageView.setImageBitmap(bitmap);
                }
            }

            @Override
            public int getItemCount() {
                return bitmaps==null ? 0 : bitmaps.size();
            }

            @Override
            public void displaying(int position) {

            }

            @Override
            public void onItemClick(View view, int position) {
                super.onItemClick(view, position);
                ARouterUtils.navigation(ARouterConstant.ACTIVITY_OTHER_GALLERY_ACTIVITY);
            }
        });
    }

}
