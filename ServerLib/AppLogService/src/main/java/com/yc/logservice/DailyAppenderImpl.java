package com.yc.logservice;


import com.yc.eventuploadlib.ExceptionReporter;
import com.yc.toolutils.AppLogUtils;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 职责描述: 带检查日志是否跨天功能的FileWriter
 */
public class DailyAppenderImpl extends FileAppenderImpl {

    public static final String TAG = DailyAppenderImpl.class.getSimpleName();

    //日期格式
    private String datePattern = "'.'yyyy-MM-dd";
    SimpleDateFormat sDateFormat;

    /**
     * scheduledFilename 意义:当前打印的日志文件tmlog.tm 对应带日期的文件名,如tmlog.tm.2019-07-16
     * 当日志打印进程跨天后(手机当前的日期 不是2019-07-16时),需要将tmlog.tm 重命名为tmlog.tm.2019-07-16
     * tmlog.tm 永远代表当前正写的日志
     */
    private String scheduledFilename;


    private LogService mLogService;


    public DailyAppenderImpl(String filename,
                             String datePattern, LogService logService) throws IOException {
        super(filename, true);
        this.datePattern = datePattern;
        mLogService = logService;
        activateOptions();
    }

    //----- 继承父类的方法 -----//

    @Override
    public void activateOptions() {
        bindLogFile();
        super.activateOptions();
    }

    @Override
    protected void doAppend(String msg) {
        checkRollOverDay();
        try {
            super.doAppend(msg);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionReporter.report("do append string" , e);
            //当app 没有获取写文件权限时,会报NullPointerException 异常,此时KillService,保证用户允许权限后,日志可以正常打印到文件系统
            if (e instanceof NullPointerException) {
                mLogService.killSelf();
            }
        }
    }

    @Override
    protected void doAppend(List<String> list) {
        checkRollOverDay();
        try {
            super.doAppend(list);
        } catch (Exception e) {
            ExceptionReporter.report("do append list" , e);
            e.printStackTrace();
        }
    }


    // -------- 扩展的方法 -------- //

    /**
     * 确定FileAppender对象实例 绑定的日志文件
     * 若文件已经存在,则将原始文件重命名。
     */
    void bindLogFile() {
        AppLogUtils.e("bindLogFile[" + fileName + "].");
        if (datePattern != null && fileName != null) {
            sDateFormat = new SimpleDateFormat(datePattern);
            File file = new File(fileName);

            //fileName文件已经存在,则将原始文件重命名.
            if (file.exists()) {
                String targetFilename = fileName + sDateFormat.format(new Date(file.lastModified()));
                renameFile(fileName, targetFilename);
            }

            scheduledFilename = fileName + sDateFormat.format(System.currentTimeMillis());

        } else {
            AppLogUtils.e("Either File or DatePattern options are not set for appender ["
                    + fileName + "].");
        }
    }


    /**
     * 文件重命名: 将src 重命名为 target.
     * 如 tmlog.tm -> tmlog.tm.2019-07-16
     * 如果tmlog.tm.2019-07-16 文件已经存在了,则将target 修改为tmlog.tm.2019-07-16_1 依次类推
     *
     * @param src
     * @param target
     */
    void renameFile(String src, String target) {
        try {

            File targetFile = new File(target);
            int i = 0;
            while (targetFile.exists()) {
                i++;
                targetFile = new File(target + "_" + i);
            }
            File scrFile = new File(src);
            boolean result = scrFile.renameTo(targetFile);
            if (result) {
                AppLogUtils.d(src + " ->>> " + target);
            } else {
                AppLogUtils.e("Failed to rename [" + src + "] to [" + target + "].");
            }
        } catch (Exception e) {
            AppLogUtils.e(e.getMessage(), e);
            ExceptionReporter.report("renameFile : " , e);
        }
    }


    /**
     * 判断当前日志文件tmlog.tm 有没有跨天
     * 若发生了跨天, 日志文件名tmlog.tm,需要修改为tmlog.tm.日期 (tmlog.tm.2019-07-16_1)的格式
     * 并重新创建tmlog.tm文件
     *
     * @throws IOException
     */
    void rollOverDay() throws IOException {
        if (datePattern == null) {
            AppLogUtils.e(TAG, "datePattern is null");
            return;
        }

        String datedFilename = fileName + sDateFormat.format(System.currentTimeMillis());

        if (!scheduledFilename.equals(datedFilename)) {
            AppLogUtils.e(TAG, "2 scheduledFilename:" + scheduledFilename + ",datedFilename:" + datedFilename);
            this.reset();
            renameFile(fileName, scheduledFilename);

            try {
                this.setFile(fileName, true, this.bufferedIO, this.bufferSize);
            } catch (IOException e) {
                AppLogUtils.e(TAG, "setFile(" + fileName + ", true) call failed.");
                ExceptionReporter.report("rollOverDay : " , e);
            }
            scheduledFilename = datedFilename;
        }

    }

    void checkRollOverDay() {
        checkFileExist();
        try {
            rollOverDay();
        } catch (IOException ioe) {
            if (ioe instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            AppLogUtils.e(TAG, "rollOver() failed."+ioe);
            ExceptionReporter.report("checkRollOverDay : " , ioe);
        }
    }

    public void checkFileExist() {

        File currentFile = new File(fileName);
        if (!currentFile.exists()) {
            activateOptions();
            AppLogUtils.d("checkFileExist fileNotExist , create it again");
        }
    }


    public static String getWholeTimeString(long time) {

        String str = String.valueOf(time);
        if (str.length() < 13) {
            time = time * 1000;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss,SSS");
        String result = formatter.format(new Date(time));
        AppLogUtils.d(TAG, "time:" + time + ",readable:" + result);
        return result;
    }


    // ---------------- getter and setter ----------------//

    public void setDatePattern(String pattern) {
        datePattern = pattern;
    }


    public String getDatePattern() {
        return datePattern;
    }


}




