package com.ns.yc.lifehelper.ui.data;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SizeUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.adapter.NarrowImageAdapter;
import com.ns.yc.lifehelper.adapter.ViewPagerGridAdapter;
import com.ns.yc.lifehelper.adapter.ViewPagerRollAdapter;
import com.ns.yc.lifehelper.api.ConstantImageApi;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.bean.ImageIconBean;
import com.ns.yc.lifehelper.ui.data.adapter.DataToolAdapter;
import com.ns.yc.lifehelper.ui.data.view.DoodleViewActivity;
import com.ns.yc.lifehelper.ui.main.MainActivity;
import com.ns.yc.lifehelper.ui.other.expressDelivery.ExpressDeliveryActivity;
import com.ns.yc.lifehelper.ui.other.listener.ListenerActivity;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.MobilePlayerActivity;
import com.ns.yc.lifehelper.ui.other.myNote.NoteActivity;
import com.ns.yc.lifehelper.ui.other.safe360.SafeHomeActivity;
import com.ns.yc.lifehelper.ui.other.toDo.ToDoTimerActivity;
import com.ns.yc.lifehelper.ui.other.weather.WeatherActivity;
import com.ns.yc.lifehelper.ui.weight.MyGridView;
import com.ns.yc.lifehelper.utils.ImageUtils;
import com.pedaily.yc.ycdialoglib.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/18
 * 描    述：工具页面
 * 修订历史：
 * ================================================
 */
public class DataFragment extends BaseFragment implements View.OnClickListener {


    @Bind(R.id.gridView)
    MyGridView gridView;
    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.vp_pager)
    ViewPager vpPager;
    @Bind(R.id.ll_points)
    LinearLayout llPoints;
    @Bind(R.id.tv_note_edit)
    TextView tvNoteEdit;
    @Bind(R.id.recyclerView)
    EasyRecyclerView recyclerView;

    private List<ImageIconBean> listDatas;  //总的数据源
    private List<View> viewPagerList;       //GridView作为一个View对象添加到ViewPager集合中
    private ImageView[] ivPoints;           //小圆点图片的集合
    private int totalPage;                  //总的页数
    private int mPageSize = 8;              //每页显示的最大的数量
    private MainActivity activity;

    private int[] proPic = {R.drawable.ic_investment, R.drawable.ic_project, R.drawable.ic_financing, R.drawable.ic_show,
            R.drawable.ic_project, R.drawable.ic_show, R.drawable.ic_project, R.drawable.ic_investment,
            R.drawable.ic_investment, R.drawable.ic_project, R.drawable.ic_financing, R.drawable.ic_show};
    private String[] proName = {"快递", "天气", "违章", "工商", "股票", "身份证", "人脸识别", "归属地", "空气质量", "今日油价", "翻译", "缴费"};


    private int[] toolLogo = {R.drawable.ic_investment, R.drawable.ic_project, R.drawable.ic_financing, R.drawable.ic_show,
            R.drawable.ic_project, R.drawable.ic_show, R.drawable.ic_project, R.drawable.ic_investment,
            R.drawable.ic_investment, R.drawable.ic_project, R.drawable.ic_financing, R.drawable.ic_show};
    private String[] toolName = {"笔记本", "计算器", "360工具", "听听", "播放器", "唐诗", "宋词", "历史", "法律", "景点", "航班", "绘画板"};
    private NarrowImageAdapter adapter;

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        if (banner != null) {
            banner.startAutoPlay();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //结束轮播
        if (banner != null) {
            banner.stopAutoPlay();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_data;
    }

    @Override
    public void initView() {
        initBanner();
        iniVpData();
        initViewPager();
        initGridView();
        initRecycleView();
    }

    @Override
    public void initListener() {
        tvNoteEdit.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_note_edit:
                startActivity(ToDoTimerActivity.class);
                break;
        }
    }

    /**
     * 初始化轮播图
     */
    private void initBanner() {
        List<String> lists = new ArrayList<>();
        for (int a = 0; a < ConstantImageApi.SPALSH_URLS.length; a++) {
            lists.add(ConstantImageApi.SPALSH_URLS[a]);
        }
        //设置banner样式
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new BannerImageLoader());
        //设置图片集合
        banner.setImages(lists);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        //banner.setBannerTitles(titleLists);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private class BannerImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            ImageUtils.loadImgByPicasso(context, (String) path, imageView);
        }
    }

    private void iniVpData() {
        listDatas = new ArrayList<>();
        for (int i = 0; i < proPic.length; i++) {
            listDatas.add(new ImageIconBean(proName[i], proPic[i]));
        }
        //总的页数向上取整
        totalPage = (int) Math.ceil(listDatas.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<>();
        for (int i = 0; i < totalPage; i++) {
            //每个页面都是inflate出一个新实例
            final GridView gridView = (GridView) View.inflate(activity, R.layout.item_vp_grid_view, null);
            gridView.setAdapter(new ViewPagerGridAdapter(activity, listDatas, i, mPageSize));
            //添加item点击监听
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Object obj = gridView.getItemAtPosition(position);
                    if (obj != null && obj instanceof ImageIconBean) {
                        switch (position) {
                            case 0:
                                startActivity(ExpressDeliveryActivity.class);
                                break;
                            case 1:
                                startActivity(WeatherActivity.class);
                                break;
                            case 2:

                                break;
                            case 4:

                                break;
                            default:
                                Toast.makeText(activity, ((ImageIconBean) obj).getName() + "---", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }
            });
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(gridView);
        }
        //设置ViewPager适配器
        vpPager.setAdapter(new ViewPagerRollAdapter(viewPagerList));

        //添加小圆点
        ivPoints = new ImageView[totalPage];
        for (int i = 0; i < totalPage; i++) {
            //循坏加入点点图片组
            ivPoints[i] = new ImageView(activity);
            if (i == 0) {
                ivPoints[i].setImageResource(R.drawable.ic_page_focuese);
            } else {
                ivPoints[i].setImageResource(R.drawable.ic_page_unfocused);
            }
            ivPoints[i].setPadding(8, 8, 8, 8);
            llPoints.addView(ivPoints[i]);
        }
    }


    private void initViewPager() {
        //设置ViewPager的滑动监听，主要是设置点点的背景颜色的改变
        vpPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < totalPage; i++) {
                    if (i == position) {
                        ivPoints[i].setImageResource(R.drawable.ic_page_focuese);
                    } else {
                        ivPoints[i].setImageResource(R.drawable.ic_page_unfocused);
                    }
                }
            }
        });
    }

    private void initGridView() {
        DataToolAdapter adapter = new DataToolAdapter(activity, toolName, toolLogo);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(NoteActivity.class);
                        break;
                    case 1:

                        break;
                    case 2:
                        startActivity(SafeHomeActivity.class);
                        break;
                    case 3:
                        startActivity(ListenerActivity.class);
                        break;
                    case 4:
                        startActivity(MobilePlayerActivity.class);
                        break;
                    case 11:
                        startActivity(DoodleViewActivity.class);
                        break;
                }
            }
        });
    }


    private void initRecycleView() {
        recyclerView.setAdapter(adapter = new NarrowImageAdapter(activity));
        //recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setHorizontalScrollBarEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.addItemDecoration(new SpaceDecoration(SizeUtils.dp2px(8)));
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                //adapter.addAll(ConstantImageApi.createSmallImage());
                adapter.addAll(ConstantImageApi.getNarrowImage());
            }
        });
        //思考，为什么下面这种方式会导致加载图片OOM
        //adapter.addAll(ConstantImageApi.createSmallImage());
        //下面这个是正常的
        adapter.addAll(ConstantImageApi.getNarrowImage());
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ToastUtil.showToast(activity,position+"被点击呢");
            }
        });
    }


}
