package com.innovamates.learnenglish.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.innovamates.learnenglish.repository.database.LearnEnglishDatabase
import com.innovamates.learnenglish.repository.database.videoitem.asDomainModel
import com.innovamates.learnenglish.models.videoitem.VideoItem
import com.innovamates.learnenglish.utils.FakeVideoItemGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoItemsRepository(private val database: LearnEnglishDatabase) {

    val videoItems: LiveData<List<VideoItem>> =
        Transformations.map(database.videoItemDao.getVideoItems()) {
            it.asDomainModel()
        }

    suspend fun refreshVideoItems() {
//        withContext(Dispatchers.IO) {
//            val videoItems = LearnEnglishNetwork.videoItems.getVideoItems()
//            database.videoItemsDao.insertAll(videoItems)
//        }

        withContext(Dispatchers.IO) {
            val vItems = FakeVideoItemGenerator.getVideoItems()
            database.videoItemDao.insert(vItems)
        }
    }
}