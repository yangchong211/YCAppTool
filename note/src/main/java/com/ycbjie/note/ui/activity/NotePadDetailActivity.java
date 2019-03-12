package com.ycbjie.note.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.note.R;
import com.ycbjie.library.utils.DoShareUtils;
import com.ns.yc.yccustomtextlib.hyper.HyperTextView;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.note.model.bean.NotePadDetail;
import com.ycbjie.note.utils.SDCardUtils;
import com.ycbjie.note.utils.StringUtils;

import java.io.File;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/14
 * 描    述：简易记事本详情页面
 * 修订历史：
 * ================================================
 */
public class NotePadDetailActivity extends BaseActivity implements View.OnClickListener {

    FrameLayout llTitleMenu;
    TextView toolbarTitle;
    Toolbar toolbar;
    TextView tvNoteDetailDate;
    TextView tvNoteType;
    TextView etNewTitle;
    HyperTextView etNewContent;
    private NotePadDetail notePadDetail;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_not_pad_detail;
    }

    @Override
    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        tvNoteDetailDate = findViewById(R.id.tv_note_detail_date);
        tvNoteType = findViewById(R.id.tv_note_type);
        etNewTitle = findViewById(R.id.et_new_title);
        etNewContent = findViewById(R.id.et_new_content);

        initToolBar();
        initIntentData();
    }


    private void initToolBar() {
        toolbarTitle.setText("笔记详情");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("data");
            notePadDetail = (NotePadDetail) bundle.getSerializable("notePad");
            showData(notePadDetail);
        }
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }


    @Override
    public void initData() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_pad_menu_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_note_share) {
            DoShareUtils.shareTextAndImage(this, notePadDetail.getTitle(), notePadDetail.getContent(), null);

        } else if (i == R.id.action_note_edit) {
            Intent intent = new Intent(NotePadDetailActivity.this, NotePadNewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("notePad", notePadDetail);
            intent.putExtra("data", bundle);
            intent.putExtra("flag", 1);         //编辑笔记
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            finish();

        }
    }


    /**
     * 展示数据
     */
    private void showData(final NotePadDetail notePadDetail) {
        if(notePadDetail.getCreateTime()!=null){
            tvNoteDetailDate.setText(notePadDetail.getCreateTime());
        }
        tvNoteType.setText(String.valueOf(notePadDetail.getType()));
        etNewTitle.setText(notePadDetail.getTitle());
        etNewContent.post(new Runnable() {
            @Override
            public void run() {
                //showEditData(myContent);
                etNewContent.clearAllLayout();
                showDataSync(notePadDetail.getContent());
            }
        });
    }


    /**
     * 异步方式显示数据
     */
    private void showDataSync(final String html){
        Subscription subsLoading = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                showEditData(subscriber, html);
            }
        })
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtils.showToast("解析错误：图片不存在或已损坏");
                    }

                    @Override
                    public void onNext(String text) {
                        if (text.contains(SDCardUtils.getPictureDir())) {
                            etNewContent.addImageViewAtIndex(etNewContent.getLastIndex(), text);
                        } else {
                            etNewContent.addTextViewAtIndex(etNewContent.getLastIndex(), text);
                        }
                    }
                });

    }


    /**
     * 显示数据
     */
    private void showEditData(Subscriber<? super String> subscriber, String html) {
        try {
            List<String> textList = StringUtils.cutStringByImgTag(html);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                if (text.contains("<img") && text.contains("src=")) {
                    String imagePath = StringUtils.getImgSrc(text);
                    if (new File(imagePath).exists()) {
                        subscriber.onNext(imagePath);
                    } else {
                        ToastUtils.showToast("图片"+1+"已丢失，请重新插入！");
                    }
                } else {
                    subscriber.onNext(text);
                }
            }
            subscriber.onCompleted();
        } catch (Exception e){
            e.printStackTrace();
            subscriber.onError(e);
        }
    }


}
