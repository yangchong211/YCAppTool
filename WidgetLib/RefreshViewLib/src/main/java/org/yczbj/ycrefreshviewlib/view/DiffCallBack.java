package org.yczbj.ycrefreshviewlib.view;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class DiffCallBack<T> extends DiffUtil.Callback {

    /**
     * 分别是旧数据和新数据集合，这里使用泛型
     */
    private List<T> oldList , newList;

    public DiffCallBack(List<T> oldList , List<T> newList){
        this.oldList = oldList;
        this.newList = newList;
    }

    /**
     * 获取旧数据的长度
     * @return                  长度
     */
    @Override
    public int getOldListSize() {
        return oldList!=null ? oldList.size() : 0;
    }

    /**
     * 获取新数据的长度
     * @return                  长度
     */
    @Override
    public int getNewListSize() {
        return newList!=null ? newList.size() : 0;
    }

    /**
     *
     * @param i                 i
     * @param i1                i
     * @return
     */
    @Override
    public boolean areItemsTheSame(int i, int i1) {
        return false;
    }

    /**
     *
     * @param i
     * @param i1
     * @return
     */
    @Override
    public boolean areContentsTheSame(int i, int i1) {
        return false;
    }
}
