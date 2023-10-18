package com.saeeed.devejump.project.tailoring.domain.model

import com.google.gson.annotations.SerializedName

data class Banner(
    var id: Int,
    var title: String,
    var imageURL: String,
    var clickDestination: String,
)