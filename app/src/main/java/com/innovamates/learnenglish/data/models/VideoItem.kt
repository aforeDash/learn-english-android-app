package com.innovamates.learnenglish.data.models

data class VideoItem(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnailURL: String,
    val videoId: String,
    val videoStartTimeSec : Long,
    val videoEndTimeSec : Long,
    val sentences : List<Sentence>,
    var isSelected: Boolean
)


