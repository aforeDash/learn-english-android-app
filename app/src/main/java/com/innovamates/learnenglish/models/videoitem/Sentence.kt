package com.innovamates.learnenglish.models.videoitem

data class Sentence(
    val id: Int,
    val sentence: String,
    val translation: String,
    val startTimeSec: Long,
    val endTimeSec: Long,
) {
}