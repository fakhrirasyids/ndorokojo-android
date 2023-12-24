package com.ndorokojo.data.models

import com.google.gson.annotations.SerializedName

data class ListKandangResponse(

    @field:SerializedName("payload")
    val kandang: List<Kandang?>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class Kandang(

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

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("panjang")
    val panjang: String? = null,

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

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("lebar")
    val lebar: String? = null,

    @field:SerializedName("district_id")
    val districtId: String? = null,

    @field:SerializedName("rt_rw")
    val rtRw: String? = null,

    @field:SerializedName("longitude")
    val longitude: String? = null,

    @field:SerializedName("livestocks_count")
    val livestocksCount: String? = null
)
