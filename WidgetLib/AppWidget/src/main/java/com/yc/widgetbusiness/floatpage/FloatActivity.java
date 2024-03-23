package com.yc.widgetbusiness.floatpage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.yc.roundcorner.view.RoundTextView;
import com.yc.statusbar.bar.StateAppBar;
import com.yc.toastutils.ToastUtils;
import com.yc.toolutils.appinfo.AppWindowUtils;
import com.yc.window.FloatWindow;
import com.yc.window.draggable.MovingTouchListener;
import com.yc.toolutils.click.PerfectClickListener;
import com.yc.widgetbusiness.R;
import com.yc.window.draggable.SpringTouchListener;
import com.yc.window.inter.IClickListener;
import com.yc.window.permission.FloatWindowUtils;
import com.yc.window.permission.PermissionActivity;

public class FloatActivity extends AppCompatActivity {

    private RoundTextView tvWidgetFloat11;
    private RoundTextView tvWidgetFloat12;
    private RoundTextView tvWidgetFloat2;
    private RoundTextView tvWidgetFloat3;
    private RoundTextView tvWidgetFloat4;
    private FloatWindow floatWindow2;
    private FloatWindow floatWindowWxBig;
    private FloatWindow floatWindowWxSmall;
    private ConstraintLayout clFloatView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_float);
        StateAppBar.setStatusBarLightMode(this, Color.WHITE);
        initView();
        initListener();
    }

    private void initView() {
        tvWidgetFloat11 = findViewById(R.id.tv_widget_float1_1);
        tvWidgetFloat12 = findViewById(R.id.tv_widget_float1_2);
        tvWidgetFloat2 = findViewById(R.id.tv_widget_float2);
        tvWidgetFloat3 = findViewById(R.id.tv_widget_float3);
        tvWidgetFloat4 = findViewById(R.id.tv_widget_float4);
    }

    public void initListener() {
        tvWidgetFloat11.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                showFloat1();
            }
        });
        tvWidgetFloat12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (floatWindow1 != null) {
                    floatWindow1.dismiss();
                }
            }
        });
        tvWidgetFloat2.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (FloatWindowUtils.hasPermission(FloatActivity.this)) {
                        showFloat2();
                    } else {
                        PermissionActivity.request(FloatActivity.this, new PermissionActivity.PermissionListener() {
                            @Override
                            public void onSuccess() {
                                showFloat2();
                            }

                            @Override
                            public void onFail() {

                            }
                        });
                    }
                }
            }
        });
        tvWidgetFloat3.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (floatWindow2 != null) {
                    floatWindow2.dismiss();
                }
            }
        });
        tvWidgetFloat4.setOnClickListener(new PerfectClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                showFloat4();
            }
        });
    }

    private void showFloat2() {
        // 传入 Application 表示这个是一个全局的 Toast
        if (floatWindow2 == null) {
            floatWindow2 = new FloatWindow(this.getApplication())
                    .setContentView(R.layout.float_window_view)
                    .setSize(200, 400)
                    .setGravity(Gravity.END | Gravity.BOTTOM, 0, 200)
                    // 设置指定的拖拽规则
                    .setDraggable(new SpringTouchListener())
                    .setOnClickListener(R.id.icon, new IClickListener() {
                        @Override
                        public void onClick(FloatWindow toast, View view) {
                            ToastUtils.showRoundRectToast("我被点击了");
                        }
                    });
        }
        floatWindow2.show();
    }

    FloatWindow floatWindow1;

    private void showFloat1() {
        if (floatWindow1 == null) {
            floatWindow1 = new FloatWindow(this)
                    .setContentView(R.layout.float_window_view)
                    // 设置成可拖拽的
                    .setDraggable(new MovingTouchListener())
                    .setOnClickListener(R.id.icon, new IClickListener() {
                        @Override
                        public void onClick(FloatWindow floatWindow, View view) {
                            floatWindow.dismiss();
                        }
                    });
        }
        floatWindow1.show();
    }

    private boolean isSmall = false;

    private void showFloat4() {
        //整个大悬浮
        if (floatWindowWxBig == null) {
            floatWindowWxBig = new FloatWindow(this)
                    .setContentView(R.layout.float_wx_view)
                    .setDraggable(new SpringTouchListener())
                    .setOnClickListener(R.id.iv_video_back, new IClickListener() {
                        @Override
                        public void onClick(FloatWindow floatWindow, View view) {
                            //开启缩小动画
                            if (!isSmall) {
                                animSmall(clFloatView);
                                ToastUtils.showRoundRectToast("大浮窗开启缩小动画");
                            }
                            isSmall = !isSmall;
                        }
                    })
                    .setOnClickListener(R.id.fl_view, new IClickListener() {
                        @Override
                        public void onClick(FloatWindow floatWindow, View view) {
                            //开启缩小动画
                            if (isSmall) {
                                animBig(clFloatView);
                                ToastUtils.showRoundRectToast("大浮窗开启放大动画");
                            }
                            isSmall = !isSmall;
                        }
                    });
            clFloatView = floatWindowWxBig.getDecorView().findViewById(R.id.cl_float_view);
        }
        floatWindowWxBig.show();

        //添加小悬浮
        if (clFloatView != null) {
            if (floatWindowWxSmall == null) {
                floatWindowWxSmall = new FloatWindow(this)
                        .setContentView(R.layout.float_window_view)
                        .setDraggable(new SpringTouchListener())
                        .setOnClickListener(R.id.icon, new IClickListener() {
                            @Override
                            public void onClick(FloatWindow floatWindow, View view) {
                                ToastUtils.showRoundRectToast("小悬浮窗被点击了");
                            }
                        });
            }
            floatWindowWxSmall.showAsDropDown(clFloatView, Gravity.BOTTOM, 100, 200);
        }
    }

    private void animSmall(ConstraintLayout clFloatView) {
        //设置宽高
        ViewGroup.LayoutParams layoutParams = clFloatView.getLayoutParams();
        layoutParams.width = AppWindowUtils.getScreenWidth();
        layoutParams.height = AppWindowUtils.getScreenHeight();
        clFloatView.setLayoutParams(layoutParams);

        //设置显示动画
        clFloatView.setPivotX(0);
        clFloatView.setPivotY(0);

        AnimatorSet animatorSetScale = new AnimatorSet();
        final float from = 1f;
        final float to = 0.2f;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(clFloatView, "scaleX", from,to);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(clFloatView, "scaleY", from, to);
        animatorSetScale.setDuration(1000);
        animatorSetScale.setInterpolator(new DecelerateInterpolator());
        animatorSetScale.play(scaleX).with(scaleY);
        animatorSetScale.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                int i = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int n = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                clFloatView.measure(i, n);
                int width = clFloatView.getMeasuredWidth();
                int height = clFloatView.getMeasuredHeight();
                Log.d("FloatView 缩小" , "width: " + width + " height: " + height);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        animatorSetScale.start();
    }

    private void animBig(ConstraintLayout clFloatView) {
        //设置宽高
        ViewGroup.LayoutParams layoutParams = clFloatView.getLayoutParams();
        // 测量View大小
        int i = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int n = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        clFloatView.measure(i, n);
        layoutParams.width = clFloatView.getMeasuredWidth();
        layoutParams.height = clFloatView.getMeasuredHeight();
        Log.d("FloatView" , "width: " + layoutParams.width + " height: " + layoutParams.height);
        clFloatView.setLayoutParams(layoutParams);

        //设置显示动画
        clFloatView.setPivotX(0);
        clFloatView.setPivotY(0);

        AnimatorSet animatorSetScale = new AnimatorSet();
        final float from = 1.0f;
        final float to = 6f;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(clFloatView, "scaleX", from, to);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(clFloatView, "scaleY", from, to);
        animatorSetScale.setDuration(1000);
        animatorSetScale.setInterpolator(new DecelerateInterpolator());
        //同时执行
        animatorSetScale.play(scaleX).with(scaleY);
        animatorSetScale.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                int i = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int n = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                clFloatView.measure(i, n);
                int width = clFloatView.getMeasuredWidth();
                int height = clFloatView.getMeasuredHeight();
                Log.d("FloatView 放大" , "width: " + width + " height: " + height);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        animatorSetScale.start();
    }

}
