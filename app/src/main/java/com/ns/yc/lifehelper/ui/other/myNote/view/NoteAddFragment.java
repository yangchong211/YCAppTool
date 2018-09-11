package com.ns.yc.lifehelper.ui.other.myNote.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp.BaseFragment;
import com.ns.yc.lifehelper.utils.animation.AnimationsUtils;
import com.ns.yc.lifehelper.weight.imageView.NoteItemCircleView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/12
 * 描    述：我的笔记本添加页面
 * 修订历史：
 * ================================================
 */
public class NoteAddFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener {

    protected boolean isNewNote;
    @Bind(R.id.note_detail_edit)
    EditText noteDetailEdit;
    @Bind(R.id.note_detail_img_menu)
    ImageView noteDetailImgMenu;
    @Bind(R.id.note_detail_img_green)
    NoteItemCircleView noteDetailImgGreen;
    @Bind(R.id.note_detail_img_blue)
    NoteItemCircleView noteDetailImgBlue;
    @Bind(R.id.note_detail_img_purple)
    NoteItemCircleView noteDetailImgPurple;
    @Bind(R.id.note_detail_img_yellow)
    NoteItemCircleView noteDetailImgYellow;
    @Bind(R.id.note_detail_img_red)
    NoteItemCircleView noteDetailImgRed;
    @Bind(R.id.note_detail_tv_date)
    TextView noteDetailTvDate;
    @Bind(R.id.note_detail_img_button)
    ImageView noteDetailImgButton;
    @Bind(R.id.note_detail_titlebar)
    RelativeLayout noteDetailTitlebar;
    @Bind(R.id.note_detail_menu)
    RelativeLayout note_detail_menu;
    @Bind(R.id.note_detail_img_thumbtack)
    ImageView noteDetailImgThumbtack;
    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_right_img)
    ImageView ivRightImg;
    @Bind(R.id.ll_search)
    FrameLayout llSearch;
    private NoteAddActivity activity;
    private String time;
    private String id;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (NoteAddActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_note_add_edit;
    }

    @Override
    public void initView() {
        initToolBar();
    }


    private void initToolBar() {
        toolbarTitle.setText("添加记录");
        llSearch.setVisibility(View.VISIBLE);
        ivRightImg.setBackgroundResource(R.drawable.actionbar_send_icon);
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        llSearch.setOnClickListener(this);
        noteDetailImgButton.setOnTouchListener(this);
        note_detail_menu.setOnTouchListener(this);
        noteDetailImgBlue.setOnClickListener(this);
        noteDetailImgGreen.setOnClickListener(this);
        noteDetailImgPurple.setOnClickListener(this);
        noteDetailImgRed.setOnClickListener(this);
        noteDetailImgYellow.setOnClickListener(this);
    }

    @Override
    public void initData() {
        initNoteData();
        initBackground();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_menu:
                activity.finish();
                break;
            case R.id.ll_search:
                saveContent();
                break;
            case R.id.note_detail_img_blue:
                noteDetailEdit.setBackgroundColor(sBackGrounds[3]);
                noteDetailTitlebar.setBackgroundColor(sTitleBackGrounds[3]);
                noteDetailImgThumbtack.setImageResource(sThumbtackImg[3]);
                closeMenu();
                break;
            case R.id.note_detail_img_green:
                noteDetailEdit.setBackgroundColor(sBackGrounds[0]);
                noteDetailTitlebar.setBackgroundColor(sTitleBackGrounds[0]);
                noteDetailImgThumbtack.setImageResource(sThumbtackImg[0]);
                closeMenu();
                break;
            case R.id.note_detail_img_purple:
                noteDetailEdit.setBackgroundColor(sBackGrounds[4]);
                noteDetailTitlebar.setBackgroundColor(sTitleBackGrounds[4]);
                noteDetailImgThumbtack.setImageResource(sThumbtackImg[4]);
                closeMenu();
                break;
            case R.id.note_detail_img_red:
                noteDetailEdit.setBackgroundColor(sBackGrounds[2]);
                noteDetailTitlebar.setBackgroundColor(sTitleBackGrounds[2]);
                noteDetailImgThumbtack.setImageResource(sThumbtackImg[2]);
                closeMenu();
                break;
            case R.id.note_detail_img_yellow:
                noteDetailEdit.setBackgroundColor(sBackGrounds[1]);
                noteDetailTitlebar.setBackgroundColor(sTitleBackGrounds[1]);
                noteDetailImgThumbtack.setImageResource(sThumbtackImg[1]);
                closeMenu();
                break;
            default:
                break;
        }
    }



    public boolean onBackPressed() {
        if (isNewNote) {

        }
        return false;
    }


    /**
     * 回显点击笔记时的内容
     */
    private void initNoteData() {
        if(activity.time!=null && activity.time.length()>0){
            noteDetailTvDate.setText(activity.time);
            time = activity.time;
        }else {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            time = df.format(new Date());
        }

        if(activity.content!=null && activity.content.length()>0){
            noteDetailEdit.setText(activity.content);
            noteDetailEdit.setSelection(noteDetailEdit.getText().length());
        }else {
            noteDetailEdit.setText("");
        }

        if(activity.id!=null && activity.id.length()>0){
            id = activity.id;
        }else {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sf = new SimpleDateFormat("ddHHmmss");
            id = sf.format(new Date());
        }
    }

    /**
     * 初始化背景色
     */
    public static final int[] sBackGrounds = {
            0xffe5fce8,// 绿色
            0xfffffdd7,// 黄色
            0xffffddde,// 红色
            0xffccf2fd,// 蓝色
            0xfff7f5f6,// 紫色
    };
    public static final int[] sTitleBackGrounds = {
            0xffcef3d4,// 绿色
            0xffebe5a9,// 黄色
            0xffecc4c3,// 红色
            0xffa9d5e2,// 蓝色
            0xffddd7d9,// 紫色
    };
    public static final int[] sThumbtackImg = {
            R.drawable.green,
            R.drawable.yellow,
            R.drawable.red,
            R.drawable.blue,
            R.drawable.purple
    };

    private void initBackground() {
        noteDetailEdit.setBackgroundColor(sBackGrounds[0]);
        noteDetailTitlebar.setBackgroundColor(sTitleBackGrounds[0]);
        noteDetailImgThumbtack.setImageResource(sThumbtackImg[0]);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            if (note_detail_menu.getVisibility() == View.GONE) {
                openMenu();
            } else {
                closeMenu();
            }
        }
        return true;
    }

    /**
     * 切换便签颜色的菜单
     */
    private void openMenu() {
        AnimationsUtils.openAnimation(note_detail_menu, noteDetailImgButton, 700);
    }

    /**
     * 切换便签颜色的菜单
     */
    private void closeMenu() {
        AnimationsUtils.closeAnimation(note_detail_menu, noteDetailImgButton, 500);
    }


    private void saveContent() {
        String content =  noteDetailEdit.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            ToastUtils.showShort("记录笔记不能为空");
            return;
        }


        if(activity.from!=null && activity.from.length()>0){
            //数据是使用Intent返回
            Intent intent = new Intent();
            //把返回数据存入Intent
            intent.putExtra("content",content);
            intent.putExtra("time",time);
            intent.putExtra("id",id);
            intent.putExtra("fromType",activity.from);
            //设置返回数据
            activity.setResult(100, intent);
            //关闭Activity
            activity.finish();
        }
    }

}
