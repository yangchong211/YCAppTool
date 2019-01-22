package com.ycbjie.book.mzGame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.blankj.utilcode.util.LogUtils;
import com.ycbjie.book.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2016/12/7
 *     desc  : 2048的游戏面板，加入布局文件即可开始游戏
 *     revise: 
 * </pre>
 */
public class GamePinTuLayout extends RelativeLayout implements View.OnClickListener{
    /**
     * 设置Item的数量n*n；默认为3
     */
    private int mColumn = 3;
    /**
     * 布局的宽度
     */
    private int mWidth;
    /**
     * 布局的padding
     */
    private int mPadding;
    /**
     * 存放所有的Item
     */
    private ImageView[] mGamePinTuItems;
    /**
     * Item的宽度
     */
    private int mItemWidth;
    /**
     * Item横向与纵向的边距
     */
    private int mMargin = 3;
    /**
     * 拼图的图片
     */
    private Bitmap mBitmap;
    /**
     * 存放切完以后的图片bean
     */
    public List<ImagePiece> mItemBitmaps ;

    /**
     * 初始化界面的标记
     */
    private boolean once;

    public GamePinTuLayout(Context context) {
        this(context, null);
    }
    public GamePinTuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造函数，用来初始化
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     * @author https://github.com/yangchong211
     */
    public GamePinTuLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //把设置的margin值转换为dp
        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mMargin, getResources().getDisplayMetrics());
        // 设置Layout的内边距，四边一致，设置为四内边距中的最小值
        mPadding = min(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    /**
     * 用来设置设置自定义的View的宽高，
     * @param widthMeasureSpec  the width measure spec
     * @param heightMeasureSpec the height measure spec
     * @author https://github.com/yangchong211
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获得游戏布局的边长
        mWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());
        if (!once) {
            initBitmap();
            initItem();
        }
        once = true;
        setMeasuredDimension(mWidth, mWidth);
    }

    /**
     * 初始化bitmap
     * @author https://github.com/yangchong211
     */
    @SuppressWarnings("ComparatorMethodParameterNotUsed")
    private void initBitmap() {
        if (mBitmap == null){
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.girl10);
        }
        mItemBitmaps = split(mBitmap, mColumn);
        //对图片进行排序
        Collections.sort(mItemBitmaps, (lhs, rhs) -> {
            //我们使用random随机比较大小
            return Math.random() > 0.5 ? 1 : -1;
        });
    }

    /**
     * 将图片切成 piece * piece 块
     */
    private List<ImagePiece> split(Bitmap bitmap, int piece){
        List<ImagePiece> pieces = new ArrayList<>(piece * piece);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        LogUtils.e("TAG", "bitmap Width = " + width + " , height = " + height);
        int pieceWidth = Math.min(width, height) / piece;
        for (int i = 0; i < piece; i++){
            for (int j = 0; j < piece; j++){
                ImagePiece imagePiece = new ImagePiece();
                imagePiece.index = j + i * piece;
                int xValue = j * pieceWidth;
                int yValue = i * pieceWidth;
                imagePiece.bitmap = Bitmap.createBitmap(bitmap, xValue, yValue, pieceWidth, pieceWidth);
                pieces.add(imagePiece);
            }
        }
        return pieces;
    }


    /**
     * 初始化每一个item
     * @author https://github.com/yangchong211
     */
    private void initItem() {
        // 获得Item的宽度
        int childWidth = (mWidth - mPadding * 2 - mMargin * (mColumn - 1)) / mColumn;
        mItemWidth = childWidth;

        mGamePinTuItems = new ImageView[mColumn * mColumn];
        // 放置Item
        for (int i = 0; i < mGamePinTuItems.length; i++) {
            ImageView item = new ImageView(getContext());

            item.setOnClickListener(this);

            item.setImageBitmap(mItemBitmaps.get(i).bitmap);
            mGamePinTuItems[i] = item;
            item.setId(i + 1);
            item.setTag(i + "_" + mItemBitmaps.get(i).index);

            RelativeLayout.LayoutParams lp = new LayoutParams(mItemWidth, mItemWidth);
            // 设置横向边距,不是最后一列
            if ((i + 1) % mColumn != 0) {
                lp.rightMargin = mMargin;
            }
            // 如果不是第一列
            if (i % mColumn != 0) {
                lp.addRule(RelativeLayout.RIGHT_OF, mGamePinTuItems[i - 1].getId());
            }
            // 如果不是第一行，//设置纵向边距，非最后一行
            if ((i + 1) > mColumn) {
                lp.topMargin = mMargin;
                lp.addRule(RelativeLayout.BELOW, mGamePinTuItems[i - mColumn].getId());
            }
            addView(item, lp);
        }
    }
    /**
     * 用来得到最小值
     * @param params the params
     * @return the int
     * @author https://github.com/yangchong211
     */
    private int min(int ... params) {
        int minValue = params[0];
        for(int param : params){
            if(minValue>param){
                minValue=param;
            }
        }
        return minValue;
    }

    /**
     * 记录第一次点击的ImageView
     */
    private ImageView mFirst;
    /**
     * 记录第二次点击的ImageView
     */
    private ImageView mSecond;
    /**
     * 点击事件
     * @param view the view
     * @author https://github.com/yangchong211
     */
    @Override
    public void onClick(View view) {
        LogUtils.d("TAG", "onClick: "+view.getTag());
        // 如果正在执行动画，则屏蔽
        if(isAniming)
            return;
        //如果两次点击的是同一个View
        if(mFirst == view){
            mFirst.setColorFilter(null);
            mFirst = null;
            return;
        }
        //点击第一个View
        if(mFirst==null){
            mFirst= (ImageView) view;
            mFirst.setColorFilter(Color.parseColor("#55FF0000"));
        }else{//点击第二个View
            mSecond= (ImageView) view;
            exchangView();
        }
    }

    /**
     * 动画运行的标志位
     */
    private boolean isAniming;
    /**
     * 动画层
     */
    private RelativeLayout mAnimLayout;
    /**
     * 交换两个Item图片
     * @author https://github.com/yangchong211
     */
    private void exchangView(){
        mFirst.setColorFilter(null);
        setUpAnimLayout();
        // 添加FirstView
        ImageView first = new ImageView(getContext());
        first.setImageBitmap(mItemBitmaps
                .get(getImageIndexByTag((String) mFirst.getTag())).bitmap);
        LayoutParams lp = new LayoutParams(mItemWidth, mItemWidth);
        lp.leftMargin = mFirst.getLeft() - mPadding;
        lp.topMargin = mFirst.getTop() - mPadding;
        first.setLayoutParams(lp);
        mAnimLayout.addView(first);
        // 添加SecondView
        ImageView second = new ImageView(getContext());
        second.setImageBitmap(mItemBitmaps
                .get(getImageIndexByTag((String) mSecond.getTag())).bitmap);
        LayoutParams lp2 = new LayoutParams(mItemWidth, mItemWidth);
        lp2.leftMargin = mSecond.getLeft() - mPadding;
        lp2.topMargin = mSecond.getTop() - mPadding;
        second.setLayoutParams(lp2);
        mAnimLayout.addView(second);

        // 设置动画
        TranslateAnimation anim = new TranslateAnimation(0, mSecond.getLeft()
                - mFirst.getLeft(), 0, mSecond.getTop() - mFirst.getTop());
        anim.setDuration(300);
        anim.setFillAfter(true);
        first.startAnimation(anim);

        TranslateAnimation animSecond = new TranslateAnimation(0,
                mFirst.getLeft() - mSecond.getLeft(), 0, mFirst.getTop()
                - mSecond.getTop());
        animSecond.setDuration(300);
        animSecond.setFillAfter(true);
        second.startAnimation(animSecond);
        // 添加动画监听
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isAniming = true;
                mFirst.setVisibility(INVISIBLE);
                mSecond.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String firstTag = (String) mFirst.getTag();
                String secondTag = (String) mSecond.getTag();

                String[] firstParams = firstTag.split("_");
                String[] secondParams = secondTag.split("_");

                mFirst.setImageBitmap(mItemBitmaps.get(Integer
                        .parseInt(secondParams[0])).bitmap);
                mSecond.setImageBitmap(mItemBitmaps.get(Integer
                        .parseInt(firstParams[0])).bitmap);

                mFirst.setTag(secondTag);
                mSecond.setTag(firstTag);
                mFirst.setVisibility(VISIBLE);
                mSecond.setVisibility(VISIBLE);
                mFirst = mSecond = null;
                mAnimLayout.removeAllViews();
                //checkSuccess();
                isAniming = false;
                //进行游戏胜利判断
                checkSuccess();
            }
        });
    }

    /**
     * 用来判断游戏是否成功
     * @author https://github.com/yangchong211
     */
    private void checkSuccess() {
        boolean isSuccess = true;
        for (int i = 0; i < mGamePinTuItems.length; i++) {
            ImageView first = mGamePinTuItems[i];
            LogUtils.e("TAG", getIndexByTag((String) first.getTag()) + "");
            if (getIndexByTag((String) first.getTag()) != i) {
                isSuccess = false;
            }
        }
        if (isSuccess) {
            Toast.makeText(getContext(), "Success , Level Up !",
                    Toast.LENGTH_LONG).show();
            nextLevel();
        }
    }

    /**
     * 进入下一关
     * @author https://github.com/yangchong211
     */
    private void nextLevel() {
        this.removeAllViews();
        mAnimLayout = null;
        mColumn++;
        initBitmap();
        initItem();
    }

    /**
     * 创建动画层
     */
    private void setUpAnimLayout(){
        if(mAnimLayout==null){
            mAnimLayout = new RelativeLayout(getContext());
            addView(mAnimLayout);
        }
    }

    /**
     * 获得存储在mItemBitmaps中存储图片的角标
     * @param tag the tag
     * @return the image index by tag
     */
    private int getImageIndexByTag(String tag) {
        String[] split = tag.split("_");
        return Integer.parseInt(split[0]);
    }

    /**
     * 获得图片的真正索引
     * @param tag the tag
     * @return the index by tag
     */
    private int getIndexByTag(String tag) {
        String[] split = tag.split("_");
        return Integer.parseInt(split[1]);
    }
}