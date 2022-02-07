package com.yc.netlib.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.netlib.R;
import com.yc.netlib.utils.NetWorkUtils;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class NetRequestInfoFragment extends Fragment {

    private Activity activity;
    private TextView mTotalSec;
    private TextView mTotalTips;
    private TextView mTotalNumber;
    private TextView mTotalUpload;
    private TextView mTotalDown;
    private NetBarChart mNetworkBarChart;
    private NetPieChart mNetworkPierChart;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_net_info, container, false);
        return inflate;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFindViewById(view);
        initData();
    }


    private void initFindViewById(View view) {
        mTotalSec = view.findViewById(R.id.total_sec);
        mTotalTips = view.findViewById(R.id.total_tips);
        mTotalNumber = view.findViewById(R.id.total_number);
        mTotalUpload = view.findViewById(R.id.total_upload);
        mTotalDown = view.findViewById(R.id.total_down);
        mNetworkBarChart = view.findViewById(R.id.network_bar_chart);
        mNetworkPierChart = view.findViewById(R.id.network_pier_chart);
    }

    private void initData() {
        int postCount = NetworkManager.get().getPostCount();
        int getCount = NetworkManager.get().getGetCount();
        int totalCount = NetworkManager.get().getTotalCount();
        mTotalNumber.setText(String.valueOf(totalCount));
        long time = NetworkManager.get().getRunningTime();
        mTotalSec.setText(NetWorkUtils.formatTime(getContext(), time));
        long requestSize = NetworkManager.get().getTotalRequestSize();
        long responseSize = NetworkManager.get().getTotalResponseSize();
        mTotalUpload.setText(NetWorkUtils.getPrintSizeForSpannable(requestSize));
        mTotalDown.setText(NetWorkUtils.getPrintSizeForSpannable(responseSize));
        List<NetPieChart.PieData> data = new ArrayList<>();
        Resources resource = getResources();
        if (postCount != 0) {
            data.add(new NetPieChart.PieData(resource.getColor(R.color.colorAccent), postCount));
        }
        if (getCount != 0) {
            data.add(new NetPieChart.PieData(resource.getColor(R.color.colorPrimary), getCount));
        }
        mNetworkPierChart.setData(data);
        mNetworkBarChart.setData(postCount, getResources().getColor(R.color.colorAccent),
                getCount, getResources().getColor(R.color.colorPrimary));
    }

}
