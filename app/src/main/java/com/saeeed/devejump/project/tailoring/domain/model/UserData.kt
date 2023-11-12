package com.saeeed.devejump.project.tailoring.domain.model

import com.saeeed.devejump.project.tailoring.network.model.UserComments

data class UserData (
    var id: Int,
    var likes:  List<String> = listOf(),
    var bookMarks:   List<String> = listOf(),
    var comments:   List<UserComments> = listOf()
)
