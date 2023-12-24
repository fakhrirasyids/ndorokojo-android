package com.ndorokojo.data.models

import com.google.gson.annotations.SerializedName

data class StoreTernakResponse(

	@field:SerializedName("payload")
	val storeTernakBody: StoreTernakBody? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Livestock(

	@field:SerializedName("acquired_status")
	val acquiredStatus: String? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("sensor_gps_type")
	val sensorGpsType: String? = null,

	@field:SerializedName("limbah_id")
	val limbahId: Int? = null,

	@field:SerializedName("sensor_longitude")
	val sensorLongitude: String? = null,

	@field:SerializedName("type_id")
	val typeId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("sensor_latitude")
	val sensorLatitude: String? = null,

	@field:SerializedName("acquired_year")
	val acquiredYear: String? = null,

	@field:SerializedName("sensor_batterypercent")
	val sensorBatterypercent: Int? = null,

	@field:SerializedName("pakan_id")
	val pakanId: Int? = null,

	@field:SerializedName("sensor_status")
	val sensorStatus: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("kandang_id")
	val kandangId: Int? = null,

	@field:SerializedName("sensor_report")
	val sensorReport: String? = null,

	@field:SerializedName("acquired_month_name")
	val acquiredMonthName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("acquired_month")
	val acquiredMonth: String? = null,

	@field:SerializedName("age")
	val age: String? = null
)

data class StoreTernakBody(

	@field:SerializedName("kandang")
	val kandang: Kandang? = null,

	@field:SerializedName("livestock")
	val livestock: Livestock? = null
)