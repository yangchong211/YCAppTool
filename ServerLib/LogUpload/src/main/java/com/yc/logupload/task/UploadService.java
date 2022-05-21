package com.yc.logupload.task;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2018/4/17
 *     desc   : 核心上传文件类
 *     revise :
 * </pre>
 */
public class UploadService extends IntentService {

    public static final String TAG = "UploadService";

    public UploadService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
