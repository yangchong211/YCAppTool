package com.ns.yc.lifehelper.ui.other.myPicture.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp.BaseActivity;
import com.ns.yc.lifehelper.ui.other.myPicture.bean.PicBeautifulContentBean;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/4
 * 描    述：美图查看大图
 * 修订历史：
 * ================================================
 */
public class MyPicLargeActivity extends BaseActivity {

    private static final int SYSTEM_UI_BASE_VISIBILITY = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
    private static final int SYSTEM_UI_IMMERSIVE = View.SYSTEM_UI_FLAG_IMMERSIVE
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;

    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.tool_bar)
    Toolbar toolBar;
    private int index;
    private String groupId;
    private Realm realm;
    private RealmResults<PicBeautifulContentBean> images;
    private PagerAdapter adapter;

    @Override
    public int getContentView() {
        return R.layout.activity_my_pic_large;
    }

    @Override
    public void initView() {
        initToolBar();
        initIntentData();
        initRealm();
        initViewPager();
    }

    private void initToolBar() {
        toolBar.setNavigationIcon(R.drawable.ic_back_white);
        toolBar.setTitle("大图");
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });
    }

    private void initIntentData() {
        if(getIntent()!=null){
            index = getIntent().getIntExtra("index", 0);
            groupId = getIntent().getStringExtra("groupId");
        }
    }

    private void initRealm() {
        realm = Realm.getDefaultInstance();
        if( realm.where(PicBeautifulContentBean.class).findAll()!=null){
            images = realm.where(PicBeautifulContentBean.class)
                    .equalTo("groupId", groupId)
                    .findAll();
        }

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void supportFinishAfterTransition() {
        Intent data = new Intent();
        data.putExtra("index", pager.getCurrentItem());
        setResult(RESULT_OK, data);
        super.supportFinishAfterTransition();
    }


    private void initViewPager() {
        adapter = new PagerAdapter();
        pager.setAdapter(adapter);
        pager.setCurrentItem(index);
        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                PicBeautifulContentBean image = images.get(pager.getCurrentItem());
                LargePicFragment fragment = (LargePicFragment) adapter.instantiateItem(pager, pager.getCurrentItem());
                sharedElements.clear();
                sharedElements.put(image.getUrl(), fragment.getSharedElement());
            }
        });
    }


    public void toggleToolbar() {
        if (toolBar.getTranslationY() == 0) {
            hideSystemUi();
        } else {
            showSystemUi();
        }
    }

    private void showSystemUi() {
        pager.setSystemUiVisibility(SYSTEM_UI_BASE_VISIBILITY);
        toolBar.animate()
                .translationY(0)
                .setDuration(400)
                .start();
    }

    private void hideSystemUi() {
        pager.setSystemUiVisibility(SYSTEM_UI_BASE_VISIBILITY | SYSTEM_UI_IMMERSIVE);
        toolBar.animate()
                .translationY(-toolBar.getHeight())
                .setDuration(400)
                .start();
    }


    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Fragment getItem(int position) {
            return LargePicFragment.newFragment(images.get(position).getUrl(), position == index);
        }

    }

}
