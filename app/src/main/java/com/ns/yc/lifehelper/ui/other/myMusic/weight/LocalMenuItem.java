package com.ns.yc.lifehelper.ui.other.myMusic.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/8/25
 * 描    述：我的音乐，本地音乐item抽取自定义
 * 修订历史：
 * ================================================
 */
public class LocalMenuItem extends FrameLayout {


    @Bind(R.id.icon)
    ImageView icon;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.count)
    TextView count;
    @Bind(R.id.speaker)
    ImageView speaker;
    private Context mContext;

    Drawable mIcon;
    String mTitle;

    public LocalMenuItem(Context context) {
        this(context, null);
    }

    public LocalMenuItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LocalMenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    void init(AttributeSet attrs) {
        ButterKnife.bind(LayoutInflater.from(mContext).inflate(R.layout.view_local_menu_item, this));
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.LocalMenuItem);
        mIcon = ta.getDrawable(R.styleable.LocalMenuItem_icon);
        mTitle = ta.getString(R.styleable.LocalMenuItem_title);
        ta.recycle();
        icon.setImageDrawable(mIcon);
        title.setText(mTitle);
    }

    public void showSpeaker() {
        speaker.setVisibility(VISIBLE);
    }

    public void hideSpeaker() {
        speaker.setVisibility(GONE);
    }

    public void setCount(int s) {
        count.setText("(" + String.valueOf(s) + ")");
    }

}
