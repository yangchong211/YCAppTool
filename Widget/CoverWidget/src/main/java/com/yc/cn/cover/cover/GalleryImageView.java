package com.yc.cn.cover.cover;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.yc.cn.cover.CoverLayoutManger;
import com.yc.cn.cover.CoverRecyclerView;
import com.ycbjie.photocoverlib.R;

import java.util.ArrayList;
import java.util.List;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/05/30
 *     desc  : 支持滑动viewpager图片浏览控件
 *     revise: https://github.com/yangchong211/YCGallery
 * </pre>
 */
public class GalleryImageView extends LinearLayout {

    private Context context;
    private CoverImageAdapter pagerAdapter;
    /**
     * url图片
     */
    private List<Object> mPictureList;
    private int thumbnailSize;
    private int thumbnailHeightSize;
    private GalleryViewPager viewPager;
    private CoverRecyclerView recyclerView;
    private int position = 0;

    public GalleryImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        mPictureList = new ArrayList<>();
        setOrientation(VERTICAL);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_scroll_gallery, this, true);
        recyclerView = findViewById(R.id.recycler_view);
        viewPager = findViewById(R.id.viewPager);
        recyclerView.setVisibility(VISIBLE);
        initializeViewPager();
    }

    public GalleryViewPager getViewPager() {
        return viewPager;
    }

    public int getCurrentItem() {
        return viewPager.getCurrentItem();
    }

    public GalleryImageView addOnPageChangeListener(final ViewPager.OnPageChangeListener listener) {
        viewPager.clearOnPageChangeListeners();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                if (recyclerView.getVisibility()==View.VISIBLE){
                    if(recyclerView.getCoverFlowLayout()!=null){
                        recyclerView.getCoverFlowLayout().smoothScrollToPosition(recyclerView,
                                new RecyclerView.State(), position);
                    }
                }
                listener.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                listener.onPageScrollStateChanged(state);
            }
        });
        return this;
    }

    public GalleryImageView addUrlToRecyclerView(List<Object> url){
        if (url == null){
            throw new NullPointerException("url may not be null!");
        }
        recyclerView.setVisibility(VISIBLE);
        mPictureList.addAll(url);
        initRecyclerView(mPictureList);
        if (viewPager!=null){
            viewPager.setCurrentItem(position);
        }
        pagerAdapter.notifyDataSetChanged();
        return this;
    }


    public GalleryImageView setCurrentItem(int i) {
        viewPager.setCurrentItem(i, false);
        return this;
    }

    public GalleryImageView setThumbnailSize(int thumbnailSize,int thumbnailHeightSize) {
        this.thumbnailSize = thumbnailSize;
        this.thumbnailHeightSize = thumbnailHeightSize;
        return this;
    }

    public GalleryImageView setPosition(int position) {
        this.position = position;
        return this;
    }

    public GalleryImageView hideThumbnails(boolean thumbnailsHiddenEnabled) {
        if (thumbnailsHiddenEnabled){
            recyclerView.setVisibility(GONE);
        } else {
            recyclerView.setVisibility(VISIBLE);
        }
        return this;
    }

    private void initializeViewPager() {
        pagerAdapter = new CoverImageAdapter(mPictureList,context);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(position);
    }

    private void initRecyclerView(final List<Object> mPictureList) {
        CoverAdapter adapter = new CoverAdapter(context, null, mPictureList);
        recyclerView.setAdapter(adapter);
        recyclerView.setAlphaItem(true);
        recyclerView.setFlatFlow(true);
        recyclerView.setIntervalRatio(1.05f);
        recyclerView.getCoverFlowLayout().scrollToPosition(position);
        recyclerView.setOnItemSelectedListener(new CoverLayoutManger.OnSelected() {
            @Override
            public void onItemSelected(int position) {
                if (position < mPictureList.size() && position>=0) {
                    setCurrentItem(position);
                }
            }
        });
        adapter.setOnClickLstn(new CoverAdapter.onItemClick() {
            @Override
            public void clickItem(int pos) {
                if (mPictureList.size()>pos && pos>=0){
                    setCurrentItem(pos);
                }
            }
        });
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private OnPhotoClickListener onPhotoClickListener;
    public interface OnPhotoClickListener{
        /**
         * 点击事件
         */
        void onClick();
    }

    /**
     * 设置点击事件
     * @param onPhotoClickListener                          listener
     */
    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }
}
