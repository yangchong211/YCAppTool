package com.yc.lifehelper.component;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yc.android.view.activity.AndroidActivity;
import com.yc.blesample.ble.BleMainActivity;
import com.yc.common.CommonActivity;
import com.yc.easy.demo.NetMainActivity;
import com.yc.kotlinbusiness.KotlinHomeActivity;
import com.yc.lifehelper.R;
import com.yc.logging.LoggerService;
import com.yc.logging.logger.Logger;
import com.yc.toolutils.click.FastClickUtils;
import com.yc.ycnotification.NotifyHomeActivity;
import com.yc.ycstatusbar.BarActivity;
import com.yc.ycthreadpool.ThreadMainActivity;

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
                //网络
                case R.id.tv_tool3:
                    NetMainActivity.startActivity(context);
                    break;
                //蓝牙
                case R.id.tv_tool4:
                    BleMainActivity.startActivity(context);
                    break;
                //基础组件案例
                case R.id.tv_tool5:
                    CommonActivity.startActivity(context);
                    break;
                //线程池组件案例
                case R.id.tv_tool6:
                    ThreadMainActivity.startActivity(context);
                    break;
                //通知栏
                case R.id.tv_tool7:
                    NotifyHomeActivity.startActivity(context);
                    break;
                //kotlin原理
                case R.id.tv_tool8:
                    KotlinHomeActivity.Companion.startActivity(context);
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
