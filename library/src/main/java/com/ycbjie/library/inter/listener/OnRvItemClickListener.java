package com.ycbjie.library.inter.listener;

import android.view.View;

public interface OnRvItemClickListener<T> {

    void onItemClick(View view, int position, T data);

}