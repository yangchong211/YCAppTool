package com.ns.yc.lifehelper.ui.other.bookReader.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp.BaseActivity;
import com.ns.yc.lifehelper.weight.pdfview.PDFViewPager;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/29
 * 描    述：页面PDF
 * 修订历史：
 * ================================================
 */
public class BookReadPDFActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.llPdfRoot)
    LinearLayout llPdfRoot;

    @Override
    public int getContentView() {
        return R.layout.activity_reader_book_tex;
    }

    @Override
    public void initView() {
        initToolBar();
    }

    private void initToolBar() {
        String filePath = Uri.decode(getIntent().getDataString().replace("file://", ""));
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
        toolbarTitle.setText(fileName);
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
            String filePath = Uri.decode(getIntent().getDataString().replace("file://", ""));
            PDFViewPager pdfViewPager = new PDFViewPager(this, filePath);
            llPdfRoot.addView(pdfViewPager);
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
}
