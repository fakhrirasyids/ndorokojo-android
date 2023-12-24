package com.ndorokojo.ui.buy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ndorokojo.data.remote.ApiService
import com.ndorokojo.data.repo.AuthRepository
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.utils.UserPreferences

class BuyViewModelFactory private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BuyViewModel::class.java)) {
            return BuyViewModel(TernakRepository(apiService), userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: BuyViewModelFactory? = null

        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ) = INSTANCE ?: synchronized(this) {
            val instance = BuyViewModelFactory(apiService, userPreferences)
            INSTANCE = instance
            instance
        }
    }
}