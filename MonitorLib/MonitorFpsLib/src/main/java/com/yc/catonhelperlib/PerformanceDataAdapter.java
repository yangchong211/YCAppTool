package com.yc.catonhelperlib;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PerformanceDataAdapter extends AbsRecyclerAdapter<AbsViewBinder<PerformanceData>, PerformanceData> {
    private PerformanceDataAdapter.OnViewClickListener mOnViewClickListener;
    private PerformanceDataAdapter.OnViewLongClickListener mOnViewLongClickListener;

    public PerformanceDataAdapter(Context context) {
        super(context);
    }

    protected AbsViewBinder<PerformanceData> createViewHolder(View view, int viewType) {
        return new PerformanceDataAdapter.PerformanceItemViewHolder(view);
    }

    protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.item_performance_detail, parent, false);
    }

    public void setOnViewClickListener(PerformanceDataAdapter.OnViewClickListener onViewClickListener) {
        this.mOnViewClickListener = onViewClickListener;
    }

    public void setOnViewLongClickListener(PerformanceDataAdapter.OnViewLongClickListener onViewLongClickListener) {
        this.mOnViewLongClickListener = onViewLongClickListener;
    }

    public interface OnViewLongClickListener {
        boolean onViewLongClick(View var1, PerformanceData var2);
    }

    public interface OnViewClickListener {
        void onViewClick(View var1, PerformanceData var2);
    }

    public class PerformanceItemViewHolder extends AbsViewBinder<PerformanceData> {
        private TextView date;
        private TextView time;
        private TextView parameter;

        public PerformanceItemViewHolder(View view) {
            super(view);
        }

        protected void getViews() {
            this.date = (TextView)this.getView(R.id.date);
            this.time = (TextView)this.getView(R.id.time);
            this.parameter = (TextView)this.getView(R.id.parameter);
        }

        public void bind(PerformanceData performanceData) {
            this.date.setText(performanceData.date);
            this.time.setText(performanceData.time);
            this.parameter.setText(String.valueOf(performanceData.parameter));
        }

        protected void onViewClick(View view, PerformanceData data) {
            super.onViewClick(view, data);
            super.onViewClick(view, data);
            if (PerformanceDataAdapter.this.mOnViewClickListener != null) {
                PerformanceDataAdapter.this.mOnViewClickListener.onViewClick(view, data);
            }

        }
    }
}

