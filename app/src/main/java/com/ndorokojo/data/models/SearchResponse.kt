package com.ndorokojo.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class SearchResponse(

    @field:SerializedName("payload")
    val searched: Searched? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

@Parcelize
data class SearchedKandang(

    @field:SerializedName("village_id")
    val villageId: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("regency_id")
    val regencyId: String? = null,

    @field:SerializedName("type_id")
    val typeId: String? = null,

    @field:SerializedName("latitude")
    val latitude: String? = null,

    @field:SerializedName("luas")
    val luas: @RawValue Any? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("panjang")
    val panjang: String? = null,

    @field:SerializedName("sensor_status")
    val sensorStatus: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("province_id")
    val provinceId: String? = null,

    @field:SerializedName("farmer_id")
    val farmerId: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("jenis")
    val jenis: String? = null,

    @field:SerializedName("farmer")
    val farmer: @RawValue SearchedFarmer? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("lebar")
    val lebar: String? = null,

    @field:SerializedName("district_id")
    val districtId: String? = null,

    @field:SerializedName("rt_rw")
    val rtRw: String? = null,

    @field:SerializedName("longitude")
    val longitude: String? = null
) : Parcelable

data class SearchedNewsItem(

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null
)

@Parcelize
data class SearchedFarmer(

    @field:SerializedName("village_id")
    val villageId: String? = null,

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("occupation")
    val occupation: String? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("regency_id")
    val regencyId: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("kelompok_ternak")
    val kelompokTernak: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("phone")
    val phone: String? = null,

    @field:SerializedName("province_id")
    val provinceId: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("fullname")
    val fullname: String? = null,

    @field:SerializedName("district_id")
    val districtId: String? = null,

    @field:SerializedName("remember_token")
    val rememberToken: @RawValue Any? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("age")
    val age: String? = null,

    @field:SerializedName("username")
    val username: String? = null
) : Parcelable

@Parcelize
data class SearchedLivestockType(

    @field:SerializedName("parent_type_id")
    val parentTypeId: @RawValue String? = null,

    @field:SerializedName("livestock_type")
    val livestockType: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: @RawValue Any? = null,

    @field:SerializedName("level")
    val level: String? = null,

    @field:SerializedName("created_at")
    val createdAt: @RawValue Any? = null,

    @field:SerializedName("id")
    val id: Int? = null
) : Parcelable

@Parcelize
data class SearchedEventItem(

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("gender")
    val gender: @RawValue Any? = null,

    @field:SerializedName("sold_month_name")
    val soldMonthName: @RawValue Any? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("availability")
    val availability: String? = null,

    @field:SerializedName("sensor_latitude")
    val sensorLatitude: @RawValue Any? = null,

    @field:SerializedName("sensor_batterypercent")
    val sensorBatterypercent: @RawValue Any? = null,

    @field:SerializedName("dead_type")
    val deadType: @RawValue Any? = null,

    @field:SerializedName("sold_year")
    val soldYear: @RawValue Any? = null,

    @field:SerializedName("is_mine")
    val isMine: Boolean? = null,

    @field:SerializedName("sold_proposed_price")
    val soldProposedPrice: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("nominal")
    val nominal: String? = null,

    @field:SerializedName("dead_reason")
    val deadReason: @RawValue Any? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("acquired_month")
    val acquiredMonth: String? = null,

    @field:SerializedName("dead_year")
    val deadYear: @RawValue Any? = null,

    @field:SerializedName("acquired_status")
    val acquiredStatus: String? = null,

    @field:SerializedName("sold_deal_price")
    val soldDealPrice: @RawValue Any? = null,

    @field:SerializedName("dead_month")
    val deadMonth: @RawValue Any? = null,

    @field:SerializedName("sensor_gps_type")
    val sensorGpsType: @RawValue Any? = null,

    @field:SerializedName("limbah_id")
    val limbahId: String? = null,

    @field:SerializedName("sensor_longitude")
    val sensorLongitude: @RawValue Any? = null,

    @field:SerializedName("type_id")
    val typeId: String? = null,

    @field:SerializedName("acquired_year")
    val acquiredYear: String? = null,

    @field:SerializedName("dead_month_name")
    val deadMonthName: @RawValue Any? = null,

    @field:SerializedName("livestock_type")
    val livestockType: @RawValue SearchedLivestockType? = null,

    @field:SerializedName("pakan_id")
    val pakanId: String? = null,

    @field:SerializedName("sensor_status")
    val sensorStatus: String? = null,

    @field:SerializedName("kandang")
    val kandang: @RawValue SearchedKandang? = null,

    @field:SerializedName("kandang_id")
    val kandangId: String? = null,

    @field:SerializedName("acquired_month_name")
    val acquiredMonthName: String? = null,

    @field:SerializedName("sensor_report")
    val sensorReport: @RawValue Any? = null,

    @field:SerializedName("sold_month")
    val soldMonth: @RawValue Any? = null,

    @field:SerializedName("age")
    val age: String? = null
) : Parcelable

data class Searched(

    @field:SerializedName("kandang")
    val kandang: List<SearchedKandangItem?>? = null,

    @field:SerializedName("today")
    val today: List<SearchedNewsItem?>? = null,

    @field:SerializedName("event")
    val event: List<SearchedEventItem?>? = null,

    @field:SerializedName("finance")
    val finance: List<SearchedNewsItem?>? = null
)

data class SearchedKandangItem(

    @field:SerializedName("livestock_type")
    val livestockType: SearchedLivestockType? = null,

    @field:SerializedName("statistic")
    val statistic: SearchedStatistic? = null,

    @field:SerializedName("type_id")
    val typeId: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("livestocks")
    val livestocks: List<Any?>? = null,

    @field:SerializedName("id")
    val id: Int? = null
)

data class SearchedStatistic(

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
