package com.yc.mmaplog.test;

import static com.yc.mmaplog.test.LogInit.BUFFER_SIZE;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yc.mmaplog.LogManager;
import com.yc.mmaplog.appender.AbsAppender;
import com.yc.mmaplog.appender.AndroidAppender;
import com.yc.mmaplog.appender.FileAppender;
import com.yc.mmaplog.inter.InterFormatter;
import com.yc.mmaplog.inter.InterAppender;
import com.yc.mmaplog.inter.InterLogger;
import com.yc.mmaplog.logger.AppenderLogger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.pqpo.mmaplog.R;

public class MmapLogActivity extends AppCompatActivity {

    private static final String TAG = "MmapLog：";
    EditText etContent;
    EditText etThread;
    Button btnWrite;
    Button btnTest;
    TextView tvTest;
    EditText etTimes;

    boolean testing = false;

    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, MmapLogActivity.class);
            context.startActivity(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mmap_log);
        LogInit.init(this);
        etThread = findViewById(R.id.et_thread);
        etContent = findViewById(R.id.et_content);
        btnWrite = findViewById(R.id.btn_write);
        btnTest = findViewById(R.id.btn_test);
        tvTest = findViewById(R.id.tv_test);
        etTimes = findViewById(R.id.et_times);

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogManager.i(TAG, "点击时间触发");
                if (testing) {
                    Toast.makeText(getApplicationContext(), "testing", Toast.LENGTH_SHORT).show();
                    return;
                }
                int threads = Integer.valueOf(etThread.getText().toString());
                if (threads > 500) {
                    LogManager.i(TAG, "不要超过500个线程");
                    Toast.makeText(getApplicationContext(), "Do not exceed 500 threads", Toast.LENGTH_SHORT).show();
                    return;
                }
                LogManager.i(TAG, "开始写入数据拉");
                final String str = etContent.getText().toString();
                for (int i = 0; i < threads; i++) {
                    int count = i;
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            LogManager.i(TAG, str + " --- " + count);
                        }
                    }.start();
                }
                tvTest.setText("done!\nlog file path:" + getLogPath());
            }
        });

        btnTest.setOnClickListener(view -> {
            if (!testing) {
                tvTest.setText("start testing\n");
                testing = true;
                int times = Integer.valueOf(etTimes.getText().toString());
                performanceTest(times);
                testing = false;
            } else {
                Toast.makeText(getApplicationContext(), "testing", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_sync_log).setOnClickListener(view -> LogManager.flush());
        findViewById(R.id.btn_change_log).setOnClickListener(view -> changeLogPath());
    }

    private void performanceTest(int times) {
        tvTest.append(String.format(Locale.getDefault(),
                "## prints %d logs:\n", times));
        LogManager.release();
        AppenderLogger logger = new AppenderLogger.Builder().create();
        LogManager.setLogger(logger);

        logger.addAppender(createLog4aFileAppender());
        doPerformanceTest("log4a", times);
        LogManager.release();

        LogManager.setLogger(logger);
        logger.addAppender(createAndroidLogAppender());
        doPerformanceTest("android log", times);
        LogManager.release();

        LogManager.setLogger(logger);
        List<String> buffer = new ArrayList<>(times);
        logger.addAppender(createMemAppender(buffer));
        doPerformanceTest("array list log", times);
        buffer.clear();
        LogManager.release();

        LogManager.setLogger(logger);
        logger.addAppender(createNoBufferFileAppender());
        doPerformanceTest("file log(no buffer)", times);
        LogManager.release();

        LogManager.setLogger(logger);
        logger.addAppender(createWithBufferFileAppender());
        doPerformanceTest("file log(with buffer)", times);
        LogManager.release();

        LogManager.setLogger(logger);
        logger.addAppender(createNoBufferInThreadFileAppender());
        doPerformanceTest("file log(no buffer in thread)", times);
        tvTest.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogManager.release();
            }
        }, 1000);

        LogInit.init(this);
        tvTest.append("## end");
    }

    private InterAppender createNoBufferFileAppender() {
        File logFile = new File(LogInit.getLogDir(this), "logNoBufferFileTest.txt");
        logFile.delete();
        return new NoBufferFileAppender(logFile);
    }

    private InterAppender createWithBufferFileAppender() {
        File logFile = new File(LogInit.getLogDir(this), "logBufferFileTest.txt");
        logFile.delete();
        return new BufferFileAppender(logFile, BUFFER_SIZE);
    }

    private InterAppender createNoBufferInThreadFileAppender() {
        File logFile = new File(LogInit.getLogDir(this), "logNoBufferInThreadFileTest.txt");
        logFile.delete();
        return new NoBufferInThreadFileAppender(logFile);
    }

    private InterAppender createMemAppender(final List<String> buffer) {
        return new AbsAppender() {
            @Override
            protected void doAppend(int logLevel, String tag, String msg) {
                buffer.add(msg);
            }
        };
    }

    private InterAppender createLog4aFileAppender() {
        File log = LogInit.getLogDir(this);
        File cacheFile = new File(log, "test.logCache");
        File logFile = new File(log, "log4aTest.txt");
        cacheFile.delete();
        logFile.delete();
        FileAppender.Builder fileBuild = new FileAppender.Builder(this)
                .setLogFilePath(logFile.getAbsolutePath())
                .setBufferSize(BUFFER_SIZE)
                .setFormatter(new InterFormatter() {
                    @Override
                    public String format(int logLevel, String tag, String msg) {
                        return msg;
                    }
                })
                .setBufferFilePath(cacheFile.getAbsolutePath());
        return fileBuild.create();
    }

    private InterAppender createAndroidLogAppender() {
        return new AndroidAppender.Builder().create();
    }

    private void doPerformanceTest(String testName, int times) {
        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < times; i++) {
            LogManager.i(TAG, "log-" + i);
        }
        tvTest.append(String.format(Locale.getDefault(),
                "* %s spend: %d ms\n",
                testName,
                System.currentTimeMillis() - currentTimeMillis));
    }

    public String getLogPath() {
        String logPath = "";
        InterLogger logger = LogManager.getLogger();
        if (logger instanceof AppenderLogger) {
            List<InterAppender> appenderList = ((AppenderLogger) logger).getAppenderList();
            for (InterAppender appender : appenderList) {
                if (appender instanceof FileAppender) {
                    FileAppender fileAppender = (FileAppender) appender;
                    logPath = fileAppender.getLogPath();
                    break;
                }
            }
        }
        return logPath;
    }

    public void changeLogPath() {
        InterLogger logger = LogManager.getLogger();
        if (logger instanceof AppenderLogger) {
            List<InterAppender> appenderList = ((AppenderLogger) logger).getAppenderList();
            for (InterAppender appender : appenderList) {
                if (appender instanceof FileAppender) {
                    FileAppender fileAppender = (FileAppender) appender;
                    File log = LogInit.getLogDir(this);
                    String time = new SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(new Date());
                    String logPath = new File(log, time + "-yc-" + System.currentTimeMillis() + ".txt").getAbsolutePath();
                    fileAppender.changeLogPath(logPath);
                    break;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LogManager.flush();
    }
}
