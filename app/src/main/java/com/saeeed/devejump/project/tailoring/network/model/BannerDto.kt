package com.saeeed.devejump.project.tailoring.network.model

import com.google.gson.annotations.SerializedName

data class BannerDto(

    @SerializedName("id")
    var id: Int,

    @SerializedName("title")
    var title: String,

    @SerializedName("image_url")
    var imageURL: String,

    @SerializedName("click_destination")
    var clickDestination: String,


)
