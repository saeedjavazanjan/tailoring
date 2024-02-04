package com.saeeed.devejump.project.tailoring.domain.model


data class UserData (
    var userId: Int,
    var userName:String="",
    var phoneNumber:String="",
    var avatar:String="",
    var bio:String="",
    var likes:  List<String> = listOf(),
    var bookMarks:   List<String> = listOf(),
    var followers:Int,
    var following:Int,
)
