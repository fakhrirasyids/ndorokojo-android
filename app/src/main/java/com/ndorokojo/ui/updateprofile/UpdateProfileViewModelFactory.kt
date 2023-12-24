package com.ndorokojo.ui.updateprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ndorokojo.data.remote.ApiService
import com.ndorokojo.data.repo.AuthRepository
import com.ndorokojo.utils.UserPreferences

class UpdateProfileViewModelFactory private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateProfileViewModel::class.java)) {
            return UpdateProfileViewModel(AuthRepository(apiService), userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: UpdateProfileViewModelFactory? = null

        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences,
        ) = INSTANCE ?: synchronized(this) {
            val instance = UpdateProfileViewModelFactory(apiService, userPreferences)
            INSTANCE = instance
            instance
        }
    }
}