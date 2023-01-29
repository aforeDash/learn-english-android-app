package com.innovamates.learnenglish.data.database.category

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.innovamates.learnenglish.data.models.Category
import com.innovamates.learnenglish.data.models.Sentence
import com.innovamates.learnenglish.data.models.SubCategory
import com.innovamates.learnenglish.data.models.VideoItem

/**
 * DatabaseCategory represents a video entity in the database
 */

@Entity
data class DbCategory constructor(
    @PrimaryKey
    val id: Int,
    val is_subcategory: Int,
    val name: String,
    val order: Int,
    val parent_id: Int,
    val sub_categories: List<SubCategory>,
    val updated_at: String,
    val created_at: String,
)

/**
 * Map DatabaseCategory to domain entities
 */
fun List<DbCategory>.asDomainModel(): List<Category> {
    return map {
        Category(
            id = it.id,
            is_subcategory = it.is_subcategory,
            name = it.name,
            order = it.order,
            parent_id = it.parent_id,
            sub_categories = it.sub_categories,
            updated_at = it.updated_at,
            created_at = it.created_at
        )
    }
}
