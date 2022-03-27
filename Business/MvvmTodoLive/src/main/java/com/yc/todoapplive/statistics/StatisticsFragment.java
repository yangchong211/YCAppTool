

package com.yc.todoapplive.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yc.todoapplive.R;
import com.yc.todoapplive.databinding.StatisticsFragBinding;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

/**
 * Main UI for the statistics screen.
 */
public class StatisticsFragment extends Fragment {

    private StatisticsFragBinding mViewDataBinding;

    private StatisticsViewModel mStatisticsViewModel;

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(
                inflater, R.layout.statistics_frag, container, false);
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mStatisticsViewModel = StatisticsActivity.obtainViewModel(getActivity());
        mViewDataBinding.setStats(mStatisticsViewModel);
        mViewDataBinding.setLifecycleOwner(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        mStatisticsViewModel.start();
    }

    public boolean isActive() {
        return isAdded();
    }
}
