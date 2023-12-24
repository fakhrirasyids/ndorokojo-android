package com.ndorokojo.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ndorokojo.data.remote.ApiService
import com.ndorokojo.data.repo.AuthRepository
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.utils.UserPreferences

class MainViewModelFactory private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(TernakRepository(apiService), userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: MainViewModelFactory? = null

        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences,
        ) = INSTANCE ?: synchronized(this) {
            val instance = MainViewModelFactory(apiService, userPreferences)
            INSTANCE = instance
            instance
        }
    }
}