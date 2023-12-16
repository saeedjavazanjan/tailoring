package com.saeeed.devejump.project.tailoring.presentation.ui.upload

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UploadPostViewModel
    @Inject constructor(

):ViewModel() {

    val loading = mutableStateOf(false)
    val dialogQueue = DialogQueue()

}