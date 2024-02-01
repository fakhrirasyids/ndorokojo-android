package com.ndorokojo.ui.buy

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ndorokojo.data.models.Event
import com.ndorokojo.data.models.ItemsItem
import com.ndorokojo.data.models.Kandang
import com.ndorokojo.data.models.Ternak
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.utils.Result
import com.ndorokojo.utils.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Error

class BuyViewModel(
    private val ternakRepository: TernakRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    val buyTernakEvent = MutableLiveData<ItemsItem>(null)
    val selectedKandangId = MutableLiveData<Int>(null)

    val isLoadingKandang = MutableLiveData<Boolean>()
    val listKandang = MutableLiveData<ArrayList<Kandang>>(null)
    val responseMessage = MutableLiveData<String>()
    val isErrorKandang = MutableLiveData<Boolean>()

    fun getListKandang(typeId: Int) {
        viewModelScope.launch {
            ternakRepository.getListKandang(typeId).asFlow().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        isLoadingKandang.postValue(true)
                        isErrorKandang.postValue(false)
                    }

                    is Result.Success -> {
                        isLoadingKandang.postValue(false)
                        isErrorKandang.postValue(false)
                        listKandang.postValue(result.data.kandang!! as ArrayList<Kandang>?)
                    }

                    is Result.Error -> {
                        isLoadingKandang.postValue(false)
                        isErrorKandang.postValue(true)
                    }
                }
            }
        }
    }

    fun buyTernak() = ternakRepository.buyTernak(
        liveStockId = buyTernakEvent.value?.id!!,
        kandangId = selectedKandangId.value!!,
        dealPrice = buyDealPrice.value!!
    )

    val buyDealPrice = MutableLiveData<Int>(null)

    fun getFullname() = runBlocking { userPreferences.getFullname().first() }
}