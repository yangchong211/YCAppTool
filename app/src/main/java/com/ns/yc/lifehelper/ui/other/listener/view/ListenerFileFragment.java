package com.ns.yc.lifehelper.ui.other.listener.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2016/9/19
 * 描    述：我的笔记本页面
 * 修订历史：
 * ================================================
 */
public class ListenerFileFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.radio_button_all)
    RadioButton radioButtonAll;
    @Bind(R.id.radio_button_folder)
    RadioButton radioButtonFolder;
    @Bind(R.id.radio_group_segmented_control)
    RadioGroup radioGroupSegmentedControl;
    @Bind(R.id.divider)
    View divider;
    @Bind(R.id.fragment_container_all)
    FrameLayout fragmentContainerAll;
    @Bind(R.id.fragment_container_folder)
    FrameLayout fragmentContainerFolder;

    private List<Fragment> mFragments = new ArrayList<>(2);
    private static final int DEFAULT_SEGMENT_INDEX = 0;
    final int[] FRAGMENT_CONTAINER_IDS = {R.id.fragment_container_all, R.id.fragment_container_folder};
    @Bind({R.id.radio_button_all, R.id.radio_button_folder})
    List<RadioButton> segmentedControls;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragments.add(new ListenerAllLocalFragment());
        mFragments.add(new ListenerFolderFragment());
    }

    @Override
    public int getContentView() {
        return R.layout.fragment_listener_file;
    }

    @Override
    public void initView() {
        initGroupButton();
    }

    private void initGroupButton() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment fragment = mFragments.get(i);
            fragmentTransaction.add(FRAGMENT_CONTAINER_IDS[i], fragment, fragment.getTag());
            fragmentTransaction.hide(fragment);
        }
        fragmentTransaction.commit();
        segmentedControls.get(DEFAULT_SEGMENT_INDEX).setChecked(true);
    }


    @Override
    public void initListener() {
        radioButtonAll.setOnCheckedChangeListener(this);
        radioButtonFolder.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int index = segmentedControls.indexOf(buttonView);
        Fragment fragment = mFragments.get(index);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (isChecked) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.hide(fragment);
        }
        fragmentTransaction.commit();
    }


}
