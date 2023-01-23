package com.innovamates.learnenglish.repository.database.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.innovamates.learnenglish.models.videoitem.Sentence
import com.innovamates.learnenglish.models.videoitem.VideoItem

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
    }
}
