package com.aforeapps.learnenglish.data.network.videodata

import com.aforeapps.learnenglish.data.models.SingleVideoData
import com.aforeapps.learnenglish.data.models.VideoData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface VideoItemService {
    @GET("/api/v1/categories/{id}/videos")
    fun getVideoItems(@Path("id") categoryId: Int): Call<VideoData>

    @GET("/api/v1/videos/{id}")
    fun getFullVideoItem(@Path("id") videoId: Int): Call<SingleVideoData>

    @GET("api/v1/random-video")
    fun getRandomVideoItem(): Call<SingleVideoData>
}