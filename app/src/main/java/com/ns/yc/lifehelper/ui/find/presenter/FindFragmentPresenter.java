package com.ns.yc.lifehelper.ui.find.presenter;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelper;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constant.Constant;
import com.ns.yc.lifehelper.base.adapter.BaseDelegateAdapter;
import com.ns.yc.lifehelper.model.bean.HomeBlogEntity;
import com.ns.yc.lifehelper.ui.find.contract.FindFragmentContract;
import com.ns.yc.lifehelper.ui.main.view.MainActivity;
import com.ns.yc.lifehelper.weight.MarqueeView;
import com.ns.yc.lifehelper.utils.image.ImageUtils;
import com.yc.cn.ycbannerlib.first.BannerView;
import com.yc.cn.ycbaseadapterlib.first.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/11/21
 * 描    述：数据页面
 * 修订历史：
 * ================================================
 */
public class FindFragmentPresenter implements FindFragmentContract.Presenter {

    private FindFragmentContract.View mView;
    private CompositeSubscription mSubscriptions;
    private MainActivity activity;

    public FindFragmentPresenter(FindFragmentContract.View androidView) {
        this.mView = androidView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {

    }


    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }


    @Override
    public void setActivity(MainActivity activity) {
        this.activity = activity;
    }


    @Override
    public DelegateAdapter initRecyclerView(RecyclerView recyclerView) {
        //初始化
        //创建VirtualLayoutManager对象
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);

        //设置回收复用池大小，（如果一屏内相同类型的 View 个数比较多，需要设置一个合适的大小，防止来回滚动时重新创建 View）
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);

        //设置适配器
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager, true);
        recyclerView.setAdapter(delegateAdapter);
        return delegateAdapter;
    }


    @Override
    public BaseDelegateAdapter initBannerAdapter() {
        final List<Object> arrayList = new ArrayList<>();
        arrayList.add("http://bpic.wotucdn.com/11/66/23/55bOOOPIC3c_1024.jpg!/fw/780/quality/90/unsharp/true/compress/true/watermark/url/L2xvZ28ud2F0ZXIudjIucG5n/repeat/true");
        arrayList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505470629546&di=194a9a92bfcb7754c5e4d19ff1515355&imgtype=0&src=http%3A%2F%2Fpics.jiancai.com%2Fimgextra%2Fimg01%2F656928666%2Fi1%2FT2_IffXdxaXXXXXXXX_%2521%2521656928666.jpg");
        //banner
        return new BaseDelegateAdapter(activity, new LinearLayoutHelper(), R.layout.view_vlayout_banner, 1, Constant.viewType.typeBanner) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                // 绑定数据
                BannerView mBanner = holder.getView(R.id.banner);
                mView.setBanner(mBanner,arrayList);
            }
        };
    }

    @Override
    public BaseDelegateAdapter initGvMenu() {
        //menu
        // 在构造函数设置每行的网格个数
        final TypedArray proPic = activity.getResources().obtainTypedArray(R.array.find_gv_image);
        final String[] proName = activity.getResources().getStringArray(R.array.find_gv_title);
        final List<Integer> images = new ArrayList<>();
        for(int a=0 ; a<proName.length ; a++){
            images.add(proPic.getResourceId(a,R.drawable.ic_data_picture));
        }
        proPic.recycle();
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setPadding(0, 16, 0, 16);
        gridLayoutHelper.setVGap(16);   // 控制子元素之间的垂直间距
        gridLayoutHelper.setHGap(0);    // 控制子元素之间的水平间距
        gridLayoutHelper.setBgColor(Color.WHITE);
        return new BaseDelegateAdapter(activity, gridLayoutHelper, R.layout.item_vp_grid_iv, 8, Constant.viewType.typeGv) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
                super.onBindViewHolder(holder, position);
                holder.setText(R.id.tv_new_seed_title, proName[position]);
                holder.setImageResource(R.id.iv_new_seed_ic, images.get(position));
                holder.getView(R.id.ll_new_seed_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mView.setOnclick(position);
                    }
                });
            }
        };
    }

    @Override
    public BaseDelegateAdapter initMarqueeView() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        return new BaseDelegateAdapter(activity,linearLayoutHelper , R.layout.view_vlayout_marquee, 1, Constant.viewType.typeMarquee) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                MarqueeView marqueeView = holder.getView(R.id.marqueeView);

                List<String> info1 = new ArrayList<>();
                info1.add("1.坚持读书，写作，源于内心的动力！");
                info1.add("2.欢迎订阅喜马拉雅听书！");
                marqueeView.startWithList(info1);
                // 在代码里设置自己的动画
                marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, TextView textView) {
                        mView.setMarqueeClick(position);
                    }
                });
            }
        };
    }

    @Override
    public BaseDelegateAdapter initTitle(final String title) {
        return new BaseDelegateAdapter(activity, new LinearLayoutHelper(), R.layout.view_vlayout_title, 1, Constant.viewType.typeTitle) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.setText(R.id.tv_title, title);
            }
        };
    }

    @Override
    public BaseDelegateAdapter initList1() {
        //item1 gird
        final TypedArray list1_image = activity.getResources().obtainTypedArray(R.array.find_list1_image);
        final String[] list1_title = activity.getResources().getStringArray(R.array.find_list1_title);
        final List<Integer> images = new ArrayList<>();
        for(int a=0 ; a<list1_title.length ; a++){
            images.add(list1_image.getResourceId(a,R.drawable.ic_data_picture));
        }
        list1_image.recycle();
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(4);
        gridLayoutHelper.setMargin(0, 0, 0, 0);
        gridLayoutHelper.setPadding(0, 20, 0, 10);
        gridLayoutHelper.setVGap(10);// 控制子元素之间的垂直间距
        gridLayoutHelper.setHGap(0);// 控制子元素之间的水平间距
        gridLayoutHelper.setWeights(new float[]{25, 25, 25 , 25});
        gridLayoutHelper.setBgColor(Color.WHITE);
        //gridLayoutHelper.setAutoExpand(true);//是否自动填充空白区域
        return new BaseDelegateAdapter(activity, gridLayoutHelper, R.layout.view_vlayout_grid, 8, Constant.viewType.typeGvSecond) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
                super.onBindViewHolder(holder, position);
                holder.setText(R.id.tv_title, list1_title[position]);
                ImageView iv = holder.getView(R.id.iv_image);
                ImageUtils.loadImgByPicasso(activity, images.get(position), iv);
                holder.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mView.setGridClick(position);
                    }
                });
            }
        };
    }

    @Override
    public BaseDelegateAdapter initList2() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setAspectRatio(4.0f);
        linearLayoutHelper.setDividerHeight(5);
        linearLayoutHelper.setMargin(0, 0, 0, 0);
        linearLayoutHelper.setPadding(0, 0, 0, 10);
        //linearLayoutHelper.setBgColor(0xFFF5A623);
        return new BaseDelegateAdapter(activity, linearLayoutHelper, R.layout.item_tx_news_list, 3, Constant.viewType.typeNews) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, final int position) {
                super.onBindViewHolder(holder, position);
                if (Constant.findNews != null && Constant.findNews.size() > 0) {
                    HomeBlogEntity model = Constant.findNews.get(position);
                    holder.setText(R.id.tv_title, model.getTitle());
                    ImageView imageView = holder.getView(R.id.iv_logo);
                    ImageUtils.loadImgByPicassoError(activity, model.getImageUrl(), R.drawable.image_default, imageView);
                    holder.setText(R.id.tv_time, model.getTime());
                    holder.getItemView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mView.setNewsList2Click(position,Constant.findNews.get(position).getUrl());
                        }
                    });
                } else {
                    ImageView imageView = holder.getView(R.id.iv_logo);
                    holder.setText(R.id.tv_title, "标题 ");
                    imageView.setImageResource(R.drawable.image_default);
                    holder.setText(R.id.tv_time, "时间");
                }
            }
        };
    }


    @Override
    public BaseDelegateAdapter initList3() {
        final TypedArray list3_image = activity.getResources().obtainTypedArray(R.array.find_list3_image);
        final String[] list3_title = activity.getResources().getStringArray(R.array.find_list3_title);
        final List<Integer> images = new ArrayList<>();
        for(int a=0 ; a<list3_title.length ; a++){
            images.add(list3_image.getResourceId(a,R.drawable.ic_data_picture));
        }
        list3_image.recycle();
        GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(3);
        gridLayoutHelper.setMargin(20, 0, 20, 0);
        gridLayoutHelper.setPadding(0, 20, 0, 10);
        gridLayoutHelper.setBgColor(Color.WHITE);
        //gridLayoutHelper.setAspectRatio(1.5f);  // 设置设置布局内每行布局的宽与高的比

        // gridLayoutHelper特有属性（下面会详细说明）
        //设置每行中 每个网格宽度 占 每行总宽度 的比例
        gridLayoutHelper.setWeights(new float[]{30, 40, 30});
        gridLayoutHelper.setVGap(0);// 控制子元素之间的垂直间距
        gridLayoutHelper.setHGap(5);// 控制子元素之间的水平间距
        //gridLayoutHelper.setAutoExpand(false);//是否自动填充空白区域
        //gridLayoutHelper.setSpanCount(6);   // 设置每行多少个网格
        return new BaseDelegateAdapter(activity, gridLayoutHelper, R.layout.view_vlayout_hor, 3, Constant.viewType.typeList) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
                super.onBindViewHolder(holder, position);
                holder.setText(R.id.tv_title, list3_title[position]);
                ImageView iv = holder.getView(R.id.iv_image);
                ImageUtils.loadImgByPicasso(activity, images.get(position), iv);
                holder.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mView.setGridClickThird(position);
                    }
                });
            }
        };
    }


    @Override
    public BaseDelegateAdapter initList4() {
        final TypedArray list4_image = activity.getResources().obtainTypedArray(R.array.find_list4_image);
        final String[] list4_title = activity.getResources().getStringArray(R.array.find_list4_title);
        final List<Integer> images = new ArrayList<>();
        for(int a=0 ; a<list4_title.length ; a++){
            images.add(list4_image.getResourceId(a,R.drawable.ic_data_picture));
        }
        list4_image.recycle();
        OnePlusNLayoutHelper onePlusNLayoutHelper = new OnePlusNLayoutHelper();
        //noinspection deprecation
        onePlusNLayoutHelper.setBgColor(activity.getResources().getColor(R.color.colorWhite));
        //onePlusNLayoutHelper.setAspectRatio(2.0f);
        //onePlusNLayoutHelper.setColWeights(new float[]{40f});
        //onePlusNLayoutHelper.setRowWeight(30f);
        onePlusNLayoutHelper.setMargin(0, 0, 0, 0);
        onePlusNLayoutHelper.setPadding(10, 20, 10, 10);
        return new BaseDelegateAdapter(activity, onePlusNLayoutHelper, R.layout.view_vlayout_plus, 3, Constant.viewType.typePlus) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, @SuppressLint("RecyclerView") final int position) {
                super.onBindViewHolder(holder, position);
                if (position == 0) {
                    holder.getView(R.id.ll_first).setVisibility(View.VISIBLE);
                    holder.getView(R.id.ll_second).setVisibility(View.GONE);
                    holder.setText(R.id.tv_title, list4_title[position]);
                    holder.setImageResource(R.id.iv_image, images.get(position));
                } else {
                    holder.getView(R.id.ll_first).setVisibility(View.GONE);
                    holder.getView(R.id.ll_second).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_title2, list4_title[position]);
                    holder.setImageResource(R.id.iv_image2, images.get(position));
                }
                holder.getItemView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mView.setGridClickFour(position);
                    }
                });
            }
        };
    }


    @Override
    public BaseDelegateAdapter initList5() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setAspectRatio(4.0f);
        linearLayoutHelper.setDividerHeight(5);
        linearLayoutHelper.setMargin(0, 0, 0, 0);
        linearLayoutHelper.setPadding(0, 0, 0, 10);
        return new BaseDelegateAdapter(activity, linearLayoutHelper, R.layout.view_vlayout_news, 10, Constant.viewType.typeFooter) {
            @Override
            public void onBindViewHolder(BaseViewHolder holder, final int position) {
                super.onBindViewHolder(holder, position);
                if (Constant.findBottomNews != null && Constant.findBottomNews.size() > 0) {
                    HomeBlogEntity model = Constant.findBottomNews.get(position);
                    holder.setText(R.id.tv_title, model.getTitle());
                    ImageView imageView = holder.getView(R.id.iv_logo);
                    ImageUtils.loadImgByPicassoError(activity, model.getImageUrl(), R.drawable.image_default, imageView);
                    holder.setText(R.id.tv_time, model.getTime());
                    holder.getItemView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mView.setNewsList5Click(position,Constant.findBottomNews.get(position).getUrl());
                        }
                    });
                } else {
                    ImageView imageView = holder.getView(R.id.iv_logo);
                    holder.setText(R.id.tv_title, "新闻标题 ");
                    imageView.setImageResource(R.drawable.image_default);
                    holder.setText(R.id.tv_time, "新闻时间");
                }
            }
        };
    }
}
