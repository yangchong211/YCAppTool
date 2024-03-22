package com.yc.widgetbusiness.status;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.yc.statusbar.bar.StateAppBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ycbjie.ycstatusbar.R;


public class StatusBarFiveFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment3_statusbar_white, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
