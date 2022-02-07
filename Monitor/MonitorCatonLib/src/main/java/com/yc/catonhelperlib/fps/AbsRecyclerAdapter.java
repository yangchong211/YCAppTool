package com.yc.catonhelperlib.fps;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbsRecyclerAdapter<T extends AbsViewBinder, V> extends Adapter<T> {
    private static final String TAG = "AbsRecyclerAdapter";
    protected List<V> mList;
    private LayoutInflater mInflater;

    public AbsRecyclerAdapter(Context context) {
        if (context == null) {
            Log.e("AbsRecyclerAdapter", "Context should not be null");
        } else {
            this.mList = new ArrayList();
            this.mInflater = LayoutInflater.from(context);
        }
    }

    public final T onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.createView(this.mInflater, parent, viewType);
        return this.createViewHolder(view, viewType);
    }

    protected abstract T createViewHolder(View var1, int var2);

    protected abstract View createView(LayoutInflater var1, ViewGroup var2, int var3);

    public final void onBindViewHolder(T holder, int position) {
        V data = this.mList.get(position);
        holder.setData(data);
        holder.bind(data, position);
    }

    public int getItemCount() {
        return this.mList.size();
    }

    public void append(V item) {
        if (item != null) {
            this.mList.add(item);
            this.notifyDataSetChanged();
        }
    }

    public void append(V item, int position) {
        if (item != null) {
            if (position < 0) {
                position = 0;
            } else if (position > this.mList.size()) {
                position = this.mList.size();
            }

            this.mList.add(position, item);
            this.notifyDataSetChanged();
        }
    }

    public final void append(Collection<V> items) {
        if (items != null && items.size() != 0) {
            this.mList.addAll(items);
            this.notifyDataSetChanged();
        }
    }

    public final void clear() {
        if (!this.mList.isEmpty()) {
            this.mList.clear();
            this.notifyDataSetChanged();
        }
    }

    public final void remove(V item) {
        if (item != null) {
            if (this.mList.contains(item)) {
                this.mList.remove(item);
                this.notifyDataSetChanged();
            }

        }
    }

    public final void remove(int index) {
        if (index < this.mList.size()) {
            this.mList.remove(index);
            this.notifyDataSetChanged();
        }

    }

    public final void remove(Collection<V> items) {
        if (items != null && items.size() != 0) {
            if (this.mList.removeAll(items)) {
                this.notifyDataSetChanged();
            }

        }
    }

    public void setData(Collection<V> items) {
        if (items != null && items.size() != 0) {
            if (this.mList.size() > 0) {
                this.mList.clear();
            }

            this.mList.addAll(items);
            this.notifyDataSetChanged();
        }
    }

    public List<V> getData() {
        return new ArrayList(this.mList);
    }
}

