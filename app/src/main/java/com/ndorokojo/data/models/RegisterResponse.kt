package com.ndorokojo.data.models

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

//	@field:SerializedName("farmer")
//	val farmer: FarmerRegister? = null,

	@field:SerializedName("token_type")
	val tokenType: String? = null,

	@field:SerializedName("expires_in")
	val expiresIn: Int? = null
)

//data class FarmerRegister(
//
//	@field:SerializedName("is_profile_complete")
//	val isProfileComplete: Boolean? = null,
//
//	@field:SerializedName("code")
//	val code: String? = null,
//
//	@field:SerializedName("updated_at")
//	val updatedAt: String? = null,
//
//	@field:SerializedName("created_at")
//	val createdAt: String? = null,
//
//	@field:SerializedName("fullname")
//	val fullname: String? = null,
//
//	@field:SerializedName("id")
//	val id: Int? = null,
//
//	@field:SerializedName("email")
//	val email: String? = null,
//
//	@field:SerializedName("username")
//	val username: String? = null
//)
