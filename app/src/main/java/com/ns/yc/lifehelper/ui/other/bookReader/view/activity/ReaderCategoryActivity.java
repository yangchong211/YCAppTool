package com.ns.yc.lifehelper.ui.other.bookReader.view.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.Constant;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.listener.OnRvItemClickListener;
import com.ns.yc.lifehelper.ui.other.bookReader.view.adapter.CategoryListAdapter;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.CategoryList;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderCategoryBean;
import com.ns.yc.lifehelper.ui.other.bookReader.model.BookReaderModel;
import com.ns.yc.lifehelper.ui.weight.manager.FullyGridLayoutManager;
import com.ns.yc.lifehelper.ui.weight.itemLine.SupportGridItemDecoration;
import com.ns.yc.lifehelper.utils.AppUtil;
import com.ns.yc.lifehelper.utils.RxUtil;

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
 * 创建日期：22017/9/21
 * 描    述：小说阅读器分类
 * 修订历史：
 * ================================================
 */
public class ReaderCategoryActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.rv_male)
    RecyclerView rvMale;
    @Bind(R.id.rv_female)
    RecyclerView rvFemale;
    //private List<ReaderCategoryBean.MaleBean> mMaleCategoryList = new ArrayList<>();
    //private List<ReaderCategoryBean.MaleBean> mFemaleCategoryList = new ArrayList<>();
    private List<CategoryList.MaleBean> mMaleCategoryList = new ArrayList<>();
    private List<CategoryList.MaleBean> mFemaleCategoryList = new ArrayList<>();
    private CategoryListAdapter maleAdapter;
    private CategoryListAdapter femaleAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_reader_category;
    }

    @Override
    public void initView() {
        initToolBar();
        initRecycleView();
    }

    private void initToolBar() {
        toolbarTitle.setText("分类");
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getData();
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
        rvMale.setHasFixedSize(true);
        rvMale.setLayoutManager(new FullyGridLayoutManager(this, 3));
        rvMale.addItemDecoration(new SupportGridItemDecoration(this));
        rvFemale.setHasFixedSize(true);
        rvFemale.setLayoutManager(new FullyGridLayoutManager(this, 3));
        rvFemale.addItemDecoration(new SupportGridItemDecoration(this));
        maleAdapter = new CategoryListAdapter(ReaderCategoryActivity.this, mMaleCategoryList, new ClickListener(Constant.Gender.MALE));
        femaleAdapter = new CategoryListAdapter(ReaderCategoryActivity.this, mFemaleCategoryList, new ClickListener(Constant.Gender.FEMALE));
        rvMale.setAdapter(maleAdapter);
        rvFemale.setAdapter(femaleAdapter);
    }

    class ClickListener implements OnRvItemClickListener<CategoryList.MaleBean> {

        private String gender;

        ClickListener(@Constant.Gender String gender) {
            this.gender = gender;
        }

        @Override
        public void onItemClick(View view, int position, CategoryList.MaleBean data) {
            Intent intent = new Intent(ReaderCategoryActivity.this,SubCategoryListActivity.class);
            intent.putExtra("gender",gender);
            intent.putExtra("name",data.getName());
            intent.putExtra("bookCount",data.getBookCount());
            startActivity(intent);
        }
    }

    private void getData() {
        String key = AppUtil.createCacheKey("book-category-list");
        BookReaderModel model = BookReaderModel.getInstance(ReaderCategoryActivity.this);
        Observable<ReaderCategoryBean> data = model.getCategoryList()
                .compose(RxUtil.<ReaderCategoryBean>rxCacheBeanHelper(key));

        Observable.concat(RxUtil.rxCreateDiskObservable(key, ReaderCategoryBean.class), data)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReaderCategoryBean>() {
                    @Override
                    public void onNext(ReaderCategoryBean data) {
                        if(data!=null){
                            List<ReaderCategoryBean.MaleBean> male = data.getMale();
                            List<ReaderCategoryBean.FemaleBean> female = data.getFemale();
                            mMaleCategoryList.clear();
                            mFemaleCategoryList.clear();
                            for(int a=0 ; a<male.size() ; a++){
                                CategoryList.MaleBean maleBean = new CategoryList.MaleBean();
                                maleBean.setName(male.get(a).getName());
                                maleBean.setBookCount(male.get(a).getBookCount());
                                mMaleCategoryList.add(maleBean);
                            }
                            for(int a=0 ; a<female.size() ; a++){
                                CategoryList.MaleBean maleBean = new CategoryList.MaleBean();
                                maleBean.setName(male.get(a).getName());
                                maleBean.setBookCount(male.get(a).getBookCount());
                                mFemaleCategoryList.add(maleBean);
                            }
                            maleAdapter.notifyDataSetChanged();
                            femaleAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }


}
