package com.saeeed.devejump.project.tailoring.network.model

import com.google.gson.annotations.SerializedName

data class ProductDto(

	@field:SerializedName("unit")
	val unit: String? = null,

	@field:SerializedName("images")
	val images: List<String?>? = null,

	@field:SerializedName("typeOfProduct")
	val typeOfProduct: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("postId")
	val postId: Int? = null,

	@field:SerializedName("supply")
	val supply: String? = null,

	@field:SerializedName("mas")
	val mas: String? = null,

	@field:SerializedName("attachedFile")
	val attachedFile: String? = null
)
