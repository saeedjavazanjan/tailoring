package com.saeeed.devejump.project.tailoring.network.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
data class CommentDto(

    @field:SerializedName("date")
    val date: Long? = null,

    @field:SerializedName("userId")
    val userId: Int? = null,

    @field:SerializedName("commentText")
    val comment: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("avatar")
    val avatar: String? = null,

    @field:SerializedName("postId")
    val postId: Int? = null,

    @field:SerializedName("userName")
    val userName: String? = null
)

