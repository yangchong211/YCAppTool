package com.ycbjie.todo.data;

import android.util.Log;

import com.blankj.utilcode.util.FileUtils;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.db.realm.RealmWorkDoHelper;
import com.ycbjie.library.db.cache.CacheTaskDetailEntity;
import com.ycbjie.library.utils.time.DateUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BackupManager {

    private BackupManager() {}

    public static Observable<Boolean> backup() {
        final File src = new File(Constant.EXTERNAL_STORAGE_DIRECTORY + Constant.DATABASE_FILE_PATH_FOLDER, Constant.DATABASE_FILE_PATH_FILE_NAME);
        File desDir = new File(Constant.EXTERNAL_STORAGE_DIRECTORY + Constant.DATABASE_FILE_BACKUP_PATH_FOLDER);
        desDir.mkdirs();
        final File des = new File(desDir, src.getName());

        return Observable
                .fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return copyFile(src, des);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static Observable<Boolean> export() {
        return Observable
                .fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        List<CacheTaskDetailEntity> allTask = RealmWorkDoHelper.getInstance().findAllTask();
                        File desDir = new File(Constant.EXTERNAL_STORAGE_DIRECTORY + Constant.DATABASE_FILE_EXPORT_PATH_FOLDER);
                        desDir.mkdirs();
                        File desFile = new File(desDir, System.currentTimeMillis() + ".txt");
                        desFile.createNewFile();
                        ItemStringCreator<CacheTaskDetailEntity> creator = new ItemStringCreator<CacheTaskDetailEntity>() {
                            @Override
                            public String create(int position, CacheTaskDetailEntity taskDetailEntity) {
                                return DateUtils.formatDateTime(taskDetailEntity.getTimeStamp()) +
                                        "\t星期" +
                                        DateUtils.weekNumberToChinese(taskDetailEntity.getDayOfWeek()) +
                                        '\t' +
                                        (taskDetailEntity.getState() == Constant.TaskState.FINISHED ? "已完成" : "待完成") +
                                        "\n标题: " +
                                        taskDetailEntity.getTitle() +
                                        "\n正文: " +
                                        taskDetailEntity.getContent() +
                                        '\n';
                            }
                        };
                        return exportToFile(desFile, allTask, creator);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Observable<Boolean> recovery() {
        final File des = new File(Constant.EXTERNAL_STORAGE_DIRECTORY + Constant.DATABASE_FILE_PATH_FOLDER, Constant.DATABASE_FILE_PATH_FILE_NAME);
        final File src = new File(Constant.EXTERNAL_STORAGE_DIRECTORY + Constant.DATABASE_FILE_BACKUP_PATH_FOLDER, des.getName());
        return Observable
                .fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return copyFile(src, des);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    private static boolean copyFile(File src, File des) {
        if (src == null || des == null) throw new NullPointerException("file is null");
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(src);
            outputStream = new FileOutputStream(des);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException ignored) {
            }
        }

        return true;
    }


    private static <T> boolean exportToFile(File des, List<T> list, ItemStringCreator<T> creator) {
        if (des == null || list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Des file or list is null");
        }
        BufferedWriter bw = null;
        if (creator == null){
            creator = new ItemStringCreator<T>() {
                @Override
                public String create(int position, T t) {
                    return t.toString();
                }
            };
        }
        try {
            bw = new BufferedWriter(new FileWriter(des));
            for (int i = 0; i < list.size(); i++) {
                String line = creator.create(i, list.get(i));
                bw.write(line);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            Log.e(FileUtils.class.getSimpleName(), e.getMessage());
            return false;
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException ignored) {

            }
        }
        return true;
    }

    interface ItemStringCreator<T> {
        String create(int position, T t);
    }


}
