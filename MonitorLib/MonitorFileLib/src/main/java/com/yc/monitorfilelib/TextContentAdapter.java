package com.yc.monitorfilelib;

import android.content.Context;
import android.widget.TextView;

import com.yc.eastadapterlib.BaseRecycleAdapter;
import com.yc.eastadapterlib.BaseViewHolder;


/**
 * <pre>
 *     author : 杨充
 *     email  : yangchong211@163.com
 *     time   : 2021/8/11
 *     desc   : 文本适配器
 *     revise :
 * </pre>
 */
public class TextContentAdapter extends BaseRecycleAdapter<String> {

    public TextContentAdapter(Context context) {
        super(context, R.layout.item_text_content_view);
    }

    @Override
    protected void bindData(BaseViewHolder holder, String s) {
        TextView tvName = holder.getView(R.id.tv_name);
        tvName.setText(s);
    }

}
