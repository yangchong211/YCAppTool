package com.yc.xlogupload.persist;


import androidx.room.*;
import androidx.annotation.RestrictTo;

import java.util.List;

@RestrictTo(RestrictTo.Scope.LIBRARY)
@Dao
public interface SliceRecordDao {

    @Query("SELECT * FROM SliceRecord WHERE taskId = :taskId ORDER BY sliceId ASC")
    List<SliceRecord> getRecordsByTaskId(String taskId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(SliceRecord record);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAll(List<SliceRecord> records);

    @Delete
    void delete(SliceRecord record);

    @Query("DELETE FROM SliceRecord WHERE taskId = :taskId")
    void deleteByTaskId(String taskId);

    @Update
    void update(SliceRecord record);
}