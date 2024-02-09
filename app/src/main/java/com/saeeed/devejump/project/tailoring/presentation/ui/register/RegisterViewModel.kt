package com.saeeed.devejump.project.tailoring.presentation.ui.register

import android.os.Build
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
    val smsSendTime= mutableStateOf(0L)
    val retry= mutableStateOf(true)

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
                smsSendTime.value=System.currentTimeMillis()
                stateOfRegisterPasswordRequest.value = true
                snackbarController.getScope().launch {
                    snackbarController.showSnackbar(
                        scaffoldState = scaffoldState,
                        message = it,
                        actionLabel = "Ok"
                    )
                }
            }
            dataState.error?.let {
              // val leftTime=60- ((System.currentTimeMillis()-smsSendTime.value)/1000)
                snackbarController.getScope().launch {
                    snackbarController.showSnackbar(
                        scaffoldState = scaffoldState,
                        message = /*if (it.equals("Too many Request")) "بعد از $leftTime ثانیه تلاش کنید. " else*/ it,
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
                //smsSendTime.value=System.currentTimeMillis()
                countDownFlow(
                    System.currentTimeMillis()
                ).onEach {
                    smsSendTime.value=it
                }.launchIn(viewModelScope)
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

              //  val leftTime=30- ((System.currentTimeMillis()-smsSendTime.value)/1000)
                snackbarController.getScope().launch {
                    snackbarController.showSnackbar(
                        scaffoldState = scaffoldState,
                        message = /*if (it.equals("Too many Request")) "بعد از $leftTime ثانیه تلاش کنید. " else*/ it ,
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

    private fun countDownFlow(
        start: Long,
        delayInSeconds: Long = 1_000L,
    ): Flow<Long> = flow {
        var count = start
        while  (count >= 0L) {
            emit(count--)
            delay(delayInSeconds)
        }

    }

    fun sendSmsButtonText():String{
        var result="ارسال رمز"
        if(successSmsSenState.value){
            val leftTime=10- ((System.currentTimeMillis()-smsSendTime.value)/1000)
            if (leftTime>=0){
                retry.value=false
              result=  "بعد از $leftTime ثانیه تلاش کنید"
            }else{
                retry.value=true

            }
        }
        return result
    }
}