package com.yc.lifehelper.component;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.yc.jetpack.ui.activity.JetpackActivity;
import com.yc.lifehelper.R;
import com.yc.todoapp.tasks.TasksActivity;
import com.yc.todoapplive.tasks.TasksLiveActivity;
import com.yc.todoappmvvm.tasks.TasksMvvmActivity;
import com.yc.toolutils.click.FastClickUtils;

import org.yczbj.ycrefreshviewlib.inter.InterItemView;

public class RecommendComponent implements InterItemView {

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
                    JetpackActivity.Companion.startActivity(context);
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
