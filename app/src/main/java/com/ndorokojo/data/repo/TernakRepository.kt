package com.ndorokojo.data.repo

import androidx.lifecycle.liveData
import com.ndorokojo.data.models.BuyPayload
import com.ndorokojo.data.models.KandangPayload
import com.ndorokojo.data.models.LiveStockItem
import com.ndorokojo.data.models.StoreTernakPayload
import com.ndorokojo.data.remote.ApiService
import com.ndorokojo.utils.Result
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class TernakRepository(
    private val apiService: ApiService
) {
    fun getAllRasTernak() = liveData {
        emit(Result.Loading)
        try {
            val rasTernakResponse = apiService.getAllRasTernakList()
            emit(Result.Success(rasTernakResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getListKandang(typeId: Int) = liveData {
        emit(Result.Loading)
        try {
            val listKandangResponse = apiService.getListKandang(typeId)
            emit(Result.Success(listKandangResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun storeTernak(
        ternakPayload: StoreTernakPayload
    ) = liveData {
        emit(Result.Loading)
        try {
            val storeTernakResponse = apiService.storeTernak(ternakPayload)
            emit(Result.Success(storeTernakResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun storeKandang(
        payload: KandangPayload
    ) = liveData {
        emit(Result.Loading)
        try {
            val storeKandangResponse = apiService.storeKandang(
                payload
            )
            emit(Result.Success(storeKandangResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllLimbah() = liveData {
        emit(Result.Loading)
        try {
            val limbahResponse = apiService.getAllLimbah()
            emit(Result.Success(limbahResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllPakan() = liveData {
        emit(Result.Loading)
        try {
            val pakanResponse = apiService.getAllPakan()
            emit(Result.Success(pakanResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun birthTernak(
        kandangId: Int,
        pakan: String,
        limbahId: Int,
        gender: String,
        age: String,
        typeId: Int,
        nominal: Int
    ) = liveData {
        emit(Result.Loading)
        try {
            val birthResponse = apiService.birthTernak(
                kandangId, pakan, limbahId, gender, age, typeId, nominal
            )
            emit(Result.Success(birthResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun diedTernak(
        id: Int,
        deadType: String,
        deadReason: String,
        deadYear: String,
        deadMonth: String,
    ) = liveData {
        emit(Result.Loading)
        try {
            val diedResponse = apiService.diedTernak(
                id, deadType, deadReason, deadYear, deadMonth
            )
            emit(Result.Success(diedResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getListTernak(livestocksTypeId: Int) = liveData {
        emit(Result.Loading)
        try {
            val listTernakResponse = apiService.getListTernak(
                livestocksTypeId
            )
            emit(Result.Success(listTernakResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun sellTernak(
        id: Int,
        proposedPrice: Int,
    ) = liveData {
        emit(Result.Loading)
        try {
            val sellResponse = apiService.sellTernak(
                id, proposedPrice
            )
            emit(Result.Success(sellResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllEventList() = liveData {
        emit(Result.Loading)
        try {
            val eventResponse = apiService.getAllListEvents()
            emit(Result.Success(eventResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun buyTernak(
        liveStockId: Int,
        kandangId: Int,
        dealPrice: Int,
    ) = liveData {
        emit(Result.Loading)
        try {
            val buyResponse = apiService.buyTernak(
                BuyPayload(
                    livestockList = listOf(LiveStockItem(liveStockId)),
                    kandangId = kandangId,
                    dealPrice = dealPrice
                )
            )
            emit(Result.Success(buyResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllBrebesToday() = liveData {
        emit(Result.Loading)
        try {
            val brebesTodayResponse = apiService.getAllBrebesToday()
            emit(Result.Success(brebesTodayResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getAllFinanceToday() = liveData {
        emit(Result.Loading)
        try {
            val financeTodayResponse = apiService.getAllFinanceToday()
            emit(Result.Success(financeTodayResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getDetailNews(slug: String, id: Int) = liveData {
        emit(Result.Loading)
        try {
            val brebesDetailResponse = apiService.getDetailNews(slug, id)
            emit(Result.Success(brebesDetailResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

//    fun getFinanceDetail(id: Int) = liveData {
//        emit(Result.Loading)
//        try {
//            val financeDetailResponse = apiService.getFinanceToday(id)
//            emit(Result.Success(financeDetailResponse))
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emit(Result.Error(e.message.toString()))
//        }
//    }

    fun getDetailKandang(kandangId: Int) = liveData {
        emit(Result.Loading)
        try {
            val detailKandangResponse = apiService.getDetailKandang(kandangId)
            emit(Result.Success(detailKandangResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun updateTernak(
        id: Int,
        status: String,
        year: String,
        month: String,
    ) = liveData {
        emit(Result.Loading)
        try {
            val updateTernakResponse = apiService.updateTernak(
                id, status, year, month
            )
            emit(Result.Success(updateTernakResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun searchAnything(query: String) = liveData {
        emit(Result.Loading)
        try {
            val searchResponse = apiService.searchQuery(
                query
            )
            emit(Result.Success(searchResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun updateImage(id: String, image: File) = liveData {
        emit(Result.Loading)
        try {
            val idMultipart = id.toRequestBody("text/plain".toMediaType())
            val imageMultipart = MultipartBody.Part.createFormData(
                "image",
                image.name,
                image.asRequestBody("image/jpeg".toMediaTypeOrNull()),
            )
            val updateImageResponse = apiService.updateImage(
                idMultipart, imageMultipart
            )
            emit(Result.Success(updateImageResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getNotifications() = liveData {
        emit(Result.Loading)
        try {
            val notificationResponse = apiService.getNotifications()
            emit(Result.Success(notificationResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun updatePenawaran(id: Int, status: String) = liveData {
        emit(Result.Loading)
        try {
            val updatePenawaranResponse = apiService.updatePenawaran(id, status)
            emit(Result.Success(updatePenawaranResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getSliderCategories() = liveData {
        emit(Result.Loading)
        try {
            val sliderCategoryResponse = apiService.getSliderCategories()
            emit(Result.Success(sliderCategoryResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

//    fun getSliderCategoryItem(slug: String) = liveData {
//        emit(Result.Loading)
//        try {
//            val sliderItemResponse = apiService.getSliderCategoryItem(slug)
//            emit(Result.Success(sliderItemResponse))
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emit(Result.Error(e.message.toString()))
//        }
//    }

    fun storeTernakFree(
        kandangId: Int,
        pakan: String,
        limbahId: Int,
        gender: String,
        age: String,
        typeId: Int,
        nominal: Int,
        status: String
    ) = liveData {
        emit(Result.Loading)
        try {
            val birthResponse = apiService.storeTernakFree(
                kandangId, pakan, limbahId, gender, age, typeId, nominal, status
            )
            emit(Result.Success(birthResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getNotificationsNegotiateResult() = liveData {
        emit(Result.Loading)
        try {
            val negotiationResponse = apiService.getNotificationsNegotiateResult(
            )
            emit(Result.Success(negotiationResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }
}