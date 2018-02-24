package com.ns.yc.lifehelper.ui.other.bookReader.bean;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.listener.OnRvItemClickListener;
import com.ns.yc.lifehelper.ui.other.bookReader.view.adapter.TopReaderAdapter;
import com.ns.yc.lifehelper.api.http.bookReader.BookReaderModel;
import com.ns.yc.lifehelper.ui.other.bookReader.view.activity.SubHomeTopRankActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.view.activity.SubTopRankActivity;
import com.ns.yc.lifehelper.weight.CustomExpandableListView;
import com.ns.yc.lifehelper.utils.AppUtil;
import com.ns.yc.lifehelper.utils.rx.RxUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/19
 * 描    述：小说阅读器排行版
 * 修订历史：
 * ================================================
 */
public class ReaderTopBookActivity extends BaseActivity implements View.OnClickListener {


    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.elv_male)
    CustomExpandableListView elvMale;
    @Bind(R.id.elv_fe_male)
    CustomExpandableListView elvFeMale;

    private List<ReaderTopBookBean.MaleBean> maleGroups = new ArrayList<>();
    private List<List<ReaderTopBookBean.MaleBean>> maleChilds = new ArrayList<>();
    private TopReaderAdapter maleAdapter;

    private List<ReaderTopBookBean.MaleBean> femaleGroups = new ArrayList<>();
    private List<List<ReaderTopBookBean.MaleBean>> femaleChilds = new ArrayList<>();
    private TopReaderAdapter femaleAdapter;
    private ReaderTopBookActivity activity;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity = null;
    }


    @Override
    public int getContentView() {
        return R.layout.activity_reader_top_look;
    }

    @Override
    public void initView() {
        activity = ReaderTopBookActivity.this;
        initToolBar();
        initRecycleView();
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }


    @Override
    public void initData() {
        getTopReaderData();
    }


    private void initToolBar() {
        toolbarTitle.setText("排行榜");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
        }
    }


    private void initRecycleView() {
        maleAdapter = new TopReaderAdapter(this, maleGroups, maleChilds);
        femaleAdapter = new TopReaderAdapter(this, femaleGroups, femaleChilds);
        elvMale.setAdapter(maleAdapter);
        elvFeMale.setAdapter(femaleAdapter);
        maleAdapter.setItemClickListener(new ClickListener());
        femaleAdapter.setItemClickListener(new ClickListener());
    }


    class ClickListener implements OnRvItemClickListener<ReaderTopBookBean.MaleBean> {
        @Override
        public void onItemClick(View view, int position, ReaderTopBookBean.MaleBean data) {
            if (data.getMonthRank() == null) {
                //追书最热榜
                //SubOtherHomeRankActivity.startActivity(mContext, data._id, data.title);
                Intent intent = new Intent(activity, SubHomeTopRankActivity.class);
                intent.putExtra("id",data.get_id());
                intent.putExtra("title",data.getTitle());
                startActivity(intent);
            } else {
                //排行榜
                //SubRankActivity.startActivity(mContext, data._id, data.monthRank, data.totalRank, data.title);
                Intent intent = new Intent(activity, SubTopRankActivity.class);
                intent.putExtra("id",data.get_id());
                intent.putExtra("title",data.getTitle());
                intent.putExtra("monthRank",data.getMonthRank());
                intent.putExtra("totalRank",data.getTotalRank());
                startActivity(intent);
            }
        }
    }


    private void getTopReaderData() {
        String key = AppUtil.createCacheKey("book-ranking-list");
        BookReaderModel model = BookReaderModel.getInstance(this);
        Observable<ReaderTopBookBean> topRanking = model.getTopRanking();
        Observable<ReaderTopBookBean> compose = topRanking.compose(RxUtil.<ReaderTopBookBean>rxCacheBeanHelper(key));
        Observable.concat(RxUtil.rxCreateDiskObservable(key, ReaderTopBookBean.class), compose)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReaderTopBookBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ReaderTopBookBean readerTopBookBean) {
                        if(readerTopBookBean!=null){
                            maleGroups.clear();
                            femaleGroups.clear();
                            updateMale(readerTopBookBean);
                            updateFemale(readerTopBookBean);
                        }
                    }
                });
    }


    private void updateMale(ReaderTopBookBean rankingList) {
        List<ReaderTopBookBean.MaleBean> list = rankingList.getMale();
        List<ReaderTopBookBean.MaleBean> collapse = new ArrayList<>();
        for (ReaderTopBookBean.MaleBean bean : list) {
            if (bean.isCollapse()) { // 折叠
                collapse.add(bean);
            } else {
                maleGroups.add(bean);
                maleChilds.add(new ArrayList<ReaderTopBookBean.MaleBean>());
            }
        }
        if (collapse.size() > 0) {
            maleGroups.add(new ReaderTopBookBean.MaleBean("别人家的排行榜"));
            maleChilds.add(collapse);
        }
        maleAdapter.notifyDataSetChanged();
    }

    private void updateFemale(ReaderTopBookBean rankingList) {
        List<ReaderTopBookBean.MaleBean> list = rankingList.getFemale();
        List<ReaderTopBookBean.MaleBean> collapse = new ArrayList<>();
        for (ReaderTopBookBean.MaleBean bean : list) {
            if (bean.isCollapse()) { // 折叠
                collapse.add(bean);
            } else {
                femaleGroups.add(bean);
                femaleChilds.add(new ArrayList<ReaderTopBookBean.MaleBean>());
            }
        }
        if (collapse.size() > 0) {
            femaleGroups.add(new ReaderTopBookBean.MaleBean("别人家的排行榜"));
            femaleChilds.add(collapse);
        }
        femaleAdapter.notifyDataSetChanged();
    }


}
