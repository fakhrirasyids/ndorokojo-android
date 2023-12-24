package com.ndorokojo.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ndorokojo.data.remote.ApiService
import com.ndorokojo.data.repo.AuthRepository
import com.ndorokojo.utils.UserPreferences

class AuthViewModelFactory private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(AuthRepository(apiService), userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthViewModelFactory? = null

        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences,
        ) = INSTANCE ?: synchronized(this) {
            val instance = AuthViewModelFactory(apiService, userPreferences)
            INSTANCE = instance
            instance
        }
    }
}