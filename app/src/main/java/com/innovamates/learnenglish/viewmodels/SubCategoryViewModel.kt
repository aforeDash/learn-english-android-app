package com.innovamates.learnenglish.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.innovamates.learnenglish.data.repository.CategoryRepository
import com.innovamates.learnenglish.data.database.getDatabase
import com.innovamates.learnenglish.data.repository.VideoItemRepository
import kotlinx.coroutines.launch
import java.io.IOException

class SubCategoryViewModel(application: Application) : AndroidViewModel(application) {

    private var _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown


    private val videosRepository = VideoItemRepository(getDatabase(application))

    val videoItemList = videosRepository.videoItems


    init {
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                videosRepository.refreshVideoItems()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if (videoItemList.value.isNullOrEmpty())
                    _eventNetworkError.value = true
            }
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SubCategoryViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SubCategoryViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}