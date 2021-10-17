package com.yc.yc.lifehelper.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/11/21
 *     desc  : Vlayout框架基类适配器
 *     revise:
 * </pre>
 */
public class BaseDelegateAdapter extends DelegateAdapter.Adapter<BaseViewHolder> {

    private LayoutHelper mLayoutHelper;
    private int mCount;
    private int mLayoutId;
    private Context mContext;
    private int mViewTypeItem;

    protected BaseDelegateAdapter(Context context, LayoutHelper layoutHelper, int layoutId, int count, int viewTypeItem) {
        this.mContext = context;
        this.mCount = count;
        this.mLayoutHelper = layoutHelper;
        this.mLayoutId = layoutId;
        this.mViewTypeItem = viewTypeItem;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == mViewTypeItem) {
            MyViewHolder holder = new MyViewHolder(parent,mLayoutId);
            return holder;
        }
        return null;
    }

    private class MyViewHolder extends BaseViewHolder<Object> {
        public MyViewHolder(ViewGroup parent, int res) {
            super(parent, res);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

    }

    /**
     * 必须重写不然会出现滑动不流畅的情况
     */
    @Override
    public int getItemViewType(int position) {
        return mViewTypeItem;
    }

    /**
     * 条目数量
     * @return          条目数量
     */
    @Override
    public int getItemCount() {
        return mCount;
    }
}
