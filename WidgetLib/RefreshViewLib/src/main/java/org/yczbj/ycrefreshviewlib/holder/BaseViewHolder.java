/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package org.yczbj.ycrefreshviewlib.holder;

import android.content.Context;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.inter.OnItemChildClickListener;
import org.yczbj.ycrefreshviewlib.utils.RefreshLogUtils;

import java.lang.reflect.Field;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/4/22
 *     desc  : ViewHolder的简单封装，M为这个itemView对应的model，使用RecyclerArrayAdapter就一定要用
 *             这个ViewHolder
 *             推荐子类继承第二个构造函数。并将子类的构造函数设为一个ViewGroup parent
 *     revise: 参考鸿洋大神的baseAdapter封装库
 *             具体可以参考我的adapter封装库：https://github.com/yangchong211/YCBaseAdapter
 * </pre>
 */
public class BaseViewHolder<M> extends RecyclerView.ViewHolder {


    // SparseArray 比 HashMap 更省内存，在某些条件下性能更好，只能存储 key 为 int 类型的数据，
    // 用来存放 View 以减少 findViewById 的次数

    private SparseArray<View> viewSparseArray;

    public BaseViewHolder(View itemView) {
        super(itemView);
        if(viewSparseArray==null){
            viewSparseArray = new SparseArray<>();
        }
    }

    public BaseViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
        if(viewSparseArray==null){
            viewSparseArray = new SparseArray<>();
        }
    }

    /**
     * 子类设置数据方法
     * @param data                 data
     */
    public void setData(M data) {}

    /**
     * findViewById方式
     * 根据 ID 来获取 View
     * @param viewId viewID
     * @param <T>    泛型
     * @return 将结果强转为 View 或 View 的子类型
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        // 先从缓存中找，找打的话则直接返回
        // 如果找不到则 findViewById ，再把结果存入缓存中
        View view = viewSparseArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            viewSparseArray.put(viewId, view);
        }
        return (T) view;
    }


    /**
     * 获取上下文context
     * @return                  context
     */
    protected Context getContext(){
        return itemView.getContext();
    }


    /**
     * 获取数据索引的位置
     * @return                  position
     */
    protected int getDataPosition(){
        RecyclerView.Adapter adapter = getOwnerAdapter();
        if (adapter instanceof RecyclerArrayAdapter){
            int headerCount = ((RecyclerArrayAdapter) adapter).getHeaderCount();
            //注意需要减去header的count，否则造成索引错乱
            return getAdapterPosition() - headerCount;
        }
        return getAdapterPosition();
    }


    /**
     * 获取adapter对象
     * @param <T>               adapter
     * @return                  adapter
     */
    @Nullable
    private  <T extends RecyclerView.Adapter> T getOwnerAdapter(){
        RecyclerView recyclerView = getOwnerRecyclerView();
        //noinspection unchecked
        return recyclerView != null ? (T) recyclerView.getAdapter() : null;
    }


    @SuppressWarnings("CatchMayIgnoreException")
    @Nullable
    private RecyclerView getOwnerRecyclerView(){
        try {
            //使用反射
            Field field = RecyclerView.ViewHolder.class.getDeclaredField("mOwnerRecyclerView");
            //设置暴力访问权限
            field.setAccessible(true);
            return (RecyclerView) field.get(this);
        } catch (NoSuchFieldException ignored) {
            RefreshLogUtils.e(ignored.getLocalizedMessage());
        } catch (IllegalAccessException ignored) {
            RefreshLogUtils.e(ignored.getLocalizedMessage());
        }
        return null;
    }

    /**
     * 添加子控件的点击事件
     * @param viewId                        控件id
     */
    protected void addOnClickListener(@IdRes final int viewId) {
        final View view = getView(viewId);
        if (view != null) {
            if (!view.isClickable()) {
                //如果是不可点击，则需要手动设置可以点击
                view.setClickable(true);
            }
            view.setOnClickListener(listener);
        }
    }

    /**
     * 创建listener监听，主要是item中的child点击监听
     */
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(getOwnerAdapter()!=null){
                OnItemChildClickListener onItemChildClickListener = ((RecyclerArrayAdapter)
                        getOwnerAdapter()).getOnItemChildClickListener();
                if (onItemChildClickListener != null) {
                    onItemChildClickListener.onItemChildClick(v, getDataPosition());
                }
            }
        }
    };

    /**
     * 设置TextView的值
     */
    public BaseViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }


}