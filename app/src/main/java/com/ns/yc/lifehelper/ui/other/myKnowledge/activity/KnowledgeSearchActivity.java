package com.ns.yc.lifehelper.ui.other.myKnowledge.activity;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp.BaseActivity;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.base.adapter.BasePagerAdapter;
import com.ns.yc.lifehelper.ui.other.myKnowledge.adapter.KnowledgeSearchAdapter;
import com.ns.yc.lifehelper.ui.other.myKnowledge.bean.SearchHotBean;
import com.ns.yc.lifehelper.ui.other.myKnowledge.cache.SearchHis;
import com.ns.yc.lifehelper.ui.other.myKnowledge.fragment.KnowledgeSearchAllFragment;
import com.ns.yc.lifehelper.ui.other.myKnowledge.fragment.KnowledgeSearchFragment;
import com.ns.yc.lifehelper.ui.other.myKnowledge.model.KnowledgeSearchHotModel;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/28
 * 描    述：我的干货搜索页面
 * 修订历史：
 * ================================================
 */
public class KnowledgeSearchActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_put_in)
    TextView tvPutIn;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.iv_clean)
    ImageView ivClean;
    @Bind(R.id.fl_search)
    FrameLayout flSearch;
    @Bind(R.id.tv_click)
    TextView tvClick;
    @Bind(R.id.tfl_search_hot)
    TagFlowLayout tflSearchHot;
    @Bind(R.id.rv_search_his)
    RecyclerView rvSearchHis;
    @Bind(R.id.ll_search_start)
    LinearLayout llSearchStart;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.vp_content)
    ViewPager vpContent;
    @Bind(R.id.ll_search_after)
    LinearLayout llSearchAfter;
    @Bind(R.id.tv_clean_his)
    TextView tvCleanHis;
    private Realm realm;
    private RealmResults<SearchHis> searchHises;
    private List<String> hisBeen = new ArrayList<>();
    private KnowledgeSearchAdapter hisAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(realm!=null){
            realm.close();
        }
    }

    @Override
    public int getContentView() {
        return R.layout.activity_knowledge_search;
    }

    @Override
    public void initView() {
        initSearchView();
        initRealm();
        initHisRecycleView();
    }

    private void initSearchView() {
        llSearchStart.setVisibility(View.VISIBLE);
        llSearchAfter.setVisibility(View.GONE);
    }

    private void initRealm() {
        if(realm == null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }

    @Override
    public void initListener() {
        tvPutIn.setOnClickListener(this);
        tvClick.setOnClickListener(this);
        tvCleanHis.setOnClickListener(this);
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    tvClick.setText("搜索");
                    tvClick.setTextColor(getResources().getColor(R.color.redTab));
                    tvPutIn.setVisibility(View.GONE);
                    etContent.setVisibility(View.VISIBLE);
                } else {
                    tvClick.setText("取消");
                    tvClick.setTextColor(getResources().getColor(R.color.colorWhite));
                    tvPutIn.setVisibility(View.VISIBLE);
                    etContent.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Editable editableText = etContent.getEditableText();
                Selection.setSelection(s, s.length());                            // 将光标移动到最后
            }
        });
    }

    @Override
    public void initData() {
        getHotTags();
        getHisTags();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            String k = etContent.getText().toString().trim();
            if(TextUtils.isEmpty(k)){
                addRealm(k);
                initTabLayout(k);
            } else {
                ToastUtils.showShort("输入搜索内容不能为空");
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_put_in:
                tvPutIn.setVisibility(View.GONE);
                etContent.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_click:
                String click = tvClick.getText().toString().trim();
                if (click.equals("搜索")) {
                    String k = etContent.getText().toString().trim();
                    if(!TextUtils.isEmpty(k)){
                        addRealm(k);
                        initTabLayout(k);
                    } else {
                        ToastUtils.showShort("输入搜索内容不能为空");
                    }
                } else if (click.equals("取消")) {
                    finish();
                }
                break;
            case R.id.tv_clean_his:
                cleanHisRealm();
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }




    private void initHisRecycleView() {
        rvSearchHis.setLayoutManager(new LinearLayoutManager(this));
        final RecycleViewItemLine line = new RecycleViewItemLine(this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        rvSearchHis.addItemDecoration(line);
        hisAdapter = new KnowledgeSearchAdapter(hisBeen,this);
        rvSearchHis.setAdapter(hisAdapter);
    }

    private String[] topId = {"0", "7", "6", "5", "4", "8", "9"};
    private String[] top = {"全部", "资讯", "融资事件", "项目", "机构", "投资人", "创业者"};
    private void initTabLayout(final String k) {
        llSearchStart.setVisibility(View.GONE);
        llSearchAfter.setVisibility(View.VISIBLE);

        ArrayList<String> mTitleList = new ArrayList<>();
        ArrayList<Fragment> mFragments = new ArrayList<>();
        for (int a=0 ; a<top.length ; a++){
            mTitleList.add(top[a]);
        }
        mFragments.add(new KnowledgeSearchAllFragment());
        for(int a=1 ; a<top.length ; a++){
            mFragments.add(KnowledgeSearchFragment.newInstance("资讯"));
        }

        /**
         * 注意使用的是：getChildFragmentManager，
         * 这样setOffscreenPageLimit()就可以添加上，保留相邻2个实例，切换时不会卡
         * 但会内存溢出，在显示时加载数据
         */
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        final BasePagerAdapter myAdapter = new BasePagerAdapter(supportFragmentManager, mFragments, mTitleList);
        vpContent.setAdapter(myAdapter);
        // 左右预加载页面的个数
        vpContent.setOffscreenPageLimit(1);
        myAdapter.notifyDataSetChanged();
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(vpContent);
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    KnowledgeSearchAllFragment allFragment = (KnowledgeSearchAllFragment) myAdapter.getItem(0);
                } else {
                    KnowledgeSearchFragment fragment = (KnowledgeSearchFragment) myAdapter.getItem(position);
                    fragment.doSearch(k,topId[position],"");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 获取热门记录
     */
    private void getHotTags() {
        KnowledgeSearchHotModel hotModel = KnowledgeSearchHotModel.getInstance(this);
        hotModel.getHotTag()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchHotBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SearchHotBean searchHotBean) {
                        if (searchHotBean != null && searchHotBean.getData() != null) {
                            List<SearchHotBean.DataBean.DataBeanHot> data = searchHotBean.getData().getData();
                            initHotTags(data);
                            startCacheHotTags();
                        }
                    }
                });
    }


    /**
     * 获取历史记录
     */
    private void getHisTags() {
        initRealm();
        if (realm!=null && realm.where(SearchHis.class).findAll() != null) {
            searchHises = realm.where(SearchHis.class).findAll();
        } else {
            tvCleanHis.setVisibility(View.GONE);
            return;
        }
        if(searchHises.size()>0){
            tvCleanHis.setVisibility(View.VISIBLE);
            hisBeen.clear();
            for(int a=0 ; a<searchHises.size() ; a++){
                hisBeen.add(searchHises.get(a).getName());
            }
            if(hisAdapter!=null){
                hisAdapter.notifyDataSetChanged();
            } else {
                hisAdapter = new KnowledgeSearchAdapter(hisBeen,this);
                rvSearchHis.setAdapter(hisAdapter);
            }
        } else {
            tvCleanHis.setVisibility(View.GONE);
        }
    }


    private void addRealm(String k) {
        initRealm();
        if (realm!=null && realm.where(SearchHis.class).findAll() != null) {
            searchHises = realm.where(SearchHis.class).findAll();
        } else {
            return;
        }
        //去重
        for (int i = 0; i < searchHises.size(); i++) {
            if (k.equals(searchHises.get(i).getName())) {
                realm.beginTransaction();
                searchHises.clear();
                realm.commitTransaction();
                i--;
            }
        }

        realm.beginTransaction();
        SearchHis his = realm.createObject(SearchHis.class);
        his.setName(k);
        realm.commitTransaction();
    }


    private void cleanHisRealm() {
        initRealm();
        if(realm!=null && searchHises!=null){
            realm.beginTransaction();
            searchHises.clear();
            realm.commitTransaction();
            hisBeen.clear();
            if (hisAdapter != null){
                hisAdapter.notifyDataSetChanged();
            }
        } else {
            ToastUtils.showShort("清除历史记录");
        }
    }


    private void initHotTags(final List<SearchHotBean.DataBean.DataBeanHot> data) {
        tflSearchHot.setAdapter(new TagAdapter(data) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                LayoutInflater from = LayoutInflater.from(KnowledgeSearchActivity.this);
                TextView tv = (TextView) from.inflate(R.layout.tag_tv_view, tflSearchHot, false);
                tv.setText(data.get(position).getTitle());
                return tv;
            }
        });
        tflSearchHot.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                etContent.setText(data.get(position).getTitle());
                etContent.setVisibility(View.VISIBLE);
                tvPutIn.setVisibility(View.GONE);
                return true;
            }
        });
    }

    /**
     * 缓存热门搜索标签
     */
    private void startCacheHotTags() {

    }

}
