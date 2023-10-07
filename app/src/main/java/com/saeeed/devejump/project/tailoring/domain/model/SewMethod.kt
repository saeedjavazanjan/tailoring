package com.saeeed.devejump.project.tailoring.domain.model
data class SewMethod (
    val id: Int,
    val title: String,
    val publisher: String,
    val featuredImage: String,
    val rating: Int = 0,
    val sourceUrl: String,
    val ingredients: List<String> = listOf(),
    val dateAdded: String,
    val dateUpdated: String,
)