package com.saeeed.devejump.project.tailoring.presentation.ui.upload

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.ContactsContract.RawContacts.Data
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.saeeed.devejump.project.tailoring.domain.model.CreatedPost
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.domain.model.Product
import com.saeeed.devejump.project.tailoring.interactor.description.GetPost
import com.saeeed.devejump.project.tailoring.interactor.upload_post.UploadPostFunctions
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class UploadPostViewModel
    @Inject constructor(
        private val uploadPostFunctions: UploadPostFunctions,
        private val userPreferencesDataStore: DataStore<Preferences>,
        private val connectivityManager: ConnectivityManager,
        private val getPost: GetPost

        ):ViewModel() {

    val authorToken= mutableStateOf<String?>("")
    val loading = mutableStateOf(false)
    val fileZippingLoading= mutableStateOf(false)
    val dialogQueue = DialogQueue()
    val successFulUpload= mutableStateOf(false)

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

    val onEditPost= mutableStateOf( Post(
        id = 0,
        postType = "",
        description = "",
        longDataAdded = 0,
        haveProduct = 0,
        authorId = 0,
        category = "",
        title = "",
        dateAdded = "",
        videoUrl = "",
        publisher = "",
        like =0,
        authorAvatar = ""
    ))

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

    fun zipSelectedFile(uris: List<Uri?>,context: Context){
        val outputZipPath=createFolder()
        uris.let {
            uploadPostFunctions.zipSelectedFiles(uris,outputZipPath,context).onEach {dataState ->
               fileZippingLoading.value= dataState.loading

                dataState.data?.let {
                    zipFilePath.value=it
                    productAttachedFile.value=it

                    Toast.makeText(context,"compressed successfull",Toast.LENGTH_SHORT).show()
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
                onEditPost.value=it
            }
            dataState.error?.let {
                dialogQueue.appendErrorMessage("خطایی رخ داده است",it)
            }


        }.catch {
            dialogQueue.appendErrorMessage("خطایی رخ داده است",it.message.toString())
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

        }.launchIn(viewModelScope)

    }
    fun uploadPost(post:CreatedPost){
        viewModelScope.launch {
            authorToken.value=getTokenFromPreferencesStore()
        }
        uploadPostFunctions.uploadPost(
            token =authorToken.value,
            post=post,
            isNetworkAvailable =   connectivityManager.isNetworkAvailable.value

        ).onEach {dataState->
            dataState.loading.let {
                loading.value=it
            }
            dataState.data?.let {
                if (it.haveProduct==1 && product.value!!.name !=""){
                    uploadProduct(it.id)
                }else{
                    successFulUpload.value=true
                }


            }
            dataState.error?.let {
                dialogQueue.appendErrorMessage("مشکلی رخ داده است.",it)
            }

        }.catch {
            dialogQueue.appendErrorMessage("مشکلی رخ داده است.",it.message.toString())

        }.launchIn(viewModelScope)


    }


    fun uploadProduct(
        postId:Int
    ){
        uploadPostFunctions.uploadProduct(
            authorToken.value,
            product.value!!,
            postId
        ).onEach { dataState ->

        dataState.loading.let {
            loading.value=it
        }
        dataState.data?.let {
            successFulUpload.value=true


        }
        dataState.error?.let {
            dialogQueue.appendErrorMessage("مشکلی رخ داده است.",it)

        }

        }.catch {
            dialogQueue.appendErrorMessage("مشکلی رخ داده است.",it.message.toString())

        }.launchIn(viewModelScope)


    }


    fun createFolder():String{
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            .toString()

        val file=File(path,"tailoring")
        if(!file.exists()){
            file.mkdirs()
        }
        return file.path+"productst"
    }
    fun jsonStringOfProduct(product: Product): String {
        var gson = Gson()
        return gson.toJson(product)
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
}