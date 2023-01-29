package com.innovamates.learnenglish.data.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.innovamates.learnenglish.data.network.networkdata.DataService
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

    val dataService: DataService =
        retrofit.create(DataService::class.java)

}