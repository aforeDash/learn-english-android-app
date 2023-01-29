package com.innovamates.learnenglish.data.database.videoitem

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.innovamates.learnenglish.data.models.Sentence
import com.innovamates.learnenglish.data.models.VideoItem

/**
 * DatabaseVideoItem represents a video entity in the database
 */

@Entity
data class DbVideoItem constructor(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val thumbnailURL: String,
    val videoId: String,
    val videoStartTimeSec: Long,
    val videoEndTimeSec: Long,
    val sentences: List<Sentence>,
)

/**
 * Map DatabaseVideoItem to domain entities
 */
fun List<DbVideoItem>.asDomainModel(): List<VideoItem> {
    return map {
        VideoItem(
            id = it.id,
            title = it.title,
            description = it.description,
            thumbnailURL = it.thumbnailURL,
            videoId = it.videoId,
            videoStartTimeSec = it.videoStartTimeSec,
            videoEndTimeSec = it.videoEndTimeSec,
            sentences = it.sentences,
            isSelected = false
        )

    }
}
