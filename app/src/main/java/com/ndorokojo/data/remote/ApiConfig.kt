package com.ndorokojo.data.remote

import com.ndorokojo.BuildConfig
import com.ndorokojo.utils.Constants.API_ENDPOINT
import com.ndorokojo.utils.UserPreferences.Companion.preferenceDefaultValue
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun getApiService(token: String): ApiService {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(API_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                if (token != preferenceDefaultValue) {
                    OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .addInterceptor(Interceptor { chain ->
                            val req = chain.request()
                            val reqHeaders = req.newBuilder()
                                .addHeader("Authorization", "Bearer $token")
                                .build()
                            chain.proceed(reqHeaders)
                        })
                        .build()
                } else {
                    OkHttpClient.Builder()
                        .addInterceptor(loggingInterceptor)
                        .build()
                }
            )
            .build()
        return retrofit.create(ApiService::class.java)
    }
}