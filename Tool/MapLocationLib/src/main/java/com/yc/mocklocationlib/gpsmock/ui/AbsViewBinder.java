package com.yc.mocklocationlib.gpsmock.ui;


import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;


public abstract class AbsViewBinder<T> extends RecyclerView.ViewHolder {
    private T data;
    private View mView;

    public AbsViewBinder(final View view) {
        super(view);
        this.mView = view;
        this.getViews();
        view.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AbsViewBinder.this.onViewClick(view, AbsViewBinder.this.data);
            }
        });
    }

    protected final View getView() {
        return this.mView;
    }

    protected abstract void getViews();

    public final <V extends View> V getView(@IdRes int id) {
        return this.mView.findViewById(id);
    }

    public abstract void bind(T var1);

    public void bind(T t, int position) {
        this.bind(t);
    }

    protected void onViewClick(View view, T data) {
    }

    protected final void setData(T data) {
        this.data = data;
    }

    protected final Context getContext() {
        return this.mView.getContext();
    }
}

