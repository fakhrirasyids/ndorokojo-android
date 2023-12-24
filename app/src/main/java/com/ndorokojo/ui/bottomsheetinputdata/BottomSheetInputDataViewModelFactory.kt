package com.ndorokojo.ui.bottomsheetinputdata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ndorokojo.data.remote.ApiService
import com.ndorokojo.data.repo.AuthRepository
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.ui.bottomsheetinputdata.fragments.StoreTernakViewModel
import com.ndorokojo.ui.bottomsheetinputdata.fragments.profile.ProfileViewModel
import com.ndorokojo.utils.UserPreferences

class BottomSheetInputDataViewModelFactory private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            ProfileViewModel(AuthRepository(apiService), userPreferences) as T
        } else if (modelClass.isAssignableFrom(StoreTernakViewModel::class.java)) {
            StoreTernakViewModel(
                AuthRepository(apiService),
                TernakRepository(apiService), userPreferences
            ) as T
        } else
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: BottomSheetInputDataViewModelFactory? = null

        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences,
        ) = INSTANCE ?: synchronized(this) {
            val instance = BottomSheetInputDataViewModelFactory(apiService, userPreferences)
            INSTANCE = instance
            instance
        }
    }
}