package com.aforeapps.learnenglish.data.network.categorydata

import com.aforeapps.learnenglish.data.models.CategoryData
import retrofit2.Call
import retrofit2.http.GET

interface CategoryService {
    @GET("api/v1/categories")
    fun getCategories(): Call<CategoryData>
}