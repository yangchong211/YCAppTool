package com.yc.toollib.tool;

import android.view.View;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/7/10
 *     desc  : 点击事件
 *     revise:
 * </pre>
 */
public interface OnItemClickListener {

    void onItemClick(View view, int position);

    void onLongClick(View view, int position);
}
