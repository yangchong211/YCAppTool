package com.ns.yc.lifehelper.ui.other.toDo.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.ConstantImageApi;
import com.ns.yc.lifehelper.base.BaseActivity;
import com.ns.yc.lifehelper.base.BaseApplication;
import com.ns.yc.lifehelper.cache.CacheToDoDetail;
import com.ns.yc.lifehelper.ui.other.toDo.adapter.ChoosePriorityAdapter;
import com.ns.yc.lifehelper.ui.other.toDo.dialog.ChoosePaperColorDialog;
import com.ns.yc.lifehelper.utils.ImageUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/15
 * 描    述：时光日志添加页面
 * 修订历史：
 * ================================================
 */
public class ToDoAddActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.sdv_bg)
    ImageView sdvBg;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.iv_curr_priority)
    ImageView ivCurrPriority;
    @Bind(R.id.ll_priority)
    LinearLayout llPriority;
    @Bind(R.id.rv_choose_priority)
    RecyclerView rvChoosePriority;
    @Bind(R.id.ll_priority_list)
    LinearLayout llPriorityList;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.ll_content)
    LinearLayout llContent;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.content_new)
    LinearLayout contentNew;
    @Bind(R.id.fab_ok)
    FloatingActionButton fabOk;
    @Bind(R.id.cl)
    CoordinatorLayout cl;

    private final List<Integer> mBgImg = ConstantImageApi.createBgImg();
    private ChoosePriorityAdapter mChoosePriorityAdapter;
    private Integer mCurrBgUri;
    private Realm realm;
    private RealmResults<CacheToDoDetail> cacheToDoDetails;
    private String from;
    private String weekDate;
    private int type;
    private String date;
    private int priority = 0;

    @Override
    public int getContentView() {
        return R.layout.activity_to_do_edit;
    }

    @Override
    public void initView() {
        initToolBar();
        initStateView();
        initRealm();
        initIntentData();
        initRecycleView();
    }

    private void initRealm() {
        realm = BaseApplication.getInstance().getRealmHelper();
    }

    @Override
    public void initListener() {
        fabOk.setOnClickListener(this);
        llPriority.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_ok:
                saveNote();
                break;
            case R.id.ll_priority:
                showAddDialog();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_choose_icon:       //切换背景图片
                showIconChooseDialog();
                break;
        }
        return true;
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }


    private void initToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("添加任务");
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ImageUtils.loadImgByPicasso(this,R.drawable.bg_autumn_tree_min,sdvBg);
    }


    private void initStateView() {
        final View decorView = this.getWindow().getDecorView();
        decorView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int x = (int) (fabOk.getWidth() / 2 + fabOk.getX());
                    int y = (int) (fabOk.getHeight() / 2 + fabOk.getY());
                    Animator animator = ViewAnimationUtils.createCircularReveal(decorView, x, y, 0, decorView.getHeight());
                    animator.setDuration(400);
                    animator.start();
                }
            }
        });
    }


    private void initIntentData() {
        Intent intent = getIntent();
        if(intent!=null){
            type = intent.getIntExtra("type", 0);
            weekDate = intent.getStringExtra("weekDate");
            from = intent.getStringExtra("from");
            if(from.equals("new")){                 //新建
                if (getSupportActionBar() != null){
                    getSupportActionBar().setTitle(weekDate +"   添加任务");
                }
                etContent.requestFocus();
                int i = new Random(System.currentTimeMillis()).nextInt(mBgImg.size());
                loadBgImgWithIndex(i);
                date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
                tvDate.setText(date);
                //mChoosePriorityAdapter.setCheckItem(0);
            }else if(from.equals("update")){        //更新
                if (getSupportActionBar() != null){
                    getSupportActionBar().setTitle(weekDate +"   更新任务");
                }
            }
        }
    }

    /**初始化recycleView，分类*/
    private void initRecycleView() {
        rvChoosePriority.setLayoutManager(new GridLayoutManager(this, 4));
        mChoosePriorityAdapter = new ChoosePriorityAdapter(this);
        mChoosePriorityAdapter.setOnItemClickListener(new ChoosePriorityAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                priority = position;
                ivCurrPriority.setImageResource(ConstantImageApi.createPriorityIcons()[position]);
                mChoosePriorityAdapter.setCheckItem(position);
                hidePriorityList();
            }
        });
        rvChoosePriority.setAdapter(mChoosePriorityAdapter);
        mChoosePriorityAdapter.getCheckItem();
    }

    public void loadBgImgWithIndex(int i) {
        mCurrBgUri = mBgImg.get(i);
        ImageUtils.loadImgByPicasso(this,mBgImg.get(i),sdvBg);
    }

    /**弹出更改背景对话框*/
    private void showIconChooseDialog() {
        ChoosePaperColorDialog.newInstance(mCurrBgUri).show(getSupportFragmentManager(), "IconChooseDialog");
    }

    /**保存笔记*/
    private void saveNote() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();
        if(TextUtils.isEmpty(title)){
            ToastUtils.showShortSafe("标题不能为空");
            return;
        }
        if(TextUtils.isEmpty(content)){
            ToastUtils.showShortSafe("内容不能为空");
            return;
        }
        if(realm.where(CacheToDoDetail.class).findAll()!=null){
            cacheToDoDetails = realm.where(CacheToDoDetail.class).findAll();
        }
        if(from.equals("new")){
            addDataToRealm(title,content);
        }else if(from.equals("update")){
            updateDataToRealm(title,content);
        }
        Intent intent = new Intent();
        intent.putExtra("type",type);
        setResult(100,intent);
        finish();
    }

    private String[] prioritys = {"日常","一般","重要","紧急"};
    private void addDataToRealm(String title, String content) {
        realm.beginTransaction();
        CacheToDoDetail cacheToDoDetail = new CacheToDoDetail();
        cacheToDoDetail.setTitle(title);
        cacheToDoDetail.setContent(content);
        cacheToDoDetail.setTime(date);
        cacheToDoDetail.setDayOfWeek(weekDate);
        cacheToDoDetail.setIcon(mCurrBgUri);
        cacheToDoDetail.setPriority(prioritys[priority]);
        realm.copyToRealm(cacheToDoDetail);
        realm.commitTransaction();
    }

    private void updateDataToRealm(String title, String content) {

    }

    private void showAddDialog() {
        if (llPriorityList.isShown()){
            hidePriorityList();
        } else {
            showPriorityList();
        }
    }


    private void hidePriorityList() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(llContent, "translationY", 0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(llPriorityList, "alpha", 1, 1, 0f);
        alpha.setInterpolator(new FastOutSlowInInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                llPriorityList.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(alpha, animator);
        set.start();
    }


    private void showPriorityList() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(llContent, "translationY", SizeUtils.dp2px(36F));
        animator.setInterpolator(new FastOutSlowInInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                llPriorityList.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(animator, ObjectAnimator.ofFloat(llPriorityList, "alpha", 0f, 1, 1));
        set.start();
    }


}
