package com.ns.yc.lifehelper.ui.other.listener;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.listener.view.ListenerFileFragment;
import com.ns.yc.lifehelper.ui.other.listener.view.ListenerListFragment;
import com.ns.yc.lifehelper.ui.other.listener.view.ListenerSettingFragment;
import com.ns.yc.lifehelper.ui.other.listener.view.ListenerTingFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnCheckedChanged;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/9/18
 * 描    述：我的听听页面
 * 修订历史：
 * ================================================
 */
public class ListenerActivity extends BaseActivity {

    @Bind(R.id.tool_bar)
    Toolbar toolBar;
    @Bind(R.id.vp_pager)
    ViewPager vpPager;
    @Bind(R.id.root)
    RelativeLayout root;
    @Bind(R.id.radio_button_play_list)
    RadioButton radioButtonPlayList;
    @Bind(R.id.radio_button_music)
    RadioButton radioButtonMusic;
    @Bind(R.id.radio_button_local_files)
    RadioButton radioButtonLocalFiles;
    @Bind(R.id.radio_button_settings)
    RadioButton radioButtonSettings;
    @Bind(R.id.radio_group_controls)
    RadioGroup radioGroupControls;
    @Bind({R.id.radio_button_play_list, R.id.radio_button_music, R.id.radio_button_local_files, R.id.radio_button_settings})
    List<RadioButton> radioButtons;

    private String[] titles = {"list", "听", "文件夹", "设置"};
    private List<Fragment> fragments = new ArrayList<>();


    @Override
    public int getContentView() {
        return R.layout.activity_listener_home;
    }

    @Override
    public void initView() {
        initToolBar();
        initFragment();
        initRadioGroup();
    }

    private void initToolBar() {
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //设置显示返回箭头和customView
            actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM);
            toolBar.setTitleTextColor(Color.WHITE);
            toolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    private void initFragment() {
        fragments.clear();
        fragments.add(new ListenerListFragment());
        fragments.add(new ListenerTingFragment());
        fragments.add(new ListenerFileFragment());
        fragments.add(new ListenerSettingFragment());

        vpPager.setAdapter(new BasePagerAdapter(getSupportFragmentManager(), fragments));
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                radioButtons.get(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpPager.setCurrentItem(1);
        radioButtons.get(1).setChecked(true);
    }

    private void initRadioGroup() {

    }


    @OnCheckedChanged({R.id.radio_button_play_list, R.id.radio_button_music, R.id.radio_button_local_files, R.id.radio_button_settings})
    public void onRadioButtonChecked(RadioButton button, boolean isChecked) {
        if (isChecked) {
            onItemChecked(radioButtons.indexOf(button));
        }
    }

    private void onItemChecked(int position) {
        vpPager.setCurrentItem(position);
        toolBar.setTitle(titles[position]);
    }

}
