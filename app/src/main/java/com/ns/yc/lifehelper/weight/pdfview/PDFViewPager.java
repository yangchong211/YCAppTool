
package com.ns.yc.lifehelper.weight.pdfview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;

public class PDFViewPager extends ViewPager {

    protected Context context;

    public PDFViewPager(Context context, String pdfPath) {
        super(context);
        this.context = context;
        init(pdfPath);
    }

    protected void init(String pdfPath) {
        setClickable(true);
        initAdapter(context, pdfPath);
    }

    protected void initAdapter(Context context, String pdfPath) {
        setAdapter(new PDFPagerAdapter.Builder(context)
                .setPdfPath(pdfPath)
                .setOffScreenSize(getOffscreenPageLimit())
                .setOnPageClickListener(clickListener)
                .create());
    }

    private OnPageClickListener clickListener = new OnPageClickListener() {
        @Override
        public void onPageTap(View view, float x, float y) {
            int item = getCurrentItem();
            int total = getChildCount();
            if (x < 0.33f && item > 0) {
                item -= 1;
                setCurrentItem(item);
            } else if (x >= 0.67f && item < total - 1) {
                item += 1;
                setCurrentItem(item);
            }
        }
    };

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
