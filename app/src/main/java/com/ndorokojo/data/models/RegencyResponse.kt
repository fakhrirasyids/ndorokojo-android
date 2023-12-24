package com.ndorokojo.data.models

import com.google.gson.annotations.SerializedName

data class RegencyResponse(

    @field:SerializedName("payload")
    val regency: List<Regency?>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class Regency(

    @field:SerializedName("updated_at")
    val updatedAt: Any? = null,

    @field:SerializedName("province_id")
    val provinceId: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("created_at")
    val createdAt: Any? = null,

    @field:SerializedName("id")
    val id: Int? = null
)
