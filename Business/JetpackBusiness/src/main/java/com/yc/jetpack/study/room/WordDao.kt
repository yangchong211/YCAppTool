package com.yc.jetpack.study.room

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * 第二步：创建Dao
 * 如果想声明一个Dao，只要在抽象类或者接口加一个@Dao注解就行。
 */
@Dao
interface WordDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @get:Query("SELECT * FROM word_table ORDER BY word ASC")
    var alphabetizedWords: LiveData<List<Word?>?>?

    /**
     * 插入数据
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(word: Word?)

    @Query("DELETE FROM word_table")
    fun deleteAll()

    @Delete
    fun delete(word: Word?)
}