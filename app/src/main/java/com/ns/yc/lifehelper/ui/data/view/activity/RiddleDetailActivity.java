package com.ns.yc.lifehelper.ui.data.view.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constantApi.ConstantALiYunApi;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.find.model.bean.RiddleDetailBean;
import com.ns.yc.lifehelper.ui.find.model.RiddleDetailModel;

import java.util.List;

import butterknife.Bind;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：谜语详情页面
 * 修订历史：
 * ================================================
 */
public class RiddleDetailActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.ll_search)
    FrameLayout llSearch;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_riddle_one)
    TextView tvRiddleOne;
    @Bind(R.id.tv_riddle_qa)
    TextView tvRiddleQa;
    @Bind(R.id.tv_look_one)
    TextView tvLookOne;
    @Bind(R.id.tv_riddle_two)
    TextView tvRiddleTwo;
    @Bind(R.id.tv_riddle_qa_two)
    TextView tvRiddleQaTwo;
    @Bind(R.id.tv_look_two)
    TextView tvLookTwo;

    private String type;
    private String classId;
    private int pagenum = 1;            //当前页
    private int pagesize = 2;           //每页数据 最大为2
    private String keyword = "";        //搜索关键词
    private boolean isLookOne = false;  //谜底是否可见
    private boolean isLookTwo = false;

    @Override
    public int getContentView() {
        return R.layout.activity_other_riddle_detail;
    }

    @Override
    public void initView() {
        initToolBar();
        initIntentData();
    }


    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getStringExtra("type");
            classId = intent.getStringExtra("classId");
        }
        if (type == null || type.length() == 0) {
            type = "字谜";
        }
        if (classId == null || classId.length() == 0) {
            classId = "1";
        }
    }


    private void initToolBar() {
        toolbarTitle.setText("谜语大全");
        llSearch.setVisibility(View.VISIBLE);
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        llSearch.setOnClickListener(this);
        tvLookOne.setOnClickListener(this);
        tvLookTwo.setOnClickListener(this);
    }


    @Override
    public void initData() {
        tvRiddleQa.setVisibility(View.INVISIBLE);
        tvRiddleQaTwo.setVisibility(View.INVISIBLE);
        getRiddleData(type, keyword, pagenum, pagesize);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_search:

                break;
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.tv_look_one:
                if(isLookOne){
                    tvRiddleQa.setVisibility(View.GONE);
                }else {
                    tvRiddleQa.setVisibility(View.VISIBLE);
                }
                isLookOne = !isLookOne;
                break;
            case R.id.tv_look_two:
                if(isLookTwo){
                    tvRiddleQaTwo.setVisibility(View.GONE);
                }else {
                    tvRiddleQaTwo.setVisibility(View.VISIBLE);
                }
                isLookTwo = !isLookTwo;
                break;
        }
    }

    private void getRiddleData(String type, String keyword, int pagenum, int pagesize) {
        RiddleDetailModel model = RiddleDetailModel.getInstance(this);
        model.getRiddleDetail(ConstantALiYunApi.Key, type, keyword, String.valueOf(pagenum), String.valueOf(pagesize))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RiddleDetailBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RiddleDetailBean riddleDetailBean) {
                        if (riddleDetailBean != null) {
                            if(riddleDetailBean.getResult()!=null && riddleDetailBean.getResult().getList()!=null){
                                List<RiddleDetailBean.ResultBean.ListBean> list = riddleDetailBean.getResult().getList();
                                if(list.size()>1){
                                    tvRiddleOne.setText(list.get(0).getContent());
                                    tvRiddleQa.setText(list.get(0).getAnswer());

                                    tvRiddleTwo.setText(list.get(1).getContent());
                                    tvRiddleQaTwo.setText(list.get(1).getAnswer());
                                }
                            }
                        }
                    }
                });
    }

}
