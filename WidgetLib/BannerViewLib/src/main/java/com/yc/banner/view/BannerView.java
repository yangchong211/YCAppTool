package com.yc.banner.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;


import com.yc.banner.listener.OnBannerListener;
import com.yc.banner.loader.ImageLoaderInterface;
import com.yc.banner.util.BannerConstant;
import com.yc.apphandlerlib.WeakHandler;
import com.yc.bannerwidget.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/6/20
 *     desc  : 自定义轮播图
 *     revise:
 * </pre>
 */
public class BannerView extends FrameLayout implements ViewPager.OnPageChangeListener {

    public static final String tag = "banner_view";
    private int mIndicatorMargin = BannerConstant.PADDING_SIZE;
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int mIndicatorSelectedWidth;
    private int mIndicatorSelectedHeight;
    private int indicatorSize;
    private int bannerStyle = BannerConstant.CIRCLE_INDICATOR;
    private int delayTime = BannerConstant.TIME;
    private boolean isAutoPlay = BannerConstant.IS_AUTO_PLAY;
    private boolean isScroll = BannerConstant.IS_SCROLL;
    private int mIndicatorSelectedResId = R.drawable.gray_radius;
    private int mIndicatorUnselectedResId = R.drawable.white_radius;
    private int mLayoutResId;
    private int titleHeight;
    private int titleBackground;
    private int titleTextColor;
    private int titleTextSize;
    private int startIndex;
    private int count = 0;
    private int currentItem;
    private int gravity = -1;
    private int lastPosition = 1;
    private List<String> titles;
    private List imageUrls;
    private List<View> imageViews;
    private List<ImageView> indicatorImages;
    private Context context;
    private BannerViewPager viewPager;
    private TextView bannerTitle, numIndicatorInside, numIndicator;
    private LinearLayout indicator, indicatorInside, titleView;
    private ImageLoaderInterface imageLoader;
    private BannerPagerAdapter adapter;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private OnBannerListener listener;
    private long mRecentTouchTime;
    /**
     * 是否轮播，默认是轮播的状态，设置成false则为静态轮播图
     */
    private boolean isBannnerPlay = true;

    public void setBannerPlay(boolean isPlay){
        this.isBannnerPlay = isPlay;
    }

    private WeakHandler handler = new WeakHandler();

    /**
     * 轮播图图在异常状态下，需要保存一些比较重要的信息
     * @return
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        super.onSaveInstanceState();
        final Bundle bundle = new Bundle();
        bundle.putBoolean("isAutoPlay", isAutoPlay);
        bundle.putInt("delay", delayTime);
        bundle.putInt("gravity", gravity);
        return bundle;
    }

    /**
     * 重启的时候，再取出一些重要的信息
     * @param state                             state
     */
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;
            isAutoPlay = bundle.getBoolean("isAutoPlay",BannerConstant.IS_AUTO_PLAY);
            delayTime = bundle.getInt("delay",3000);
            gravity = bundle.getInt("gravity",gravity);
            return;
        }
        super.onRestoreInstanceState(state);
    }


    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        titles = new ArrayList<>();
        imageUrls = new ArrayList<>();
        imageViews = new ArrayList<>();
        indicatorImages = new ArrayList<>();
        indicatorSize = 14;
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        imageViews.clear();
        handleTypedArray(context, attrs);
        initBannerView();
    }

    /**
     * 初始化操作
     */
    private void initBannerView() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.banner_view, this, true);
        viewPager = view.findViewById(R.id.bannerViewPager);
        titleView = view.findViewById(R.id.titleView);
        indicator = view.findViewById(R.id.circleIndicator);
        indicatorInside = view.findViewById(R.id.indicatorInside);
        bannerTitle = view.findViewById(R.id.bannerTitle);
        numIndicator = view.findViewById(R.id.numIndicator);
        numIndicatorInside = view.findViewById(R.id.numIndicatorInside);
    }

    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        //attr属性的读取。如何读取？
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        mIndicatorWidth = typedArray.getDimensionPixelSize(
                R.styleable.BannerView_indicator_width, indicatorSize);
        mIndicatorHeight = typedArray.getDimensionPixelSize(
                R.styleable.BannerView_indicator_height, indicatorSize);
        mIndicatorSelectedWidth = typedArray.getDimensionPixelSize(
                R.styleable.BannerView_indicator_selected_width, indicatorSize);
        mIndicatorSelectedHeight = typedArray.getDimensionPixelSize(
                R.styleable.BannerView_indicator_selected_height, indicatorSize);
        mIndicatorMargin = typedArray.getDimensionPixelSize(
                R.styleable.BannerView_indicator_margin, BannerConstant.PADDING_SIZE);
        mIndicatorSelectedResId = typedArray.getResourceId(
                R.styleable.BannerView_indicator_drawable_selected, R.drawable.gray_radius);
        mIndicatorUnselectedResId = typedArray.getResourceId(
                R.styleable.BannerView_indicator_drawable_unselected, R.drawable.white_radius);
        delayTime = typedArray.getInt(R.styleable.BannerView_delay_time, BannerConstant.TIME);
        isAutoPlay = typedArray.getBoolean(R.styleable.BannerView_is_auto_play,
                BannerConstant.IS_AUTO_PLAY);
        titleBackground = typedArray.getColor(R.styleable.BannerView_title_background,
                BannerConstant.TITLE_BACKGROUND);
        titleHeight = typedArray.getDimensionPixelSize(R.styleable.BannerView_title_height,
                BannerConstant.TITLE_HEIGHT);
        titleTextColor = typedArray.getColor(R.styleable.BannerView_title_textcolor,
                BannerConstant.TITLE_TEXT_COLOR);
        titleTextSize = typedArray.getDimensionPixelSize(R.styleable.BannerView_title_textsize,
                BannerConstant.TITLE_TEXT_SIZE);
        mLayoutResId = typedArray.getResourceId(R.styleable.BannerView_banner_layout, mLayoutResId);
        typedArray.recycle();
    }

    /**
     * 设置viewPager滑动动画持续时间
     * API>19
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void setAnimationDuration(final int during){
        try {
            // viewPager平移动画事件
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            // 动画效果与ViewPager的一致
            Interpolator interpolator = t -> {
                t -= 1.0f;
                return t * t * t * t * t + 1.0f;
            };
            Scroller mScroller = new Scroller(getContext(),interpolator){
                @Override
                public void startScroll(int startX, int startY, int dx, int dy, int duration) {
                    // 如果手工滚动,则加速滚动
                    if (System.currentTimeMillis() - mRecentTouchTime > delayTime) {
                        duration = during;
                    } else {
                        duration /= 2;
                    }
                    super.startScroll(startX, startY, dx, dy, duration);
                }

                @Override
                public void startScroll(int startX, int startY, int dx, int dy) {
                    super.startScroll(startX, startY, dx, dy,during);
                }
            };
            mField.set(this, mScroller);
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    public BannerView isAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        return this;
    }

    public BannerView setImageLoader(ImageLoaderInterface imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    public BannerView setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    public BannerView setIndicatorGravity(int type) {
        switch (type) {
            case BannerConstant.LEFT:
                this.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                break;
            case BannerConstant.CENTER:
                this.gravity = Gravity.CENTER;
                break;
            case BannerConstant.RIGHT:
                this.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
            default:
                break;
        }
        return this;
    }

    public BannerView setBannerAnimation(Class<? extends ViewPager.PageTransformer> transformer) {
        try {
            viewPager.setPageTransformer(true, transformer.newInstance());
        } catch (Exception e) {
            Log.e(tag, "Please set the PageTransformer class");
        }
        return this;
    }

    public BannerView setOffscreenPageLimit(int limit) {
        if (viewPager != null) {
            viewPager.setOffscreenPageLimit(limit);
        }
        return this;
    }

    public BannerView setBannerTitles(List<String> titles) {
        this.titles = titles;
        return this;
    }

    public BannerView setBannerStyle(int bannerStyle) {
        this.bannerStyle = bannerStyle;
        return this;
    }

    public BannerView setViewPagerIsScroll(boolean isScroll) {
        this.isScroll = isScroll;
        return this;
    }

    public BannerView setImages(List<?> imageUrls) {
        this.imageUrls.addAll(imageUrls);
        this.count = imageUrls.size();
        return this;
    }

    public void update(List<?> imageUrls, List<String> titles) {
        this.titles.clear();
        this.titles.addAll(titles);
        update(imageUrls);
    }

    public void update(List<?> imageUrls) {
        this.imageUrls.clear();
        this.imageViews.clear();
        this.indicatorImages.clear();
        this.imageUrls.addAll(imageUrls);
        this.count = this.imageUrls.size();
        start();
    }

    public void updateBannerStyle(int bannerStyle) {
        indicator.setVisibility(GONE);
        numIndicator.setVisibility(GONE);
        numIndicatorInside.setVisibility(GONE);
        indicatorInside.setVisibility(GONE);
        bannerTitle.setVisibility(View.GONE);
        titleView.setVisibility(View.GONE);
        this.bannerStyle = bannerStyle;
        start();
    }

    public BannerView start() {
        setBannerStyleUI();
        setImageList(imageUrls);
        setData();
        return this;
    }

    private void setTitleStyleUI() {
        if (titles.size() != imageUrls.size()) {
            throw new RuntimeException("The number of titles and images is different");
        }
        if (titleBackground != -1) {
            titleView.setBackgroundColor(titleBackground);
        }
        if (titleHeight != -1) {
            titleView.setLayoutParams(new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, titleHeight));
        }
        if (titleTextColor != -1) {
            bannerTitle.setTextColor(titleTextColor);
        }
        if (titleTextSize != -1) {
            bannerTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        }
        if (titles != null && titles.size() > 0) {
            bannerTitle.setText(titles.get(0));
            bannerTitle.setVisibility(View.VISIBLE);
            titleView.setVisibility(View.VISIBLE);
        }
    }

    private void setBannerStyleUI() {
        int visibility = count > 1 ? View.VISIBLE : View.GONE;
        switch (bannerStyle) {
            case BannerConstant.CIRCLE_INDICATOR:
                indicator.setVisibility(visibility);
                break;
            case BannerConstant.NUM_INDICATOR:
                numIndicator.setVisibility(visibility);
                break;
            case BannerConstant.NUM_INDICATOR_TITLE:
                numIndicatorInside.setVisibility(visibility);
                setTitleStyleUI();
                break;
            case BannerConstant.CIRCLE_INDICATOR_TITLE:
                indicator.setVisibility(visibility);
                setTitleStyleUI();
                break;
            case BannerConstant.CIRCLE_INDICATOR_TITLE_INSIDE:
                indicatorInside.setVisibility(visibility);
                setTitleStyleUI();
                break;
            default:
                break;
        }
    }

    private void initImages() {
        imageViews.clear();
        if (bannerStyle == BannerConstant.CIRCLE_INDICATOR ||
                bannerStyle == BannerConstant.CIRCLE_INDICATOR_TITLE ||
                bannerStyle == BannerConstant.CIRCLE_INDICATOR_TITLE_INSIDE) {
            createIndicator();
        } else if (bannerStyle == BannerConstant.NUM_INDICATOR_TITLE) {
            numIndicatorInside.setText("1/" + count);
        } else if (bannerStyle == BannerConstant.NUM_INDICATOR) {
            numIndicator.setText("1/" + count);
        }
    }

    private void setImageList(List<?> imagesUrl) {
        if (imagesUrl == null || imagesUrl.size() <= 0) {
            Log.e(tag, "The image data set is empty.");
            return;
        }
        initImages();
        for (int i = 0; i <= count + 1; i++) {
            View imageView = null;
            if (imageLoader != null) {
                imageView = imageLoader.createImageView(context);
            }
            if (imageView == null) {
                imageView = new ImageView(context);
            }
            Object url;
            if (i == 0) {
                url = imagesUrl.get(count - 1);
            } else if (i == count + 1) {
                url = imagesUrl.get(0);
            } else {
                url = imagesUrl.get(i - 1);
            }
            imageViews.add(imageView);
            if (imageLoader != null) {
                imageLoader.displayImage(context, url, imageView);
            } else {
                Log.e(tag, "Please set images loader.");
            }
        }
    }

    private void createIndicator() {
        indicatorImages.clear();
        indicator.removeAllViews();
        indicatorInside.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params;
            if (i == 0) {
                params = new LinearLayout.LayoutParams(mIndicatorSelectedWidth, mIndicatorSelectedHeight);
                imageView.setImageResource(mIndicatorSelectedResId);
            } else {
                params = new LinearLayout.LayoutParams(mIndicatorWidth, mIndicatorHeight);
                imageView.setImageResource(mIndicatorUnselectedResId);
            }
            params.leftMargin = mIndicatorMargin;
            params.rightMargin = mIndicatorMargin;
            indicatorImages.add(imageView);
            if (bannerStyle == BannerConstant.CIRCLE_INDICATOR ||
                    bannerStyle == BannerConstant.CIRCLE_INDICATOR_TITLE) {
                indicator.addView(imageView, params);
            } else if (bannerStyle == BannerConstant.CIRCLE_INDICATOR_TITLE_INSIDE) {
                indicatorInside.addView(imageView, params);
            }
        }
    }


    private void setData() {
        if (startIndex != 0) {
            currentItem = startIndex;
        } else {
            currentItem = 1;
        }
        if (adapter == null) {
            adapter = new BannerPagerAdapter();
            viewPager.addOnPageChangeListener(this);
            viewPager.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
        viewPager.setFocusable(true);
        viewPager.setCurrentItem(currentItem);
        if (gravity != -1) {
            indicator.setGravity(gravity);
        }
        if (isScroll && count > 1) {
            viewPager.setScrollable(true);
        } else {
            viewPager.setScrollable(false);
        }
        if (isAutoPlay) {
            startAutoPlay();
        }
    }

    public void startAutoPlay() {
        if (isBannnerPlay){
            handler.removeCallbacks(task);
            handler.postDelayed(task, delayTime);
        }
    }

    public void stopAutoPlay() {
        if (isBannnerPlay){
            handler.removeCallbacks(task);
        }
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (count > 1 && isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1;
                if (currentItem == 1) {
                    viewPager.setCurrentItem(currentItem, false);
                    handler.post(task);
                } else {
                    viewPager.setCurrentItem(currentItem);
                    handler.postDelayed(task, delayTime);
                }
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mRecentTouchTime = System.currentTimeMillis();
        if (isAutoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                //手指抬起，结束，滑动到外面，开始轮播图
                startAutoPlay();
            } else if (action == MotionEvent.ACTION_DOWN) {
                //手指触摸的时候，停止轮播图
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 返回真实的位置
     *
     * @param position
     * @return 下标从0开始
     */
    public int toRealPosition(int position) {
        int realPosition = 0;
        if(count!=0){
            realPosition = (position - 1) % count;
        }
        if (realPosition < 0) {
            realPosition += count;
        }
        return realPosition;
    }

    class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(@Nullable View view, @Nullable Object object) {
            return view == object;
        }

        @Nullable
        @Override
        public Object instantiateItem(@Nullable ViewGroup container, final int position) {
            container.addView(imageViews.get(position));
            View view = imageViews.get(position);
            if (listener != null) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.OnBannerClick(toRealPosition(position));
                    }
                });
            }
            return view;
        }

        @Override
        public void destroyItem(@Nullable ViewGroup container, int position, @Nullable Object object) {
            container.removeView((View) object);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
        switch (state) {
            //No operation
            case 0:
                if (currentItem == 0) {
                    viewPager.setCurrentItem(count, false);
                } else if (currentItem == count + 1) {
                    viewPager.setCurrentItem(1, false);
                }
                break;
            //start Sliding
            case 1:
                if (currentItem == count + 1) {
                    viewPager.setCurrentItem(1, false);
                } else if (currentItem == 0) {
                    viewPager.setCurrentItem(count, false);
                }
                break;
            //end Sliding
            case 2:
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(toRealPosition(position),
                    positionOffset, positionOffsetPixels);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPageSelected(int position) {
        currentItem = position;
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(toRealPosition(position));
        }
        if (bannerStyle == BannerConstant.CIRCLE_INDICATOR ||
                bannerStyle == BannerConstant.CIRCLE_INDICATOR_TITLE ||
                bannerStyle == BannerConstant.CIRCLE_INDICATOR_TITLE_INSIDE) {

            LinearLayout.LayoutParams Selectedparams = new LinearLayout.LayoutParams(
                    mIndicatorSelectedWidth, mIndicatorSelectedHeight);

            Selectedparams.leftMargin = mIndicatorMargin;
            Selectedparams.rightMargin = mIndicatorMargin;
            LinearLayout.LayoutParams
                    Unselectedparams = new LinearLayout.LayoutParams(mIndicatorWidth, mIndicatorHeight);
            Unselectedparams.leftMargin = mIndicatorMargin;
            Unselectedparams.rightMargin = mIndicatorMargin;
            indicatorImages.get((lastPosition - 1 + count) % count).setImageResource(mIndicatorUnselectedResId);
            indicatorImages.get((lastPosition - 1 + count) % count).setLayoutParams(Unselectedparams);
            indicatorImages.get((position - 1 + count) % count).setImageResource(mIndicatorSelectedResId);
            indicatorImages.get((position - 1 + count) % count).setLayoutParams(Selectedparams);
            lastPosition = position;
        }
        if (position == 0) {
            position = count;
        }
        if (position > count) {
            position = 1;
        }
        switch (bannerStyle) {
            case BannerConstant.CIRCLE_INDICATOR:
                break;
            case BannerConstant.NUM_INDICATOR:
                numIndicator.setText(position + "/" + count);
                break;
            case BannerConstant.NUM_INDICATOR_TITLE:
                numIndicatorInside.setText(position + "/" + count);
                if (titles.size()>position && position>0){
                    bannerTitle.setText(titles.get(position - 1));
                }
                break;
            case BannerConstant.CIRCLE_INDICATOR_TITLE:
                if (titles.size()>position && position>0){
                    bannerTitle.setText(titles.get(position - 1));
                }
                break;
            case BannerConstant.CIRCLE_INDICATOR_TITLE_INSIDE:
                if (titles.size()>position && position>0){
                    bannerTitle.setText(titles.get(position - 1));
                }
                break;
            default:
                break;
        }
    }

    public ViewPager getViewPager(){
        return viewPager;
    }

    public BannerView setOnBannerListener(OnBannerListener listener) {
        this.listener = listener;
        return this;
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    public void releaseBanner() {
        if (handler!=null){
            handler.removeCallbacks(task);
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
}
