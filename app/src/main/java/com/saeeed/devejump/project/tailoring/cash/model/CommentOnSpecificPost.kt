package com.saeeed.devejump.project.tailoring.cash.model

import com.google.gson.annotations.SerializedName

data class CommentOnSpecificPost(

    @SerializedName("postid")
    var postId:String,

    @SerializedName("comment")
    var comments:MutableList<CommentEntity> = mutableListOf()
)
