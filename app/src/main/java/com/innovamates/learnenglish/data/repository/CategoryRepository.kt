package com.innovamates.learnenglish.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.innovamates.learnenglish.data.models.CategoryData
import com.innovamates.learnenglish.data.network.LearnEnglishNetwork
import retrofit2.Call
import retrofit2.Response

class CategoryRepository {


    fun getCategories(): MutableLiveData<CategoryData> {
        val networkCategoryData: MutableLiveData<CategoryData> = MutableLiveData()

        LearnEnglishNetwork.categoryService.getCategories()
            .enqueue(object : retrofit2.Callback<CategoryData> {
                override fun onFailure(call: Call<CategoryData>, t: Throwable) {
                    Log.e("CategoryRepository", "Failed to get categories", t)
                }

                override fun onResponse(
                    call: Call<CategoryData>,
                    response: Response<CategoryData>,
                ) {
                    if (response.isSuccessful) {
                        val categoryData = response.body()
                        if (categoryData != null) {
                            networkCategoryData.postValue(categoryData)
                        }
                    }
                }
            })

        return networkCategoryData
    }

}