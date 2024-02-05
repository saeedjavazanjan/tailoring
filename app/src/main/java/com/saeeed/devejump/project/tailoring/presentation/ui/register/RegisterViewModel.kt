package com.saeeed.devejump.project.tailoring.presentation.ui.register

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.interactor.register.RegisterUser
import com.saeeed.devejump.project.tailoring.presentation.components.SnackbarController
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
    @Inject constructor(
       private val registerUser: RegisterUser
    ) :ViewModel() {
    val loading = mutableStateOf(false)
    val dialogQueue= DialogQueue()


@OptIn(ExperimentalMaterialApi::class)
fun registerPasswordRequest(userName:String, phoneNumber:String, scaffoldState: ScaffoldState, scope: CoroutineScope){
    val snackbarController= SnackbarController(scope)

    registerUser.registerPasswordRequest(
        userName=userName,
        phoneNumber
    ).onEach { dataState ->

    dataState.loading.let {
        loading.value=it
    }
    dataState.data?.let {
        snackbarController.getScope().launch {
            snackbarController.showSnackbar(
                scaffoldState = scaffoldState,
                message = it,
                actionLabel = "Ok"
            )
        }
    }
    dataState.error?.let {
        snackbarController.getScope().launch {
            snackbarController.showSnackbar(
                scaffoldState = scaffoldState,
                message = it,
                actionLabel = "Ok"
            )
        }

    }



    }.catch {
        dialogQueue.appendErrorMessage("An Error Occurred", it.message.toString())

    }.launchIn(viewModelScope)

}

    @OptIn(ExperimentalMaterialApi::class)
    fun loginPasswordRequest(phoneNumber: String, scaffoldState: ScaffoldState, scope:CoroutineScope){
        val snackbarController= SnackbarController(scope)

        registerUser.loginPasswordRequest(
            userName="userName",
            phoneNumber=phoneNumber
        ).onEach { dataState ->

            dataState.loading.let {
                loading.value=it
            }
            dataState.data?.let {
                snackbarController.getScope().launch {
                    snackbarController.showSnackbar(
                        scaffoldState = scaffoldState,
                        message = it?:"ارسال موفق",
                        actionLabel = "Ok"
                    )
                }
            }
            dataState.error?.let {
                snackbarController.getScope().launch {
                    snackbarController.showSnackbar(
                        scaffoldState = scaffoldState,
                        message = it?:"خطای ارسال",
                        actionLabel = "Ok"
                    )
                }

            }



        }.catch {
            dialogQueue.appendErrorMessage("An Error Occurred", it.message.toString())

        }.launchIn(viewModelScope)



    }



}