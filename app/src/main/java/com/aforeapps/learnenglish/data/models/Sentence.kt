package com.aforeapps.learnenglish.data.models

import com.google.gson.annotations.SerializedName

data class Sentence(
    val id: Int,
    val words: String,
    @SerializedName("start_time_sec") val startTimeSec: Long,
    @SerializedName("end_time_sec") val endTimeSec: Long,
    var isPlaying: Boolean = false,
) {
}