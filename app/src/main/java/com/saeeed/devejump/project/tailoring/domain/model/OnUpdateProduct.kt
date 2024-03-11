package com.saeeed.devejump.project.tailoring.domain.model

import android.net.Uri

data class OnUpdateProduct(
    val name:String,
    val description:String,
    val typeOfProduct:String="محصول فیزیکی",
    val mas:String,
    val supply:String,
    val unit:String,
    val price:String,
    val attachedFile:String=""
)
