package com.saeeed.devejump.project.tailoring.domain.model

import android.net.Uri
import java.util.Date

data class CreatedPost (
    val id: Int,
    val title: String,
    val postType:String,
    val publisher: String,
    val authorId:Int,
    val featuredImage: List<Uri?> = listOf(),
    val like: Int = 0,
    val videoUri: Uri,
    val description:String,
    val dateAdded: Date,
    val haveProduct: Int=0,
)