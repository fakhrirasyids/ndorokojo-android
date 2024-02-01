package com.ndorokojo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ndorokojo.data.models.Event
import com.ndorokojo.data.models.EventsItem
import com.ndorokojo.data.models.Kandang
import com.ndorokojo.data.models.News
import com.ndorokojo.data.models.SearchPayload
import com.ndorokojo.data.models.SliderCategory
import com.ndorokojo.data.models.TernakItem
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.utils.Result
import com.ndorokojo.utils.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

    val eventList = MutableLiveData<ArrayList<EventsItem>>(arrayListOf())
    val kandangList = MutableLiveData<ArrayList<Kandang>>(arrayListOf())

//    val brebesTodayList = MutableLiveData<ArrayList<News>>(arrayListOf())
//    val digitalFinanceList = MutableLiveData<ArrayList<News>>(arrayListOf())

    val sliderCategoryList = MutableLiveData<ArrayList<SliderCategory>>(arrayListOf())

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
                            eventList.postValue(result.data.payload as ArrayList<EventsItem>?)
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
                            ternakList.postValue(result.data.ternakItem as ArrayList<TernakItem>?)
                            getSliderCategories()
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

    private fun getSliderCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            ternakRepository.getSliderCategories().asFlow().collect { result ->
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Result.Loading -> {
                            isLoading.postValue(true)
                            isErrorLoadingData.postValue(false)
                        }

                        is Result.Success -> {
                            isLoading.postValue(false)
                            sliderCategoryList.postValue(result.data.payload as ArrayList<SliderCategory>?)
                            isErrorLoadingData.postValue(false)
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

//    private fun getBrebesTodayList() {
//        viewModelScope.launch(Dispatchers.IO) {
//            ternakRepository.getAllBrebesToday().asFlow().collect { result ->
//                withContext(Dispatchers.Main) {
//                    when (result) {
//                        is Result.Loading -> {
//                            isLoading.postValue(true)
//                        }
//
//                        is Result.Success -> {
////                            isLoading.postValue(false)
//                            brebesTodayList.postValue(result.data.news as ArrayList<News>?)
//                            getDigitalFinanceList()
//                        }
//
//                        is Result.Error -> {
//                            isLoading.postValue(false)
//                            responseMessage.postValue(result.error)
//                            isErrorLoadingData.postValue(false)
//                        }
//                    }
//                }
//            }
//        }
//    }

//    private fun getDigitalFinanceList() {
//        viewModelScope.launch(Dispatchers.IO) {
//            ternakRepository.getAllFinanceToday().asFlow().collect { result ->
//                withContext(Dispatchers.Main) {
//                    when (result) {
//                        is Result.Loading -> {
//                            isLoading.postValue(true)
//                        }
//
//                        is Result.Success -> {
//                            isLoading.postValue(false)
//                            digitalFinanceList.postValue(result.data.news as ArrayList<News>?)
//                            isErrorLoadingData.postValue(false)
//                        }
//
//                        is Result.Error -> {
//                            isLoading.postValue(false)
//                            responseMessage.postValue(result.error)
//                            isErrorLoadingData.postValue(false)
//                        }
//                    }
//                }
//            }
//        }
//    }

    val searchResponse = MutableLiveData<SearchPayload>(null)

    fun searchAnything(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            ternakRepository.searchAnything(query).asFlow().collect { result ->
                withContext(Dispatchers.Main) {
                    when (result) {
                        is Result.Loading -> {
                            isLoading.postValue(true)
                        }

                        is Result.Success -> {
                            isLoading.postValue(false)
                            searchResponse.postValue(result.data.payload!!)
                        }

                        is Result.Error -> {
                            isLoading.postValue(false)
                            responseMessage.postValue(result.error)
                        }
                    }
                }
            }
        }
    }

    fun getListKandang(typeId: Int) = ternakRepository.getListKandang(typeId)

    fun getUsername() = userPreferences.getFullname().asLiveData()
}