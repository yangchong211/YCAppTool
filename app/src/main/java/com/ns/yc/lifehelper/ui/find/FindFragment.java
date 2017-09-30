package com.ns.yc.lifehelper.ui.find;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.adapter.ViewPagerGridAdapter;
import com.ns.yc.lifehelper.adapter.ViewPagerRollAdapter;
import com.ns.yc.lifehelper.base.BaseFragment;
import com.ns.yc.lifehelper.bean.ImageIconBean;
import com.ns.yc.lifehelper.ui.find.view.RiddleActivity;
import com.ns.yc.lifehelper.ui.main.MainActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.BookReaderActivity;
import com.ns.yc.lifehelper.ui.other.douBook.DouBookActivity;
import com.ns.yc.lifehelper.ui.other.douMovie.DouMovieActivity;
import com.ns.yc.lifehelper.ui.other.douMusic.DouMusicActivity;
import com.ns.yc.lifehelper.ui.other.myMusic.MyMusicActivity;
import com.ns.yc.lifehelper.ui.other.myPicture.MyPictureActivity;
import com.ns.yc.lifehelper.ui.other.myTsSc.SongCiActivity;
import com.ns.yc.lifehelper.ui.other.myTsSc.YuanQuActivity;
import com.ns.yc.lifehelper.ui.other.myTsSc.view.TangShiFirstActivity;
import com.ns.yc.lifehelper.ui.other.myVideo.MyVideoActivity;
import com.ns.yc.lifehelper.ui.other.sharpBendOfBrain.SharpBendOfBrainActivity;
import com.ns.yc.lifehelper.ui.other.timeListener.TimeListenerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/7/21
 * 描    述：数据页面
 * 修订历史：
 * ================================================
 */
public class FindFragment extends BaseFragment implements View.OnClickListener {


    @Bind(R.id.tv_music)
    TextView tvMusic;
    @Bind(R.id.tv_player)
    TextView tvPlayer;
    @Bind(R.id.tv_photo)
    TextView tvPhoto;
    @Bind(R.id.tv_dou_movie)
    TextView tvDouMovie;
    @Bind(R.id.tv_dou_music)
    TextView tvDouMusic;
    @Bind(R.id.tv_dou_book)
    TextView tvDouBook;
    @Bind(R.id.tv_model_ts)
    TextView tvModelTs;
    @Bind(R.id.tv_model_sc)
    TextView tvModelSc;
    @Bind(R.id.tv_model_yq)
    TextView tvModelYq;
    @Bind(R.id.vp_pager)
    ViewPager vpPager;
    @Bind(R.id.ll_points)
    LinearLayout llPoints;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private MainActivity activity;

    private List<View> viewPagerList;       //GridView作为一个View对象添加到ViewPager集合中
    private ImageView[] ivPoints;           //小圆点图片的集合
    private int totalPage;                  //总的页数
    private int mPageSize = 8;              //每页显示的最大的数量

    private int[] proPic = {R.drawable.ic_investment, R.drawable.ic_project, R.drawable.ic_financing, R.drawable.ic_show,
            R.drawable.ic_project, R.drawable.ic_show, R.drawable.ic_project, R.drawable.ic_investment,
            R.drawable.ic_investment, R.drawable.ic_project, R.drawable.ic_financing, R.drawable.ic_show};
    private String[] proName = {"猜谜语", "阅读器", "急转弯", "笑话大全", "股票", "身份证", "人脸识别", "时光聆听", "空气质量", "今日油价", "翻译", "缴费"};


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
        return R.layout.fragment_find;
    }

    @Override
    public void initView() {
        iniVpData();
        initViewPager();
    }


    @Override
    public void initListener() {
        tvMusic.setOnClickListener(this);
        tvPhoto.setOnClickListener(this);
        tvPlayer.setOnClickListener(this);
        tvDouMusic.setOnClickListener(this);
        tvDouMovie.setOnClickListener(this);
        tvDouBook.setOnClickListener(this);
        tvModelTs.setOnClickListener(this);
        tvModelSc.setOnClickListener(this);
        tvModelYq.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_music:
                startActivity(MyMusicActivity.class);
                break;
            case R.id.tv_photo:
                startActivity(MyPictureActivity.class);
                break;
            case R.id.tv_player:
                startActivity(MyVideoActivity.class);
                break;
            case R.id.tv_dou_movie:
                startActivity(DouMovieActivity.class);
                break;
            case R.id.tv_dou_book:
                startActivity(DouBookActivity.class);
                break;
            case R.id.tv_dou_music:
                startActivity(DouMusicActivity.class);
                break;
            case R.id.tv_model_ts:
                startActivity(TangShiFirstActivity.class);
                break;
            case R.id.tv_model_sc:
                startActivity(SongCiActivity.class);
                break;
            case R.id.tv_model_yq:
                startActivity(YuanQuActivity.class);
                break;
        }
    }

    private void iniVpData() {
        ArrayList<ImageIconBean> listDatas = new ArrayList<>();
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
                                startActivity(RiddleActivity.class);
                                break;
                            case 1:
                                startActivity(BookReaderActivity.class);
                                break;
                            case 2:
                                startActivity(SharpBendOfBrainActivity.class);
                                break;
                            case 4:

                                break;
                            case 7:
                                startActivity(TimeListenerActivity.class);
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


}
