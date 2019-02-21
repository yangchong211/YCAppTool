package com.ycbjie.todo.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.ycbjie.library.api.ConstantImageApi;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.todo.R;
import com.ycbjie.todo.contract.WorkNewContract;
import com.ycbjie.library.db.realm.RealmWorkDoHelper;
import com.ycbjie.library.utils.time.DateUtils;
import com.ycbjie.todo.inter.WorkState;
import com.ycbjie.library.db.cache.CacheTaskDetailEntity;
import com.ycbjie.todo.presenter.WorkNewPresenter;
import com.ycbjie.todo.ui.adapter.ChoosePriorityAdapter;
import com.ycbjie.todo.ui.dialog.WorkChooseColorDialog;
import com.ycbjie.library.utils.image.ImageUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/10/21
 * 描    述：此版块训练dagger2+MVP
 * 修订历史：
 * ================================================
 */
public class WorkNewActivity extends BaseActivity implements WorkNewContract.View, View.OnClickListener {


    private CoordinatorLayout cl;
    private ImageView sdvBg;
    private Toolbar toolbar;
    private LinearLayout contentNew;
    private EditText etTitle;
    private LinearLayout llPriority;
    private ImageView ivCurrPriority;
    private LinearLayout llPriorityList;
    private RecyclerView rvChoosePriority;
    private LinearLayout llContent;
    private EditText etContent;
    private TextView tvDate;
    private FloatingActionButton fabOk;

    //@Inject
    //WorkNewPresenter presenter;
    WorkNewContract.Presenter presenter = new WorkNewPresenter(this);

    private ChoosePriorityAdapter mChoosePriorityAdapter;
    private int mPriority = 0;
    private WorkState mState;
    private final List<Integer> mBgImg = ConstantImageApi.createBgImg();
    private CacheTaskDetailEntity mEntityFromMain;
    private int mCurrBgUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.subscribe();
        presenter.bindView(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
        mChoosePriorityAdapter = null;
        mEntityFromMain = null;
        mState = null;
        rvChoosePriority = null;
        sdvBg = null;
    }


    @Override
    public int getContentView() {
        return R.layout.activity_to_do_edit;
    }


    @Override
    public void initView() {


        cl = (CoordinatorLayout) findViewById(R.id.cl);
        sdvBg = (ImageView) findViewById(R.id.sdv_bg);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contentNew = (LinearLayout) findViewById(R.id.content_new);
        etTitle = (EditText) findViewById(R.id.et_title);
        llPriority = (LinearLayout) findViewById(R.id.ll_priority);
        ivCurrPriority = (ImageView) findViewById(R.id.iv_curr_priority);
        llPriorityList = (LinearLayout) findViewById(R.id.ll_priority_list);
        rvChoosePriority = (RecyclerView) findViewById(R.id.rv_choose_priority);
        llContent = (LinearLayout) findViewById(R.id.ll_content);
        etContent = (EditText) findViewById(R.id.et_content);
        tvDate = (TextView) findViewById(R.id.tv_date);
        fabOk = (FloatingActionButton) findViewById(R.id.fab_ok);


        initToolBar();
        initStateView();
        initRecycleView();
        initIntentData();
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
        int i = v.getId();
        if (i == R.id.fab_ok) {
            saveNote();

        } else if (i == R.id.ll_priority) {
            showAddDialog();

        }
    }


    private void saveNote() {
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort("请填写内容后保存哦");
            return;
        }
        if (TextUtils.isEmpty(title)) {
            title = content.substring(0, Math.min(5, content.length()));
        }

        int dayOfWeek = getIntent().getIntExtra(Constant.INTENT_EXTRA_DAY_OF_WEEK, Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        CacheTaskDetailEntity taskDetailEntity = new CacheTaskDetailEntity(dayOfWeek);
        taskDetailEntity.setTitle(title);
        taskDetailEntity.setContent(content);
        // 不论编辑或者新建 都将状态设置成未完成状态
        taskDetailEntity.setState(Constant.TaskState.DEFAULT);
        taskDetailEntity.setTimeStamp(System.currentTimeMillis());
        taskDetailEntity.setIcon(mCurrBgUri);
        taskDetailEntity.setPriority(mPriority);
        if (mState != null) mState.onComplete(taskDetailEntity);
        finish();
    }


    private void showAddDialog() {
        if (llPriorityList.isShown()){
            hidePriorityList();
        } else {
            showPriorityList();
        }
    }


    private void initToolBar() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("添加任务");
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ImageUtils.loadImgByPicasso(this,ConstantImageApi.NarrowImage[0],sdvBg);
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



    /**初始化recycleView，分类*/
    private void initRecycleView() {
        rvChoosePriority.setLayoutManager(new GridLayoutManager(this, 4));
        mChoosePriorityAdapter = new ChoosePriorityAdapter(this);
        mChoosePriorityAdapter.setOnItemClickListener(new ChoosePriorityAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                mPriority = position;
                ivCurrPriority.setImageResource(ConstantImageApi.createPriorityIcons()[position]);
                mChoosePriorityAdapter.setCheckItem(position);
                hidePriorityList();
            }
        });
        rvChoosePriority.setAdapter(mChoosePriorityAdapter);
        mChoosePriorityAdapter.getCheckItem();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();//切换背景图片
        if (i == android.R.id.home) {
            finish();

        } else if (i == R.id.action_choose_icon) {
            showIconChooseDialog();

        }
        return true;
    }


    private void showIconChooseDialog() {
        WorkChooseColorDialog.newInstance(mCurrBgUri).show(getSupportFragmentManager(), "IconChooseDialog");
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
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }




    private void initIntentData() {
        Intent intent = getIntent();
        int mode = intent.getIntExtra(Constant.INTENT_EXTRA_MODE_OF_NEW_ACT, Constant.MODE_OF_NEW_ACT.MODE_QUICK);
        if (mode == Constant.MODE_OF_NEW_ACT.MODE_EDIT){
            mState = new EditOld();
        } else if (mode == Constant.MODE_OF_NEW_ACT.MODE_CREATE){
            mState = new CreateNew();
        } else{
            mState = new QuickNew();
        }
        mState.initView(intent);
    }


    private class CreateNew implements WorkState {
        @Override
        public void initView(Intent intent) {
            if (getSupportActionBar() != null){
                getSupportActionBar().setTitle("添加任务");
            }
            etContent.requestFocus();
            int i = new Random(System.currentTimeMillis()).nextInt(mBgImg.size());
            loadBgImgWithIndex(i);
            String date = DateUtils.formatDate(System.currentTimeMillis());
            tvDate.setText(date);
            mChoosePriorityAdapter.setCheckItem(0);
        }

        @Override
        public void onComplete(CacheTaskDetailEntity entity) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.INTENT_BUNDLE_NEW_TASK_DETAIL, entity);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
        }
    }


    private class QuickNew implements WorkState {

        @Override
        public void initView(Intent intent) {
            if (getSupportActionBar() != null){
                getSupportActionBar().setTitle("添加任务");
            }
            etContent.requestFocus();
            int i = new Random(System.currentTimeMillis()).nextInt(mBgImg.size());
            loadBgImgWithIndex(i);
            String date = DateUtils.formatDate(System.currentTimeMillis());
            tvDate.setText(date);
            mChoosePriorityAdapter.setCheckItem(0);
        }

        @Override
        public void onComplete(CacheTaskDetailEntity entity) {
            RealmWorkDoHelper.getInstance().insertTask(entity);
        }
    }



    private class EditOld implements WorkState {
        @Override
        public void initView(Intent intent) {
            if (getSupportActionBar() != null){
                getSupportActionBar().setTitle("编辑任务");
            }
            fabOk.hide();
            etTitle.setFocusable(false);
            etTitle.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    etTitle.setFocusableInTouchMode(true);
                    fabOk.show();
                    return false;
                }
            });

            etContent.setFocusable(false);
            etContent.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    etContent.setFocusableInTouchMode(true);
                    fabOk.show();
                    return false;
                }
            });

            mEntityFromMain = (CacheTaskDetailEntity) intent.getSerializableExtra(Constant.INTENT_EXTRA_EDIT_TASK_DETAIL_ENTITY);
            intent.putExtra(Constant.INTENT_EXTRA_DAY_OF_WEEK, mEntityFromMain.getDayOfWeek());
            mPriority = mEntityFromMain.getPriority();
            etTitle.setText(mEntityFromMain.getTitle());
            etContent.setText(mEntityFromMain.getContent());
            loadBgImgWithUri(mEntityFromMain.getIcon());
            String date = DateUtils.formatDate(mEntityFromMain.getTimeStamp());
            tvDate.setText(date);
            ivCurrPriority.setImageResource(ConstantImageApi.createPriorityIcons()[mEntityFromMain.getPriority()]);
            mChoosePriorityAdapter.setCheckItem(mEntityFromMain.getPriority());
        }

        @Override
        public void onComplete(CacheTaskDetailEntity entity) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            // edited & content is changed
            bundle.putSerializable(Constant.INTENT_BUNDLE_NEW_TASK_DETAIL, entity);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
        }
    }

    public void loadBgImgWithIndex(int i) {
        loadBgImgWithUri(mBgImg.get(i));
    }


    public void loadBgImgWithUri(int uri) {
        mCurrBgUri = uri;
        ImageUtils.loadImgByPicasso(this,uri,sdvBg);
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
