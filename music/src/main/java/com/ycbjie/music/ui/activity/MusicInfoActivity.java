package com.ycbjie.music.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.tablayout.SegmentTabLayout;
import com.pedaily.yc.ycdialoglib.toast.ToastUtils;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.music.R;
import com.ycbjie.music.model.bean.AudioBean;
import com.ycbjie.music.utils.CoverLoader;

import java.io.File;
import java.util.Locale;


import cn.ycbjie.ycstatusbarlib.bar.StateAppBar;

public class MusicInfoActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView tvTitleLeft;
    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private FrameLayout llSearch;
    private ImageView ivRightImg;
    private TextView tvTitleRight;
    private ImageView ivMusicInfoCover;
    private TextInputLayout layoutMusicInfoTitle;
    private EditText etMusicInfoTitle;
    private TextInputLayout labelMusicInfoArtist;
    private EditText etMusicInfoArtist;
    private TextInputLayout labelMusicInfoAlbum;
    private EditText etMusicInfoAlbum;
    private TextInputLayout labelMusicInfoDuration;
    private EditText tvMusicInfoDuration;
    private TextInputLayout labelMusicInfoFileName;
    private EditText tvMusicInfoFileName;
    private TextInputLayout labelMusicInfoFileSize;
    private EditText tvMusicInfoFileSize;
    private TextInputLayout labelMusicInfoFilePath;
    private EditText tvMusicInfoFilePath;
    private AudioBean mMusic;
    private File mMusicFile;
    private Bitmap mCoverBitmap;

    public static void start(Context context, AudioBean music) {
        Intent intent = new Intent(context, MusicInfoActivity.class);
        intent.putExtra("music", music);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_music_info, menu);
        return true;
    }


    @Override
    public int getContentView() {
        return R.layout.activity_local_music_info;
    }

    @Override
    public void initView() {
        initFindById();
        initToolBar();
        initIntentData();
        initViewData();
    }

    private void initFindById() {


        toolbar = findViewById(R.id.toolbar);
        tvTitleLeft = findViewById(R.id.tv_title_left);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        llSearch = findViewById(R.id.ll_search);
        ivRightImg = findViewById(R.id.iv_right_img);
        tvTitleRight = findViewById(R.id.tv_title_right);


        ivMusicInfoCover = findViewById(R.id.iv_music_info_cover);
        layoutMusicInfoTitle = findViewById(R.id.layout_music_info_title);
        etMusicInfoTitle = findViewById(R.id.et_music_info_title);
        labelMusicInfoArtist = findViewById(R.id.label_music_info_artist);
        etMusicInfoArtist = findViewById(R.id.et_music_info_artist);
        labelMusicInfoAlbum = findViewById(R.id.label_music_info_album);
        etMusicInfoAlbum = findViewById(R.id.et_music_info_album);
        labelMusicInfoDuration = findViewById(R.id.label_music_info_duration);
        tvMusicInfoDuration = findViewById(R.id.tv_music_info_duration);
        labelMusicInfoFileName = findViewById(R.id.label_music_info_file_name);
        tvMusicInfoFileName = findViewById(R.id.tv_music_info_file_name);
        labelMusicInfoFileSize = findViewById(R.id.label_music_info_file_size);
        tvMusicInfoFileSize = findViewById(R.id.tv_music_info_file_size);
        labelMusicInfoFilePath = findViewById(R.id.label_music_info_file_path);
        tvMusicInfoFilePath = findViewById(R.id.tv_music_info_file_path);

    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("歌曲详情信息");
        toolbar.setTitleTextColor(this.getResources().getColor(R.color.white));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_save) {
            ToastUtils.showRoundRectToast("保存信息功能");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }


    private void initIntentData() {
        mMusic = (AudioBean) getIntent().getSerializableExtra("music");
        if (mMusic == null || mMusic.getType() != AudioBean.Type.LOCAL) {
            finish();
        }
        mMusicFile = new File(mMusic.getPath());
        mCoverBitmap = CoverLoader.getInstance().loadThumbnail(mMusic);
    }

    private void initViewData() {
        ivMusicInfoCover.setImageBitmap(mCoverBitmap);
        etMusicInfoTitle.setText(mMusic.getTitle());
        etMusicInfoTitle.setSelection(etMusicInfoTitle.length());
        etMusicInfoArtist.setText(mMusic.getArtist());
        etMusicInfoArtist.setSelection(etMusicInfoArtist.length());
        etMusicInfoAlbum.setText(mMusic.getAlbum());
        etMusicInfoAlbum.setSelection(etMusicInfoAlbum.length());
        tvMusicInfoDuration.setText(formatTime("mm:ss", mMusic.getDuration()));
        tvMusicInfoFileName.setText(mMusic.getFileName());
        tvMusicInfoFilePath.setText(mMusicFile.getParent());
    }

    private String formatTime(String pattern, long milli) {
        int m = (int) (milli / DateUtils.MINUTE_IN_MILLIS);
        int s = (int) ((milli / DateUtils.SECOND_IN_MILLIS) % 60);
        String mm = String.format(Locale.getDefault(), "%02d", m);
        String ss = String.format(Locale.getDefault(), "%02d", s);
        return pattern.replace("mm", mm).replace("ss", ss);
    }

}
