package com.ndorokojo.data.models

import com.google.gson.annotations.SerializedName

data class NewsResponse(

	@field:SerializedName("payload")
	val news: List<News?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class News(

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)
