package com.ndorokojo.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ndorokojo.data.models.Village
import com.ndorokojo.utils.UserPreferences.Companion.userPreferencesName
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = userPreferencesName)

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    fun getAccessToken() = dataStore.data.map { it[PREF_ACCESS_TOKEN] ?: preferenceDefaultValue }
    fun getUsername() = dataStore.data.map { it[PREF_USERNAME] ?: preferenceDefaultValue }
    fun getFullname() = dataStore.data.map { it[PREF_FULLNAME] ?: preferenceDefaultValue }
    fun getEmail() = dataStore.data.map { it[PREF_EMAIL] ?: preferenceDefaultValue }

    fun getCode() = dataStore.data.map { it[PREF_CODE] ?: preferenceDefaultValue }
    fun getOccupation() = dataStore.data.map { it[PREF_OCCUPATION] ?: preferenceDefaultValue }
    fun getGender() = dataStore.data.map { it[PREF_GENDER] ?: preferenceDefaultValue }
    fun getAge() = dataStore.data.map { it[PREF_AGE] ?: preferenceDefaultValue }
    fun getKelompokTernak() = dataStore.data.map { it[PREF_KELOMPOK_TERNAK] ?: preferenceDefaultValue }
    fun getProvinceId() = dataStore.data.map { it[PREF_PROVINCE_ID] ?: preferenceDefaultValue }
    fun getRegencyId() = dataStore.data.map { it[PREF_REGENCY_ID] ?: preferenceDefaultValue }
    fun getDistrictId() = dataStore.data.map { it[PREF_DISTRICT_ID] ?: preferenceDefaultValue }
    fun getVillageId() = dataStore.data.map { it[PREF_VILLAGE_ID] ?: preferenceDefaultValue }

    fun getIsProfileComplete() = dataStore.data.map { it[PREF_IS_PROFILE_COMPLETE] ?: false }

    suspend fun saveProfileCompleted(isProfileComplete: Boolean) {
        dataStore.edit { prefs -> prefs[PREF_IS_PROFILE_COMPLETE] = isProfileComplete }
    }

    suspend fun savePreferences(
        accessToken: String,
        username: String,
        fullname: String,
        email: String,
        code: String,
        phone: String,
        address: String,
        occupation: String,
        gender: String,
        age: String,
        kelompokTernak: String,
        provinceId: String,
        regencyId: String,
        districtId: String,
        villageId: String,
        isProfileComplete: Boolean
    ) {
        dataStore.edit { prefs ->
            prefs[PREF_ACCESS_TOKEN] = accessToken
            prefs[PREF_USERNAME] = username
            prefs[PREF_FULLNAME] = fullname
            prefs[PREF_EMAIL] = email
            prefs[PREF_CODE] = code
            prefs[PREF_PHONE] = phone
            prefs[PREF_ADDRESS] = address
            prefs[PREF_OCCUPATION] = occupation
            prefs[PREF_GENDER] = gender
            prefs[PREF_AGE] = age
            prefs[PREF_KELOMPOK_TERNAK] = kelompokTernak
            prefs[PREF_PROVINCE_ID] = provinceId
            prefs[PREF_REGENCY_ID] = regencyId
            prefs[PREF_DISTRICT_ID] = districtId
            prefs[PREF_VILLAGE_ID] = villageId
            prefs[PREF_IS_PROFILE_COMPLETE] = isProfileComplete
        }
    }

    suspend fun savePreferencesUpdate(
        username: String,
        fullname: String,
        email: String,
        code: String,
        phone: String,
        address: String,
        occupation: String,
        gender: String,
        age: String,
        kelompokTernak: String,
        provinceId: String,
        regencyId: String,
        districtId: String,
        villageId: String,
        isProfileComplete: Boolean
    ) {
        dataStore.edit { prefs ->
            prefs[PREF_USERNAME] = username
            prefs[PREF_FULLNAME] = fullname
            prefs[PREF_EMAIL] = email
            prefs[PREF_CODE] = code
            prefs[PREF_PHONE] = phone
            prefs[PREF_ADDRESS] = address
            prefs[PREF_OCCUPATION] = occupation
            prefs[PREF_GENDER] = gender
            prefs[PREF_AGE] = age
            prefs[PREF_KELOMPOK_TERNAK] = kelompokTernak
            prefs[PREF_PROVINCE_ID] = provinceId
            prefs[PREF_REGENCY_ID] = regencyId
            prefs[PREF_DISTRICT_ID] = districtId
            prefs[PREF_VILLAGE_ID] = villageId
            prefs[PREF_IS_PROFILE_COMPLETE] = isProfileComplete
        }
    }


    suspend fun clearPreferences() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>) = INSTANCE ?: synchronized(this) {
            val instance = UserPreferences(dataStore)
            INSTANCE = instance
            instance
        }

        const val userPreferencesName = "userPreferences"

        val PREF_ACCESS_TOKEN = stringPreferencesKey("pref_access_token")
        val PREF_USERNAME = stringPreferencesKey("pref_username")
        val PREF_FULLNAME = stringPreferencesKey("pref_fullname")
        val PREF_EMAIL = stringPreferencesKey("pref_email")
        val PREF_CODE = stringPreferencesKey("pref_code")
        val PREF_PHONE = stringPreferencesKey("pref_phone")
        val PREF_ADDRESS = stringPreferencesKey("pref_address")
        val PREF_OCCUPATION = stringPreferencesKey("pref_occupation")
        val PREF_GENDER = stringPreferencesKey("pref_gender")
        val PREF_AGE = stringPreferencesKey("pref_age")
        val PREF_KELOMPOK_TERNAK = stringPreferencesKey("pref_kelompok_ternak")
        val PREF_PROVINCE_ID = stringPreferencesKey("pref_province_id")
        val PREF_REGENCY_ID = stringPreferencesKey("pref_regency_id")
        val PREF_DISTRICT_ID = stringPreferencesKey("pref_district_id")
        val PREF_VILLAGE_ID = stringPreferencesKey("pref_village_id")

        val PREF_IS_PROFILE_COMPLETE = booleanPreferencesKey("pref_is_profile_complete")

        const val preferenceDefaultValue: String = "preferences_default_value"
    }
}