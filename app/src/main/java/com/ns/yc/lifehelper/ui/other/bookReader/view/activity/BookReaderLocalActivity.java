package com.ns.yc.lifehelper.ui.other.bookReader.view.activity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.ConstantBookReader;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.ui.other.bookReader.view.adapter.ReaderLocalAdapter;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.ReaderRecommendBean;
import com.ns.yc.lifehelper.ui.other.bookReader.bean.support.RefreshCollectionListEvent;
import com.ns.yc.lifehelper.ui.other.bookReader.manager.CollectionsManager;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;
import com.ns.yc.lifehelper.utils.EventBusUtils;
import com.ns.yc.lifehelper.utils.FileUtils;

import org.yczbj.ycrefreshviewlib.YCRefreshView;
import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/29
 * 描    述：扫描本地书籍页面
 * 修订历史：
 * ================================================
 */
public class BookReaderLocalActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.recyclerView)
    YCRefreshView recyclerView;
    private ReaderLocalAdapter adapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.base_refresh_recycle_bar;
    }

    @Override
    public void initView() {
        initToolBar();
        initRecycleView();
    }

    private void initToolBar() {
        toolbarTitle.setText("扫描本地书籍");
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
    }

    @Override
    public void initData() {
        startQueryFiles();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReaderLocalAdapter(this);
        final RecycleViewItemLine line = new RecycleViewItemLine(this, LinearLayout.HORIZONTAL,
                SizeUtils.dp2px(1), Color.parseColor("#f5f5f7"));
        recyclerView.addItemDecoration(line);
        //recyclerView.setAdapter(adapter);
        recyclerView.setAdapterWithProgress(adapter);
        adapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                initOnItemClick(position);
            }
        });
    }

    private void startQueryFiles() {
        String[] projection = new String[]{MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE
        };
        // cache
        String bookPath = FileUtils.createRootPath(BookReaderLocalActivity.this.getApplicationContext());

        String selection = MediaStore.Files.FileColumns.DATA + " not like ? and ("
                + MediaStore.Files.FileColumns.DATA + " like ? or "
                + MediaStore.Files.FileColumns.DATA + " like ? or "
                + MediaStore.Files.FileColumns.DATA + " like ? or "
                + MediaStore.Files.FileColumns.DATA + " like ? or "
                + MediaStore.Files.FileColumns.DATA + " like ? or "
                + MediaStore.Files.FileColumns.DATA + " like ? )";

        String[] type = {"%" + bookPath + "%",
                "%" + ConstantBookReader.SUFFIX_TXT,
                "%" + ConstantBookReader.SUFFIX_PDF,
                "%" + ConstantBookReader.SUFFIX_EPUB,
                "%" + ConstantBookReader.SUFFIX_DOC,
                "%" + ConstantBookReader.SUFFIX_DOCX,
                "%" + ConstantBookReader.SUFFIX_CHM};

        Uri uri = Uri.parse("content://media/external/file");

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(uri, projection, selection, type, null);
        if(cursor!=null && cursor.moveToFirst()){
            //int idindex = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
            int dataindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            int sizeindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
            List<ReaderRecommendBean.RecommendBooks> list = new ArrayList<>();


            do {
                String path = cursor.getString(dataindex);

                int dot = path.lastIndexOf("/");
                String name = path.substring(dot + 1);
                if (name.lastIndexOf(".") > 0){
                    name = name.substring(0, name.lastIndexOf("."));
                }

                ReaderRecommendBean.RecommendBooks localBookBean = new ReaderRecommendBean.RecommendBooks();
                localBookBean.set_id(name);
                localBookBean.setPath(path);
                localBookBean.setFromSD(true);
                localBookBean.setTitle(name);
                localBookBean.setLastChapter(FileUtils.formatFileSizeToString(cursor.getLong(sizeindex)));

                list.add(localBookBean);
            }while (cursor.moveToNext());


            cursor.close();
            adapter.addAll(list);
            adapter.notifyDataSetChanged();
        }else {
            adapter.clear();
        }
    }


    private void initOnItemClick(int position) {
        final ReaderRecommendBean.RecommendBooks books = adapter.getItem(position);
        if (books.path.endsWith(ConstantBookReader.SUFFIX_TXT)) {
            // TXT
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage(String.format(getString(R.string.book_detail_is_joined_the_book_shelf), books.title))
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 拷贝到缓存目录
                            FileUtils.fileChannelCopy(new File(books.path), new File(FileUtils.getChapterPath(books._id, 1)));
                            // 加入书架
                            if (CollectionsManager.getInstance().add(books)) {
                                //showTipViewAndDelayClose(String.format(getString(R.string.book_detail_has_joined_the_book_shelf), books.title));
                                String format = String.format(getString(R.string.book_detail_has_joined_the_book_shelf), books.title);
                                Snackbar.make(recyclerView,format ,Snackbar.LENGTH_LONG)
                                        .setAction("确定", new View.OnClickListener(){
                                            @Override
                                            public void onClick(View v) {
                                                //点击事件
                                            }
                                        })
                                        .show();
                                // 通知
                                RefreshCollectionListEvent refreshCollectionListEvent = new RefreshCollectionListEvent();
                                EventBusUtils.post(refreshCollectionListEvent);
                            } else {
                                //showTipViewAndDelayClose("书籍已存在");
                                Snackbar.make(recyclerView,"书籍已存在" ,Snackbar.LENGTH_LONG)
                                        .setAction("确定", new View.OnClickListener(){
                                            @Override
                                            public void onClick(View v) {
                                                //点击事件
                                            }
                                        })
                                        .show();
                            }
                            dialog.dismiss();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        } else if (books.path.endsWith(ConstantBookReader.SUFFIX_PDF)) {
            // PDF
            Intent intent = new Intent(BookReaderLocalActivity.this, BookReadPDFActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.fromFile(new File(books.path)));
            BookReaderLocalActivity.this.startActivity(intent);
        } else if (books.path.endsWith(ConstantBookReader.SUFFIX_EPUB)) {
            // EPub
            Intent intent = new Intent(BookReaderLocalActivity.this, BookReadPubActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.fromFile(new File(books.path)));
            BookReaderLocalActivity.this.startActivity(intent);
        } else if (books.path.endsWith(ConstantBookReader.SUFFIX_CHM)) {
            // CHM
            Intent intent = new Intent(BookReaderLocalActivity.this, BookReadChmActivity.class);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.fromFile(new File(books.path)));
            BookReaderLocalActivity.this.startActivity(intent);
        }
    }

    public void showTipViewAndDelayClose(String tip) {
        final TextView tipView = new TextView(BookReaderLocalActivity.this);
        tipView.setText(tip);
        Animation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
        tipView.startAnimation(mShowAction);
        tipView.setVisibility(View.VISIBLE);

        tipView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        -1.0f);
                mHiddenAction.setDuration(500);
                tipView.startAnimation(mHiddenAction);
                tipView.setVisibility(View.GONE);
            }
        }, 2200);
    }



}
