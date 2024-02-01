package com.ndorokojo.ui.birth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ndorokojo.data.models.Limbah
import com.ndorokojo.data.models.Pakan
import com.ndorokojo.data.repo.AuthRepository
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BirthViewModel(
    private val authRepository: AuthRepository,
    private val ternakRepository: TernakRepository
) : ViewModel() {
    val isLoadingParent = MutableLiveData<Boolean>()

    val responseMessage = MutableLiveData<String>()
    val isErrorFetchingProfileInfo = MutableLiveData(false)

    // Ternak Data
    val listLimbah = MutableLiveData<ArrayList<Limbah>>(null)
    val listPakan = MutableLiveData<ArrayList<Pakan>>(null)

    val selectedKandangId = MutableLiveData<Int>(null)
    val selectedRasTypeId = MutableLiveData<Int>(null)
    val selectedLimbahId = MutableLiveData<Int>(null)
    val selectedPakanId = MutableLiveData<Int>(null)


    init {
        getAllLimbahList()
    }

    private fun getAllLimbahList() {
        viewModelScope.launch(Dispatchers.IO) {
            ternakRepository.getAllLimbah().asFlow().collect { result ->
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Result.Loading -> {
                            isLoadingParent.postValue(true)
                            isErrorFetchingProfileInfo.postValue(false)
                        }

                        is Result.Success -> {
//                            getAllPakanList()
                            isLoadingParent.postValue(false)
                            isErrorFetchingProfileInfo.postValue(false)
                            listLimbah.postValue(result.data.limbah as ArrayList<Limbah>?)
                        }

                        is Result.Error -> {
                            isLoadingParent.postValue(false)
                            isErrorFetchingProfileInfo.postValue(true)
                        }
                    }
                }
            }
        }
    }

//    private fun getAllPakanList() {
//        viewModelScope.launch(Dispatchers.IO) {
//            ternakRepository.getAllPakan().asFlow().collect { result ->
//                withContext(Dispatchers.Main) {
//                    when (result) {
//                        is Result.Loading -> {
//                            isLoadingParent.postValue(false)
//                            isErrorFetchingProfileInfo.postValue(false)
//                        }
//
//                        is Result.Success -> {
//                            isErrorFetchingProfileInfo.postValue(false)
//                            listPakan.postValue(result.data.pakan as ArrayList<Pakan>?)
//                        }
//
//                        is Result.Error -> {
//                            isLoadingParent.postValue(false)
//                            isErrorFetchingProfileInfo.postValue(true)
//                        }
//                    }
//                }
//            }
//        }
//    }

    fun birthTernak(pakan: String, nominal: Int) = ternakRepository.birthTernak(
        kandangId = selectedKandangId.value!!,
        pakan = pakan,
        limbahId = selectedLimbahId.value!!,
        gender = ternakGender.value.toString(),
        age = ternakAge.value.toString(),
        typeId = selectedRasTypeId.value!!,
        nominal = nominal
    )

    // Ternak Data
    val ternakGender = MutableLiveData<String>(null)
    val ternakAge = MutableLiveData<String>(null)
}