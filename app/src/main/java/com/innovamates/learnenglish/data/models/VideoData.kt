package com.innovamates.learnenglish.data.models

import com.google.gson.annotations.SerializedName

data class VideoData(
    @SerializedName("data") val videoItems: List<VideoItem>? = null,
    val success: Boolean,
)

data class VideoItem(
    val category_id: Int,
    val created_at: String,
    val description: String,
    val endTimeSec: Int,
    val full_url: String,
    val id: Int,
    val is_active: Int,
    val startTimeSec: Int,
    val subcategory_id: Int,
    val thumbnail_url: String,
    val title: String,
    val updated_at: String,
    val video_url: String,
    val youtube_id: String,
    val sentences: List<Sentence>,
)