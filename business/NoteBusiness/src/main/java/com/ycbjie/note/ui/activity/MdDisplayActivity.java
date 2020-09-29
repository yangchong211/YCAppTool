package com.ycbjie.note.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.note.R;
import com.ycbjie.note.markdown.MDReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/13
 *     desc  : markDown记事本
 *     revise:
 * </pre>
 */
public class MdDisplayActivity extends BaseActivity {

    private static final String DEFAULT_DIR = Environment.getExternalStorageDirectory() + File.separator + "JNote";
    private Toolbar toolbar;
    private TextView tvTitleLeft;
    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private FrameLayout llSearch;
    private ImageView ivRightImg;
    private TextView tvTitleRight;
    private TextView mTextView;
    private MDReader mMDReader;
    private ScrollView mRootView;

    @Override
    public int getContentView() {
        return R.layout.activity_display;
    }

    @Override
    public void initView() {
        String content = getIntent().getExtras().getString("Content");
        mMDReader = new MDReader(content);
        checkStorageDir();
        initFindViewById();
        initToolBar();
    }


    private void initFindViewById() {
        toolbar = findViewById(R.id.toolbar);
        tvTitleLeft = findViewById(R.id.tv_title_left);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        llSearch = findViewById(R.id.ll_search);
        ivRightImg = findViewById(R.id.iv_right_img);
        tvTitleRight = findViewById(R.id.tv_title_right);
        mRootView = findViewById(R.id.DisplayRootView);
        mTextView = findViewById(R.id.DisplayTextView);
        mTextView.setTextKeepState(mMDReader.getFormattedContent(), BufferType.SPANNABLE);
    }


    private void initToolBar() {
        toolbarTitle.setText("超文本记事");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save_md) {
            saveAsMardown();

        } else if (id == R.id.action_save_txt) {
            saveAsRawContent();

        } else if (id == R.id.action_save_img) {
            saveAsBitmap();

        } else {
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean isSDCardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public void checkStorageDir() {
        if (isSDCardMounted()) {
            File directory = new File(DEFAULT_DIR);
            if (!directory.exists()) {
                directory.mkdir();
            }
        }
    }

    public boolean checkSaveEnv() {
        if (!isSDCardMounted()) {
            Toast.makeText(this, "找不到 SDCard !", Toast.LENGTH_LONG).show();
            return false;
        }
        if ("".equals(mMDReader.getContent())) {
            Toast.makeText(this, "没有内容,无法保存 !", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void saveAsMardown() {
        if (!checkSaveEnv()) {
            return;
        }
        String filepath = DEFAULT_DIR + File.separator + mMDReader.getTitle() + ".md";
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), "UTF-8"));
            writer.write(mMDReader.getContent());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "成功保存到:" + filepath, Toast.LENGTH_LONG).show();
    }

    public void saveAsRawContent() {
        if (!checkSaveEnv()) {
            return;
        }
        String filepath = DEFAULT_DIR + File.separator + mMDReader.getTitle() + ".txt";
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath), "UTF-8"));
            writer.write(mMDReader.getRawContent());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "成功保存到:" + filepath, Toast.LENGTH_LONG).show();
    }

    public void saveAsBitmap() {
        if (!checkSaveEnv()) {
            return;
        }
        String filepath = DEFAULT_DIR + File.separator + mMDReader.getTitle() + ".jpg";
        try {
            FileOutputStream stream = new FileOutputStream(filepath);
            Bitmap bitmap = createBitmap(mRootView);
            if (bitmap != null) {
                bitmap.compress(CompressFormat.JPEG, 100, stream);
                Toast.makeText(this, "成功保存到:" + filepath, Toast.LENGTH_LONG).show();
            }
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap createBitmap(ScrollView v) {
        int width = 0, height = 0;
        for (int i = 0; i < v.getChildCount(); i++) {
            width += v.getChildAt(i).getWidth();
            height += v.getChildAt(i).getHeight();
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    public static Bitmap createBitmap(View v) {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return bitmap;
    }
}
