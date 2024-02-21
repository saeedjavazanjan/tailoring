package com.saeeed.devejump.project.tailoring.presentation.ui.splash

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.interactor.Splash.GetUserStuffsFromServer
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import com.saeeed.devejump.project.tailoring.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@SuppressLint("SuspiciousIndentation")
@HiltViewModel
class SplashViewModel

    @Inject constructor(
        private val getUserData: GetUserStuffsFromServer,
        private val userPreferencesDataStore: DataStore<Preferences>


    ):ViewModel() {
    val dialogQueue = DialogQueue()
    val loading = mutableStateOf(false)
    val dataLoadedSuccessfully= mutableStateOf(false)
    val authorToken= mutableStateOf<String?>("")

    init {
     /*   viewModelScope.launch {
            authorToken.value=getTokenFromPreferencesStore()
        }
        if (authorToken.value!="" )
        getUserData()*/

    }
    fun getUserData(){
            getUserData.execute(authorToken.value!!).onEach {dataState->
                loading.value = dataState.loading
                Log.d(TAG,"splash loading")

                dataState.data?.let {
                    dataLoadedSuccessfully.value=true
                    Log.d(TAG,"splash success")
                }
                dataState.error?.let { error ->
                    dataLoadedSuccessfully.value=false
                    dialogQueue.appendErrorMessage("An Error Occurred", error)
                    Log.e(TAG,"splash error${error}")

                }

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

}