package com.ns.yc.lifehelper.ui.other.gold.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.ui.other.gold.model.GoldManagerItemBean;
import com.ns.yc.ycutilslib.switchButton.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

import static com.ns.yc.lifehelper.ui.other.gold.view.activity.GoldMainActivity.typeStr;


public class GoldManagerAdapter extends RecyclerView.Adapter<GoldManagerAdapter.ViewHolder> {

    private RealmList<GoldManagerItemBean> mList;
    private LayoutInflater inflater;

    public GoldManagerAdapter(Context mContext, RealmList<GoldManagerItemBean> mList) {
        inflater = LayoutInflater.from(mContext);
        this.mList = mList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_gold_manager, parent, false));
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvType.setText(typeStr[mList.get(position).getIndex()]);
        holder.sb_btn.setChecked(mList.get(position).getIsSelect());
        holder.sb_btn.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                mList.get(holder.getAdapterPosition()).setSelect(isChecked);
            }
        });
    }


    @Override
    public int getItemCount() {
        return typeStr.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_gold_manager_type)
        TextView tvType;
        @BindView(R.id.sb_btn)
        SwitchButton sb_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
