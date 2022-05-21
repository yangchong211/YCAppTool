package com.yc.jetpack.study.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * 第三步：创建数据库
 * @Database注解声明当前是一个数据库文件，注解中entities变量声明数据库中的表（实体），以及其他的例如版本等变量。
 * 同时，获取的Dao也必须在数据库类中。完成之后，点击build目录下的make project，系统就会自动帮我创建AppDataBase和xxxDao的实现类。
 */
@Database(entities = [Word::class], version = 1, exportSchema = false)
internal abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null
        private const val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        fun getDatabase(context: Context): WordRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(WordRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            WordRoomDatabase::class.java, "word_database"
                        )
                            .addCallback(sRoomDatabaseCallback)
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        private val sRoomDatabaseCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                databaseWriteExecutor.execute {
                    val dao = INSTANCE?.wordDao()
                    dao?.deleteAll()
                    var word = Word("yangchong")
                    dao?.insert(word)
                    word = Word("逗比")
                    dao?.insert(word)
                }
            }
        }
    }
}