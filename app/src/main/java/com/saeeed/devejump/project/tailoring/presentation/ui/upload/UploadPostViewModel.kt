package com.saeeed.devejump.project.tailoring.presentation.ui.upload

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.saeeed.devejump.project.tailoring.domain.model.CreatedPost
import com.saeeed.devejump.project.tailoring.domain.model.Product
import com.saeeed.devejump.project.tailoring.interactor.upload_post.UploadPostFunctions
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val fileZippingLoading= mutableStateOf(false)
    val dialogQueue = DialogQueue()
    val product: MutableState<Product?> =
        mutableStateOf(
            Product(
                id=0,
                name = "",
                description = "",
                typeOfProduct = "محصول فیزیکی",
                unit = "عدد",
                mas = "",
                supply = "",
                price = "",
                postId = 0
    )
        )

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
               fileZippingLoading.value= dataState.loading

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

    fun uploadPostAndProduct(post:CreatedPost){



    }
    fun createFolder():String{
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            .toString()

        val file=File(path,"tailoring")
        if(!file.exists()){
            file.mkdirs()
        }
        return file.path+"products"
    }
    fun jsonStringOfProduct(product: Product): String {
        var gson = Gson()
        return gson.toJson(product)
    }
}