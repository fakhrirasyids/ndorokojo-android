package com.ndorokojo.ui.detailkandang

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ndorokojo.data.remote.ApiService
import com.ndorokojo.data.repo.AuthRepository
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.utils.UserPreferences

class DetailKandangViewModelFactory private constructor(
    private val apiService: ApiService,
    private val kandangId: Int,
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailKandangViewModel::class.java)) {
            return DetailKandangViewModel(TernakRepository(apiService), kandangId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}