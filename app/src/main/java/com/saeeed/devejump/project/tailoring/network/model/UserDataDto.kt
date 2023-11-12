package com.saeeed.devejump.project.tailoring.network.model

import com.google.gson.annotations.SerializedName

data class UserDataDto (

    @SerializedName("id")
    var id: Int,

    @SerializedName("likes")
    var likes: List<String> = emptyList(),

    @SerializedName("bookMarks")
    var bookMarks: List<String> = emptyList(),

    @SerializedName("comments")
    var comments: List<UserComments> = emptyList()

    )

data class UserComments(
    @SerializedName("id")
    var postId: String,

    @SerializedName("comment")
    var comment: String,


    )

