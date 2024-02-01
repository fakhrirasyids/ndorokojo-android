package com.ndorokojo.ui.detailnews

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ndorokojo.data.models.DetailNews
import com.ndorokojo.data.models.News
import com.ndorokojo.data.models.Ternak
import com.ndorokojo.data.repo.TernakRepository
import com.ndorokojo.utils.Result
import kotlinx.coroutines.launch

class DetailNewsViewModel(
    private val ternakRepository: TernakRepository,
    private val slug: String,
    private val newsId: Int,
) : ViewModel() {
    val isLoading = MutableLiveData<Boolean>()
    val newsDetail = MutableLiveData<DetailNews>(null)
    val responseMessage = MutableLiveData<String>()
    val isError = MutableLiveData<Boolean>()

    init {
        getDetails(slug, newsId)
    }

     fun getDetails(slug: String, id: Int) {
        viewModelScope.launch {
            ternakRepository.getDetailNews(slug, id).asFlow().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        isLoading.postValue(true)
                        isError.postValue(false)
                    }

                    is Result.Success -> {
                        isLoading.postValue(false)
                        isError.postValue(false)
                        newsDetail.postValue(result.data.detailNews!!)
                    }

                    is Result.Error -> {
                        isLoading.postValue(false)
                        responseMessage.postValue(result.error)
                        isError.postValue(true)
                    }
                }
            }
        }
    }

//    fun getFinanceDetailList(id: Int) {
//        viewModelScope.launch {
//            ternakRepository.getFinanceDetail(id).asFlow().collect { result ->
//                when (result) {
//                    is Result.Loading -> {
//                        isError.postValue(false)
//                        isLoading.postValue(true)
//                    }
//
//                    is Result.Success -> {
//                        isLoading.postValue(false)
//                        isError.postValue(false)
//                        newsDetail.postValue(result.data.detailNews!!)
//                    }
//
//                    is Result.Error -> {
//                        isLoading.postValue(false)
//                        responseMessage.postValue(result.error)
//                        isError.postValue(true)
//                    }
//                }
//            }
//        }
//    }
}