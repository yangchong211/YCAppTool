package com.ns.yc.lifehelper.ui.other.myTsSc;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp.BaseActivity;
import com.ns.yc.lifehelper.ui.other.myTsSc.view.TangShiFirstActivity;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/29
 * 描    述：唐诗页面，分类页面
 * 修订历史：
 * ================================================
 */
public class TangShiActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.ll_search)
    FrameLayout llSearch;
    @Bind(R.id.rl_ts_first)
    RelativeLayout rlTsFirst;
    @Bind(R.id.rl_ts_second)
    RelativeLayout rlTsSecond;
    @Bind(R.id.rl_ts_three)
    RelativeLayout rlTsThree;
    @Bind(R.id.rl_ts_four)
    RelativeLayout rlTsFour;
    @Bind(R.id.rl_ts_five)
    RelativeLayout rlTsFive;
    @Bind(R.id.rl_ts_six)
    RelativeLayout rlTsSix;
    @Bind(R.id.rl_ts_seven)
    RelativeLayout rlTsSeven;
    @Bind(R.id.rl_ts_eight)
    RelativeLayout rlTsEight;

    @Override
    public int getContentView() {
        return R.layout.activity_my_ts_shi;
    }

    @Override
    public void initView() {
        initToolBar();
    }

    private void initToolBar() {
        toolbarTitle.setText("唐诗");
        llSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        llSearch.setOnClickListener(this);
        rlTsFirst.setOnClickListener(this);
        rlTsSecond.setOnClickListener(this);
        rlTsThree.setOnClickListener(this);
        rlTsFour.setOnClickListener(this);
        rlTsFive.setOnClickListener(this);
        rlTsSix.setOnClickListener(this);
        rlTsSeven.setOnClickListener(this);
        rlTsEight.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        Intent intent ;
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.ll_search:

                break;
            case R.id.rl_ts_first:
                intent = new Intent(this,TangShiFirstActivity.class);
                //intent.putExtra("search","五言古诗");
                intent.putExtra("search","花");
                startActivity(intent);
                break;
            case R.id.rl_ts_second:
                intent = new Intent(this,TangShiFirstActivity.class);
                //intent.putExtra("search","五言乐府");
                intent.putExtra("search","雪");
                startActivity(intent);
                break;
            case R.id.rl_ts_three:
                intent = new Intent(this,TangShiFirstActivity.class);
                //intent.putExtra("search","七言古诗");
                intent.putExtra("search","树");
                startActivity(intent);
                break;
            case R.id.rl_ts_four:
                intent = new Intent(this,TangShiFirstActivity.class);
                //intent.putExtra("search","七言乐府");
                intent.putExtra("search","思");
                startActivity(intent);
                break;
            case R.id.rl_ts_five:
                intent = new Intent(this,TangShiFirstActivity.class);
                //intent.putExtra("search","五言律诗");
                intent.putExtra("search","诗");
                startActivity(intent);
                break;
            case R.id.rl_ts_six:
                intent = new Intent(this,TangShiFirstActivity.class);
                //intent.putExtra("search","七言律诗");
                intent.putExtra("search","友");
                startActivity(intent);
                break;
            case R.id.rl_ts_seven:
                intent = new Intent(this,TangShiFirstActivity.class);
                //intent.putExtra("search","五言绝句");
                intent.putExtra("search","情");
                startActivity(intent);
                break;
            case R.id.rl_ts_eight:
                intent = new Intent(this,TangShiFirstActivity.class);
                //intent.putExtra("search","七言绝句");
                intent.putExtra("search","夜");
                startActivity(intent);
                break;
        }
    }
}
