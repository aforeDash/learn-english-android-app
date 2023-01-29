package com.innovamates.learnenglish.data.models

import com.innovamates.learnenglish.data.database.category.DbCategory

data class Category(
    val created_at: String,
    val id: Int,
    val is_subcategory: Int,
    val name: String,
    val order: Int,
    val parent_id: Int,
    val sub_categories: List<SubCategory>,
    val updated_at: String,
)

fun Category.asDatabaseModel(): DbCategory {
    return DbCategory(
        created_at = this.created_at,
        id = this.id,
        is_subcategory = this.id,
        name = this.name,
        order = this.order,
        parent_id = this.parent_id,
        sub_categories = this.sub_categories,
        updated_at = this.updated_at
    )
}