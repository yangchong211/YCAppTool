package com.yc.adapter;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2018/9/18
 *     desc  : 这个类是用来记录分组列表中组的结构的
 *     revise: 通过GroupStructure记录每个组是否有头部，是否有尾部和子项的数量。从而能方便的计算
 *             列表的长度和每个组的组头、组尾和子项在列表中的位置。
 *             https://github.com/yangchong211/YCGroupAdapter
 * </pre>
 */
public class GroupStructure {

    private boolean hasHeader;
    private boolean hasFooter;
    private int childrenCount;

    public GroupStructure(boolean hasHeader, boolean hasFooter, int childrenCount) {
        this.hasHeader = hasHeader;
        this.hasFooter = hasFooter;
        this.childrenCount = childrenCount;
    }

    public boolean hasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public boolean hasFooter() {
        return hasFooter;
    }

    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }
}
