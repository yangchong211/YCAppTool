package com.ycbjie.zhihu.ui.activity;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ycbjie.library.base.mvp.BaseActivity;
import com.ycbjie.library.utils.time.TimeUtils;
import com.ycbjie.library.utils.rxUtils.RxBus;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.ycbjie.zhihu.R;

import java.util.Calendar;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/04/21
 *     desc  : 知乎
 *     revise:
 * </pre>
 */
public class ZhiHuCalendarActivity extends BaseActivity implements View.OnClickListener {

    FrameLayout llTitleMenu;
    TextView toolbarTitle;
    MaterialCalendarView viewCalender;
    TextView tvCalenderEnter;
    private CalendarDay mDate;

    @Override
    public int getContentView() {
        return R.layout.activity_zh_calendar;
    }


    @Override
    public void initView() {
        llTitleMenu = findViewById(R.id.ll_title_menu);
        toolbarTitle = findViewById(R.id.toolbar_title);
        viewCalender = findViewById(R.id.view_calender);
        tvCalenderEnter = findViewById(R.id.tv_calender_enter);

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
        int i = v.getId();
        if (i == R.id.ll_title_menu) {
            finish();
        } else if (i == R.id.tv_calender_enter) {
            RxBus.getDefault().post(mDate);
            finish();
        } else {
        }
    }
}
