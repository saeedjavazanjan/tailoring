package com.saeeed.devejump.project.tailoring.domain.model

data class Product(
    val id:Int,
    val name:String,
    val description:String,
    val images:List<String> = listOf(),
    val typeOfProduct:String,
    val mas:Int=0,
    val supply:Int=0,
    val unit:String,
    val price:Int,
    val postId:Int
)