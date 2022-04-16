package com.yc.lifehelper.component;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.jetpack.study.navigation.NavigationActivity;
import com.yc.jetpack.ui.activity.JetpackActivity;
import com.yc.jetpack.ui.activity.LoginActivity;
import com.yc.lifehelper.R;
import com.yc.logging.LoggerService;
import com.yc.logging.logger.Logger;
import com.yc.todoapp.tasks.TasksActivity;
import com.yc.todoapplive.tasks.TasksLiveActivity;
import com.yc.todoappmvvm.tasks.TasksMvvmActivity;
import com.yc.toolutils.click.FastClickUtils;
import com.ycbjie.android.view.activity.AndroidActivity;

import org.yczbj.ycrefreshviewlib.inter.InterItemView;

public class ToolComponent implements InterItemView {

    private final Logger logger = LoggerService.getInstance().getLogger("RecommendComponent");
    private Context context;

    @Override
    public View onCreateView(ViewGroup parent) {
        View headerView = View.inflate(parent.getContext(), R.layout.head_recommend_view, null);
        context = headerView.getContext();
        return headerView;
    }

    @Override
    public void onBindView(View header) {
        TextView tvHomeNine = header.findViewById(R.id.tv_home_nine);
        TextView tvHomeTen = header.findViewById(R.id.tv_home_ten);
        TextView tvHomeEleven = header.findViewById(R.id.tv_home_eleven);
        TextView tvHomeTwelve = header.findViewById(R.id.tv_home_twelve);

        tvHomeNine.setText("玩Android");
        tvHomeTen.setText("飞机大战");
        tvHomeEleven.setText("好玩工具");
        tvHomeTwelve.setText("音视频播放器");
        View.OnClickListener listener = v -> {
            if (FastClickUtils.isFastDoubleClick()){
                return;
            }
            switch (v.getId()) {
                //MVP经典todo案例
                case R.id.tv_home_nine:
                    AndroidActivity.Companion.startActivity((Activity) context,0);
                    break;
                //飞机大战
                case R.id.tv_home_ten:
                    NavigationActivity.Companion.startActivity(context);
                    break;
                //好玩工具
                case R.id.tv_home_eleven:
                    TasksLiveActivity.startActivity(context);
                    break;
                //音视频播放器
                case R.id.tv_home_twelve:
                    LoginActivity.Companion.startActivity(context);
                    break;
                default:
                    break;
            }
        };
        tvHomeNine.setOnClickListener(listener);
        tvHomeTen.setOnClickListener(listener);
        tvHomeEleven.setOnClickListener(listener);
        tvHomeTwelve.setOnClickListener(listener);
    }

}
