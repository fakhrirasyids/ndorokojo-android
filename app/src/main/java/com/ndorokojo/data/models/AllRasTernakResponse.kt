package com.ndorokojo.data.models

import com.google.gson.annotations.SerializedName

data class AllRasTernakResponse(

	@field:SerializedName("payload")
	val ternakItem: List<TernakItem?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class RasTernakItem(

	@field:SerializedName("parent_type_id")
	val parentTypeId: String? = null,

	@field:SerializedName("livestock_type")
	val livestockType: String? = null,

	@field:SerializedName("image")
	val image: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("level")
	val level: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class TernakItem(

	@field:SerializedName("parent_type_id")
	val parentTypeId: Any? = null,

	@field:SerializedName("livestock_type")
	val livestockType: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("level")
	val level: String? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("livestock_children")
	val rasTernakItem: List<RasTernakItem?>? = null,

	@field:SerializedName("id")
	val id: Int
)
