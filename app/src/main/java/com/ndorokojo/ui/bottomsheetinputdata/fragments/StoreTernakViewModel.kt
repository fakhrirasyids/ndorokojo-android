package com.ndorokojo.ui.bottomsheetinputdata.fragments

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ndorokojo.data.models.District
import com.ndorokojo.data.models.KandangPayload
import com.ndorokojo.data.models.Limbah
import com.ndorokojo.data.models.LivestockPayload
import com.ndorokojo.data.models.Pakan
import com.ndorokojo.data.models.Province
import com.ndorokojo.data.models.Regency
import com.ndorokojo.data.models.SensorPayload
import com.ndorokojo.data.models.StoreTernakPayload
import com.ndorokojo.data.models.Village
import com.ndorokojo.data.repo.AuthRepository
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.utils.Result
import com.ndorokojo.utils.UserPreferences
import kotlinx.coroutines.launch

class StoreTernakViewModel(
    private val authRepository: AuthRepository,
    private val ternakRepository: TernakRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    val isLoadingParent = MutableLiveData<Boolean>()
    val isLoadingAddress = MutableLiveData<Boolean>()
    val isLoadingSaving = MutableLiveData<Boolean>()

    val responseMessage = MutableLiveData<String>()
    val isErrorFetchingProfileInfo = MutableLiveData(false)

    // Kandang Data
    val listProvince = MutableLiveData<ArrayList<Province>>(null)
    val listRegency = MutableLiveData<ArrayList<Regency>?>(null)
    val listDistrict = MutableLiveData<ArrayList<District>?>(null)
    val listVillage = MutableLiveData<ArrayList<Village>?>(null)

    val isNameFilled = MutableLiveData(false)
    val isPanjangFilled = MutableLiveData(false)
    val isLebarFilled = MutableLiveData(false)
    val isJenisKandangFilled = MutableLiveData(false)
    val isProvinceFilled = MutableLiveData(false)
    val isRegencyFilled = MutableLiveData(false)
    val isDistrictFilled = MutableLiveData(false)
    val isVillageFilled = MutableLiveData(false)
    val isAddressFilled = MutableLiveData(false)
    val isRtRwFilled = MutableLiveData(false)
    val isLongitudeFilled = MutableLiveData(false)
    val isLatitudeFilled = MutableLiveData(false)

    val selectedProvinceId = MutableLiveData<Int>(null)
    val selectedRegencyId = MutableLiveData<Int>(null)
    val selectedDistrictId = MutableLiveData<Int>(null)
    val selectedVillageId = MutableLiveData<Long>(null)

    // Sensor Data
    val isSensorStatusFilled = MutableLiveData(false)
    val isSensorLatFilled = MutableLiveData(false)
    val isSensorLonFilled = MutableLiveData(false)
    val isSensorBatteryFilled = MutableLiveData(false)
    val isSensorGPSFilled = MutableLiveData(false)
    val isSensorReportFilled = MutableLiveData(false)
    val isSensorStatusTerpasang = MutableLiveData<Boolean>(null)

    // Ternak Data
    val listLimbah = MutableLiveData<ArrayList<Limbah>>(null)
    val listPakan = MutableLiveData<ArrayList<Pakan>>(null)

    val selectedRasTypeId = MutableLiveData<Int>(null)
    val selectedParentTypeId = MutableLiveData<Int>(null)
    val selectedLimbahId = MutableLiveData<Int>(null)
    val selectedPakanId = MutableLiveData<Int>(null)


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
                        getAllLimbahList()
                        getAllPakanList()

                        isLoadingAddress.postValue(false)
                        isErrorFetchingProfileInfo.postValue(false)

                        listRegency.postValue(null)
                        listDistrict.postValue(null)
                        listVillage.postValue(null)

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
                        isLoadingAddress.postValue(false)
                        isErrorFetchingProfileInfo.postValue(false)

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

    fun getDistrictList(regencyId: Int) {
        viewModelScope.launch {
            authRepository.getAllDistricts(regencyId).asFlow().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        isLoadingAddress.postValue(true)
                        isErrorFetchingProfileInfo.postValue(false)
                    }

                    is Result.Success -> {
                        isLoadingAddress.postValue(false)
                        isErrorFetchingProfileInfo.postValue(false)

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

    private fun getAllLimbahList() {
        viewModelScope.launch {
            ternakRepository.getAllLimbah().asFlow().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        isErrorFetchingProfileInfo.postValue(false)
                    }

                    is Result.Success -> {
                        isErrorFetchingProfileInfo.postValue(false)
                        listLimbah.postValue(result.data.limbah as ArrayList<Limbah>?)
                    }

                    is Result.Error -> {
                        isErrorFetchingProfileInfo.postValue(true)
                    }
                }
            }
        }
    }

    private fun getAllPakanList() {
        viewModelScope.launch {
            ternakRepository.getAllPakan().asFlow().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        isErrorFetchingProfileInfo.postValue(false)
                    }

                    is Result.Success -> {
                        isErrorFetchingProfileInfo.postValue(false)
                        listPakan.postValue(result.data.pakan as ArrayList<Pakan>?)
                    }

                    is Result.Error -> {
                        isErrorFetchingProfileInfo.postValue(true)
                    }
                }
            }
        }
    }

    fun storeTernak() = ternakRepository.storeTernak(
        StoreTernakPayload(
            kandang = KandangPayload(
                name = kandangName.value.toString(),
                type_id = selectedParentTypeId.value!!,
                panjang = kandangPanjang.value!!,
                lebar = kandangLebar.value!!,
                jenis = kandangJenis.value.toString(),
                province_id = selectedProvinceId.value!!,
                regency_id = selectedRegencyId.value!!,
                district_id = selectedDistrictId.value!!,
                village_id = selectedVillageId.value!!.toString(),
                address = kandangAlamat.value.toString(),
                rt_rw = kandangRtRw.value.toString(),
                longitude = kandangLon.value.toString(),
                latitude = kandangLat.value.toString(),
                sensor_status = sensorStatus.value.toString(),
                sensor = SensorPayload(
                    sensor_latitude = sensorLat.value.toString(),
                    sensor_longitude = sensorLon.value.toString(),
                    sensor_batterypercent = sensorBattery.value,
                    sensor_gps_type = sensorGPS.value.toString(),
                    sensor_report = sensorReport.value.toString()
                )
            ),
            livestock = LivestockPayload(
                pakan_id = selectedPakanId.value!!,
                limbah_id = selectedLimbahId.value!!,
                age = ternakAge.value.toString(),
                type_id = selectedRasTypeId.value!!,
            )
        )
    )


    // Kandang Data
    val kandangName = MutableLiveData<String>(null)
    val kandangPanjang = MutableLiveData<Double>(null)
    val kandangLebar = MutableLiveData<Double>(null)
    val kandangJenis = MutableLiveData<String>(null)
    val kandangAlamat = MutableLiveData<String>(null)
    val kandangRtRw = MutableLiveData<String>(null)
    val kandangLat = MutableLiveData<String>(null)
    val kandangLon = MutableLiveData<String>(null)

    // Sensor Data
    val sensorStatus = MutableLiveData<String>(null)
    val sensorLat = MutableLiveData<String>(null)
    val sensorLon = MutableLiveData<String>(null)
    val sensorBattery = MutableLiveData<Int>(null)
    val sensorGPS = MutableLiveData<String>(null)
    val sensorReport = MutableLiveData<String>(null)

    // Ternak Data
    val ternakAge = MutableLiveData<String>(null)
}