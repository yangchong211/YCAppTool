package com.yc.logging.upload.persist;


import androidx.room.*;
import androidx.annotation.RestrictTo;

import java.util.List;

@RestrictTo(RestrictTo.Scope.LIBRARY)
@Dao
public interface TaskRecordDao {

    @Query("select * FROM TaskRecord")
    List<TaskRecord> getRecordList();

    @Query("select * FROM TaskRecord WHERE taskId = :taskId")
    TaskRecord getRecordsByTaskId(String taskId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(TaskRecord record);

    @Delete()
    void delete(TaskRecord record);

    @Query("DELETE FROM TaskRecord WHERE taskId = :taskId")
    void deleteByTaskId(String taskId);
}