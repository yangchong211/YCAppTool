package com.ns.yc.lifehelper.ui.data.view.activity;

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

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.weight.doodleView.DoodleView;

import butterknife.BindView;
/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/31
 * 描    述：绘画板
 * 修订历史：
 * 参考：http://www.jianshu.com/p/f05f71446771
 * ================================================
 */

public class DoodleViewActivity extends BaseActivity {

    @BindView(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.dv_view)
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
        switch (item.getItemId()) {
            case R.id.main_color:
                showColorDialog();
                break;
            case R.id.main_size:
                showSizeDialog();
                break;
            case R.id.main_action:
                showShapeDialog();
                break;
            case R.id.main_reset:
                dvView.reset();
                break;
            case R.id.main_save:
                String path = dvView.saveBitmap(dvView);
                Log.d(TAG, "onOptionsItemSelected: " + path);
                Toast.makeText(this, "保存图片的路径为：" + path, Toast.LENGTH_SHORT).show();
                break;
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














