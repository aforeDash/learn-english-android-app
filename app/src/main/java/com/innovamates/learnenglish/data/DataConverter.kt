package com.innovamates.learnenglish.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.innovamates.learnenglish.data.models.Sentence
import com.innovamates.learnenglish.data.models.SubCategory
import com.innovamates.learnenglish.data.models.VideoItem

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
    }
}
