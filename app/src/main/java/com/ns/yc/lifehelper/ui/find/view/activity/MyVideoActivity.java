package com.ns.yc.lifehelper.ui.find.view.activity;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.adapter.BaseViewPagerRollAdapter;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ns.yc.lifehelper.ui.find.view.adapter.VideoGridAdapter;
import com.ns.yc.lifehelper.ui.find.model.VideoIconBean;

import java.util.ArrayList;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：我的视频页面
 * 修订历史：
 * ================================================
 */
public class MyVideoActivity extends BaseActivity implements View.OnClickListener {

    FrameLayout llTitleMenu;
    TextView toolbarTitle;
    ViewPager vpPager;
    LinearLayout llDot;

    private String[] titles = {"本地", "电影", "酒店住宿", "休闲娱乐", "外卖", "自助餐", "KTV", "火车票", "周边游", "美甲美睫",
            "火锅", "生日蛋糕", "甜品饮品", "水上乐园", "汽车服务", "美发", "丽人", "景点", "足疗按摩", "运动健身", "健身", "超市", "买菜",
            "今日新单", "小吃快餐", "面膜", "洗浴/汗蒸", "母婴亲子", "生活服务", "婚纱摄影", "学习培训", "家装", "结婚", "全部分配"};
    //总的页数
    private int pageCount;
    //每一页显示的个数
    private static final int pageSize = 10;

    private ArrayList<VideoIconBean> iconData;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_video_main;
    }

    @Override
    public void initView() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        vpPager = findViewById(R.id.vp_pager);
        llDot = findViewById(R.id.ll_dot);
        initToolBar();
        initIconData();
        initViewPager();
    }

    private void initToolBar() {
        toolbarTitle.setText("我的视频");
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {

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

    /**
     * 初始化图标资源[TODO 记录笔记]
     * http://blog.csdn.net/qq_20785431/article/details/52528404
     */
    private void initIconData() {
        iconData = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            //动态获取资源ID，第一个参数是资源名，第二个参数是资源类型例如drawable，string等，第三个参数包名
            int imageId = getResources().getIdentifier("ic_category_" + i, "mipmap", getPackageName());
            iconData.add(new VideoIconBean(titles[i], imageId,i));
        }
    }

    /**
     * 初始化ViewPager，根据页数来动态创建GridView
     */
    private void initViewPager() {
        LayoutInflater inflater = LayoutInflater.from(this);
        ArrayList<View> mPagerList = new ArrayList<>();
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(iconData.size()*1.0 / pageSize);
        for(int a=0 ; a<pageCount ; a++){
            final GridView gridView = (GridView) inflater.inflate(R.layout.item_vp_grid_view,vpPager,false);
            GridView gv = (GridView) gridView.findViewById(R.id.gridView);
            gv.setNumColumns(5);
            gridView.setAdapter(new VideoGridAdapter(MyVideoActivity.this, iconData, a, pageSize));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object obj = gridView.getItemAtPosition(position);
                    //int pos = position + pageCount * pageSize;
                    if(obj!=null && obj instanceof VideoIconBean){
                        int pos = ((VideoIconBean) obj).getId();
                        ToastUtils.showShort("被点击呢"+pos + "---"+position);
                    }
                }
            });
            //每一个GridView作为一个View对象添加到ViewPager集合中
            mPagerList.add(gridView);
        }

        //设置适配器
        vpPager.setAdapter(new BaseViewPagerRollAdapter(mPagerList));
        //设置圆点
        setOvalLayout();
    }

    private void setOvalLayout() {
        //添加小圆点
        final ImageView[] ivPoints = new ImageView[pageCount];
        for (int i = 0; i < pageCount; i++) {
            //循坏加入点点图片组
            ivPoints[i] = new ImageView(MyVideoActivity.this);
            if (i == 0) {
                ivPoints[i].setImageResource(R.drawable.ic_page_focuese);
            } else {
                ivPoints[i].setImageResource(R.drawable.ic_page_unfocused);
            }
            ivPoints[i].setPadding(8, 8, 8, 8);
            llDot.addView(ivPoints[i]);
        }
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < pageCount; i++) {
                    if (i == position) {
                        ivPoints[i].setImageResource(R.drawable.ic_page_focuese);
                    } else {
                        ivPoints[i].setImageResource(R.drawable.ic_page_unfocused);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}
