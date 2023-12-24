package com.ndorokojo.data.models

import com.google.gson.annotations.SerializedName

data class StoreKandangResponse(

    @field:SerializedName("payload")
    val storeKandang: StoreKandang? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class StoreKandang(

    @field:SerializedName("village_id")
    val villageId: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("regency_id")
    val regencyId: String? = null,

    @field:SerializedName("type_id")
    val typeId: Int? = null,

    @field:SerializedName("latitude")
    val latitude: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("panjang")
    val panjang: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("province_id")
    val provinceId: String? = null,

    @field:SerializedName("farmer_id")
    val farmerId: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("jenis")
    val jenis: String? = null,

    @field:SerializedName("lebar")
    val lebar: Int? = null,

    @field:SerializedName("district_id")
    val districtId: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("rt_rw")
    val rtRw: String? = null,

    @field:SerializedName("longitude")
    val longitude: String? = null
)
