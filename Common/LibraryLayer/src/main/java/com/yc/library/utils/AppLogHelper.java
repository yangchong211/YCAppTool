package com.yc.library.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import com.yc.logging.config.LoggerConfig;
import com.yc.logging.LoggerFactory;
import com.yc.logging.util.Supplier;
import com.ycbjie.library.BuildConfig;

public final class AppLogHelper {

    private AppLogHelper() {

    }

    public static void config(@NonNull Application application) {
        /*
         * - serverHost 日志上传服务器地址，默认为国内地址，国外 APP 需要设置该参数。
         * - totalFileSize 总日志文件大小限制，默认 70 M，超出限制时将删除最老的文件。
         * - maxFileSize 单个日志文件大小限制，默认 5M，超出后将自动生成下一个文件。
         * - fileSectionLength 日志上传时分片大小，默认 2M，可以根据网络增加或减少，网络较差时可以减少分片大小。
         * - debuggable 是否是 debug 模式。
         * - fileLogEnabled 是否输出日志到文件，默认在 APK 非 debuggable 模式时开启。
         * - logcatLogEnabled 是否输出日志到 logcat，默认在 APK debuggable 模式时开启。
         * - encryptEnabled 日志是否加密，默认开启。
         * - fileLogLevel 文件日志输出级别，默认 INFO 级别。
         * - logcatLogLevel logcat 日志输出级别，默认 TRACE 级别。
         * - phoneNumSupplier 设置当前用户手机号，设置手机号后可以通过主动拉取渠道获取上传任务，为了提高日志上传成功率设置。
         */
        LoggerConfig.Builder configBuilder = new LoggerConfig.Builder();
        // 把脉日志上传的服务器，国际版走 global 域名，国内走 china 域名
        final String serverHost = "https://baidu.com/log";
        configBuilder.serverHost(serverHost);
        // 日志输出相关配置
        final boolean debug = BuildConfig.IS_DEBUG;
        configBuilder.debuggable(debug);
        // -- Debug 模式默认输出到 logcat
        configBuilder.logcatLogEnabled(debug);
        // -- 是否输出日志到文件：非 Debug 模式或指定输出日志到文件
        configBuilder.fileLogEnabled(true);
        // -- 是否对日志文件加密：非 Debug 模式默认对文件加密，业务不可配置
        configBuilder.encryptEnabled(true);
        // -- 日志文件总大小
        configBuilder.totalFileSize(100 * 1024 * 1024);
        // 用户手机号
        configBuilder.phoneNumSupplier(new Supplier<String>() {
            @Override
            public String get() {
                return "";
            }
        });
        LoggerConfig loggerConfig = configBuilder.build();
        // application 中调用
        LoggerFactory.init(application, loggerConfig);
    }

}
