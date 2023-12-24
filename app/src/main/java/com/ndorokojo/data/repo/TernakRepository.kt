package com.ndorokojo.data.repo

import androidx.lifecycle.liveData
import com.ndorokojo.data.models.KandangPayload
import com.ndorokojo.data.models.StoreTernakPayload
import com.ndorokojo.data.remote.ApiService
import com.ndorokojo.utils.Result

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
        pakanId: Int,
        limbahId: Int,
        age: String,
        typeId: Int,
    ) = liveData {
        emit(Result.Loading)
        try {
            val birthResponse = apiService.birthTernak(
                kandangId, pakanId, limbahId, age, typeId
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
                liveStockId, kandangId, dealPrice
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

    fun getBrebesDetail(id: Int) = liveData {
        emit(Result.Loading)
        try {
            val brebesDetailResponse = apiService.getDetailBrebesToday(id)
            emit(Result.Success(brebesDetailResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFinanceDetail(id: Int) = liveData {
        emit(Result.Loading)
        try {
            val financeDetailResponse = apiService.getFinanceToday(id)
            emit(Result.Success(financeDetailResponse))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

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
}