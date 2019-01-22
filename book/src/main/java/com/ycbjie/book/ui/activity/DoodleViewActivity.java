package com.ycbjie.book.ui.activity;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ycbjie.book.R;
import com.ycbjie.book.weight.doodleView.DoodleView;
import com.ycbjie.library.arounter.ARouterConstant;
import com.ycbjie.library.base.mvp.BaseActivity;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/8/31
 *     desc  : 绘画板
 *     revise:
 * </pre>
 */
@Route(path = ARouterConstant.ACTIVITY_BOOK_DOODLE_ACTIVITY)
public class DoodleViewActivity extends BaseActivity {

    private Toolbar toolbar;
    private FrameLayout llTitleMenu;
    private TextView toolbarTitle;
    DoodleView dvView;

    private AlertDialog mColorDialog;
    private AlertDialog mPaintDialog;
    private AlertDialog mShapeDialog;

    private static final String TAG = "DoodleViewActivity";

    @Override
    public int getContentView() {
        return R.layout.activity_doodleview;
    }

    @Override
    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        dvView = findViewById(R.id.dv_view);
        dvView.setSize(dip2px(5));
        initToolBar();
    }

    private void initToolBar() {
        toolbarTitle.setText("绘画板");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return dvView.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doodle_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.main_color) {
            showColorDialog();
        } else if (i == R.id.main_size) {
            showSizeDialog();
        } else if (i == R.id.main_action) {
            showShapeDialog();
        } else if (i == R.id.main_reset) {
            dvView.reset();
        } else if (i == R.id.main_save) {
            String path = dvView.saveBitmap(dvView);
            Log.d(TAG, "onOptionsItemSelected: " + path);
            Toast.makeText(this, "保存图片的路径为：" + path, Toast.LENGTH_SHORT).show();

        }
        return true;
    }

    /**
     * 显示选择画笔颜色的对话框
     */
    private void showColorDialog() {
        if (mColorDialog == null) {
            mColorDialog = new AlertDialog.Builder(this)
                    .setTitle("选择颜色")
                    .setSingleChoiceItems(new String[]{"蓝色", "红色", "黑色"}, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            dvView.setColor("#0000ff");
                                            break;
                                        case 1:
                                            dvView.setColor("#ff0000");
                                            break;
                                        case 2:
                                            dvView.setColor("#272822");
                                            break;
                                        default:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        }
        mColorDialog.show();
    }

    /**
     * 显示选择画笔粗细的对话框
     */
    private void showSizeDialog() {
        if (mPaintDialog == null) {
            mPaintDialog = new AlertDialog.Builder(this)
                    .setTitle("选择画笔粗细")
                    .setSingleChoiceItems(new String[]{"细", "中", "粗"}, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            dvView.setSize(dip2px(5));
                                            break;
                                        case 1:
                                            dvView.setSize(dip2px(10));
                                            break;
                                        case 2:
                                            dvView.setSize(dip2px(15));
                                            break;
                                        default:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        }
        mPaintDialog.show();
    }

    /**
     * 显示选择画笔形状的对话框
     */
    private void showShapeDialog() {
        if (mShapeDialog == null) {
            mShapeDialog = new AlertDialog.Builder(this)
                    .setTitle("选择形状")
                    .setSingleChoiceItems(new String[]{"路径", "直线", "矩形", "圆形", "实心矩形", "实心圆"}, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            dvView.setType(DoodleView.ActionType.Path);
                                            break;
                                        case 1:
                                            dvView.setType(DoodleView.ActionType.Line);
                                            break;
                                        case 2:
                                            dvView.setType(DoodleView.ActionType.Rect);
                                            break;
                                        case 3:
                                            dvView.setType(DoodleView.ActionType.Circle);
                                            break;
                                        case 4:
                                            dvView.setType(DoodleView.ActionType.FillEcRect);
                                            break;
                                        case 5:
                                            dvView.setType(DoodleView.ActionType.FilledCircle);
                                            break;
                                        default:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        }
        mShapeDialog.show();
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}














