package com.ndorokojo.ui.sell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ndorokojo.data.remote.ApiService
import com.ndorokojo.data.repo.AuthRepository
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.utils.UserPreferences

class SellViewModelFactory private constructor(
    private val apiService: ApiService
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SellViewModel::class.java)) {
            return SellViewModel(TernakRepository(apiService)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: SellViewModelFactory? = null

        fun getInstance(
            apiService: ApiService
        ) = INSTANCE ?: synchronized(this) {
            val instance = SellViewModelFactory(apiService)
            INSTANCE = instance
            instance
        }
    }
}