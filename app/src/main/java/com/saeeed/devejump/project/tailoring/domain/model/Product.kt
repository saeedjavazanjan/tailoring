package com.saeeed.devejump.project.tailoring.domain.model

import android.net.Uri

data class Product(
    val id:Int,
    val name:String,
    val description:String,
    val images:List<Uri?> = mutableListOf(Uri.EMPTY),
    val typeOfProduct:String,
    val mas:String="0",
    val supply:String="0",
    val unit:String,
    val price:String,
    val postId:Int
)