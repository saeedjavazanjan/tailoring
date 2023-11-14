package com.saeeed.devejump.project.tailoring.domain.model

import com.google.gson.annotations.SerializedName

data class UserPublicData(
    @SerializedName("userid")
    var userId: Int,

    @SerializedName("user_name")
    var userName:String,

    @SerializedName("avatar")
    var avatar:String,
)