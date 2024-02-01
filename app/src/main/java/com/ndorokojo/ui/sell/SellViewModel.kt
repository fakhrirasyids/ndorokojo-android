package com.ndorokojo.ui.sell

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
import java.io.File

class SellViewModel(
    private val ternakRepository: TernakRepository,
) : ViewModel() {
    val isLoadingParent = MutableLiveData<Boolean>()

    val responseMessage = MutableLiveData<String>()
    val isErrorFetchingProfileInfo = MutableLiveData(false)

    val listTernak = MutableLiveData<ArrayList<Ternak>>(null)
    val selectedRasTypeId = MutableLiveData<Int>(null)
    val selectedTernakId = MutableLiveData<Int>(null)

    val imageTernak = MutableLiveData<File>()

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

    fun sellTernak() = ternakRepository.sellTernak(
        id = selectedTernakId.value!!,
        proposedPrice = sellProposedPrice.value!!,
    )

    fun updateTernakImage() = ternakRepository.updateImage(
        id = selectedTernakId.value.toString(),
        image = imageTernak.value!!
    )

    // Died Data
    val sellProposedPrice = MutableLiveData<Int>(null)
}