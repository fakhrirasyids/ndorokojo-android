package com.ndorokojo.data.models

data class KandangPayload(
    val name: String,
    val type_id: Int,
    val panjang: Double,
    val lebar: Double,
    val jenis: String,
    val province_id: Int,
    val regency_id: Int,
    val district_id: Int,
    val village_id: String,
    val address: String,
    val rt_rw: String,
    val longitude: String,
    val latitude: String,
    val sensor_status: String,
    val sensor: SensorPayload
)

data class LivestockPayload(
    val pakan: String,
    val limbah_id: Int,
    val age: String,
    val type_id: Int,
    val nominal: Int,
    val gender: String
)

data class SensorPayload(
    val sensor_latitude: String? = null,
    val sensor_longitude: String? = null,
    val sensor_batterypercent: Int? = null,
    val sensor_gps_type: String? = null,
    val sensor_report: String? = null
)

data class StoreTernakPayload(
    val kandang: KandangPayload,
    val livestock: LivestockPayload
)