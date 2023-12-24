package com.saeeed.devejump.project.tailoring.presentation.ui.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.model.Post
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.interactor.sew_list.RestoreSewMethods
import com.saeeed.devejump.project.tailoring.interactor.user_profile.GetUserProfileData
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named
@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    private val getUserProfileData: GetUserProfileData,
    private val restoreSewMethods: RestoreSewMethods,
    private val connectivityManager: ConnectivityManager,
    @Named("auth_token") private val token: String,
    private val savedStateHandle: SavedStateHandle,

    ) : ViewModel() {
    val loading = mutableStateOf(false)
    val dialogQueue = DialogQueue()
    val user:MutableState<UserData?> = mutableStateOf(null)
    val userPosts:MutableState<List<Post>> = mutableStateOf(ArrayList())
    val bookMarkedPosts:MutableState<List<Post>> = mutableStateOf(ArrayList())

    val followingStatus= mutableStateOf(true)




    /*init {
    getUserData()
    }*/


    fun getUserData(userId:Int){
        getUserProfileData.getUserData(
            token = token,
            userId = userId,
            isNetworkAvailable = connectivityManager.isNetworkAvailable.value
        ).onEach { dataState->
            dataState.data?.let{
                user.value=it

            }

        }.catch {
            dialogQueue.appendErrorMessage("مشکلی رخ داده است.",it.message.toString())

        }
            .launchIn(viewModelScope)

    }

    fun getUserPosts(userId:Int){
        getUserProfileData.getUserPosts(
            token = token,
            userId = userId,
            isNetworkAvailable = connectivityManager.isNetworkAvailable.value
        ).onEach { dataState->
            dataState.loading.let {
                loading.value=it
            }

            dataState.data?.let{
                userPosts.value =it
            }
            dataState.error?.let {
                dialogQueue.appendErrorMessage("مشکلی رخ داده است.",it.toString())

            }
        }.catch {

            dialogQueue.appendErrorMessage("مشکلی رخ داده است.",it.message.toString())

        }
            .launchIn(viewModelScope)

    }


    fun stateOfFollowing(userId:Int,token: String){
        getUserProfileData.checkIfUserFollowed(userId,token).onEach { dataState ->

            dataState.data?.let {
                if (it==204){
                    followingStatus.value=true
                }else if (it==404){
                    followingStatus.value=true

                }
            }

        }.catch {
            dialogQueue.appendErrorMessage("خطایی رخ داده است",it.message.toString())

        }

    }

}