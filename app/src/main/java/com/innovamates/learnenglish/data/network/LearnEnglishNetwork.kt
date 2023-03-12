package com.innovamates.learnenglish.data.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.innovamates.learnenglish.data.models.VideoItem
import com.innovamates.learnenglish.data.network.categorydata.CategoryService
import com.innovamates.learnenglish.data.network.videodata.VideoItemService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LearnEnglishNetwork {

    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://learnenglish.aforedash.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val categoryService: CategoryService =
        retrofit.create(CategoryService::class.java)

    val videoItemService: VideoItemService =
        retrofit.create(VideoItemService::class.java)

}