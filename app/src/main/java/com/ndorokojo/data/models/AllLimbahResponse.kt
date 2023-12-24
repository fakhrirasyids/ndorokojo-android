package com.ndorokojo.data.models

import com.google.gson.annotations.SerializedName

data class AllLimbahResponse(

	@field:SerializedName("payload")
	val limbah: List<Limbah?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Limbah(

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("pengolahan_limbah")
	val pengolahanLimbah: String? = null
)
