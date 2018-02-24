package com.ns.yc.lifehelper.ui.other.myTsSc.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.ns.yc.lifehelper.api.constantApi.ConstantJsApi;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.ui.other.myKnowledge.cache.SearchHis;
import com.ns.yc.lifehelper.ui.other.myTsSc.adapter.TangShiSearchAdapter;
import com.ns.yc.lifehelper.ui.other.myTsSc.bean.TangShiBean;
import com.ns.yc.lifehelper.ui.other.myTsSc.cache.TsSearchHis;
import com.ns.yc.lifehelper.api.http.tangshi.TangShiModel;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/29
 * 描    述：唐诗宋词元曲搜索页面
 * 修订历史：
 * ================================================
 */
public class TangShiSearchActivity extends BaseActivity implements View.OnClickListener {

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
    @Bind(R.id.tv_clean_his)
    TextView tvCleanHis;
    @Bind(R.id.ll_search_start)
    LinearLayout llSearchStart;
    @Bind(R.id.ll_search_after)
    LinearLayout llSearchAfter;
    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    private Realm realm;
    private RealmResults<TsSearchHis> searchHises;
    private String type;
    private TangShiSearchAdapter searchAdapter;
    private TangShiSearchAdapter hisAdapter;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if(realm!=null){
            realm.close();
        }*/
    }

    @Override
    public int getContentView() {
        return R.layout.base_search_view;
    }

    @Override
    public void initView() {
        initIntentData();
        initSearchView();
        initRealm();
        initHisRecycleView();
        initSearchRecycleView();
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if(intent!=null){
            type = intent.getStringExtra("type");
        }
        if(TextUtils.isEmpty(type)){
            type = "ts";
        }
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
        ivClean.setOnClickListener(this);
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
                    ivClean.setVisibility(View.VISIBLE);
                } else {
                    tvClick.setText("取消");
                    tvClick.setTextColor(getResources().getColor(R.color.colorWhite));
                    tvPutIn.setVisibility(View.VISIBLE);
                    etContent.setVisibility(View.GONE);
                    ivClean.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Editable editableText = etContent.getEditableText();
                Selection.setSelection(s, s.length());                            // 将光标移动到最后
            }
        });
        searchAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(position>-1 && searchAdapter.getAllData().size()>position){
                    TangShiBean.ResultBean.ListBean listBean = searchAdapter.getAllData().get(position);
                    Intent intent = new Intent(TangShiSearchActivity.this,TangShiSearchDetailActivity.class);
                    intent.putExtra("type",listBean.getType());
                    intent.putExtra("title",listBean.getTitle());
                    intent.putExtra("appreciation",listBean.getAppreciation());
                    intent.putExtra("author",listBean.getAuthor());
                    intent.putExtra("content",listBean.getContent());
                    intent.putExtra("explanation",listBean.getExplanation());
                    intent.putExtra("translation",listBean.getTranslation());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void initData() {
        getHotTags();
        getHisTags();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_put_in:
                tvPutIn.setVisibility(View.GONE);
                etContent.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_clean:
                tvPutIn.setVisibility(View.GONE);
                etContent.setVisibility(View.VISIBLE);
                etContent.setText("");
                break;
            case R.id.tv_click:
                String click = tvClick.getText().toString().trim();
                if (click.equals("搜索")) {
                    String k = etContent.getText().toString().trim();
                    if(!TextUtils.isEmpty(k)){
                        addRealm(k);
                        llSearchStart.setVisibility(View.GONE);
                        llSearchAfter.setVisibility(View.VISIBLE);
                        recyclerView.showProgress();
                        startSearch(k);
                    } else {
                        ToastUtils.showShortSafe("输入搜索内容不能为空");
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            String k = etContent.getText().toString().trim();
            if(TextUtils.isEmpty(k)){
                addRealm(k);
                llSearchStart.setVisibility(View.GONE);
                llSearchAfter.setVisibility(View.VISIBLE);
                recyclerView.showProgress();
                startSearch(k);
            } else {
                ToastUtils.showShortSafe("输入搜索内容不能为空");
            }
            return true;
        } else if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return false;
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



    private void addRealm(String k) {
        initRealm();
        if (realm!=null && realm.where(TsSearchHis.class).findAll() != null) {
            searchHises = realm.where(TsSearchHis.class).findAll();
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

        } else {
            ToastUtils.showShortSafe("清除历史记录");
        }
    }


    private void initHisRecycleView() {
        rvSearchHis.setLayoutManager(new LinearLayoutManager(this));
        final RecycleViewItemLine line = new RecycleViewItemLine(this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        rvSearchHis.addItemDecoration(line);
        hisAdapter = new TangShiSearchAdapter(TangShiSearchActivity.this);
        rvSearchHis.setAdapter(hisAdapter);
    }


    private void initSearchRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final RecycleViewItemLine line = new RecycleViewItemLine(this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        recyclerView.addItemDecoration(line);
        searchAdapter = new TangShiSearchAdapter(TangShiSearchActivity.this);
        recyclerView.setAdapter(searchAdapter);
    }


    private String[] hot_tags = {"苏轼","欧阳修","王安石","苏澈","温庭筠","李煜","范仲淹","晏殊","李清照"};
    private void getHotTags() {
        final List<String> tags = new ArrayList<>();
        for(int a=0 ; a<hot_tags.length ; a++){
            tags.add(hot_tags[a]);
        }
        tflSearchHot.setAdapter(new TagAdapter<String>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                LayoutInflater from = LayoutInflater.from(TangShiSearchActivity.this);
                TextView tv = (TextView) from.inflate(R.layout.tag_hot, tflSearchHot, false);
                tv.setText(o);
                return tv;
            }
        });
        tflSearchHot.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                ToastUtils.showShortSafe(hot_tags[position]);
                return true;
            }
        });
    }

    private void getHisTags() {
        if (realm!=null && realm.where(TsSearchHis.class).findAll() != null) {
            searchHises = realm.where(TsSearchHis.class).findAll();
        } else {
            return;
        }
        TangShiBean.ResultBean.ListBean listBean = null;
        for(int a=0 ; a<searchHises.size() ; a++){
            listBean = new TangShiBean.ResultBean.ListBean();
            listBean.setTitle(searchHises.get(a).getName());
        }
    }


    private void startSearch(String k) {
        TangShiModel model = TangShiModel.getInstance(this);
        Observable<TangShiBean> search = null;
        switch (type){
            case "ts":
                search = model.getTangShiSearch(ConstantJsApi.JsAppKey, k);
                break;
            case "sc":
                search = model.getSongCiSearch(ConstantJsApi.JsAppKey, k);
                break;
            case "yq":
                search = model.getYuanQuSearch(ConstantJsApi.JsAppKey, k);
                break;
        }

        if(search!=null){
            search.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<TangShiBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(TangShiBean tangShiBean) {
                            if(searchAdapter==null){
                                searchAdapter = new TangShiSearchAdapter(TangShiSearchActivity.this);
                            }

                            if(tangShiBean!=null && tangShiBean.getResult()!=null && tangShiBean.getResult().getList()!=null){
                                recyclerView.showRecycler();
                                searchAdapter.addAll(tangShiBean.getResult().getList());
                                searchAdapter.notifyDataSetChanged();

                            } else {
                                recyclerView.showError();
                                recyclerView.setErrorView(R.layout.view_custom_empty_data);
                            }
                        }
                    });
        }
    }


}
