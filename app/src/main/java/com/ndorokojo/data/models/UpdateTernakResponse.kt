package com.ndorokojo.data.models

import com.google.gson.annotations.SerializedName

data class UpdateTernakResponse(

	@field:SerializedName("payload")
	val updateTernak: UpdateTernak? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class UpdateTernak(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("gender")
	val gender: Any? = null,

	@field:SerializedName("sold_month_name")
	val soldMonthName: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("availability")
	val availability: String? = null,

	@field:SerializedName("sensor_latitude")
	val sensorLatitude: Any? = null,

	@field:SerializedName("sensor_batterypercent")
	val sensorBatterypercent: Any? = null,

	@field:SerializedName("dead_type")
	val deadType: Any? = null,

	@field:SerializedName("sold_year")
	val soldYear: Any? = null,

	@field:SerializedName("sold_proposed_price")
	val soldProposedPrice: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("nominal")
	val nominal: String? = null,

	@field:SerializedName("dead_reason")
	val deadReason: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("acquired_month")
	val acquiredMonth: String? = null,

	@field:SerializedName("dead_year")
	val deadYear: String? = null,

	@field:SerializedName("acquired_status")
	val acquiredStatus: String? = null,

	@field:SerializedName("sold_deal_price")
	val soldDealPrice: Any? = null,

	@field:SerializedName("dead_month")
	val deadMonth: String? = null,

	@field:SerializedName("sensor_gps_type")
	val sensorGpsType: Any? = null,

	@field:SerializedName("limbah_id")
	val limbahId: String? = null,

	@field:SerializedName("sensor_longitude")
	val sensorLongitude: Any? = null,

	@field:SerializedName("type_id")
	val typeId: String? = null,

	@field:SerializedName("acquired_year")
	val acquiredYear: String? = null,

	@field:SerializedName("dead_month_name")
	val deadMonthName: String? = null,

	@field:SerializedName("pakan_id")
	val pakanId: String? = null,

	@field:SerializedName("sensor_status")
	val sensorStatus: String? = null,

	@field:SerializedName("kandang_id")
	val kandangId: String? = null,

	@field:SerializedName("acquired_month_name")
	val acquiredMonthName: String? = null,

	@field:SerializedName("sensor_report")
	val sensorReport: Any? = null,

	@field:SerializedName("sold_month")
	val soldMonth: Any? = null,

	@field:SerializedName("age")
	val age: String? = null
)
