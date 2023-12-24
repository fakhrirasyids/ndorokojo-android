package com.ndorokojo.data.models

import com.google.gson.annotations.SerializedName

data class DistrictResponse(

	@field:SerializedName("payload")
	val district: List<District?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class District(

	@field:SerializedName("regency_id")
	val regencyId: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
