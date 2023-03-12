package com.innovamates.learnenglish.data.models

import com.google.gson.annotations.SerializedName

data class CategoryData(
    @SerializedName("data") val categories: List<Category>? = null,
    val error: String,
    val success: Boolean,
)

data class Category(
    val created_at: String,
    val id: Int,
    val is_subcategory: Int,
    val name: String,
    val order: Int,
    val parent_id: Any,
    val sub_categories: List<SubCategory>,
    val updated_at: String,
)

data class SubCategory(
    val created_at: String,
    val id: Int,
    val is_subcategory: Int,
    val name: String,
    val order: Int,
    val parent_id: Int,
    val updated_at: String
)