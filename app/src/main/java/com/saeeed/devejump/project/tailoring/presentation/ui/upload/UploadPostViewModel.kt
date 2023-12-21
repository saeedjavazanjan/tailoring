package com.saeeed.devejump.project.tailoring.presentation.ui.upload

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.interactor.upload_post.UploadPostFunctions
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import com.saeeed.devejump.project.tailoring.utils.GetPathFromUri
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import javax.inject.Inject


@HiltViewModel
class UploadPostViewModel
    @Inject constructor(
       private val uploadPostFunctions: UploadPostFunctions

):ViewModel() {


    val loading = mutableStateOf(false)
    val dialogQueue = DialogQueue()

    val zipFilePath= mutableStateOf("")
    val digitalFileStatus= mutableStateOf(false)


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

    fun zipSelectedFile(uris: List<Uri?>,context: Context){
        val outputZipPath=createFolder()
        uris.let {
            uploadPostFunctions.zipSelectedFiles(uris,outputZipPath,context).onEach {dataState ->
               loading.value= dataState.loading

                dataState.data?.let {
                    zipFilePath.value=it
                    digitalFileStatus.value=true
                    Toast.makeText(context,"compressed successfull",Toast.LENGTH_SHORT).show()
                }
                dataState.error?.let {
                    dialogQueue.appendErrorMessage("خطا",it)
                    digitalFileStatus.value=false

                }

            }.catch {error->
                dialogQueue.appendErrorMessage("خطا",error.message.toString())
                digitalFileStatus.value=false


            }
                .launchIn(viewModelScope)
        }
    }


    fun createFolder():String{
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            .toString()

        val file=File(path,"tailoring")
        if(!file.exists()){
            file.mkdirs()
        }
        return file.path+"product"
    }

}