package com.saeeed.devejump.project.tailoring.network.model

import com.google.gson.annotations.SerializedName
import com.saeeed.devejump.project.tailoring.domain.model.Comment

data class SewMethodDto(

    @SerializedName("id")
    var pk: Int,

    @SerializedName("title")
    var title: String,

    @SerializedName("post_type")
    var postType: String,

    @SerializedName("publisher")
    var publisher: String,

    @SerializedName("featured_image")
    var featuredImage: String,

    @SerializedName("like")
    var like: Int = 0,

    @SerializedName("video")
    var videoUrl: String,

    @SerializedName("description")
    var description: String,

    @SerializedName("long_date_added")
    var longDateAdded: Long,

    @SerializedName("long_date_updated")
    var longDateUpdated: Long,
)

