package com.aforeapps.learnenglish.data.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.aforeapps.learnenglish.data.network.categorydata.CategoryService
import com.aforeapps.learnenglish.data.network.videodata.VideoItemService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LearnEnglishNetwork {

    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val client = OkHttpClient.Builder()
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .callTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://learnenglish.aforedash.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val categoryService: CategoryService =
        retrofit.create(CategoryService::class.java)

    val videoItemService: VideoItemService =
        retrofit.create(VideoItemService::class.java)

}