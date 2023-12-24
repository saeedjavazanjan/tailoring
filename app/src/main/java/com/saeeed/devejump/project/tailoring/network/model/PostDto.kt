package com.saeeed.devejump.project.tailoring.network.model

import com.google.gson.annotations.SerializedName

data class PostDto(

    @SerializedName("id")
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
    var haveProduct: Int,
)

