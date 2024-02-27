package com.saeeed.devejump.project.tailoring.domain.model

import android.net.Uri
import java.time.LocalDateTime
import java.util.Date

data class CreatedPost(
    val title: String,
    val postType:String,
    val category: String,
    val featuredImage: List<Uri?> = listOf(),
    val like: Int = 0,
    val videoUri: Uri,
    val description:String,
    val longDataAdded:Long,
    val haveProduct: Int=0,
)