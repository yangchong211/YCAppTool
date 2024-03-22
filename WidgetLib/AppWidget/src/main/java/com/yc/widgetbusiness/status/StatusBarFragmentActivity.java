package com.yc.widgetbusiness.status;

import android.app.Activity;
import android.os.Bundle;

import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yc.statusbar.bar.StateAppBar;
import com.yc.statusbar.utils.StatusBarUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.ycbjie.ycstatusbar.R;


public class StatusBarFragmentActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewpager;

    /**
     * 方法1：布局里添加占位状态栏
     * 对一个Activity中有多个Fragment，每个Fragment都有不同的状态栏颜色
     * 1.在activity中设置状态栏隐藏或者透明
     * 2.在activity中设置[如果不在activity中设置，则需要在activity中的fragment都设置]
     *   android:fitsSystemWindows="true"
     * 3.添加空的view就是用来填充状态栏的
     * 4.设置view的颜色
     *      遇到问题：由于设置了主题背景颜色为白色，默认白色。因此，即使设置View的颜色透明也无法生效
     *
     *
     *
     * 方法2：
     * 如果是单Activity多Fragment，由Fragment控制状态栏颜色的应用，有两种方案：
     * 1.由Activity控制状态栏背景颜色和字体颜色，提供方法供Fragment调用即可。
     * 2.首先设置Activity侵入状态栏，并设置状态栏为透明色，相当于隐藏Activity的状态栏，
     * 然后在BaseFragment中封装状态栏（使用StatusView），由Fragment控制自己的颜色即可；
     * 但是状态栏字体颜色还是需要通过Activity控制。
     *
     *
     *
     * Android4.4以上系统版本可以修改状态栏颜色，但是只有小米的MIUI、魅族的Flyme和Android6.0以上系统可以把
     * 状态栏文字和图标换成深色，其他的系统状态栏文字都是白色的，换成浅色背景的话就看不到了。
     */

    /**
     * 出现的问题：
     * 如果是在设置fragment中，有的是白色【或者其他色】，有的是透明色【就相当于隐藏了状态栏】，则还是会出现设置透明色无效
     *
     * 思路：
     * 这其实更像是一个效果，而不是问题，透明色时应该显示了下面的Fragment的颜色，所以看起来无效。
     * 实际上每一个Activity的Window都有背景色，如果你把Activity的Window设置为透明色，也会出现这个效果，
     * 所以你应该给Fragment的RootView设置一个背景色，才能更像一个Activity，再设置透明色就没有问题了。
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statusbar_fragment);
        StateAppBar.setStatusBarColor(StatusBarFragmentActivity.this,
                ContextCompat.getColor(StatusBarFragmentActivity.this,
                        R.color.colorTheme));

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        ArrayList<String> titleList = new ArrayList<>();
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        BasePagerAdapter pagerAdapter = new BasePagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewpager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
        titleList.add("黑色");
        titleList.add("红色");
        titleList.add("蓝色");
        titleList.add("透明色");
        titleList.add("白色");
        fragmentList.add(new StatusBarFirstFragment());
        fragmentList.add(new StatusBarSecondFragment());
        fragmentList.add(new StatusBarThirdFragment());
        fragmentList.add(new StatusBarFourFragment());
        fragmentList.add(new StatusBarFourFragment());
        for (int i = 0; i < titleList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titleList.get(i)));
        }
        pagerAdapter.notifyDataSetChanged();
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        //设置状态栏为黑色
                        StateAppBar.setStatusBarColor(StatusBarFragmentActivity.this,
                                ContextCompat.getColor(StatusBarFragmentActivity.this,
                                        R.color.colorTheme));
                        break;
                    case 1:
                        //设置状态栏为红色
                        StateAppBar.setStatusBarColor(StatusBarFragmentActivity.this,
                                ContextCompat.getColor(StatusBarFragmentActivity.this,
                                        R.color.colorAccent));
                        break;
                    case 2:
                        //设置状态栏为蓝色
                        StateAppBar.setStatusBarColor(StatusBarFragmentActivity.this,
                                ContextCompat.getColor(StatusBarFragmentActivity.this,
                                        R.color.colorPrimary));
                        break;
                    case 3:
                        //设置状态栏为透明，相当于隐藏状态栏，也称之为沉浸式状态栏
                        StateAppBar.translucentStatusBar(StatusBarFragmentActivity.this,
                                true);
                        break;
                    case 4:
                        //设置状态栏为白色
                        StateAppBar.setStatusBarColor(StatusBarFragmentActivity.this,
                                ContextCompat.getColor(StatusBarFragmentActivity.this,
                                        R.color.white));
                        //状态栏亮色模式，设置状态栏黑色文字、图标
                        StatusBarUtils.StatusBarLightMode(StatusBarFragmentActivity.this);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public class BasePagerAdapter extends FragmentPagerAdapter {

        private List<?> mFragment;
        private List<String> mTitleList;

        /**
         * 普通，主页使用
         */
        public BasePagerAdapter(FragmentManager fm, List<?> mFragment) {
            super(fm);
            this.mFragment = mFragment;
        }

        /**
         * 接收首页传递的标题
         */
        public BasePagerAdapter(FragmentManager fm, List<?> mFragment, List<String> mTitleList) {
            super(fm);
            this.mFragment = mFragment;
            this.mTitleList = mTitleList;
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) mFragment.get(position);
        }

        @Override
        public int getCount() {
            return mFragment.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        /**
         * 首页显示title，每日推荐等..
         * 若有问题，移到对应单独页面
         */
        @Override
        public CharSequence getPageTitle(int position) {
            if (mTitleList != null) {
                return mTitleList.get(position);
            } else {
                return "";
            }
        }

        public void addFragmentList(List<?> fragment) {
            this.mFragment.clear();
            this.mFragment = null;
            this.mFragment = fragment;
            notifyDataSetChanged();
        }

    }


    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     * @param activity 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        Window window = activity.getWindow();
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;
            }catch (Exception e){

            }
        }
        return result;
    }


    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * @param activity 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public boolean FlymeSetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }


}
