package com.saeeed.devejump.project.tailoring.presentation.ui.UpdatePost

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.saeeed.devejump.project.tailoring.domain.model.OnUpdatePost
import com.saeeed.devejump.project.tailoring.domain.model.OnUpdateProduct
import com.saeeed.devejump.project.tailoring.domain.model.OnUploadPost
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.domain.model.Product
import com.saeeed.devejump.project.tailoring.interactor.description.GetPost
import com.saeeed.devejump.project.tailoring.interactor.upload_post.UploadPostFunctions
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
@HiltViewModel
class UpdatePostViewModel

@Inject
constructor(
    private val userPreferencesDataStore: DataStore<Preferences>,
    private val connectivityManager: ConnectivityManager,
    private val uploadPostFunctions: UploadPostFunctions,
    private val getPost: GetPost
):ViewModel()
{
    val authorToken= mutableStateOf<String?>("")
    val loading = mutableStateOf(false)
    val dialogQueue = DialogQueue()
    val successFulUpdate= mutableStateOf(false)
    val successFulProductUpdate= mutableStateOf(false)
    val post: MutableState<Post?> = mutableStateOf(null)
    val product: MutableState<Product?> = mutableStateOf(null)
    val fileZippingLoading= mutableStateOf(false)
    val zipFilePath= mutableStateOf("")
    val productAttachedFile= mutableStateOf("")
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


    fun zipSelectedFile(uris: List<Uri?>, context: Context){
        val outputZipPath=createFolder()
        uris.let {
            uploadPostFunctions.zipSelectedFiles(uris,outputZipPath,context).onEach {dataState ->
                fileZippingLoading.value= dataState.loading

                dataState.data?.let {
                    zipFilePath.value=it
                    productAttachedFile.value=it

                    Toast.makeText(context,"compressed successfull", Toast.LENGTH_SHORT).show()
                }
                dataState.error?.let {
                    dialogQueue.appendErrorMessage("خطا",it)

                }

            }.catch {error->
                dialogQueue.appendErrorMessage("خطا",error.message.toString())


            }
                .launchIn(viewModelScope)
        }
    }

    fun createFolder():String{
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            .toString()

        val file= File(path,"tailoring")
        if(!file.exists()){
            file.mkdirs()
        }
        return file.path+"productst"
    }


    fun getPostFromServerForEdit(
       postId:Int
   ){
       getPost.execute(
           postId=postId,
           isNetworkAvailable = connectivityManager.isNetworkAvailable.value
       ).onEach {dataState ->
       dataState.loading.let {
           loading.value=it
       }
           dataState.data?.let {
               post.value=it
               if(it.haveProduct==1){
                   getProductFromServer(postId)
               }
           }
           dataState.error?.let {
               dialogQueue.appendErrorMessage("خطایی رخ داده است",it)
           }


       }.catch {
           dialogQueue.appendErrorMessage("خطایی رخ داده است",it.message.toString())
           loading.value=false
       }.launchIn(viewModelScope)

   }

     fun getProductFromServer(
         postId: Int
     ){
         getPost.getProductOfCurrentPost(
             postId =postId,
             isNetworkAvailable = connectivityManager.isNetworkAvailable.value
         ).onEach {dataState ->
         dataState.loading.let {
             loading.value=it
         }
             dataState.data?.let {
                 product.value=it
             }
             dataState.error?.let {
                 dialogQueue.appendErrorMessage("خطایی رخ داده است",it)
             }
         }.catch {
             dialogQueue.appendErrorMessage("خطایی رخ داده است",it.message.toString())
             loading.value=false
         }.launchIn(viewModelScope)

     }

    fun updatePost(postId:Int,post: OnUpdatePost){
        viewModelScope.launch {
            authorToken.value=getTokenFromPreferencesStore()
        }
        uploadPostFunctions.UpdatePost(
            token = authorToken.value,
            postId=postId,
            isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
            post = post
        ).onEach {dataState ->
            dataState.loading.let {
                loading.value=it
            }
            dataState.data?.let {
                successFulUpdate.value=true
            }
            dataState.error?.let {
                dialogQueue.appendErrorMessage("خطایی رخ داده است",it)
            }

        }.catch {
            dialogQueue.appendErrorMessage("خطایی رخ داده است",it.message.toString())

        }.launchIn(viewModelScope)



    }

    fun updateProduct(productId:Int,product:OnUpdateProduct){
        viewModelScope.launch {
            authorToken.value=getTokenFromPreferencesStore()
        }
        uploadPostFunctions.UpdateProduct(
            token = authorToken.value,
            productId=productId,
            isNetworkAvailable = connectivityManager.isNetworkAvailable.value,
            product = product
        ).onEach {dataState ->
            dataState.loading.let {
                loading.value=it
            }
            dataState.data?.let {
                successFulProductUpdate.value=true
            }
            dataState.error?.let {
                dialogQueue.appendErrorMessage("خطایی رخ داده است",it)
            }

        }.catch {
            dialogQueue.appendErrorMessage("خطایی رخ داده است",it.message.toString())

        }.launchIn(viewModelScope)

    }
    suspend fun getTokenFromPreferencesStore():String {
        val dataStoreKey= stringPreferencesKey("user_token")
        return try{
            val preferences=userPreferencesDataStore.data.first()
            if(preferences[dataStoreKey]==null){
                ""
            }else
                "Bearer ${preferences[dataStoreKey]}"
        }catch (e:NoSuchElementException){
            ""
        }

    }
    fun jsonStringOfProduct(product: Product): String {
        var gson = Gson()
        return gson.toJson(product)
    }
}