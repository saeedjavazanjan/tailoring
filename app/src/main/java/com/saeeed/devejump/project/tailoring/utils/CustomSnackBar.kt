package com.saeeed.devejump.project.tailoring.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import com.saeeed.devejump.project.tailoring.components.GenericDialogInfo
import com.saeeed.devejump.project.tailoring.presentation.components.CustomSnackBarInfo
import java.util.LinkedList
import java.util.Queue

class CustomSnackBar {
    var customSnackBarInfo:CustomSnackBarInfo?=null
    fun show(text:String){
       customSnackBarInfo= CustomSnackBarInfo.Builder()
            .text(text = text)
            .onDismiss {  }
            .build()
    }
}