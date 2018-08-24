package com.ns.yc.lifehelper.ui.other.expressDelivery.indexModel;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantALiYunApi;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.base.app.BaseApplication;
import com.ns.yc.lifehelper.db.cache.CacheExpressCompany;
import com.ns.yc.lifehelper.ui.other.expressDelivery.bean.ExpressDeliveryBean;
import com.ns.yc.lifehelper.api.http.expressDelivery.ExpressDeliveryModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmResults;
import me.yokeyword.indexablerv.EntityWrapper;
import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableLayout;
import me.yokeyword.indexablerv.SimpleHeaderAdapter;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/6/23
 * 描    述：城市选择页面
 * 修订历史：
 * 备注：此搜索功能更为强大，搜索结果可以是多条，比如输入‘安’，那么结果便是以安开头的所有城市
 * ================================================
 */
public class SelectorCompanyActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.tv_title_left)
    TextView tvTitleLeft;
    @BindView(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.ll_search)
    FrameLayout llSearch;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_start_put_in)
    TextView tvStartPutIn;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.iv_clean)
    ImageView ivClean;
    @BindView(R.id.fl_search)
    FrameLayout flSearch;
    @BindView(R.id.index_layout)
    IndexableLayout indexLayout;
    @BindView(R.id.progress)
    FrameLayout progress;
    private CompanySearchFragment searchFragment;
    private List<ExpressDeliveryEntity> mData;
    private CompanyAdapter adapter;
    private SimpleHeaderAdapter<ExpressDeliveryEntity> mHotCityAdapter;
    private Realm realm;
    private RealmResults<CacheExpressCompany> cacheExpressCompanies;

    @Override
    public void onBackPressed() {
        if (!searchFragment.isHidden()) {
            getSupportFragmentManager().beginTransaction().hide(searchFragment).commit();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if(realm!=null){
            realm.close();
        }*/
    }

    @Override
    public int getContentView() {
        return R.layout.activity_express_delivery_index;
    }

    @Override
    public void initView() {
        initToolBar();
        initSearchBar();
        initRealm();
        initIndexAbleLayout();
        initClickListener();
    }

    private void initRealm() {
        if(realm == null){
            realm = BaseApplication.getInstance().getRealmHelper();
        }
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        //long time = TimeUtils.date2Millis(new Date());
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new Date());
        if(TimeUtils.isToday(date)){
            readCacheExpressCompany();
        }else {
            getData();
        }
    }

    private void initToolBar() {
        llTitleMenu.setVisibility(View.VISIBLE);
        toolbarTitle.setText("选择快递公司");
    }

    private void initClickListener() {
        llTitleMenu.setOnClickListener(this);
        tvStartPutIn.setOnClickListener(this);
        ivClean.setOnClickListener(this);
    }

    private void initSearchBar() {
        tvStartPutIn.setVisibility(View.VISIBLE);
        etContent.setVisibility(View.GONE);
        ivClean.setVisibility(View.GONE);
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvStartPutIn.setVisibility(View.GONE);
                etContent.setVisibility(View.VISIBLE);
                ivClean.setVisibility(View.VISIBLE);
                if (charSequence.length() > 0) {
                    if (searchFragment.isHidden()) {
                        getSupportFragmentManager().beginTransaction().show(searchFragment).commit();
                    }
                } else {
                    if (!searchFragment.isHidden()) {
                        getSupportFragmentManager().beginTransaction().hide(searchFragment).commit();
                    }
                }
                searchFragment.bindQueryText(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.tv_start_put_in:
                tvStartPutIn.setVisibility(View.GONE);
                etContent.setVisibility(View.VISIBLE);
                ivClean.setVisibility(View.GONE);
                break;
            case R.id.iv_clean:
                etContent.setText("");
                tvStartPutIn.setVisibility(View.GONE);
                etContent.setVisibility(View.VISIBLE);
                ivClean.setVisibility(View.GONE);
                break;
        }
    }

    private void initIndexAbleLayout() {
        searchFragment = (CompanySearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_fragment);
        indexLayout.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CompanyAdapter(this);
        indexLayout.setAdapter(adapter);
        setListener();
        initSearch();
    }

    /**
     * 初始化数据
     */
    private List<ExpressDeliveryEntity> initData(List<ExpressDeliveryBean.ResultBean> company) {
        List<ExpressDeliveryEntity> list = new ArrayList<>();
        for (int a = 0; a < company.size(); a++) {
            ExpressDeliveryEntity entity = new ExpressDeliveryEntity();
            entity.setName(company.get(a).getName());
            entity.setNumber(company.get(a).getNumber());
            entity.setType(company.get(a).getType());
            list.add(entity);
        }
        return list;
    }



    /**
     * 设置点击事件
     */
    private void setListener() {
        adapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<ExpressDeliveryEntity>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, ExpressDeliveryEntity entity) {
                Intent intent = new Intent();
                intent.putExtra("name",entity.getName());
                intent.putExtra("type",entity.getType());
                SelectorCompanyActivity.this.setResult(100,intent);
                SelectorCompanyActivity.this.finish();
            }
        });

        //标题点击事件，暂时不需要
        adapter.setOnItemTitleClickListener(new IndexableAdapter.OnItemTitleClickListener() {
            @Override
            public void onItemClick(View v, int currentPosition, String indexTitle) {

            }
        });
    }


    private void initSearch() {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(searchFragment)
                .commit();
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

    /**
     * 从网络获取数据
     */
    private void getData() {
        ExpressDeliveryModel expressDeliveryModel = ExpressDeliveryModel.getInstance(this);
        expressDeliveryModel.queryInfo(ConstantALiYunApi.Key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ExpressDeliveryBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("访问数据","完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        //Log.i("访问数据",e.getMessage());
                        readCacheExpressCompany();
                    }

                    @Override
                    public void onNext(ExpressDeliveryBean expressDeliveryBean) {
                        if(expressDeliveryBean!=null){
                            if(expressDeliveryBean.getResult()!=null){
                                List<ExpressDeliveryBean.ResultBean> result = expressDeliveryBean.getResult();
                                initAdapterData(result);
                                StartCacheExpressCompany(result);
                            }
                        }else {
                            readCacheExpressCompany();
                        }
                    }
                });
    }

    private void initAdapterData(List<ExpressDeliveryBean.ResultBean> result) {
        mData = initData(result);
        // 快速排序。  排序规则设置为：只按首字母  （默认全拼音排序）  效率很高，是默认的10倍左右。  按需开启～
        indexLayout.setCompareMode(IndexableLayout.MODE_FAST);
        adapter.setDatas(mData, new IndexableAdapter.IndexCallback<ExpressDeliveryEntity>() {
            @Override
            public void onFinished(List<EntityWrapper<ExpressDeliveryEntity>> datas) {
                // 数据处理完成后回调
                searchFragment.bindData(mData);
                progress.setVisibility(View.GONE);
            }
        });
        indexLayout.setOverlayStyle_Center();
    }

    /**
     * 将快递公司缓存到本地
     * @param result
     */
    private void StartCacheExpressCompany(List<ExpressDeliveryBean.ResultBean> result) {
        initRealm();
        if(realm!=null && realm.where(CacheExpressCompany.class).findAll()!=null){
            cacheExpressCompanies = realm.where(CacheExpressCompany.class).findAll();
        }else {
            return;
        }
        realm.beginTransaction();
        cacheExpressCompanies.deleteAllFromRealm();
        realm.commitTransaction();
        realm.beginTransaction();
        for(int a=0 ; a<result.size() ; a++){
            CacheExpressCompany expressCompany = realm.createObject(CacheExpressCompany.class);
            expressCompany.setName(result.get(a).getName());
            expressCompany.setLetter(result.get(a).getLetter());
            expressCompany.setNumber(result.get(a).getNumber());
            expressCompany.setTel(result.get(a).getTel());
            expressCompany.setType(result.get(a).getType());
        }
        realm.commitTransaction();
    }

    /**
     * 读取缓存
     */
    private void readCacheExpressCompany() {
        initRealm();
        if(realm!=null && realm.where(CacheExpressCompany.class).findAll()!=null){
            cacheExpressCompanies = realm.where(CacheExpressCompany.class).findAll();
        }else {
            getData();
            return;
        }
        if(cacheExpressCompanies.size()==0){
            getData();
            return;
        }
        List<ExpressDeliveryBean.ResultBean> result = new ArrayList<>();
        for(int a=0 ; a<cacheExpressCompanies.size() ; a++){
            ExpressDeliveryBean.ResultBean resultBean = new ExpressDeliveryBean.ResultBean();
            resultBean.setName(cacheExpressCompanies.get(a).getName());
            resultBean.setLetter(cacheExpressCompanies.get(a).getLetter());
            resultBean.setNumber(cacheExpressCompanies.get(a).getNumber());
            resultBean.setTel(cacheExpressCompanies.get(a).getTel());
            resultBean.setType(cacheExpressCompanies.get(a).getType());
            result.add(resultBean);
        }
        initAdapterData(result);
    }

}
