package com.ndorokojo.ui.main.tambahkandang

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ndorokojo.data.remote.ApiService
import com.ndorokojo.data.repo.AuthRepository
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.ui.bottomsheetinputdata.fragments.StoreTernakViewModel
import com.ndorokojo.ui.bottomsheetinputdata.fragments.profile.ProfileViewModel
import com.ndorokojo.utils.UserPreferences

class BottomSheetTambahKandangViewModelFactory private constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(StoreKandangViewModel::class.java)) {
            StoreKandangViewModel(
                AuthRepository(apiService),
                TernakRepository(apiService),
                userPreferences
            ) as T
        } else
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: BottomSheetTambahKandangViewModelFactory? = null

        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences,
        ) = INSTANCE ?: synchronized(this) {
            val instance = BottomSheetTambahKandangViewModelFactory(apiService, userPreferences)
            INSTANCE = instance
            instance
        }
    }
}