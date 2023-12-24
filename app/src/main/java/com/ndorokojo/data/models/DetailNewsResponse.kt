package com.ndorokojo.data.models

import com.google.gson.annotations.SerializedName

data class DetailNewsResponse(

	@field:SerializedName("payload")
	val detailNews: DetailNews? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DetailNews(

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("author_id")
	val authorId: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)
