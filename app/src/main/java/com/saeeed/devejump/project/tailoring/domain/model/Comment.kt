package com.saeeed.devejump.project.tailoring.domain.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Comment (
    var id: Int,
    var comment: String,
    var avatar: String,
    var userName: String,
    @SerializedName("user_id")
    var userId:Int,
    var date:String,
    var postId:Int
)