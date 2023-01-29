package com.innovamates.learnenglish.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.innovamates.learnenglish.data.database.LearnEnglishDb
import com.innovamates.learnenglish.data.database.videoitem.asDomainModel
import com.innovamates.learnenglish.data.models.VideoItem
import com.innovamates.learnenglish.utils.FakeVideoItemGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideoItemRepository(private val database: LearnEnglishDb) {

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