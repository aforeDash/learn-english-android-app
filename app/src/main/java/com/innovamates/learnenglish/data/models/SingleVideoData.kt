package com.innovamates.learnenglish.data.models


import com.google.gson.annotations.SerializedName

data class SingleVideoData(
    @SerializedName("data") val videoItem: VideoItem? = null,
    val success: Boolean,
)