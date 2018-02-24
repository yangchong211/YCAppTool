package com.ns.yc.lifehelper.ui.other.zhihu.ui.activity;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.utils.time.TimeUtils;
import com.ns.yc.lifehelper.utils.rx.RxBus;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;

import butterknife.Bind;


public class ZhiHuCalendarActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.view_calender)
    MaterialCalendarView viewCalender;
    @Bind(R.id.tv_calender_enter)
    TextView tvCalenderEnter;
    private CalendarDay mDate;

    @Override
    public int getContentView() {
        return R.layout.activity_zh_calendar;
    }


    @Override
    public void initView() {
        toolbarTitle.setText("选择日期");
    }


    @Override
    public void initListener() {
        llTitleMenu.setOnClickListener(this);
        tvCalenderEnter.setOnClickListener(this);
    }


    @Override
    public void initData() {
        viewCalender.state().edit()
                .setFirstDayOfWeek(Calendar.WEDNESDAY)
                .setMinimumDate(CalendarDay.from(2013, 5, 20))
                .setMaximumDate(CalendarDay.from(TimeUtils.getCurrentYear(),
                        TimeUtils.getCurrentMonth(), TimeUtils.getCurrentDay()))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        viewCalender.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget,
                                       @NonNull CalendarDay date, boolean selected) {
                mDate = date;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_menu:
                finish();
                break;
            case R.id.tv_calender_enter:
                RxBus.getDefault().post(mDate);
                finish();
                break;
            default:
                break;
        }
    }
}
