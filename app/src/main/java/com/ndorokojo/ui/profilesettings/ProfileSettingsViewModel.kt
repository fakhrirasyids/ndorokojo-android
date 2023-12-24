package com.ndorokojo.ui.profilesettings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ndorokojo.data.models.District
import com.ndorokojo.data.models.ProfileInfo
import com.ndorokojo.data.models.Province
import com.ndorokojo.data.models.Regency
import com.ndorokojo.data.models.Village
import com.ndorokojo.data.repo.AuthRepository
import com.ndorokojo.utils.Result
import com.ndorokojo.utils.UserPreferences
import kotlinx.coroutines.launch

class ProfileSettingsViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val responseMessage = MutableLiveData<String>()

    fun logout() = authRepository.logoutUser()

    fun clearPreferences() {
        viewModelScope.launch {
            userPreferences.clearPreferences()
        }
    }
}