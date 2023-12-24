package com.ndorokojo.data.repo

import androidx.lifecycle.liveData
import com.ndorokojo.data.remote.ApiService
import com.ndorokojo.utils.Result

class AuthRepository(
    private val apiService: ApiService
) {
    fun loginUser(username: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val loginUser = apiService.loginUser(username, password)
            emit(Result.Success(loginUser))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun registerUser(
        username: String,
        fullname: String,
        email: String,
        password: String,
    ) = liveData {
        emit(Result.Loading)
        try {
            val registerUser = apiService.registerUser(username, fullname, email, password)
            emit(Result.Success(registerUser))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getProfileInfo() = liveData {
        emit(Result.Loading)
        try {
            val profileInfoResponse = apiService.getProfileInfo()
            emit(Result.Success(profileInfoResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun updateUser(
        fullname: String,
        username: String,
        email: String,
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
    ) = liveData {
        emit(Result.Loading)
        try {
            val updateUser = apiService.updateUser(
                fullname,
                username,
                email,
                phone,
                address,
                occupation,
                gender,
                age,
                kelompokTernak,
                provinceId,
                regencyId,
                districtId,
                villageId
            )
            emit(Result.Success(updateUser))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun logoutUser() = liveData {
        emit(Result.Loading)
        try {
            val logoutResponse = apiService.logoutUser()
            emit(Result.Success(logoutResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllProvinces() = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllProvinces()
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllRegencies(provinceId: Int) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllRegencies(provinceId)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllDistricts(regencyId: Int) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllDistricts(regencyId)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllVillages(districtId: Int) = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getAllVillages(districtId)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }
}