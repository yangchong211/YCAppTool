package com.yc.jetpack.study.room

import android.app.Application
import androidx.lifecycle.LiveData
import com.yc.jetpack.study.room.WordRoomDatabase.Companion.databaseWriteExecutor
import com.yc.jetpack.study.room.WordRoomDatabase.Companion.getDatabase

internal class WordRepository(application: Application?) {

    private var mWordDao: WordDao ?= null
    private var allWords: LiveData<List<Word?>?>?

    fun insert(word: Word?) {
        databaseWriteExecutor.execute {
            mWordDao?.insert(word)
            //allWords = mWordDao?.alphabetizedWords
        }
    }

    fun remove(word: Word?) {
        databaseWriteExecutor.execute {
            mWordDao?.delete(word)
            //allWords = mWordDao?.alphabetizedWords
        }
    }

    fun getAllWords(): LiveData<List<Word?>?>? {
        return allWords
    }

    init {
        val db = getDatabase(application!!)
        mWordDao = db?.wordDao()
        allWords = mWordDao?.alphabetizedWords
    }
}