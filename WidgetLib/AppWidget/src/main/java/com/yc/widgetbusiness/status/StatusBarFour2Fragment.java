package com.yc.widgetbusiness.status;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.yc.statusbar.bar.StateAppBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ycbjie.ycstatusbar.R;

public class StatusBarFour2Fragment extends Fragment {


    private StatusBarFragment2Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (StatusBarFragment2Activity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    //判断是否展示—与ViewPager连用，进行左右切换
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            Log.e("StatusBarFragment2","setUserVisibleHint"+4);
            if(activity!=null){
                StateAppBar.translucentStatusBar(activity,
                        true);
            }
        }//展示
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment2_statusbar_translucent, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
