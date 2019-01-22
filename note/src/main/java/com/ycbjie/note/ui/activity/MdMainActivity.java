package com.ycbjie.note.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.arounter.ARouterUtils;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.note.R;
import com.ycbjie.note.ui.adapter.MdNoteAdapter;
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
@Route(path = ARouterConstant.ACTIVITY_MARKDOWN_ACTIVITY)
public class MdMainActivity extends BaseActivity {

    public static final String CONFIG_FIRST_START = "isFirstStart";
    private MdNoteAdapter mNoteAdapter;
    private static final int REQUEST_CODE_ADD = 0;
    private static final int REQUEST_CODE_EDIT = 1;
    private int mSelectedPosition;
    private Toolbar toolbar;
    private TextView tvTitleLeft;
    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    private FrameLayout llSearch;
    private ImageView ivRightImg;
    private TextView tvTitleRight;
    private ListView listView;

    @Override
    protected void onDestroy() {
        NoteDB.getInstance().close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.data_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(this, MdNoteActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD);
            return true;
        } else if (id == R.id.action_about) {
            ARouterUtils.navigation(ARouterConstant.ACTIVITY_OTHER_ABOUT_ME);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.DataDelete) {
            if (mSelectedPosition != -1) {
                NoteDB.getInstance().delete(mSelectedPosition);
                mNoteAdapter.notifyDataSetChanged();
            }
            return true;
        } else if (i == R.id.DataClear) {
            NoteDB.getInstance().clear();
            mNoteAdapter.notifyDataSetChanged();
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }


    @Override
    public int getContentView() {
        return R.layout.base_list_view_bar;
    }

    @Override
    public void initView() {
        NoteDB.getInstance().open(this);
        onCheckFirstStart();
        initFindViewById();
        initToolBar();
        initListView();
    }

    private void initListView() {
        mNoteAdapter = new MdNoteAdapter(this);
        listView.setAdapter(mNoteAdapter);
        registerForContextMenu(listView);
        AdapterView.OnItemLongClickListener longListener = (parent, view, position, id) -> {
            mSelectedPosition = position;
            listView.showContextMenu();
            return true;
        };
        listView.setOnItemLongClickListener(longListener);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(MdMainActivity.this, MdNoteActivity.class);
            intent.putExtra("NoteId", id);
            startActivityForResult(intent, REQUEST_CODE_EDIT);
        });
    }

    private void initFindViewById() {
        toolbar = findViewById(R.id.toolbar);
        tvTitleLeft = findViewById(R.id.tv_title_left);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        llSearch = findViewById(R.id.ll_search);
        ivRightImg = findViewById(R.id.iv_right_img);
        tvTitleRight = findViewById(R.id.tv_title_right);
        listView = findViewById(R.id.listView);
    }

    @SuppressLint("SetTextI18n")
    private void initToolBar() {
        toolbarTitle.setText("markDown记事本");
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

    protected void onCheckFirstStart() {
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!mSharedPreferences.getBoolean(CONFIG_FIRST_START, true)) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("# Markdown功能介绍\n\n");
        builder.append("本App支持一些简单的Markdown语法,您可以手动输入,也可以通过快捷工具栏来添加Markdown符号\n\n");
        builder.append("## **用法与规则**\n\n");
        builder.append("### **标题**\n");
        builder.append("使用\"#\"加空格在段首来创建标题\n\n");
        builder.append("例如:\n");
        builder.append("# 一级标题\n");
        builder.append("## 二级标题\n");
        builder.append("### 三级标题\n\n");
        builder.append("### **加粗功能**\n");
        builder.append("使用一组\"**\"来加粗一段文字\n\n");
        builder.append("例如:\n");
        builder.append("这是**加粗的文字**\n\n");
        builder.append("### **居中**\n");
        builder.append("使用一对大括号\"{}\"来居中一段文字(注:这是JNote特别添加的特性,非Markdown语法)\n\n");
        builder.append("例如:\n");
        builder.append("### {这是一个居中的标题}\n\n");
        builder.append("### **引用**\n");
        builder.append("使用\">\"在段首来创建引用\n\n");
        builder.append("例如:\n");
        builder.append("> 这是一段引用\n");
        builder.append("> 这是一段引用\n\n");
        builder.append("### **无序列表**\n");
        builder.append("使用\"-\"加空格在段首来创建无序列表\n\n");
        builder.append("例如:\n");
        builder.append("> 这是一个无序列表\n");
        builder.append("> 这是一个无序列表\n");
        builder.append("> 这是一个无序列表\n\n");
        builder.append("### **有序列表**\n");
        builder.append("使用数字圆点加空格在段首来创建有序列表\n\n");
        builder.append("例如:\n");
        builder.append("1. 这是一个有序列表\n");
        builder.append("2. 这是一个有序列表\n");
        builder.append("3. 这是一个有序列表\n\n");
        NoteDB.Note note = new NoteDB.Note();
        note.title = "Markdown功能介绍";
        note.content = builder.toString();
        note.date = Calendar.getInstance().getTimeInMillis();
        NoteDB.getInstance().insert(note);
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putBoolean(CONFIG_FIRST_START, false);
        edit.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD || requestCode == REQUEST_CODE_EDIT) {
            mNoteAdapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
