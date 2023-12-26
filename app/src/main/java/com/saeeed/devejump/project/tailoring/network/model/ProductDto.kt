package com.saeeed.devejump.project.tailoring.network.model

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("id")
    val id:Int,

    @SerializedName("name")
    val name:String,

    @SerializedName("description")
    val description:String,

    @SerializedName("images")
    val images:List<String> = mutableListOf(),

    @SerializedName("type_of_product")
    val typeOfProduct:String,

    @SerializedName("mas")
    val mas:String="0",

    @SerializedName("supply")
    val supply:String="0",

    @SerializedName("unit")
    val unit:String,

    @SerializedName("price")
    val price:String,

    @SerializedName("post_id")
    val postId:Int,

    @SerializedName("attached_file")
    val attachedFile:String=""
)
