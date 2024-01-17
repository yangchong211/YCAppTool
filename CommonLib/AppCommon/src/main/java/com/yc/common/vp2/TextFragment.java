package com.yc.common.vp2;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yc.common.R;
import com.yc.library.base.mvp.BaseFragment;

/**
 * @author yangchong
 */
public class TextFragment extends BaseFragment {

    private TextView tvVpTitle;

    public static Fragment newInstance(String title){
        TextFragment fragment = new TextFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title" , title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_vp2_view;
    }

    @Override
    public void initView(View view) {
        tvVpTitle = view.findViewById(R.id.tv_vp_title);
        tvVpTitle.setText("文本");
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            tvVpTitle.setText(getArguments().getString("title"));
        }
    }


}
