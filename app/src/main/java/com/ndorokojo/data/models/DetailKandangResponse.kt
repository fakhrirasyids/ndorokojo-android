package com.ndorokojo.data.models

import com.google.gson.annotations.SerializedName

data class DetailKandangResponse(

    @field:SerializedName("payload")
    val detailKandang: DetailKandang? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class LivestocksItem(

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("gender")
    val gender: Any? = null,

    @field:SerializedName("sold_month_name")
    val soldMonthName: Any? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("availability")
    val availability: String? = null,

    @field:SerializedName("sensor_latitude")
    val sensorLatitude: Any? = null,

    @field:SerializedName("sensor_batterypercent")
    val sensorBatterypercent: Any? = null,

    @field:SerializedName("dead_type")
    val deadType: Any? = null,

    @field:SerializedName("sold_year")
    val soldYear: Any? = null,

    @field:SerializedName("sold_proposed_price")
    val soldProposedPrice: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("nominal")
    val nominal: String? = null,

    @field:SerializedName("dead_reason")
    val deadReason: Any? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("acquired_month")
    val acquiredMonth: String? = null,

    @field:SerializedName("dead_year")
    val deadYear: Any? = null,

    @field:SerializedName("acquired_status")
    var acquiredStatus: String? = null,

    @field:SerializedName("sold_deal_price")
    val soldDealPrice: Any? = null,

    @field:SerializedName("dead_month")
    val deadMonth: Any? = null,

    @field:SerializedName("sensor_gps_type")
    val sensorGpsType: Any? = null,

    @field:SerializedName("limbah_id")
    val limbahId: String? = null,

    @field:SerializedName("sensor_longitude")
    val sensorLongitude: Any? = null,

    @field:SerializedName("type_id")
    val typeId: String? = null,

    @field:SerializedName("acquired_year")
    val acquiredYear: String? = null,

    @field:SerializedName("dead_month_name")
    val deadMonthName: Any? = null,

    @field:SerializedName("pakan_id")
    val pakanId: String? = null,

    @field:SerializedName("sensor_status")
    val sensorStatus: String? = null,

    @field:SerializedName("kandang_id")
    val kandangId: String? = null,

    @field:SerializedName("acquired_month_name")
    val acquiredMonthName: String? = null,

    @field:SerializedName("sensor_report")
    val sensorReport: Any? = null,

    @field:SerializedName("sold_month")
    val soldMonth: Any? = null,

    @field:SerializedName("age")
    val age: String? = null
)

data class DetailKandang(

    @field:SerializedName("village_id")
    val villageId: String? = null,

    @field:SerializedName("statistic")
    val statistic: Statistic? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("regency_id")
    val regencyId: String? = null,

    @field:SerializedName("type_id")
    val typeId: String? = null,

    @field:SerializedName("latitude")
    val latitude: Any? = null,

    @field:SerializedName("livestocks")
    val livestocks: List<LivestocksItem?>? = null,

    @field:SerializedName("luas")
    val luas: String? = null,

    @field:SerializedName("created_at")
    val createdAt: Any? = null,

    @field:SerializedName("panjang")
    val panjang: String? = null,

    @field:SerializedName("sensor_status")
    val sensorStatus: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: Any? = null,

    @field:SerializedName("province_id")
    val provinceId: String? = null,

    @field:SerializedName("farmer_id")
    val farmerId: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("jenis")
    val jenis: String? = null,

    @field:SerializedName("livestocks_count")
    val livestocksCount: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("lebar")
    val lebar: String? = null,

    @field:SerializedName("district_id")
    val districtId: String? = null,

    @field:SerializedName("rt_rw")
    val rtRw: String? = null,

    @field:SerializedName("longitude")
    val longitude: Any? = null
)

data class Statistic(

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("jual")
    val jual: Int? = null,

    @field:SerializedName("available")
    val available: Int? = null,

    @field:SerializedName("mati")
    val mati: Int? = null,

    @field:SerializedName("beli")
    val beli: Int? = null
)
