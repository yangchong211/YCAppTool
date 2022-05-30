package com.yc.lifehelper.component;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.apploglib.AppLogHelper;
import com.yc.easy.demo.NetMainActivity;
import com.yc.jetpack.study.navigation.NavigationActivity;
import com.yc.jetpack.ui.activity.JetpackActivity;
import com.yc.lifehelper.R;
import com.yc.logging.LoggerService;
import com.yc.logging.logger.Logger;
import com.yc.todoapplive.tasks.TasksLiveActivity;
import com.yc.toolutils.click.FastClickUtils;
import com.ycbjie.android.view.activity.AndroidActivity;
import com.ycbjie.ycstatusbar.BarActivity;

import org.yczbj.ycrefreshviewlib.inter.InterItemView;

public class ToolComponent implements InterItemView {

    private final Logger logger = LoggerService.getInstance().getLogger("RecommendComponent");
    private Context context;

    @Override
    public View onCreateView(ViewGroup parent) {
        View headerView = View.inflate(parent.getContext(), R.layout.foot_tool_view, null);
        context = headerView.getContext();
        return headerView;
    }

    @Override
    public void onBindView(View header) {
        TextView tvTool1 = header.findViewById(R.id.tv_tool1);
        TextView tvTool2 = header.findViewById(R.id.tv_tool2);
        TextView tv_tool3 = header.findViewById(R.id.tv_tool3);
        TextView tv_tool4 = header.findViewById(R.id.tv_tool4);
        TextView tv_tool5 = header.findViewById(R.id.tv_tool5);
        TextView tv_tool6 = header.findViewById(R.id.tv_tool6);
        TextView tv_tool7 = header.findViewById(R.id.tv_tool7);
        TextView tv_tool8 = header.findViewById(R.id.tv_tool8);
        TextView tv_tool9 = header.findViewById(R.id.tv_tool9);
        TextView tv_tool10 = header.findViewById(R.id.tv_tool10);
        TextView tv_tool11 = header.findViewById(R.id.tv_tool11);
        TextView tv_tool12 = header.findViewById(R.id.tv_tool12);

        View.OnClickListener listener = v -> {
            if (FastClickUtils.isFastDoubleClick()){
                return;
            }
            switch (v.getId()) {
                //玩Android
                case R.id.tv_tool1:
                    AndroidActivity.Companion.startActivity((Activity) context,0);
                    break;
                //状态栏
                case R.id.tv_tool2:
                    BarActivity.startActivity(context);
                    break;
                //好玩工具
                case R.id.tv_tool3:
                    NetMainActivity.startActivity(context);
                    break;
                //音视频播放器
                case R.id.tv_tool4:
                    break;
                //音视频播放器
                case R.id.tv_tool5:
                    break;
                case R.id.tv_tool6:
                    break;
                case R.id.tv_tool7:
                    break;
                case R.id.tv_tool8:
                    break;
                default:
                    break;
            }
        };
        tvTool1.setOnClickListener(listener);
        tvTool2.setOnClickListener(listener);
        tv_tool3.setOnClickListener(listener);
        tv_tool4.setOnClickListener(listener);
        tv_tool5.setOnClickListener(listener);
        tv_tool6.setOnClickListener(listener);
        tv_tool7.setOnClickListener(listener);
        tv_tool8.setOnClickListener(listener);
        tv_tool9.setOnClickListener(listener);
        tv_tool10.setOnClickListener(listener);
        tv_tool11.setOnClickListener(listener);
        tv_tool12.setOnClickListener(listener);
    }

}
