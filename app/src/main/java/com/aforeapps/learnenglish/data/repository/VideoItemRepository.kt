package com.aforeapps.learnenglish.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aforeapps.learnenglish.data.models.SingleVideoData
import com.aforeapps.learnenglish.data.models.VideoData
import com.aforeapps.learnenglish.data.models.VideoItem
import com.aforeapps.learnenglish.data.network.LearnEnglishNetwork

class VideoItemRepository {
    fun refreshVideoItems(id: Int): MutableLiveData<VideoData> {
        val networkVideoData: MutableLiveData<VideoData> = MutableLiveData()

        LearnEnglishNetwork.videoItemService.getVideoItems(id)
            .enqueue(object : retrofit2.Callback<VideoData> {
                override fun onFailure(call: retrofit2.Call<VideoData>, t: Throwable) {
                    Log.e("VideoItemRepository", "Failed to get video items", t)
                    networkVideoData.postValue(null)
                }

                override fun onResponse(
                    call: retrofit2.Call<VideoData>,
                    response: retrofit2.Response<VideoData>,
                ) {
                    if (response.isSuccessful) {
                        val videoData = response.body()
                        videoData?.let {
                            networkVideoData.postValue(it)
                        } ?: networkVideoData.postValue(null)
                    } else {
                        networkVideoData.postValue(null)
                    }

                }
            })

        return networkVideoData
    }

    fun getFullVideoData(id: Int): MutableLiveData<VideoItem> {
        val networkFullVideoItem: MutableLiveData<VideoItem> = MutableLiveData()

        LearnEnglishNetwork.videoItemService.getFullVideoItem(id)
            .enqueue(object : retrofit2.Callback<SingleVideoData> {
                override fun onFailure(call: retrofit2.Call<SingleVideoData>, t: Throwable) {
                    Log.e("VideoItemRepository", "Failed to get video items", t)
                    networkFullVideoItem.postValue(null)
                }

                override fun onResponse(
                    call: retrofit2.Call<SingleVideoData>,
                    response: retrofit2.Response<SingleVideoData>,
                ) {
                    if (response.isSuccessful) {
                        val videoItem = response.body()
                        videoItem?.let {
                            networkFullVideoItem.postValue(it.videoItem)
                        } ?: networkFullVideoItem.postValue(null)
                    } else {
                        networkFullVideoItem.postValue(null)
                    }
                }
            })

        return networkFullVideoItem
    }

    fun getRandomVideoData(): MutableLiveData<VideoItem> {
        val networkFullVideoItem: MutableLiveData<VideoItem> = MutableLiveData()

        LearnEnglishNetwork.videoItemService.getRandomVideoItem()
            .enqueue(object : retrofit2.Callback<SingleVideoData> {
                override fun onFailure(call: retrofit2.Call<SingleVideoData>, t: Throwable) {
                    Log.e("VideoItemRepository", "Failed to get video items", t)
                    networkFullVideoItem.postValue(null)
                }

                override fun onResponse(
                    call: retrofit2.Call<SingleVideoData>,
                    response: retrofit2.Response<SingleVideoData>,
                ) {
                    if (response.isSuccessful) {
                        val videoItem = response.body()
                        videoItem?.let {
                            networkFullVideoItem.postValue(it.videoItem)
                        } ?: networkFullVideoItem.postValue(null)
                    }
                    networkFullVideoItem.postValue(null)
                }
            })

        return networkFullVideoItem
    }
}