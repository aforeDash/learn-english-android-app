package com.innovamates.learnenglish.repository.database

import android.content.Context
import androidx.room.*
import com.innovamates.learnenglish.repository.database.typeconverter.DataConverter
import com.innovamates.learnenglish.repository.database.videoitem.DatabaseVideoItem
import com.innovamates.learnenglish.repository.database.videoitem.VideoItemDao

@Database(entities = [DatabaseVideoItem::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class LearnEnglishDatabase : RoomDatabase() {
    abstract val videoItemDao: VideoItemDao
}

private lateinit var INSTANCE: LearnEnglishDatabase

fun getDatabase(context: Context): LearnEnglishDatabase {
    synchronized(LearnEnglishDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                LearnEnglishDatabase::class.java,
                "learn_english"
            ).build()
        }
    }
    return INSTANCE
}