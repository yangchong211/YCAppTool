package com.ns.yc.lifehelper.ui.other.weather.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.adapter.BaseRecyclerViewAdapter;
import com.ns.yc.lifehelper.ui.other.weather.bean.WeatherForecast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WeatherForecastAdapter extends BaseRecyclerViewAdapter<WeatherForecastAdapter.ViewHolder> {

    private List<WeatherForecast.ForecastBean> weatherForecasts;

    public WeatherForecastAdapter(List<WeatherForecast.ForecastBean> weatherForecasts) {
        this.weatherForecasts = weatherForecasts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_forecast, parent, false);
        return new ViewHolder(itemView, this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(WeatherForecastAdapter.ViewHolder holder, int position) {
        holder.weekTextView.setText(weatherForecasts.get(position).getDate());
        holder.dateTextView.setText("日出时间"+ weatherForecasts.get(position).getAstro().getSr());
        holder.weatherTextView.setText(weatherForecasts.get(position).getCond().getCond_d());
        holder.tempMaxTextView.setText(weatherForecasts.get(position).getTmp().getMax()+ "°");
        holder.tempMinTextView.setText(weatherForecasts.get(position).getTmp().getMin()+ "°");
    }

    @Override
    public int getItemCount() {
        return weatherForecasts == null ? 0 : weatherForecasts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.week_text_view)
        TextView weekTextView;
        @Bind(R.id.date_text_view)
        TextView dateTextView;
        @Bind(R.id.weather_text_view)
        TextView weatherTextView;
        @Bind(R.id.temp_max_text_view)
        TextView tempMaxTextView;
        @Bind(R.id.temp_min_text_view)
        TextView tempMinTextView;

        ViewHolder(View itemView, final WeatherForecastAdapter adapter) {
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
