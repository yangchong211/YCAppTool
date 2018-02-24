package com.ns.yc.lifehelper.weight.noteView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.LinkedList;
import java.util.List;

/**
 * 感谢这篇博客的作者，http://blog.csdn.net/xiaanming/article/details/17718579<br>
 * 在这个基础上解决了原作者的问题:Adapter无法使用ViewHolder优化的问题，优化了手势识别率，并添加了trashView功能，
 * 优化自定义控件对外扩展性，解决在上下拉环境下手势冲突问题<br>
 *
 */
public class HTQDragGridView extends GridView {

    private long dragResponseMS = 700; // item长按响应的时间
    private int mDragPosition;// 正在拖拽的position

    private boolean isDrag = false; // 是否可以拖拽，用于控件内部逻辑实现
    private boolean canDrag = true; // 是否可用拖拽，主要用于外部开放设置
    private boolean mAnimationEnd = true;

    private int mDownX;
    private int mDownY;
    private int moveX;
    private int moveY;

    private View mStartDragItemView = null; // 刚开始拖拽的item对应的View
    private ImageView mDragImageView; // 用于拖拽时显示的幻影镜像
    private Bitmap mDragBitmap; // 幻影镜像对应的Bitmap
    private View mTrashView; // 删除item的垃圾桶图标

    private final Vibrator mVibrator; // 震动器
    private final int mStatusHeight;// 状态栏的高度
    private final WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowLayoutParams; // item镜像的布局参数

    private int mPoint2ItemTop; // 按下的点到所在item的上边缘的距离
    private int mPoint2ItemLeft;
    private int mOffset2Top; // DragGridView距离屏幕顶部的偏移量
    private int mOffset2Left;

    private int mDownScrollBorder; // DragGridView自动向下滚动的边界值
    private int mUpScrollBorder; // DragGridView自动向上滚动的边界值

    private DragGridBaseAdapter mDragAdapter;
    private int mNumColumns;
    private int mColumnWidth;
    private boolean mNumColumnsSet;
    private int mHorizontalSpacing;

    private static final int speed = 20; // DragGridView自动滚动的速度
    private static final int MOVE_OFFSET = 25;
    private boolean moved = false;

    public static final int HANDLE_START = 0x3587;
    public static final int HANDLE_CANCLE = 0x3588;
    public static final int HANDLE_FINISH = 0x3589;
    private static OnMoveListener moveListener; // 拖拽开始与结束监听器
    private OnDeleteListener deleteListener; // 移动到垃圾桶时的监听器

    private final TouchRect moveRect = new TouchRect();
    private final TouchRect gridRect = new TouchRect();
    private final TouchRect trashRect = new TouchRect();

    public HTQDragGridView(Context context) {
        this(context, null);
    }

    public HTQDragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HTQDragGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mVibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mStatusHeight = getStatusHeight(context); // 获取状态栏的高度

        if (!mNumColumnsSet) {
            mNumColumns = AUTO_FIT;
        }
    }

    /**
     * 获取状态栏的高度
     * 
     * @param context
     * @return
     */
    private static int getStatusHeight(Context context) {
        int statusHeight = 0;
        Rect localRect = new Rect();

        ((Activity) context).getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }


    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (moveListener != null) {
                if (msg.what == HANDLE_START) {
                    moveListener.startMove();
                } else if (msg.what == HANDLE_FINISH) {
                    moveListener.finishMove();
                } else if (msg.what == HANDLE_CANCLE) {
                    moveListener.cancelMove();
                }
            }
        };
    };

    // 用来处理是否为长按的Runnable

    private final Runnable mLongClickRunnable = new Runnable() {
        @Override
        public void run() {
            if (!canDrag) {
                return;
            }
            isDrag = true; // 设置可以拖拽
            moved = true;
            mHandler.sendEmptyMessage(HANDLE_START);
            mVibrator.vibrate(50); // 震动一下

            mStartDragItemView.setVisibility(View.INVISIBLE);// 隐藏该item

            createDragImage(mDragBitmap, mDownX, mDownY);
            mDragBitmap = null;
        }
    };

    /**
     * 若设置为AUTO_FIT，计算有多少列
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mNumColumns == AUTO_FIT) {
            int numFittedColumns = 1; // 可用列

            if (mColumnWidth > 0) {
                int gridWidth = Math.max(MeasureSpec.getSize(widthMeasureSpec)
                        - getPaddingLeft() - getPaddingRight(), 0);
                numFittedColumns = gridWidth / mColumnWidth;
                if (numFittedColumns > 0) {
                    while (numFittedColumns != 1) {
                        if (numFittedColumns * mColumnWidth
                                + (numFittedColumns - 1) * mHorizontalSpacing > gridWidth) {
                            numFittedColumns--;
                        } else {
                            break;
                        }
                    }
                }
            } else {
                numFittedColumns = 2;
            }
            mNumColumns = numFittedColumns;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initRecord();
    }

    /**
     * 初始化坐标数据
     */
    private void initRecord() {
        gridRect.left = this.getLeft();
        gridRect.right = this.getRight();
        gridRect.top = this.getTop();
        gridRect.bottom = this.getBottom();
        if (mTrashView != null) {
            trashRect.left = mTrashView.getLeft();
            trashRect.right = mTrashView.getRight();
            trashRect.bottom = mTrashView.getBottom();
            trashRect.top = mTrashView.getTop();
        }
    }

    /******************** preference method ********************/

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);

        if (adapter instanceof DragGridBaseAdapter) {
            mDragAdapter = (DragGridBaseAdapter) adapter;
        } else {
            throw new IllegalStateException(
                    "the adapter must be implements DragGridAdapter");
        }
    }

    @Override
    public void setNumColumns(int numColumns) {
        super.setNumColumns(numColumns);
        mNumColumnsSet = true;
        this.mNumColumns = numColumns;
    }

    @Override
    public void setColumnWidth(int columnWidth) {
        super.setColumnWidth(columnWidth);
        mColumnWidth = columnWidth;
    }

    @Override
    public void setHorizontalSpacing(int horizontalSpacing) {
        super.setHorizontalSpacing(horizontalSpacing);
        this.mHorizontalSpacing = horizontalSpacing;
    }

    /**
     * 设置响应拖拽的毫秒数，默认是700毫秒
     * 
     * @param dragResponseMS
     */
    public void setDragResponseMS(long dragResponseMS) {
        this.dragResponseMS = dragResponseMS;
    }

    public void setOnDeleteListener(OnDeleteListener l) {
        this.deleteListener = l;
    }

    public void setTrashView(View trashView) {
        this.mTrashView = trashView;
    }

    public void setDragEnable(boolean isDrag) {
        this.canDrag = isDrag;
        if (canDrag) {
            mHandler.removeCallbacks(mLongClickRunnable);
        }
    }

    /******************** touch method ********************/

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (canDrag) {
            switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                moveRect.left = mDownX - MOVE_OFFSET;
                moveRect.right = mDownX + MOVE_OFFSET;
                moveRect.top = mDownY - MOVE_OFFSET;
                moveRect.bottom = mDownY + MOVE_OFFSET;

                // 根据按下的X,Y坐标获取所点击item的position
                mDragPosition = pointToPosition(mDownX, mDownY);

                if (mDragPosition == AdapterView.INVALID_POSITION) {
                    return super.dispatchTouchEvent(ev);
                }

                // 使用Handler延迟dragResponseMS执行mLongClickRunnable
                mHandler.postDelayed(mLongClickRunnable, dragResponseMS);

                // 根据position获取该item所对应的View
                mStartDragItemView = getChildAt(mDragPosition
                        - getFirstVisiblePosition());

                mPoint2ItemTop = mDownY - mStartDragItemView.getTop();
                mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();

                mOffset2Top = (int) (ev.getRawY() - mDownY);
                mOffset2Left = (int) (ev.getRawX() - mDownX);

                // 获取DragGridView自动向上滚动的偏移量，小于这个值，DragGridView向下滚动
                mDownScrollBorder = getHeight() / 5;
                // 大于这个值，DragGridView向上滚动
                mUpScrollBorder = getHeight() * 4 / 5;

                // 开启mDragItemView绘图缓存
                mStartDragItemView.setDrawingCacheEnabled(true);
                // 获取mDragItemView在缓存中的Bitmap对象
                mDragBitmap = Bitmap.createBitmap(mStartDragItemView
                        .getDrawingCache());
                // 这一步很关键，释放绘图缓存，避免出现重复的镜像
                mStartDragItemView.destroyDrawingCache();
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果我们在按下的item上面移动，只要不超过item的边界我们就不移除mRunnable
                if (!isTouchInItem(moveRect, ev.getX(), ev.getY())) {
                    mHandler.removeCallbacks(mLongClickRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:
                mHandler.removeCallbacks(mScrollRunnable);
                mHandler.removeCallbacks(mLongClickRunnable);
                if (moved && getAdapter().getCount() > 0) {
                    mHandler.sendEmptyMessage(HANDLE_FINISH);
                } else {
                    mHandler.sendEmptyMessage(HANDLE_CANCLE);
                }
                moved = false;
                break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isDrag && canDrag && mDragImageView != null) {
            switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initRecord();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) ev.getX();
                moveY = (int) ev.getY();

                onDragItem(moveX, moveY);// 拖动item

                if (mTrashView != null) {
                    if (inTrash(moveX, moveY)) {
                        mTrashView.setScaleX(1.7f);
                        mTrashView.setScaleY(1.7f);
                    } else {
                        mTrashView.setScaleX(1f);
                        mTrashView.setScaleY(1f);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                onStopDrag();
                isDrag = false;
                if (deleteListener != null && inTrash(ev.getX(), ev.getY())) {
                    deleteListener.onDelete(mDragPosition);
                }
                break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 是否点击在GridView的item上面
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isTouchInItem(TouchRect moveRect, float x, float y) {
        // 防止手抖的处理，如果是横向在item上移动是没有影响的，
        // 但是纵向由于外层上下拉控件还是会有影响，具体解决请看NoteBookFragment类中的mSwipeRefreshLayout.setOnTouchListener方法
        if (x < moveRect.right && x > moveRect.left && y < moveRect.bottom
                && y > moveRect.top) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建拖动的镜像
     * 
     * @param bitmap
     * @param downX
     *            按下的点相对父控件的X坐标
     * @param downY
     *            按下的点相对父控件的X坐标
     */
    private void createDragImage(Bitmap bitmap, int downX, int downY) {
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; // 图片之外的其他地方透明

        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowLayoutParams.x = downX - mPoint2ItemLeft + mOffset2Left;
        mWindowLayoutParams.y = downY - mPoint2ItemTop + mOffset2Top
                - mStatusHeight;
        mWindowLayoutParams.alpha = 0.55f; // 透明度


        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(bitmap);
        mWindowManager.addView(mDragImageView, mWindowLayoutParams);
    }

    /**
     * 从界面上面移动拖动镜像
     */
    private void removeDragImage() {
        if (mDragImageView != null) {
            mWindowManager.removeView(mDragImageView);
            mDragImageView = null;
        }
    }

    /**
     * 拖动item，在里面实现了item镜像的位置更新，item的相互交换以及GridView的自行滚动
     * 
     * @param moveX
     * @param moveY
     */
    private void onDragItem(int moveX, int moveY) {
        mWindowLayoutParams.x = moveX - mPoint2ItemLeft + mOffset2Left;
        mWindowLayoutParams.y = moveY - mPoint2ItemTop + mOffset2Top
                - mStatusHeight;
        mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams); // 更新镜像的位置
        onSwapItem(moveX, moveY);
        // GridView自动滚动
        mHandler.post(mScrollRunnable);
    }

    /**
     * 手指当前处于垃圾桶图标上
     * 
     * @param x
     * @param y
     * @return
     */
    private boolean inTrash(float x, float y) {
        x += gridRect.left;
        y += gridRect.top;
        if (x > trashRect.left && x < trashRect.right && y > trashRect.top
                && y < trashRect.bottom) {
            if (mHandler != null && mScrollRunnable != null) {
                mHandler.removeCallbacks(mScrollRunnable);
            }
            if (mDragImageView != null) {
                mDragImageView.setScaleX(0.6f);
                mDragImageView.setScaleY(0.6f);
            }
            return true;
        } else {
            if (mDragImageView != null) {
                mDragImageView.setScaleX(1f);
                mDragImageView.setScaleY(1f);
            }
            return false;
        }
    }

    /**
     * 当moveY的值大于向上滚动的边界值，触发GridView自动向上滚动 当moveY的值小于向下滚动的边界值，触发GridView自动向下滚动
     * 否则不进行滚动
     */
    private final Runnable mScrollRunnable = new Runnable() {

        @Override
        public void run() {
            int scrollY;
            if (getFirstVisiblePosition() == 0
                    || getLastVisiblePosition() == getCount() - 1) {
                mHandler.removeCallbacks(mScrollRunnable);
            }

            if (moveY > mUpScrollBorder) {
                scrollY = speed;
                mHandler.postDelayed(mScrollRunnable, 25);
            } else if (moveY < mDownScrollBorder) {
                scrollY = -speed;
                mHandler.postDelayed(mScrollRunnable, 25);
            } else {
                scrollY = 0;
                mHandler.removeCallbacks(mScrollRunnable);
            }

            smoothScrollBy(scrollY, 10);
        }
    };

    /**
     * 交换item,并且控制item之间的显示与隐藏效果
     * 
     * @param moveX
     * @param moveY
     */
    private void onSwapItem(int moveX, int moveY) {
        // 获取我们手指移动到的那个item的position

        final int tempPosition = pointToPosition(moveX, moveY);

        // 假如tempPosition 改变了并且tempPosition不等于-1,则进行交换

        if (tempPosition != mDragPosition
                && tempPosition != AdapterView.INVALID_POSITION
                && mAnimationEnd) {
            mDragAdapter.reorderItems(mDragPosition, tempPosition);
            mDragAdapter.setHideItem(tempPosition);

            final ViewTreeObserver observer = getViewTreeObserver();
            observer.addOnPreDrawListener(new OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    observer.removeOnPreDrawListener(this);
                    animateReorder(mDragPosition, tempPosition);
                    mDragPosition = tempPosition;
                    return true;
                }
            });

        }
    }

    /**
     * 创建移动动画
     * 
     * @param view
     * @param startX
     * @param endX
     * @param startY
     * @param endY
     * @return
     */
    private AnimatorSet createTranslationAnimations(View view, float startX,
                                                    float endX, float startY, float endY) {
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "translationX",
                startX, endX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "translationY",
                startY, endY);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        return animSetXY;
    }

    /**
     * item的交换动画效果
     * 
     * @param oldPosition
     * @param newPosition
     */
    private void animateReorder(final int oldPosition, final int newPosition) {
        boolean isForward = newPosition > oldPosition;
        List<Animator> resultList = new LinkedList<Animator>();
        if (isForward) {
            for (int pos = oldPosition; pos < newPosition; pos++) {
                View view = getChildAt(pos - getFirstVisiblePosition());
                if (view == null) {
                    continue;
                }
                if ((pos + 1) % mNumColumns == 0) {
                    resultList.add(createTranslationAnimations(view,
                            -view.getWidth() * (mNumColumns - 1), 0,
                            view.getHeight(), 0));
                } else {
                    resultList.add(createTranslationAnimations(view,
                            view.getWidth(), 0, 0, 0));
                }
            }
        } else {
            for (int pos = oldPosition; pos > newPosition; pos--) {
                View view = getChildAt(pos - getFirstVisiblePosition());
                if ((pos + mNumColumns) % mNumColumns == 0) {
                    resultList.add(createTranslationAnimations(view,
                            view.getWidth() * (mNumColumns - 1), 0,
                            -view.getHeight(), 0));
                } else {
                    resultList.add(createTranslationAnimations(view,
                            -view.getWidth(), 0, 0, 0));
                }
            }
        }

        AnimatorSet resultSet = new AnimatorSet();
        resultSet.playTogether(resultList);
        resultSet.setDuration(300);
        resultSet.setInterpolator(new AccelerateDecelerateInterpolator());
        resultSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimationEnd = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimationEnd = true;
            }
        });
        resultSet.start();
    }

    /**
     * 停止拖拽我们将之前隐藏的item显示出来，并将镜像移除
     */
    private void onStopDrag() {
        View view = getChildAt(mDragPosition - getFirstVisiblePosition());
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        mDragAdapter.setHideItem(-1);
        removeDragImage();

        if (mTrashView != null) {
            mTrashView.setScaleX(1f);
            mTrashView.setScaleY(1f);
        }
    }

    public void setOnMoveListener(OnMoveListener l) {
        moveListener = l;
    }

    public interface OnMoveListener {
        void startMove();

        void finishMove();

        void cancelMove();
    }

    public interface OnDeleteListener {
        void onDelete(int position);
    }

    public interface DragGridBaseAdapter {
        /**
         * 移动时回调
         */
        void reorderItems(int oldPosition, int newPosition);

        /**
         * 隐藏时回调
         */
        void setHideItem(int hidePosition);
    }

    private class TouchRect {
        int top;
        int bottom;
        int left;
        int right;
    }
}