package com.yc.videoview.impl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.yc.videoview.FloatLifecycle;
import com.yc.videoview.FloatWindow;
import com.yc.videoview.tool.FloatMoveType;
import com.yc.videoview.tool.FloatScreenType;
import com.yc.videoview.tool.FloatWindowUtils;
import com.yc.videoview.abs.AbsFloatView;
import com.yc.videoview.inter.IFloatWindow;
import com.yc.videoview.inter.ILifecycleListener;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/10/21
 *     desc  : 定义悬浮Window接口类的具体实现类
 *     revise:
 * </pre>
 */
public class FloatWindowImpl implements IFloatWindow {


    private FloatWindow.Builder mBuilder;
    private AbsFloatView mFloatView;
    private FloatLifecycle mFloatLifecycle;
    private boolean isShow;
    private boolean once = true;
    private ValueAnimator mAnimator;
    private TimeInterpolator mDecelerateInterpolator;

    private FloatWindowImpl() {

    }

    public FloatWindowImpl(FloatWindow.Builder b) {
        mBuilder = b;
        //这一步相当于创建系统级的window，通过windowManager添加view并且展示
        if (mBuilder.mMoveType == FloatMoveType.FIXED) {
            if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.N_MR1) {
                mFloatView = new FloatPhoneImpl(b.mApplicationContext);
            } else {
                mFloatView = new FloatToastImpl(b.mApplicationContext);
            }
        } else {
            mFloatView = new FloatPhoneImpl(b.mApplicationContext);
            initTouchEvent();
        }
        mFloatView.setSize(mBuilder.mWidth, mBuilder.mHeight);
        mFloatView.setGravity(mBuilder.gravity, mBuilder.xOffset, mBuilder.yOffset);
        mFloatView.setView(mBuilder.mView);
        mFloatLifecycle = new FloatLifecycle(mBuilder.mApplicationContext,
                mBuilder.mShow, mBuilder.mActivities, new ILifecycleListener() {
            @Override
            public void onShow() {
                show();
            }

            @Override
            public void onHide() {
                hide();
            }

            @Override
            public void onPostHide() {
                postHide();
            }
        });
    }

    @Override
    public void show() {
        if (once) {
            mFloatView.init();
            once = false;
            isShow = true;
        } else {
            if (isShow) {
                return;
            }
            if (getView()!=null){
                getView().setVisibility(View.VISIBLE);
                isShow = true;
            }
        }
    }

    @Override
    public void hide() {
        if (once || !isShow) {
            return;
        }
        if (getView()!=null){
            getView().setVisibility(View.INVISIBLE);
        }
        isShow = false;
    }

    @Override
    public void dismiss() {
        mFloatView.dismiss();
        isShow = false;
    }

    @Override
    public void updateX(int x) {
        checkMoveType();
        mBuilder.xOffset = x;
        mFloatView.updateX(x);
    }

    @Override
    public void updateY(int y) {
        checkMoveType();
        mBuilder.yOffset = y;
        mFloatView.updateY(y);
    }

    @Override
    public void updateX(int screenType, float ratio) {
        checkMoveType();
        mBuilder.xOffset = (int) ((screenType == FloatScreenType.WIDTH ?
                FloatWindowUtils.getScreenWidth(mBuilder.mApplicationContext) :
                FloatWindowUtils.getScreenHeight(mBuilder.mApplicationContext)) * ratio);
        mFloatView.updateX(mBuilder.xOffset);

    }

    @Override
    public void updateY(int screenType, float ratio) {
        checkMoveType();
        mBuilder.yOffset = (int) ((screenType == FloatScreenType.WIDTH ?
                FloatWindowUtils.getScreenWidth(mBuilder.mApplicationContext) :
                FloatWindowUtils.getScreenHeight(mBuilder.mApplicationContext)) * ratio);
        mFloatView.updateY(mBuilder.yOffset);

    }

    @Override
    public int getX() {
        return mFloatView.getX();
    }

    @Override
    public int getY() {
        return mFloatView.getY();
    }


    @Override
    public View getView() {
        return mBuilder.mView;
    }

    void postHide() {
        if (once || !isShow) {
            return;
        }
        getView().post(new Runnable() {
            @Override
            public void run() {
                getView().setVisibility(View.INVISIBLE);
            }
        });
        isShow = false;
    }

    private void checkMoveType() {
        if (mBuilder.mMoveType == FloatMoveType.FIXED) {
            throw new IllegalArgumentException("FloatWindow of this tag is not allowed to move!");
        }
    }

    private void initTouchEvent() {
        switch (mBuilder.mMoveType) {
            case FloatMoveType.FREE:
                break;
            default:
                getView().setOnTouchListener(onTouchListener);
                break;
        }
    }

    private final View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        float lastX, lastY, changeX, changeY;
        int newX, newY;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    cancelAnimator();
                    break;
                case MotionEvent.ACTION_MOVE:
                    changeX = event.getRawX() - lastX;
                    changeY = event.getRawY() - lastY;
                    newX = (int) (mFloatView.getX() + changeX);
                    newY = (int) (mFloatView.getY() + changeY);
                    mFloatView.updateXY(newX, newY);
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    switch (mBuilder.mMoveType) {
                        case FloatMoveType.SLIDE:
                            int startX = mFloatView.getX();
                            int endX = (startX * 2 + v.getWidth() >
                                    FloatWindowUtils.getScreenWidth(mBuilder.mApplicationContext)) ?
                                    FloatWindowUtils.getScreenWidth(mBuilder.mApplicationContext) - v.getWidth() : 0;
                            mAnimator = ObjectAnimator.ofInt(startX, endX);
                            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    int x = (int) animation.getAnimatedValue();
                                    mFloatView.updateX(x);
                                }
                            });
                            startAnimator();
                            break;
                        case FloatMoveType.BACK:
                            PropertyValuesHolder pvhX = PropertyValuesHolder.ofInt("x", mFloatView.getX(), mBuilder.xOffset);
                            PropertyValuesHolder pvhY = PropertyValuesHolder.ofInt("y", mFloatView.getY(), mBuilder.yOffset);
                            mAnimator = ObjectAnimator.ofPropertyValuesHolder(pvhX, pvhY);
                            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    int x = (int) animation.getAnimatedValue("x");
                                    int y = (int) animation.getAnimatedValue("y");
                                    mFloatView.updateXY(x, y);
                                }
                            });
                            startAnimator();
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;

            }
            return false;
        }
    };

    /**
     * 开启动画
     */
    private void startAnimator() {
        if (mBuilder.mInterpolator == null) {
            if (mDecelerateInterpolator == null) {
                mDecelerateInterpolator = new DecelerateInterpolator();
            }
            mBuilder.mInterpolator = mDecelerateInterpolator;
        }
        mAnimator.setInterpolator(mBuilder.mInterpolator);
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator.removeAllUpdateListeners();
                mAnimator.removeAllListeners();
                mAnimator = null;
            }
        });
        mAnimator.setDuration(mBuilder.mDuration).start();
    }

    /**
     * 关闭动画
     */
    private void cancelAnimator() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

}
