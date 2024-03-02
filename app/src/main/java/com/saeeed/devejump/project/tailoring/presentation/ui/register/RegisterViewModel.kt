package com.saeeed.devejump.project.tailoring.presentation.ui.register

import android.os.CountDownTimer
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.interactor.register.RegisterUser
import com.saeeed.devejump.project.tailoring.presentation.components.SnackbarController
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
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
    private val registerUser: RegisterUser,
    private val userPreferencesDataStore: DataStore<Preferences>,
    private val connectivityManager: ConnectivityManager,


    ) : ViewModel() {
    private val USER_TOKEN = stringPreferencesKey("user_token")
    private val USER_NAME = stringPreferencesKey("user_name")
    private val USER_ID= intPreferencesKey("user_id")

    val loading = mutableStateOf(false)
    val dialogQueue = DialogQueue()
    val stateOfLoginPasswordRequest = mutableStateOf(false)
    val stateOfRegisterPasswordRequest = mutableStateOf(false)
    val retry= mutableStateOf(true)
    val sendSmsText= mutableStateOf("ارسال رمز")

    val loginState= mutableStateOf(false)

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
            phoneNumber,
            isNetworkAvailable =   connectivityManager.isNetworkAvailable.value
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
            phoneNumber = phoneNumber,
            isNetworkAvailable =   connectivityManager.isNetworkAvailable.value

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
           password=password,
            phoneNumber = phoneNumber,
          isNetworkAvailable =   connectivityManager.isNetworkAvailable.value

        ).onEach { dataState ->
            dataState.loading.let {
                loading.value=it
            }
            dataState.data?.let {response->
                saveUserToPreferencesStore(
                    usertoken = response.token!!,
                    userId = response.userData!!.userId,
                    userName = response.userData.userName
                )
                loginState.value=true
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
            password=password,
            phoneNumber = phoneNumber,
            userName =userName,
            isNetworkAvailable =   connectivityManager.isNetworkAvailable.value
        ).onEach { dataState ->
            dataState.loading.let {
                loading.value=it
            }
            dataState.data?.let {response->
                saveUserToPreferencesStore(
                    usertoken = response.token!!,
                    userId = response.userData!!.userId,
                    userName=response.userData.userName
                )
                loginState.value=true
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

   /* suspend fun getUserFromPreferencesStore() {
        val dataStoreKey= intPreferencesKey("user_id")
        val preferences=userPreferencesDataStore.data.first()
        authorId.value= preferences[dataStoreKey]
    }*/

    suspend fun saveUserToPreferencesStore(usertoken:String,userId:Int,userName:String) {
        userPreferencesDataStore.edit { preferences ->
            preferences[USER_TOKEN] = usertoken
            preferences[USER_NAME]=userName
            preferences[USER_ID]=userId
        }
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