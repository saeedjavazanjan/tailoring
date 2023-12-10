package com.saeeed.devejump.project.tailoring.presentation.ui.splash

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.interactor.Splash.GetUserStuffsFromServer
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import com.saeeed.devejump.project.tailoring.utils.TAG
import com.saeeed.devejump.project.tailoring.utils.USERID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SplashViewModel

    @Inject constructor(
        private val getUserData: GetUserStuffsFromServer

):ViewModel() {
    val dialogQueue = DialogQueue()
    val loading = mutableStateOf(false)
    val loaded= mutableStateOf(false)

    init {
        getUserData(USERID)

    }
    fun getUserData(userId:Int){
            getUserData.execute(userId).onEach {dataState->
                loading.value = dataState.loading
                Log.d(TAG,"splash loading")

                dataState.data?.let {
                    loaded.value=true
                    Log.d(TAG,"splash success")
                }
                dataState.error?.let { error ->
                    dialogQueue.appendErrorMessage("An Error Occurred", error)
                    Log.e(TAG,"splash error${error}")

                }

            }.launchIn(viewModelScope)
        }


}