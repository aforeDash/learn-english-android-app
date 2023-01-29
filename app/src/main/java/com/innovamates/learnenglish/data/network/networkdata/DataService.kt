package com.innovamates.learnenglish.data.network.networkdata

import retrofit2.http.GET

interface DataService {
    @GET("api/v1/categories")
    suspend fun getCategories(): NetworkData
}