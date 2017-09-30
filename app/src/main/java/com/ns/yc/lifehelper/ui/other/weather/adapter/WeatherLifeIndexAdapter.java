package com.ns.yc.lifehelper.ui.other.weather.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.BaseRecyclerViewAdapter;
import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherSuggestion;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WeatherLifeIndexAdapter extends BaseRecyclerViewAdapter<WeatherLifeIndexAdapter.ViewHolder> {

    private Context context;
    private List<WeatherSuggestion> indexList;

    public WeatherLifeIndexAdapter(Context context, List<WeatherSuggestion> indexList) {
        this.context = context;
        this.indexList = indexList;
    }

    @Override
    public WeatherLifeIndexAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_life_index, parent, false);
        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(WeatherLifeIndexAdapter.ViewHolder holder, int position) {
        holder.indexIconImageView.setImageDrawable(getIndexDrawable(context, indexList.get(position).getIndex()));
        holder.indexLevelTextView.setText(indexList.get(position).getValue());
        holder.indexNameTextView.setText(indexList.get(position).getKey());
    }

    @Override
    public int getItemCount() {
        return indexList == null ? 0 : indexList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.index_icon_image_view)
        ImageView indexIconImageView;
        @Bind(R.id.index_level_text_view)
        TextView indexLevelTextView;
        @Bind(R.id.index_name_text_view)
        TextView indexNameTextView;

        ViewHolder(View itemView, final WeatherLifeIndexAdapter adapter) {
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


    private Drawable getIndexDrawable(Context context, String indexName) {
        int colorResourceId = R.drawable.ic_index_sunscreen;
        if (indexName.contains("1")) {
            colorResourceId = R.drawable.ic_index_sunscreen;
        } else if (indexName.contains("2")) {
            colorResourceId = R.drawable.ic_index_dress;
        } else if (indexName.contains("3")) {
            colorResourceId = R.drawable.ic_index_sport;
        } else if (indexName.contains("4")) {
            colorResourceId = R.drawable.ic_index_shopping;
        } else if (indexName.contains("5")) {
            colorResourceId = R.drawable.ic_index_sun_cure;
        } else if (indexName.contains("6")) {
            colorResourceId = R.drawable.ic_index_car_wash;
        } else if (indexName.contains("7")) {
            colorResourceId = R.drawable.ic_index_clod;
        } else if (indexName.contains("8")) {
            colorResourceId = R.drawable.ic_index_dance;
        }
        return context.getResources().getDrawable(colorResourceId);
    }

}
