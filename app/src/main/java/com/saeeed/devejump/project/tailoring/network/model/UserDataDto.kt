package com.saeeed.devejump.project.tailoring.network.model

import com.google.gson.annotations.SerializedName
import com.saeeed.devejump.project.tailoring.domain.model.CommentOnSpecificPost
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.domain.model.UserPublicData

data class UserDataDto (

    @SerializedName("userid")
    var userId: Int,

    @SerializedName("user_name")
    var userName:String="",

    @SerializedName("phone_number")
    var phoneNumber:String="",

    @SerializedName("avatar")
    var avatar:String="",

    @SerializedName("bio")
    var bio:String="",

    @SerializedName("followers")
    var followers:List<UserPublicData> = emptyList(),

    @SerializedName("following")
    var following:List<UserPublicData> = emptyList(),

    @SerializedName("likes")
    var likes: List<String> = emptyList(),

    @SerializedName("bookMarks")
    var bookMarks: List<String> = emptyList(),

    @SerializedName("comments")
    var comments: List<CommentOnSpecificPost> = emptyList()

    )


