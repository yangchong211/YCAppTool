package com.yc.widgetbusiness.dialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yc.library.base.mvp.BaseActivity;
import com.yc.popup.CustomPopupWindow;
import com.yc.statusbar.bar.StateAppBar;
import com.yc.toastutils.ToastUtils;
import com.yc.toolutils.appinfo.AppWindowUtils;
import com.yc.widgetbusiness.R;

public class DialogActivity extends BaseActivity {

    private LinearLayout llMain;
    private TextView tvPop1;
    private TextView tvPop2;
    private TextView tvPop3;
    private TextView tvPop4;

    @Override
    public int getContentView() {
        return R.layout.activity_dialog_main;
    }

    @Override
    public void initView() {
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        llMain = findViewById(R.id.activity_main);
        tvPop1 = findViewById(R.id.tv_pop_1);
        tvPop2 = findViewById(R.id.tv_pop_2);
        tvPop3 = findViewById(R.id.tv_pop_3);
        tvPop4 = findViewById(R.id.tv_pop_4);
    }

    @Override
    public void initListener() {
        tvPop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop1();
            }
        });
        tvPop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop2();
            }
        });
        tvPop3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop3();
            }
        });
        tvPop4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop4();
            }
        });
    }

    private AlertDialog alertDialog=null;
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("潇湘剑雨");
        builder.setTitle("这个是标题");
        builder.setView(R.layout.view_pop_custom);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
                alertDialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }


    private void showPop1() {
        //创建对象
        PopupWindow popupWindow = new PopupWindow(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.view_pop_custom, null);
        //设置view布局
        popupWindow.setContentView(inflate);
        popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置动画的方法
        popupWindow.setAnimationStyle(R.style.BottomDialog);
        //设置PopUpWindow的焦点，设置为true之后，PopupWindow内容区域，才可以响应点击事件
        popupWindow.setTouchable(true);
        //设置背景透明
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        //点击空白处的时候让PopupWindow消失
        popupWindow.setOutsideTouchable(true);
        // true时，点击返回键先消失 PopupWindow
        // 但是设置为true时setOutsideTouchable，setTouchable方法就失效了（点击外部不消失，内容区域也不响应事件）
        // false时PopupWindow不处理返回键，默认是false
        popupWindow.setFocusable(false);
        //设置dismiss事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        boolean showing = popupWindow.isShowing();
        if (!showing){
            //show，并且可以设置位置
            popupWindow.showAsDropDown(tvPop4);
        }
    }


    private void showPop2() {
        CustomPop customPop = new CustomPop(this);
        customPop.setDelayedMsDismiss(2500);
        customPop.setBgAlpha(0.5f);
        customPop.showAsDropDown(tvPop4, 0, 0);
    }

    private CustomPopupWindow popWindow3;
    private void showPop3(){
        View contentView = LayoutInflater.from(this).inflate(R.layout.view_pop_custom,null);
        //处理popWindow 显示内容,recycleView
        //handleListView(contentView);
        //创建并显示popWindow
        popWindow3 = new CustomPopupWindow.PopupWindowBuilder(this)
                //.setView(R.layout.pop_layout)
                .setView(contentView)
                //设置是否可以设置焦点
                .setFocusable(true)
                //弹出popWindow时，背景是否变暗
                .enableBackgroundDark(true)
                //控制亮度
                .setBgDarkAlpha(0.5f)
                //设置是否可以点击弹窗外部消失
                .setOutsideTouchable(true)
                //设置动画
                .setAnimationStyle(R.style.TopAnimationStyle1)
                //设置弹窗关闭监听
                .setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        //对话框销毁时
                    }
                })
                //创建弹窗
                .create()
                //传入x，y值位置展示
                .showAsDropDown(tvPop4,0,0);
    }

    private void showPop4(){
        final CustomPopupWindow popWindow = new CustomPopupWindow
                .PopupWindowBuilder(this)
                .setView(R.layout.view_pop_custom)
                .setOutsideTouchable(false)
                .setClippingEnable(false)
                .size(AppWindowUtils.getScreenWidth(),500)
                .setAnimationStyle(R.style.TopAnimationStyle2)
                .setBgDarkAlpha(0.5f)
                .create();
        popWindow.setViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showRoundRectToast("我被点击了");
                popWindow.dismiss();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popWindow.showAtLocation(tvPop4,Gravity.TOP,0,  0);
        }
    }

    @Override
    public void initData() {

    }


    private void showSnackBar(View v){
        Snackbar sb = Snackbar.make(v,"潇湘剑雨",Snackbar.LENGTH_LONG)
                .setAction("删除吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击了"是吗？"字符串操作
                        ToastUtils.showRoundRectToast("逗比");
                    }
                })
                .setActionTextColor(Color.RED)
                .setText("杨充是个逗比")
                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        switch (event) {
                            case Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE:
                            case Snackbar.Callback.DISMISS_EVENT_MANUAL:
                            case Snackbar.Callback.DISMISS_EVENT_SWIPE:
                            case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                                ToastUtils.showRoundRectToast("删除成功");
                                break;
                            case Snackbar.Callback.DISMISS_EVENT_ACTION:
                                ToastUtils.showRoundRectToast("撤销了删除操作");
                                break;
                            default:
                                break;
                        }
                        Log.d("MainActivity","onDismissed");
                    }
                    @Override
                    public void onShown(Snackbar transientBottomBar) {
                        super.onShown(transientBottomBar);
                        Log.d("MainActivity","onShown");
                    }
                });
        sb.show();
        //sb.dismiss();
    }
}
