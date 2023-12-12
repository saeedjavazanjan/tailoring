package com.saeeed.devejump.project.tailoring.domain.model

data class Peoples(
    var userId: Int,
    var userName:String="",
    var phoneNumber:String="",
    var avatar:String="",
    var bio:String="",
    var followers:Int,
    var following:Int,
    var consideredUserId: Int

)
