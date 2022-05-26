package com.yc.lifehelper.component;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.apploglib.AppLogHelper;
import com.yc.banner.view.BannerView;
import com.yc.catonhelperlib.fps.PerformanceActivity;
import com.yc.leetbusiness.LeetCodeActivity;
import com.yc.lifehelper.LocaleActivity;
import com.yc.lifehelper.R;
import com.yc.logging.LoggerService;
import com.yc.logging.logger.Logger;
import com.yc.monitorfilelib.FileExplorerActivity;
import com.yc.mvctodo.NetworkActivity;
import com.yc.other.thread.MainThreadActivity;
import com.yc.toollib.crash.CrashLibUtils;
import com.yc.toolutils.click.FastClickUtils;
import com.yc.video.ui.activity.VideoActivity;

import org.yczbj.ycrefreshviewlib.inter.InterItemView;

import java.util.ArrayList;
import java.util.List;

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
                    CrashLibUtils.startCrashTestActivity(context);
                    AppLogHelper.d("crash app log");
                    break;
                //网络工具
                case R.id.tv_home_third:
                    NetworkActivity.start(context);
                    AppLogHelper.w("net work tool");
                    break;
                //ANR监控
                case R.id.tv_home_four:
                    AppLogHelper.w("net work tool");
                    PerformanceActivity.startActivity(context);
                    break;
                //算法试题
                case R.id.tv_home_five:
                    LeetCodeActivity.startActivity(context);
                    AppLogHelper.v("leet code");
                    break;
                //国际化
                case R.id.tv_home_six:
                    LocaleActivity.startActivity(context);
                    break;
                //线程库
                case R.id.tv_home_seven:
                    MainThreadActivity.startActivity(context);
                    break;
                //视频播放器
                case R.id.tv_home_eight:
                    VideoActivity.startActivity(context);
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
