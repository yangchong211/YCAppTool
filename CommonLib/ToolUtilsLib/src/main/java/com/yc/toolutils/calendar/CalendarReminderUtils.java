package com.yc.toolutils.calendar;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.text.TextUtils;

import com.yc.toolutils.system.AppLogUtils;

import java.util.Calendar;
import java.util.TimeZone;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time  : 2020/8/6
 *     desc  : 向系统日历插入事件工具类
 *     revise:
 * </pre>
 */
public class CalendarReminderUtils {

    /**
     * 一般来说实现向系统日历中读写事件一般有以下几个步骤：
     * (1)需要有读写日历权限;
     *      <uses-permission android:name="android.permission.READ_CALENDAR" />
     *      <uses-permission android:name="android.permission.WRITE_CALENDAR" />
     * (2)如果没有日历账户需要先创建账户;
     * (3)实现日历事件增删改查、提醒功能;
     *
     *
     * 为何这样做
     * 在项目开发过程中，有时会有预约提醒、定时提醒等需求，这时我们可以使用系统日历来辅助提醒。
     * 通过向系统日历中写入事件、设置提醒方式（闹钟），实现到达某个特定的时间自动提醒的功能。
     * 这样做的好处是由于提醒功能是交付给系统日历来做，不会出现应用被杀情况，能够做到准时提醒。
     *
     *
     * 内容提供者：ContentProvide， 是不同应用程序间进行数据交换的标准API，以URL形式对外提供数据接口；
     * 内容解析者：ContentResolver，根据URL 访问操作ContentProvide接口。
     *
     * 日历相关的资料：https://developer.android.com/guide/topics/providers/calendar-provider.html?hl=zh-cn#calendar
     */

    /**
     * 日历网址
     */
    private static final String CALENDER_URL = "content://com.android.calendar/calendars";
    /**
     * 日历事件URL
     */
    private static final String CALENDER_EVENT_URL = "content://com.android.calendar/events";
    /**
     * 日历提醒URL
     */
    private static final String CALENDER_REMINDER_URL = "content://com.android.calendar/reminders";

    /**
     * 初始化uri
     */
    /*static {
        if (Build.VERSION.SDK_INT >= 8) {
            CALENDER_URL = "content://com.android.calendar/calendars";
            CALENDER_EVENT_URL = "content://com.android.calendar/events";
            CALENDER_REMINDER_URL = "content://com.android.calendar/reminders";
        } else {
            CALENDER_URL = "content://calendar/calendars";
            CALENDER_EVENT_URL = "content://calendar/events";
            CALENDER_REMINDER_URL = "content://calendar/reminders";
        }
    }*/

    /**
     * 检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
     * 获取账户成功返回账户id，否则返回-1
     */
    private static int checkAndAddCalendarAccount(Context context, CalendarEvent calendarEvent) {
        //检查是否存在现有账户，存在则返回账户id，否则返回-1
        int oldId = checkCalendarAccount(context);
        if( oldId >= 0 ){
            //如果已经创建，就直接返回
            return oldId;
        }else{
            //添加日历账户，账户创建成功则返回账户id，否则返回-1
            long addId = addCalendarAccount(context,calendarEvent);
            if (addId >= 0) {
                //检查是否存在现有账户，存在则返回账户id，否则返回-1
                return checkCalendarAccount(context);
            } else {
                return -1;
            }
        }
    }

    /**
     * 检查是否存在现有账户，存在则返回账户id，否则返回-1
     */
    private static int checkCalendarAccount(Context context) {
        if (context == null){
            return -1;
        }
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null){
            return -1;
        }
        Cursor userCursor = contentResolver.query(Uri.parse(CALENDER_URL),
                null, null, null, null);
        try {
            if (userCursor == null) {
                //查询返回空值
                return -1;
            }
            int count = userCursor.getCount();
            if (count > 0) {
                //存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                int columnIndex = userCursor.getColumnIndex(CalendarContract.Calendars._ID);
                return userCursor.getInt(columnIndex);
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }

    /**
     * 添加日历账户，账户创建成功则返回账户id，否则返回-1。-1表示创建失败
     */
    private static long addCalendarAccount(Context context, CalendarEvent calendarEvent) {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        //  日历名称
        value.put(CalendarContract.Calendars.NAME,calendarEvent.getCalendars_name());
        //  日历账号，为邮箱格式
        value.put(CalendarContract.Calendars.ACCOUNT_NAME,calendarEvent.getCalendars_account_name());
        //  账户类型，com.android.exchange
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE,calendarEvent.getCalendars_account_type());
        //  展示给用户的日历名称
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,calendarEvent.getCalendars_display_name());
        //  它是一个表示被选中日历是否要被展示的值。
        //  0值表示关联这个日历的事件不应该展示出来。
        //  而1值则表示关联这个日历的事件应该被展示出来。
        //  这个值会影响CalendarContract.instances表中的生成行。
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        //  账户标记颜色
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        //  账户级别
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        //  它是一个表示日历是否应该被同步和是否应该把它的事件保存到设备上的值。
        //  0值表示不要同步这个日历或者不要把它的事件存储到设备上。
        //  1值则表示要同步这个日历的事件并把它的事件储存到设备上。
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        //  时区
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        //  账户拥有者
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT,calendarEvent.getCalendars_account_name());
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(CALENDER_URL);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME,calendarEvent.getCalendars_account_name())
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,calendarEvent.getCalendars_account_type())
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    /**
     * 添加日历事件
     */
    public static void addCalendarEvent(Context context, CalendarEvent calendarEvent,CalendarRemindListener callback) {
        if (context == null) {
            return;
        }
        if (!hasPermission(context)){
            throw new IllegalStateException("请先允许打开读取系统日历的权限");
        }
        //获取日历账户的id
        int calId = checkAndAddCalendarAccount(context,calendarEvent);
        AppLogUtils.i("addCalendarEvent----获取日历账户的id---"+calId);
        //获取账户id失败直接返回，添加日历事件失败
        if (calId < 0) {
            // 获取日历失败直接返回
            if(null != callback){
                callback.onFailed(CalendarRemindListener.Status.CALENDAR_ERROR);
            }
            return;
        }

        //添加日历事件
        Calendar mCalendar = Calendar.getInstance();
        //设置开始时间
        mCalendar.setTimeInMillis(calendarEvent.getReminderTime());
        long start = mCalendar.getTime().getTime();
        //设置终止时间，开始时间加10分钟
        mCalendar.setTimeInMillis(start + 10 * 60 * 1000);
        long end = mCalendar.getTime().getTime();

        //创建日历事件
        ContentValues event = new ContentValues();
        // 事件的日历_ID。插入账户的id
        event.put(CalendarContract.Events.CALENDAR_ID, calId);
        // 事件标题
        event.put(CalendarContract.Events.TITLE, calendarEvent.getTitle());
        // 事件发生的地点
        //event.put(CalendarContract.Events.EVENT_LOCATION, location);
        // 事件描述
        event.put(CalendarContract.Events.DESCRIPTION, calendarEvent.getDescription());
        // 事件开始时间
        event.put(CalendarContract.Events.DTSTART, start);
        // 事件结束时间
        event.put(CalendarContract.Events.DTEND, end);
        // 设置有闹钟提醒
        event.put(CalendarContract.Events.HAS_ALARM, 1);
        // 事件时区，必须有
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");
        // 添加事件
        Uri newEvent = context.getContentResolver().insert(Uri.parse(CALENDER_EVENT_URL), event);
        if (newEvent == null) {
            //添加日历事件失败直接返回
            // 添加日历事件失败直接返回
            if(null != callback){
                callback.onFailed(CalendarRemindListener.Status.EVENT_ERROR);
            }
            return;
        }

        // 事件提醒的设定
        ContentValues values = new ContentValues();
        // 事件的ID
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
        // 准时提醒    提前previous分钟提醒
        values.put(CalendarContract.Reminders.MINUTES, calendarEvent.getPrevious());
        // 提醒的方式
        // METHOD_ALERT         警报
        // METHOD_DEFAULT       默认的
        // METHOD_EMAIL         邮件
        // METHOD_SMS           短信息
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = context.getContentResolver().insert(Uri.parse(CALENDER_REMINDER_URL), values);
        if(uri == null) {
            //添加事件提醒失败直接返回
            // 添加提醒失败直接返回
            if(null != callback){
                callback.onFailed(CalendarRemindListener.Status.REMIND_ERROR);
            }
            return;
        }

        //添加提醒成功
        if(null != callback){
            callback.onSuccess();
        }
    }

    /**
     * 删除日历事件
     */
    public static void deleteCalendarEvent(Context context,String title ,CalendarRemindListener callback) {
        if (context == null){
            return;
        }
        if (!hasPermission(context)){
            throw new IllegalStateException("请先允许打开读取系统日历的权限");
        }
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null){
            return;
        }
        Cursor eventCursor = contentResolver.query(Uri.parse(CALENDER_EVENT_URL),
                null, null, null, null);
        try {
            if (eventCursor == null) {
                //查询返回空值
                return;
            }
            if (eventCursor.getCount() > 0) {
                //遍历所有事件，找到title跟需要查询的title一样的项
                for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
                    String eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
                    if (!TextUtils.isEmpty(title) && title.equals(eventTitle)) {
                        //取得id
                        int id = eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID));
                        Uri deleteUri = ContentUris.withAppendedId(Uri.parse(CALENDER_EVENT_URL), id);
                        int rows = context.getContentResolver().delete(deleteUri, null, null);
                        AppLogUtils.i("addCalendarEvent----取得id---"+id);
                        if (rows == -1) {
                            // 删除提醒失败直接返回
                            if(null != callback){
                                callback.onFailed(CalendarRemindListener.Status.REMIND_ERROR);
                            }
                            return;
                        }
                        //删除提醒成功
                        if(null != callback){
                            callback.onSuccess();
                        }
                    }
                }
            }
        } finally {
            if (eventCursor != null) {
                eventCursor.close();
            }
        }
    }

    /**
     * 日历提醒添加成功与否监控器
     */
    public interface CalendarRemindListener{
        enum Status {
            CALENDAR_ERROR,
            EVENT_ERROR,
            REMIND_ERROR
        }
        void onFailed(Status error_code);
        void onSuccess();
    }

    /**
     * 验证一下权限
     * @param context                           上下文
     * @return
     */
    private static boolean hasPermission(Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                (context.checkSelfPermission(Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED &&
                        context.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED);
    }

    public static void test(Context context){
        CalendarEvent calendarEvent = new CalendarEvent();
        long currentTimeMillis = System.currentTimeMillis();
        calendarEvent.setCalendars_name("日历"+currentTimeMillis);
        calendarEvent.setCalendars_account_name("杨充");
        calendarEvent.setCalendars_account_type("com.android.yangchong");
        calendarEvent.setCalendars_display_name("yangchong");
        calendarEvent.setTitle("杨充同学需要每一周写博客连载");
        calendarEvent.setDescription("写博客");
        //long remindTimeCalculator = remindTimeCalculator(2020, 9, 16, 10, 20);
        //long millis = TimeUtils.getMillis(new Date(), 5 * 60* 60, TimeConstants.SEC);
        //AppLogUtils.i("addCalendarEvent------onFailed-"+millis);
        calendarEvent.setReminderTime(100);
        calendarEvent.setPrevious(5);
        //添加到日历中
        addCalendarEvent(context, calendarEvent, new CalendarRemindListener() {
            @Override
            public void onFailed(Status error_code) {
                AppLogUtils.i("addCalendarEvent------onFailed-"+error_code);
            }

            @Override
            public void onSuccess() {
                AppLogUtils.i("addCalendarEvent------onSuccess-");
            }
        });
    }


    /**
     * 辅助方法：获取设置时间起止时间的对应毫秒数
     * @param year                      year
     * @param month                     1-12
     * @param day                       1-31
     * @param hour                      0-23
     * @param minute                    0-59
     * @return
     */
    public static long remindTimeCalculator(int year,int month,int day,int hour,int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month-1,day,hour,minute);
        return calendar.getTimeInMillis();
    }

    /**
     * 实体bean
     */
    public static class CalendarEvent{
        //日历名称
        private String calendars_name;
        //日历帐户名称
        private String calendars_account_name;
        //日历帐户类型
        private String calendars_account_type;
        //展示给用户的日历名称
        private String calendars_display_name;

        //标题
        private String title;
        //事件描述
        private String description;
        //开始时间
        private long reminderTime;
        //提前previous分钟提醒
        private long previous;

        public String getCalendars_name() {
            return calendars_name;
        }

        public void setCalendars_name(String calendars_name) {
            this.calendars_name = calendars_name;
        }

        public String getCalendars_account_name() {
            return calendars_account_name;
        }

        public void setCalendars_account_name(String calendars_account_name) {
            this.calendars_account_name = calendars_account_name;
        }

        public String getCalendars_account_type() {
            return calendars_account_type;
        }

        public void setCalendars_account_type(String calendars_account_type) {
            this.calendars_account_type = calendars_account_type;
        }

        public String getCalendars_display_name() {
            return calendars_display_name;
        }

        public void setCalendars_display_name(String calendars_display_name) {
            this.calendars_display_name = calendars_display_name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public long getReminderTime() {
            return reminderTime;
        }

        public void setReminderTime(long reminderTime) {
            this.reminderTime = reminderTime;
        }

        public long getPrevious() {
            return previous;
        }

        public void setPrevious(long previous) {
            this.previous = previous;
        }
    }

}
