package com.ns.yc.lifehelper.ui.other.weather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseRecyclerViewAdapter;
import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherDetail;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WeatherDetailAdapter extends BaseRecyclerViewAdapter<WeatherDetailAdapter.ViewHolder> {

    private List<WeatherDetail> details;

    public WeatherDetailAdapter(List<WeatherDetail> details) {
        this.details = details;
    }

    @Override
    public WeatherDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_detail, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(WeatherDetailAdapter.ViewHolder holder, int position) {
        WeatherDetail detail = details.get(position);
        holder.detailIconImageView.setImageResource(detail.getIconResourceId());
        holder.detailKeyTextView.setText(detail.getKey());
        holder.detailValueTextView.setText(detail.getValue());
    }

    @Override
    public int getItemCount() {
        return details == null ? 0 : details.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.detail_icon_image_view)
        ImageView detailIconImageView;
        @Bind(R.id.detail_key_text_view)
        TextView detailKeyTextView;
        @Bind(R.id.detail_value_text_view)
        TextView detailValueTextView;

        ViewHolder(View itemView, final WeatherDetailAdapter adapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.onItemHolderClick(ViewHolder.this);
                }
            });
        }
    }
}
