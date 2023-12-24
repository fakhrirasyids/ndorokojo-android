package com.ndorokojo.ui.detailkandang

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ndorokojo.data.models.DetailKandang
import com.ndorokojo.data.models.District
import com.ndorokojo.data.models.ProfileInfo
import com.ndorokojo.data.models.Province
import com.ndorokojo.data.models.Regency
import com.ndorokojo.data.models.Village
import com.ndorokojo.data.repo.AuthRepository
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.utils.Result
import com.ndorokojo.utils.UserPreferences
import kotlinx.coroutines.launch

class DetailKandangViewModel(
    private val ternakRepository: TernakRepository,
    private val kandangId: Int
) : ViewModel() {
    val isLoadingAddress = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val detailKandang = MutableLiveData<DetailKandang>(null)
    val isErrorFetchingDetailKandang = MutableLiveData(false)
    val responseMessage = MutableLiveData<String>()

    init {
        getDetailKandang(kandangId)
    }

    fun getDetailKandang(id: Int) {
        viewModelScope.launch {
            ternakRepository.getDetailKandang(id).asFlow().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        isLoading.postValue(true)
                        isErrorFetchingDetailKandang.postValue(false)
                    }

                    is Result.Success -> {
                        isLoading.postValue(false)
                        isErrorFetchingDetailKandang.postValue(false)
                        detailKandang.postValue(result.data.detailKandang!!)
                    }

                    is Result.Error -> {
                        isLoading.postValue(false)
                        isErrorFetchingDetailKandang.postValue(true)
                        responseMessage.postValue(result.error)
                    }
                }
            }
        }
    }
}