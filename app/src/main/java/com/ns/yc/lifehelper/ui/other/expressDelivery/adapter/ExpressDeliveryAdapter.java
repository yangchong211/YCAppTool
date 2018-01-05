package com.ns.yc.lifehelper.ui.other.expressDelivery.adapter;

import android.view.ViewGroup;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.expressDelivery.bean.ExpressDeliverySearchBean;
import com.ns.yc.lifehelper.ui.other.expressDelivery.activity.ExpressDeliveryInfoActivity;

import org.yczbj.ycrefreshviewlib.adapter.RecyclerArrayAdapter;
import org.yczbj.ycrefreshviewlib.viewHolder.BaseViewHolder;

/**
 * Created by PC on 2017/8/21.
 * 作者：PC
 */

public class ExpressDeliveryAdapter extends RecyclerArrayAdapter<ExpressDeliverySearchBean.ResultBean.ListBean> {

    public ExpressDeliveryAdapter(ExpressDeliveryInfoActivity activity) {
        super(activity);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpressDeliveryViewHolder(parent);
    }


    private class ExpressDeliveryViewHolder extends BaseViewHolder<ExpressDeliverySearchBean.ResultBean.ListBean> {

        TextView tv_content;

        ExpressDeliveryViewHolder(ViewGroup parent) {
            super(parent, R.layout.item_express_delivery_info);
            tv_content = $(R.id.tv_content);

        }

        @Override
        public void setData(ExpressDeliverySearchBean.ResultBean.ListBean data) {
            super.setData(data);
            tv_content.setText(data.getStatus());
        }
    }
}
