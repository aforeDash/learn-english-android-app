package com.innovamates.learnenglish.data.database

import android.content.Context
import androidx.room.*
import com.innovamates.learnenglish.data.database.category.CategoryDao
import com.innovamates.learnenglish.data.database.category.DbCategory
import com.innovamates.learnenglish.data.database.typeconverter.DataConverter
import com.innovamates.learnenglish.data.database.videoitem.DbVideoItem
import com.innovamates.learnenglish.data.database.videoitem.VideoItemDao

@Database(entities = [DbVideoItem::class, DbCategory::class], version = 1)
@TypeConverters(DataConverter::class)
abstract class LearnEnglishDb : RoomDatabase() {
    abstract val videoItemDao: VideoItemDao
    abstract val categoryDao: CategoryDao
}

private lateinit var INSTANCE: LearnEnglishDb

fun getDatabase(context: Context): LearnEnglishDb {
    synchronized(LearnEnglishDb::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                LearnEnglishDb::class.java,
                "learn_english"
            ).build()
        }
    }
    return INSTANCE
}