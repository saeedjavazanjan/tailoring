package com.saeeed.devejump.project.tailoring.presentation.ui.author_profile

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.interactor.sew_list.RestoreSewMethods
import com.saeeed.devejump.project.tailoring.interactor.user_profile.GetUserProfileData
import com.saeeed.devejump.project.tailoring.presentation.components.SnackbarController
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import com.saeeed.devejump.project.tailoring.utils.USERID
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Named
@HiltViewModel
class AuthorProfileViewModel
@Inject
constructor(
    private val getUserProfileData: GetUserProfileData,
    private val restoreSewMethods: RestoreSewMethods,
    private val connectivityManager: ConnectivityManager,
    @Named("auth_token") private val token: String,
    private val savedStateHandle: SavedStateHandle,
    private val userPreferencesDataStore: DataStore<Preferences>

    ) : ViewModel() {

    val authorToken= mutableStateOf<String?>("")
    val loading = mutableStateOf(false)
    val dialogQueue = DialogQueue()
    val user:MutableState<UserData?> = mutableStateOf(
        UserData(
            userId = 0,
            userName = "خیاط باشی",
            phoneNumber = "",
            avatar = "",
            following = 0,
            followers = 0,
            likes = emptyList(),
            bookMarks = emptyList(),
            bio = ""
        )

    )
    val userPosts:MutableState<List<Post>> = mutableStateOf(ArrayList())
    val bookMarkedPosts:MutableState<List<Post>> = mutableStateOf(ArrayList())
    init {
    //getUserData()

    }


    fun getUserData(){
        getUserProfileData.getAuthorData(
        ).onEach { dataState->
            dataState.data?.let{
                user.value=it
                Log.i("AVATAR",it.avatar)
            }

        }.catch {
            dialogQueue.appendErrorMessage("مشکلی رخ داده است.",it.message.toString())

        }
            .launchIn(viewModelScope)

    }

    fun getUserPosts(){
        getUserProfileData.getUserPosts(
            token = token,
            userId = USERID,
            isNetworkAvailable = connectivityManager.isNetworkAvailable.value
        ).onEach { dataState->
            dataState.loading.let {
                loading.value=it
            }

            dataState.data?.let{
                userPosts.value =it
                getUserBookMarkedPosts()
            }
            dataState.error?.let {
                dialogQueue.appendErrorMessage("مشکلی رخ داده است.",it.toString())

            }
        }.catch {

            dialogQueue.appendErrorMessage("مشکلی رخ داده است.",it.message.toString())

        }
            .launchIn(viewModelScope)

    }

    fun getUserBookMarkedPosts(){
        getUserProfileData.getUserBookMarkedPosts(
            token = token,
            userId = USERID,
            isNetworkAvailable = connectivityManager.isNetworkAvailable.value
        ).onEach { dataState->
            dataState.loading.let {
                loading.value=it
            }

            dataState.data?.let{
                bookMarkedPosts.value =it
            }
            dataState.error?.let {
                dialogQueue.appendErrorMessage("مشکلی رخ داده است.",it.toString())

            }
        }.catch {

            dialogQueue.appendErrorMessage("مشکلی رخ داده است.",it.message.toString())

        }
            .launchIn(viewModelScope)

    }
    @OptIn(ExperimentalMaterialApi::class)
    fun updateUserData(
        userData: UserData?,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope,
        avatarUri: Uri
    ){
        val snackbarController= SnackbarController(scope)

        viewModelScope.launch {
          authorToken.value=getTokenFromPreferencesStore()
        }
        getUserProfileData.updateUserData(
            userData,
            authorToken.value,
            avatarUri
        ).onEach {dataState ->
            dataState.loading.let {
                loading.value=it
            }

            dataState.data?.let {

                snackbarController.getScope().launch {
                        snackbarController.showSnackbar(
                            scaffoldState = scaffoldState,
                            message = "با موفقیت به روز رسانی  شد.",
                            actionLabel = "Ok"
                        )
                }
            }

            dataState.error?.let {
                dialogQueue.appendErrorMessage("مشکلی رخ داده است.",it.toString())

            }

        }.catch {
            dialogQueue.appendErrorMessage("مشکلی رخ داده است.",it.toString())

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

    fun getResourceUri(resources: Resources, resourceID: Int): Uri? {
        return Uri.parse(
            "android.resource://" + resources.getResourcePackageName(resourceID) + "/" +
                    resources.getResourceTypeName(resourceID) + '/'
                    + resources.getResourceEntryName(resourceID)
        )
    }

}