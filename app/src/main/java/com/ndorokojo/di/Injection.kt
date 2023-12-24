package com.ndorokojo.di

import android.content.Context
import com.ndorokojo.data.remote.ApiConfig
import com.ndorokojo.data.remote.ApiService
import com.ndorokojo.utils.UserPreferences
import com.ndorokojo.utils.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideUserPreferences(context: Context): UserPreferences {
        return UserPreferences.getInstance(context.dataStore)
    }

    fun provideApiService(context: Context): ApiService {
        val prefs = provideUserPreferences(context)
        val accessToken = runBlocking { prefs.getAccessToken().first() }
        return ApiConfig.getApiService(accessToken)
    }
}