package com.yc.lifehelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.banner.view.BannerView;
import com.yc.catonhelperlib.fps.PerformanceActivity;
import com.yc.leetbusiness.LeetCodeActivity;
import com.yc.logging.LoggerService;
import com.yc.logging.logger.Logger;
import com.yc.monitorfilelib.FileExplorerActivity;
import com.yc.other.thread.MainThreadActivity;
import com.yc.other.ui.activity.net.NetworkActivity;
import com.yc.todoapp.tasks.TasksActivity;
import com.yc.todoapplive.tasks.TasksLiveActivity;
import com.yc.todoappmvvm.tasks.TasksMvvmActivity;
import com.yc.toollib.crash.CrashToolUtils;
import com.yc.toolutils.click.FastClickUtils;
import com.yc.video.ui.activity.VideoActivity;

import org.yczbj.ycrefreshviewlib.inter.InterItemView;

import java.util.ArrayList;
import java.util.List;

public class HeaderComponent implements InterItemView {

    private final Logger logger = LoggerService.getInstance().getLogger("HeaderComponent");
    private BannerView banner;
    private Context context;
    @Override
    public View onCreateView(ViewGroup parent) {
        View headerView = View.inflate(parent.getContext(), R.layout.head_home_main, null);
        context = headerView.getContext();
        return headerView;
    }

    @Override
    public void onBindView(View header) {
        banner = header.findViewById(R.id.banner);
        TextView tvHomeFirst = header.findViewById(R.id.tv_home_first);
        TextView tvHomeSecond =header.findViewById(R.id.tv_home_second);
        TextView tvHomeThird = header.findViewById(R.id.tv_home_third);
        TextView tvHomeFour = header.findViewById(R.id.tv_home_four);
        TextView tvHomeFive = header.findViewById(R.id.tv_home_five);
        TextView tvHomeSix =header.findViewById(R.id.tv_home_six);
        TextView tvHomeSeven = header.findViewById(R.id.tv_home_seven);
        TextView tvHomeEight = header.findViewById(R.id.tv_home_eight);
        TextView tvHomeNine = header.findViewById(R.id.tv_home_nine);
        TextView tvHomeTen = header.findViewById(R.id.tv_home_ten);
        TextView tvHomeEleven = header.findViewById(R.id.tv_home_eleven);

        View.OnClickListener listener = v -> {
            if (FastClickUtils.isFastDoubleClick()){
                return;
            }
            switch (v.getId()) {
                //沙盒File
                case R.id.tv_home_first:
                    logger.debug("file app tool");
                    FileExplorerActivity.startActivity(context);
                    break;
                //崩溃监控
                case R.id.tv_home_second:
                    CrashToolUtils.startCrashTestActivity(context);
                    logger.info("crash app log");
                    break;
                //网络工具
                case R.id.tv_home_third:
                    NetworkActivity.start(context);
                    logger.warn("net work tool");
                    break;
                //ANR监控
                case R.id.tv_home_four:
                    logger.error("net work tool");
                    PerformanceActivity.startActivity(context);
                    break;
                //算法试题
                case R.id.tv_home_five:
                    LeetCodeActivity.startActivity(context);
                    logger.error("leet code");
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
                //MVP经典todo案例
                case R.id.tv_home_nine:
                    TasksActivity.startActivity(context);
                    break;
                //MVVM案例经典todo案例
                case R.id.tv_home_ten:
                    TasksMvvmActivity.startActivity(context);
                    break;
                //MVVM案例经典todo案例
                case R.id.tv_home_eleven:
                    TasksLiveActivity.startActivity(context);
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
        tvHomeNine.setOnClickListener(listener);
        tvHomeTen.setOnClickListener(listener);
        tvHomeEleven.setOnClickListener(listener);
        initBanner();
    }

    /**
     * 初始化轮播图
     */
    private void initBanner() {
        if (banner!=null){
            List<Integer> list = new ArrayList<>();
            list.add(R.drawable.bg_small_autumn_tree_min);
            list.add(R.drawable.bg_small_kites_min);
            list.add(R.drawable.bg_small_leaves_min);
            list.add(R.drawable.bg_small_tulip_min);
            list.add(R.drawable.bg_small_tree_min);
            banner.setAnimationDuration(3000);
            banner.setDelayTime(4000);
            banner.setImages(list);
            banner.setImageLoader(new BannerImageLoader());
            banner.start();
        }
    }

    public BannerView getBannerView() {
        return banner;
    }
}
