

package com.yc.todoappmvvm.data.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.yc.todoappmvvm.data.Task;

/**
 * The Room Database that contains the Task table.
 */
@Database(entities = {Task.class}, version = 1)
public abstract class ToDoDatabase extends RoomDatabase {

    private static ToDoDatabase INSTANCE;

    public abstract TasksDao taskDao();

    private static final Object sLock = new Object();

    public static ToDoDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ToDoDatabase.class, "TasksMvvmBind.db")
                        .build();
            }
            return INSTANCE;
        }
    }

}
