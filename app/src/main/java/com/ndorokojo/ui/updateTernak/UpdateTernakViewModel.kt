package com.ndorokojo.ui.updateTernak

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ndorokojo.data.models.DetailKandang
import com.ndorokojo.data.models.District
import com.ndorokojo.data.models.LivestocksItem
import com.ndorokojo.data.models.ProfileInfo
import com.ndorokojo.data.models.Province
import com.ndorokojo.data.models.Regency
import com.ndorokojo.data.models.Village
import com.ndorokojo.data.repo.AuthRepository
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.utils.Result
import com.ndorokojo.utils.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Month

class UpdateTernakViewModel(
    private val ternakRepository: TernakRepository,
) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val isErrorFetchingDetailKandang = MutableLiveData(false)
    val responseMessage = MutableLiveData<String>()

    fun updateTernak(id: Int, status: String) =
        ternakRepository.updateTernak(
            id,
            status,
            terjualYear.value.toString(),
            terjualMonth.value.toString()
        )

    fun diedTernak(id: Int) =
        ternakRepository.diedTernak(
            id,
            "null",
            "null",
            terjualYear.value.toString(),
            terjualMonth.value.toString()
        )

    val terjualMonth = MutableLiveData<String>(null)
    val terjualYear = MutableLiveData<String>(null)
}