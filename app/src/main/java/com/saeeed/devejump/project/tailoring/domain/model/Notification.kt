package com.saeeed.devejump.project.tailoring.domain.model

import android.icu.text.CaseMap.Title

data class Notification(
    val id:Int,
    val date:String,
    val title: String,
    val text:String
)