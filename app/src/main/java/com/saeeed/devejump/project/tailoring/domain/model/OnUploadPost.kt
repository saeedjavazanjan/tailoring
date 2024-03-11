package com.saeeed.devejump.project.tailoring.domain.model

import android.net.Uri

data class OnUploadPost(
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