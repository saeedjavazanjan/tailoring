package com.saeeed.devejump.project.tailoring.presentation.ui.upload

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.interactor.upload_post.UploadPostFunctions
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UploadPostViewModel
    @Inject constructor(
       private val uploadPostFunctions: UploadPostFunctions

):ViewModel() {

    val loading = mutableStateOf(false)
    val dialogQueue = DialogQueue()


    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

    fun zipSelectedFile(uris: List<Uri?>){

        val listOfPaths= mutableListOf<String>()
        uris.let {
            it.forEach {
                listOfPaths.add(it!!.path!!)
            }
            uploadPostFunctions.zipSelectedFiles(listOfPaths).onEach {dataState ->
               loading.value= dataState.loading

                dataState.data?.let {

                }

            }.catch {error->
                dialogQueue.appendErrorMessage("خطا",error.message.toString())
            }
                .launchIn(viewModelScope)
        }
    }

}