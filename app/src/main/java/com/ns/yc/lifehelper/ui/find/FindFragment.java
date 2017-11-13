package com.ns.yc.lifehelper.ui.find;

import android.content.Context;
import android.content.res.TypedArray;
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
import com.ns.yc.lifehelper.ui.data.view.activity.DoodleViewActivity;
import com.ns.yc.lifehelper.ui.find.view.activity.FastLookActivity;
import com.ns.yc.lifehelper.ui.find.view.activity.RiddleActivity;
import com.ns.yc.lifehelper.ui.main.view.activity.MainActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.view.BookReaderActivity;
import com.ns.yc.lifehelper.ui.other.douBook.view.DouBookActivity;
import com.ns.yc.lifehelper.ui.other.douMovie.view.DouMovieActivity;
import com.ns.yc.lifehelper.ui.other.douMusic.view.DouMusicActivity;
import com.ns.yc.lifehelper.ui.other.gank.view.activity.GanKHomeActivity;
import com.ns.yc.lifehelper.ui.other.mobilePlayer.MobilePlayerActivity;
import com.ns.yc.lifehelper.ui.other.myMusic.MyMusicActivity;
import com.ns.yc.lifehelper.ui.other.myPicture.MyPictureActivity;
import com.ns.yc.lifehelper.ui.other.myTsSc.SongCiActivity;
import com.ns.yc.lifehelper.ui.other.myTsSc.YuanQuActivity;
import com.ns.yc.lifehelper.ui.other.myTsSc.view.TangShiFirstActivity;
import com.ns.yc.lifehelper.ui.other.myVideo.MyVideoActivity;
import com.ns.yc.lifehelper.ui.other.notePad.NotePadActivity;
import com.ns.yc.lifehelper.ui.other.sharpBendOfBrain.SharpBendOfBrainActivity;
import com.ns.yc.lifehelper.ui.other.timeListener.TimeListenerActivity;
import com.ns.yc.lifehelper.ui.other.toDo.view.ToDoTimerActivity;
import com.ns.yc.lifehelper.ui.other.weather.SevenWeatherActivity;

import java.util.ArrayList;

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
    @Bind(R.id.tv_reader)
    TextView tvReader;
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
    }


    @Override
    public void initListener() {
        tvMusic.setOnClickListener(this);
        tvPhoto.setOnClickListener(this);
        tvPlayer.setOnClickListener(this);
        tvReader.setOnClickListener(this);
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
            case R.id.tv_reader:
                startActivity(BookReaderActivity.class);
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
        int mPageSize = 8;          //每页显示的最大的数量
        ArrayList<ImageIconBean> listDatas = new ArrayList<>();
        TypedArray proPic = activity.getResources().obtainTypedArray(R.array.find_pro_pic);
        String[] proName = activity.getResources().getStringArray(R.array.find_pro_title);
        for (int i = 0; i < proName.length; i++) {
            int proPicId = proPic.getResourceId(i, R.drawable.ic_investment);
            listDatas.add(new ImageIconBean(proName[i], proPicId));
        }
        proPic.recycle();
        //总的页数向上取整  //总的页数
        final int totalPage = (int) Math.ceil(listDatas.size() * 1.0 / mPageSize);
        ArrayList<View> viewPagerList = new ArrayList<>();
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
                                startActivity(GanKHomeActivity.class);
                                break;
                            case 1:
                                startActivity(MobilePlayerActivity.class);
                                break;
                            case 2:
                                startActivity(NotePadActivity.class);
                                break;
                            case 3:
                                startActivity(ToDoTimerActivity.class);
                                break;
                            case 4:
                                startActivity(SevenWeatherActivity.class);
                                break;
                            case 5:
                                startActivity(DoodleViewActivity.class);
                                break;
                            case 6:
                                startActivity(FastLookActivity.class);
                                break;
                            case 7:
                                startActivity(TimeListenerActivity.class);
                                break;
                            case 8:
                                startActivity(SharpBendOfBrainActivity.class);
                                break;
                            case 9:
                                startActivity(RiddleActivity.class);
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
