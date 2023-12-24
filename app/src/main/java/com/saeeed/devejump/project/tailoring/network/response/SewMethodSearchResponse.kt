package com.saeeed.devejump.project.tailoring.network.response

import com.google.gson.annotations.SerializedName
import com.saeeed.devejump.project.tailoring.network.model.PostDto

data class SewMethodSearchResponse(

    @SerializedName("count")
    var count: Int,

    @SerializedName("results")
    var sewmethods: List<PostDto>,
)