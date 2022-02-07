package com.yc.logging.upload.persist;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.RestrictTo;

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