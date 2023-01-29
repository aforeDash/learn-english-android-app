package com.innovamates.learnenglish.data.database.videoitem

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VideoItemDao {
    @Query("SELECT * FROM dbvideoitem")
    fun getVideoItems(): LiveData<List<DbVideoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(videoItems: List<DbVideoItem>)
}