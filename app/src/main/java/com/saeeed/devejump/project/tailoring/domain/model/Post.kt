package com.saeeed.devejump.project.tailoring.domain.model

import java.util.Date

data class Post (
    val id: Int,
    val title: String,
    val category:String,
    val postType:String,
    val publisher: String,
    val authorId:Int,
    val authorAvatar:String,
    val featuredImage: List<String> = listOf(),
    val like: Int = 0,
    val videoUrl: String,
    val description:String,
    val dateAdded: String,
    val longDataAdded:Long,
    val haveProduct: Int=0,
)