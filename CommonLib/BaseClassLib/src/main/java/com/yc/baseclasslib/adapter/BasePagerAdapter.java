package com.yc.baseclasslib.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * <pre>
 *     @author yangchong
 *     GitHub : https://github.com/yangchong211/YCCommonLib
 *     time  : 2018/11/9
 *     desc  : PagerAdapter 简单封装
 *     revise: ViewPager的适配器
 * </pre>
 */
public abstract class BasePagerAdapter extends PagerAdapter {

    /**
     * 返回view的数量
     * @return  数量
     */
    @Override
    public abstract int getCount();

    /**
     * 获取item索引
     *
     * POSITION_UNCHANGED表示位置没有变化，即在添加或移除一页或多页之后该位置的页面保持不变，
     * 可以用于一个ViewPager中最后几页的添加或移除时，保持前几页仍然不变；
     *
     * POSITION_NONE，表示当前页不再作为ViewPager的一页数据，将被销毁，可以用于无视View缓存的刷新；
     * 根据传过来的参数Object来判断这个key所指定的新的位置
     * @param object                        object
     * @return
     */
    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    /**
     * 如果页面不是当前显示的页面也不是要缓存的页面，会调用这个方法，将页面销毁。
     * @param container                     container
     * @param position                      索引
     * @param object                        object
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    /**
     * 要显示的页面或需要缓存的页面，会调用这个方法进行布局的初始化。
     * @param container                     container
     * @param position                      索引
     * @return
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    /**
     * 刷新全部
     */
    @Override
    public void notifyDataSetChanged() {
        try {
            super.notifyDataSetChanged();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 注册数据观察者监听
     * @param observer                      observer
     */
    @Override
    public void registerDataSetObserver(@NonNull DataSetObserver observer) {
        super.registerDataSetObserver(observer);
    }

    /**
     * 解绑注册数据观察者监听
     * @param observer                      observer
     */
    @Override
    public void unregisterDataSetObserver(@NonNull DataSetObserver observer) {
        super.unregisterDataSetObserver(observer);
    }


}
