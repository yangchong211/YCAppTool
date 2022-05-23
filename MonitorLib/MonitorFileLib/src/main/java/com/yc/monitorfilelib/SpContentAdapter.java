package com.yc.monitorfilelib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yc.eastadapterlib.BaseRecycleAdapter;
import com.yc.eastadapterlib.BaseViewHolder;

import java.util.List;


/**
 * <pre>
 *     author : 杨充
 *     email  : yangchong211@163.com
 *     time   : 2021/8/11
 *     desc   : sp适配器
 *     revise :
 * </pre>
 */
public class SpContentAdapter extends BaseRecycleAdapter<SpDataBean> {


    public SpContentAdapter(Context context) {
        super(context, R.layout.item_sp_content_view);
    }

    @Override
    protected void bindData(BaseViewHolder holder, SpDataBean spBean) {
        TextView tvSpKey = holder.getView(R.id.tv_sp_key);
        TextView tvSpType = holder.getView(R.id.tv_sp_type);
        TextView tvSpValue = holder.getView(R.id.tv_sp_value);

        tvSpKey.setText(spBean.key);
        tvSpType.setText(spBean.value.getClass().getSimpleName());
        tvSpValue.setText(spBean.value.toString());
    }

}
