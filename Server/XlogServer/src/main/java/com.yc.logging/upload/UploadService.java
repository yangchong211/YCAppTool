package com.yc.logging.upload;

import android.content.Context;
import android.support.annotation.RestrictTo;
import com.yc.logging.upload.persist.*;
import com.yc.logging.util.ArchTaskExecutor;
import com.yc.logging.util.Debug;
import com.yc.logging.util.LoggerUtils;
import com.yc.logging.util.ReportUtils;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestrictTo(RestrictTo.Scope.LIBRARY)
public class UploadService {

    private static UploadService sInstance;
    private Set<SliceRecord> mUploadingQueue = Collections.synchronizedSet(new HashSet<SliceRecord>());

    public static UploadService getInstance() {
        if (sInstance == null) {
            synchronized (UploadService.class) {
                if (sInstance == null) {
                    sInstance = new UploadService();
                }
            }
        }
        return sInstance;
    }

    private UploadService() {
    }

    public void start(final Context context) {
        ArchTaskExecutor.getInstance().executeOnDiskIO(new Runnable() {
            @Override
            public void run() {
                try {
                    resumeUploadTask(context);
                } catch (Exception e) {
                    Debug.logOrThrow("init err", e);
                    ReportUtils.reportProgramError("logging_upload_err", e);
                }
            }
        });
    }

    private static void LOG(String msg) {
        Debug.d("UploadService: " + msg);
    }

    private void resumeUploadTask(Context context) {
        LOG("resume upload task");
        TaskRecordDao taskRecordDao = UploadTaskDatabase.getDatabase().getTaskRecordDao();
        List<TaskRecord> taskRecords = taskRecordDao.getRecordList();
        if (taskRecords.isEmpty()) {
            LOG("no pending task to upload");
        }
        for (TaskRecord record : taskRecords) {
            resumeTaskRecord(context, record);
        }
    }

    private void resumeTaskRecord(Context context, TaskRecord record) {
        LOG("start upload task: " + record);
        SliceRecordDao sliceRecordDao = UploadTaskDatabase.getDatabase().getFileRecordDao();
        String taskId = record.getTaskId();
        List<SliceRecord> sliceRecords = sliceRecordDao.getRecordsByTaskId(taskId);

        boolean fatal = uploadSections(context, sliceRecords);

        if (!fatal) {
            sliceRecords = sliceRecordDao.getRecordsByTaskId(taskId);
            if (sliceRecords.isEmpty()) {
                //success
                LOG("task upload success: " + record);
                UploadTaskManager.getInstance().taskSuccess(context, record.getTaskId());
            } else {
                LOG("task upload stopped: " + record + "reminds slice records:" + sliceRecords);
            }
        }
    }

    private boolean uploadSections(final Context context, List<SliceRecord> sliceRecords) {
        SliceRecordDao sliceRecordDao = UploadTaskDatabase.getDatabase().getFileRecordDao();

        boolean failed = false;
        for (SliceRecord record : sliceRecords) {
            LOG("uploading slice:" + record);
            String taskId = record.getTaskId();

            //正在上传中
            if (mUploadingQueue.contains(record)) {
                LOG("slice is uploading, stop:" + record);
                break;
            }

            //超过重试次数
            if (record.getUploadCount() >= 10) {
                LOG("slice failed with count >=10:" + record);
                String reason = "文件分片" + record.getSliceId() + "/" + record.getSliceCount() + "超过重试次数";
                UploadTaskManager.getInstance().taskFailed(context, taskId, reason);
                break;
            }

            //文件被删除
            if (!new File(record.getFile()).exists()) {
                LOG("slice failed file not exists:" + record);
                UploadTaskManager.getInstance().taskFailed(context, taskId, "文件已被删除");
                break;
            }

            mUploadingQueue.add(record);
            RequestResult<String> result = uploadSection(context, record);
            mUploadingQueue.remove(record);

            if (result.isSuccess()) {
                LOG("slice upload success:" + record);
                sliceRecordDao.delete(record);
            } else {
                LOG("slice upload failed:" + record);
                if (result.getCode() == RequestManager.RESPONSE_OTHER_ERROR_CODE) {
                    UploadTaskManager.getInstance().taskFailed(context, taskId, "任务失败:" + result.getMsg());
                    failed = true;
                } else {
                    record.increaseUploadCount();
                    sliceRecordDao.update(record);
                    UploadTaskManager.getInstance().delayStart();
                }
                break;
            }
        }
        return failed;
    }

    private RequestResult<String> uploadSection(Context context, SliceRecord record) {
        LOG("upload section record:" + record);

        int sliceIndex = record.getSliceId() + 1;
        String taskId = record.getTaskId();
        String fileSize = LoggerUtils.formatFileSize(record.getFileSize());
        RequestManager.uploadTaskStatus(
                taskId,
                4,
                "文件大小:" + fileSize +
                        ",正在上传第" + sliceIndex + "/" + record.getSliceCount() + "个分片");
        String networkType = LoggerUtils.getNetworkType(context);
        RequestResult<String> result =
                RequestManager.uploadSectionFile(taskId,
                        new File(record.getFile()), networkType,
                        record.getSliceId(), record.getStartPos(), record.getEndPos());
        boolean success = result.isSuccess();
        if (!success && result.getCode() != RequestManager.RESPONSE_OTHER_ERROR_CODE) {
            RequestManager.uploadTaskStatus(
                    taskId, 4,
                    "文件大小:" + fileSize + ",第" + sliceIndex + "/" + record.getSliceCount() +
                            "个分片上传失败(" + result.getMsg() + "),即将进行第" + (record.getUploadCount() + 1) + "次重试");
        }
        return result;
    }
}
