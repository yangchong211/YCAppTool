package com.ycbjie.zoomimagelib.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.ycbjie.zoomimagelib.anim.FlingAnimator;
import com.ycbjie.zoomimagelib.anim.MaskAnimator;
import com.ycbjie.zoomimagelib.anim.ScaleAnimator;
import com.ycbjie.zoomimagelib.config.ZoomConfig;
import com.ycbjie.zoomimagelib.listener.OnZoomClickListener;
import com.ycbjie.zoomimagelib.listener.OnZoomLongClickListener;
import com.ycbjie.zoomimagelib.utils.MathUtils;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/30
 *     desc  : 一款支持缩放与滑动处理的图片控件
 *     revise:
 * </pre>
 */
public class ZoomImageView extends AppCompatImageView {


    /**
     * 外层变换矩阵，如果是单位矩阵，那么图片是fit center状态
     */
    private Matrix mOuterMatrix = new Matrix();
    /**
     * 矩形遮罩
     */
    private RectF mMask;
    /**
     * 当前手势状态
     */
    private int mPinchMode = ZoomConfig.PINCH_MODE_FREE;
    /**
     * 在单指模式下:记录上一次手指的位置,用于计算新的位置和上一次位置的差值.
     * 双指模式下:记录两个手指的中点,作为和mScaleCenter绑定的点.
     * 这个绑定可以保证mScaleCenter无论如何都会跟随这个中点.
     */
    private PointF mLastMovePoint = new PointF();
    /**
     * 缩放模式下图片的缩放中点，为其指代的点经过innerMatrix变换之后的值.
     * 其指代的点在手势过程中始终跟随mLastMovePoint，通过双指缩放时,其为缩放中心点.
     */
    private PointF mScaleCenter = new PointF();
    /**
     * 缩放模式下的基础缩放比例
     * 为外层缩放值除以开始缩放时两指距离，其值乘上最新的两指之间距离为最新的图片缩放比例.
     */
    private float mScaleBase = 0;
    /**
     * 图片缩放动画
     * 缩放模式把图片的位置大小超出限制之后触发，双击图片放大或缩小时触发，手动调用outerMatrixTo触发.
     */
    private ScaleAnimator mScaleAnimator;
    /**
     * 滑动产生的惯性动画
     */
    private FlingAnimator mFlingAnimator;
    /**
     * mask修改的动画，和图片的动画相互独立
     */
    private MaskAnimator mMaskAnimator;


    public ZoomImageView(Context context) {
        super(context);
        initView();
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        //强制设置图片scaleType为matrix
        super.setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //在绘制前设置变换矩阵
        if (isReady()) {
            Matrix matrix = MathUtils.matrixTake();
            setImageMatrix(getCurrentImageMatrix(matrix));
            MathUtils.matrixGiven(matrix);
        }
        //对图像做遮罩处理
        if (mMask != null) {
            canvas.save();
            canvas.clipRect(mMask);
            super.onDraw(canvas);
            canvas.restore();
        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 重写view中该方法，检查此视图是否可以在某个方向水平滚动
     * 与ViewPager结合的时候使用
     * @param direction                 否定检查滚动左，正检查滚动右
     * @return                          返回true或者false
     */
    @Override
    public boolean canScrollHorizontally(int direction) {
        if (mPinchMode == ZoomConfig.PINCH_MODE_SCALE) {
            return true;
        }
        RectF bound = getImageBound(null);
        if (bound == null) {
            return false;
        }
        if (bound.isEmpty()) {
            return false;
        }
        if (direction > 0) {
            return bound.right > getWidth();
        } else {
            return bound.left < 0;
        }
    }

    /**
     * 重写view中该方法，检查此视图是否可以在某个方向竖直滚动
     * 与ViewPager结合的时候使用
     * @param direction                 否定检查滚动向上，积极检查滚动下降。
     * @return                          返回true或者false
     */
    @Override
    public boolean canScrollVertically(int direction) {
        if (mPinchMode == ZoomConfig.PINCH_MODE_SCALE) {
            return true;
        }
        RectF bound = getImageBound(null);
        if (bound == null) {
            return false;
        }
        if (bound.isEmpty()) {
            return false;
        }
        if (direction > 0) {
            return bound.bottom > getHeight();
        } else {
            return bound.top < 0;
        }
    }

    /**
     * 处理触摸事件的逻辑，更多可以看我的GitHub：https://github.com/yangchong211
     * ACTION_UP                            手指抬起
     * ACTION_CANCEL                        手指结束
     * ACTION_POINTER_UP                    多个手指情况下抬起一个手指,此时需要是缩放模式才触发
     * ACTION_DOWN                          手指抬起
     * ACTION_POINTER_DOWN                  代表用户又使用一个手指触摸到屏幕上，在已有一个触摸点的情况下，有新出现了一个触摸点。
     * ACTION_MOVE                          手指移动
     * @param event                         event
     * @return                              返回true表示自己处理事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        //最后一个点抬起或者取消，结束所有模式
        if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            //如果之前是缩放模式,还需要触发一下缩放结束动画
            if (mPinchMode == ZoomConfig.PINCH_MODE_SCALE) {
                scaleEnd();
            }
            mPinchMode = ZoomConfig.PINCH_MODE_FREE;
        } else if (action == MotionEvent.ACTION_POINTER_UP) {
            //多个手指情况下抬起一个手指,此时需要是缩放模式才触发
            if (mPinchMode == ZoomConfig.PINCH_MODE_SCALE) {
                //抬起的点如果大于2，那么缩放模式还有效，但是有可能初始点变了，重新测量初始点
                if (event.getPointerCount() > 2) {
                    //如果还没结束缩放模式，但是第一个点抬起了，那么让第二个点和第三个点作为缩放控制点
                    if (event.getAction() >> 8 == 0) {
                        saveScaleContext(event.getX(1), event.getY(1), event.getX(2), event.getY(2));
                        //如果还没结束缩放模式，但是第二个点抬起了，那么让第一个点和第三个点作为缩放控制点
                    } else if (event.getAction() >> 8 == 1) {
                        saveScaleContext(event.getX(0), event.getY(0), event.getX(2), event.getY(2));
                    }
                }
                //如果抬起的点等于2,那么此时只剩下一个点,也不允许进入单指模式,因为此时可能图片没有在正确的位置上
            }
            //第一个点按下，开启滚动模式，记录开始滚动的点
        } else if (action == MotionEvent.ACTION_DOWN) {
            //在矩阵动画过程中不允许启动滚动模式
            if (!(mScaleAnimator != null && mScaleAnimator.isRunning())) {
                //停止所有动画
                cancelAllAnimator();
                //切换到滚动模式
                mPinchMode = ZoomConfig.PINCH_MODE_SCROLL;
                //保存触发点用于move计算差值
                mLastMovePoint.set(event.getX(), event.getY());
            }
            //非第一个点按下，关闭滚动模式，开启缩放模式，记录缩放模式的一些初始数据
        } else if (action == MotionEvent.ACTION_POINTER_DOWN) {
            //停止所有动画
            cancelAllAnimator();
            //切换到缩放模式
            mPinchMode = ZoomConfig.PINCH_MODE_SCALE;
            //保存缩放的两个手指
            saveScaleContext(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (!(mScaleAnimator != null && mScaleAnimator.isRunning())) {
                //在滚动模式下移动
                if (mPinchMode == ZoomConfig.PINCH_MODE_SCROLL) {
                    //每次移动产生一个差值累积到图片位置上
                    scrollBy(event.getX() - mLastMovePoint.x, event.getY() - mLastMovePoint.y);
                    //记录新的移动点
                    mLastMovePoint.set(event.getX(), event.getY());
                    //在缩放模式下移动
                } else if (mPinchMode == ZoomConfig.PINCH_MODE_SCALE && event.getPointerCount() > 1) {
                    //两个缩放点间的距离
                    float distance = MathUtils.getDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                    //保存缩放点中点
                    float[] lineCenter = MathUtils.getCenterPoint(event.getX(0), event.getY(0), event.getX(1), event.getY(1));
                    mLastMovePoint.set(lineCenter[0], lineCenter[1]);
                    //处理缩放
                    scale(mScaleCenter, mScaleBase, distance, mLastMovePoint);
                }
            }
        }
        //无论如何都处理各种外部手势
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    /**
     * 常用手势处理
     * 在onTouchEvent末尾被执行.
     */
    private GestureDetector mGestureDetector = new GestureDetector(ZoomImageView.this.getContext(), new GestureDetector.SimpleOnGestureListener() {

        /**
         * Fling处理
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //只有在单指模式结束之后才允许执行fling
            if (mPinchMode == ZoomConfig.PINCH_MODE_FREE && !(mScaleAnimator != null && mScaleAnimator.isRunning())) {
                fling(velocityX, velocityY);
            }
            return true;
        }

        /**
         * 长按事件处理
         */
        @Override
        public void onLongPress(MotionEvent e) {
            //触发长按
            if (mOnLongClickListener != null) {
                mOnLongClickListener.onLongClick(ZoomImageView.this);
            }
        }

        /**
         * 双击事件处理
         */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //当手指快速第二次按下触发,此时必须是单指模式才允许执行doubleTap
            if (mPinchMode == ZoomConfig.PINCH_MODE_SCROLL && !(mScaleAnimator != null && mScaleAnimator.isRunning())) {
                doubleTap(e.getX(), e.getY());
            }
            return true;
        }

        /**
         * 单击事件处理
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //触发点击
            if (mOnClickListener != null) {
                mOnClickListener.onClick(ZoomImageView.this);
            }
            return true;
        }
    });


    /*------------------------------------------------------------------------------------------*/
    /*--------------------常规操作方法【https://github.com/yangchong211】-------------------------*/
    /*------------------------------------------------------------------------------------------*/

    /**
     * 获取外部变换矩阵，外部变换矩阵记录了图片手势操作的最终结果，是相对于图片fit center状态的变换.
     * @param matrix            用于填充结果的对象
     * @return                  如果传了matrix参数则将matrix填充后返回,否则new一个填充返回
     */
    private Matrix getOuterMatrix(Matrix matrix) {
        if (matrix == null) {
            matrix = new Matrix(mOuterMatrix);
        } else {
            matrix.set(mOuterMatrix);
        }
        return matrix;
    }

    /**
     * 获取内部变换矩阵，内部变换矩阵是原图到fit center状态的变换，当原图尺寸变化或者控件大小变化都会发生改变
     * 当尚未布局或者原图不存在时,其值无意义.所以在调用前需要确保前置条件有效,否则将影响计算结果.
     * @param matrix            用于填充结果的对象
     * @return                  如果传了matrix参数则将matrix填充后返回,否则new一个填充返回
     */
    private Matrix getInnerMatrix(Matrix matrix) {
        if (matrix == null) {
            matrix = new Matrix();
        } else {
            matrix.reset();
        }
        if (isReady()) {
            //原图大小
            RectF tempSrc = MathUtils.rectFTake(0, 0, getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight());
            //控件大小
            RectF tempDst = MathUtils.rectFTake(0, 0, getWidth(), getHeight());
            //计算fit center矩阵
            matrix.setRectToRect(tempSrc, tempDst, Matrix.ScaleToFit.CENTER);
            //释放临时对象
            MathUtils.rectFGiven(tempDst);
            MathUtils.rectFGiven(tempSrc);
        }
        return matrix;
    }

    /**
     * 获取图片总变换矩阵.
     * 总变换矩阵为内部变换矩阵x外部变换矩阵,决定了原图到所见最终状态的变换
     * 当尚未布局或者原图不存在时,其值无意义.所以在调用前需要确保前置条件有效,否则将影响计算结果.
     * @param matrix                用于填充结果的对象
     * @return                      如果传了matrix参数则将matrix填充后返回,否则new一个填充返回
     */
    private Matrix getCurrentImageMatrix(Matrix matrix) {
        //获取内部变换矩阵
        matrix = getInnerMatrix(matrix);
        //乘上外部变换矩阵
        matrix.postConcat(mOuterMatrix);
        return matrix;
    }

    /**
     * 获取当前变换后的图片位置和尺寸
     * @param rectF                 用于填充结果的对象
     * @return                      如果传了rectF参数则将rectF填充后返回,否则new一个填充返回
     */
    private RectF getImageBound(RectF rectF) {
        if (rectF == null) {
            rectF = new RectF();
        } else {
            rectF.setEmpty();
        }
        if (!isReady()) {
            return rectF;
        } else {
            //申请一个空matrix
            Matrix matrix = MathUtils.matrixTake();
            //获取当前总变换矩阵
            getCurrentImageMatrix(matrix);
            //对原图矩形进行变换得到当前显示矩形
            rectF.set(0, 0, getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight());
            matrix.mapRect(rectF);
            //释放临时matrix
            MathUtils.matrixGiven(matrix);
            return rectF;
        }
    }

    /**
     * 获取图片最大可放大的比例，如果放大大于这个比例则不被允许
     * 在双手缩放过程中如果图片放大比例大于这个值,手指释放将回弹到这个比例.
     * 在双击放大过程中不允许放大比例大于这个值.
     * 覆盖此方法可以定制不同情况使用不同的最大可放大比例.
     *
     * @return                      缩放比例
     */
    private float getMaxScale() {
        return ZoomConfig.MAX_SCALE;
    }

    /**
     * 计算双击之后图片接下来应该被缩放的比例
     *
     * 如果值大于getMaxScale或者小于fit center尺寸，则实际使用取边界值.
     * 通过覆盖此方法可以定制不同的图片被双击时使用不同的放大策略.
     *
     * @param innerScale            当前内部矩阵的缩放值
     * @param outerScale            当前外部矩阵的缩放值
     * @return                      接下来的缩放比例
     */
    private float calculateNextScale(float innerScale, float outerScale) {
        float currentScale = innerScale * outerScale;
        if (currentScale < getMaxScale()) {
            return getMaxScale();
        } else {
            return innerScale;
        }
    }

    /**
     * 判断当前情况是否能执行手势相关计算
     * 包括:是否有图片,图片是否有尺寸,控件是否有尺寸.
     * @return                  是否能执行手势相关计算
     */
    private boolean isReady() {
        return getDrawable() != null && getDrawable().getIntrinsicWidth() > 0
                && getDrawable().getIntrinsicHeight() > 0 && getWidth() > 0 && getHeight() > 0;
    }


    /**
     * 让图片移动一段距离
     * 不能移动超过可移动范围,超过了就到可移动范围边界为止.
     *
     * @param xDiff 移动距离
     * @param yDiff 移动距离
     * @return 是否改变了位置
     */
    public boolean scrollBy(float xDiff, float yDiff) {
        if (!isReady()) {
            return false;
        }
        //原图方框
        RectF bound = MathUtils.rectFTake();
        getImageBound(bound);
        //控件大小
        float displayWidth = getWidth();
        float displayHeight = getHeight();
        //如果当前图片宽度小于控件宽度，则不能移动
        if (bound.right - bound.left < displayWidth) {
            xDiff = 0;
            //如果图片左边在移动后超出控件左边
        } else if (bound.left + xDiff > 0) {
            //如果在移动之前是没超出的，计算应该移动的距离
            if (bound.left < 0) {
                xDiff = -bound.left;
                //否则无法移动
            } else {
                xDiff = 0;
            }
            //如果图片右边在移动后超出控件右边
        } else if (bound.right + xDiff < displayWidth) {
            //如果在移动之前是没超出的，计算应该移动的距离
            if (bound.right > displayWidth) {
                xDiff = displayWidth - bound.right;
                //否则无法移动
            } else {
                xDiff = 0;
            }
        }
        //以下同理
        if (bound.bottom - bound.top < displayHeight) {
            yDiff = 0;
        } else if (bound.top + yDiff > 0) {
            if (bound.top < 0) {
                yDiff = -bound.top;
            } else {
                yDiff = 0;
            }
        } else if (bound.bottom + yDiff < displayHeight) {
            if (bound.bottom > displayHeight) {
                yDiff = displayHeight - bound.bottom;
            } else {
                yDiff = 0;
            }
        }
        MathUtils.rectFGiven(bound);
        //应用移动变换
        mOuterMatrix.postTranslate(xDiff, yDiff);
        //触发重绘
        invalidate();
        //检查是否有变化
        if (xDiff != 0 || yDiff != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 记录缩放前的一些信息，保存基础缩放值，保存图片缩放中点.
     * @param x1 缩放第一个手指
     * @param y1 缩放第一个手指
     * @param x2 缩放第二个手指
     * @param y2 缩放第二个手指
     */
    private void saveScaleContext(float x1, float y1, float x2, float y2) {
        //记录基础缩放值,其中图片缩放比例按照x方向来计算
        //理论上图片应该是等比的,x和y方向比例相同
        //但是有可能外部设定了不规范的值.
        //但是后续的scale操作会将xy不等的缩放值纠正,改成和x方向相同
        mScaleBase = MathUtils.getMatrixScale(mOuterMatrix)[0] / MathUtils.getDistance(x1, y1, x2, y2);
        //两手指的中点在屏幕上落在了图片的某个点上,图片上的这个点在经过总矩阵变换后和手指中点相同
        //现在我们需要得到图片上这个点在图片是fit center状态下在屏幕上的位置
        //因为后续的计算都是基于图片是fit center状态下进行变换
        //所以需要把两手指中点除以外层变换矩阵得到mScaleCenter
        float[] center = MathUtils.inverseMatrixPoint(MathUtils.getCenterPoint(x1, y1, x2, y2), mOuterMatrix);
        mScaleCenter.set(center[0], center[1]);
    }

    /**
     * 对图片按照一些手势信息进行缩放
     *
     * @param scaleCenter mScaleCenter
     * @param scaleBase mScaleBase
     * @param distance 手指两点之间距离
     * @param lineCenter 手指两点之间中点
     *
     * @see #mScaleCenter
     * @see #mScaleBase
     */
    private void scale(PointF scaleCenter, float scaleBase, float distance, PointF lineCenter) {
        if (!isReady()) {
            return;
        }
        //计算图片从fit center状态到目标状态的缩放比例
        float scale = scaleBase * distance;
        Matrix matrix = MathUtils.matrixTake();
        //按照图片缩放中心缩放，并且让缩放中心在缩放点中点上
        matrix.postScale(scale, scale, scaleCenter.x, scaleCenter.y);
        //让图片的缩放中点跟随手指缩放中点
        matrix.postTranslate(lineCenter.x - scaleCenter.x, lineCenter.y - scaleCenter.y);
        //应用变换
        mOuterMatrix.set(matrix);
        MathUtils.matrixGiven(matrix);
        //重绘
        invalidate();
    }

    /**
     * 双击后放大或者缩小
     *
     * 将图片缩放比例缩放到nextScale指定的值.
     * 但nextScale值不能大于最大缩放值不能小于fit center情况下的缩放值.
     * 将双击的点尽量移动到控件中心.
     *
     * @param x 双击的点
     * @param y 双击的点
     */
    private void doubleTap(float x, float y) {
        if (!isReady()) {
            return;
        }
        //获取第一层变换矩阵
        Matrix innerMatrix = MathUtils.matrixTake();
        getInnerMatrix(innerMatrix);
        //当前总的缩放比例
        float innerScale = MathUtils.getMatrixScale(innerMatrix)[0];
        float outerScale = MathUtils.getMatrixScale(mOuterMatrix)[0];
        float currentScale = innerScale * outerScale;
        //控件大小
        float displayWidth = getWidth();
        float displayHeight = getHeight();
        //最大放大大小
        float maxScale = getMaxScale();
        //接下来要放大的大小
        float nextScale = calculateNextScale(innerScale, outerScale);
        //如果接下来放大大于最大值或者小于fit center值，则取边界
        if (nextScale > maxScale) {
            nextScale = maxScale;
        }
        if (nextScale < innerScale) {
            nextScale = innerScale;
        }
        //开始计算缩放动画的结果矩阵
        Matrix animEnd = MathUtils.matrixTake(mOuterMatrix);
        //计算还需缩放的倍数
        animEnd.postScale(nextScale / currentScale, nextScale / currentScale, x, y);
        //将放大点移动到控件中心
        animEnd.postTranslate(displayWidth / 2f - x, displayHeight / 2f - y);
        //得到放大之后的图片方框
        Matrix testMatrix = MathUtils.matrixTake(innerMatrix);
        testMatrix.postConcat(animEnd);
        RectF testBound = MathUtils.rectFTake(0, 0, getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight());
        testMatrix.mapRect(testBound);
        //修正位置
        float postX = 0;
        float postY = 0;
        if (testBound.right - testBound.left < displayWidth) {
            postX = displayWidth / 2f - (testBound.right + testBound.left) / 2f;
        } else if (testBound.left > 0) {
            postX = -testBound.left;
        } else if (testBound.right < displayWidth) {
            postX = displayWidth - testBound.right;
        }
        if (testBound.bottom - testBound.top < displayHeight) {
            postY = displayHeight / 2f - (testBound.bottom + testBound.top) / 2f;
        } else if (testBound.top > 0) {
            postY = -testBound.top;
        } else if (testBound.bottom < displayHeight) {
            postY = displayHeight - testBound.bottom;
        }
        //应用修正位置
        animEnd.postTranslate(postX, postY);
        //清理当前可能正在执行的动画
        cancelAllAnimator();
        //启动矩阵动画
        mScaleAnimator = new ScaleAnimator(mOuterMatrix, animEnd,this);
        mScaleAnimator.start();
        //清理临时变量
        MathUtils.rectFGiven(testBound);
        MathUtils.matrixGiven(testMatrix);
        MathUtils.matrixGiven(animEnd);
        MathUtils.matrixGiven(innerMatrix);
    }

    /**
     * 当缩放操作结束动画
     * 如果图片超过边界，找到最近的位置动画恢复.
     * 如果图片缩放尺寸超过最大值或者最小值，找到最近的值动画恢复.
     */
    private void scaleEnd() {
        if (!isReady()) {
            return;
        }
        //是否修正了位置
        boolean change = false;
        //获取图片整体的变换矩阵
        Matrix currentMatrix = MathUtils.matrixTake();
        getCurrentImageMatrix(currentMatrix);
        //整体缩放比例
        float currentScale = MathUtils.getMatrixScale(currentMatrix)[0];
        //第二层缩放比例
        float outerScale = MathUtils.getMatrixScale(mOuterMatrix)[0];
        //控件大小
        float displayWidth = getWidth();
        float displayHeight = getHeight();
        //最大缩放比例
        float maxScale = getMaxScale();
        //比例修正
        float scalePost = 1f;
        //位置修正
        float postX = 0;
        float postY = 0;
        //如果整体缩放比例大于最大比例，进行缩放修正
        if (currentScale > maxScale) {
            scalePost = maxScale / currentScale;
        }
        //如果缩放修正后整体导致第二层缩放小于1（就是图片比fit center状态还小），重新修正缩放
        if (outerScale * scalePost < 1f) {
            scalePost = 1f / outerScale;
        }
        //如果缩放修正不为1，说明进行了修正
        if (scalePost != 1f) {
            change = true;
        }
        //尝试根据缩放点进行缩放修正
        Matrix testMatrix = MathUtils.matrixTake(currentMatrix);
        testMatrix.postScale(scalePost, scalePost, mLastMovePoint.x, mLastMovePoint.y);
        RectF testBound = MathUtils.rectFTake(0, 0, getDrawable().getIntrinsicWidth(), getDrawable().getIntrinsicHeight());
        //获取缩放修正后的图片方框
        testMatrix.mapRect(testBound);
        //检测缩放修正后位置有无超出，如果超出进行位置修正
        if (testBound.right - testBound.left < displayWidth) {
            postX = displayWidth / 2f - (testBound.right + testBound.left) / 2f;
        } else if (testBound.left > 0) {
            postX = -testBound.left;
        } else if (testBound.right < displayWidth) {
            postX = displayWidth - testBound.right;
        }
        if (testBound.bottom - testBound.top < displayHeight) {
            postY = displayHeight / 2f - (testBound.bottom + testBound.top) / 2f;
        } else if (testBound.top > 0) {
            postY = -testBound.top;
        } else if (testBound.bottom < displayHeight) {
            postY = displayHeight - testBound.bottom;
        }
        //如果位置修正不为0，说明进行了修正
        if (postX != 0 || postY != 0) {
            change = true;
        }
        //只有有执行修正才执行动画
        if (change) {
            //计算结束矩阵
            Matrix animEnd = MathUtils.matrixTake(mOuterMatrix);
            animEnd.postScale(scalePost, scalePost, mLastMovePoint.x, mLastMovePoint.y);
            animEnd.postTranslate(postX, postY);
            //清理当前可能正在执行的动画
            cancelAllAnimator();
            //启动矩阵动画
            mScaleAnimator = new ScaleAnimator(mOuterMatrix, animEnd,this);
            mScaleAnimator.start();
            //清理临时变量
            MathUtils.matrixGiven(animEnd);
        }
        //清理临时变量
        MathUtils.rectFGiven(testBound);
        MathUtils.matrixGiven(testMatrix);
        MathUtils.matrixGiven(currentMatrix);
    }

    /**
     * 执行惯性动画，动画在遇到不能移动就停止，动画速度衰减到很小就停止.
     * 其中参数速度单位为 像素/秒
     * @param vx            x方向速度
     * @param vy            y方向速度
     */
    private void fling(float vx, float vy) {
        if (!isReady()) {
            return;
        }
        //清理当前可能正在执行的动画
        cancelAllAnimator();
        //创建惯性动画
        //FlingAnimator单位为 像素/帧,一秒60帧
        mFlingAnimator = new FlingAnimator(vx / 60f, vy / 60f , this);
        mFlingAnimator.start();
    }

    /**
     * 停止所有手势动画
     */
    private void cancelAllAnimator() {
        if (mScaleAnimator != null) {
            mScaleAnimator.cancel();
            mScaleAnimator = null;
        }
        if (mFlingAnimator != null) {
            mFlingAnimator.cancel();
            mFlingAnimator = null;
        }
    }

    /*------------------------------------------------------------------------------------------*/
    /*--------------------开发者设置调用方法【https://github.com/yangchong211】--------------------*/
    /*------------------------------------------------------------------------------------------*/
    /*------------------------------下面方法开发者可以自己设置-------------------------------------*/

    /**
     * 设置图片的点击事件和长按事件
     */
    private OnZoomClickListener mOnClickListener;
    public void setOnZoomClickListener(OnZoomClickListener listener) {
        mOnClickListener = listener;
    }

    private OnZoomLongClickListener mOnLongClickListener;
    public void setOnZoomLongClickListener(OnZoomLongClickListener listener) {
        mOnLongClickListener = listener;
    }

    /**
     * 执行当前outerMatrix到指定outerMatrix渐变的动画
     * 调用此方法会停止正在进行中的手势以及手势动画.
     * 当duration为0时,outerMatrix值会被立即设置而不会启动动画.
     * @param endMatrix             动画目标矩阵
     * @param duration              动画持续时间
     */
    public void outerMatrixTo(Matrix endMatrix, long duration) {
        if (endMatrix == null) {
            return;
        }
        //将手势设置为PINCH_MODE_FREE将停止后续手势的触发
        mPinchMode = ZoomConfig.PINCH_MODE_FREE;
        //停止所有正在进行的动画
        cancelAllAnimator();
        //如果时间不合法立即执行结果
        if (duration <= 0) {
            mOuterMatrix.set(endMatrix);
            invalidate();
        } else {
            //创建矩阵变化动画
            mScaleAnimator = new ScaleAnimator(mOuterMatrix, endMatrix, duration,this);
            mScaleAnimator.start();
        }
    }

    /**
     * 执行当前mask到指定mask的变化动画
     * 调用此方法不会停止手势以及手势相关动画,但会停止正在进行的mask动画.
     * @param mask              动画目标mask
     * @param duration          动画持续时间
     */
    public void zoomMaskTo(RectF mask, long duration) {
        if (mask == null) {
            return;
        }
        //停止mask动画
        if (mMaskAnimator != null) {
            mMaskAnimator.cancel();
            mMaskAnimator = null;
        }
        //如果duration为0或者之前没有设置过mask,不执行动画,立即设置
        if (duration <= 0 || mMask == null) {
            if (mMask == null) {
                mMask = new RectF();
            }
            mMask.set(mask);
            invalidate();
        } else {
            //执行mask动画
            mMaskAnimator = new MaskAnimator(mMask, mask, duration,this);
            mMaskAnimator.start();
        }
    }

    /**
     * 获取当前手势状态
     */
    public int getImageMode() {
        return mPinchMode;
    }

    /**
     * 设置最大缩放的倍数，建议是两倍。
     * @param maxScale                  int
     */
    public void setMaxScale(int maxScale){
        if (maxScale<0 || maxScale>10){
            ZoomConfig.MAX_SCALE = 2;
        }else {
            ZoomConfig.MAX_SCALE = maxScale;
        }
    }

    public void setBitmap(Bitmap bitmap){
        Drawable drawable = new BitmapDrawable(bitmap);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        }
    }

    /**
     * 优化相关，在界面销毁的时候可以调用，比如在Activity的onDestroy方法调用。重置所有状态
     * 重置位置到fit center状态，清空mask，停止所有手势，停止所有动画，但不清空drawable，以及事件绑定相关数据
     */
    public void reset() {
        //重置位置到fit
        if (mOuterMatrix!=null){
            mOuterMatrix.reset();
        }
        //清空mask
        mMask = null;
        //停止所有手势
        mPinchMode = ZoomConfig.PINCH_MODE_FREE;
        mLastMovePoint.set(0, 0);
        mScaleCenter.set(0, 0);
        mScaleBase = 0;
        //停止所有动画
        if (mMaskAnimator != null) {
            mMaskAnimator.cancel();
            mMaskAnimator = null;
        }
        cancelAllAnimator();
        //重绘
        invalidate();
    }


}