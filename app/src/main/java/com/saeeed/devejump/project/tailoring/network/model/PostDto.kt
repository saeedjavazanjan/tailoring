package com.saeeed.devejump.project.tailoring.network.model

import com.google.gson.annotations.SerializedName

data class PostDto(
    @field:SerializedName("longDataAdded")
    val longDataAdded: Long? = null,

    @field:SerializedName("haveProduct")
    val haveProduct: Int? = null,

    @field:SerializedName("postType")
    val postType: String? = null,

    @field:SerializedName("like")
    val like: Int? = null,

    @field:SerializedName("author")
    val author: String? = null,

    @field:SerializedName("featuredImages")
    val featuredImages: List<String>? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("video")
    val video: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("authorId")
    val authorId: Int? = null,
    @field:SerializedName("authorAvatar")
    val authorAvatar: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("dataAdded")
    val dataAdded: String? = null
   /* @SerializedName("id")
    var pk: Int,

    @SerializedName("title")
    var title: String,

    @SerializedName("post_type")
    var postType: String,

    @SerializedName("publisher")
    var publisher: String,

    @SerializedName("author_id")
    var authorId:Int,


    @SerializedName("featured_image")
    var featuredImage: List<String> = emptyList(),
    @SerializedName("like")
    var like: Int = 0,

    @SerializedName("video")
    var videoUrl: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("long_date_added")
    var longDateAdded: Long,

    @SerializedName("have_product")
    var haveProduct: Int,*/
)

