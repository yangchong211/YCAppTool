package com.yc.lifehelper.component;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.TimeUtils;
import com.yc.catonhelperlib.fps.PerformanceActivity;
import com.yc.eastadapterlib.BaseRecycleAdapter;
import com.yc.eastadapterlib.BaseViewHolder;
import com.yc.jetpack.ui.activity.LoginActivity;
import com.yc.leetbusiness.LeetCodeActivity;
import com.yc.library.api.ConstantImageApi;
import com.yc.library.api.ConstantStringApi;
import com.yc.library.bean.ListNewsData;
import com.yc.lifehelper.LocaleActivity;
import com.yc.lifehelper.R;
import com.yc.logging.LoggerService;
import com.yc.logging.logger.Logger;
import com.yc.monitorfilelib.FileExplorerActivity;
import com.yc.mvctodo.NetworkActivity;
import com.yc.other.thread.MainThreadActivity;
import com.yc.other.ui.activity.TestActivity;
import com.yc.todoapp.tasks.TasksActivity;
import com.yc.todoapplive.tasks.TasksLiveActivity;
import com.yc.todoappmvvm.tasks.TasksMvvmActivity;
import com.yc.toollib.crash.CrashToolUtils;
import com.yc.toolutils.click.FastClickUtils;
import com.yc.video.ui.activity.VideoActivity;

import org.yczbj.ycrefreshviewlib.inter.InterItemView;
import org.yczbj.ycrefreshviewlib.item.RecycleViewItemLine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecommendComponent implements InterItemView {

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

        View.OnClickListener listener = v -> {
            if (FastClickUtils.isFastDoubleClick()){
                return;
            }
            switch (v.getId()) {
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
                //jetpack案例
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
