package com.yc.mocklocationlib.gpsmock.ui;


import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.mocklocationlib.R;

public class HomeTitleBar extends FrameLayout {
    private HomeTitleBar.OnTitleBarClickListener mListener;
    private TextView mTitle;
    private ImageView mIcon;

    public HomeTitleBar(@NonNull Context context) {
        this(context, (AttributeSet)null);
    }

    public HomeTitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeTitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.dk_home_title_bar, this, true);
        this.mIcon = (ImageView)this.findViewById(R.id.icon);
        this.mTitle = (TextView)this.findViewById(R.id.title);
        this.mIcon.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (HomeTitleBar.this.mListener != null) {
                    HomeTitleBar.this.mListener.onRightClick();
                }

            }
        });
    }

    public void setTitle(@StringRes int title) {
        this.setTitle(this.getResources().getString(title));
    }

    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            this.mTitle.setText("");
        } else {
            this.mTitle.setText(title);
            this.mTitle.setAlpha(0.0F);
            this.mTitle.setVisibility(VISIBLE);
            this.mTitle.animate().alpha(1.0F).start();
        }

    }

    public void setIcon(@DrawableRes int id) {
        if (id != 0) {
            this.mIcon.setImageResource(id);
            this.mIcon.setVisibility(VISIBLE);
        }
    }

    public void setListener(HomeTitleBar.OnTitleBarClickListener listener) {
        this.mListener = listener;
    }

    public interface OnTitleBarClickListener {
        void onRightClick();
    }
}

