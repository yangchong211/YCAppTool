package com.ycbjie.library.utils.time;

import android.os.CountDownTimer;

import java.util.Calendar;
import java.util.Date;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/7/3
 * 描    述：倒计时工具类
 * 修订历史：
 * ================================================
 */
public class TimerUtils {



    public static TimeCount timeCount;
    /**
     * 第二种倒计时，用系统自带的，可以在销毁
     * @return
     */
    public static TimeCount setTimer(int time){
        timeCount = new TimeCount(time*1000, 1000);
        timeCount.start();
        return timeCount;
    }

    public static class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);   //参数依次为总时长,和计时的时间间隔

        }
        @Override
        public void onFinish() {                            //计时完毕时触发

        }

        @Override
        public void onTick(long millisUntilFinished) {       //计时过程显示

        }
    }


    /**
     * <pre>
     * 判断date和当前日期是否在同一周内
     * 注:
     * Calendar类提供了一个获取日期在所属年份中是第几周的方法，对于上一年末的某一天
     * 和新年初的某一天在同一周内也一样可以处理，例如2012-12-31和2013-01-01虽然在
     * 不同的年份中，但是使用此方法依然判断二者属于同一周内
     * </pre>
     *
     * @param date
     * @return
     */
    public static boolean isSameWeekWithToday(Date date) {
        if (date == null) {
            return false;
        }
        // 0.先把Date类型的对象转换Calendar类型的对象
        Calendar todayCal = Calendar.getInstance();
        Calendar dateCal = Calendar.getInstance();
        todayCal.setTime(new Date());
        dateCal.setTime(date);
        // 1.比较当前日期在年份中的周数是否相同
        if (todayCal.get(Calendar.WEEK_OF_YEAR) == dateCal.get(Calendar.WEEK_OF_YEAR)) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean isSameDay(Calendar tvCalendar, Calendar today) {
        return tvCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && tvCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && tvCalendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }

}
