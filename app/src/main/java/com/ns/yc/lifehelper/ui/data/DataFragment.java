package com.ns.yc.lifehelper.ui.data;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.adapter.NarrowImageAdapter;
import com.ns.yc.lifehelper.adapter.ViewPagerGridAdapter;
import com.ns.yc.lifehelper.adapter.ViewPagerRollAdapter;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;
import com.ns.yc.lifehelper.bean.ImageIconBean;
import com.ns.yc.lifehelper.ui.data.contract.DataFragmentContract;
import com.ns.yc.lifehelper.ui.data.presenter.DataFragmentPresenter;
import com.ns.yc.lifehelper.ui.data.view.activity.DoodleViewActivity;
import com.ns.yc.lifehelper.ui.data.view.activity.GalleryImageActivity;
import com.ns.yc.lifehelper.ui.data.view.activity.OpenFileActivity;
import com.ns.yc.lifehelper.ui.data.view.activity.RiddleActivity;
import com.ns.yc.lifehelper.ui.data.view.adapter.DataToolAdapter;
import com.ns.yc.lifehelper.ui.main.view.activity.MainActivity;
import com.ns.yc.lifehelper.ui.other.expressDelivery.ExpressDeliveryActivity;
import com.ns.yc.lifehelper.ui.other.imTalk.ui.ImTalkActivity;
import com.ns.yc.lifehelper.ui.other.listener.ListenerActivity;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.MobilePlayerActivity;
import com.ns.yc.lifehelper.ui.other.myNote.NoteActivity;
import com.ns.yc.lifehelper.ui.other.safe360.SafeHomeActivity;
import com.ns.yc.lifehelper.ui.other.vtex.view.WTexNewsActivity;
import com.ns.yc.lifehelper.ui.other.weather.WeatherActivity;
import com.ns.yc.lifehelper.ui.other.workDo.ui.WorkDoActivity;
import com.ns.yc.lifehelper.ui.other.zhihu.ui.ZhiHuNewsActivity;
import com.ns.yc.lifehelper.ui.test.aidl.BankActivity;
import com.ns.yc.lifehelper.ui.weight.MyGridView;
import com.pedaily.yc.ycdialoglib.toast.ToastUtil;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.item.SpaceViewItemLine;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/3/18
 * 描    述：工具页面
 * 修订历史：
 *          v1.4 17年6月8日
 *          v1.5 17年10月3日修改
 * ================================================
 */
public class DataFragment extends BaseFragment implements View.OnClickListener
        ,DataFragmentContract.View{


    @Bind(R.id.gridView)
    MyGridView gridView;
    @Bind(R.id.vp_pager)
    ViewPager vpPager;
    @Bind(R.id.ll_points)
    LinearLayout llPoints;
    @Bind(R.id.tv_note_edit)
    TextView tvNoteEdit;
    @Bind(R.id.tv_news_zhi_hu)
    TextView tvNewsZhiHu;
    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;


    private MainActivity activity;
    private NarrowImageAdapter adapter;
    private DataFragmentContract.Presenter presenter = new DataFragmentPresenter(this);


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(activity!=null){
            activity = null;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.bindView(activity);
        presenter.subscribe();
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_data;
    }

    @Override
    public void initView() {
        iniVpData();
    }

    @Override
    public void initListener() {
        tvNoteEdit.setOnClickListener(this);
        tvNewsZhiHu.setOnClickListener(this);
    }


    @Override
    public void initData() {
        presenter.initGridViewData();
        presenter.initRecycleViewData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_note_edit:
                //startActivity(ToDoTimerActivity.class);
                startActivity(WorkDoActivity.class);
                break;
            case R.id.tv_news_zhi_hu:
                startActivity(ZhiHuNewsActivity.class);
                break;
            default:
                break;
        }
    }


    private void iniVpData() {
        List<ImageIconBean> listData = presenter.getVpData();
        final int mPageSize = 8;              //每页显示的最大的数量
        //总的页数向上取整
        final int totalPage = (int) Math.ceil(listData.size() * 1.0 / mPageSize);
        List<View> viewPagerList = new ArrayList<>();
        for (int i = 0; i < totalPage; i++) {
            //每个页面都是inflate出一个新实例
            final GridView gridView = (GridView) View.inflate(activity, R.layout.item_vp_grid_view, null);
            gridView.setAdapter(new ViewPagerGridAdapter(activity, listData, i, mPageSize));
            //添加item点击监听
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Object obj = gridView.getItemAtPosition(position);
                    //int pos = position + totalPage * mPageSize;
                    if (obj != null && obj instanceof ImageIconBean) {
                        int pos = ((ImageIconBean) obj).getId();
                        switch (pos) {
                            case 0:
                                startActivity(ExpressDeliveryActivity.class);
                                break;
                            case 1:

                                startActivity(WeatherActivity.class);
                                break;
                            case 2:
                                startActivity(NoteActivity.class);
                                break;
                            case 3:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if(!Settings.canDrawOverlays(activity)) {
                                        ToastUtils.showShort("请打开投资界允许权限开关");
                                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        activity.startActivity(intent);
                                    }
                                }
                                break;
                            case 4:
                                startActivity(GalleryImageActivity.class);
                                break;
                            case 5:
                                startActivity(WTexNewsActivity.class);
                                break;
                            case 6:
                                startActivity(ImTalkActivity.class);
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
        final ImageView[] ivPoints = new ImageView[totalPage];
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


    @Override
    public void setGridView(String[] toolName, ArrayList<Integer> logoList) {
        DataToolAdapter adapter = new DataToolAdapter(activity, toolName, logoList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(NoteActivity.class);
                        break;
                    case 1:
                        startActivity(OpenFileActivity.class);
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
                    case 9:
                        startActivity(RiddleActivity.class);
                        break;
                    case 10:
                        startActivity(BankActivity.class);
                        break;
                    case 11:
                        startActivity(DoodleViewActivity.class);
                        break;
                }
            }
        });
    }


    @Override
    public void setRecycleView(final ArrayList<Integer> list) {
        recyclerView.setAdapter(adapter = new NarrowImageAdapter(activity));
        //recyclerView.setVerticalScrollBarEnabled(false);
        recyclerView.setHorizontalScrollBarEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.addItemDecoration(new SpaceViewItemLine(SizeUtils.dp2px(8)));
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                //adapter.addAll(ConstantImageApi.createSmallImage());
                //adapter.addAll(ConstantImageApi.getNarrowImage());
                adapter.addAll(list);
            }
        });
        //思考，为什么下面这种方式会导致加载图片OOM
        //adapter.addAll(ConstantImageApi.createSmallImage());
        //下面这个是正常的
        //adapter.addAll(ConstantImageApi.getNarrowImage());
        adapter.addAll(list);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ToastUtil.showToast(activity,position+"被点击呢");
            }
        });
    }
}
