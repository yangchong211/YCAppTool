package com.cheoo.photo.internal.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.cheoo.photo.internal.entity.Item;
import com.cheoo.photo.internal.ui.PreviewItemFragment;

import java.util.ArrayList;
import java.util.List;

public class PreviewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Item> mItems = new ArrayList<>();
    private OnPrimaryItemSetListener mListener;

    public PreviewPagerAdapter(FragmentManager manager, OnPrimaryItemSetListener listener) {
        super(manager);
        mListener = listener;
    }

    @Override
    public Fragment getItem(int position) {
        return PreviewItemFragment.newInstance(mItems.get(position));
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        if (mListener != null) {
            mListener.onPrimaryItemSet(position);
        }
    }

    public Item getMediaItem(int position) {
        return mItems.get(position);
    }

    public void addAll(List<Item> items) {
        mItems.addAll(items);
    }

    interface OnPrimaryItemSetListener {

        void onPrimaryItemSet(int position);
    }

}
