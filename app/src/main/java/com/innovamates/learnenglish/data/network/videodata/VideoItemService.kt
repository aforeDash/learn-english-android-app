package com.innovamates.learnenglish.data.network.videodata

import com.google.gson.JsonObject
import com.innovamates.learnenglish.data.models.CategoryData
import com.innovamates.learnenglish.data.models.SingleVideoData
import com.innovamates.learnenglish.data.models.VideoData
import com.innovamates.learnenglish.data.models.VideoItem
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface VideoItemService {
    @GET("/api/v1/categories/{id}/videos")
    fun getVideoItems(@Path("id") categoryId: Int): Call<VideoData>

    @GET("/api/v1/videos/{id}")
    fun getFullVideoItem(@Path("id") videoId: Int): Call<SingleVideoData>
}