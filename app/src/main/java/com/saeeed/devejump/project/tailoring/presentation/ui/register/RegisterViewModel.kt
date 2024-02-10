package com.saeeed.devejump.project.tailoring.presentation.ui.register

import android.os.Build
import android.os.CountDownTimer
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject constructor(
    private val registerUser: RegisterUser
) : ViewModel() {
    val loading = mutableStateOf(false)
    val dialogQueue = DialogQueue()
    val stateOfLoginPasswordRequest = mutableStateOf(false)
    val stateOfRegisterPasswordRequest = mutableStateOf(false)
    val retry= mutableStateOf(true)
    val sendSmsText= mutableStateOf("ارسال رمز")

    val successSmsSenState= mutableStateOf(false)
    @OptIn(ExperimentalMaterialApi::class)
    fun registerPasswordRequest(
        userName: String,
        phoneNumber: String,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope
    ) {

        val snackbarController = SnackbarController(scope)

        registerUser.registerPasswordRequest(
            userName = userName,
            phoneNumber
        ).onEach { dataState ->

            dataState.loading.let {
                loading.value = it
            }
            dataState.data?.let {
                successSmsSenState.value=true
                countDown()
                stateOfLoginPasswordRequest.value = true
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
                        message =  it,
                        actionLabel = "Ok"
                    )
                }

            }


        }.catch {
            dialogQueue.appendErrorMessage("An Error Occurred", it.message.toString())

        }.launchIn(viewModelScope)

    }

    @OptIn(ExperimentalMaterialApi::class)
    fun loginPasswordRequest(
        phoneNumber: String,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope
    ) {
        val snackbarController = SnackbarController(scope)

        registerUser.loginPasswordRequest(
            userName = "userName",
            phoneNumber = phoneNumber
        ).onEach { dataState ->

            dataState.loading.let {
                loading.value = it
            }
            dataState.data?.let {
                successSmsSenState.value=true
                countDown()
                stateOfLoginPasswordRequest.value = true
                snackbarController.getScope().launch {
                    snackbarController.showSnackbar(
                        scaffoldState = scaffoldState,
                        message = it ?: "ارسال موفق",
                        actionLabel = "Ok"
                    )
                }
            }
            dataState.error?.let {

                snackbarController.getScope().launch {
                    snackbarController.showSnackbar(
                        scaffoldState = scaffoldState,
                        message =  it ,
                        actionLabel = "Ok"
                    )
                }

            }


        }.catch {
            dialogQueue.appendErrorMessage("An Error Occurred", it.message.toString())

        }.launchIn(viewModelScope)


    }

    @OptIn(ExperimentalMaterialApi::class)
    fun loginPasswordCheck(
        phoneNumber: String,
        password:String,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope
    ){
        val snackbarController = SnackbarController(scope)
        registerUser.loginPasswordCheck(
           password=password, phoneNumber = phoneNumber
        ).onEach { dataState ->
            dataState.loading.let {
                loading.value=it
            }
            dataState.data?.let {
                snackbarController.getScope().launch {
                    snackbarController.showSnackbar(
                        scaffoldState = scaffoldState,
                        message = "ورود موفق" ,
                        actionLabel = "Ok"
                    )
                }

            }
            dataState.error?.let {
                snackbarController.getScope().launch {
                    snackbarController.showSnackbar(
                        scaffoldState = scaffoldState,
                        message =  it ,
                        actionLabel = "Ok"
                    )
                }
            }

        }.catch {
            dialogQueue.appendErrorMessage("An Error Occurred", it.message.toString())

        }.launchIn(viewModelScope)

    }

    @OptIn(ExperimentalMaterialApi::class)
    fun registerPasswordCheck(
        phoneNumber: String,
        password:String,
        userName: String,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope
    ){
        val snackbarController = SnackbarController(scope)
        registerUser.registerPasswordCheck(
            password=password, phoneNumber = phoneNumber, userName =userName
        ).onEach { dataState ->
            dataState.loading.let {
                loading.value=it
            }
            dataState.data?.let {
                snackbarController.getScope().launch {
                    snackbarController.showSnackbar(
                        scaffoldState = scaffoldState,
                        message = "ورود موفق" ,
                        actionLabel = "Ok"
                    )
                }

            }
            dataState.error?.let {
                snackbarController.getScope().launch {
                    snackbarController.showSnackbar(
                        scaffoldState = scaffoldState,
                        message =  it ,
                        actionLabel = "Ok"
                    )
                }
            }

        }.catch {
            dialogQueue.appendErrorMessage("An Error Occurred", it.message.toString())

        }.launchIn(viewModelScope)

    }



    fun countDown(){
        retry.value=false
        val timer=object :CountDownTimer(10000,1000){
            override fun onTick(millisUntilFinished: Long) {
                val leftTime=millisUntilFinished/1000
                sendSmsText.value= "بعد از $leftTime ثانیه تلاش کنید"
            }
            override fun onFinish(){
                sendSmsText.value="ارسال رمز"
                retry.value=true
            }

        }
        timer.start()
    }

}