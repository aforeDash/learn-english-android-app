package com.innovamates.learnenglish.data.models

data class SubCategory(
    val created_at: String,
    val id: Int,
    val is_subcategory: Int,
    val name: String,
    val order: Int,
    val parent_id: Int,
    val updated_at: String
)