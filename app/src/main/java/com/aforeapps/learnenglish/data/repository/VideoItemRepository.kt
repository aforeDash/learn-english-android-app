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
                    networkVideoData.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<VideoData>,
                    response: retrofit2.Response<VideoData>,
                ) {
                    if (response.isSuccessful) {
                        val videoData = response.body()
                        videoData?.let {
                            networkVideoData.value = (it)
                        } ?: {
                            networkVideoData.value = null
                        }
                    } else {
                        networkVideoData.value = null
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
                    networkFullVideoItem.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<SingleVideoData>,
                    response: retrofit2.Response<SingleVideoData>,
                ) {
                    if (response.isSuccessful) {
                        val videoItem = response.body()
                        videoItem?.let {
                            networkFullVideoItem.value = (it.videoItem)
                        } ?: {
                            networkFullVideoItem.value = null
                        }
                    } else {
                        networkFullVideoItem.value = null
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
                    networkFullVideoItem.value = null
                }

                override fun onResponse(
                    call: retrofit2.Call<SingleVideoData>,
                    response: retrofit2.Response<SingleVideoData>,
                ) {
                    if (response.isSuccessful) {
                        val videoItem = response.body()
                        videoItem?.let {
                            networkFullVideoItem.value = (it.videoItem)
                        } ?: {
                            networkFullVideoItem.value = null
                        }
                    }
                    networkFullVideoItem.value = null
                }
            })

        return networkFullVideoItem
    }
}