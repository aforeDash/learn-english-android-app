package com.aforeapps.learnenglish.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.aforeapps.learnenglish.data.models.CategoryData
import com.aforeapps.learnenglish.data.repository.CategoryRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    fun getCategories(): MutableLiveData<CategoryData> {
        return CategoryRepository().getCategories()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}