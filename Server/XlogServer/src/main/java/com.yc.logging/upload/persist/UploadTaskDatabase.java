package com.yc.logging.upload.persist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.RestrictTo;

@RestrictTo(RestrictTo.Scope.LIBRARY)
@Database(
        entities = {TaskRecord.class, SliceRecord.class, TaskFileRecord.class},
        version = 2,
        exportSchema = false
)
public abstract class UploadTaskDatabase extends RoomDatabase {

    private static UploadTaskDatabase sInstance;

    public static UploadTaskDatabase initDatabase(Context context) {
        if (sInstance == null) {
            sInstance =
                    Room.databaseBuilder(
                            context.getApplicationContext(),
                            UploadTaskDatabase.class,
                            "log.db")
                            .build();
        }
        return sInstance;
    }

    public static UploadTaskDatabase getDatabase() {
        return sInstance;
    }

    public static void onDestroy() {
        sInstance = null;
    }

    public abstract TaskRecordDao getTaskRecordDao();

    public abstract SliceRecordDao getFileRecordDao();

    public abstract TaskFileRecordDao getTaskFileRecordDao();

}