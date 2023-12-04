package com.saeeed.devejump.project.tailoring.presentation.ui.user_profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.R
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.interactor.sew_list.RestoreSewMethods
import com.saeeed.devejump.project.tailoring.interactor.user_profile.GetUserProfileData
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import com.saeeed.devejump.project.tailoring.utils.USERID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named
@HiltViewModel
class UserProfileViewModel
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
    lateinit var user:UserData
    val userPosts:MutableState<List<SewMethod>> = mutableStateOf(ArrayList())






    init {
    getUserData()

    }




    fun getUserData(){
        getUserProfileData.getUserData().onEach { dataState->
            dataState.data.let{
                user=it!!

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
            }
        }/*.catch {

            dialogQueue.appendErrorMessage("مشکلی رخ داده است.",it.message.toString())

        }*/
            .launchIn(viewModelScope)

    }



}