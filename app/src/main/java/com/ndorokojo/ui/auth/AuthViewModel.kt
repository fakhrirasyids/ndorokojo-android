package com.ndorokojo.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ndorokojo.data.repo.AuthRepository
import com.ndorokojo.utils.UserPreferences
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val responseMessage = MutableLiveData<String>()

    val isLoginUsernameFilled = MutableLiveData(false)
    val isLoginPasswordFilled = MutableLiveData(false)

    val isRegisterUsernameFilled = MutableLiveData(false)
    val isRegisterFullnameFilled = MutableLiveData(false)
    val isRegisterEmailFilled = MutableLiveData(false)
    val isRegisterPasswordFilled = MutableLiveData<Boolean>()

    fun loginUser(username: String, password: String) =
        authRepository.loginUser(username, password)

    fun registerUser(username: String, fullname: String, email: String, password: String) =
        authRepository.registerUser(username, fullname, email, password)

    fun savePreferences(
        accessToken: String,
        username: String,
        fullname: String,
        email: String,
        code: String,
        phone: String,
        address: String,
        occupation: String,
        gender: String,
        age: String,
        kelompokTernak: String,
        provinceId: String,
        regencyId: String,
        districtId: String,
        villageId: String,
        isProfileComplete: Boolean
    ) {
        viewModelScope.launch {
            userPreferences.savePreferences(
                accessToken,
                username,
                fullname,
                email,
                code,
                phone,
                address,
                occupation,
                gender,
                age,
                kelompokTernak,
                provinceId,
                regencyId,
                districtId,
                villageId,
                isProfileComplete
            )
        }
    }

    fun getAccessToken() = userPreferences.getAccessToken().asLiveData()
    fun getIsProfileComplete() = userPreferences.getIsProfileComplete().asLiveData()
}