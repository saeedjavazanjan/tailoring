package com.saeeed.devejump.project.tailoring.cash.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity
data class CommentEntity (
    @PrimaryKey(autoGenerate = false)
    var id: Int,
    var comment: String,
    var avatar: String,
    var userName: String,
    @SerializedName("user_id")
    var userId:Int,
    var date:String,
    var postId:Int
)