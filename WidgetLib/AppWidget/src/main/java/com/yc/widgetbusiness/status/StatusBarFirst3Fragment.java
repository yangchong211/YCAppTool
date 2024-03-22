package com.yc.widgetbusiness.status;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.yc.statusbar.bar.StateAppBar;
import com.yc.statusbar.utils.StatusBarUtils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ycbjie.ycstatusbar.R;

public class StatusBarFirst3Fragment extends Fragment {

    private StatusBarFragment3Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (StatusBarFragment3Activity) context;
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
            if(activity!=null){
                StateAppBar.setStatusBarColor(activity, ContextCompat.getColor(activity, R.color.white));
                //状态栏亮色模式，设置状态栏黑色文字、图标
                //注意：如果是设置白色状态栏，则需要添加下面这句话。如果是设置其他的颜色，则可以不添加，状态栏大都默认是白色字体和图标
                StatusBarUtils.StatusBarLightMode(activity);
            }
        }//展示
    }


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
