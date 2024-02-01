package com.ndorokojo.data.models

import com.google.gson.annotations.SerializedName

data class BirthResponse(

//	@field:SerializedName("payload")
//	val birth: Birth? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Birth(

	@field:SerializedName("acquired_status")
	val acquiredStatus: String? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("limbah_id")
	val limbahId: Int? = null,

	@field:SerializedName("type_id")
	val typeId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("availability")
	val availability: String? = null,

	@field:SerializedName("acquired_year")
	val acquiredYear: String? = null,

	@field:SerializedName("pakan_id")
	val pakanId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("kandang_id")
	val kandangId: Int? = null,

	@field:SerializedName("acquired_month_name")
	val acquiredMonthName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("acquired_month")
	val acquiredMonth: String? = null,

	@field:SerializedName("age")
	val age: String? = null
)
