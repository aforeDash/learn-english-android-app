package com.innovamates.learnenglish.repository.database.videoitem

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VideoItemDao {
    @Query("SELECT * FROM databasevideoitem")
    fun getVideoItems(): LiveData<List<DatabaseVideoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(videoItems: List<DatabaseVideoItem>)
}