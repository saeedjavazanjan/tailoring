package com.saeeed.devejump.project.tailoring.network.model

import com.google.gson.annotations.SerializedName
import com.saeeed.devejump.project.tailoring.domain.model.CommentOnSpecificPost
import com.saeeed.devejump.project.tailoring.domain.model.UserPublicData

data class UserDataDto (

    @SerializedName("userId")
    var userId: Int,

    @SerializedName("userName")
    var userName:String="",

    @SerializedName("password")
    var password:String="",

    @SerializedName("phoneNumber")
    var phoneNumber:String="",

    @SerializedName("avatar")
    var avatar:String="",

    @SerializedName("bio")
    var bio:String="",

    @SerializedName("followers")
    var followers:Int=0,

    @SerializedName("following")
    var following:Int=0,

    @SerializedName("likes")
    var likes: List<String> = emptyList(),

    @SerializedName("bookmarks")
    var bookMarks: List<String> = emptyList(),


    )

