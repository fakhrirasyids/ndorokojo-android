package com.ndorokojo.data.models

import com.google.gson.annotations.SerializedName

data class VillageResponse(

	@field:SerializedName("payload")
	val village: List<Village?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Village(

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("district_id")
	val districtId: String? = null
)
