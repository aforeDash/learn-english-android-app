package com.aforeapps.learnenglish.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aforeapps.learnenglish.data.models.VideoItem
import com.aforeapps.learnenglish.data.repository.VideoItemRepository

class PlayerViewModel : ViewModel() {

    private val videosRepository = VideoItemRepository()

    fun getRandomVideoData(): MutableLiveData<VideoItem> {
        return videosRepository.getRandomVideoData()
    }
}