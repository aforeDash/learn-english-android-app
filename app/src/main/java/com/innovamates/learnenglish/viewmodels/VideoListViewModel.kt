package com.innovamates.learnenglish.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.innovamates.learnenglish.data.models.VideoData
import com.innovamates.learnenglish.data.models.VideoItem
import com.innovamates.learnenglish.data.repository.VideoItemRepository
import java.io.IOException

class VideoListViewModel(application: Application) : AndroidViewModel(application) {

    private val videosRepository = VideoItemRepository()

    fun getVideoData(id: Int): MutableLiveData<VideoData> {
        return videosRepository.refreshVideoItems(id)
    }

    fun getFullVideoData(id: Int): MutableLiveData<VideoItem> {
        return videosRepository.getFullVideoData(id)
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VideoListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return VideoListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}