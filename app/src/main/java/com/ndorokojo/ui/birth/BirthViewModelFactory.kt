package com.ndorokojo.ui.birth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ndorokojo.data.remote.ApiService
import com.ndorokojo.data.repo.AuthRepository
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.utils.UserPreferences

class BirthViewModelFactory private constructor(
    private val apiService: ApiService,
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BirthViewModel::class.java)) {
            return BirthViewModel(AuthRepository(apiService), TernakRepository(apiService)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: BirthViewModelFactory? = null

        fun getInstance(
            apiService: ApiService,
        ) = INSTANCE ?: synchronized(this) {
            val instance = BirthViewModelFactory(apiService)
            INSTANCE = instance
            instance
        }
    }
}