package com.innovamates.learnenglish.data.database.category

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoryDao {
    @Query("SELECT * FROM dbcategory")
    fun getCategories(): LiveData<List<DbCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dbCategory: DbCategory)
}