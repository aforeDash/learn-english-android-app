package com.innovamates.learnenglish.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.innovamates.learnenglish.data.database.LearnEnglishDb
import com.innovamates.learnenglish.data.database.category.asDomainModel
import com.innovamates.learnenglish.data.models.Category
import com.innovamates.learnenglish.data.models.asDatabaseModel
import com.innovamates.learnenglish.data.network.LearnEnglishNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository(private val database: LearnEnglishDb) {

    val categories: LiveData<List<Category>> =
        Transformations.map(database.categoryDao.getCategories()) {
            it.asDomainModel()
        }

    suspend fun refreshCategories() {
        withContext(Dispatchers.IO) {
            val categories = LearnEnglishNetwork.dataService.getCategories()
            categories.data.forEach {
                database.categoryDao.insert(it.asDatabaseModel())
            }
        }
    }
}