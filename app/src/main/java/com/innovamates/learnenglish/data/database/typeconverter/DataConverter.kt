package com.innovamates.learnenglish.data.database.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.innovamates.learnenglish.data.models.Sentence
import com.innovamates.learnenglish.data.models.SubCategory
import com.innovamates.learnenglish.data.models.VideoItem

class DataConverter {

    @TypeConverter
    fun fromSentenceList(value: List<Sentence>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Sentence>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toSentenceList(value: String): List<Sentence> {
        val gson = Gson()
        val type = object : TypeToken<List<Sentence>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toSubCategoryList(value: String): List<SubCategory> {
        val gson = Gson()
        val type = object : TypeToken<List<SubCategory>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromSubCategoryList(value: List<SubCategory>): String {
        val gson = Gson()
        val type = object : TypeToken<List<SubCategory>>() {}.type
        return gson.toJson(value, type)
    }

    companion object {
        fun fromVideoItemList(value: List<VideoItem>): String {
            val gson = Gson()
            val type = object : TypeToken<List<VideoItem>>() {}.type
            return gson.toJson(value, type)
        }

        fun toVideoItemList(value: String): List<VideoItem> {
            val gson = Gson()
            val type = object : TypeToken<List<VideoItem>>() {}.type
            return gson.fromJson(value, type)
        }

        fun toSubCategoryList(value: String): List<SubCategory> {
            val gson = Gson()
            val type = object : TypeToken<List<SubCategory>>() {}.type
            return gson.fromJson(value, type)
        }

        fun fromSubCategoryList(value: List<SubCategory>): String {
            val gson = Gson()
            val type = object : TypeToken<List<SubCategory>>() {}.type
            return gson.toJson(value, type)
        }
    }
}
