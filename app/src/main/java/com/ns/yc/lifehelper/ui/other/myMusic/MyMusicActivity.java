package com.ns.yc.lifehelper.ui.other.myMusic;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.base.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.myMusic.activity.MyMusicSearchActivity;
import com.ns.yc.lifehelper.ui.other.myMusic.fragment.MyMusicNetFragment;
import com.ns.yc.lifehelper.ui.other.myMusic.fragment.MyMusicLocalFragment;
import com.ns.yc.lifehelper.ui.other.myMusic.fragment.MyMusicSocialFragment;
import com.ns.yc.lifehelper.ui.other.myMusic.weight.OutPlayerController;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/25
 * 描    述：我的音乐页面
 * 修订历史：
 * ================================================
 */
public class MyMusicActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.vp_pager)
    ViewPager vpPager;
    @Bind(R.id.player_controller)
    OutPlayerController playerController;
    private RadioGroup radioGroup;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_my_music_main;
    }

    @Override
    public void initView() {
        initToolbar();
        initRadioGroup();
        initViewPager();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_music_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search) {
            startActivity(MyMusicSearchActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //设置显示返回箭头和customView
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM);
        radioGroup = (RadioGroup) LayoutInflater.from(this).inflate(R.layout.music_main_toolbar_head, null);
        radioGroup.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        actionBar.setCustomView(radioGroup, layoutParams);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    boolean fromViewPager;
    boolean fromRadioGroup;
    private void initRadioGroup() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (fromViewPager) {
                    fromViewPager = false;
                    return;
                }
                fromRadioGroup = true;
                switch (checkedId) {
                    case R.id.net_music:
                        vpPager.setCurrentItem(0, true);
                        break;
                    case R.id.local_music:
                        vpPager.setCurrentItem(1, true);
                        break;
                    case R.id.social:
                        vpPager.setCurrentItem(2, true);
                        break;
                    default:
                }
            }
        });
    }


    private void initViewPager() {
        fragments.clear();
        fragments.add(new MyMusicNetFragment());
        fragments.add(new MyMusicLocalFragment());
        fragments.add(new MyMusicSocialFragment());

        vpPager.setAdapter(new BasePagerAdapter(getSupportFragmentManager(),fragments));
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (fromRadioGroup) {
                    fromRadioGroup = false;
                    return;
                }
                fromViewPager = true;
                ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpPager.setCurrentItem(1);
    }

}
