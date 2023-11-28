package com.saeeed.devejump.project.tailoring.domain.model

import java.util.Date

data class SewMethod (
    val id: Int,
    val title: String,
    val postType:String,
    val publisher: String,
    val featuredImage: List<String> = listOf(),
    val like: Int = 0,
    val videoUrl: String,
    val description:String,
    val dateAdded: Date,
    val dateUpdated: Date,
)