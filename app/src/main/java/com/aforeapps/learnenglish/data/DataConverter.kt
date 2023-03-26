package com.aforeapps.learnenglish.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.aforeapps.learnenglish.data.models.SubCategory
import com.aforeapps.learnenglish.data.models.VideoItem

class DataConverter {

    companion object {
        fun fromVideoItem(value: VideoItem): String {
            val gson = Gson()
            val type = object : TypeToken<VideoItem>() {}.type
            return gson.toJson(value, type)
        }

        fun toVideoItem(value: String): VideoItem {
            val gson = Gson()
            val type = object : TypeToken<VideoItem>() {}.type
            return gson.fromJson(value, type)
        }

        fun fromSubCategory(value: SubCategory): String {
            val gson = Gson()
            val type = object : TypeToken<SubCategory>() {}.type
            return gson.toJson(value, type)
        }

        fun toSubCategory(value: String): SubCategory {
            val gson = Gson()
            val type = object : TypeToken<SubCategory>() {}.type
            return gson.fromJson(value, type)
        }

        fun fromSubCategoryList(value: List<SubCategory>): String {
            val gson = Gson()
            val type = object : TypeToken<List<SubCategory>>() {}.type
            return gson.toJson(value, type)
        }

        fun toSubCategoryList(value: String): List<SubCategory> {
            val gson = Gson()
            val type = object : TypeToken<List<SubCategory>>() {}.type
            return gson.fromJson(value, type)
        }
    }
}
