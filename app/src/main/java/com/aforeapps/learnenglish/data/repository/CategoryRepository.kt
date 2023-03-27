package com.aforeapps.learnenglish.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.aforeapps.learnenglish.data.models.CategoryData
import com.aforeapps.learnenglish.data.network.LearnEnglishNetwork
import retrofit2.Call
import retrofit2.Response

class CategoryRepository {


    fun getCategories(): MutableLiveData<CategoryData> {
        val networkCategoryData: MutableLiveData<CategoryData> = MutableLiveData()

        LearnEnglishNetwork.categoryService.getCategories()
            .enqueue(object : retrofit2.Callback<CategoryData> {
                override fun onFailure(call: Call<CategoryData>, t: Throwable) {
                    Log.e("CategoryRepository", "Failed to get categories", t)
                    networkCategoryData.postValue(null)
                }

                override fun onResponse(
                    call: Call<CategoryData>,
                    response: Response<CategoryData>,
                ) {
                    networkCategoryData.postValue(response.body())
                }
            })

        return networkCategoryData
    }

}