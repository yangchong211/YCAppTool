package com.ns.yc.lifehelper.ui.other.bookReader.view.activity;

import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.weight.epubview.DirectionalViewpager;

import butterknife.BindView;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/29
 * 描    述：页面Pub
 * 修订历史：
 * ================================================
 */
public class BookReadPubActivity extends BaseActivity {

    @BindView(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.vp_content)
    DirectionalViewpager vpContent;
    @BindView(R.id.llPdfRoot)
    FrameLayout llPdfRoot;

    private String filePath;
    private String fileName;

    @Override
    public int getContentView() {
        return R.layout.activity_reader_epub;
    }

    @Override
    public void initView() {
        initToolBar();

    }

    private void initToolBar() {
        filePath = Uri.decode(getIntent().getDataString().replace("file://", ""));
        fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
        toolbarTitle.setText(fileName);
        toolbar.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                            toolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            toolbar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                });
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }



}
