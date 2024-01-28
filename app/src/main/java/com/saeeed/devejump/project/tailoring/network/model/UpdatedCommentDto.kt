package com.saeeed.devejump.project.tailoring.network.model

import com.google.gson.annotations.SerializedName

data class UpdatedCommentDto(
    @field:SerializedName("commentText")
    val commentText: String? = null,

    @field:SerializedName("date")
    val date: Long? = null,


    )
