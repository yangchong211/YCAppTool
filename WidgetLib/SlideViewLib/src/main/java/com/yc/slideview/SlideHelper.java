package com.yc.slideview;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/7/3
 * 描    述：帮助类
 * 修订历史：
 * 项目地址：https://github.com/yangchong211/YCSlideView
 * ================================================
 */
public class SlideHelper {

    private List<Slide> mISlides = new ArrayList<>();

    public void add(Slide iSlide) {
        mISlides.add(iSlide);
    }

    public void remove(Slide iSlide) {
        mISlides.remove(iSlide);
    }

    public void clear() {
        mISlides.clear();
    }

    public List<Slide> getISlideList() {
        return mISlides;
    }

    public void slideOpen() {
        for (Slide slide : mISlides) {
            slide.slideOpen();
        }
    }

    public void slideClose() {
        for (Slide slide : mISlides) {
            slide.slideClose();
        }
    }
}
