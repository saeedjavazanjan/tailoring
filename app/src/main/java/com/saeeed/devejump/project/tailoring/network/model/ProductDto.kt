package com.saeeed.devejump.project.tailoring.network.model

import android.net.Uri
import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("Id")
    val id:Int,

    @SerializedName("Name")
    val name:String,

    @SerializedName("Description")
    val description:String,

    @SerializedName("Images")
    val images:List<String> = mutableListOf(),

    @SerializedName("typeOfProduct")
    val typeOfProduct:String,

    @SerializedName("Mas")
    val mas:String="0",

    @SerializedName("Supply")
    val supply:String="0",

    @SerializedName("Unit")
    val unit:String,

    @SerializedName("Price")
    val price:String,

    @SerializedName("postId")
    val postId:Int,

    @SerializedName("AttachedFile")
    val attachedFile:String=""
)
