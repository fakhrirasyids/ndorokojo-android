package com.ndorokojo.data.models

import com.google.gson.annotations.SerializedName

data class AllPakanResponse(

	@field:SerializedName("payload")
	val pakan: List<Pakan?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Pakan(

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("jenis_pakan")
	val jenisPakan: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
