package com.ndorokojo.ui.updateprofile

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateProfileViewModel(
    private val authRepository: AuthRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    val isLoadingParent = MutableLiveData<Boolean>()
    val isLoadingAddress = MutableLiveData<Boolean>()
    val isLoadingSaving = MutableLiveData<Boolean>()

    val responseMessage = MutableLiveData<String>()
    val isErrorFetchingProfileInfo = MutableLiveData(false)

    val userProfileInfo = MutableLiveData<ProfileInfo>()
    val listProvince = MutableLiveData<ArrayList<Province>>(null)
    val listRegency = MutableLiveData<ArrayList<Regency>?>(null)
    val listDistrict = MutableLiveData<ArrayList<District>?>(null)
    val listVillage = MutableLiveData<ArrayList<Village>?>(null)

    val isUsernameFilled = MutableLiveData(false)
    val isFullnameFilled = MutableLiveData(false)
    val isEmailFilled = MutableLiveData(false)
    val isPhoneFilled = MutableLiveData(false)
    val isAddressFilled = MutableLiveData(false)
    val isOccupationFilled = MutableLiveData(false)
    val isGenderFilled = MutableLiveData(false)
    val isAgeFilled = MutableLiveData(false)
    val isKelompokTernakFilled = MutableLiveData(false)
    val isProvinceFilled = MutableLiveData(false)
    val isRegencyFilled = MutableLiveData(false)
    val isDistrictFilled = MutableLiveData(false)
    val isVillageFilled = MutableLiveData(false)

    val selectedProvinceId = MutableLiveData<Int>(null)
    val selectedRegencyId = MutableLiveData<Int>(null)
    val selectedDistrictId = MutableLiveData<Int>(null)
    val selectedVillageId = MutableLiveData<Long>(null)

    init {
        getProfileInfo()
    }

    fun getProfileInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.getProfileInfo().asFlow().collect { result ->
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Result.Loading -> {
//                        isLoadingParent.postValue(true)
                            isLoadingAddress.postValue(true)
                            isErrorFetchingProfileInfo.postValue(false)
                        }

                        is Result.Success -> {
//                        isLoadingParent.postValue(false)
                            isErrorFetchingProfileInfo.postValue(false)

                            userProfileInfo.postValue(result.data.profileInfo!!)
                            getProvinceList()

                            if (result.data.profileInfo.provinceId != null) {
                                selectedProvinceId.postValue(Integer.parseInt(result.data.profileInfo.provinceId!!))
                                getProvinceList(Integer.parseInt(result.data.profileInfo.provinceId))
                                selectedRegencyId.postValue(Integer.parseInt(result.data.profileInfo.regencyId!!))
                                selectedDistrictId.postValue(Integer.parseInt(result.data.profileInfo.districtId!!))
                                selectedVillageId.postValue(result.data.profileInfo.villageId!!.toLong())
//                            getRegencyList(userProfileInfo.value?.provinceId!!)
                            }

//                        if (result.data.profileInfo.regencyId != null) {
//                            selectedRegencyId.postValue(result.data.profileInfo.regencyId!!)
//                            getDistrictList(result.data.profileInfo.regencyId)
//                        }
//
//                        if (result.data.profileInfo.districtId != null) {
//                            selectedDistrictId.postValue(result.data.profileInfo.districtId!!)
//                            getVillageList(result.data.profileInfo.districtId)
//                        }
//
//                        if (result.data.profileInfo.villageId != null) {
//                            selectedVillageId.postValue(result.data.profileInfo.villageId!!)
//                        }
                        }

                        is Result.Error -> {
//                        isLoadingParent.postValue(false)
                            isLoadingAddress.postValue(false)
                            isErrorFetchingProfileInfo.postValue(true)
                        }
                    }
                }
            }
        }
    }

    fun getProvinceList(selectedProvince: Int? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.getAllProvinces().asFlow().collect { result ->
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Result.Loading -> {
                            isLoadingAddress.postValue(true)
                            isErrorFetchingProfileInfo.postValue(false)
                        }

                        is Result.Success -> {
                            isLoadingAddress.postValue(false)
                            isErrorFetchingProfileInfo.postValue(false)

                            if (selectedProvince != null) {
                                getRegencyList(selectedProvince)
                            }
                            listProvince.postValue(result.data.province as ArrayList<Province>?)

                            listRegency.postValue(null)
                            listDistrict.postValue(null)
                            listVillage.postValue(null)
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

    fun getRegencyList(provinceId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.getAllRegencies(provinceId).asFlow().collect { result ->
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Result.Loading -> {
                            isLoadingAddress.postValue(true)
                            isErrorFetchingProfileInfo.postValue(false)
                        }

                        is Result.Success -> {
                            isLoadingAddress.postValue(false)
                            isErrorFetchingProfileInfo.postValue(false)

                            if (selectedRegencyId.value != null) {
                                getDistrictList(selectedRegencyId.value!!)
                            }

                            listDistrict.postValue(null)
                            listVillage.postValue(null)
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
    }

    fun getDistrictList(regencyId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.getAllDistricts(regencyId).asFlow().collect { result ->
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Result.Loading -> {
                            isLoadingAddress.postValue(true)
                            isErrorFetchingProfileInfo.postValue(false)
                        }

                        is Result.Success -> {
                            isLoadingAddress.postValue(false)
                            isErrorFetchingProfileInfo.postValue(false)

                            if (selectedDistrictId.value != null) {
                                getVillageList(selectedDistrictId.value!!)
                            }

                            listVillage.postValue(null)
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
    }

    fun getVillageList(districtId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.getAllVillages(districtId).asFlow().collect { result ->
                withContext(Dispatchers.Main) {
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
    ) = authRepository.updateUser(
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

    fun savePreferences(
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
            userPreferences.savePreferencesUpdate(
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
}