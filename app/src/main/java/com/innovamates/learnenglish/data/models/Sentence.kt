package com.innovamates.learnenglish.data.models

data class Sentence(
    val id: Int,
    val sentence: String,
    val translation: String,
    val startTimeSec: Long,
    val endTimeSec: Long,
    var isPlaying: Boolean = false
) {
}