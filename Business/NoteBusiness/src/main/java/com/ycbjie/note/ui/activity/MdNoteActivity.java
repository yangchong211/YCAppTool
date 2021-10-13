package com.ycbjie.note.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.note.R;
import com.ycbjie.note.markdown.MDWriter;
import com.ycbjie.note.utils.NoteDB;

import java.util.Calendar;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/06/13
 *     desc  : markDown记事本
 *     revise:
 * </pre>
 */
public class MdNoteActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView tvTitleLeft;
    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private FrameLayout llSearch;
    private ImageView ivRightImg;
    private TextView tvTitleRight;
    private EditText mNoteEditText;

    private NoteDB.Note mNote = new NoteDB.Note();
    private MDWriter mMDWriter;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_note;
    }

    @Override
    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        tvTitleLeft = findViewById(R.id.tv_title_left);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        llSearch = findViewById(R.id.ll_search);
        ivRightImg = findViewById(R.id.iv_right_img);
        tvTitleRight = findViewById(R.id.tv_title_right);
        mNoteEditText = findViewById(R.id.NoteEditText);
        initToolBar();
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
        mMDWriter = new MDWriter(mNoteEditText);
        mNote.key = getIntent().getLongExtra("NoteId", -1);
        if (mNote.key != -1) {
            NoteDB.Note note = NoteDB.getInstance().get(mNote.key);
            if (note != null) {
                mMDWriter.setContent(note.content);
                mNote = note;
            } else {
                mNote.key = -1;
            }
        }
    }

    @Override
    protected void onPause() {
        onSaveNote();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_display) {
            onSaveNote();
            Intent intent = new Intent(this, MdDisplayActivity.class);
            intent.putExtra("Content", mMDWriter.getContent());
            startActivity(intent);
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickHeader(View v) {
        mMDWriter.setAsHeader();
    }

    public void onClickCenter(View v) {
        mMDWriter.setAsCenter();
    }

    public void onClickList(View v) {
        mMDWriter.setAsList();
    }

    public void onClickBold(View v) {
        mMDWriter.setAsBold();
    }

    public void onClickQuote(View v) {
        mMDWriter.setAsQuote();
    }

    public void onSaveNote() {
        mNote.title = mMDWriter.getTitle();
        mNote.content = mMDWriter.getContent();
        if (mNote.key == -1) {
            if (!"".equals(mNote.content)) {
                mNote.date = Calendar.getInstance().getTimeInMillis();
                NoteDB.getInstance().insert(mNote);
            }
        } else {
            NoteDB.getInstance().update(mNote);
        }
    }
}
