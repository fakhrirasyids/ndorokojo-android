package com.ndorokojo.ui.died

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ndorokojo.data.models.Ternak
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiedViewModel(
    private val ternakRepository: TernakRepository,
) : ViewModel() {
    val isLoadingParent = MutableLiveData<Boolean>()

    val responseMessage = MutableLiveData<String>()
    val isErrorFetchingProfileInfo = MutableLiveData(false)

    val listTernak = MutableLiveData<ArrayList<Ternak>>(null)
    val selectedRasTypeId = MutableLiveData<Int>(null)
    val selectedTernakId = MutableLiveData<Int>(null)

    fun getAllTernakList(liveStockId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            ternakRepository.getListTernak(liveStockId).asFlow().collect { result ->
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Result.Loading -> {
                            isLoadingParent.postValue(true)
                            isErrorFetchingProfileInfo.postValue(false)
                        }

                        is Result.Success -> {
                            Log.e("NGOGNO", "getAllTernakList: $liveStockId ${result.data.ternak!!}")
                            listTernak.postValue(result.data.ternak!! as ArrayList<Ternak>?)
                            isLoadingParent.postValue(false)
                            isErrorFetchingProfileInfo.postValue(false)
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

    fun diedTernak() = ternakRepository.diedTernak(
        id = selectedTernakId.value!!,
        deadType = deadType.value.toString(),
        deadReason = deadReason.value.toString(),
        deadMonth = deadMonth.value.toString(),
        deadYear = deadYear.value.toString()
    )

    // Died Data
    val deadType = MutableLiveData<String>(null)
    val deadReason = MutableLiveData<String>(null)
    val deadMonth = MutableLiveData<String>(null)
    val deadYear = MutableLiveData<String>(null)
}