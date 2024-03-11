package com.saeeed.devejump.project.tailoring.domain.model

import android.net.Uri

data class OnUpdatePost(
    val title: String,
    val category: String,
    val description:String,
    val longDataAdded:Long,
    val haveProduct: Int=0,
    )
