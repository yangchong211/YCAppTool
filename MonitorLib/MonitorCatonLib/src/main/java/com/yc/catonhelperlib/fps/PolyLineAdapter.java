package com.yc.catonhelperlib.fps;


import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.LayoutParams;
import java.util.ArrayList;
import java.util.List;

public class PolyLineAdapter extends Adapter<PolyLineAdapter.ViewHolder> {

    private List<PerformanceData> data;
    public int maxValue;
    public int minValue;
    public int itemWidth;
    public boolean drawDiver;
    public float pointSize;
    public boolean touchable;
    private boolean showLatestLabel;
    private PolyLineAdapter.OnViewClickListener onViewClickListener;

    private PolyLineAdapter() {
    }

    private PolyLineAdapter(List<PerformanceData> data, int maxValue, int minValue, int itemWidth) {
        this.data = data;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.itemWidth = itemWidth;
    }

    public void setData(List<PerformanceData> d) {
        if (this.data != null) {
            this.data.clear();
            this.data.addAll(d);
            this.notifyDataSetChanged();
        }

    }

    public void addData(PerformanceData bean) {
        this.data.add(bean);
        this.notifyDataSetChanged();
    }

    public void addData(List<PerformanceData> beans) {
        this.data.addAll(beans);
        this.notifyDataSetChanged();
    }

    public PolyLineAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PolyLineItemView item = new PolyLineItemView(parent.getContext());
        item.setMinValue(this.minValue);
        item.setMaxValue(this.maxValue);
        LayoutParams layoutParams = new LayoutParams(this.itemWidth, -1);
        item.setLayoutParams(layoutParams);
        return new PolyLineAdapter.ViewHolder(item);
    }

    public void onBindViewHolder(@NonNull PolyLineAdapter.ViewHolder holder, int position) {
        holder.bindData(position);
    }

    public int getItemCount() {
        return this.data.size();
    }

    public void setOnViewClickListener(PolyLineAdapter.OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }

    public interface OnViewClickListener {
        void onViewClick(int var1, PerformanceData var2);
    }

    class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        PolyLineItemView item;

        public ViewHolder(View itemView) {
            super(itemView);
            this.item = (PolyLineItemView)itemView;
            this.item.setDrawDiver(PolyLineAdapter.this.drawDiver);
            this.item.setPointSize(PolyLineAdapter.this.pointSize);
            this.item.setTouchable(PolyLineAdapter.this.touchable);
        }

        public void bindData(final int position) {
            if (PolyLineAdapter.this.onViewClickListener != null) {
                this.item.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        PolyLineAdapter.this.onViewClickListener.onViewClick(position, (PerformanceData)PolyLineAdapter.this.data.get(position));
                    }
                });
            }

            if (position == 0) {
                this.item.setDrawLeftLine(false);
            } else {
                this.item.setDrawLeftLine(true);
                this.item.setlastValue(((PerformanceData)PolyLineAdapter.this.data.get(position - 1)).parameter);
            }

            this.item.setCurrentValue(((PerformanceData)PolyLineAdapter.this.data.get(position)).parameter);
            this.item.setLabel(((PerformanceData)PolyLineAdapter.this.data.get(position)).date);
            if (position == PolyLineAdapter.this.data.size() - 1) {
                this.item.setDrawRightLine(false);
            } else {
                this.item.setDrawRightLine(true);
                this.item.setNextValue(((PerformanceData)PolyLineAdapter.this.data.get(position + 1)).parameter);
            }

            this.item.showLabel(PolyLineAdapter.this.showLatestLabel && position > PolyLineAdapter.this.data.size() - 3);
        }
    }

    public static class Builder {
        private int maxValue = 100;
        private int minValue = 0;
        private final int itemWidth;
        private List<PerformanceData> data = new ArrayList();
        private boolean drawDiver = true;
        private float pointSize;
        private boolean touchable = true;
        private boolean showLatestLabel;

        public Builder(Context context, int itemNumber) {
            this.itemWidth = getWidthPixels(context) / itemNumber;
        }

        public PolyLineAdapter.Builder setMaxValue(int maxValue) {
            this.maxValue = maxValue;
            return this;
        }

        public PolyLineAdapter.Builder setMinValue(int minValue) {
            this.minValue = minValue;
            return this;
        }

        public PolyLineAdapter.Builder setData(List<PerformanceData> data) {
            this.data = data;
            return this;
        }

        public PolyLineAdapter.Builder setDrawDiver(boolean drawDiver) {
            this.drawDiver = drawDiver;
            return this;
        }

        public PolyLineAdapter.Builder setPointSize(float size) {
            this.pointSize = size;
            return this;
        }

        public PolyLineAdapter build() {
            PolyLineAdapter adapter = new PolyLineAdapter(this.data, this.maxValue, this.minValue, this.itemWidth);
            adapter.drawDiver = this.drawDiver;
            adapter.pointSize = this.pointSize;
            adapter.touchable = this.touchable;
            adapter.showLatestLabel = this.showLatestLabel;
            return adapter;
        }

        public PolyLineAdapter.Builder setTouchable(boolean touchable) {
            this.touchable = touchable;
            return this;
        }

        public PolyLineAdapter.Builder setShowLatestLabel(boolean showLatestLabel) {
            this.showLatestLabel = showLatestLabel;
            return this;
        }
    }

    public static int getWidthPixels(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return 0;
        } else {
            windowManager.getDefaultDisplay().getMetrics(metrics);
            return metrics.widthPixels;
        }
    }
}

