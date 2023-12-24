package com.ndorokojo.data.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("farmer")
	val farmer: Farmer? = null,

	@field:SerializedName("token_type")
	val tokenType: String? = null,

	@field:SerializedName("expires_in")
	val expiresIn: Int? = null
)

data class Farmer(

	@field:SerializedName("village_id")
	val villageId: String? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("occupation")
	val occupation: String? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("regency_id")
	val regencyId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("kelompok_ternak")
	val kelompokTernak: String? = null,

	@field:SerializedName("is_profile_complete")
	val isProfileComplete: Boolean? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("province_id")
	val provinceId: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("district_id")
	val districtId: String? = null,

	@field:SerializedName("remember_token")
	val rememberToken: Any? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("age")
	val age: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
