package com.saeeed.devejump.project.tailoring.network.model

import com.google.gson.annotations.SerializedName

data class PeoplesDto (

    @SerializedName("userid")
    var userId: Int,

    @SerializedName("user_name")
    var userName:String="",

    @SerializedName("phone_number")
    var phoneNumber:String="",

    @SerializedName("avatar")
    var avatar:String="",

    @SerializedName("bio")
    var bio:String="",
    @SerializedName("followers")
    var followers:Int=0,

    @SerializedName("following")
    var following:Int=0,

    @SerializedName("considered_user_id")
    var consideredUseId:Int

)

