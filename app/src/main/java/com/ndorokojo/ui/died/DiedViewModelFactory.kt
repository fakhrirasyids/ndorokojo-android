package com.ndorokojo.ui.died

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ndorokojo.data.remote.ApiService
import com.ndorokojo.data.repo.AuthRepository
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.utils.UserPreferences

class DiedViewModelFactory constructor(
    private val apiService: ApiService
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiedViewModel::class.java)) {
            return DiedViewModel(TernakRepository(apiService)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}