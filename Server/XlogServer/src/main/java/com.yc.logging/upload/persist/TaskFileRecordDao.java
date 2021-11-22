package com.yc.logging.upload.persist;


import android.arch.persistence.room.*;
import android.support.annotation.RestrictTo;

import java.util.List;

@RestrictTo(RestrictTo.Scope.LIBRARY)
@Dao
public interface TaskFileRecordDao {

    @Query("SELECT * FROM TaskFileRecord WHERE taskId = :taskId")
    List<TaskFileRecord> getRecordsByTaskId(String taskId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(TaskFileRecord record);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAll(List<TaskFileRecord> records);

    @Delete()
    void delete(TaskFileRecord record);

    @Query("DELETE FROM TaskFileRecord WHERE taskId = :taskId")
    void deleteByTaskId(String taskId);

    @Update
    void update(TaskFileRecord record);
}