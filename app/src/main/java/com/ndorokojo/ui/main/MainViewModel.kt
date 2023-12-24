package com.ndorokojo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ndorokojo.data.models.Event
import com.ndorokojo.data.models.Kandang
import com.ndorokojo.data.models.News
import com.ndorokojo.data.models.TernakItem
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.utils.Result
import com.ndorokojo.utils.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainViewModel(
    private val ternakRepository: TernakRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val ternakList = MutableLiveData<ArrayList<TernakItem>>(arrayListOf())
    val responseMessage = MutableLiveData<String>()

    val isErrorLoadingData = MutableLiveData<Boolean>()

    val isTernakExpanded = MutableLiveData(false)

    val clickedTernakId = MutableLiveData<Int>(null)
    val clickedTernak = MutableLiveData<TernakItem>(null)

    val isLoadingKandang = MutableLiveData<Boolean>()

    val eventList = MutableLiveData<ArrayList<Event>>(arrayListOf())
    val kandangList = MutableLiveData<ArrayList<Kandang>>(arrayListOf())

    val brebesTodayList = MutableLiveData<ArrayList<News>>(arrayListOf())
    val digitalFinanceList = MutableLiveData<ArrayList<News>>(arrayListOf())


    init {
        getAllEventList()
    }

    fun getAllEventList() {
        viewModelScope.launch(Dispatchers.IO) {
            ternakRepository.getAllEventList().asFlow().collect { result ->
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Result.Loading -> {
                            isLoading.postValue(true)
                            isErrorLoadingData.postValue(false)

                        }

                        is Result.Success -> {
                            isErrorLoadingData.postValue(false)
                            eventList.postValue(result.data.listEvent as ArrayList<Event>?)
                            getAllTernakList()
                        }

                        is Result.Error -> {
                            isLoading.postValue(false)
                            responseMessage.postValue(result.error)
                            isErrorLoadingData.postValue(true)
                        }
                    }
                }
            }
        }
    }

    private fun getAllTernakList() {
        viewModelScope.launch(Dispatchers.IO) {
            ternakRepository.getAllRasTernak().asFlow().collect { result ->
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Result.Loading -> {
                            isLoading.postValue(true)
                        }

                        is Result.Success -> {
//                            isLoading.postValue(false)
                            ternakList.postValue(result.data.ternakItem as ArrayList<TernakItem>?)
                            getBrebesTodayList()
                        }

                        is Result.Error -> {
                            isLoading.postValue(false)
                            responseMessage.postValue(result.error)
                            isErrorLoadingData.postValue(false)
                        }
                    }
                }
            }
        }
    }

    private fun getBrebesTodayList() {
        viewModelScope.launch(Dispatchers.IO) {
            ternakRepository.getAllBrebesToday().asFlow().collect { result ->
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Result.Loading -> {
                            isLoading.postValue(true)
                        }

                        is Result.Success -> {
//                            isLoading.postValue(false)
                            brebesTodayList.postValue(result.data.news as ArrayList<News>?)
                            getDigitalFinanceList()
                        }

                        is Result.Error -> {
                            isLoading.postValue(false)
                            responseMessage.postValue(result.error)
                            isErrorLoadingData.postValue(false)
                        }
                    }
                }
            }
        }
    }

    private fun getDigitalFinanceList() {
        viewModelScope.launch(Dispatchers.IO) {
            ternakRepository.getAllFinanceToday().asFlow().collect { result ->
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Result.Loading -> {
                            isLoading.postValue(true)
                        }

                        is Result.Success -> {
                            isLoading.postValue(false)
                            digitalFinanceList.postValue(result.data.news as ArrayList<News>?)
                            isErrorLoadingData.postValue(false)
                        }

                        is Result.Error -> {
                            isLoading.postValue(false)
                            responseMessage.postValue(result.error)
                            isErrorLoadingData.postValue(false)
                        }
                    }
                }
            }
        }
    }


    fun getListKandang(typeId: Int) = ternakRepository.getListKandang(typeId)

    fun getUsername() = userPreferences.getFullname().asLiveData()
}