package com.yc.lifehelper.component;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.apploglib.AppLogHelper;
import com.yc.lifehelper.R;
import com.yc.monitorfilelib.FileExplorerActivity;
import com.yc.monitorpinglib.MonitorPingActivity;
import com.yc.crash.lib.CrashListActivity;
import com.yc.toolutils.click.FastClickUtils;
import org.yczbj.ycrefreshviewlib.inter.InterItemView;

public class HeaderComponent implements InterItemView {

    private Context context;

    @Override
    public View onCreateView(ViewGroup parent) {
        View headerView = View.inflate(parent.getContext(), R.layout.head_home_main, null);
        context = headerView.getContext();
        return headerView;
    }

    @Override
    public void onBindView(View header) {
        TextView tvHomeFirst = header.findViewById(R.id.tv_home_first);
        TextView tvHomeSecond =header.findViewById(R.id.tv_home_second);
        TextView tvHomeThird = header.findViewById(R.id.tv_home_third);
        TextView tvHomeFour = header.findViewById(R.id.tv_home_four);
        TextView tvHomeFive = header.findViewById(R.id.tv_home_five);
        TextView tvHomeSix =header.findViewById(R.id.tv_home_six);
        TextView tvHomeSeven = header.findViewById(R.id.tv_home_seven);
        TextView tvHomeEight = header.findViewById(R.id.tv_home_eight);

        View.OnClickListener listener = v -> {
            if (FastClickUtils.isFastDoubleClick()){
                return;
            }
            switch (v.getId()) {
                //沙盒File
                case R.id.tv_home_first:
                    AppLogHelper.d("file app tool");
                    FileExplorerActivity.startActivity(context);
                    break;
                //崩溃监控
                case R.id.tv_home_second:
                    CrashListActivity.startActivity(context);
                    AppLogHelper.d("crash app log");
                    break;
                //网络工具
                case R.id.tv_home_third:
                    AppLogHelper.w("net work tool");
                    break;
                //ANR监控
                case R.id.tv_home_four:
                    AppLogHelper.w("net work tool");
                    PerformanceActivity.startActivity(context);
                    break;
                //算法试题
                case R.id.tv_home_five:

                    break;
                //Debug工具
                case R.id.tv_home_six:

                    break;
                //线程库
                case R.id.tv_home_seven:

                    break;
                //Ping一下
                case R.id.tv_home_eight:
                    MonitorPingActivity.startActivity(context,"https://www.wanandroid.com/banner/json");
                    break;
                default:
                    break;
            }
        };
        tvHomeFirst.setOnClickListener(listener);
        tvHomeSecond.setOnClickListener(listener);
        tvHomeThird.setOnClickListener(listener);
        tvHomeFour.setOnClickListener(listener);
        tvHomeFive.setOnClickListener(listener);
        tvHomeSix.setOnClickListener(listener);
        tvHomeSeven.setOnClickListener(listener);
        tvHomeEight.setOnClickListener(listener);
    }


}
