package com.ndorokojo.ui.bottomsheetinputdata.fragments.profile

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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    fun getUserCode() = runBlocking { userPreferences.getCode().first() }
    fun getFullname() = runBlocking { userPreferences.getFullname().first() }
    fun getUserOccupation() = runBlocking { userPreferences.getOccupation().first() }
    fun getUserGender() = runBlocking { userPreferences.getGender().first() }
    fun getUserAge() = runBlocking { userPreferences.getAge().first() }
    fun getUserKelompokTernak() = runBlocking { userPreferences.getKelompokTernak().first() }
    fun getUserProvinceId() = runBlocking { userPreferences.getProvinceId().first() }
    fun getUserRegencyId() = runBlocking { userPreferences.getRegencyId().first() }
    fun getUserDistrictId() = runBlocking { userPreferences.getDistrictId().first() }
    fun getUserVillageId() = runBlocking { userPreferences.getVillageId().first() }

    val isLoadingAddress = MutableLiveData<Boolean>()
    val isErrorFetchingProfileInfo = MutableLiveData(false)

    val listProvince = MutableLiveData<ArrayList<Province>>(null)
    val listRegency = MutableLiveData<ArrayList<Regency>?>(null)
    val listDistrict = MutableLiveData<ArrayList<District>?>(null)
    val listVillage = MutableLiveData<ArrayList<Village>?>(null)


    val selectedProvinceId = MutableLiveData<Int>(Integer.parseInt(getUserProvinceId()))
    val selectedRegencyId = MutableLiveData<Int>(Integer.parseInt(getUserRegencyId()))
    val selectedDistrictId = MutableLiveData<Int>(Integer.parseInt(getUserDistrictId()))
    val selectedVillageId = MutableLiveData<Long>(getUserVillageId().toLong())

    init {
        getProvinceList()
    }

    private fun getProvinceList() {
        viewModelScope.launch {
            authRepository.getAllProvinces().asFlow().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        isLoadingAddress.postValue(true)
                        isErrorFetchingProfileInfo.postValue(false)
                    }

                    is Result.Success -> {
                        getRegencyList(selectedProvinceId.value!!)
                        listProvince.postValue(result.data.province as ArrayList<Province>?)
                    }

                    is Result.Error -> {
                        isLoadingAddress.postValue(false)
                        isErrorFetchingProfileInfo.postValue(true)
                    }
                }
            }
        }
    }

    fun getRegencyList(provinceId: Int) {
        viewModelScope.launch {
            authRepository.getAllRegencies(provinceId).asFlow().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        isLoadingAddress.postValue(true)
                        isErrorFetchingProfileInfo.postValue(false)
                    }

                    is Result.Success -> {
                        getDistrictList(selectedRegencyId.value!!)
                        listRegency.postValue(result.data.regency as ArrayList<Regency>?)
                    }

                    is Result.Error -> {
                        isLoadingAddress.postValue(false)
                        isErrorFetchingProfileInfo.postValue(true)
                    }
                }
            }
        }
    }

    fun getDistrictList(regencyId: Int) {
        viewModelScope.launch {
            authRepository.getAllDistricts(regencyId).asFlow().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        isLoadingAddress.postValue(true)
                        isErrorFetchingProfileInfo.postValue(false)
                    }

                    is Result.Success -> {
                        getVillageList(selectedDistrictId.value!!)
                        listDistrict.postValue(result.data.district as ArrayList<District>?)
                    }

                    is Result.Error -> {
                        isLoadingAddress.postValue(false)
                        isErrorFetchingProfileInfo.postValue(true)
                    }
                }
            }
        }
    }

    fun getVillageList(districtId: Int) {
        viewModelScope.launch {
            authRepository.getAllVillages(districtId).asFlow().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        isLoadingAddress.postValue(true)
                        isErrorFetchingProfileInfo.postValue(false)
                    }

                    is Result.Success -> {
                        isLoadingAddress.postValue(false)
                        isErrorFetchingProfileInfo.postValue(false)

                        listVillage.postValue(result.data.village as ArrayList<Village>?)
                    }

                    is Result.Error -> {
                        isLoadingAddress.postValue(false)
                        isErrorFetchingProfileInfo.postValue(true)
                    }
                }
            }
        }
    }
}