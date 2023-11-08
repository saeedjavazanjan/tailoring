package com.saeeed.devejump.project.tailoring.domain.model

data class UserData (
    var id: Int,
    var likes:  List<String> = listOf(),
    var bookMarks:   List<String> = listOf(),
    var comments:  List<String> = listOf()

)
